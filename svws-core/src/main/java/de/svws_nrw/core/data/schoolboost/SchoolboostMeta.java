package de.svws_nrw.core.data.schoolboost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse enthält die Metadaten der Schule für die Synchronisation mit
 * einem Schoolboost-Server.
 */
@XmlRootElement
@Schema(description = "Die Metadaten der Schule für die Synchronisation mit einem Schoolboost-Server.")
@TranspilerDTO
public class SchoolboostMeta {

	/** Die Schulnummer der Schule. */
	@Schema(description = "die Schulnummer der Schule.", example = "123456")
	public String schulnummer = null;

	/** Das Schuljahr des aktuellen Schuljahresabschnittes. */
	@Schema(description = "das Schuljahr des aktuellen Schuljahresabschnittes.", example = "2026")
	public int schuljahr = -1;

	/** Die Nummer des aktuellen Abschnittes im Schuljahr. */
	@Schema(description = "die Nummer des aktuellen Abschnittes im Schuljahr.", example = "2")
	public int abschnitt = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchoolboostMeta() {
		// leer
	}

}
