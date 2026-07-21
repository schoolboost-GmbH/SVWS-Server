package de.svws_nrw.core.data.schoolboost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse enthält die Daten einer Klasse für die Synchronisation mit
 * einem Schoolboost-Server. Die Attributnamen entsprechen dem Wire-Format
 * des Schoolboost-Push-Endpunktes.
 */
@XmlRootElement
@Schema(description = "Die Daten einer Klasse für die Synchronisation mit einem Schoolboost-Server.")
@TranspilerDTO
public class SchoolboostKlasse {

	/** Die ID der Klasse in der SVWS-Datenbank. */
	@Schema(description = "die ID der Klasse in der SVWS-Datenbank.", example = "47")
	public String svwsId = null;

	/** Das Kürzel der Klasse. */
	@Schema(description = "das Kürzel der Klasse.", example = "1A")
	public @NotNull String className = "";

	/** Das Kürzel des Jahrgangs der Klasse. */
	@Schema(description = "das Kürzel des Jahrgangs der Klasse.", example = "01")
	public @NotNull String gradeLevel = "";

	/** Die SVWS-ID der Klassenleitung. */
	@Schema(description = "die SVWS-ID der Klassenleitung.", example = "10")
	public String homeroomTeacherSvwsId = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchoolboostKlasse() {
		// leer
	}

}
