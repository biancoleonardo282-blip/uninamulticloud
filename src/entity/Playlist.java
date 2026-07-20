package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

	private int codicePlaylist;
	private String nome;
	private String descrizione;
	private TipoPlaylist tipo;
	private String tagRicerca;
	private String percorsoCopertina;
	private LocalDate dataCreazione;
	private int numeroBrani;
	private Utente proprietario;

	private List<ElementoMultimediale> elementi = new ArrayList<ElementoMultimediale>();
	private List<Condivisione> condivisioni = new ArrayList<Condivisione>();

	public Playlist() {
	}

	public Playlist(int codicePlaylist, String nome, String descrizione, TipoPlaylist tipo, String tagRicerca,
			Utente proprietario) {
		this.codicePlaylist = codicePlaylist;
		this.nome = nome;
		this.descrizione = descrizione;
		this.tipo = tipo;
		this.tagRicerca = tagRicerca;
		this.proprietario = proprietario;
		this.dataCreazione = LocalDate.now();
	}

	public int getCodicePlaylist() {
		return codicePlaylist;
	}

	public void setCodicePlaylist(int codicePlaylist) {
		this.codicePlaylist = codicePlaylist;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public TipoPlaylist getTipo() {
		return tipo;
	}

	public void setTipo(TipoPlaylist tipo) {
		this.tipo = tipo;
	}

	public String getTagRicerca() {
		return tagRicerca;
	}

	public void setTagRicerca(String tagRicerca) {
		this.tagRicerca = tagRicerca;
	}

	public String getPercorsoCopertina() {
		return percorsoCopertina;
	}

	public void setPercorsoCopertina(String percorsoCopertina) {
		this.percorsoCopertina = percorsoCopertina;
	}

	public LocalDate getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDate dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Utente getProprietario() {
		return proprietario;
	}

	public void setProprietario(Utente proprietario) {
		this.proprietario = proprietario;
	}

	public List<ElementoMultimediale> getElementi() {
		return elementi;
	}

	public void setElementi(List<ElementoMultimediale> elementi) {
		this.elementi = elementi;
	}

	public List<Condivisione> getCondivisioni() {
		return condivisioni;
	}

	public void setCondivisioni(List<Condivisione> condivisioni) {
		this.condivisioni = condivisioni;
	}

	// mantenuto dal trigger sul database, non calcolato da elementi.size()
	public int getNumeroBrani() {
		return numeroBrani;
	}

	public void setNumeroBrani(int numeroBrani) {
		this.numeroBrani = numeroBrani;
	}

	@Override
	public String toString() {
		return nome;
	}
}
