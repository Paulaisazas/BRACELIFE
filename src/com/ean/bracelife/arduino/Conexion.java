package com.ean.bracelife.arduino;

import java.util.ArrayList;
import java.util.List;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import org.apache.log4j.Logger;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Conexion {
	Logger logger = Logger.getLogger(Conexion.class);
	
	PanamaHitek_Arduino iniciador = new PanamaHitek_Arduino();
	ProcesadorPulso procesador = new ProcesadorPulso();
	String IDPaciente;
	
	SerialPortEventListener listener = new SerialPortEventListener(){
		@Override
		public void serialEvent(SerialPortEvent arg0) {
			try {
				if(iniciador.isMessageAvailable()){
					String mensaje = iniciador.printMessage();
					logger.info(mensaje);
					procesador.procesarPulso(mensaje,"CARLOS BERNAL");
				}
			} catch (SerialPortException | ArduinoException e) {
				logger.error("Se ha presentado un error con la recepción de mensajería: "+ e);
				System.exit(-1);
			}
		}
	};
			
	public Conexion(String idPaciente){
		List<String> ports = new ArrayList<>();
		try {
			ports = iniciador.getSerialPorts();
			IDPaciente = idPaciente;
			if(ports.isEmpty()){
				logger.error("No se han encontrado puertos disponibles");
				logger.warn("Se finaliza el programa.");
				System.exit(-1);
			}
			for (String port : ports){
				logger.info("Se ha encontrado el puerto: " + port);
			}
			logger.info("Iniciando recepción de mensajes desde el puerto: " + ports.get(0));
			iniciador.arduinoRX(ports.get(0), 9600, listener);
		} catch (ArduinoException | SerialPortException e) {
			logger.error("Se ha presentado un error con la conexión al puerto: " + ports.get(0));
			logger.error(e);
			System.exit(-1);
		}
	}
}
