package entity;

import java.time.Duration;
import java.time.LocalDate;

public abstract class ElementoMultimediale {

	private int codiceElemento;
	private String titolo;
	private String descrizione;
	private Duration durata;
	private LocalDate dataCreazione;
	private String percorsoCopertina;
	private Utente autore;
	private StatisticheRiproduzione statistiche;

	public ElementoMultimediale() {
	}

	public ElementoMultimediale(int codiceElemento, String titolo, String descrizione, Duration durata,
			Utente autore) {
		this.codiceElemento = codiceElemento;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.durata = durata;
		this.autore = autore;
		this.dataCreazione = LocalDate.now();
	}

	public abstract String getTipoMedia();

	public int getCodiceElemento() {
		return codiceElemento;
	}

	public void setCodiceElemento(int codiceElemento) {
		this.codiceElemento = codiceElemento;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Duration getDurata() {
		return durata;
	}

	public void setDurata(Duration durata) {
		this.durata = durata;
	}

	public LocalDate getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDate dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public String getPercorsoCopertina() {
		return percorsoCopertina;
	}

	public void setPercorsoCopertina(String percorsoCopertina) {
		this.percorsoCopertina = percorsoCopertina;
	}

	public Utente getAutore() {
		return autore;
	}

	public void setAutore(Utente autore) {
		this.autore = autore;
	}

	public StatisticheRiproduzione getStatistiche() {
		return statistiche;
	}

	public void setStatistiche(StatisticheRiproduzione statistiche) {
		this.statistiche = statistiche;
	}

	public String getDurataFormattata() {
		if (durata == null) {
			return "0:00";
		}
		long secondiTotali = durata.getSeconds();
		return String.format("%d:%02d", secondiTotali / 60, secondiTotali % 60);
	}

	@Override
	public String toString() {
		String nomeAutore = (autore == null) ? "" : autore.getNomeCompleto();
		return titolo + "  ·  " + nomeAutore + "  ·  " + getDurataFormattata();
	}
}
