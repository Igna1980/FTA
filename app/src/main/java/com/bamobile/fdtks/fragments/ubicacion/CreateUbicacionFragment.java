package com.bamobile.fdtks.fragments.ubicacion;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

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
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Ubicacion;
import com.bamobile.fdtks.entities.UbicacionPK;
import com.google.android.gms.maps.model.LatLng;
import com.google.myjson.Gson;

@SuppressLint("ValidFragment")
public class CreateUbicacionFragment extends Fragment implements LocationListener{
	
	public static final int ITEM_FRAGMENT = 5;
	public LocationManager mLocationManager;
	private Location mLocation;

	private Camion camion;
	
	private Handler updateBarHandler;
	

	public CreateUbicacionFragment(Camion camion) {
		this.camion = camion;
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
		
		View view = inflater.inflate(R.layout.fragment_create_ubicacion, container, false);
		
//		ImageView logo = (ImageView) view.findViewById(R.id.image_logo_camion);
//		
//		if(camion.getLogo() != null){
//		   	Bitmap image = null;
//            String imageUrl = camion.getLogo();
//		  // String imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + camion.getLogo();
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
//		textView_nombre_camion.setText(camion.getNombre());
		
		final EditText editText_pais = (EditText) view.findViewById(R.id.editText_pais);
		editText_pais.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText_pais.getText().toString().equals("")){
					editText_pais.setError(getResources().getString(R.string.pais_requerido));
				}
			}
		});
		final EditText editText_ciudad = (EditText) view.findViewById(R.id.editText_ciudad);
		editText_ciudad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText_ciudad.getText().toString().equals("")){
					editText_ciudad.setError(getResources().getString(R.string.city_requerido));
				}
			}
		});
		final EditText editText_barrio = (EditText) view.findViewById(R.id.editText_barrio);
		editText_barrio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText_barrio.getText().toString().equals("")){
					editText_barrio.setError(getResources().getString(R.string.barrio_requerido));
				}
			}
		});
		final EditText editText_calle = (EditText) view.findViewById(R.id.editText_calle);
		editText_calle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText_calle.getText().toString().equals("")){
					editText_calle.setError(getResources().getString(R.string.calle_requerido));
				}
			}
		});
		final EditText editText_altura = (EditText) view.findViewById(R.id.editText_altura);
		editText_altura.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(editText_altura.getText().toString().equals("")){
					editText_altura.setError(getResources().getString(R.string.altura_requerido));
				}
			}
		});
		
		final Spinner spinner_desde_dia = (Spinner) view.findViewById(R.id.spinner_desde_dia);
		final Spinner spinner_desde_mes = (Spinner) view.findViewById(R.id.spinner_desde_mes);
		final Spinner spinner_hasta_dia = (Spinner) view.findViewById(R.id.spinner_hasta_dia);
		final Spinner spinner_hasta_mes = (Spinner) view.findViewById(R.id.spinner_hasta_mes);
		final Spinner spinner_hora_aper = (Spinner) view.findViewById(R.id.spinner_hora_apertura);
		final Spinner spinner_hora_cierre = (Spinner) view.findViewById(R.id.spinner_hora_clausura);
		
		final TextView textView_lat = (TextView) view.findViewById(R.id.textView_lat);		
		final TextView textView_long = (TextView) view.findViewById(R.id.textView_long);
		
		Button get = (Button) view.findViewById(R.id.button_get);
		get.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				LocationManager service = (LocationManager) getActivity()
//						.getSystemService(MainActivity.LOCATION_SERVICE);
//				Criteria criteria = new Criteria();
//				String provider = service.getBestProvider(criteria, false);
//				Location location = service.getLastKnownLocation(provider);
				
				LatLng userLocation;
				if (mLocation == null) {
					double ltd = -34.558244;
					double lng = -58.444777;
					userLocation = new LatLng(ltd, lng);
				} else {
					userLocation = new LatLng(mLocation.getLatitude(),
							mLocation.getLongitude());
				}
				
				Geocoder gcd = new Geocoder(CreateUbicacionFragment.this.getActivity(), Locale.getDefault());
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
		
		Button button_confirmar = (Button) view.findViewById(R.id.button_confirmar);
		button_confirmar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Ubicacion ubicacion = new Ubicacion();
				final UbicacionPK ubicacionPK = new UbicacionPK();
				
				ubicacionPK.setCamionIdcamion(String.valueOf(camion.getCamionPK().getIdcamion()));
				ubicacionPK.setCamionUsuarioIdusuario(FdTksApplication.getUsuario().getIdusuario());
				ubicacionPK.setIdubicacion("0");
				
				ubicacion.setCamion(camion);
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
				
				ubicacion.setUbicacionPK(ubicacionPK);
				
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
							HttpPost request = new HttpPost(
									"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.ubicacion/create"
									/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.ubicacion/create"*/);
							Gson gson = new Gson();

							request.setHeader("Content-Type","application/json");

							Log.i("JSON Object", gson.toJson(ubicacion).toString());

							try {
								StringEntity se;
								se = new StringEntity(gson.toJson(ubicacion),"UTF-8");
//								se.setContentEncoding("UTF-8");
								se.setContentType("application/json");
								request.setEntity(se);

							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							try {

								HttpResponse response = client.execute(request);
								Log.d("HTTP", response.toString());
								MainActivity.wsis.getConn().sendTextMessage(ubicacion.getCamion().getNombre() +" - "+ 
										ubicacion.getCalle());
								updateBarHandler.post(new Runnable() {

									public void run() {
										pDialog.incrementProgressBy(100);
									}
								});
						
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
						}
						
					}).start();
					
					Log.d("HTTP", "HTTP: OK");
				} catch (Exception e) {
					Log.e("HTTP", "Error in http connection " + e.toString());
				}
				pDialog.dismiss();
				FdTksApplication.getUbicaciones().add(ubicacion);
				getActivity().finish();
				
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
