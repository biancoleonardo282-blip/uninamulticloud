package db;

import java.util.List;

import entity.Brano;
import entity.ElementoMultimediale;
import entity.Video;

public interface ElementoMultimedialeDAO {

	ElementoMultimediale findById(int codiceElemento);

	List<ElementoMultimediale> findByTitolo(String chiave);

	List<ElementoMultimediale> findByAutore(int codiceUtente);

	List<ElementoMultimediale> findRaccomandati(int codiceUtente, int limite);

	boolean createBrano(Brano brano);

	boolean createVideo(Video video);

	boolean update(ElementoMultimediale elemento);

	boolean delete(int codiceElemento);
}
