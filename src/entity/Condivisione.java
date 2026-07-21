package entity;

public class Condivisione {

	private Playlist playlist;
	private Utente destinatario;
	private String linkCondivisione;
	private boolean condivisioneAmico;

	public Condivisione() {
	}

	public Condivisione(Playlist playlist, Utente destinatario, String linkCondivisione, boolean condivisioneAmico) {
		this.playlist = playlist;
		this.destinatario = destinatario;
		this.linkCondivisione = linkCondivisione;
		this.condivisioneAmico = condivisioneAmico;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public Utente getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Utente destinatario) {
		this.destinatario = destinatario;
	}

	public String getLinkCondivisione() {
		return linkCondivisione;
	}

	public void setLinkCondivisione(String linkCondivisione) {
		this.linkCondivisione = linkCondivisione;
	}

	public boolean isCondivisioneAmico() {
		return condivisioneAmico;
	}

	public void setCondivisioneAmico(boolean condivisioneAmico) {
		this.condivisioneAmico = condivisioneAmico;
	}
}
