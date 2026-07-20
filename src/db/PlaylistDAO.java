package db;

import java.util.List;

import entity.Condivisione;
import entity.ElementoMultimediale;
import entity.Playlist;

public interface PlaylistDAO {

	Playlist findById(int codicePlaylist);

	List<Playlist> findByProprietario(int codiceUtente);

	List<Playlist> findCondiviseCon(int codiceUtente);

	List<Playlist> findPubbliche();

	boolean create(Playlist playlist);

	boolean update(Playlist playlist);

	boolean delete(int codicePlaylist);

	List<ElementoMultimediale> findElementi(int codicePlaylist);

	boolean addElemento(int codicePlaylist, int codiceElemento);

	boolean removeElemento(int codicePlaylist, int codiceElemento);

	boolean createCondivisione(Condivisione condivisione);

	boolean deleteCondivisione(int codicePlaylist, int codiceUtente);

	List<Condivisione> findCondivisioni(int codicePlaylist);
}
