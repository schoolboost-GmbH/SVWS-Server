import type { BenutzerKompetenz, SchoolboostConnection, SimpleOperationResponse } from "@core";

export interface VerbindungenProps {
	verbindung: () => SchoolboostConnection;
	letzteAktion: () => SimpleOperationResponse | null;
	pair: (code: string, appUrl: string) => Promise<void>;
	patch: (data: Partial<SchoolboostConnection>) => Promise<void>;
	check: () => Promise<void>;
	push: () => Promise<void>;
	disconnect: () => Promise<void>;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
