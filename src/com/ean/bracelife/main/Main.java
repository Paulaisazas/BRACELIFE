package com.ean.bracelife.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.ean.bracelife.arduino.Conexion;
import com.ean.bracelife.db.ConexionDB;


public class Main {
	static Logger logger = Logger.getLogger(Main.class);
	static ConexionDB conexionDB = new ConexionDB();

	public static void main(String[] args) {
		
		logger.info("Se inicia la recepción de datos desde Arduino.");
		
		//Obtengo el ID del paciente, del archivo properties\ID.txt
		int pacienteID = Integer.parseInt(obtenerIDArchivo());
		
		//Con el id obtenido, extraigo el nombre de la Base de datos
		String nombre = conexionDB.getID(pacienteID);
		
		logger.info("Se obtiene el nombre: " + nombre + " del id " + pacienteID);
		
		//Inicio la recepción de mensajes de Arduino con el id y el nombre del paciente.
		new Conexion(pacienteID, nombre);

	}
	
	public static String obtenerIDArchivo(){
		FileReader entrada = null;
		String id = "";
		BufferedReader buffer = null;
		try {
			entrada = new FileReader("properties\\ID.txt");
			buffer = new BufferedReader(entrada);
			id = buffer.readLine();
			buffer.close();
			entrada.close();
		} catch (IOException e) {
			logger.error("Ocurrió un error al leer el archivo properties\\ID.txt");
			logger.error(e);
			System.exit(-1);
		} finally{
				try {
					if (buffer!=null){
						buffer.close();
					}
					if (entrada!=null){
						entrada.close();
					}
				} catch (IOException e) {
					logger.error("Ocurrió un error al leer el archivo properties\\ID.txt");
					logger.error(e);
					System.exit(-1);
				}
		}
		return id;
	}

}
