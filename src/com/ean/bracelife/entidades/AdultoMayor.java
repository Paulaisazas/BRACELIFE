package com.ean.bracelife.entidades;

public class AdultoMayor {

	private int idPersona;
	private String nombre;
	private int idTutor;
	private String enfermedad;
	private int brazaleteId;
	private int contadorPulsoBajo;
	private int contadorPulsoAlto;
	
	public int getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(int idPersona) {
		this.idPersona = idPersona;
	}
	public int getIdTutor() {
		return idTutor;
	}
	public void setIdTutor(int idTutor) {
		this.idTutor = idTutor;
	}
	public String getEnfermedad() {
		return enfermedad;
	}
	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}
	public int getBrazaleteId() {
		return brazaleteId;
	}
	public void setBrazaleteId(int brazaleteId) {
		this.brazaleteId = brazaleteId;
	}
	public int getContadorPulsoBajo() {
		return contadorPulsoBajo;
	}
	public void setContadorPulsoBajo(int contadorPulsoBajo) {
		this.contadorPulsoBajo = contadorPulsoBajo;
	}
	public int getContadorPulsoAlto() {
		return contadorPulsoAlto;
	}
	public void setContadorPulsoAlto(int contadorPulsoAlto) {
		this.contadorPulsoAlto = contadorPulsoAlto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
