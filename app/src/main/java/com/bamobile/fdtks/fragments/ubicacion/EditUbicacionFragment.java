package com.bamobile.fdtks.fragments.ubicacion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.MainActivity;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.entities.Ubicacion;
import com.google.android.gms.maps.model.LatLng;
import com.google.myjson.Gson;
import com.google.myjson.JsonParser;
import com.google.myjson.stream.JsonReader;

@SuppressLint("ValidFragment")
public class EditUbicacionFragment extends Fragment implements LocationListener{

	private Ubicacion ubicacion;
	private URL url;
	private HttpURLConnection conn;
	
	public LocationManager mLocationManager;
	private Location mLocation;
	
	private Handler updateBarHandler;

	public EditUbicacionFragment(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
		updateBarHandler = new Handler();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LocationManager service = (LocationManager) getActivity()
		.getSystemService(MainActivity.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = service.getBestProvider(criteria, false);
		mLocation = service.getLastKnownLocation(provider);
		
	    mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
	    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L,
	            1.1f, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_edit_ubicacion,
				container, false);
		
//		ImageView logo = (ImageView) view.findViewById(R.id.image_logo_camion);
//		
//		if(ubicacion.getCamion().getLogo() != null){
//		   	Bitmap image = null;
//            String imageUrl = ubicacion.getCamion().getLogo();
//		  // String imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + ubicacion.getCamion().getLogo();
//            ImageDatabase imageDb = new ImageDatabase(getActivity());
//            if (imageDb.exists(imageUrl)) {
//                image = imageDb.getImage(imageUrl);
//            } else {
//                image = Tools.getBitmapFromURL(imageUrl);
//                imageDb.addImage(imageUrl, image);
//            }
//            imageDb.close();
//        	logo.setImageBitmap(Tools.getRoundedCornerBitmap(image));
//		}
//		
//		TextView textView_nombre_camion = (TextView) view.findViewById(R.id.textView_nombre_camion);
//		textView_nombre_camion.setText(ubicacion.getCamion().getNombre());

		final EditText editText_pais = (EditText) view.findViewById(R.id.editText_pais);
		editText_pais.setText(ubicacion.getPais());
		final EditText editText_ciudad = (EditText) view.findViewById(R.id.editText_ciudad);
		editText_ciudad.setText(ubicacion.getCiudad());
		final EditText editText_barrio = (EditText) view.findViewById(R.id.editText_barrio);
		editText_barrio.setText(ubicacion.getBarrio());
		final EditText editText_calle = (EditText) view.findViewById(R.id.editText_calle);
		editText_calle.setText(ubicacion.getCalle());
		final EditText editText_altura = (EditText) view.findViewById(R.id.editText_altura);
		editText_altura.setText(ubicacion.getAltura());
		
		final Spinner spinner_desde_dia = (Spinner) view.findViewById(R.id.spinner_desde_dia);
		final Spinner spinner_desde_mes = (Spinner) view.findViewById(R.id.spinner_desde_mes);
		final Spinner spinner_hasta_dia = (Spinner) view.findViewById(R.id.spinner_hasta_dia);
		final Spinner spinner_hasta_mes = (Spinner) view.findViewById(R.id.spinner_hasta_mes);
		final Spinner spinner_hora_aper = (Spinner) view.findViewById(R.id.spinner_hora_apertura);
		final Spinner spinner_hora_cierre = (Spinner) view.findViewById(R.id.spinner_hora_clausura);
		
		if(ubicacion.getDesde() != null && ubicacion.getHasta() != null && ubicacion.getHorario()!= null){
			String[] fecha_desde = ubicacion.getDesde().split("/");
			String[] fecha_hasta = ubicacion.getHasta().split("/");
			String[] hora = ubicacion.getHorario().split("/");			
			String []hora_pos_ape = hora[0].split(":");
			int hora_apertura = Integer.parseInt(hora_pos_ape[0]);			
			String []hora_pos_cierre = hora[1].split(":");
			int hora_cierre = Integer.parseInt(hora_pos_cierre[0]);
			
			spinner_desde_dia.setSelection(Integer.parseInt(fecha_desde[0])-1);
			spinner_desde_mes.setSelection(Integer.parseInt(fecha_desde[1])-1);
			spinner_hasta_dia.setSelection(Integer.parseInt(fecha_hasta[0])-1);
			spinner_hasta_mes.setSelection(Integer.parseInt(fecha_hasta[1])-1);
			spinner_hora_aper.setSelection(hora_apertura);
			spinner_hora_cierre.setSelection(hora_cierre);	
		}
		
		final TextView textView_lat = (TextView) view.findViewById(R.id.textView_lat);
		textView_lat.setText(ubicacion.getLatitud());
		final TextView textView_long = (TextView) view.findViewById(R.id.textView_long);
		textView_long.setText(ubicacion.getLongitud());
		
		Button hide = (Button) view.findViewById(R.id.button_hide);
		hide.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				editText_pais.setText("");
				editText_ciudad.setText("");
				editText_barrio.setText("");
				editText_calle.setText("");
				editText_altura.setText("");
				textView_lat.setText("");
				textView_long.setText("");
				
			}});

		Button get = (Button) view.findViewById(R.id.button_get);
		get.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				LatLng userLocation;
				if (mLocation == null) {
					double ltd = -34.558244;
					double lng = -58.444777;
					userLocation = new LatLng(ltd, lng);
				} else {
					userLocation = new LatLng(mLocation.getLatitude(),
							mLocation.getLongitude());
				}
				
				Geocoder gcd = new Geocoder(EditUbicacionFragment.this.getActivity(), Locale.getDefault());
				List<Address> addresses;
				try {
					addresses = gcd.getFromLocation(userLocation.latitude, userLocation.longitude, 1);
					if (addresses.size() > 0) 
						editText_pais.setText(addresses.get(0).getCountryName());
						editText_ciudad.setText(addresses.get(0).getLocality());
						editText_barrio.setText(addresses.get(0).getSubLocality());
						editText_calle.setText(addresses.get(0).getAddressLine(0));
						editText_altura.setText(addresses.get(0).getFeatureName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				textView_lat.setText(String.valueOf(userLocation.latitude));
				textView_long.setText(String.valueOf(userLocation.longitude));
			}
		});

		Button button_confirmar = (Button) view
				.findViewById(R.id.button_confirmar);
		button_confirmar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ubicacion.setPais(editText_pais.getText().toString());
				ubicacion.setCiudad(editText_ciudad.getText().toString());
				ubicacion.setBarrio(editText_barrio.getText().toString());
				ubicacion.setCalle(editText_calle.getText().toString());
				ubicacion.setAltura(editText_altura.getText().toString());
				ubicacion.setLatitud(textView_lat.getText().toString());
				ubicacion.setLongitud(textView_long.getText().toString());

				ubicacion.setDesde(spinner_desde_dia.getSelectedItem().toString() + "/" + spinner_desde_mes.getSelectedItem().toString());
				ubicacion.setHasta(spinner_hasta_dia.getSelectedItem().toString() + "/" + spinner_hasta_mes.getSelectedItem().toString());
				ubicacion.setHorario(spinner_hora_aper.getSelectedItem().toString() + "/" + spinner_hora_cierre.getSelectedItem().toString());
								
				final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setTitle(getResources().getString(R.string.uploading));
                pDialog.setMessage(getResources().getString(R.string.progress));
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setProgress(0);
                pDialog.setMax(100);
                pDialog.show();
				
				try {

					new Thread(new Runnable() {
						@Override
						public void run() {

							HttpParams httpParams = new BasicHttpParams();
							HttpClient client = new DefaultHttpClient(
									httpParams);
							HttpPut request = new HttpPut(
									"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.ubicacion/put"
									/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.ubicacion/put"*/);

							Gson gson = new Gson();

							request.setHeader("Content-Type",
									"application/json");

							Log.i("JSON Object", gson.toJson(ubicacion)
									.toString());

							try {
								StringEntity se;
								se = new StringEntity(gson.toJson(ubicacion),"UTF-8");
								se.setContentType("application/json");
								request.setEntity(se);

							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							try {

								HttpResponse response = client.execute(request);
								Log.d("HTTP", response.toString());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							try {
								url = new URL(
										"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.ubicacion/"
										/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.ubicacion"*/);
								conn = (HttpURLConnection) url.openConnection();
								conn.setDoInput(true);
								conn.setRequestProperty("Accept",
										"application/json");
								InputStream in = conn.getInputStream();
								JsonReader reader = new JsonReader(new InputStreamReader(in));
								JsonParser parser = new JsonParser();
								gson = new Gson();

								String json = gson.toJson(parser.parse(reader));
								JSONArray array = new JSONArray(json);
								Ubicacion responseUbicacion = new Ubicacion();
								FdTksApplication.getUbicaciones().clear();
								for (int i = 0; i < array.length(); i++) {
									responseUbicacion = gson.fromJson(array
											.getJSONObject(i).toString(),
											Ubicacion.class);
									FdTksApplication.getUbicaciones().add(
											responseUbicacion);
								}
							} catch (Exception e) {

							}
							pDialog.dismiss();
							getActivity().finish();
						}
						
					}).start();

					Log.d("HTTP", "HTTP: OK");
					MainActivity.wsis.getConn().sendTextMessage(ubicacion.getCamion().getNombre() +" - "+ 
																ubicacion.getCalle());
					updateBarHandler.post(new Runnable() {

						public void run() {
							pDialog.incrementProgressBy(100);
							pDialog.dismiss();
						}
					});
					
				} catch (Exception e) {
					Log.e("HTTP", "Error in http connection " + e.toString());
				}

			}
		});

		return view;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		mLocation = location;	
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
