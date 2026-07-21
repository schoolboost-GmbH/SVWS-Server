package de.svws_nrw.api.server;

import java.io.InputStream;

import de.svws_nrw.controller.schoolboost.SchoolboostControllerFactory;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schoolboost.SchoolboostConnection;
import de.svws_nrw.core.data.schoolboost.SchoolboostPairRequest;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.JSONMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Klasse spezifiziert die OpenAPI-Schnittstelle für die Verbindung zu einem
 * Schoolboost-Server. Ein Zugriff erfolgt über den Pfad
 * https://{Hostname}/db/{schema}/schoolboost/...
 */
@Path("/db/{schema}/schoolboost")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Server")
public class APISchoolboost {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public APISchoolboost() {
		// leer
	}


	/**
	 * Die OpenAPI-Methode für die Abfrage der Schoolboost-Verbindung.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die Schoolboost-Verbindung
	 */
	@GET
	@Path("/connection")
	@Operation(summary = "Gibt die Schoolboost-Verbindung zurück.",
			description = "Gibt die Schoolboost-Verbindung zurück. Wurde noch keine Verbindung eingerichtet, so wird ein leeres Objekt mit der ID -1"
					+ " zurückgegeben. Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Verwalten der Schoolboost-Verbindung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die Schoolboost-Verbindung",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchoolboostConnection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schoolboost-Verbindung zu verwalten.")
	public Response getSchoolboostConnection(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		final SchoolboostConnection daten = SchoolboostControllerFactory.withAdminAccess(request)
				.getVerbindungenService().get();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Die OpenAPI-Methode für das Herstellen der Schoolboost-Verbindung über einen Pairing-Code.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param daten     die Pairing-Informationen (Code und App-URL)
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die eingerichtete Schoolboost-Verbindung
	 */
	@POST
	@Path("/connection/pair")
	@Operation(summary = "Stellt die Verbindung zu einem Schoolboost-Server über einen Pairing-Code her.",
			description = "Tauscht den übergebenen Pairing-Code bei dem Schoolboost-Server gegen einen API-Key und hinterlegt diesen in der"
					+ " SVWS-Datenbank. Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Verwalten der Schoolboost-Verbindung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die eingerichtete Schoolboost-Verbindung",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchoolboostConnection.class)))
	@ApiResponse(responseCode = "400", description = "Der Pairing-Code ist ungültig oder abgelaufen.")
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schoolboost-Verbindung zu verwalten.")
	@ApiResponse(responseCode = "502", description = "Der Schoolboost-Server ist nicht erreichbar.")
	public Response pairSchoolboostConnection(@PathParam("schema") final String schema,
			@RequestBody(description = "Die Pairing-Informationen", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchoolboostPairRequest.class))) @Valid final SchoolboostPairRequest daten,
			@Context final HttpServletRequest request) {
		final Logger logger = new Logger();
		logger.addConsumer(new LogConsumerList());
		final SchoolboostConnection result = SchoolboostControllerFactory.withAdminAccess(request)
				.getVerbindungenService().pair(daten, logger);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}


	/**
	 * Die OpenAPI-Methode für das Anpassen der Schoolboost-Verbindung.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param is        der Input-Stream mit den zu patchenden Attributen
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die angepasste Schoolboost-Verbindung
	 */
	@PATCH
	@Path("/connection")
	@Operation(summary = "Passt die Schoolboost-Verbindung an.",
			description = "Passt die Bezeichnung sowie die Synchronisations-Optionen der Schoolboost-Verbindung an. Es wird geprüft, ob der"
					+ " SVWS-Benutzer die notwendige Berechtigung zum Verwalten der Schoolboost-Verbindung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die angepasste Schoolboost-Verbindung",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchoolboostConnection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schoolboost-Verbindung zu verwalten.")
	@ApiResponse(responseCode = "404", description = "Es wurde noch keine Schoolboost-Verbindung eingerichtet.")
	public Response patchSchoolboostConnection(@PathParam("schema") final String schema,
			@RequestBody(description = "Die zu patchenden Attribute", required = true,
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = SchoolboostConnection.class))) final InputStream is,
			@Context final HttpServletRequest request) {
		final SchoolboostConnection result = SchoolboostControllerFactory.withAdminAccess(request)
				.getVerbindungenService().patch(JSONMapper.toMap(is));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}


	/**
	 * Die OpenAPI-Methode für das Trennen der Schoolboost-Verbindung.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return die getrennte Schoolboost-Verbindung
	 */
	@POST
	@Path("/connection/disconnect")
	@Operation(summary = "Trennt die Verbindung zu dem Schoolboost-Server.",
			description = "Verwirft den in der SVWS-Datenbank hinterlegten API-Key sowie ein ggf. vorhandenes Token. Die Schoolboost-URL und die"
					+ " Synchronisations-Optionen bleiben erhalten, sodass die Verbindung über einen neuen Pairing-Code wiederhergestellt werden"
					+ " kann. Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Verwalten der Schoolboost-Verbindung besitzt.")
	@ApiResponse(responseCode = "200", description = "Die getrennte Schoolboost-Verbindung",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SchoolboostConnection.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schoolboost-Verbindung zu verwalten.")
	@ApiResponse(responseCode = "404", description = "Es wurde noch keine Schoolboost-Verbindung eingerichtet.")
	public Response disconnectSchoolboostConnection(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		final SchoolboostConnection result = SchoolboostControllerFactory.withAdminAccess(request)
				.getVerbindungenService().disconnect();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}


	/**
	 * Die OpenAPI-Methode für den Test der Schoolboost-Verbindung.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis des Verbindungstests
	 */
	@GET
	@Path("/connection/check")
	@Operation(summary = "Prüft, ob der Schoolboost-Server mit den hinterlegten Verbindungsdaten erreichbar ist.",
			description = "Prüft, ob der Schoolboost-Server mit den hinterlegten Verbindungsdaten erreichbar ist, indem ein neues Token angefordert"
					+ " wird. Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Verwalten der Schoolboost-Verbindung besitzt.")
	@ApiResponse(responseCode = "200", description = "Das Ergebnis des Verbindungstests",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schoolboost-Verbindung zu verwalten.")
	@ApiResponse(responseCode = "404", description = "Es wurde noch keine Schoolboost-Verbindung eingerichtet.")
	public Response checkSchoolboostConnection(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		final SimpleOperationResponse result = SchoolboostControllerFactory.withAdminAccess(request)
				.getPushService().check();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}


	/**
	 * Die OpenAPI-Methode für das Übertragen der Schuldaten an den Schoolboost-Server.
	 *
	 * @param schema    das Datenbankschema, auf welches die Abfrage ausgeführt werden soll
	 * @param request   die Informationen zur HTTP-Anfrage
	 *
	 * @return das Ergebnis der Synchronisation
	 */
	@POST
	@Path("/connection/push")
	@Operation(summary = "Überträgt die Schuldaten an den Schoolboost-Server.",
			description = "Aggregiert die Schuldaten (Schüler, Klassen, Lehrkräfte) gemäß den Synchronisations-Optionen der Verbindung und überträgt"
					+ " diese an den Schoolboost-Server. Es wird geprüft, ob der SVWS-Benutzer die notwendige Berechtigung zum Verwalten der"
					+ " Schoolboost-Verbindung besitzt.")
	@ApiResponse(responseCode = "200", description = "Das Ergebnis der Synchronisation",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimpleOperationResponse.class)))
	@ApiResponse(responseCode = "403", description = "Der SVWS-Benutzer hat keine Rechte, um die Schoolboost-Verbindung zu verwalten.")
	@ApiResponse(responseCode = "404", description = "Es wurde noch keine Schoolboost-Verbindung eingerichtet.")
	@ApiResponse(responseCode = "502", description = "Der Schoolboost-Server ist nicht erreichbar oder hat die Daten abgelehnt.")
	public Response pushSchoolboostDaten(@PathParam("schema") final String schema, @Context final HttpServletRequest request) {
		final SimpleOperationResponse result = SchoolboostControllerFactory.withAdminAccess(request)
				.getPushService().push();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}

}
