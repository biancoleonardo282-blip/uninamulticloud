package control;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import db.ElementoMultimedialeDAO;
import db.StatisticheRiproduzioneDAO;
import entity.Brano;
import entity.ElementoMultimediale;
import entity.StatisticheRiproduzione;
import entity.Utente;
import entity.Video;

public class ControllerMedia {

	private static final int LIMITE_RACCOMANDATI = 12;

	private ControllerUtente controllerUtente;
	private ElementoMultimedialeDAO elementoDAO;
	private StatisticheRiproduzioneDAO statisticheDAO;

	public ControllerMedia(ControllerUtente controllerUtente, ElementoMultimedialeDAO elementoDAO,
			StatisticheRiproduzioneDAO statisticheDAO) {
		this.controllerUtente = controllerUtente;
		this.elementoDAO = elementoDAO;
		this.statisticheDAO = statisticheDAO;
	}

	public boolean pubblicaBrano(String titolo, String descrizione, Duration durata, String percorsoCopertina,
			int bitRate, String formato, String codec, String testo) {
		Utente autore = controllerUtente.getUtenteInSessione();
		if (autore == null || titolo == null || titolo.trim().isEmpty()) {
			return false;
		}
		Brano brano = new Brano(0, titolo.trim(), descrizione, durata, autore, bitRate, formato, codec, testo);
		brano.setPercorsoCopertina(percorsoCopertina);
		return elementoDAO.createBrano(brano);
	}

	public boolean pubblicaVideo(String titolo, String descrizione, Duration durata, String percorsoCopertina,
			int frameRate, String risoluzione) {
		Utente autore = controllerUtente.getUtenteInSessione();
		if (autore == null || titolo == null || titolo.trim().isEmpty()) {
			return false;
		}
		Video video = new Video(0, titolo.trim(), descrizione, durata, autore, frameRate, risoluzione);
		video.setPercorsoCopertina(percorsoCopertina);
		return elementoDAO.createVideo(video);
	}

	public boolean eliminaElemento(int codiceElemento) {
		return elementoDAO.delete(codiceElemento);
	}

	public List<ElementoMultimediale> getElementiRaccomandati() {
		Utente utente = controllerUtente.getUtenteInSessione();
		if (utente == null) {
			return new ArrayList<ElementoMultimediale>();
		}
		return elementoDAO.findRaccomandati(utente.getCodiceUtente(), LIMITE_RACCOMANDATI);
	}

	public List<ElementoMultimediale> cercaElementiPerTitolo(String chiaveRicerca) {
		return elementoDAO.findByTitolo(chiaveRicerca);
	}

	public List<ElementoMultimediale> filtraElementiPerAutore(String usernameAutore) {
		List<ElementoMultimediale> risultati = new ArrayList<ElementoMultimediale>();
		for (ElementoMultimediale elemento : elementoDAO.findByTitolo("")) {
			Utente autore = elemento.getAutore();
			if (autore != null && autore.getUsername() != null && autore.getUsername().equalsIgnoreCase(usernameAutore)) {
				risultati.add(elemento);
			}
		}
		return risultati;
	}

	public StatisticheRiproduzione getStatistiche(int codiceElemento) {
		return statisticheDAO.findByElemento(codiceElemento);
	}

	public boolean registraMiPiace(int codiceElemento) {
		return statisticheDAO.incrementaMiPiace(codiceElemento);
	}
}
