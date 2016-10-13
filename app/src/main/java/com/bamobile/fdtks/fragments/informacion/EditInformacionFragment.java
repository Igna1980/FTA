package com.bamobile.fdtks.fragments.informacion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Informacion;
import com.bamobile.fdtks.fragments.ListMisCamionesFragment;
import com.google.myjson.Gson;
import com.google.myjson.JsonParser;
import com.google.myjson.stream.JsonReader;

@SuppressLint("ValidFragment")
public class EditInformacionFragment extends Fragment {

	private Camion camion;
	private Informacion info;

	private URL url;
	private HttpURLConnection conn;
	
	private Handler updateBarHandler;

	public EditInformacionFragment(Informacion menu, Camion camion) {
		this.info = menu;
		this.camion = camion;
		updateBarHandler = new Handler();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_create_informacion, container,
				false);
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

		final EditText descripcion = (EditText) view.findViewById(R.id.editText_descrip);
		descripcion.setText(info.getDescripcion());
		
		final EditText menu = (EditText) view.findViewById(R.id.editText_comidas);
		menu.setText(info.getMenu()); 
		final EditText informacion = (EditText) view.findViewById(R.id.editText_informacion);
		informacion.setText(info.getInformacion());
		final EditText contacto = (EditText) view.findViewById(R.id.editText_contacto);
		contacto.setText(info.getContacto());
		final EditText facebook = (EditText) view.findViewById(R.id.editText_facebook);
					   facebook.setText(info.getFacebook());
					   facebook.setHint("fb://profile/<id_here> o https://www.facebook.com/<user_name_here>");
		final EditText twitter = (EditText) view.findViewById(R.id.editText_twitter);
					   twitter.setText(info.getTwitter());
					   twitter.setHint("twitter://user?user_id=<id_here> o https://twitter.com/<user_name_here>");
		final EditText instagram = (EditText) view.findViewById(R.id.editText_instagram);
					   instagram.setText(info.getInstagram());
		   			   instagram.setHint("http://instagram.com/<user_name_here>");
		

		final Button botonconfirmar = (Button) view
				.findViewById(R.id.button_confirmar);

		botonconfirmar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				info.setCamion(camion);
				info.setInformacionPK(info.getInformacionPK());
				
				info.setDescripcion(descripcion.getText().toString());
				info.setMenu(menu.getText().toString());
				info.setInformacion(informacion.getText().toString());
				info.setContacto(contacto.getText().toString());
				info.setFacebook(facebook.getText().toString());
				info.setTwitter(twitter.getText().toString());
				info.setInstagram(instagram.getText().toString());
				
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
									"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.informacion/put"
									/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.informacion/put"*/);
							Gson gson = new Gson();

							request.setHeader("Content-Type",
									"application/json");

							Log.i("JSON Object", gson.toJson(info).toString());

							try {
								StringEntity se;
								se = new StringEntity(gson.toJson(info),"UTF-8");
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
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							try {
								url = new URL(
										"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.informacion"
										/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.informacion"*/);
								conn = (HttpURLConnection) url.openConnection();
								conn.setDoInput(true);
								conn.setRequestProperty("Accept",
										"application/json");
								InputStream in = conn.getInputStream();
								JsonReader reader = new JsonReader(
										new InputStreamReader(in));
								JsonParser parser = new JsonParser();
								gson = new Gson();

								String json = gson.toJson(parser.parse(reader));
								JSONArray array = new JSONArray(json);
								Informacion responseMenu = new Informacion();
								FdTksApplication.getInformaciones().clear();
								for (int i = 0; i < array.length(); i++) {
									responseMenu = gson.fromJson(array
											.getJSONObject(i).toString(),
											Informacion.class);
									FdTksApplication.getInformaciones().add(
											responseMenu);
								}
								updateBarHandler.post(new Runnable() {

									public void run() {
										pDialog.incrementProgressBy(100);
									}
								});
							
								getActivity().finish();
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
						
					}).start();
					
					Log.d("HTTP", "HTTP: OK");
				} catch (Exception e) {
					Log.e("HTTP", "Error in http connection " + e.toString());
				}

				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ListMisCamionesFragment.getAdapter()
								.notifyDataSetChanged();
					}
				});
				pDialog.dismiss();
				getActivity().finish();
			}
		});
		return view;
	}
}
