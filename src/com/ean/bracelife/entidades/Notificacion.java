package com.ean.bracelife.entidades;

import java.sql.Timestamp;

public class Notificacion {

	private int idTutor;
	private Timestamp fechaNotificacion;
	private String mensaje;
	private int idNotificacion;
	
	public int getIdTutor() {
		return idTutor;
	}
	public void setIdTutor(int idTutor) {
		this.idTutor = idTutor;
	}
	public Timestamp getFechaNotificacion() {
		return fechaNotificacion;
	}
	public void setFechaNotificacion(Timestamp fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public int getIdNotificacion() {
		return idNotificacion;
	}
	public void setIdNotificacion(int idNotificacion) {
		this.idNotificacion = idNotificacion;
	}
}
