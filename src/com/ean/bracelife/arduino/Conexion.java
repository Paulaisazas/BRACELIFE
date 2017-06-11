package com.ean.bracelife.arduino;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ean.bracelife.db.ConexionDB;
import com.ean.bracelife.entidades.AdultoMayor;
import com.ean.bracelife.entidades.Brazalete;
import com.ean.bracelife.entidades.Parametros;
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
	AdultoMayor adultoMayor = new AdultoMayor();
	Parametros parametros = new Parametros();
	Brazalete brazalete = new Brazalete();
	
	//Estas variables ID y nombrePaciente quedarán en memoria durante el tiempo que corra el programa
	String IDPaciente;
	String nombrePaciente;
	int contadorBajo = 0;
	int contadorAlto = 0;
	
	SerialPortEventListener listener = new SerialPortEventListener(){
		@Override
		public void serialEvent(SerialPortEvent arg0) {
			try {
				//Si existen datos desde Arduino, se procesan
				if(iniciador.isMessageAvailable()){
					//Obtenemos el pulso del paciente
					String mensaje = iniciador.printMessage();
					logger.info(mensaje);
					int pulso = Integer.parseInt(mensaje);
					
					if(pulso<parametros.getValorMinimo()){
						contadorBajo++;
						contadorAlto = 0;
						adultoMayor.setContadorPulsoBajo(contadorBajo);
						logger.info("Contador bajo: " + contadorBajo);
					} else if(pulso>parametros.getValorMaximo()){
						contadorAlto++;
						contadorBajo = 0;
						adultoMayor.setContadorPulsoAlto(contadorAlto);
						logger.info("Contador alto: " + contadorAlto);
					} else{
						contadorBajo = 0;
						contadorAlto = 0;
						adultoMayor.setContadorPulsoBajo(0);
						adultoMayor.setContadorPulsoAlto(0);
					}
					
					//Se procesa cada pulso en BD
					procesador.procesarPulso(mensaje, adultoMayor);
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
			//Buscamos el adulto mayor con el id del paciente
			adultoMayor = ConexionDB.obtenerAdultoMayorporID(idPaciente);
			
			//Obtenemos los parametros
			parametros = ConexionDB.obtenerParametros();
			
			//Generamos un nuevo brazalete para el paciente.
			brazalete.setIdPaciente(idPaciente);
			ConexionDB.obtenerBrazalete(brazalete);
			
			
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
		} catch (SQLException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
