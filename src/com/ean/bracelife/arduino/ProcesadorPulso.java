package com.ean.bracelife.arduino;

import org.apache.log4j.Logger;

public class ProcesadorPulso {

	Logger logger = Logger.getLogger(ProcesadorPulso.class);
	
	public void procesarPulso(String mensaje, String usuario){
		logger.info("Se procesa pulso " + mensaje + " para el usuario "+ usuario);
	}
}
