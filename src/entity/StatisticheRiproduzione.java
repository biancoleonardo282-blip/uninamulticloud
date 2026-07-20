package entity;

public class StatisticheRiproduzione {

	private int codiceElemento;
	private int numeroAscolti;
	private int numeroCondivisioni;
	private int miPiace;

	public StatisticheRiproduzione() {
	}

	public StatisticheRiproduzione(int codiceElemento, int numeroAscolti, int numeroCondivisioni, int miPiace) {
		this.codiceElemento = codiceElemento;
		this.numeroAscolti = numeroAscolti;
		this.numeroCondivisioni = numeroCondivisioni;
		this.miPiace = miPiace;
	}

	public int getCodiceElemento() {
		return codiceElemento;
	}

	public void setCodiceElemento(int codiceElemento) {
		this.codiceElemento = codiceElemento;
	}

	public int getNumeroAscolti() {
		return numeroAscolti;
	}

	public void setNumeroAscolti(int numeroAscolti) {
		this.numeroAscolti = numeroAscolti;
	}

	public int getNumeroCondivisioni() {
		return numeroCondivisioni;
	}

	public void setNumeroCondivisioni(int numeroCondivisioni) {
		this.numeroCondivisioni = numeroCondivisioni;
	}

	public int getMiPiace() {
		return miPiace;
	}

	public void setMiPiace(int miPiace) {
		this.miPiace = miPiace;
	}
}
