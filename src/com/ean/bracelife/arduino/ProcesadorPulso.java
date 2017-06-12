package com.ean.bracelife.arduino;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import javax.activation.*;

import org.apache.log4j.Logger;

import com.ean.bracelife.db.ConexionDB;
import com.ean.bracelife.entidades.AdultoMayor;
import com.ean.bracelife.entidades.Notificacion;
import com.ean.bracelife.entidades.Pulso;
import com.ean.bracelife.entidades.Tutor;

public class ProcesadorPulso {

	Logger logger = Logger.getLogger(ProcesadorPulso.class);
	Properties prop = new Properties();
	
	public void procesarPulso(String mensaje, Tutor tutor, AdultoMayor paciente, int pulsoLimite){
		try {
			prop.load(new FileReader("properties//db.properties"));
			
		Date date = new Date();
		String alerta = "";
		
		Pulso pulso = new Pulso();
		pulso.setHora(new Timestamp(date.getTime()));
		pulso.setValorPulso(Integer.parseInt(mensaje));
		pulso.setIdPaciente(paciente.getIdPersona());
		pulso.setIdPulso(Integer.parseInt(prop.getProperty("pulso")));
		
		ConexionDB.guardarPulso(pulso);
		
		if(paciente.getContadorPulsoAlto()== pulsoLimite){
			alerta = "El pulso del paciente " + paciente.getNombre() + " y número de documento "  + paciente.getIdPersona() +
					" se encuentra por encima de los límites permitidos. El último pulso detectado ha sido de " + pulso.getValorPulso() +
					" con fecha: " + pulso.getHora().toString().substring(0, pulso.getHora().toString().length()-3);
			logger.error(alerta);
			notificarTutor(tutor, alerta, paciente);
		}else if(paciente.getContadorPulsoBajo() == pulsoLimite){
			alerta = "El pulso del paciente " + paciente.getNombre() + " y número de documento "  + paciente.getIdPersona() +
					" se encuentra por debajo de los límites permitidos. El último pulso detectado ha sido de " + pulso.getValorPulso() +
					" con fecha: " + pulso.getHora().toString().substring(0, pulso.getHora().toString().length()-3);
			notificarTutor(tutor, alerta, paciente);
			logger.error(alerta);
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void notificarTutor(Tutor tutor, String mensaje, AdultoMayor paciente){
		try{
			Date date = new Date();
			Notificacion notificacion = new Notificacion();
			notificacion.setIdNotificacion(Integer.parseInt(prop.getProperty("notificacion")));
			notificacion.setFechaNotificacion(new Timestamp(date.getTime()));
			notificacion.setIdTutor(tutor.getIdTutor());
			notificacion.setMensaje(mensaje);
			ConexionDB.guardarNotificacion(notificacion);
			enviarCorreo(tutor, notificacion.getMensaje(), paciente.getNombre());
		} catch (Exception e){
			logger.error(e);
		}
	}
	
	public void enviarCorreo(Tutor tutor, String mensaje, String nombrePaciente){
		try{
			logger.info("Enviando correo al tutor.");
			String password = prop.getProperty("mailPassword");
			
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			
			Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(prop.getProperty("emailFrom"), password);
						}
					  });
			
			try{
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(prop.getProperty("emailFrom")));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(tutor.getEmailTutor()));
				message.setSubject("Alerta de pulso para "+ nombrePaciente + " - documento " + tutor.getIdPaciente());
				message.setText(mensaje);
				
				Transport.send(message);
				logger.info("Finalizando envío de correo.");
			}catch (MessagingException e){
				logger.error(e);
				e.printStackTrace();
			}
			
		} catch(Exception e){
			logger.error(e);
		}
	}
}
