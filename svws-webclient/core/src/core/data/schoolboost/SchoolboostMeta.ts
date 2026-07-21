import { JavaObject } from '../../../java/lang/JavaObject';
import { Class } from '../../../java/lang/Class';

export class SchoolboostMeta extends JavaObject {

	/**
	 * Die Schulnummer der Schule.
	 */
	public schulnummer: string | null = null;

	/**
	 * Das Schuljahr des aktuellen Schuljahresabschnittes.
	 */
	public schuljahr: number = -1;

	/**
	 * Die Nummer des aktuellen Abschnittes im Schuljahr.
	 */
	public abschnitt: number = -1;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.data.schoolboost.SchoolboostMeta';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.data.schoolboost.SchoolboostMeta'].includes(name);
	}

	public static readonly class = new Class<SchoolboostMeta>('de.svws_nrw.core.data.schoolboost.SchoolboostMeta');

	public static transpilerFromJSON(json: string): SchoolboostMeta {
		const obj = JSON.parse(json) as Partial<SchoolboostMeta>;
		const result = new SchoolboostMeta();
		result.schulnummer = (obj.schulnummer === undefined) ? null : obj.schulnummer === null ? null : obj.schulnummer;
		if (obj.schuljahr === undefined)
			throw new Error('invalid json format, missing attribute schuljahr');
		result.schuljahr = obj.schuljahr;
		if (obj.abschnitt === undefined)
			throw new Error('invalid json format, missing attribute abschnitt');
		result.abschnitt = obj.abschnitt;
		return result;
	}

	public static transpilerToJSON(obj: SchoolboostMeta): string {
		let result = '{';
		result += '"schulnummer" : ' + ((obj.schulnummer === null) ? 'null' : JSON.stringify(obj.schulnummer)) + ',';
		result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

	public static transpilerToJSONPatch(obj: Partial<SchoolboostMeta>): string {
		let result = '{';
		if (obj.schulnummer !== undefined) {
			result += '"schulnummer" : ' + ((obj.schulnummer === null) ? 'null' : JSON.stringify(obj.schulnummer)) + ',';
		}
		if (obj.schuljahr !== undefined) {
			result += '"schuljahr" : ' + obj.schuljahr.toString() + ',';
		}
		if (obj.abschnitt !== undefined) {
			result += '"abschnitt" : ' + obj.abschnitt.toString() + ',';
		}
		result = result.slice(0, -1);
		result += '}';
		return result;
	}

}

export function cast_de_svws_nrw_core_data_schoolboost_SchoolboostMeta(obj: unknown): SchoolboostMeta {
	return obj as SchoolboostMeta;
}
