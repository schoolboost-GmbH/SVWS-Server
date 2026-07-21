<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>Schoolboost</span>
					</h2>
				</div>
			</div>
		</header>
		<div class="page page-grid-cards">
			<svws-ui-content-card title="Verbindung">
				<svws-ui-input-wrapper :grid="2">
					<div class="col-span-full mb-8">
						<template v-if="verbindung().istVerbunden">
							<svws-ui-badge type="success">
								Verbunden mit Schule {{ verbindung().schoolId }}
							</svws-ui-badge>
							<p class="text-ui-secondary mt-3">
								Die Schule ist mit Schoolboost gekoppelt. Welche Daten übertragen werden, legen Sie
								unter Synchronisation fest. Mit einem neuen Pairing-Code können Sie die Verbindung erneuern.
							</p>
						</template>
						<template v-else>
							<p>
								Koppeln Sie Ihre Schule mit Schoolboost, um Schüler-, Klassen- und Lehrkraftdaten
								automatisch zu übertragen.
							</p>
							<p class="text-ui-secondary mt-3">
								Erzeugen Sie dazu im Schoolboost-Dashboard unter <span class="font-bold">Verwaltung → Verbindungen</span>
								einen Pairing-Code und tragen Sie ihn zusammen mit der Adresse Ihrer Schoolboost-Instanz unten ein.
								Der Code ist nur einmal gültig.
							</p>
						</template>
					</div>
					<svws-ui-text-input placeholder="Schoolboost-URL" class="contentFocusField"
						:model-value="appUrl" @update:model-value="value => appUrl = String(value ?? '')" />
					<svws-ui-text-input placeholder="Pairing-Code"
						:model-value="pairingCode" @update:model-value="value => pairingCode = String(value ?? '')" />
					<div class="col-span-full mt-8 flex gap-2">
						<svws-ui-button type="primary" :disabled="istBeschaeftigt || (pairingCode.trim() === '') || (appUrl.trim() === '')"
							@click="verbindungHerstellen">
							{{ verbindung().istVerbunden ? "Verbindung erneuern" : "Verbindung herstellen" }}
						</svws-ui-button>
						<svws-ui-button v-if="verbindung().istVerbunden" type="danger" :disabled="istBeschaeftigt"
							@click="zeigeTrennenDialog = true">
							Verbindung trennen
						</svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-modal v-model:show="zeigeTrennenDialog" size="small">
				<template #modalTitle>
					Verbindung trennen
				</template>
				<template #modalContent>
					Der hinterlegte Zugriffsschlüssel wird verworfen, es werden keine Daten mehr an Schoolboost übertragen.
					Die Schoolboost-URL und die Synchronisations-Optionen bleiben erhalten. Zum erneuten Verbinden benötigen
					Sie einen neuen Pairing-Code.
				</template>
				<template #modalActions>
					<svws-ui-button type="secondary" @click="zeigeTrennenDialog = false">Abbrechen</svws-ui-button>
					<svws-ui-button type="danger" :disabled="istBeschaeftigt" @click="verbindungTrennen">Verbindung trennen</svws-ui-button>
				</template>
			</svws-ui-modal>
			<svws-ui-content-card v-if="verbindung().istVerbunden" title="Synchronisation">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-checkbox type="toggle"
						:model-value="verbindung().syncSchueler"
						@update:model-value="value => void patch({ syncSchueler: value === true })">
						Schüler übertragen
					</svws-ui-checkbox>
					<svws-ui-checkbox type="toggle"
						:model-value="verbindung().syncKlassen"
						@update:model-value="value => void patch({ syncKlassen: value === true })">
						Klassen übertragen
					</svws-ui-checkbox>
					<svws-ui-checkbox type="toggle"
						:model-value="verbindung().syncLehrer"
						@update:model-value="value => void patch({ syncLehrer: value === true })">
						Lehrkräfte übertragen
					</svws-ui-checkbox>
					<div class="col-span-full flex gap-2">
						<svws-ui-button type="secondary" :disabled="istBeschaeftigt" @click="verbindungTesten">
							Verbindung testen
						</svws-ui-button>
						<svws-ui-button type="primary" :disabled="istBeschaeftigt" @click="synchronisieren">
							Jetzt synchronisieren
						</svws-ui-button>
					</div>
					<div v-if="fehlermeldung !== null" class="col-span-full text-ui-danger">
						{{ fehlermeldung }}
					</div>
					<div v-if="letzteAktion() !== null" class="col-span-full">
						<div class="font-bold">
							{{ letzteAktion()?.success === true ? "Erfolgreich abgeschlossen" : "Fehlgeschlagen" }}
						</div>
						<div class="font-mono text-sm whitespace-pre-wrap">
							<div v-for="(zeile, index) in logZeilen" :key="index">{{ zeile }}</div>
						</div>
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { VerbindungenProps } from "~/components/einstellungen/verbindungen/VerbindungenProps";
	import { computed, ref, watchEffect } from "vue";

	const props = defineProps<VerbindungenProps>();

	const appUrl = ref<string>("https://app.schoolboost.de");
	const pairingCode = ref<string>("");
	const istBeschaeftigt = ref<boolean>(false);
	const fehlermeldung = ref<string | null>(null);
	const zeigeTrennenDialog = ref<boolean>(false);

	watchEffect(() => {
		const url = props.verbindung().appUrl;
		if (url !== "") {
			appUrl.value = url;
		}
	});

	const logZeilen = computed<string[]>(() => {
		const aktion = props.letzteAktion();
		if (aktion === null) {
			return [];
		}
		const result: string[] = [];
		for (const zeile of aktion.log) {
			result.push(zeile);
		}
		return result;
	});

	async function fuehreAus(aktion: () => Promise<void>, meldung: string) {
		istBeschaeftigt.value = true;
		fehlermeldung.value = null;
		try {
			await aktion();
		} catch {
			fehlermeldung.value = meldung;
		} finally {
			istBeschaeftigt.value = false;
		}
	}

	async function verbindungHerstellen() {
		await fuehreAus(async () => {
			await props.pair(pairingCode.value.trim(), appUrl.value.trim());
			pairingCode.value = "";
		}, "Die Verbindung konnte nicht hergestellt werden. Bitte prüfen Sie den Pairing-Code und die Schoolboost-URL.");
	}

	async function verbindungTesten() {
		await fuehreAus(() => props.check(), "Der Verbindungstest ist fehlgeschlagen.");
	}

	async function synchronisieren() {
		await fuehreAus(() => props.push(), "Die Synchronisation ist fehlgeschlagen.");
	}

	async function verbindungTrennen() {
		await fuehreAus(() => props.disconnect(), "Die Verbindung konnte nicht getrennt werden.");
		zeigeTrennenDialog.value = false;
	}

</script>
