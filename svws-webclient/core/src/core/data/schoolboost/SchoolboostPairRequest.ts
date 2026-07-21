import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchoolboostPairRequest extends JavaObject {

	/**
	 * Der Pairing-Code, welcher im Schoolboost-Dashboard erzeugt wurde.
	 */
	public code: string = "";

	/**
	 * Die URL der Schoolboost-Anwendung.
	 */
	public appUrl: string = "";

	/**
	 * Die Bezeichnung der Verbindung (optional).
	 */
	public bezeichnung: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schoolboost.SchoolboostPairRequest';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schoolboost.SchoolboostPairRequest'].includes(name);
	}

	public static readonly class = new Class<SchoolboostPairRequest>('de.svws_nrw.core.data.schoolboost.SchoolboostPairRequest');

	public static transpilerFromJSON(json: string): SchoolboostPairRequest {
		const obj = JSON.parse(json) as Partial<SchoolboostPairRequest>;
		const result = new SchoolboostPairRequest();
		if (obj.code === undefined)
			throw new Error('invalid json format, missing attribute code');
		result.code = obj.code;
		if (obj.appUrl === undefined)
			throw new Error('invalid json format, missing attribute appUrl');
		result.appUrl = obj.appUrl;
		result.bezeichnung = (obj.bezeichnung === undefined) ? null : obj.bezeichnung === null ? null : obj.bezeichnung;
		return result;
	}

	public static transpilerToJSON(obj: SchoolboostPairRequest): string {
		let result = '{';
		result += '"code" : ' + JSON.stringify(obj.code) + ',';
		result += '"appUrl" : ' + JSON.stringify(obj.appUrl) + ',';
		result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchoolboostPairRequest>): string {
		let result = '{';
		if (obj.code !== undefined) {
			result += '"code" : ' + JSON.stringify(obj.code) + ',';
		}
		if (obj.appUrl !== undefined) {
			result += '"appUrl" : ' + JSON.stringify(obj.appUrl) + ',';
		}
		if (obj.bezeichnung !== undefined) {
			result += '"bezeichnung" : ' + ((obj.bezeichnung === null) ? 'null' : JSON.stringify(obj.bezeichnung)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schoolboost_SchoolboostPairRequest(obj: unknown): SchoolboostPairRequest {
	return obj as SchoolboostPairRequest;
}
