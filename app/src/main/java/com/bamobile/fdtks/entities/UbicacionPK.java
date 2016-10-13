package com.bamobile.fdtks.entities;

import com.google.myjson.annotations.SerializedName;

import java.io.Serializable;


public class UbicacionPK implements Serializable{
	
	@SerializedName("idubicacion")
	private String idubicacion;
	
	@SerializedName("camionIdcamion")
	private String camionIdcamion;
	
	@SerializedName("camionUsuarioIdusuario")
	private String camionUsuarioIdusuario;

	public String getIdubicacion() {
		return idubicacion;
	}

	public void setIdubicacion(String idubicacion) {
		this.idubicacion = idubicacion;
	}

	public String getCamionIdcamion() {
		return camionIdcamion;
	}

	public void setCamionIdcamion(String camionIdcamion) {
		this.camionIdcamion = camionIdcamion;
	}

	public String getCamionUsuarioIdusuario() {
		return camionUsuarioIdusuario;
	}

	public void setCamionUsuarioIdusuario(String camionUsuarioIdusuario) {
		this.camionUsuarioIdusuario = camionUsuarioIdusuario;
	}
	
	

}
