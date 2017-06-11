package com.ean.bracelife.arduino;

import java.util.List;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Conexion {
	PanamaHitek_Arduino iniciador = new PanamaHitek_Arduino();
	
	SerialPortEventListener listener = new SerialPortEventListener(){
		@Override
		public void serialEvent(SerialPortEvent arg0) {
			try {
				if(iniciador.isMessageAvailable()){
					System.out.println(iniciador.printMessage());
				}
			} catch (SerialPortException | ArduinoException e) {
				e.printStackTrace();
			}
		}
	};
			
	public Conexion(){
		try {
			int con = iniciador.getPortsAvailable();
			List<String> ports = iniciador.getSerialPorts();
			iniciador.arduinoRX(ports.get(0), 9600, listener);
			
		} catch (ArduinoException | SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
