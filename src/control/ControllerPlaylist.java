package control;

import java.util.ArrayList;
import java.util.List;

import db.ElementoMultimedialeDAO;
import db.PlaylistDAO;
import entity.Condivisione;
import entity.ElementoMultimediale;
import entity.Playlist;
import entity.TipoPlaylist;
import entity.Utente;


public class ControllerPlaylist {

	private ControllerUtente controllerUtente;
	private PlaylistDAO playlistDAO;
	private ElementoMultimedialeDAO elementoDAO;

	public ControllerPlaylist(ControllerUtente controllerUtente, PlaylistDAO playlistDAO,
			ElementoMultimedialeDAO elementoDAO) {
		this.controllerUtente = controllerUtente;
		this.playlistDAO = playlistDAO;
		this.elementoDAO = elementoDAO;
	}

	public boolean creaNuovaPlaylist(String nome, String descrizione, TipoPlaylist tipo, String tagRicerca) {
		Utente proprietario = controllerUtente.getUtenteInSessione();
		if (proprietario == null || nome == null || nome.trim().isEmpty()) {
			return false;
		}
		if (tipo == TipoPlaylist.PUBBLICA && (tagRicerca == null || tagRicerca.trim().isEmpty())) {
			return false;
		}
		Playlist playlist = new Playlist(0, nome.trim(), descrizione, tipo, tagRicerca, proprietario);
		return playlistDAO.create(playlist);
	}

	public boolean aggiornaDettagliPlaylist(int codicePlaylist, String nome, String descrizione, TipoPlaylist tipo,
			String tagRicerca) {
		if (!puoModificare(codicePlaylist)) {
			return false;
		}
		if (tipo == TipoPlaylist.PUBBLICA && (tagRicerca == null || tagRicerca.trim().isEmpty())) {
			return false;
		}
		Playlist playlist = playlistDAO.findById(codicePlaylist);
		if (playlist == null) {
			return false;
		}
		playlist.setNome(nome);
		playlist.setDescrizione(descrizione);
		playlist.setTipo(tipo);
		playlist.setTagRicerca(tagRicerca);
		return playlistDAO.update(playlist);
	}

	public boolean eliminaPlaylist(int codicePlaylist) {
		Playlist playlist = playlistDAO.findById(codicePlaylist);
		Utente utente = controllerUtente.getUtenteInSessione();
		if (playlist == null || utente == null || playlist.getProprietario() == null
				|| playlist.getProprietario().getCodiceUtente() != utente.getCodiceUtente()) {
			return false;
		}
		return playlistDAO.delete(codicePlaylist);
	}

	public boolean salvaPlaylistPubblica(int codicePlaylist) {
		Playlist origine = playlistDAO.findById(codicePlaylist);
		Utente utente = controllerUtente.getUtenteInSessione();
		if (origine == null || utente == null || origine.getTipo() != TipoPlaylist.PUBBLICA) {
			return false;
		}
		Playlist copia = new Playlist(0, origine.getNome(), origine.getDescrizione(), TipoPlaylist.PRIVATA,
				origine.getTagRicerca(), utente);
		if (!playlistDAO.create(copia)) {
			return false;
		}
		for (ElementoMultimediale elemento : playlistDAO.findElementi(codicePlaylist)) {
			playlistDAO.addElemento(copia.getCodicePlaylist(), elemento.getCodiceElemento());
		}
		return true;
	}

	public List<Playlist> getLeMiePlaylist() {
		Utente utente = controllerUtente.getUtenteInSessione();
		if (utente == null) {
			return new ArrayList<Playlist>();
		}
		return playlistDAO.findByProprietario(utente.getCodiceUtente());
	}

	public List<Playlist> getPlaylistCondivise() {
		Utente utente = controllerUtente.getUtenteInSessione();
		if (utente == null) {
			return new ArrayList<Playlist>();
		}
		return playlistDAO.findCondiviseCon(utente.getCodiceUtente());
	}

	public List<Playlist> getPlaylistPubbliche() {
		return playlistDAO.findPubbliche();
	}

	public List<ElementoMultimediale> getElementiPlaylist(int codicePlaylist) {
		return playlistDAO.findElementi(codicePlaylist);
	}

	public boolean aggiungiElementoAPlaylist(int codicePlaylist, int codiceElemento) {
		if (!puoModificare(codicePlaylist) || elementoDAO.findById(codiceElemento) == null) {
			return false;
		}
		return playlistDAO.addElemento(codicePlaylist, codiceElemento);
	}

	public boolean rimuoviElementoDaPlaylist(int codicePlaylist, int codiceElemento) {
		if (!puoModificare(codicePlaylist)) {
			return false;
		}
		return playlistDAO.removeElemento(codicePlaylist, codiceElemento);
	}

	// il trigger sul database blocca l'INSERT in condivisione se la playlist non e' gia' "Condivisa": la promuoviamo qui
	public boolean condividiPlaylist(int codicePlaylist, String usernameAmico) {
		Playlist playlist = playlistDAO.findById(codicePlaylist);
		Utente utente = controllerUtente.getUtenteInSessione();
		if (playlist == null || utente == null || playlist.getProprietario() == null
				|| playlist.getProprietario().getCodiceUtente() != utente.getCodiceUtente()) {
			return false;
		}
		Utente destinatario = null;
		for (Utente amico : controllerUtente.getListaAmici()) {
			if (amico.getUsername() != null && amico.getUsername().equalsIgnoreCase(usernameAmico)) {
				destinatario = amico;
				break;
			}
		}
		if (destinatario == null) {
			return false;
		}
		for (Condivisione esistente : getCollaboratori(codicePlaylist)) {
			if (esistente.getDestinatario() != null
					&& esistente.getDestinatario().getCodiceUtente() == destinatario.getCodiceUtente()) {
				return false;
			}
		}
		if (playlist.getTipo() != TipoPlaylist.CONDIVISA) {
			playlist.setTipo(TipoPlaylist.CONDIVISA);
			if (!playlistDAO.update(playlist)) {
				return false;
			}
		}
		String link = "https://uninamulticloud.local/p/" + codicePlaylist + "-" + destinatario.getCodiceUtente();
		return playlistDAO.createCondivisione(new Condivisione(playlist, destinatario, link, true));
	}

	public boolean revocaCondivisione(int codicePlaylist, int codiceUtente) {
		Playlist playlist = playlistDAO.findById(codicePlaylist);
		Utente utente = controllerUtente.getUtenteInSessione();
		if (playlist == null || utente == null || playlist.getProprietario() == null
				|| playlist.getProprietario().getCodiceUtente() != utente.getCodiceUtente()) {
			return false;
		}
		return playlistDAO.deleteCondivisione(codicePlaylist, codiceUtente);
	}

	public List<Condivisione> getCollaboratori(int codicePlaylist) {
		return playlistDAO.findCondivisioni(codicePlaylist);
	}

	public boolean puoModificare(int codicePlaylist) {
		Utente utente = controllerUtente.getUtenteInSessione();
		Playlist playlist = playlistDAO.findById(codicePlaylist);
		if (utente == null || playlist == null || playlist.getProprietario() == null) {
			return false;
		}
		return playlist.getProprietario().getCodiceUtente() == utente.getCodiceUtente();
	}
}
