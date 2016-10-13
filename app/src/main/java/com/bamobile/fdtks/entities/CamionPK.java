package com.bamobile.fdtks.entities;

import com.google.myjson.annotations.SerializedName;

import java.io.Serializable;


public class CamionPK implements Serializable{
	
	@SerializedName("idcamion")
	private String idcamion;
	
	@SerializedName("usuarioIdusuario")
	private String usuarioIdusuario;
	
	
	public void setUsuarioIdusuario(String usuarioIdusuario) {
		this.usuarioIdusuario = usuarioIdusuario;
	}
	
	public String getUsuarioIdusuario() {
		return usuarioIdusuario;
	}

	public String getIdcamion() {
		return idcamion;
	}

	public void setIdcamion(String idcamion) {
		this.idcamion = idcamion;
	}
}
