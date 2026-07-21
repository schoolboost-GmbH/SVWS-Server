package de.svws_nrw.service.schoolboost;

import de.svws_nrw.db.utils.ApiOperationException;

import jakarta.ws.rs.core.Response.Status;

/**
 * Eine Exception, welche signalisiert, dass der Schoolboost-Server den hinterlegten API-Key abgelehnt hat (HTTP 401).
 * Der API-Key ist ungültig oder wurde widerrufen. Ein erneuter Versuch mit einem neuen Token ist daher zwecklos,
 * stattdessen wird die Verbindung getrennt und muss über einen neuen Pairing-Code hergestellt werden.
 */
class SchoolboostApiKeyInvalidException extends ApiOperationException {

	private static final long serialVersionUID = -6153048829370051944L;

	/**
	 * Erzeugt eine neue Exception mit der angegebenen Fehlermeldung.
	 *
	 * @param message   die Fehlermeldung
	 */
	SchoolboostApiKeyInvalidException(final String message) {
		super(Status.UNAUTHORIZED, message);
	}

}
