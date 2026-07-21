package de.svws_nrw.repo.schoolboost;

import de.svws_nrw.db.dto.current.schoolboost.DTOSchoolboostVerbindungen;
import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class SchoolboostRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static SchoolboostRepositoryFactory getNewInstance() {
		return new SchoolboostRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchoolboostVerbindungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchoolboostVerbindungenRepository getSchoolboostVerbindungenRepository() {
		return getOrCreate(SchoolboostVerbindungenRepository.class, () -> new SchoolboostVerbindungenRepositoryImpl(conn));
	}

}
