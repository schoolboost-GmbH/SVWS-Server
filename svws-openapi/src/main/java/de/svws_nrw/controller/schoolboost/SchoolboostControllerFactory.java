package de.svws_nrw.controller.schoolboost;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schoolboost.SchoolboostRepositoryFactory;
import de.svws_nrw.service.schoolboost.SchoolboostGetService;
import de.svws_nrw.service.schoolboost.SchoolboostPushService;
import de.svws_nrw.service.schoolboost.SchoolboostVerbindungenService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Controller-Factory für den Bereich der Schoolboost-Verbindung
 */
public final class SchoolboostControllerFactory {

	/** Die Repository-Factory für die Schoolboost-Verbindungen */
	private final SchoolboostRepositoryFactory repositoryFactory;

	/** Die Datenbank-Verbindung */
	private final DBEntityManager conn;


	/**
	 * Erzeugt eine neue Factory für die übergebene Datenbank-Verbindung.
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	private SchoolboostControllerFactory(final DBEntityManager conn) {
		this.conn = conn;
		this.repositoryFactory = SchoolboostRepositoryFactory.getNewInstance();
	}


	/**
	 * Diese statische Methode dient dem Zugriff in der API-Schicht. Der Zugriff
	 * auf die Schoolboost-Verbindung erfordert administrative Rechte.
	 *
	 * @param request  der HTTP-Request, mit welchem die Factory erzeugt wird
	 *
	 * @return die Factory
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static SchoolboostControllerFactory withAdminAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Repositories zugegriffen wird
		final var conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
		return new SchoolboostControllerFactory(conn);
	}


	/**
	 * Erstellt einen Service für die Verwaltung der Schoolboost-Verbindung
	 *
	 * @return der Service
	 */
	public SchoolboostVerbindungenService getVerbindungenService() {
		return new SchoolboostVerbindungenService(repositoryFactory.getSchoolboostVerbindungenRepository());
	}


	/**
	 * Erstellt einen Service für die Synchronisation mit dem Schoolboost-Server
	 *
	 * @return der Service
	 */
	public SchoolboostPushService getPushService() {
		final SchoolboostVerbindungenService verbindungenService = getVerbindungenService();
		final SchoolboostGetService getService = new SchoolboostGetService(conn);
		return new SchoolboostPushService(repositoryFactory.getSchoolboostVerbindungenRepository(), verbindungenService, getService);
	}

}
