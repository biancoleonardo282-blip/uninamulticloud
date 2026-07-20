package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

	private static ConnessioneDatabase istanza;

	private static final String DRIVER = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql://localhost:5432/uninamulticloud";
	private static final String UTENTE_DB = "postgres";
	private static final String PASSWORD_DB = "postgres";

	private Connection connessione;

	private ConnessioneDatabase() throws SQLException {
		try {
			Class.forName(DRIVER);
			connessione = DriverManager.getConnection(URL, UTENTE_DB, PASSWORD_DB);
		} catch (ClassNotFoundException e) {
			throw new SQLException("Driver JDBC PostgreSQL non trovato nel classpath.", e);
		}
	}

	public static ConnessioneDatabase getIstanza() throws SQLException {
		if (istanza == null || istanza.connessione == null || istanza.connessione.isClosed()) {
			istanza = new ConnessioneDatabase();
		}
		return istanza;
	}

	public Connection getConnessione() {
		return connessione;
	}

	public void chiudiConnessione() {
		try {
			if (connessione != null && !connessione.isClosed()) {
				connessione.close();
			}
		} catch (SQLException e) {
			System.err.println("Errore nella chiusura della connessione: " + e.getMessage());
		} finally {
			istanza = null;
		}
	}
}
