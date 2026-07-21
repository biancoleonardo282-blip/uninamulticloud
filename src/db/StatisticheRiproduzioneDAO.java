package db;

import entity.StatisticheRiproduzione;

public interface StatisticheRiproduzioneDAO {

	StatisticheRiproduzione findByElemento(int codiceElemento);

	boolean create(StatisticheRiproduzione statistiche);

	boolean update(StatisticheRiproduzione statistiche);

	boolean delete(int codiceElemento);

	boolean incrementaAscolti(int codiceElemento);

	boolean incrementaMiPiace(int codiceElemento);

	boolean incrementaCondivisioni(int codiceElemento);
}
