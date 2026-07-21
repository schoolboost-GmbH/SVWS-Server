import { JavaObject } from '../../../java/lang/JavaObject';
import { SchoolboostKlasse } from '../../../core/data/schoolboost/SchoolboostKlasse';
import { SchoolboostMeta } from '../../../core/data/schoolboost/SchoolboostMeta';
import { ArrayList } from '../../../java/util/ArrayList';
import { SchoolboostSchueler } from '../../../core/data/schoolboost/SchoolboostSchueler';
import { SchoolboostLehrer } from '../../../core/data/schoolboost/SchoolboostLehrer';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class SchoolboostDaten extends JavaObject {

	/**
	 * Die Metadaten der Schule.
	 */
	public meta: SchoolboostMeta = new SchoolboostMeta();

	/**
	 * Die Liste der Schüler.
	 */
	public students: List<SchoolboostSchueler> = new ArrayList<SchoolboostSchueler>();

	/**
	 * Die Liste der Klassen.
	 */
	public classes: List<SchoolboostKlasse> = new ArrayList<SchoolboostKlasse>();

	/**
	 * Die Liste der Lehrkräfte.
	 */
	public teachers: List<SchoolboostLehrer> = new ArrayList<SchoolboostLehrer>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schoolboost.SchoolboostDaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schoolboost.SchoolboostDaten'].includes(name);
	}

	public static readonly class = new Class<SchoolboostDaten>('de.svws_nrw.core.data.schoolboost.SchoolboostDaten');

	public static transpilerFromJSON(json: string): SchoolboostDaten {
		const obj = JSON.parse(json) as Partial<SchoolboostDaten>;
		const result = new SchoolboostDaten();
		if (obj.meta === undefined)
			throw new Error('invalid json format, missing attribute meta');
		result.meta = SchoolboostMeta.transpilerFromJSON(JSON.stringify(obj.meta));
		if (obj.students !== undefined) {
			for (const elem of obj.students) {
				result.students.add(SchoolboostSchueler.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.classes !== undefined) {
			for (const elem of obj.classes) {
				result.classes.add(SchoolboostKlasse.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		if (obj.teachers !== undefined) {
			for (const elem of obj.teachers) {
				result.teachers.add(SchoolboostLehrer.transpilerFromJSON(JSON.stringify(elem)));
			}
		}
		return result;
	}

	public static transpilerToJSON(obj: SchoolboostDaten): string {
		let result = '{';
		result += '"meta" : ' + SchoolboostMeta.transpilerToJSON(obj.meta) + ',';
		result += '"students" : [ ';
		for (let i = 0; i < obj.students.size(); i++) {
			const elem = obj.students.get(i);
			result += SchoolboostSchueler.transpilerToJSON(elem);
			if (i < obj.students.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"classes" : [ ';
		for (let i = 0; i < obj.classes.size(); i++) {
			const elem = obj.classes.get(i);
			result += SchoolboostKlasse.transpilerToJSON(elem);
			if (i < obj.classes.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result += '"teachers" : [ ';
		for (let i = 0; i < obj.teachers.size(); i++) {
			const elem = obj.teachers.get(i);
			result += SchoolboostLehrer.transpilerToJSON(elem);
			if (i < obj.teachers.size() - 1)
				result += ',';
		}
		result += ' ]' + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchoolboostDaten>): string {
		let result = '{';
		if (obj.meta !== undefined) {
			result += '"meta" : ' + SchoolboostMeta.transpilerToJSON(obj.meta) + ',';
		}
		if (obj.students !== undefined) {
			result += '"students" : [ ';
			for (let i = 0; i < obj.students.size(); i++) {
				const elem = obj.students.get(i);
				result += SchoolboostSchueler.transpilerToJSON(elem);
				if (i < obj.students.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.classes !== undefined) {
			result += '"classes" : [ ';
			for (let i = 0; i < obj.classes.size(); i++) {
				const elem = obj.classes.get(i);
				result += SchoolboostKlasse.transpilerToJSON(elem);
				if (i < obj.classes.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		if (obj.teachers !== undefined) {
			result += '"teachers" : [ ';
			for (let i = 0; i < obj.teachers.size(); i++) {
				const elem = obj.teachers.get(i);
				result += SchoolboostLehrer.transpilerToJSON(elem);
				if (i < obj.teachers.size() - 1)
					result += ',';
			}
			result += ' ]' + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schoolboost_SchoolboostDaten(obj: unknown): SchoolboostDaten {
	return obj as SchoolboostDaten;
}
