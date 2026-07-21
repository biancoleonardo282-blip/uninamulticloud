package control;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import db.StatisticheRiproduzioneDAO;
import entity.ElementoMultimediale;

public class ControllerRiproduzione {

	private ControllerUtente controllerUtente;
	private StatisticheRiproduzioneDAO statisticheDAO;

	private ElementoMultimediale elementoCorrente;
	private List<ElementoMultimediale> codaRiproduzione = new ArrayList<ElementoMultimediale>();
	private int posizioneInCoda = -1;
	private boolean inRiproduzione;
	private Duration posizioneCorrente = Duration.ZERO;

	public ControllerRiproduzione(ControllerUtente controllerUtente, StatisticheRiproduzioneDAO statisticheDAO) {
		this.controllerUtente = controllerUtente;
		this.statisticheDAO = statisticheDAO;
	}

	public void avviaRiproduzione(ElementoMultimediale elemento) {
		if (elemento == null) {
			return;
		}
		codaRiproduzione = new ArrayList<ElementoMultimediale>();
		codaRiproduzione.add(elemento);
		posizioneInCoda = 0;
		elementoCorrente = elemento;
		posizioneCorrente = Duration.ZERO;
		inRiproduzione = true;
		registraAscolto(elemento.getCodiceElemento());
	}

	public void riproduciPlaylist(List<ElementoMultimediale> elementi, int posizioneIniziale) {
		if (elementi == null || elementi.isEmpty()) {
			return;
		}
		codaRiproduzione = new ArrayList<ElementoMultimediale>(elementi);
		posizioneInCoda = (posizioneIniziale < 0 || posizioneIniziale >= elementi.size()) ? 0 : posizioneIniziale;
		elementoCorrente = codaRiproduzione.get(posizioneInCoda);
		posizioneCorrente = Duration.ZERO;
		inRiproduzione = true;
		registraAscolto(elementoCorrente.getCodiceElemento());
	}

	public void playPausa() {
		if (elementoCorrente == null) {
			return;
		}
		inRiproduzione = !inRiproduzione;
	}

	public void elementoSuccessivo() {
		if (codaRiproduzione.isEmpty() || posizioneInCoda + 1 >= codaRiproduzione.size()) {
			return;
		}
		posizioneInCoda++;
		elementoCorrente = codaRiproduzione.get(posizioneInCoda);
		posizioneCorrente = Duration.ZERO;
		inRiproduzione = true;
		registraAscolto(elementoCorrente.getCodiceElemento());
	}

	public void elementoPrecedente() {
		if (codaRiproduzione.isEmpty() || posizioneInCoda <= 0) {
			return;
		}
		posizioneInCoda--;
		elementoCorrente = codaRiproduzione.get(posizioneInCoda);
		posizioneCorrente = Duration.ZERO;
		inRiproduzione = true;
		registraAscolto(elementoCorrente.getCodiceElemento());
	}

	public void spostaPosizione(Duration nuovaPosizione) {
		if (elementoCorrente == null || nuovaPosizione == null) {
			return;
		}
		Duration durata = elementoCorrente.getDurata();
		if (durata != null && nuovaPosizione.compareTo(durata) > 0) {
			posizioneCorrente = durata;
		} else {
			posizioneCorrente = nuovaPosizione;
		}
	}

	public ElementoMultimediale getElementoCorrente() {
		return elementoCorrente;
	}

	public boolean isInRiproduzione() {
		return inRiproduzione;
	}

	public Duration getPosizioneCorrente() {
		return posizioneCorrente;
	}

	public List<ElementoMultimediale> getCodaRiproduzione() {
		return codaRiproduzione;
	}

	private void registraAscolto(int codiceElemento) {
		if (controllerUtente == null || controllerUtente.getUtenteInSessione() == null) {
			return;
		}
		statisticheDAO.incrementaAscolti(codiceElemento);
	}
}
