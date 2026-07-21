package de.svws_nrw.core.data.schoolboost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse enthält das Aggregat der Daten, welche bei einer Synchronisation
 * an den Schoolboost-Server übertragen werden. Die Attributnamen entsprechen
 * dem Wire-Format des Schoolboost-Push-Endpunktes.
 */
@XmlRootElement
@Schema(description = "Das Aggregat der Daten, welche bei einer Synchronisation an den Schoolboost-Server übertragen werden.")
@TranspilerDTO
public class SchoolboostDaten {

	/** Die Metadaten der Schule. */
	@Schema(description = "die Metadaten der Schule.")
	public @NotNull SchoolboostMeta meta = new SchoolboostMeta();

	/** Die Liste der Schüler. */
	@ArraySchema(schema = @Schema(implementation = SchoolboostSchueler.class, description = "die Liste der Schüler."))
	public @NotNull List<SchoolboostSchueler> students = new ArrayList<>();

	/** Die Liste der Klassen. */
	@ArraySchema(schema = @Schema(implementation = SchoolboostKlasse.class, description = "die Liste der Klassen."))
	public @NotNull List<SchoolboostKlasse> classes = new ArrayList<>();

	/** Die Liste der Lehrkräfte. */
	@ArraySchema(schema = @Schema(implementation = SchoolboostLehrer.class, description = "die Liste der Lehrkräfte."))
	public @NotNull List<SchoolboostLehrer> teachers = new ArrayList<>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchoolboostDaten() {
		// leer
	}

}
