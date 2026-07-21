package de.svws_nrw.repo.schoolboost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schoolboost.DTOSchoolboostVerbindungen;
import de.svws_nrw.repo.RepositoryImpl;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff auf die Schoolboost-Verbindungen.
 */
public final class SchoolboostVerbindungenRepositoryImpl extends RepositoryImpl<DTOSchoolboostVerbindungen> implements SchoolboostVerbindungenRepository {

	/**
	 * Erstellt ein neues Repository.
	 *
	 * @param conn   die aktuelle Datenbank-Verbindung
	 */
	public SchoolboostVerbindungenRepositoryImpl(final DBEntityManager conn) {
		super(conn, DTOSchoolboostVerbindungen.class, o -> o.id, (o, id) -> o.id = id);
	}

}
