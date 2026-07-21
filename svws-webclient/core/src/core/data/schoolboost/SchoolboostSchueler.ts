import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchoolboostSchueler extends JavaObject {

	/**
	 * Die ID des Schülers in der SVWS-Datenbank.
	 */
	public svwsId: string = "";

	/**
	 * Der Vorname des Schülers.
	 */
	public firstName: string = "";

	/**
	 * Der Nachname des Schülers.
	 */
	public lastName: string = "";

	/**
	 * Das Geburtsdatum des Schülers (ISO-8601).
	 */
	public birthDate: string | null = null;

	/**
	 * Die E-Mail-Adresse des Schülers.
	 */
	public email: string | null = null;

	/**
	 * Die Telefonnummer des Schülers.
	 */
	public phone: string | null = null;

	/**
	 * Die Adresse des Schülers.
	 */
	public address: string | null = null;

	/**
	 * Das Kürzel des Jahrgangs des Schülers.
	 */
	public gradeLevel: string | null = null;

	/**
	 * Das Kürzel der Klasse des Schülers.
	 */
	public className: string | null = null;

	/**
	 * Das Aufnahmedatum des Schülers (ISO-8601).
	 */
	public enrollmentDate: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schoolboost.SchoolboostSchueler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schoolboost.SchoolboostSchueler'].includes(name);
	}

	public static readonly class = new Class<SchoolboostSchueler>('de.svws_nrw.core.data.schoolboost.SchoolboostSchueler');

	public static transpilerFromJSON(json: string): SchoolboostSchueler {
		const obj = JSON.parse(json) as Partial<SchoolboostSchueler>;
		const result = new SchoolboostSchueler();
		if (obj.svwsId === undefined)
			throw new Error('invalid json format, missing attribute svwsId');
		result.svwsId = obj.svwsId;
		if (obj.firstName === undefined)
			throw new Error('invalid json format, missing attribute firstName');
		result.firstName = obj.firstName;
		if (obj.lastName === undefined)
			throw new Error('invalid json format, missing attribute lastName');
		result.lastName = obj.lastName;
		result.birthDate = (obj.birthDate === undefined) ? null : obj.birthDate === null ? null : obj.birthDate;
		result.email = (obj.email === undefined) ? null : obj.email === null ? null : obj.email;
		result.phone = (obj.phone === undefined) ? null : obj.phone === null ? null : obj.phone;
		result.address = (obj.address === undefined) ? null : obj.address === null ? null : obj.address;
		result.gradeLevel = (obj.gradeLevel === undefined) ? null : obj.gradeLevel === null ? null : obj.gradeLevel;
		result.className = (obj.className === undefined) ? null : obj.className === null ? null : obj.className;
		result.enrollmentDate = (obj.enrollmentDate === undefined) ? null : obj.enrollmentDate === null ? null : obj.enrollmentDate;
		return result;
	}

	public static transpilerToJSON(obj: SchoolboostSchueler): string {
		let result = '{';
		result += '"svwsId" : ' + JSON.stringify(obj.svwsId) + ',';
		result += '"firstName" : ' + JSON.stringify(obj.firstName) + ',';
		result += '"lastName" : ' + JSON.stringify(obj.lastName) + ',';
		result += '"birthDate" : ' + ((obj.birthDate === null) ? 'null' : JSON.stringify(obj.birthDate)) + ',';
		result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		result += '"phone" : ' + ((obj.phone === null) ? 'null' : JSON.stringify(obj.phone)) + ',';
		result += '"address" : ' + ((obj.address === null) ? 'null' : JSON.stringify(obj.address)) + ',';
		result += '"gradeLevel" : ' + ((obj.gradeLevel === null) ? 'null' : JSON.stringify(obj.gradeLevel)) + ',';
		result += '"className" : ' + ((obj.className === null) ? 'null' : JSON.stringify(obj.className)) + ',';
		result += '"enrollmentDate" : ' + ((obj.enrollmentDate === null) ? 'null' : JSON.stringify(obj.enrollmentDate)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchoolboostSchueler>): string {
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
		if (obj.birthDate !== undefined) {
			result += '"birthDate" : ' + ((obj.birthDate === null) ? 'null' : JSON.stringify(obj.birthDate)) + ',';
		}
		if (obj.email !== undefined) {
			result += '"email" : ' + ((obj.email === null) ? 'null' : JSON.stringify(obj.email)) + ',';
		}
		if (obj.phone !== undefined) {
			result += '"phone" : ' + ((obj.phone === null) ? 'null' : JSON.stringify(obj.phone)) + ',';
		}
		if (obj.address !== undefined) {
			result += '"address" : ' + ((obj.address === null) ? 'null' : JSON.stringify(obj.address)) + ',';
		}
		if (obj.gradeLevel !== undefined) {
			result += '"gradeLevel" : ' + ((obj.gradeLevel === null) ? 'null' : JSON.stringify(obj.gradeLevel)) + ',';
		}
		if (obj.className !== undefined) {
			result += '"className" : ' + ((obj.className === null) ? 'null' : JSON.stringify(obj.className)) + ',';
		}
		if (obj.enrollmentDate !== undefined) {
			result += '"enrollmentDate" : ' + ((obj.enrollmentDate === null) ? 'null' : JSON.stringify(obj.enrollmentDate)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schoolboost_SchoolboostSchueler(obj: unknown): SchoolboostSchueler {
	return obj as SchoolboostSchueler;
}
