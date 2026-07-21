import { RouteNode } from "~/router/RouteNode";
import { RouteDataVerbindungen } from "~/router/apps/einstellungen/verbindungen/RouteDataVerbindungen";
import type { RouteApp } from "~/router/apps/RouteApp";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteEinstellungenMenuGroup } from "~/router/apps/einstellungen/RouteEinstellungenMenuGroup";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { api } from "~/router/Api";
import type { VerbindungenProps } from "~/components/einstellungen/verbindungen/VerbindungenProps";

export const Verbindungen = () => import("~/components/einstellungen/verbindungen/VerbindungenApp.vue");

export class RouteVerbindungen extends RouteNode<RouteDataVerbindungen, RouteApp> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.ADMIN],
			"einstellungen.verbindungen",
			"einstellungen/verbindungen",
			Verbindungen,
			new RouteDataVerbindungen());
		super.propHandler = (route) => this.getProps(route);
		super.mode = ServerMode.STABLE;
		super.text = "Schoolboost";
		super.children = [];
		super.menugroup = RouteEinstellungenMenuGroup.VERBINDUNGEN;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await routeVerbindungen.data.ladeDaten();
		}
	}

	public getProps(_: RouteLocationNormalized): VerbindungenProps {
		return {
			verbindung: () => routeVerbindungen.data.verbindung,
			letzteAktion: () => routeVerbindungen.data.letzteAktion,
			pair: routeVerbindungen.data.pair,
			patch: routeVerbindungen.data.patch,
			check: routeVerbindungen.data.check,
			push: routeVerbindungen.data.push,
			disconnect: routeVerbindungen.data.disconnect,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeVerbindungen = new RouteVerbindungen();
