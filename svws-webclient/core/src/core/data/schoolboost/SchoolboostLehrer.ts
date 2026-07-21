import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchoolboostLehrer extends JavaObject {

	/**
	 * Die ID der Lehrkraft in der SVWS-Datenbank.
	 */
	public svwsId: string = "";

	/**
	 * Der Vorname der Lehrkraft.
	 */
	public firstName: string = "";

	/**
	 * Der Nachname der Lehrkraft.
	 */
	public lastName: string = "";

	/**
	 * Das Kürzel der Lehrkraft.
	 */
	public shortName: string | null = null;

	/**
	 * Die dienstliche E-Mail-Adresse der Lehrkraft.
	 */
	public email: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schoolboost.SchoolboostLehrer';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schoolboost.SchoolboostLehrer'].includes(name);
	}

	public static readonly class = new Class<SchoolboostLehrer>('de.svws_nrw.core.data.schoolboost.SchoolboostLehrer');

	public static transpilerFromJSON(json: string): SchoolboostLehrer {
		const obj = JSON.parse(json) as Partial<SchoolboostLehrer>;
		const result = new SchoolboostLehrer();
		if (obj.svwsId === undefined)
			throw new Error('invalid json format, missing attribute svwsId');
		result.svwsId = obj.svwsId;
		if (obj.firstName === undefined)
			throw new Error('invalid json format, missing attribute firstName');
		result.firstName = obj.firstName;
		if (obj.lastName === undefined)
			throw new Error('invalid json format, missing attribute lastName');
		result.lastName = obj.lastName;
		result.shortName = (obj.shortName === undefined) ? null : obj.shortName === null ? null : obj.shortName;
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		return result;
	}

	public static transpilerToJSON(obj: SchoolboostLehrer): string {
		let result = '{';
		result += '"svwsId" : ' + JSON.stringify(obj.svwsId) + ',';
		result += '"firstName" : ' + JSON.stringify(obj.firstName) + ',';
		result += '"lastName" : ' + JSON.stringify(obj.lastName) + ',';
		result += '"shortName" : ' + ((obj.shortName === null) ? 'null' : JSON.stringify(obj.shortName)) + ',';
		result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchoolboostLehrer>): string {
		let result = '{';
		if (obj.svwsId !== undefined) {
			result += '"svwsId" : ' + JSON.stringify(obj.svwsId) + ',';
		}
		if (obj.firstName !== undefined) {
			result += '"firstName" : ' + JSON.stringify(obj.firstName) + ',';
		}
		if (obj.lastName !== undefined) {
			result += '"lastName" : ' + JSON.stringify(obj.lastName) + ',';
		}
		if (obj.shortName !== undefined) {
			result += '"shortName" : ' + ((obj.shortName === null) ? 'null' : JSON.stringify(obj.shortName)) + ',';
		}
		if (obj.email !== undefined) {
			result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schoolboost_SchoolboostLehrer(obj: unknown): SchoolboostLehrer {
	return obj as SchoolboostLehrer;
}
