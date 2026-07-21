package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Utente;

public class UtenteDAOImpl implements UtenteDAO {

	private Connection connessione;

	public UtenteDAOImpl() {
		try {
			connessione = ConnessioneDatabase.getIstanza().getConnessione();
		} catch (SQLException e) {
			System.err.println("Connessione al database non riuscita: " + e.getMessage());
		}
	}

	@Override
	public Utente findById(int codiceUtente) {
		String sql = "SELECT * FROM utente WHERE codiceutente = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceUtente);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mappaRisultato(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("findById(Utente): " + e.getMessage());
		}
		return null;
	}

	@Override
	public Utente findByUsername(String username) {
		String sql = "SELECT * FROM utente WHERE username = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mappaRisultato(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("findByUsername: " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Utente> findByChiaveRicerca(String chiave) {
		List<Utente> risultati = new ArrayList<Utente>();
		String sql = "SELECT * FROM utente WHERE LOWER(username) LIKE ? OR LOWER(nome) LIKE ? OR LOWER(cognome) LIKE ? ORDER BY username";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			String filtro = "%" + (chiave == null ? "" : chiave.toLowerCase()) + "%";
			ps.setString(1, filtro);
			ps.setString(2, filtro);
			ps.setString(3, filtro);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					risultati.add(mappaRisultato(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("findByChiaveRicerca: " + e.getMessage());
		}
		return risultati;
	}

	@Override
	public boolean create(Utente utente) {
		String sql = "INSERT INTO utente (username, email, password, nome, cognome) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connessione.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, utente.getUsername());
			ps.setString(2, utente.getEmail());
			ps.setString(3, utente.getPassword());
			ps.setString(4, utente.getNome());
			ps.setString(5, utente.getCognome());

			if (ps.executeUpdate() == 0) {
				return false;
			}
			try (ResultSet chiavi = ps.getGeneratedKeys()) {
				if (chiavi.next()) {
					utente.setCodiceUtente(chiavi.getInt(1));
				}
			}
			return true;
		} catch (SQLException e) {
			System.err.println("create(Utente): " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean update(Utente utente) {
		String sql = "UPDATE utente SET username = ?, email = ?, password = ?, nome = ?, cognome = ? WHERE codiceutente = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setString(1, utente.getUsername());
			ps.setString(2, utente.getEmail());
			ps.setString(3, utente.getPassword());
			ps.setString(4, utente.getNome());
			ps.setString(5, utente.getCognome());
			ps.setInt(6, utente.getCodiceUtente());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("update(Utente): " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean delete(int codiceUtente) {
		String sql = "DELETE FROM utente WHERE codiceutente = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceUtente);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("delete(Utente): " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean checkEsistenzaUsername(String username) {
		String sql = "SELECT 1 FROM utente WHERE username = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			System.err.println("checkEsistenzaUsername: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean checkEsistenzaEmail(String email) {
		String sql = "SELECT 1 FROM utente WHERE email = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			System.err.println("checkEsistenzaEmail: " + e.getMessage());
			return false;
		}
	}

	@Override
	public Utente verificaCredenziali(String username, String password) {
		String sql = "SELECT * FROM utente WHERE username = ? AND password = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mappaRisultato(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("verificaCredenziali: " + e.getMessage());
		}
		return null;
	}

	// un trigger normalizza la coppia (codiceutente1 < codiceutente2): una sola riga per amicizia
	@Override
	public List<Utente> findAmici(int codiceUtente) {
		List<Utente> amici = new ArrayList<Utente>();
		String sql = "SELECT u.* FROM utente u "
				+ "JOIN amicizia a ON (u.codiceutente = a.codiceutente1 OR u.codiceutente = a.codiceutente2) "
				+ "WHERE (a.codiceutente1 = ? OR a.codiceutente2 = ?) AND u.codiceutente <> ? "
				+ "ORDER BY u.username";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceUtente);
			ps.setInt(2, codiceUtente);
			ps.setInt(3, codiceUtente);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					amici.add(mappaRisultato(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("findAmici: " + e.getMessage());
		}
		return amici;
	}

	@Override
	public boolean createAmicizia(int codiceUtente, int codiceAmico) {
		String sql = "INSERT INTO amicizia (codiceutente1, codiceutente2) VALUES (?, ?)";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceUtente);
			ps.setInt(2, codiceAmico);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("createAmicizia: " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean deleteAmicizia(int codiceUtente, int codiceAmico) {
		String sql = "DELETE FROM amicizia WHERE codiceutente1 = LEAST(?, ?) AND codiceutente2 = GREATEST(?, ?)";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceUtente);
			ps.setInt(2, codiceAmico);
			ps.setInt(3, codiceUtente);
			ps.setInt(4, codiceAmico);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("deleteAmicizia: " + e.getMessage());
			return false;
		}
	}

	private Utente mappaRisultato(ResultSet rs) throws SQLException {
		Utente utente = new Utente();
		utente.setCodiceUtente(rs.getInt("codiceutente"));
		utente.setUsername(rs.getString("username"));
		utente.setEmail(rs.getString("email"));
		utente.setPassword(rs.getString("password"));
		utente.setNome(rs.getString("nome"));
		utente.setCognome(rs.getString("cognome"));
		return utente;
	}
}
