package com.bamobile.fdtks.entities;

import com.google.myjson.annotations.SerializedName;

import java.io.Serializable;


public class Informacion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	@SerializedName("descripcion")
	private String descripcion;
	
	@SerializedName("informacion")
	private String informacion;
	
	@SerializedName("menu")
	private String menu;
	
	@SerializedName("contacto")
	private String contacto;
	
	@SerializedName("web")
	private String web;
	
	@SerializedName("facebook")
	private String facebook;
	
	@SerializedName("twitter")
	private String twitter;
	
	@SerializedName("instagram")
	private String instagram;
	
	@SerializedName("camion")
	private Camion camion;
	
	@SerializedName("informacionPK")
	private InformacionPK informacionPK;
	

	public InformacionPK getInformacionPK() {
		return informacionPK;
	}
	
	public void setInformacionPK(InformacionPK informacionPK) {
		this.informacionPK = informacionPK;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	public String getContacto() {
		return contacto;
	}
	
	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public Camion getCamion() {
		return camion;
	}
	
	public void setCamion(Camion camion) {
		this.camion = camion;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (informacionPK.getIdinformacion() != null ? informacionPK.getIdinformacion().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Usuario)) {
            return false;
        }
        Informacion other = (Informacion) object;
        if ((this.informacionPK.getIdinformacion() == null && other.informacionPK.getIdinformacion() != null) ||
        		(this.informacionPK.getIdinformacion() != null && !this.informacionPK.getIdinformacion().equals(other.informacionPK.getIdinformacion()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "info:" + camion.getNombre();
    }
}
