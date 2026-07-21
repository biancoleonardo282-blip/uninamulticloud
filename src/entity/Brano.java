package entity;

import java.time.Duration;

public class Brano extends ElementoMultimediale {

	private int bitRate;
	private String formato;
	private String codec;
	private String testo;

	public Brano() {
		super();
	}

	public Brano(int codiceElemento, String titolo, String descrizione, Duration durata, Utente autore, int bitRate,
			String formato, String codec, String testo) {
		super(codiceElemento, titolo, descrizione, durata, autore);
		this.bitRate = bitRate;
		this.formato = formato;
		this.codec = codec;
		this.testo = testo;
	}

	@Override
	public String getTipoMedia() {
		return "Audio";
	}

	public int getBitRate() {
		return bitRate;
	}

	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}
}
