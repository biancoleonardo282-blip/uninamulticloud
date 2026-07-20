package control;

import java.util.ArrayList;
import java.util.List;

import db.UtenteDAO;
import entity.CampoProfilo;
import entity.Utente;

public class ControllerUtente {

	private Utente utenteInSessione;
	private UtenteDAO utenteDAO;

	public ControllerUtente(UtenteDAO utenteDAO) {
		this.utenteDAO = utenteDAO;
	}

	public boolean effettuaLogin(String username, String password) {
		if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
			return false;
		}
		Utente trovato = utenteDAO.verificaCredenziali(username.trim(), password);
		if (trovato == null) {
			return false;
		}
		utenteInSessione = trovato;
		return true;
	}

	public boolean registraUtente(String username, String password, String nome, String cognome, String email) {
		if (!verificaUsernameDisponibile(username) || !verificaEmailDisponibile(email)
				|| !formatoEmailValido(email)) {
			return false;
		}
		Utente nuovo = new Utente(0, username.trim(), email.trim(), password, nome, cognome);
		return utenteDAO.create(nuovo);
	}

	public void eseguiLogout() {
		utenteInSessione = null;
	}

	public Utente getUtenteInSessione() {
		return utenteInSessione;
	}

	public boolean verificaUsernameDisponibile(String username) {
		if (username == null || username.trim().isEmpty()) {
			return false;
		}
		return !utenteDAO.checkEsistenzaUsername(username.trim());
	}

	public boolean verificaEmailDisponibile(String email) {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		return !utenteDAO.checkEsistenzaEmail(email.trim());
	}

	// deve rispettare lo stesso pattern del CHECK sul database
	public boolean formatoEmailValido(String email) {
		return email != null && email.matches(".+@.+\\..+");
	}

	public boolean aggiornaDatoProfilo(CampoProfilo campo, String nuovoValore) {
		if (utenteInSessione == null || campo == null || nuovoValore == null || nuovoValore.trim().isEmpty()) {
			return false;
		}
		String valore = nuovoValore.trim();

		switch (campo) {
			case USERNAME:
				if (!verificaUsernameDisponibile(valore)) {
					return false;
				}
				utenteInSessione.setUsername(valore);
				break;
			case NOME:
				utenteInSessione.setNome(valore);
				break;
			case COGNOME:
				utenteInSessione.setCognome(valore);
				break;
			case EMAIL:
				if (!formatoEmailValido(valore) || !verificaEmailDisponibile(valore)) {
					return false;
				}
				utenteInSessione.setEmail(valore);
				break;
			case PASSWORD:
				utenteInSessione.setPassword(valore);
				break;
			default:
				return false;
		}
		return utenteDAO.update(utenteInSessione);
	}

	public List<Utente> cercaUtenti(String chiaveRicerca) {
		List<Utente> risultati = utenteDAO.findByChiaveRicerca(chiaveRicerca);
		if (utenteInSessione != null) {
			List<Utente> filtrati = new ArrayList<Utente>();
			for (Utente utente : risultati) {
				if (utente.getCodiceUtente() != utenteInSessione.getCodiceUtente()) {
					filtrati.add(utente);
				}
			}
			return filtrati;
		}
		return risultati;
	}

	public boolean aggiungiAmico(String usernameAmico) {
		if (utenteInSessione == null) {
			return false;
		}
		Utente amico = utenteDAO.findByUsername(usernameAmico);
		if (amico == null || amico.getCodiceUtente() == utenteInSessione.getCodiceUtente()) {
			return false;
		}
		for (Utente giaAmico : getListaAmici()) {
			if (giaAmico.getCodiceUtente() == amico.getCodiceUtente()) {
				return false;
			}
		}
		return utenteDAO.createAmicizia(utenteInSessione.getCodiceUtente(), amico.getCodiceUtente());
	}

	public boolean rimuoviAmico(int codiceAmico) {
		if (utenteInSessione == null) {
			return false;
		}
		return utenteDAO.deleteAmicizia(utenteInSessione.getCodiceUtente(), codiceAmico);
	}

	public List<Utente> getListaAmici() {
		if (utenteInSessione == null) {
			return new ArrayList<Utente>();
		}
		return utenteDAO.findAmici(utenteInSessione.getCodiceUtente());
	}
}
