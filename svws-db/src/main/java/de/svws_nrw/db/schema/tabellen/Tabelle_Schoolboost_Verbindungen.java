package de.svws_nrw.db.schema.tabellen;

import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.schema.SchemaDatentypen;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;

/**
 * Diese Klasse beinhaltet die Schema-Definition für die Tabelle Schoolboost_Verbindungen.
 */
public class Tabelle_Schoolboost_Verbindungen extends SchemaTabelle {

	/** Die Definition der Tabellenspalte id */
	public final SchemaTabelleSpalte col_id = add("id", SchemaDatentypen.BIGINT, true)
			.setNotNull()
			.setJavaComment("Die ID der Verbindung");

	/** Die Definition der Tabellenspalte bezeichnung */
	public final SchemaTabelleSpalte col_bezeichnung = add("bezeichnung", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaComment("Die Bezeichnung der Verbindung für die Darstellung in der Verbindungsliste (null, wenn einfach die URL dargestellt werden soll)");

	/** Die Definition der Tabellenspalte appUrl */
	public final SchemaTabelleSpalte col_appUrl = add("appUrl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setNotNull()
			.setJavaComment("Die URL der Schoolboost-Anwendung, über die das Pairing und die Token-Anfragen laufen");

	/** Die Definition der Tabellenspalte apiUrl */
	public final SchemaTabelleSpalte col_apiUrl = add("apiUrl", SchemaDatentypen.VARCHAR, false).setDatenlaenge(255)
			.setJavaComment("Die URL der Schoolboost-API, an welche die Daten gesendet werden (wird beim Pairing von Schoolboost übermittelt)");

	/** Die Definition der Tabellenspalte schoolId */
	public final SchemaTabelleSpalte col_schoolId = add("schoolId", SchemaDatentypen.VARCHAR, false).setDatenlaenge(64)
			.setJavaComment("Die ID der Schule bei Schoolboost (wird beim Pairing von Schoolboost übermittelt und als Client-ID für die OAuth2-Verbindung genutzt)");

	/** Die Definition der Tabellenspalte apiKey */
	public final SchemaTabelleSpalte col_apiKey = add("apiKey", SchemaDatentypen.TEXT, false)
			.setJavaComment("Der API-Key, welcher beim Pairing gegen den Pairing-Code getauscht wurde und als Client-Secret für die OAuth2-Verbindung genutzt wird");

	/** Die Definition der Tabellenspalte tokenTimestamp */
	public final SchemaTabelleSpalte col_tokenTimestamp = add("tokenTimestamp", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Verbindungs-Token: Ankunftzeitpunkt des Tokens als Zeitstempel in Millisekungen");

	/** Die Definition der Tabellenspalte tokenExpiresIn */
	public final SchemaTabelleSpalte col_tokenExpiresIn = add("tokenExpiresIn", SchemaDatentypen.BIGINT, false)
			.setJavaComment("Verbindungs-Token: Lebensdauer des Tokens in Sekunden");

	/** Die Definition der Tabellenspalte token */
	public final SchemaTabelleSpalte col_token = add("token", SchemaDatentypen.TEXT, false)
			.setJavaComment("Verbindungs-Token: Das Token");

	/** Die Definition der Tabellenspalte syncSchueler */
	public final SchemaTabelleSpalte col_syncSchueler = add("syncSchueler", SchemaDatentypen.INT, false)
			.setDefault("1")
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob die Schülerdaten bei der Synchronisation übertragen werden sollen.");

	/** Die Definition der Tabellenspalte syncKlassen */
	public final SchemaTabelleSpalte col_syncKlassen = add("syncKlassen", SchemaDatentypen.INT, false)
			.setDefault("1")
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob die Klassendaten bei der Synchronisation übertragen werden sollen.");

	/** Die Definition der Tabellenspalte syncLehrer */
	public final SchemaTabelleSpalte col_syncLehrer = add("syncLehrer", SchemaDatentypen.INT, false)
			.setDefault("1")
			.setConverter(Boolean01Converter.class)
			.setJavaComment("Gibt an, ob die Lehrerdaten bei der Synchronisation übertragen werden sollen.");


	/**
	 * Erstellt die Schema-Defintion für die Tabelle Schoolboost_Verbindungen.
	 */
	public Tabelle_Schoolboost_Verbindungen() {
		super("Schoolboost_Verbindungen", SchemaRevisionen.REV_61);
		setMigrate(false);
		setImportExport(true);
		setPKAutoIncrement();
		setJavaSubPackage("schoolboost");
		setJavaClassName("DTOSchoolboostVerbindungen");
		setJavaComment("Tabelle für die Informationen von Schoolboost-Verbindungen mit den OAuth2-Verbindungsinformationen zu dem Schoolboost-Server");
	}

}
