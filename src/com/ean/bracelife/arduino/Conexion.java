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
	
	//Estas variables ID y nombrePaciente quedarán en memoria durante el tiempo que corra el programa
	String IDPaciente;
	String nombrePaciente;
	
	SerialPortEventListener listener = new SerialPortEventListener(){
		@Override
		public void serialEvent(SerialPortEvent arg0) {
			try {
				//Si existen datos desde Arduino, se procesan
				if(iniciador.isMessageAvailable()){
					//Obtenemos el pulso del paciente
					String mensaje = iniciador.printMessage();
					//Se procesa cada pulso en BD
					procesador.procesarPulso(mensaje, nombrePaciente);
				}
			} catch (SerialPortException | ArduinoException e) {
				logger.error("Se ha presentado un error con la recepción de mensajería: "+ e);
				System.exit(-1);
			}
		}
	};
			
	public Conexion(int idPaciente, String nombre){
		List<String> ports = new ArrayList<>();
		try {
			//Extraemos todos los puertos disponibles de Arduino
			ports = iniciador.getSerialPorts();
			//Asignamos los datos de ID y Nombre que obtuvimos en la clase Main.java
			IDPaciente = String.valueOf(idPaciente);
			nombrePaciente = nombre;
			
			//Si no existen puertos, se finaliza el programa.
			if(ports.isEmpty()){
				logger.error("No se han encontrado puertos disponibles");
				logger.warn("Se finaliza el programa.");
				System.exit(-1);
			}
			
			//Nos conectamos por el primer puerto encontrado.
			logger.info("Iniciando recepción de mensajes desde el puerto: " + ports.get(0));
			iniciador.arduinoRX(ports.get(0), 9600, listener);
			
		} catch (ArduinoException | SerialPortException e) {
			logger.error("Se ha presentado un error con la conexión al puerto: " + ports.get(0));
			logger.error(e);
			System.exit(-1);
		}
	}
}
