package db;

import java.util.List;

import entity.Utente;

public interface UtenteDAO {

	Utente findById(int codiceUtente);

	Utente findByUsername(String username);

	List<Utente> findByChiaveRicerca(String chiave);

	boolean create(Utente utente);

	boolean update(Utente utente);

	boolean delete(int codiceUtente);

	boolean checkEsistenzaUsername(String username);

	boolean checkEsistenzaEmail(String email);

	Utente verificaCredenziali(String username, String password);

	List<Utente> findAmici(int codiceUtente);

	boolean createAmicizia(int codiceUtente, int codiceAmico);

	boolean deleteAmicizia(int codiceUtente, int codiceAmico);
}
