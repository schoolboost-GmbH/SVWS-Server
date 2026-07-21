package de.svws_nrw.db.dto.current.schoolboost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schoolboost_Verbindungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schoolboost_Verbindungen")
@JsonPropertyOrder({"id", "bezeichnung", "appUrl", "apiUrl", "schoolId", "apiKey", "tokenTimestamp", "tokenExpiresIn", "token", "syncSchueler", "syncKlassen", "syncLehrer"})
public final class DTOSchoolboostVerbindungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchoolboostVerbindungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes appUrl */
	public static final String QUERY_BY_APPURL = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.appUrl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes appUrl */
	public static final String QUERY_LIST_BY_APPURL = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.appUrl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes apiUrl */
	public static final String QUERY_BY_APIURL = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.apiUrl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes apiUrl */
	public static final String QUERY_LIST_BY_APIURL = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.apiUrl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schoolId */
	public static final String QUERY_BY_SCHOOLID = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.schoolId = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schoolId */
	public static final String QUERY_LIST_BY_SCHOOLID = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.schoolId IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes apiKey */
	public static final String QUERY_BY_APIKEY = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.apiKey = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes apiKey */
	public static final String QUERY_LIST_BY_APIKEY = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.apiKey IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tokenTimestamp */
	public static final String QUERY_BY_TOKENTIMESTAMP = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.tokenTimestamp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tokenTimestamp */
	public static final String QUERY_LIST_BY_TOKENTIMESTAMP = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.tokenTimestamp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tokenExpiresIn */
	public static final String QUERY_BY_TOKENEXPIRESIN = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.tokenExpiresIn = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tokenExpiresIn */
	public static final String QUERY_LIST_BY_TOKENEXPIRESIN = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.tokenExpiresIn IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes token */
	public static final String QUERY_BY_TOKEN = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.token = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes token */
	public static final String QUERY_LIST_BY_TOKEN = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.token IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes syncSchueler */
	public static final String QUERY_BY_SYNCSCHUELER = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.syncSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes syncSchueler */
	public static final String QUERY_LIST_BY_SYNCSCHUELER = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.syncSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes syncKlassen */
	public static final String QUERY_BY_SYNCKLASSEN = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.syncKlassen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes syncKlassen */
	public static final String QUERY_LIST_BY_SYNCKLASSEN = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.syncKlassen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes syncLehrer */
	public static final String QUERY_BY_SYNCLEHRER = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.syncLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes syncLehrer */
	public static final String QUERY_LIST_BY_SYNCLEHRER = "SELECT e FROM DTOSchoolboostVerbindungen e WHERE e.syncLehrer IN ?1";

	/** Die ID der Verbindung */
	@Id
	@Column(name = "id")
	@JsonProperty
	public long id;

	/** Die Bezeichnung der Verbindung für die Darstellung in der Verbindungsliste (null, wenn einfach die URL dargestellt werden soll) */
	@Column(name = "bezeichnung")
	@JsonProperty
	public String bezeichnung;

	/** Die URL der Schoolboost-Anwendung, über die das Pairing und die Token-Anfragen laufen */
	@Column(name = "appUrl")
	@JsonProperty
	public String appUrl;

	/** Die URL der Schoolboost-API, an welche die Daten gesendet werden (wird beim Pairing von Schoolboost übermittelt) */
	@Column(name = "apiUrl")
	@JsonProperty
	public String apiUrl;

	/** Die ID der Schule bei Schoolboost (wird beim Pairing von Schoolboost übermittelt und als Client-ID für die OAuth2-Verbindung genutzt) */
	@Column(name = "schoolId")
	@JsonProperty
	public String schoolId;

	/** Der API-Key, welcher beim Pairing gegen den Pairing-Code getauscht wurde und als Client-Secret für die OAuth2-Verbindung genutzt wird */
	@Column(name = "apiKey")
	@JsonProperty
	public String apiKey;

	/** Verbindungs-Token: Ankunftzeitpunkt des Tokens als Zeitstempel in Millisekungen */
	@Column(name = "tokenTimestamp")
	@JsonProperty
	public Long tokenTimestamp;

	/** Verbindungs-Token: Lebensdauer des Tokens in Sekunden */
	@Column(name = "tokenExpiresIn")
	@JsonProperty
	public Long tokenExpiresIn;

	/** Verbindungs-Token: Das Token */
	@Column(name = "token")
	@JsonProperty
	public String token;

	/** Gibt an, ob die Schülerdaten bei der Synchronisation übertragen werden sollen. */
	@Column(name = "syncSchueler")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean syncSchueler;

	/** Gibt an, ob die Klassendaten bei der Synchronisation übertragen werden sollen. */
	@Column(name = "syncKlassen")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean syncKlassen;

	/** Gibt an, ob die Lehrerdaten bei der Synchronisation übertragen werden sollen. */
	@Column(name = "syncLehrer")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean syncLehrer;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchoolboostVerbindungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchoolboostVerbindungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchoolboostVerbindungen ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param appUrl   der Wert für das Attribut appUrl
	 */
	public DTOSchoolboostVerbindungen(final long id, final String appUrl) {
		this.id = id;
		if (appUrl == null) {
			throw new NullPointerException("appUrl must not be null");
		}
		this.appUrl = appUrl;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOSchoolboostVerbindungen other = (DTOSchoolboostVerbindungen) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchoolboostVerbindungen(id=" + this.id + ", bezeichnung=" + this.bezeichnung + ", appUrl=" + this.appUrl + ", apiUrl=" + this.apiUrl + ", schoolId=" + this.schoolId + ", apiKey=" + this.apiKey + ", tokenTimestamp=" + this.tokenTimestamp + ", tokenExpiresIn=" + this.tokenExpiresIn + ", token=" + this.token + ", syncSchueler=" + this.syncSchueler + ", syncKlassen=" + this.syncKlassen + ", syncLehrer=" + this.syncLehrer + ")";
	}

}
