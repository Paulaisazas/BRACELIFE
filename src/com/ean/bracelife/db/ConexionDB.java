package com.ean.bracelife.db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConexionDB {

	static Logger logger = Logger.getLogger(ConexionDB.class);
	
	public String getID(int id){
		String str ="";
		try {
			str = selectRecordsFromTable(id);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			System.exit(-1);
		} catch (Exception e){
			logger.error(e.getMessage());
			System.exit(-1);
		}
		return str;
	}
	
	private static String selectRecordsFromTable(int id) throws SQLException {

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
	
	private static Connection getDBConnection() {
		Properties properties = new Properties();
		
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
