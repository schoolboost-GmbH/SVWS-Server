import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchoolboostConnection extends JavaObject {

	/**
	 * Die ID der Verbindung.
	 */
	public id: number = -1;

	/**
	 * Die Bezeichnung der Verbindung
	 */
	public bezeichnung: string | null = "";

	/**
	 * Die URL der Schoolboost-Anwendung.
	 */
	public appUrl: string = "";

	/**
	 * Die URL der Schoolboost-API.
	 */
	public apiUrl: string | null = null;

	/**
	 * Die ID der Schule bei Schoolboost.
	 */
	public schoolId: string | null = null;

	/**
	 * Gibt an, ob ein API-Key für die Verbindung hinterlegt ist.
	 */
	public istVerbunden: boolean = false;

	/**
	 * Gibt an, ob die Schülerdaten bei der Synchronisation übertragen werden sollen.
	 */
	public syncSchueler: boolean = true;

	/**
	 * Gibt an, ob die Klassendaten bei der Synchronisation übertragen werden sollen.
	 */
	public syncKlassen: boolean = true;

	/**
	 * Gibt an, ob die Lehrerdaten bei der Synchronisation übertragen werden sollen.
	 */
	public syncLehrer: boolean = true;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schoolboost.SchoolboostConnection';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schoolboost.SchoolboostConnection'].includes(name);
	}

	public static readonly class = new Class<SchoolboostConnection>('de.svws_nrw.core.data.schoolboost.SchoolboostConnection');

	public static transpilerFromJSON(json: string): SchoolboostConnection {
		const obj = JSON.parse(json) as Partial<SchoolboostConnection>;
		const result = new SchoolboostConnection();
		if (obj.id === undefined)
			throw new Error('invalid json format, missing attribute id');
		result.id = obj.id;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		if (obj.appUrl === undefined)
			throw new Error('invalid json format, missing attribute appUrl');
		result.appUrl = obj.appUrl;
		result.apiUrl = (obj.apiUrl === undefined) ? null : obj.apiUrl === null ? null : obj.apiUrl;
		result.schoolId = (obj.schoolId === undefined) ? null : obj.schoolId === null ? null : obj.schoolId;
		if (obj.istVerbunden === undefined)
			throw new Error('invalid json format, missing attribute istVerbunden');
		result.istVerbunden = obj.istVerbunden;
		if (obj.syncSchueler === undefined)
			throw new Error('invalid json format, missing attribute syncSchueler');
		result.syncSchueler = obj.syncSchueler;
		if (obj.syncKlassen === undefined)
			throw new Error('invalid json format, missing attribute syncKlassen');
		result.syncKlassen = obj.syncKlassen;
		if (obj.syncLehrer === undefined)
			throw new Error('invalid json format, missing attribute syncLehrer');
		result.syncLehrer = obj.syncLehrer;
		return result;
	}

	public static transpilerToJSON(obj: SchoolboostConnection): string {
		let result = '{';
		result += '"id" : ' + obj.id.toString() + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result += '"appUrl" : ' + JSON.stringify(obj.appUrl) + ',';
		result += '"apiUrl" : ' + ((obj.apiUrl === null) ? 'null' : JSON.stringify(obj.apiUrl)) + ',';
		result += '"schoolId" : ' + ((obj.schoolId === null) ? 'null' : JSON.stringify(obj.schoolId)) + ',';
		result += '"istVerbunden" : ' + obj.istVerbunden.toString() + ',';
		result += '"syncSchueler" : ' + obj.syncSchueler.toString() + ',';
		result += '"syncKlassen" : ' + obj.syncKlassen.toString() + ',';
		result += '"syncLehrer" : ' + obj.syncLehrer.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchoolboostConnection>): string {
		let result = '{';
		if (obj.id !== undefined) {
			result += '"id" : ' + obj.id.toString() + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		if (obj.appUrl !== undefined) {
			result += '"appUrl" : ' + JSON.stringify(obj.appUrl) + ',';
		}
		if (obj.apiUrl !== undefined) {
			result += '"apiUrl" : ' + ((obj.apiUrl === null) ? 'null' : JSON.stringify(obj.apiUrl)) + ',';
		}
		if (obj.schoolId !== undefined) {
			result += '"schoolId" : ' + ((obj.schoolId === null) ? 'null' : JSON.stringify(obj.schoolId)) + ',';
		}
		if (obj.istVerbunden !== undefined) {
			result += '"istVerbunden" : ' + obj.istVerbunden.toString() + ',';
		}
		if (obj.syncSchueler !== undefined) {
			result += '"syncSchueler" : ' + obj.syncSchueler.toString() + ',';
		}
		if (obj.syncKlassen !== undefined) {
			result += '"syncKlassen" : ' + obj.syncKlassen.toString() + ',';
		}
		if (obj.syncLehrer !== undefined) {
			result += '"syncLehrer" : ' + obj.syncLehrer.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schoolboost_SchoolboostConnection(obj: unknown): SchoolboostConnection {
	return obj as SchoolboostConnection;
}
