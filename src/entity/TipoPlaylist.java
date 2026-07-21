package entity;

public enum TipoPlaylist {

	PRIVATA,
	CONDIVISA,
	PUBBLICA;

	@Override
	public String toString() {
		switch (this) {
			case PRIVATA:
				return "Privata";
			case CONDIVISA:
				return "Condivisa";
			case PUBBLICA:
				return "Pubblica";
			default:
				return super.toString();
		}
	}
}
