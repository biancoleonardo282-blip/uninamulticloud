package entity;

public enum CampoProfilo {

	USERNAME("Nuovo username"),
	NOME("Nuovo nome"),
	COGNOME("Nuovo cognome"),
	EMAIL("Nuova email"),
	PASSWORD("Nuova password");

	private final String etichetta;

	private CampoProfilo(String etichetta) {
		this.etichetta = etichetta;
	}

	public String getEtichetta() {
		return etichetta;
	}

	@Override
	public String toString() {
		return etichetta;
	}
}
