package de.svws_nrw.service.schoolboost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.net.URI;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.schoolboost.SchoolboostConnection;
import de.svws_nrw.core.data.schoolboost.SchoolboostPairRequest;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.dto.current.schoolboost.DTOSchoolboostVerbindungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schoolboost.SchoolboostVerbindungenRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff auf die Schoolboost-Verbindung.
 *
 * Es wird genau eine Verbindung pro Schule verwaltet: Beim Pairing wird der
 * bestehende Eintrag aktualisiert oder ein neuer Eintrag angelegt.
 */
public final class SchoolboostVerbindungenService {

	/** Das Repository für den Zugriff auf die Schoolboost-Verbindungen */
	private final SchoolboostVerbindungenRepository repository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository   das Repository für den Zugriff auf die Schoolboost-Verbindungen
	 */
	public SchoolboostVerbindungenService(final SchoolboostVerbindungenRepository repository) {
		this.repository = repository;
	}


	private static SchoolboostConnection toApi(final DTOSchoolboostVerbindungen dto) {
		final SchoolboostConnection daten = new SchoolboostConnection();
		daten.id = dto.id;
		daten.bezeichnung = dto.bezeichnung;
		daten.appUrl = (dto.appUrl == null) ? "" : dto.appUrl;
		daten.apiUrl = dto.apiUrl;
		daten.schoolId = dto.schoolId;
		daten.istVerbunden = (dto.apiKey != null) && !dto.apiKey.isBlank();
		daten.syncSchueler = !Boolean.FALSE.equals(dto.syncSchueler);
		daten.syncKlassen = !Boolean.FALSE.equals(dto.syncKlassen);
		daten.syncLehrer = !Boolean.FALSE.equals(dto.syncLehrer);
		return daten;
	}


	/**
	 * Bestimmt das Datenbank-DTO der Verbindung, sofern eine eingerichtet wurde.
	 *
	 * @return das DTO oder null
	 */
	DTOSchoolboostVerbindungen getDto() {
		final List<DTOSchoolboostVerbindungen> list = repository.getAll();
		return list.isEmpty() ? null : list.getFirst();
	}


	/**
	 * Bestimmt das Datenbank-DTO der Verbindung.
	 *
	 * @return das DTO
	 *
	 * @throws ApiOperationException falls keine Verbindung eingerichtet wurde
	 */
	DTOSchoolboostVerbindungen getDtoOrThrow() throws ApiOperationException {
		final DTOSchoolboostVerbindungen dto = getDto();
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurde noch keine Schoolboost-Verbindung eingerichtet. Bitte stellen Sie die Verbindung zunächst über einen Pairing-Code her.");
		}
		return dto;
	}


	/**
	 * Ermittelt die Schoolboost-Verbindung. Wurde noch keine Verbindung
	 * eingerichtet, so wird ein leeres Objekt mit der ID -1 zurückgegeben.
	 *
	 * @return die Schoolboost-Verbindung
	 */
	public SchoolboostConnection get() {
		final DTOSchoolboostVerbindungen dto = getDto();
		return (dto == null) ? new SchoolboostConnection() : toApi(dto);
	}


	private static void validateUrl(final String url) {
		try {
			new URI(url).toURL();
		} catch (final Exception e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Ungültiges URL-Format: " + url);
		}
	}


	/**
	 * Stellt die Verbindung zu einem Schoolboost-Server her, indem der übergebene
	 * Pairing-Code gegen einen API-Key getauscht wird. Ein bestehender Eintrag
	 * wird dabei aktualisiert.
	 *
	 * @param request   die Pairing-Informationen (Code und App-URL)
	 * @param logger    der zu verwendende Logger
	 *
	 * @return die aktualisierte Schoolboost-Verbindung
	 */
	public SchoolboostConnection pair(final SchoolboostPairRequest request, final Logger logger) {
		if ((request.code == null) || request.code.isBlank()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde kein Pairing-Code angegeben.");
		}
		if ((request.appUrl == null) || request.appUrl.isBlank()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde keine Schoolboost-URL angegeben.");
		}
		validateUrl(request.appUrl);

		final String code = request.code.trim().toUpperCase().replaceAll("[\\s-]", "");
		final var result = HttpSchoolboostConnection.exchange(request.appUrl, code, logger);

		return transactional(() -> {
			DTOSchoolboostVerbindungen dto = getDto();
			if (dto == null) {
				dto = new DTOSchoolboostVerbindungen(repository.getNextID(), request.appUrl);
				dto.syncSchueler = true;
				dto.syncKlassen = true;
				dto.syncLehrer = true;
			} else {
				dto.appUrl = request.appUrl;
			}
			if (request.bezeichnung != null) {
				dto.bezeichnung = request.bezeichnung;
			}
			dto.apiUrl = result.apiUrl;
			dto.schoolId = result.schoolId;
			dto.apiKey = result.apiKey;
			// Ein ggf. vorhandenes Token gehört zu dem alten API-Key und wird verworfen
			dto.token = null;
			dto.tokenTimestamp = null;
			dto.tokenExpiresIn = null;
			repository.update(dto);
			repository.flush();
			return toApi(dto);
		});
	}


	/**
	 * Führt einen partiellen Patch auf die Schoolboost-Verbindung aus. Es können
	 * die Bezeichnung sowie die Synchronisations-Optionen angepasst werden.
	 *
	 * @param patch   die Map mit den zu patchenden Attributen
	 *
	 * @return die aktualisierte Schoolboost-Verbindung
	 */
	public SchoolboostConnection patch(final Map<String, Object> patch) {
		return transactional(() -> {
			final DTOSchoolboostVerbindungen dto = getDtoOrThrow();
			for (final var entry : patch.entrySet()) {
				switch (entry.getKey()) {
					case "bezeichnung" -> dto.bezeichnung = JSONMapper.convertToString(entry.getValue(), true, true, 255);
					case "syncSchueler" -> dto.syncSchueler = JSONMapper.convertToBoolean(entry.getValue(), false);
					case "syncKlassen" -> dto.syncKlassen = JSONMapper.convertToBoolean(entry.getValue(), false);
					case "syncLehrer" -> dto.syncLehrer = JSONMapper.convertToBoolean(entry.getValue(), false);
					case "id", "appUrl", "apiUrl", "schoolId", "istVerbunden" -> {
						/* nicht patchbare Attribute werden ignoriert */
					}
					default -> throw new ApiOperationException(Status.BAD_REQUEST,
							"Das Attribut '%s' kann nicht gepatcht werden.".formatted(entry.getKey()));
				}
			}
			repository.update(dto);
			repository.flush();
			return toApi(dto);
		});
	}


	/**
	 * Trennt die Schoolboost-Verbindung, indem der API-Key und ein ggf. vorhandenes Token verworfen werden.
	 * Die Schoolboost-URL sowie die Synchronisations-Optionen bleiben erhalten, sodass die Verbindung über
	 * einen neuen Pairing-Code wiederhergestellt werden kann.
	 *
	 * @return die getrennte Schoolboost-Verbindung
	 */
	public SchoolboostConnection disconnect() {
		return transactional(() -> {
			final DTOSchoolboostVerbindungen dto = getDtoOrThrow();
			dto.apiKey = null;
			dto.token = null;
			dto.tokenTimestamp = null;
			dto.tokenExpiresIn = null;
			repository.update(dto);
			repository.flush();
			return toApi(dto);
		});
	}

}
