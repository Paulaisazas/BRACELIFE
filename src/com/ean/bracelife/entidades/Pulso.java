package com.ean.bracelife.entidades;

import java.sql.Timestamp;

public class Pulso {
	
	private int valorPulso;
	private int idPaciente;
	private Timestamp hora;
	private int idPulso;
	
	public int getValorPulso() {
		return valorPulso;
	}
	public void setValorPulso(int valorPulso) {
		this.valorPulso = valorPulso;
	}
	public int getIdPaciente() {
		return idPaciente;
	}
	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}
	public Timestamp getHora() {
		return hora;
	}
	public void setHora(Timestamp hora) {
		this.hora = hora;
	}
	public int getIdPulso() {
		return idPulso;
	}
	public void setIdPulso(int idPulso) {
		this.idPulso = idPulso;
	}
	
}
