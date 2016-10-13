package com.bamobile.fdtks.entities;

import com.google.myjson.annotations.SerializedName;

import java.io.Serializable;


public class InformacionPK implements Serializable{
	
	public static final long serialVersionUID = 1L;
	
	@SerializedName("idinformacion")
	private String idinformacion;
	
	@SerializedName("camionIdcamion")
	private String camionIdcamion;
	
	@SerializedName("camionUsuarioIdusuario")
	private String camionUsuarioIdUsuario;

	public String getIdinformacion() {
		return idinformacion;
	}

	public void setIdinformacion(String idInforamcion) {
		this.idinformacion = idInforamcion;
	}

	public String getCamionIdcamion() {
		return camionIdcamion;
	}

	public void setCamionIdcamion(String camionIdcamion) {
		this.camionIdcamion = camionIdcamion;
	}

	public String getCamionUsuarioIdUsuario() {
		return camionUsuarioIdUsuario;
	}

	public void setCamionUsuarioIdUsuario(String camionUsuarioIdUsuario) {
		this.camionUsuarioIdUsuario = camionUsuarioIdUsuario;
	}
	
	

}
