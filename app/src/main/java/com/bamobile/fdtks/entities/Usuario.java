package com.bamobile.fdtks.entities;

import com.google.myjson.annotations.SerializedName;

import java.io.Serializable;


public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("idusuario")
	private String idusuario;
	
	@SerializedName("nombre")
	private String nombre;
	
	@SerializedName("mail")
	private String mail;
	
	@SerializedName("telefono")
	private String telefono;
	
	@SerializedName("password")
	private String password;
	
	
	public String getIdusuario() {
		return idusuario;
	}

	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idusuario != null ? idusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idusuario == null && other.idusuario != null) ||
        		(this.idusuario != null && !this.idusuario.equals(other.idusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
