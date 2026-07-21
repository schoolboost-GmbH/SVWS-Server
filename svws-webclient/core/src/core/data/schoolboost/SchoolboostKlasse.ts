import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchoolboostKlasse extends JavaObject {

	/**
	 * Die ID der Klasse in der SVWS-Datenbank.
	 */
	public svwsId: string | null = null;

	/**
	 * Das Kürzel der Klasse.
	 */
	public className: string = "";

	/**
	 * Das Kürzel des Jahrgangs der Klasse.
	 */
	public gradeLevel: string = "";

	/**
	 * Die SVWS-ID der Klassenleitung.
	 */
	public homeroomTeacherSvwsId: string | null = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schoolboost.SchoolboostKlasse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schoolboost.SchoolboostKlasse'].includes(name);
	}

	public static readonly class = new Class<SchoolboostKlasse>('de.svws_nrw.core.data.schoolboost.SchoolboostKlasse');

	public static transpilerFromJSON(json: string): SchoolboostKlasse {
		const obj = JSON.parse(json) as Partial<SchoolboostKlasse>;
		const result = new SchoolboostKlasse();
		result.svwsId = (obj.svwsId === undefined) ? null : obj.svwsId === null ? null : obj.svwsId;
		if (obj.className === undefined)
			throw new Error('invalid json format, missing attribute className');
		result.className = obj.className;
		if (obj.gradeLevel === undefined)
			throw new Error('invalid json format, missing attribute gradeLevel');
		result.gradeLevel = obj.gradeLevel;
		result.homeroomTeacherSvwsId = (obj.homeroomTeacherSvwsId === undefined) ? null : obj.homeroomTeacherSvwsId === null ? null : obj.homeroomTeacherSvwsId;
		return result;
	}

	public static transpilerToJSON(obj: SchoolboostKlasse): string {
		let result = '{';
		result += '"svwsId" : ' + ((obj.svwsId === null) ? 'null' : JSON.stringify(obj.svwsId)) + ',';
		result += '"className" : ' + JSON.stringify(obj.className) + ',';
		result += '"gradeLevel" : ' + JSON.stringify(obj.gradeLevel) + ',';
		result += '"homeroomTeacherSvwsId" : ' + ((obj.homeroomTeacherSvwsId === null) ? 'null' : JSON.stringify(obj.homeroomTeacherSvwsId)) + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchoolboostKlasse>): string {
		let result = '{';
		if (obj.svwsId !== undefined) {
			result += '"svwsId" : ' + ((obj.svwsId === null) ? 'null' : JSON.stringify(obj.svwsId)) + ',';
		}
		if (obj.className !== undefined) {
			result += '"className" : ' + JSON.stringify(obj.className) + ',';
		}
		if (obj.gradeLevel !== undefined) {
			result += '"gradeLevel" : ' + JSON.stringify(obj.gradeLevel) + ',';
		}
		if (obj.homeroomTeacherSvwsId !== undefined) {
			result += '"homeroomTeacherSvwsId" : ' + ((obj.homeroomTeacherSvwsId === null) ? 'null' : JSON.stringify(obj.homeroomTeacherSvwsId)) + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schoolboost_SchoolboostKlasse(obj: unknown): SchoolboostKlasse {
	return obj as SchoolboostKlasse;
}
