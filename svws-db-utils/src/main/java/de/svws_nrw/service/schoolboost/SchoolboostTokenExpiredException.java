package de.svws_nrw.service.schoolboost;

import de.svws_nrw.db.utils.ApiOperationException;

import jakarta.ws.rs.core.Response.Status;

/**
 * Eine Exception, welche signalisiert, dass der Schoolboost-Server ein Token abgelehnt hat (HTTP 401).
 * Das Token ist abgelaufen oder ungültig, der hinterlegte API-Key ist davon aber nicht betroffen.
 * Ein neues Token kann daher angefordert und die Operation wiederholt werden.
 */
class SchoolboostTokenExpiredException extends ApiOperationException {

	private static final long serialVersionUID = 4058215857392018721L;

	/**
	 * Erzeugt eine neue Exception mit der angegebenen Fehlermeldung.
	 *
	 * @param message   die Fehlermeldung
	 */
	SchoolboostTokenExpiredException(final String message) {
		super(Status.BAD_GATEWAY, message);
	}

}
