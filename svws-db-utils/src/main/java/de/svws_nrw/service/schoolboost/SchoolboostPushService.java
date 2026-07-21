package de.svws_nrw.service.schoolboost;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.function.Consumer;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schoolboost.SchoolboostDaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.schoolboost.DTOSchoolboostVerbindungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schoolboost.SchoolboostVerbindungenRepository;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

/**
 * Service für die Synchronisation der Schuldaten mit einem Schoolboost-Server.
 *
 * Der Service folgt dem Muster der Notenmodul-Synchronisation (WeNoM): Die
 * Daten werden aggregiert, GZip-komprimiert und per HTTP-POST an den
 * Schoolboost-Server übertragen. Das Ergebnis wird als
 * {@link SimpleOperationResponse} mit einem Log zurückgegeben.
 */
public final class SchoolboostPushService {

	private final SchoolboostVerbindungenRepository repository;
	private final SchoolboostVerbindungenService verbindungenService;
	private final SchoolboostGetService getService;

	/**
	 * Erstellt einen neuen Service für die Synchronisation mit einem Schoolboost-Server
	 *
	 * @param repository            das Repository für den Zugriff auf die Schoolboost-Verbindungen
	 * @param verbindungenService   der Service für die Verwaltung der Schoolboost-Verbindung
	 * @param getService            der Service zum Aggregieren der zu übertragenden Daten
	 */
	public SchoolboostPushService(final SchoolboostVerbindungenRepository repository, final SchoolboostVerbindungenService verbindungenService,
			final SchoolboostGetService getService) {
		this.repository = repository;
		this.verbindungenService = verbindungenService;
		this.getService = getService;
	}


	/**
	 * Führt die übergebene Operation mit einem Log aus und gibt das Ergebnis der Operation in
	 * einer {@link SimpleOperationResponse} zurück.
	 *
	 * @param opName     die Bezeichnung der Operation für das Logging
	 * @param runnable   die Operation
	 *
	 * @return die {@link SimpleOperationResponse} mit dem Ergebnis
	 */
	private static SimpleOperationResponse executeWithLog(final String opName, final Consumer<Logger> runnable) {
		// Erstelle zunächst einen Logger für die Operation
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);

		// Erstelle eine SimpleOperationResponse und fülle diese mit dem Ergebnis der Operation
		final var sor = new SimpleOperationResponse();
		Status status = Status.OK;
		try {
			logger.logLn("Starte " + opName + "...");
			logger.modifyIndent(2);
			runnable.accept(logger);
			logger.logLn(opName + " erfolgreich abgeschlossen.");
			sor.success = true;
			logger.setIndent(0);
			sor.log = log.getStrings();
			return sor;
		} catch (final Exception e) {
			status = Status.INTERNAL_SERVER_ERROR;
			if (e instanceof final ApiOperationException aoe) {
				status = aoe.getStatus();
			}
			logger.logLn("Fehler bei " + opName + ": " + e.getMessage());
			sor.success = false;
			logger.setIndent(0);
			sor.log = log.getStrings();
			throw new ApiOperationException(status, e, sor, MediaType.APPLICATION_JSON);
		}
	}


	/**
	 * Überträgt die Schuldaten an den Schoolboost-Server.
	 *
	 * @param client       die Verbindung zu dem Schoolboost-Server
	 * @param verbindung   die Daten der Schoolboost-Verbindung
	 * @param logger       der Logger
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void uploadDaten(final HttpSchoolboostConnection client, final DTOSchoolboostVerbindungen verbindung, final Logger logger)
			throws ApiOperationException {
		try {
			logger.logLn("Bestimme die Daten aus der Datenbank des SVWS-Servers...");
			final SchoolboostDaten schoolboostDaten = getService.get(verbindung);
			logger.logLn("Es werden %d Schüler, %d Klassen und %d Lehrkräfte übertragen.".formatted(
					schoolboostDaten.students.size(), schoolboostDaten.classes.size(), schoolboostDaten.teachers.size()));
			final byte[] daten = JSONMapper.gzipByteArrayFromObject(schoolboostDaten);

			logger.logLn("Sende die Daten an den Schoolboost-Server...");
			logger.modifyIndent(2);
			final HttpResponse<String> response = client.postGzip("/v1/schools/" + verbindung.schoolId + "/integrations/svws/push/", daten,
					BodyHandlers.ofString());
			logger.modifyIndent(-2);
			final int statusCode = response.statusCode();
			// Ein abgelehntes Token kann durch einen erneuten Versuch mit einem neuen Token behoben werden,
			// alle anderen Fehler des Schoolboost-Servers hingegen nicht.
			if (statusCode == Status.UNAUTHORIZED.getStatusCode()) {
				throw new SchoolboostTokenExpiredException("Der Schoolboost-Server hat das Token abgelehnt (401).");
			}
			if (statusCode != Status.OK.getStatusCode()) {
				throw new ApiOperationException(Status.BAD_GATEWAY,
						"Die Übertragung an den Schoolboost-Server ist mit dem Status-Code %d fehlgeschlagen:%n%s"
								.formatted(statusCode, response.body()));
			}

			logger.logLn("Die Daten wurden erfolgreich an den Schoolboost-Server übertragen.");
		} catch (final CompressionException ce) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ce, "Fehler beim Komprimieren der Daten.");
		}
	}


	/**
	 * Überträgt die Schuldaten an den Schoolboost-Server.
	 *
	 * @return die {@link SimpleOperationResponse} der Operation
	 */
	public SimpleOperationResponse push() {
		return executeWithLog("Synchronisation", logger -> {
			final DTOSchoolboostVerbindungen verbindung = verbindungenService.getDtoOrThrow();
			try {
				HttpSchoolboostConnection client = new HttpSchoolboostConnection(repository, logger, verbindung.id, true, false);
				try {
					uploadDaten(client, verbindung, logger);
				} catch (@SuppressWarnings("unused") final SchoolboostTokenExpiredException e) {
					// Nur ein abgelehntes Token wird einmalig mit einem neuen Token wiederholt
					logger.logLn("Das Token wurde abgelehnt. Erstelle eine Verbindung mit einem neuen Token und versuche es erneut.");
					client = new HttpSchoolboostConnection(repository, logger, verbindung.id, true, true);
					uploadDaten(client, verbindung, logger);
				}
			} catch (final SchoolboostApiKeyInvalidException e) {
				trenneVerbindung(logger);
				throw e;
			}
		});
	}


	/**
	 * Trennt die Verbindung, nachdem der Schoolboost-Server den API-Key abgelehnt hat, damit der Status der
	 * Verbindung den tatsächlichen Gegebenheiten entspricht und zum erneuten Pairing aufgefordert wird.
	 *
	 * @param logger   der zu verwendende Logger
	 */
	private void trenneVerbindung(final Logger logger) {
		logger.logLn("Der API-Key wurde von dem Schoolboost-Server abgelehnt. Die Verbindung wird getrennt...");
		verbindungenService.disconnect();
	}


	/**
	 * Prüft, ob der Schoolboost-Server mit den hinterlegten Verbindungsdaten erreichbar ist,
	 * indem ein neues Token angefordert wird.
	 *
	 * @return die {@link SimpleOperationResponse} der Operation
	 */
	public SimpleOperationResponse check() {
		return executeWithLog("Verbindungstest", logger -> {
			final DTOSchoolboostVerbindungen verbindung = verbindungenService.getDtoOrThrow();
			try {
				new HttpSchoolboostConnection(repository, logger, verbindung.id, true, true);
			} catch (final SchoolboostApiKeyInvalidException e) {
				trenneVerbindung(logger);
				throw e;
			}
			logger.logLn("Der Schoolboost-Server hat ein gültiges Token ausgestellt. Die Verbindung ist funktionsfähig.");
		});
	}

}
