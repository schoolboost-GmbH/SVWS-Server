import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import type { SimpleOperationResponse } from "@core";
import { SchoolboostConnection, SchoolboostPairRequest } from "@core";
import { api } from "~/router/Api";


interface RouteStateVerbindungen extends RouteStateInterface {
	verbindung: SchoolboostConnection;
	letzteAktion: SimpleOperationResponse | null;
}

const defaultState = <RouteStateVerbindungen> {
	verbindung: new SchoolboostConnection(),
	letzteAktion: null,
};

export class RouteDataVerbindungen extends RouteData<RouteStateVerbindungen> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		let verbindung = new SchoolboostConnection();
		if (api.benutzerIstAdmin) {
			verbindung = await api.server.getSchoolboostConnection(api.schema);
		}
		this.setPatchedState({ verbindung, letzteAktion: null });
	}

	public async entferneDaten() {
		const verbindung = new SchoolboostConnection();
		this.setPatchedState({ verbindung, letzteAktion: null });
	}

	get verbindung(): SchoolboostConnection {
		return this._state.value.verbindung;
	}

	get letzteAktion(): SimpleOperationResponse | null {
		return this._state.value.letzteAktion;
	}

	pair = async (code: string, appUrl: string): Promise<void> => {
		const request = new SchoolboostPairRequest();
		request.code = code;
		request.appUrl = appUrl;
		const verbindung = await api.server.pairSchoolboostConnection(request, api.schema);
		this.setPatchedState({ verbindung, letzteAktion: null });
	};

	patch = async (data: Partial<SchoolboostConnection>): Promise<void> => {
		const verbindung = this._state.value.verbindung;
		await api.server.patchSchoolboostConnection(data, api.schema);
		Object.assign(verbindung, data);
		this.setPatchedState({ verbindung });
	};

	check = async (): Promise<void> => {
		const letzteAktion = await api.server.checkSchoolboostConnection(api.schema);
		this.setPatchedState({ letzteAktion });
	};

	push = async (): Promise<void> => {
		const letzteAktion = await api.server.pushSchoolboostDaten(api.schema);
		this.setPatchedState({ letzteAktion });
	};

	disconnect = async (): Promise<void> => {
		const verbindung = await api.server.disconnectSchoolboostConnection(api.schema);
		this.setPatchedState({ verbindung, letzteAktion: null });
	};

}
