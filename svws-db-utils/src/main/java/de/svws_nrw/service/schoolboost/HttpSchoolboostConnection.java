package de.svws_nrw.service.schoolboost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.oauth2.OAuth2Token;
import de.svws_nrw.db.dto.current.schoolboost.DTOSchoolboostVerbindungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schoolboost.SchoolboostVerbindungenRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse dient der HTTP-Kommunikation zu einem Schoolboost-Server.
 *
 * Sie folgt dem Muster der Verbindung zu einem Web-Notenmodul-Server (WeNoM):
 * Über einen OAuth2 client_credentials-Flow (Client-ID = Schoolboost-School-ID,
 * Client-Secret = API-Key) wird ein kurzlebiges Token angefordert, welches in
 * der SVWS-DB zwischengespeichert und bei den Requests als Bearer-Token
 * mitgesendet wird. Da der Schoolboost-Server über ein öffentlich gültiges
 * TLS-Zertifikat verfügt, entfällt die TLS-Trust-Logik des WeNoM-Clients.
 */
final class HttpSchoolboostConnection {

	/** Das Repository für den Zugriff auf die Schoolboost-Verbindungen */
	private final SchoolboostVerbindungenRepository repository;

	/** Der zu verwendende Logger */
	private final Logger logger;

	/** Das DTO mit den OAuth2-Informationen */
	private final DTOSchoolboostVerbindungen dto;


	/**
	 * Das Ergebnis eines Pairing-Austauschs mit dem Schoolboost-Server.
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class ExchangeResult {

		/** Der API-Key, welcher gegen den Pairing-Code getauscht wurde */
		public String apiKey;

		/** Die ID der Schule bei Schoolboost */
		public String schoolId;

		/** Die URL der Schoolboost-API */
		public String apiUrl;

	}


	/**
	 * Erzeugt eine neue Verbindung zu einem Schoolboost-Server und erneuert ggf. das aktuelle Token.
	 *
	 * @param repository      das Repository für den Zugriff auf die Schoolboost-Verbindungen
	 * @param logger          ein Logger für das Loggen der Kommunikation
	 * @param id              die ID der Verbindung zu dem Schoolboost-Server
	 * @param updateToken     gibt an, ob das Token überprüft und ggf. erneuert werden soll
	 * @param forceNewToken   gibt an, ob das Token bei einer Prüfung immer erneuert werden soll
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	HttpSchoolboostConnection(final SchoolboostVerbindungenRepository repository, final Logger logger, final long id, final boolean updateToken,
			final boolean forceNewToken) throws ApiOperationException {
		this.repository = repository;
		this.logger = logger;

		// Lese die Verbindungsdaten aus der Datenbank ein.
		logger.logLn("Lese die Verbindung mit der ID %d aus der Datenbank...".formatted(id));
		this.dto = repository.findById(id).orElseThrow(
				() -> new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Verbindung mit der ID %d gefunden.".formatted(id)));
		if ((dto.appUrl == null) || dto.appUrl.isBlank()) {
			throw new ApiOperationException(Status.NOT_FOUND, "Bei der Verbindung wurde keine Schoolboost-URL angegeben.");
		}
		if (updateToken) {
			if ((dto.schoolId == null) || dto.schoolId.isBlank() || (dto.apiKey == null) || dto.apiKey.isBlank()) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Bei der Verbindung ist kein API-Key hinterlegt. Die Verbindung muss zunächst über einen Pairing-Code hergestellt werden.");
			}
			logger.logLn("Generiere den HTTP-Header für Basic-Auth bestehend aus der School-ID als User und dem API-Key als Kennwort...");
			final String basicAuth = Base64.getEncoder().encodeToString((dto.schoolId + ":" + dto.apiKey).getBytes());
			if (forceNewToken) {
				logger.logLn("Ignoriere ein ggf. existierendes Token und fordere ein neues Token an...");
				requestToken(basicAuth, logger);
			} else {
				logger.logLn("Prüfe, ob ein bestehendes Token wiederverwendet werden kann...");
				if (isTokenValid()) {
					logger.logLn("Das Token ist noch gültig und wird erneut verwendet.");
				} else {
					logger.logLn("Es existiert kein gültiges Token und ein neues Token muss angefordert werden...");
					requestToken(basicAuth, logger);
				}
			}
		}
	}


	/**
	 * Gibt wieder, ob ein Token vorhanden ist, welches nicht abgelaufen ist
	 *
	 * @return true, wenn ein nicht abgelaufenes Token vorhanden ist, und ansonsten false
	 */
	boolean isTokenValid() {
		if ((dto.token == null) || (dto.tokenExpiresIn == null) || (dto.tokenTimestamp == null)) {
			return false;
		}
		// Berechne die Zeit in Millisekunden, wann das Token abläuft
		final long tsExpiration = ((dto.tokenExpiresIn * 1000) + dto.tokenTimestamp);
		// Bestimme die aktuelle Zeit zum Vergleich, addiere aber einen Wert darauf, um das Token ggf. früher zu erneuern
		final long tsNow = System.currentTimeMillis() + 60000;
		return (tsNow < tsExpiration);
	}


	/**
	 * Erzeugt auf Basis eines Basic-Auth Strings ein Token und hinterlegt es an diesem Client
	 *
	 * @param basicAuthString   String für die BasicAuth, Base64 encoded "schoolId:apiKey"
	 * @param logger            der zu verwendende Logger
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	void requestToken(final String basicAuthString, final Logger logger) throws ApiOperationException {
		logger.logLn("Erstelle den HTTP-Header für die Token-Anfrage...");
		final URI uri = URI.create(dto.appUrl + "/api/oauth/token");
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofString("grant_type=client_credentials")).header("Content-Type", "application/x-www-form-urlencoded")
				.setHeader("Authorization", "Basic " + basicAuthString).build();
		logger.logLn("Sende die HTTP-Anfrage für ein neues Token...");
		final HttpResponse<String> response = send(request, BodyHandlers.ofString());
		final int statusCode = response.statusCode();
		if (statusCode == 401) {
			throw new SchoolboostApiKeyInvalidException("Verbindung zu dem Schoolboost-Server ergab 401 (Unauthorized)."
					+ " Der API-Key ist ungültig oder wurde widerrufen. Die Verbindung wurde getrennt und muss über einen neuen"
					+ " Pairing-Code neu hergestellt werden.");
		}
		if (statusCode == 500) {
			throw new ApiOperationException(Status.BAD_GATEWAY, "Verbindung zu dem Schoolboost-Server ergab 500 (Internal Server Error).");
		}
		if ((statusCode != 200) && (statusCode != 201)) {
			throw new ApiOperationException(Status.BAD_GATEWAY,
					"Verbindung zu dem Schoolboost-Server mit dem OAuth2-Status-Code %d fehlgeschlagen.".formatted(statusCode));
		}
		final String stringResponse = response.body();
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
			final OAuth2Token token = mapper.readValue(stringResponse, OAuth2Token.class);
			// Das Token wird in einer eigenen Transaktion persistiert, damit es bei den folgenden Anfragen
			// wiederverwendet werden kann. Die Transaktion umfasst bewusst nur den Schreibvorgang und nicht
			// die HTTP-Kommunikation, damit sie so kurz wie möglich offen bleibt.
			transactional(() -> {
				this.dto.tokenTimestamp = System.currentTimeMillis();
				this.dto.token = token.accessToken;
				this.dto.tokenExpiresIn = token.expiresIn;
				repository.update(this.dto);
				repository.flush();
				return null;
			});
			logger.logLn("Das Token wurde erfolgreich empfangen.");
		} catch (@SuppressWarnings("unused") final JsonProcessingException e) {
			throw new ApiOperationException(Status.BAD_GATEWAY, "Fehler in der Antwort des Schoolboost-Servers:\n" + stringResponse);
		}
	}


	/**
	 * Methode zum Versenden eines HTTP-Requests.
	 *
	 * @param <T> generischer Typ der Response
	 * @param request   der zu sendende Request
	 * @param handler   der BodyHandler
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	static <T> HttpResponse<T> send(final HttpRequest request, final BodyHandler<T> handler) throws ApiOperationException {
		try {
			try (HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(20)).build()) {
				return client.send(request, handler);
			}
		} catch (IOException | InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ApiOperationException(Status.BAD_GATEWAY, e, "Fehler beim Senden der Informationen: " + e.getLocalizedMessage());
		}
	}


	/**
	 * Bestimmt die Basis-URL für die Datenübertragung an die Schoolboost-API.
	 *
	 * @return die Basis-URL
	 *
	 * @throws ApiOperationException falls keine API-URL hinterlegt ist
	 */
	private String getApiBaseUrl() throws ApiOperationException {
		if ((dto.apiUrl == null) || dto.apiUrl.isBlank()) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Bei der Verbindung ist keine API-URL hinterlegt. Die Verbindung muss zunächst über einen Pairing-Code hergestellt werden.");
		}
		return dto.apiUrl.endsWith("/") ? dto.apiUrl.substring(0, dto.apiUrl.length() - 1) : dto.apiUrl;
	}


	/**
	 * Sendet GZip-komprimierte JSON-Daten per POST an die Schoolboost-API.
	 *
	 * @param <T>       der generische Typ der {@link HttpResponse} und des entsprechenden {@link BodyHandler}
	 * @param path      der Pfad als Teil der URL, an den der Request gesendet wird
	 * @param bytes     die GZip-komprimierten Daten
	 * @param handler   der BodyHandler für die Response
	 *
	 * @return die Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	<T> HttpResponse<T> postGzip(final String path, final byte[] bytes, final BodyHandler<T> handler) throws ApiOperationException {
		logger.logLn("Bereite die HTTP-Anfrage vor...");
		final URI uri = URI.create(getApiBaseUrl() + path);
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofByteArray(bytes))
				.header("Content-Type", "application/gzip")
				.header("Content-Encoding", "gzip")
				.header("Accept", "*/*")
				.header("Authorization", "Bearer " + dto.token).build();
		logger.logLn("Sende die HTTP-Anfrage...");
		return send(request, handler);
	}


	/**
	 * Tauscht einen Pairing-Code bei dem Schoolboost-Server gegen einen API-Key.
	 *
	 * @param appUrl   die URL der Schoolboost-Anwendung
	 * @param code     der Pairing-Code
	 * @param logger   der zu verwendende Logger
	 *
	 * @return das Ergebnis des Austauschs mit API-Key, School-ID und API-URL
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	static ExchangeResult exchange(final String appUrl, final String code, final Logger logger) throws ApiOperationException {
		logger.logLn("Sende die Anfrage für den Austausch des Pairing-Codes an den Schoolboost-Server...");
		final String baseUrl = appUrl.endsWith("/") ? appUrl.substring(0, appUrl.length() - 1) : appUrl;
		final URI uri = URI.create(baseUrl + "/api/pairing/exchange");
		final HttpRequest request = HttpRequest.newBuilder().uri(uri).timeout(Duration.ofMinutes(2))
				.POST(BodyPublishers.ofString("{\"code\":\"" + code.replace("\"", "") + "\"}"))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json").build();
		final HttpResponse<String> response = send(request, BodyHandlers.ofString());
		final int statusCode = response.statusCode();
		if (statusCode == 400) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Pairing-Code ist ungültig oder abgelaufen. Bitte erzeugen Sie einen neuen Code im Schoolboost-Dashboard.");
		}
		if (statusCode != 200) {
			throw new ApiOperationException(Status.BAD_GATEWAY,
					"Der Austausch des Pairing-Codes ist mit dem Status-Code %d fehlgeschlagen.".formatted(statusCode));
		}
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final ExchangeResult result = mapper.readValue(response.body(), ExchangeResult.class);
			if ((result.apiKey == null) || result.apiKey.isBlank() || (result.schoolId == null) || result.schoolId.isBlank()) {
				throw new ApiOperationException(Status.BAD_GATEWAY, "Die Antwort des Schoolboost-Servers ist unvollständig.");
			}
			logger.logLn("Der Pairing-Code wurde erfolgreich gegen einen API-Key getauscht.");
			return result;
		} catch (final JsonProcessingException e) {
			throw new ApiOperationException(Status.BAD_GATEWAY, e, "Fehler in der Antwort des Schoolboost-Servers.");
		}
	}

}
