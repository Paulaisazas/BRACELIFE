package com.ean.bracelife.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;

import org.apache.log4j.Logger;

import com.ean.bracelife.arduino.Conexion;


public class Main {
	static Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		
		logger.info("Se inicia la recepción de datos desde Arduino.");
		String pacienteID = obtenerIDArchivo();
		new Conexion(pacienteID);
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
