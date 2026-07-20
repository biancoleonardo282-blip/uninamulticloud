package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Brano;
import entity.ElementoMultimediale;
import entity.Utente;
import entity.Video;

public class ElementoMultimedialeDAOImpl implements ElementoMultimedialeDAO {

	private Connection connessione;

	public ElementoMultimedialeDAOImpl() {
		try {
			connessione = ConnessioneDatabase.getIstanza().getConnessione();
		} catch (SQLException e) {
			System.err.println("Connessione al database non riuscita: " + e.getMessage());
		}
	}

	private static final String SELECT_BASE = "SELECT e.*, u.username, u.nome AS nome_autore, u.cognome AS cognome_autore "
			+ "FROM elementomultimediale e JOIN utente u ON e.codiceutente = u.codiceutente ";

	@Override
	public ElementoMultimediale findById(int codiceElemento) {
		String sql = SELECT_BASE + "WHERE e.id_elemento = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceElemento);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mappaRisultato(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("findById(Elemento): " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<ElementoMultimediale> findByTitolo(String chiave) {
		List<ElementoMultimediale> risultati = new ArrayList<ElementoMultimediale>();
		String sql = SELECT_BASE + "WHERE LOWER(e.titolo) LIKE ? ORDER BY e.titolo";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setString(1, "%" + (chiave == null ? "" : chiave.toLowerCase()) + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					risultati.add(mappaRisultato(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("findByTitolo: " + e.getMessage());
		}
		return risultati;
	}

	@Override
	public List<ElementoMultimediale> findByAutore(int codiceUtente) {
		List<ElementoMultimediale> risultati = new ArrayList<ElementoMultimediale>();
		String sql = SELECT_BASE + "WHERE e.codiceutente = ? ORDER BY e.datacreazione DESC";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceUtente);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					risultati.add(mappaRisultato(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("findByAutore: " + e.getMessage());
		}
		return risultati;
	}

	@Override
	public List<ElementoMultimediale> findRaccomandati(int codiceUtente, int limite) {
		List<ElementoMultimediale> risultati = new ArrayList<ElementoMultimediale>();
		String sql = "SELECT e.*, u.username, u.nome AS nome_autore, u.cognome AS cognome_autore "
				+ "FROM elementomultimediale e "
				+ "JOIN utente u ON e.codiceutente = u.codiceutente "
				+ "LEFT JOIN statisticheriproduzione s ON s.id_elemento = e.id_elemento "
				+ "WHERE e.codiceutente <> ? "
				+ "ORDER BY COALESCE(s.numeroascolti, 0) DESC, e.datacreazione DESC LIMIT ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceUtente);
			ps.setInt(2, limite);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					risultati.add(mappaRisultato(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("findRaccomandati: " + e.getMessage());
		}
		return risultati;
	}

	@Override
	public boolean createBrano(Brano brano) {
		String sql = "INSERT INTO elementomultimediale (titolo, durata, descrizione, testo, datacreazione, "
				+ "immaginecopertina, tipomedia, bitrate, formato, codec, codiceutente) "
				+ "VALUES (?, ?, ?, ?, ?, ?, 'Audio', ?, ?, ?, ?)";
		try (PreparedStatement ps = connessione.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, brano.getTitolo());
			ps.setLong(2, brano.getDurata() == null ? 0L : brano.getDurata().getSeconds());
			ps.setString(3, brano.getDescrizione());
			ps.setString(4, brano.getTesto());
			LocalDate data = (brano.getDataCreazione() == null) ? LocalDate.now() : brano.getDataCreazione();
			ps.setDate(5, Date.valueOf(data));
			ps.setString(6, brano.getPercorsoCopertina());
			ps.setInt(7, brano.getBitRate());
			ps.setString(8, brano.getFormato());
			ps.setString(9, brano.getCodec());
			ps.setInt(10, brano.getAutore() == null ? 0 : brano.getAutore().getCodiceUtente());

			if (ps.executeUpdate() == 0) {
				return false;
			}
			try (ResultSet chiavi = ps.getGeneratedKeys()) {
				if (chiavi.next()) {
					brano.setCodiceElemento(chiavi.getInt(1));
				}
			}
			return true;
		} catch (SQLException e) {
			System.err.println("createBrano: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean createVideo(Video video) {
		String sql = "INSERT INTO elementomultimediale (titolo, durata, descrizione, datacreazione, "
				+ "immaginecopertina, tipomedia, framerate, risoluzione, codiceutente) "
				+ "VALUES (?, ?, ?, ?, ?, 'Video', ?, ?, ?)";
		try (PreparedStatement ps = connessione.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, video.getTitolo());
			ps.setLong(2, video.getDurata() == null ? 0L : video.getDurata().getSeconds());
			ps.setString(3, video.getDescrizione());
			LocalDate data = (video.getDataCreazione() == null) ? LocalDate.now() : video.getDataCreazione();
			ps.setDate(4, Date.valueOf(data));
			ps.setString(5, video.getPercorsoCopertina());
			ps.setInt(6, video.getFrameRate());
			ps.setString(7, video.getRisoluzione());
			ps.setInt(8, video.getAutore() == null ? 0 : video.getAutore().getCodiceUtente());

			if (ps.executeUpdate() == 0) {
				return false;
			}
			try (ResultSet chiavi = ps.getGeneratedKeys()) {
				if (chiavi.next()) {
					video.setCodiceElemento(chiavi.getInt(1));
				}
			}
			return true;
		} catch (SQLException e) {
			System.err.println("createVideo: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean update(ElementoMultimediale elemento) {
		String sql = "UPDATE elementomultimediale SET titolo = ?, durata = ?, descrizione = ?, immaginecopertina = ? "
				+ "WHERE id_elemento = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setString(1, elemento.getTitolo());
			ps.setLong(2, elemento.getDurata() == null ? 0L : elemento.getDurata().getSeconds());
			ps.setString(3, elemento.getDescrizione());
			ps.setString(4, elemento.getPercorsoCopertina());
			ps.setInt(5, elemento.getCodiceElemento());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("update(Elemento): " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean delete(int codiceElemento) {
		String sql = "DELETE FROM elementomultimediale WHERE id_elemento = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceElemento);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("delete(Elemento): " + e.getMessage());
			return false;
		}
	}

	private ElementoMultimediale mappaRisultato(ResultSet rs) throws SQLException {
		Utente autore = new Utente();
		autore.setCodiceUtente(rs.getInt("codiceutente"));
		autore.setUsername(rs.getString("username"));
		autore.setNome(rs.getString("nome_autore"));
		autore.setCognome(rs.getString("cognome_autore"));

		ElementoMultimediale elemento;
		if ("Video".equals(rs.getString("tipomedia"))) {
			Video video = new Video();
			video.setFrameRate(rs.getInt("framerate"));
			video.setRisoluzione(rs.getString("risoluzione"));
			elemento = video;
		} else {
			Brano brano = new Brano();
			brano.setBitRate(rs.getInt("bitrate"));
			brano.setFormato(rs.getString("formato"));
			brano.setCodec(rs.getString("codec"));
			brano.setTesto(rs.getString("testo"));
			elemento = brano;
		}

		elemento.setCodiceElemento(rs.getInt("id_elemento"));
		elemento.setTitolo(rs.getString("titolo"));
		elemento.setDescrizione(rs.getString("descrizione"));
		elemento.setDurata(Duration.ofSeconds(rs.getLong("durata")));

		Date dataCreazione = rs.getDate("datacreazione");
		if (dataCreazione != null) {
			elemento.setDataCreazione(dataCreazione.toLocalDate());
		}
		elemento.setPercorsoCopertina(rs.getString("immaginecopertina"));
		elemento.setAutore(autore);
		return elemento;
	}
}
