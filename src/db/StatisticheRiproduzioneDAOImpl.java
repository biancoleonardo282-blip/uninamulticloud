package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.StatisticheRiproduzione;

public class StatisticheRiproduzioneDAOImpl implements StatisticheRiproduzioneDAO {

	private Connection connessione;

	public StatisticheRiproduzioneDAOImpl() {
		try {
			connessione = ConnessioneDatabase.getIstanza().getConnessione();
		} catch (SQLException e) {
			System.err.println("Connessione al database non riuscita: " + e.getMessage());
		}
	}

	@Override
	public StatisticheRiproduzione findByElemento(int codiceElemento) {
		String sql = "SELECT * FROM statisticheriproduzione WHERE id_elemento = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceElemento);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mappaRisultato(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("findByElemento: " + e.getMessage());
		}
		return null;
	}

	@Override
	public boolean create(StatisticheRiproduzione statistiche) {
		String sql = "INSERT INTO statisticheriproduzione (id_elemento, numeroascolti, numerocondivisioni, mipiace) "
				+ "VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, statistiche.getCodiceElemento());
			ps.setInt(2, statistiche.getNumeroAscolti());
			ps.setInt(3, statistiche.getNumeroCondivisioni());
			ps.setInt(4, statistiche.getMiPiace());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("create(Statistiche): " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean update(StatisticheRiproduzione statistiche) {
		String sql = "UPDATE statisticheriproduzione SET numeroascolti = ?, numerocondivisioni = ?, mipiace = ? "
				+ "WHERE id_elemento = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, statistiche.getNumeroAscolti());
			ps.setInt(2, statistiche.getNumeroCondivisioni());
			ps.setInt(3, statistiche.getMiPiace());
			ps.setInt(4, statistiche.getCodiceElemento());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("update(Statistiche): " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean delete(int codiceElemento) {
		String sql = "DELETE FROM statisticheriproduzione WHERE id_elemento = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceElemento);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("delete(Statistiche): " + e.getMessage());
			return false;
		}
	}

	// la riga esiste gia': un trigger la crea all'inserimento dell'elemento, quindi qui basta l'UPDATE
	@Override
	public boolean incrementaAscolti(int codiceElemento) {
		return incrementaContatore("numeroascolti", codiceElemento);
	}

	@Override
	public boolean incrementaMiPiace(int codiceElemento) {
		return incrementaContatore("mipiace", codiceElemento);
	}

	@Override
	public boolean incrementaCondivisioni(int codiceElemento) {
		return incrementaContatore("numerocondivisioni", codiceElemento);
	}

	private boolean incrementaContatore(String colonna, int codiceElemento) {
		String sql = "UPDATE statisticheriproduzione SET " + colonna + " = " + colonna + " + 1 "
				+ "WHERE id_elemento = ?";
		try (PreparedStatement ps = connessione.prepareStatement(sql)) {
			ps.setInt(1, codiceElemento);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("incrementaContatore(" + colonna + "): " + e.getMessage());
			return false;
		}
	}

	private StatisticheRiproduzione mappaRisultato(ResultSet rs) throws SQLException {
		StatisticheRiproduzione statistiche = new StatisticheRiproduzione();
		statistiche.setCodiceElemento(rs.getInt("id_elemento"));
		statistiche.setNumeroAscolti(rs.getInt("numeroascolti"));
		statistiche.setNumeroCondivisioni(rs.getInt("numerocondivisioni"));
		statistiche.setMiPiace(rs.getInt("mipiace"));
		return statistiche;
	}
}
