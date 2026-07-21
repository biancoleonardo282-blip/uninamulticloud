package entity;

import java.time.Duration;

public class Video extends ElementoMultimediale {

	private int frameRate;
	private String risoluzione;

	public Video() {
		super();
	}

	public Video(int codiceElemento, String titolo, String descrizione, Duration durata, Utente autore,
			int frameRate, String risoluzione) {
		super(codiceElemento, titolo, descrizione, durata, autore);
		this.frameRate = frameRate;
		this.risoluzione = risoluzione;
	}

	@Override
	public String getTipoMedia() {
		return "Video";
	}

	public int getFrameRate() {
		return frameRate;
	}

	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}

	public String getRisoluzione() {
		return risoluzione;
	}

	public void setRisoluzione(String risoluzione) {
		this.risoluzione = risoluzione;
	}
}
