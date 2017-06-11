package com.ean.bracelife.db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ean.bracelife.entidades.AdultoMayor;
import com.ean.bracelife.entidades.Brazalete;
import com.ean.bracelife.entidades.Parametros;
import com.ean.bracelife.entidades.Pulso;

public class ConexionDB {

	static Logger logger = Logger.getLogger(ConexionDB.class);
	static Properties properties = new Properties();

	
	public String getID(int id){
		String str ="";
		try {
			str = obtenerNombrePersonaPorID(id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			System.exit(-1);
		} catch (Exception e){
			logger.error(e.getMessage());
			System.exit(-1);
		}
		return str;
	}
	
	private static String obtenerNombrePersonaPorID(int id) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String username = "";

		String selectSQL = "SELECT PER_ID, PER_NOMBRE FROM PERSONA WHERE PER_ID = ?";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				username = rs.getString("PER_NOMBRE");
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return username;
	}
	
	public static AdultoMayor obtenerAdultoMayorporID (int id) throws SQLException {
		AdultoMayor adultoMayor = new AdultoMayor();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String username = "";

		String selectSQL = "SELECT * FROM ADULTO_MAYOR WHERE PER_ID = ?";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			
			if (rs.next()) {
				adultoMayor.setIdPersona(id);
				adultoMayor.setIdTutor(rs.getInt("TUTOR_PER_ID"));
				adultoMayor.setEnfermedad(rs.getString("ADU_ENFERMEDAD"));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return adultoMayor;
	}
	
	public static Brazalete obtenerBrazalete (Brazalete brazalete) throws SQLException {
		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String username = "";

		String selectSQL = "SELECT * FROM BRAZALETE WHERE ADULTO_MAYOR_PER_ID = ?";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, brazalete.getIdPaciente());

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				brazalete.setHoraAsignacion(rs.getTimestamp("BRA_TIEMPO_AM"));
				brazalete.setIdBrazalete(rs.getInt("BRA_IDENTIFICACION"));
				brazalete.setIdPaciente(rs.getInt("ADULTO_MAYOR_PER_ID"));
			}else{
				crearBrazalete(brazalete);
				return obtenerBrazalete(brazalete);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return brazalete;
	}
	
	public static Parametros obtenerParametros() throws SQLException{
		Parametros parametros = new Parametros();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String username = "";

		String selectSQL = "SELECT * FROM PARAMETROS";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				parametros.setTiempoLimite(rs.getInt("PAR_TIEMPO"));
				parametros.setValorMinimo(rs.getInt("PAR_RANGO_MIN"));
				parametros.setValorMaximo(rs.getInt("PAR_RANGO_MAX"));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return parametros;
	}
	
	public static void guardarPulso(Pulso pulso) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		int idPulso = Integer.parseInt(properties.getProperty("pulso"));
		pulso.setIdPulso(idPulso);
		
		idPulso++;
		properties.setProperty("pulso", String.valueOf(idPulso));
		
		try {
			properties.store(new FileOutputStream("properties//db.properties"), null);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String insertTableSQL = "INSERT INTO PULSACIONES"
				+ "(PUL_VALOR, ADULTO_MAYOR_PER_ID, PUL_HORA, PUL_ID) VALUES"
				+ "(?,?,?,?)";

		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(insertTableSQL);

			preparedStatement.setInt(1, pulso.getValorPulso());
			preparedStatement.setInt(2, pulso.getIdPaciente());
			preparedStatement.setTimestamp(3, pulso.getHora());
			preparedStatement.setInt(4, pulso.getIdPulso());

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}
	
	public static void crearBrazalete(Brazalete brazalete) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		int idBrazalete = Integer.parseInt(properties.getProperty("brazalete"));
		brazalete.setIdBrazalete(idBrazalete);
		
		idBrazalete++;
		properties.setProperty("brazalete", String.valueOf(idBrazalete));
		
		try {
			properties.store(new FileOutputStream("properties//db.properties"), null);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		Date date = new Date();
		
		String insertTableSQL = "INSERT INTO BRAZALETE"
				+ "(ADULTO_MAYOR_PER_ID, BRA_IDENTIFICACION, BRA_TIEMPO_AM) VALUES"
				+ "(?,?,?)";

		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(insertTableSQL);

			preparedStatement.setInt(1, brazalete.getIdPaciente());
			preparedStatement.setInt(2, brazalete.getIdBrazalete());
			preparedStatement.setTimestamp(3, new Timestamp (date.getTime()));

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}
	
	private static Connection getDBConnection() {
		
		try {
			properties.load(new FileReader("properties\\db.properties"));
		} catch (IOException e1) {
			logger.error(e1.getMessage());
		}
		
		String driver = properties.getProperty("driver");
		String connection = properties.getProperty("connection");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		
		Connection dbConnection = null;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
		}

		try {
			dbConnection = DriverManager.getConnection(
					connection, username,password);
			return dbConnection;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return dbConnection;
	}
}
