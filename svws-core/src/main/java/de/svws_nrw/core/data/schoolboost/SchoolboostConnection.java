package de.svws_nrw.core.data.schoolboost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie enthält die Informationen zu der Verbindung zu einem Schoolboost-Server.
 */
@XmlRootElement
@Schema(description = "Die Informationen zu der Verbindung zu einem Schoolboost-Server.")
@TranspilerDTO
public class SchoolboostConnection {

	/** Die ID der Verbindung. */
	@Schema(description = "die ID der Verbindung in der Datenbank. -1, wenn noch keine Verbindung eingerichtet wurde", example = "1")
	public long id = -1;

	/** Die Bezeichnung der Verbindung */
	@Schema(description = "die Bezeichnung der Verbindung", example = "Schoolboost Grundschule Musterstadt")
	public String bezeichnung = "";

	/** Die URL der Schoolboost-Anwendung. */
	@Schema(description = "Die URL der Schoolboost-Anwendung.", example = "https://app.schoolboost.de")
	public @NotNull String appUrl = "";

	/** Die URL der Schoolboost-API. */
	@Schema(description = "Die URL der Schoolboost-API.", example = "https://api.schoolboost.de/api")
	public String apiUrl = null;

	/** Die ID der Schule bei Schoolboost. */
	@Schema(description = "Die ID der Schule bei Schoolboost.", example = "b7c9d1e3-1234-5678-9abc-def012345678")
	public String schoolId = null;

	/** Gibt an, ob ein API-Key für die Verbindung hinterlegt ist. */
	@Schema(description = "gibt an, ob ein API-Key für die Verbindung hinterlegt ist.", example = "true")
	public boolean istVerbunden = false;

	/** Gibt an, ob die Schülerdaten bei der Synchronisation übertragen werden sollen. */
	@Schema(description = "gibt an, ob die Schülerdaten bei der Synchronisation übertragen werden sollen.", example = "true")
	public boolean syncSchueler = true;

	/** Gibt an, ob die Klassendaten bei der Synchronisation übertragen werden sollen. */
	@Schema(description = "gibt an, ob die Klassendaten bei der Synchronisation übertragen werden sollen.", example = "true")
	public boolean syncKlassen = true;

	/** Gibt an, ob die Lehrerdaten bei der Synchronisation übertragen werden sollen. */
	@Schema(description = "gibt an, ob die Lehrerdaten bei der Synchronisation übertragen werden sollen.", example = "true")
	public boolean syncLehrer = true;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchoolboostConnection() {
		// leer
	}

}
