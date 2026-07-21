package de.svws_nrw.core.data.schoolboost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse enthält die Daten eines Schülers für die Synchronisation mit
 * einem Schoolboost-Server. Die Attributnamen entsprechen dem Wire-Format
 * des Schoolboost-Push-Endpunktes.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Schülers für die Synchronisation mit einem Schoolboost-Server.")
@TranspilerDTO
public class SchoolboostSchueler {

	/** Die ID des Schülers in der SVWS-Datenbank. */
	@Schema(description = "die ID des Schülers in der SVWS-Datenbank.", example = "4711")
	public @NotNull String svwsId = "";

	/** Der Vorname des Schülers. */
	@Schema(description = "der Vorname des Schülers.", example = "Max")
	public @NotNull String firstName = "";

	/** Der Nachname des Schülers. */
	@Schema(description = "der Nachname des Schülers.", example = "Mustermann")
	public @NotNull String lastName = "";

	/** Das Geburtsdatum des Schülers (ISO-8601). */
	@Schema(description = "das Geburtsdatum des Schülers (ISO-8601).", example = "2017-02-03")
	public String birthDate = null;

	/** Die E-Mail-Adresse des Schülers. */
	@Schema(description = "die E-Mail-Adresse des Schülers.", example = "max@example.com")
	public String email = null;

	/** Die Telefonnummer des Schülers. */
	@Schema(description = "die Telefonnummer des Schülers.", example = "+49 170 1234567")
	public String phone = null;

	/** Die Adresse des Schülers. */
	@Schema(description = "die Adresse des Schülers.", example = "Musterweg 1, 12345 Musterstadt")
	public String address = null;

	/** Das Kürzel des Jahrgangs des Schülers. */
	@Schema(description = "das Kürzel des Jahrgangs des Schülers.", example = "01")
	public String gradeLevel = null;

	/** Das Kürzel der Klasse des Schülers. */
	@Schema(description = "das Kürzel der Klasse des Schülers.", example = "1A")
	public String className = null;

	/** Das Aufnahmedatum des Schülers (ISO-8601). */
	@Schema(description = "das Aufnahmedatum des Schülers (ISO-8601).", example = "2024-08-01")
	public String enrollmentDate = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchoolboostSchueler() {
		// leer
	}

}
