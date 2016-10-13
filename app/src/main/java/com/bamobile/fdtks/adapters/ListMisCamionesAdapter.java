package com.bamobile.fdtks.adapters;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.CamionActivity;
import com.bamobile.fdtks.activities.InformacionActivity;
import com.bamobile.fdtks.activities.UbicacionActivity;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.databases.ImageDatabase;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Informacion;
import com.bamobile.fdtks.entities.Ubicacion;
import com.bamobile.fdtks.util.Tools;

public class ListMisCamionesAdapter extends BaseAdapter {

	private Fragment mFrag;
	private List<Camion> miscamiones;
	private LayoutInflater inflater;
    private ImageDatabase imageDb;

	public ListMisCamionesAdapter(Fragment mFrag, List<Camion> miscamiones) {
		this.mFrag = mFrag;
		this.miscamiones = miscamiones;
        imageDb = new ImageDatabase(mFrag.getActivity());
	}

	@Override
	public int getCount() {
		return miscamiones.size();
	}

	@Override
	public Object getItem(int position) {
		return miscamiones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public List<Camion> getMiscamiones() {
		return miscamiones;
	}
	
	public void setMiscamiones(List<Camion> miscamiones) {
		this.miscamiones = miscamiones;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		inflater = (LayoutInflater) mFrag.getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.fragment_mis_trucks_items, null);
		
		final Camion camion = miscamiones.get(position);

		final ImageView logo = (ImageView) view
				.findViewById(R.id.imageView_logo_camion);
        try {
            if (camion.getLogo() != null) {
                Bitmap image = null;
                // final String imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + camion.getLogo();
                final String imageUrl = camion.getLogo();
                if (imageDb.exists(imageUrl)) {
                    image = imageDb.getImage(imageUrl);
                    logo.setImageBitmap(Tools.getRoundedCornerBitmap(image));
                } else {
                    new AsyncTask<Bitmap, Bitmap, Bitmap>() {

                        @Override
                        protected Bitmap doInBackground(Bitmap... params) {
                            Bitmap imageTemp = Tools.getBitmapFromURL(imageUrl);
                            imageDb.addImage(imageUrl, imageTemp);
                            return imageTemp;
                        }

                        @Override
                        protected void onPostExecute(Bitmap result) {
                            logo.setImageBitmap(Tools.getRoundedCornerBitmap(result));
                        }
                    }.execute();
                }
            }
        } finally {
            imageDb.close();
        }

		


		TextView nombre = (TextView) view
				.findViewById(R.id.textView_nombre_camion);
		nombre.setText(camion.getNombre());
		
		Button boton_editar = (Button) view.findViewById(R.id.button_editar_camion);
		boton_editar.setBackgroundResource(R.drawable.edit);
		boton_editar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent editCamion = new Intent(mFrag.getActivity(), CamionActivity.class);
				editCamion.putExtra("camion", camion);
				mFrag.startActivity(editCamion);
			}
		});

		Button boton_borrar = (Button) view	.findViewById(R.id.button_borrar_camion);
		boton_borrar.setBackgroundResource(R.drawable.delete);
		boton_borrar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
                        try {
						URL url = new URL("http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.camion/remove?id="
								/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.camion/remove?id="*/
								+ camion.getCamionPK().getIdcamion());
						HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
						httpCon.setDoOutput(true);
						httpCon.setRequestProperty(
								"Content-Type", "application/x-www-form-urlencoded" );
						httpCon.setRequestMethod("DELETE");
						httpCon.connect();

/*						HttpClient httpClient = new DefaultHttpClient();
						HttpDelete httpDelete = new HttpDelete(
								"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.camion/remove?id="
								*//*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.camion/remove?id="*//*
										+ camion.getCamionPK().getId());
                         httpClient.execute(httpDelete);*/
							FdTksApplication.getCamiones().remove(camion);
							miscamiones.remove(camion);
							mFrag.getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									notifyDataSetChanged();
								}
							});
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						if(FdTksApplication.getUbicacionxCamion(camion).getUbicacionPK() != null){
/*						httpDelete = new HttpDelete(
								"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.ubicacion/remove?id="
								*//*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.ubicacion/remove?id="*//*
										+ ((Ubicacion)FdTksApplication.getUbicacionxCamion(camion)).getUbicacionPK().getIdubicacion());	*/
						try {
                            URL url = new URL("http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.ubicacion/remove?id="
								/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.camion/remove?id="*/
                                    + ((Ubicacion)FdTksApplication.getUbicacionxCamion(camion)).getUbicacionPK().getIdubicacion());
                            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                            httpCon.setDoOutput(true);
                            httpCon.setRequestProperty(
                                    "Content-Type", "application/x-www-form-urlencoded" );
                            httpCon.setRequestMethod("DELETE");
                            httpCon.connect();
							FdTksApplication.getUbicaciones().remove(FdTksApplication.getUbicacionxCamion(camion));
						} catch (IOException e) {
							e.printStackTrace();
						}
						}
						
						if(FdTksApplication.getInfoxCamion(camion).getInformacionPK() != null){
/*						httpDelete = new HttpDelete(
								"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.informacion/remove?id="
								*//*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.informacion/remove?id="*//*
										+ ((Informacion)FdTksApplication.getInfoxCamion(camion)).getInformacionPK().getIdinformacion());		*/
						try {
                            URL url = new URL("http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.informacion/remove?id="
								/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.camion/remove?id="*/
                                    + ((Informacion)FdTksApplication.getInfoxCamion(camion)).getInformacionPK().getIdinformacion());
                            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                            httpCon.setDoOutput(true);
                            httpCon.setRequestProperty(
                                    "Content-Type", "application/x-www-form-urlencoded" );
                            httpCon.setRequestMethod("DELETE");
                            httpCon.connect();
							FdTksApplication.getUbicaciones().remove(FdTksApplication.getUbicacionxCamion(camion));
						} catch (IOException e) {
							e.printStackTrace();
						}
						}
					}
				}).start();
			}
		});

		Button boton_info = (Button) view.findViewById(R.id.button_crear_editar_menu);
		boton_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Informacion info = FdTksApplication.getInfoxCamion(camion);
				if(info.getDescripcion() == null && info.getMenu() == null && info.getContacto() == null){
					Intent newInfoIntent = new Intent(mFrag.getActivity(), InformacionActivity.class);
					newInfoIntent.putExtra("camion", camion);
					mFrag.startActivity(newInfoIntent);
				} else {
					Intent editInfoIntent = new Intent(mFrag.getActivity(), InformacionActivity.class);
					editInfoIntent.putExtra("camion", camion);
					editInfoIntent.putExtra("menu", info);
					mFrag.startActivity(editInfoIntent);
				}
				
			}
		});

		Button boton_ubicacion = (Button) view.findViewById(R.id.button_crear_editar_ubicacion);
		boton_ubicacion.setBackgroundResource(R.drawable.earth_location);
		boton_ubicacion.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Ubicacion ubicacion = FdTksApplication.getUbicacionxCamion(camion);
				if(ubicacion.getLatitud() == null && ubicacion.getLongitud() == null){
					Intent newUbicacion = new Intent(mFrag.getActivity(), UbicacionActivity.class);
					newUbicacion.putExtra("camion", camion);
					mFrag.startActivity(newUbicacion);
				}else{
					Intent editUbicacion = new Intent(mFrag.getActivity(), UbicacionActivity.class);
					editUbicacion.putExtra("ubicacion", ubicacion);
					mFrag.startActivity(editUbicacion);
				}
	
			}
		});
		return view;
	}
}
