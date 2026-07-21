package de.svws_nrw.core.data.schoolboost;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie enthält die Informationen für das Pairing mit einem Schoolboost-Server.
 */
@XmlRootElement
@Schema(description = "Die Informationen für das Pairing mit einem Schoolboost-Server.")
@TranspilerDTO
public class SchoolboostPairRequest {

	/** Der Pairing-Code, welcher im Schoolboost-Dashboard erzeugt wurde. */
	@Schema(description = "der Pairing-Code, welcher im Schoolboost-Dashboard erzeugt wurde.", example = "AB2CD3EF")
	public @NotNull String code = "";

	/** Die URL der Schoolboost-Anwendung. */
	@Schema(description = "die URL der Schoolboost-Anwendung.", example = "https://app.schoolboost.de")
	public @NotNull String appUrl = "";

	/** Die Bezeichnung der Verbindung (optional). */
	@Schema(description = "die Bezeichnung der Verbindung (optional).", example = "Schoolboost")
	public String bezeichnung = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchoolboostPairRequest() {
		// leer
	}

}
