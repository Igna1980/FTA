package com.bamobile.fdtks.entities;

import com.google.myjson.annotations.SerializedName;

import java.io.Serializable;


public class Ubicacion implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@SerializedName("latitud")
	private String latitud;
	
	@SerializedName("longitud")
	private String longitud;
	
	@SerializedName("calle")
	private String calle;
	
	@SerializedName("altura")
	private String altura;
	
	@SerializedName("barrio")
	private String barrio;

	@SerializedName("ciudad")
	private String ciudad;
	
	@SerializedName("pais")
	private String pais;
	
	@SerializedName("desde")
	private String desde;
	
	@SerializedName("hasta")
	private String hasta;

	@SerializedName("horario")
	private String horario;
	
	@SerializedName("camion")
	private Camion camion;
	
	@SerializedName("ubicacionPK")
	private UbicacionPK ubicacionPK;


	public UbicacionPK getUbicacionPK() {
		return ubicacionPK;
	}

	public void setUbicacionPK(UbicacionPK ubicacionPK) {
		this.ubicacionPK = ubicacionPK;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getAltura() {
		return altura;
	}

	public void setAltura(String altura) {
		this.altura = altura;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getDesde() {
		return desde;
	}

	public void setDesde(String desde) {
		this.desde = desde;
	}

	public String getHasta() {
		return hasta;
	}

	public void setHasta(String hasta) {
		this.hasta = hasta;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
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
        hash += (ubicacionPK.getIdubicacion() != null ? ubicacionPK.getIdubicacion().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Ubicacion)) {
            return false;
        }
        Ubicacion other = (Ubicacion) object;
        if ((this.ubicacionPK.getIdubicacion() == null && other.ubicacionPK.getIdubicacion() != null) ||
        		(this.ubicacionPK.getIdubicacion() != null && !this.ubicacionPK.getIdubicacion().equals(other.ubicacionPK.getIdubicacion()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return calle + " " + altura;
    }
}
