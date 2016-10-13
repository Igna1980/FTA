package com.bamobile.fdtks.entities;

import com.google.myjson.annotations.SerializedName;

import java.io.Serializable;


public class Camion implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@SerializedName("nombre")
	private String nombre;
	
	@SerializedName("tipo")
	private String tipo;
	
	@SerializedName("logo")
	private String logo;
	
	@SerializedName("foto1")
	private String foto1;
	
	@SerializedName("foto2")
	private String foto2;
	
	@SerializedName("reports")
	private int reports;
	
	@SerializedName("usuario")
	private Usuario usuario;
	
	@SerializedName("camionPK")
	private CamionPK camionPK;

	public CamionPK getCamionPK() {
		return camionPK;
	}

	public void setCamionPK(CamionPK camionPK) {
		this.camionPK = camionPK;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getLogo() {
		return logo;
	}
	
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public String getFoto1() {
		return foto1;
	}
	
	public void setFoto1(String foto1) {
		this.foto1 = foto1;
	}
	
	public String getFoto2() {
		return foto2;
	}
	
	public void setFoto2(String foto2) {
		this.foto2 = foto2;
	}
	
	public int getReports() {
		return reports;
	}

	public void setReports(int reports) {
		this.reports = reports;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (camionPK.getIdcamion() != null ? camionPK.getIdcamion().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Camion)) {
            return false;
        }
        Camion other = (Camion) object;
        if ((camionPK.getIdcamion() == null && other.camionPK.getIdcamion() != null) || (camionPK.getIdcamion() != null
        		&& !camionPK.getIdcamion().equals(other.camionPK.getIdcamion()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
