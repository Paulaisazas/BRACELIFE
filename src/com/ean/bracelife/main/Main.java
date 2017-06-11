package com.ean.bracelife.main;

import org.apache.log4j.Logger;

import com.ean.bracelife.arduino.Conexion;


public class Main {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Main.class);
		
		logger.info("Te amo!");

		Conexion conexion = new Conexion();
			System.out.print("Te amo");
			
	}

}
