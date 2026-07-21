package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Condivisione;
import entity.ElementoMultimediale;
import entity.Playlist;
import entity.TipoPlaylist;
import entity.Utente;


public class PlaylistDAOImpl implements PlaylistDAO {

	private Connection connessione;

	private ElementoMultimedialeDAO elementoDAO;
	private UtenteDAO utenteDAO;

	public PlaylistDAOImpl() {
		try {
			connessione = ConnessioneDatabase.getIstanza().getConnessione();
		} catch (SQLException e) {
			System.err.println("Connessione al database non riuscita: " + e.getMessage());
		}
		elementoDAO = new ElementoMultimedialeDAOImpl();
		utenteDAO = new UtenteDAOImpl();
	}

	private static final String SELECT_BASE = "SELECT p.*, u.username, u.nome AS nome_proprietario, u.cognome AS cognome_proprietario "
			+ "FROM playlist p JOIN utente u ON p.codiceutente = u.codiceutente ";

	@Override
	public Playlist findById(int codicePlaylist) {
		String sql = SELECT_BASE + "WHERE p.id_playlist = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codicePlaylist);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mappaRisultato(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("findById(Playlist): " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Playlist> findByProprietario(int codiceUtente) {
		List<Playlist> risultati = new ArrayList<Playlist>();
		String sql = SELECT_BASE + "WHERE p.codiceutente = ? ORDER BY p.nome";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceUtente);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					risultati.add(mappaRisultato(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("findByProprietario: " + e.getMessage());
		}
		return risultati;
	}

	@Override
	public List<Playlist> findCondiviseCon(int codiceUtente) {
		List<Playlist> risultati = new ArrayList<Playlist>();
		String sql = "SELECT p.*, u.username, u.nome AS nome_proprietario, u.cognome AS cognome_proprietario "
				+ "FROM playlist p JOIN utente u ON p.codiceutente = u.codiceutente "
				+ "JOIN condivisione c ON c.id_playlist = p.id_playlist "
				+ "WHERE c.codiceutente = ? ORDER BY p.nome";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceUtente);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					risultati.add(mappaRisultato(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("findCondiviseCon: " + e.getMessage());
		}
		return risultati;
	}

	@Override
	public List<Playlist> findPubbliche() {
		List<Playlist> risultati = new ArrayList<Playlist>();
		String sql = SELECT_BASE + "WHERE p.ispubblica = TRUE ORDER BY p.nome";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					risultati.add(mappaRisultato(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("findPubbliche: " + e.getMessage());
		}
		return risultati;
	}

	@Override
	public boolean create(Playlist playlist) {
		String sql = "INSERT INTO playlist (nome, descrizione, ispubblica, iscondivisa, isprivata, tagricerca, "
				+ "copertina, datacreazione, codiceutente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connessione.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			TipoPlaylist tipo = (playlist.getTipo() == null) ? TipoPlaylist.PRIVATA : playlist.getTipo();
			ps.setString(1, playlist.getNome());
			ps.setString(2, playlist.getDescrizione());
			ps.setBoolean(3, tipo == TipoPlaylist.PUBBLICA);
			ps.setBoolean(4, tipo == TipoPlaylist.CONDIVISA);
			ps.setBoolean(5, tipo == TipoPlaylist.PRIVATA);
			ps.setString(6, playlist.getTagRicerca());
			ps.setString(7, playlist.getPercorsoCopertina());
			LocalDate data = (playlist.getDataCreazione() == null) ? LocalDate.now() : playlist.getDataCreazione();
			ps.setDate(8, Date.valueOf(data));
			ps.setInt(9, playlist.getProprietario() == null ? 0 : playlist.getProprietario().getCodiceUtente());

			if (ps.executeUpdate() == 0) {
				return false;
			}
			try (ResultSet chiavi = ps.getGeneratedKeys()) {
				if (chiavi.next()) {
					playlist.setCodicePlaylist(chiavi.getInt(1));
				}
			}
			return true;
		} catch (SQLException e) {
			System.err.println("create(Playlist): " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean update(Playlist playlist) {
		String sql = "UPDATE playlist SET nome = ?, descrizione = ?, ispubblica = ?, iscondivisa = ?, isprivata = ?, "
				+ "tagricerca = ?, copertina = ? WHERE id_playlist = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			TipoPlaylist tipo = (playlist.getTipo() == null) ? TipoPlaylist.PRIVATA : playlist.getTipo();
			ps.setString(1, playlist.getNome());
			ps.setString(2, playlist.getDescrizione());
			ps.setBoolean(3, tipo == TipoPlaylist.PUBBLICA);
			ps.setBoolean(4, tipo == TipoPlaylist.CONDIVISA);
			ps.setBoolean(5, tipo == TipoPlaylist.PRIVATA);
			ps.setString(6, playlist.getTagRicerca());
			ps.setString(7, playlist.getPercorsoCopertina());
			ps.setInt(8, playlist.getCodicePlaylist());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("update(Playlist): " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean delete(int codicePlaylist) {
		String sql = "DELETE FROM playlist WHERE id_playlist = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codicePlaylist);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("delete(Playlist): " + e.getMessage());
			return false;
		}
	}

	@Override
	public List<ElementoMultimediale> findElementi(int codicePlaylist) {
		List<ElementoMultimediale> risultati = new ArrayList<ElementoMultimediale>();
		String sql = "SELECT id_elemento FROM includeelemento WHERE id_playlist = ? ORDER BY id_elemento";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codicePlaylist);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ElementoMultimediale elemento = elementoDAO.findById(rs.getInt("id_elemento"));
					if (elemento != null) {
						risultati.add(elemento);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("findElementi: " + e.getMessage());
		}
		return risultati;
	}

	@Override
	public boolean addElemento(int codicePlaylist, int codiceElemento) {
		String sql = "INSERT INTO includeelemento (id_playlist, id_elemento) VALUES (?, ?)";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codicePlaylist);
			ps.setInt(2, codiceElemento);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("addElemento: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean removeElemento(int codicePlaylist, int codiceElemento) {
		String sql = "DELETE FROM includeelemento WHERE id_playlist = ? AND id_elemento = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codicePlaylist);
			ps.setInt(2, codiceElemento);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("removeElemento: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean createCondivisione(Condivisione condivisione) {
		String sql = "INSERT INTO condivisione (codiceutente, id_playlist, linkcondivisione, condivisioneamico) "
				+ "VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, condivisione.getDestinatario() == null ? 0 : condivisione.getDestinatario().getCodiceUtente());
			ps.setInt(2, condivisione.getPlaylist() == null ? 0 : condivisione.getPlaylist().getCodicePlaylist());
			ps.setString(3, condivisione.getLinkCondivisione());
			ps.setBoolean(4, condivisione.isCondivisioneAmico());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("createCondivisione: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteCondivisione(int codicePlaylist, int codiceUtente) {
		String sql = "DELETE FROM condivisione WHERE id_playlist = ? AND codiceutente = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codicePlaylist);
			ps.setInt(2, codiceUtente);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("deleteCondivisione: " + e.getMessage());
			return false;
		}
	}

	@Override
	public List<Condivisione> findCondivisioni(int codicePlaylist) {
		List<Condivisione> risultati = new ArrayList<Condivisione>();
		String sql = "SELECT codiceutente, linkcondivisione, condivisioneamico FROM condivisione WHERE id_playlist = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codicePlaylist);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Condivisione condivisione = new Condivisione();
					condivisione.setDestinatario(utenteDAO.findById(rs.getInt("codiceutente")));
					condivisione.setLinkCondivisione(rs.getString("linkcondivisione"));
					condivisione.setCondivisioneAmico(rs.getBoolean("condivisioneamico"));
					risultati.add(condivisione);
				}
			}
		} catch (SQLException e) {
			System.err.println("findCondivisioni: " + e.getMessage());
		}
		return risultati;
	}

	private Playlist mappaRisultato(ResultSet rs) throws SQLException {
		Utente proprietario = new Utente();
		proprietario.setCodiceUtente(rs.getInt("codiceutente"));
		proprietario.setUsername(rs.getString("username"));
		proprietario.setNome(rs.getString("nome_proprietario"));
		proprietario.setCognome(rs.getString("cognome_proprietario"));

		Playlist playlist = new Playlist();
		playlist.setCodicePlaylist(rs.getInt("id_playlist"));
		playlist.setNome(rs.getString("nome"));
		playlist.setDescrizione(rs.getString("descrizione"));

		if (rs.getBoolean("ispubblica")) {
			playlist.setTipo(TipoPlaylist.PUBBLICA);
		} else if (rs.getBoolean("iscondivisa")) {
			playlist.setTipo(TipoPlaylist.CONDIVISA);
		} else {
			playlist.setTipo(TipoPlaylist.PRIVATA);
		}

		playlist.setTagRicerca(rs.getString("tagricerca"));
		playlist.setPercorsoCopertina(rs.getString("copertina"));
		playlist.setNumeroBrani(rs.getInt("numerobrani"));

		Date dataCreazione = rs.getDate("datacreazione");
		if (dataCreazione != null) {
			playlist.setDataCreazione(dataCreazione.toLocalDate());
		}
		playlist.setProprietario(proprietario);
		return playlist;
	}
}
