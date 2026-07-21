package de.svws_nrw.core.data.schoolboost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse enthält die Daten einer Lehrkraft für die Synchronisation mit
 * einem Schoolboost-Server. Die Attributnamen entsprechen dem Wire-Format
 * des Schoolboost-Push-Endpunktes.
 */
@XmlRootElement
@Schema(description = "Die Daten einer Lehrkraft für die Synchronisation mit einem Schoolboost-Server.")
@TranspilerDTO
public class SchoolboostLehrer {

	/** Die ID der Lehrkraft in der SVWS-Datenbank. */
	@Schema(description = "die ID der Lehrkraft in der SVWS-Datenbank.", example = "10")
	public @NotNull String svwsId = "";

	/** Der Vorname der Lehrkraft. */
	@Schema(description = "der Vorname der Lehrkraft.", example = "Tina")
	public @NotNull String firstName = "";

	/** Der Nachname der Lehrkraft. */
	@Schema(description = "der Nachname der Lehrkraft.", example = "Testerin")
	public @NotNull String lastName = "";

	/** Das Kürzel der Lehrkraft. */
	@Schema(description = "das Kürzel der Lehrkraft.", example = "TT")
	public String shortName = null;

	/** Die dienstliche E-Mail-Adresse der Lehrkraft. */
	@Schema(description = "die dienstliche E-Mail-Adresse der Lehrkraft.", example = "tina@schule.example")
	public String email = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchoolboostLehrer() {
		// leer
	}

}
