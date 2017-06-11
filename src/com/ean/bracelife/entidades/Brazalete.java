package com.ean.bracelife.entidades;

import java.sql.Timestamp;

public class Brazalete {

	private int idPaciente;
	private int idBrazalete;
	private Timestamp horaAsignacion;
	
	public int getIdPaciente() {
		return idPaciente;
	}
	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}
	public int getIdBrazalete() {
		return idBrazalete;
	}
	public void setIdBrazalete(int idBrazalete) {
		this.idBrazalete = idBrazalete;
	}
	public Timestamp getHoraAsignacion() {
		return horaAsignacion;
	}
	public void setHoraAsignacion(Timestamp horaAsignacion) {
		this.horaAsignacion = horaAsignacion;
	}
}
