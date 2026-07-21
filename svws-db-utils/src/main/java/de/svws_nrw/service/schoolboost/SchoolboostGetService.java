package de.svws_nrw.service.schoolboost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.schoolboost.SchoolboostDaten;
import de.svws_nrw.core.data.schoolboost.SchoolboostKlasse;
import de.svws_nrw.core.data.schoolboost.SchoolboostLehrer;
import de.svws_nrw.core.data.schoolboost.SchoolboostSchueler;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsliste;
import de.svws_nrw.data.klassen.DataKlassendaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schoolboost.DTOSchoolboostVerbindungen;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Ein Service zum Aggregieren der Daten, welche an einen Schoolboost-Server
 * übertragen werden. Es werden die Daten des aktuellen Schuljahresabschnittes
 * verwendet; die Schüler-Datensätze entsprechen dem Snapshot-Format des
 * Schoolboost-Push-Endpunktes.
 */
public final class SchoolboostGetService {

	/** Die Datenbank-Verbindung */
	private final DBEntityManager conn;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param conn   die Datenbank-Verbindung
	 */
	public SchoolboostGetService(final DBEntityManager conn) {
		this.conn = conn;
	}


	/**
	 * Aggregiert die Daten für die Synchronisation mit dem Schoolboost-Server
	 * anhand der bei der Verbindung konfigurierten Synchronisations-Optionen.
	 *
	 * @param verbindung   die Verbindung mit den Synchronisations-Optionen
	 *
	 * @return das Aggregat der Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public SchoolboostDaten get(final DTOSchoolboostVerbindungen verbindung) throws ApiOperationException {
		final SchoolboostDaten daten = new SchoolboostDaten();

		// Metadaten der Schule
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnitt();
		daten.meta.schulnummer = String.valueOf(conn.getUser().schuleGetStammdaten().schulNr);
		daten.meta.schuljahr = abschnitt.schuljahr;
		daten.meta.abschnitt = abschnitt.abschnitt;

		final boolean syncSchueler = !Boolean.FALSE.equals(verbindung.syncSchueler);
		final boolean syncKlassen = !Boolean.FALSE.equals(verbindung.syncKlassen);
		final boolean syncLehrer = !Boolean.FALSE.equals(verbindung.syncLehrer);

		// Lehrer aus der Datenbank bestimmen (nur sichtbare Lehrkräfte)
		final Map<Long, DTOLehrer> mapLehrer = new HashMap<>();
		for (final DTOLehrer lehrer : conn.queryAll(DTOLehrer.class)) {
			if (!Boolean.FALSE.equals(lehrer.Sichtbar)) {
				mapLehrer.put(lehrer.ID, lehrer);
			}
		}
		if (syncLehrer) {
			for (final DTOLehrer lehrer : mapLehrer.values()) {
				final SchoolboostLehrer eintrag = new SchoolboostLehrer();
				eintrag.svwsId = String.valueOf(lehrer.ID);
				eintrag.firstName = (lehrer.Vorname == null) ? "" : lehrer.Vorname;
				eintrag.lastName = (lehrer.Nachname == null) ? "" : lehrer.Nachname;
				eintrag.shortName = lehrer.Kuerzel;
				eintrag.email = lehrer.eMailDienstlich;
				daten.teachers.add(eintrag);
			}
		}

		if (!syncKlassen && !syncSchueler) {
			return daten;
		}

		// Jahrgänge und Klassen des aktuellen Abschnittes bestimmen
		final Map<Long, String> mapJahrgangKuerzel = new HashMap<>();
		for (final JahrgangsDaten jahrgang : DataJahrgangsliste.getJahrgangsliste(conn)) {
			mapJahrgangKuerzel.put(jahrgang.id, (jahrgang.kuerzel == null) ? "" : jahrgang.kuerzel);
		}
		final List<KlassenDaten> klassen = new DataKlassendaten(conn).getListBySchuljahresabschnittID(abschnitt.id, false);
		final Map<Long, KlassenDaten> mapKlassen = new HashMap<>();
		for (final KlassenDaten klasse : klassen) {
			mapKlassen.put(klasse.id, klasse);
		}

		if (syncKlassen) {
			for (final KlassenDaten klasse : klassen) {
				if (klasse.kuerzel == null) {
					continue;
				}
				final SchoolboostKlasse eintrag = new SchoolboostKlasse();
				eintrag.svwsId = String.valueOf(klasse.id);
				eintrag.className = klasse.kuerzel;
				eintrag.gradeLevel = getJahrgangKuerzel(mapJahrgangKuerzel, klasse.idJahrgang);
				// Nur Klassenleitungen übertragen, die auch Teil der Lehrer-Lieferung sind
				if (syncLehrer && !klasse.klassenLeitungen.isEmpty()) {
					final Long idKlassenleitung = klasse.klassenLeitungen.getFirst();
					if ((idKlassenleitung != null) && mapLehrer.containsKey(idKlassenleitung)) {
						eintrag.homeroomTeacherSvwsId = String.valueOf(idKlassenleitung);
					}
				}
				daten.classes.add(eintrag);
			}
		}

		if (syncSchueler) {
			addSchueler(daten, abschnitt, mapKlassen, mapJahrgangKuerzel);
		}

		return daten;
	}


	private void addSchueler(final SchoolboostDaten daten, final Schuljahresabschnitt abschnitt, final Map<Long, KlassenDaten> mapKlassen,
			final Map<Long, String> mapJahrgangKuerzel) throws ApiOperationException {
		// Die aktiven Schüler des aktuellen Abschnittes. Es werden bewusst alle Schüler geladen und erst hier
		// gefiltert, da die Filterung in DataSchuelerliste über eine fehlerhafte Abfrage erfolgt (s.Status statt
		// s.idStatus), die beim Übersetzen der Query fehlschlägt.
		final int statusAktiv = Integer.parseInt(SchuelerStatus.AKTIV.daten(abschnitt.schuljahr).kuerzel);
		final List<SchuelerListeEintrag> schuelerListe = DataSchuelerliste.getListeSchueler(conn, abschnitt.id, false).stream()
				.filter(s -> s.status == statusAktiv)
				.toList();
		if (schuelerListe.isEmpty()) {
			return;
		}

		// Stammdaten für Adress- und Kontaktinformationen
		final List<Long> ids = schuelerListe.stream().map(s -> s.id).toList();
		final Map<Long, SchuelerStammdaten> mapStammdaten = new HashMap<>();
		for (final SchuelerStammdaten stammdaten : new DataSchuelerStammdaten(conn).getListByIds(ids)) {
			mapStammdaten.put(stammdaten.id, stammdaten);
		}

		// Orte für die Adressen
		final Map<Long, DTOOrt> mapOrte = new HashMap<>();
		for (final DTOOrt ort : conn.queryAll(DTOOrt.class)) {
			mapOrte.put(ort.ID, ort);
		}

		for (final SchuelerListeEintrag schueler : schuelerListe) {
			final SchoolboostSchueler eintrag = new SchoolboostSchueler();
			eintrag.svwsId = String.valueOf(schueler.id);
			eintrag.firstName = schueler.vorname;
			eintrag.lastName = schueler.nachname;
			eintrag.birthDate = schueler.geburtsdatum;

			final KlassenDaten klasse = (schueler.idKlasse == null) ? null : mapKlassen.get(schueler.idKlasse);
			if ((klasse != null) && (klasse.kuerzel != null)) {
				eintrag.className = klasse.kuerzel;
				eintrag.gradeLevel = getJahrgangKuerzel(mapJahrgangKuerzel, klasse.idJahrgang);
			}

			final SchuelerStammdaten stammdaten = mapStammdaten.get(schueler.id);
			if (stammdaten != null) {
				eintrag.email = ((stammdaten.emailSchule != null) && !stammdaten.emailSchule.isBlank())
						? stammdaten.emailSchule : stammdaten.emailPrivat;
				eintrag.phone = ((stammdaten.telefon != null) && !stammdaten.telefon.isBlank())
						? stammdaten.telefon : stammdaten.telefonMobil;
				eintrag.address = getAdresse(stammdaten, mapOrte);
				eintrag.enrollmentDate = stammdaten.aufnahmedatum;
			}
			daten.students.add(eintrag);
		}
	}


	private static String getJahrgangKuerzel(final Map<Long, String> mapJahrgangKuerzel, final Long idJahrgang) {
		if (idJahrgang == null) {
			return null;
		}
		final String kuerzel = mapJahrgangKuerzel.get(idJahrgang);
		return ((kuerzel == null) || kuerzel.isBlank()) ? null : kuerzel;
	}


	private static String getAdresse(final SchuelerStammdaten stammdaten, final Map<Long, DTOOrt> mapOrte) {
		final StringBuilder sb = new StringBuilder();
		if ((stammdaten.strassenname != null) && !stammdaten.strassenname.isBlank()) {
			sb.append(stammdaten.strassenname.trim());
			if ((stammdaten.hausnummer != null) && !stammdaten.hausnummer.isBlank()) {
				sb.append(' ').append(stammdaten.hausnummer.trim());
				if ((stammdaten.hausnummerZusatz != null) && !stammdaten.hausnummerZusatz.isBlank()) {
					sb.append(stammdaten.hausnummerZusatz.trim());
				}
			}
		}
		final DTOOrt ort = (stammdaten.wohnortID == null) ? null : mapOrte.get(stammdaten.wohnortID);
		if (ort != null) {
			if (!sb.isEmpty()) {
				sb.append(", ");
			}
			if ((ort.PLZ != null) && !ort.PLZ.isBlank()) {
				sb.append(ort.PLZ.trim()).append(' ');
			}
			if ((ort.Bezeichnung != null) && !ort.Bezeichnung.isBlank()) {
				sb.append(ort.Bezeichnung.trim());
			}
		}
		final String result = sb.toString().trim();
		return result.isBlank() ? null : result;
	}

}
