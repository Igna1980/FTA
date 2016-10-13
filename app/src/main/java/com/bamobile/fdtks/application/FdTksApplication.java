package com.bamobile.fdtks.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Informacion;
import com.bamobile.fdtks.entities.Ubicacion;
import com.bamobile.fdtks.entities.Usuario;

public class FdTksApplication extends Application{
	
	private static List<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
	private static List<Informacion> informaciones = new ArrayList<Informacion>();
	private static List<Camion> camiones = new ArrayList<Camion>();
	private static List<Usuario> usuarios = new ArrayList<Usuario>();
	private static HashMap<Camion, Bitmap> imagen1xcamion = new HashMap<Camion, Bitmap>();
	private static HashMap<Camion, Bitmap> imagen2xcamion = new HashMap<Camion, Bitmap>();
	private static HashMap<Camion, Bitmap> imagenLogoxcamion = new HashMap<Camion, Bitmap>();
	private static Usuario usuario = new Usuario();
	
	public static Context appContext;
	
	public static DisplayMetrics outMetrics;
	public static Double displayWidth;
	public static Double displayHeight;
	public static float displayDensity;
	
	@Override
	public void onCreate() {
		appContext = this;
		
		Display display = ((WindowManager) appContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		displayDensity = outMetrics.density;
		displayWidth = (double) display.getWidth();
		displayHeight = (double) display.getHeight();

		super.onCreate();
	}
	
	public static List<Ubicacion> getUbicaciones() {
		return ubicaciones;
	}
	
	public static void setUbicaciones(List<Ubicacion> ubicaciones) {
		FdTksApplication.ubicaciones = ubicaciones;
	}
	
	public static List<Informacion> getInformaciones() {
		return informaciones;
	}
	
	public static void setInformaciones(List<Informacion> informaciones) {
		FdTksApplication.informaciones = informaciones;
	}
	
	public static List<Camion> getCamiones() {
		return camiones;
	}
	
	public static void setCamiones(List<Camion> camiones) {
		FdTksApplication.camiones = camiones;
	}
	
	public static List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public static void setUsuarios(List<Usuario> usuarios) {
		FdTksApplication.usuarios = usuarios;
	}

	public static Usuario getUsuario() {
		return usuario;
	}

	public static void setUsuario(Usuario usuario) {
		FdTksApplication.usuario = usuario;
	}
	
	public static HashMap<Camion, Bitmap> getImagen1xcamion() {
		return imagen1xcamion;
	}

	public static void setImagen1xcamion(HashMap<Camion, Bitmap> imagen1xcamion) {
		FdTksApplication.imagen1xcamion = imagen1xcamion;
	}
	
	public static HashMap<Camion, Bitmap> getImagen2xcamion() {
		return imagen2xcamion;
	}

	public static void setImagen2xcamion(HashMap<Camion, Bitmap> imagen2xcamion) {
		FdTksApplication.imagen2xcamion = imagen2xcamion;
	}
	
	public static HashMap<Camion, Bitmap> getImagenLogoxcamion() {
		return imagenLogoxcamion;
	}

	public static void setImagenLogoxcamion(HashMap<Camion, Bitmap> imagenLogoxcamion) {
		FdTksApplication.imagenLogoxcamion = imagenLogoxcamion;
	}
	
	public static List<Camion> getCamionesxUsuario(){
		List<Camion> misCamiones = new ArrayList<Camion>();
		for (Camion camion : camiones) {
			if (camion != null && camion.getUsuario().equals(usuario)) {
				misCamiones.add(camion);
			}
		}
		return misCamiones;
	}
	
	public static Informacion getInfoxCamion(Camion camion){
		Informacion infoxcamion = new Informacion();
		for(Informacion m : informaciones){
			if(m.getCamion().equals(camion)){
				infoxcamion = m;
			}
		}
		return infoxcamion;
	}
	
	public static Ubicacion getUbicacionxCamion(Camion camion){
		Ubicacion ubicacionxCamion = new Ubicacion();
		for(Ubicacion u : ubicaciones){
			if(u.getCamion().equals(camion)){
				ubicacionxCamion = u;
			}
		}
		return ubicacionxCamion;
	}

}
