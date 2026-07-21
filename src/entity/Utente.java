package entity;

import java.util.ArrayList;
import java.util.List;

public class Utente {

	private int codiceUtente;
	private String username;
	private String email;
	private String password;
	private String nome;
	private String cognome;

	private List<Utente> amici = new ArrayList<Utente>();
	private List<Playlist> playlistCreate = new ArrayList<Playlist>();
	private List<ElementoMultimediale> elementiCaricati = new ArrayList<ElementoMultimediale>();
	private List<Condivisione> condivisioniRicevute = new ArrayList<Condivisione>();

	public Utente() {
	}

	public Utente(int codiceUtente, String username, String email, String password, String nome, String cognome) {
		this.codiceUtente = codiceUtente;
		this.username = username;
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
	}

	public int getCodiceUtente() {
		return codiceUtente;
	}

	public void setCodiceUtente(int codiceUtente) {
		this.codiceUtente = codiceUtente;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public List<Utente> getAmici() {
		return amici;
	}

	public void setAmici(List<Utente> amici) {
		this.amici = amici;
	}

	public List<Playlist> getPlaylistCreate() {
		return playlistCreate;
	}

	public void setPlaylistCreate(List<Playlist> playlistCreate) {
		this.playlistCreate = playlistCreate;
	}

	public List<ElementoMultimediale> getElementiCaricati() {
		return elementiCaricati;
	}

	public void setElementiCaricati(List<ElementoMultimediale> elementiCaricati) {
		this.elementiCaricati = elementiCaricati;
	}

	public List<Condivisione> getCondivisioniRicevute() {
		return condivisioniRicevute;
	}

	public void setCondivisioniRicevute(List<Condivisione> condivisioniRicevute) {
		this.condivisioniRicevute = condivisioniRicevute;
	}

	public String getNomeCompleto() {
		String parteNome = (nome == null) ? "" : nome;
		String parteCognome = (cognome == null) ? "" : cognome;
		return (parteNome + " " + parteCognome).trim();
	}

	@Override
	public String toString() {
		String completo = getNomeCompleto();
		if (completo.isEmpty()) {
			return (username == null) ? "" : username;
		}
		return completo;
	}
}
