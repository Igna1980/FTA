package com.bamobile.fdtks.fragments.informacion;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

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
import com.bamobile.fdtks.entities.InformacionPK;
import com.google.myjson.Gson;


@SuppressLint("ValidFragment")
public class CreateInformacionFragment extends Fragment{
	public static final int ITEM_FRAGMENT = 3;
	
	private Camion camion;
	
	private Handler updateBarHandler;
	
	public CreateInformacionFragment(Camion camion) {
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
		
		View view = inflater.inflate(R.layout.fragment_create_informacion, container, false);
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
		final EditText menu = (EditText) view.findViewById(R.id.editText_comidas);
		final EditText informacion = (EditText) view.findViewById(R.id.editText_informacion);
		
		final EditText web = (EditText) view.findViewById(R.id.editText_web);
					   web.setHint("http://wwww.<site_here>.com");
		final EditText contacto = (EditText) view.findViewById(R.id.editText_contacto);
					   contacto.setHint("bamobile@gmail.com");
		final EditText facebook = (EditText) view.findViewById(R.id.editText_facebook);
					   facebook.setHint("fb://profile/<id_here> o https://www.facebook.com/<user_name_here>");
		final EditText twitter = (EditText) view.findViewById(R.id.editText_twitter);
					   twitter.setHint("twitter://user?user_id=<id_here> o https://twitter.com/<user_name_here>");
		final EditText instagram = (EditText) view.findViewById(R.id.editText_instagram);
					   instagram.setHint("http://instagram.com/<user_name_here>");
		
		final Button botonconfirmar = (Button) view.findViewById(R.id.button_confirmar);
		
		botonconfirmar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Informacion info = new Informacion();
				InformacionPK infoPk = new InformacionPK();
				
				infoPk.setCamionIdcamion(String.valueOf(camion.getCamionPK().getIdcamion()));
				infoPk.setCamionUsuarioIdUsuario(FdTksApplication.getUsuario().getIdusuario());
				infoPk.setIdinformacion("0");
				
				info.setCamion(camion);
				info.setInformacionPK(infoPk);
				info.setDescripcion(descripcion.getText().toString());
				info.setMenu(menu.getText().toString());
				info.setInformacion(informacion.getText().toString());
				info.setWeb(web.getText().toString());
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
						HttpPost request = new HttpPost(
								"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.informacion/create"
								/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.informacion/create"*/);
						Gson gson = new Gson();

						request.setHeader("Content-Type","application/json");

						Log.i("JSON Object", gson.toJson(info).toString());

						try {
							StringEntity se;
							se = new StringEntity(gson.toJson(info),"UTF-8");
//							se.setContentEncoding("UTF-8");
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
						updateBarHandler.post(new Runnable() {

							public void run() {
								pDialog.incrementProgressBy(100);
							}
						});
						FdTksApplication.getInformaciones().add(info);
					}
					
				}).start();
				
				Log.d("HTTP", "HTTP: OK");
			} catch (Exception e) {
				Log.e("HTTP", "Error in http connection " + e.toString());
			}
			FdTksApplication.getInformaciones().add(info);
			pDialog.dismiss();
			getActivity().finish();
		}
	});
		
		return view;
	}

}
