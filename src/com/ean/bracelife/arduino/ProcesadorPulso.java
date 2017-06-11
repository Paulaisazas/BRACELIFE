package com.ean.bracelife.arduino;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ean.bracelife.db.ConexionDB;
import com.ean.bracelife.entidades.AdultoMayor;
import com.ean.bracelife.entidades.Pulso;

import oracle.sql.DATE;

public class ProcesadorPulso {

	Logger logger = Logger.getLogger(ProcesadorPulso.class);
	Properties prop = new Properties();
	
	public void procesarPulso(String mensaje, AdultoMayor paciente){
		try {
			prop.load(new FileReader("properties//db.properties"));
		Date date = new Date();
		
		Pulso pulso = new Pulso();
		pulso.setHora(new Timestamp(date.getTime()));
		pulso.setValorPulso(Integer.parseInt(mensaje));
		pulso.setIdPaciente(paciente.getIdPersona());
		pulso.setIdPulso(Integer.parseInt(prop.getProperty("pulso")));
		
		ConexionDB.guardarPulso(pulso);
		
		if(paciente.getContadorPulsoAlto()==2){
			logger.info("ALERTA, PULSO ALTO!!");
		}else if(paciente.getContadorPulsoBajo() == 2){
			logger.info("ALERTA, PULSO BAJO!!");
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
