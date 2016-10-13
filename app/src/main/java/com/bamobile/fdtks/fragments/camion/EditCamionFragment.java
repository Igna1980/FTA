package com.bamobile.fdtks.fragments.camion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.MainActivity;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.databases.ImageDatabase;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.fragments.ListMisCamionesFragment;
import com.bamobile.fdtks.util.Tools;
import com.google.myjson.Gson;
import com.google.myjson.JsonParser;
import com.google.myjson.annotations.SerializedName;
import com.google.myjson.stream.JsonReader;

@SuppressLint("ValidFragment")
public class EditCamionFragment extends Fragment {

	private Camion camion;

	private static final String FOTO1 = "foto1";
	private static final String FOTO2 = "foto2";
	private static final String FOTO_LOGO = "logo";
	private static String IMAGEN = "imagen";

	private ImageView foto1, foto2, logo;
	private EditText nombre;
	private Spinner tipo;
	private Button confirmar;
	private static int RESULT_LOAD_IMG = 1;

	String[] imgPath = new String[3];
	String[] fileName = new String[3];
	Bitmap[] bitmap = new Bitmap[3];
	String[] encodedString = new String[3];

	private static URL url;
	private static HttpURLConnection conn;
	
	private Handler updateBarHandler;

	public EditCamionFragment(Camion camion) {
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

		View view = inflater.inflate(R.layout.fragment_create_camion,
				container, false);
		foto1 = (ImageView) view.findViewById(R.id.imagen_foto_portada);
		foto1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Create intent to Open Image applications like Gallery, Google
				// Photos
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				IMAGEN = FOTO1;
				// Start the Intent
				startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
			}
		});
		if(camion.getFoto1() != null){
		   	Bitmap image = null;
	        final String imageUrl = camion.getFoto1();
	       // final String imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + camion.getFoto1();
            final ImageDatabase imageDb = new ImageDatabase(getActivity());
            if (imageDb.exists(imageUrl)) {
                image = imageDb.getImage(imageUrl);
            } else {
            	new AsyncTask<Bitmap, Bitmap, Bitmap>(){

					@Override
					protected Bitmap doInBackground(Bitmap... params) {
		                params[0] = Tools.getBitmapFromURL(imageUrl);
		                imageDb.addImage(imageUrl, params[0]);
						return params[0];
					}
            		
					@Override
					protected void onPostExecute(Bitmap result) {
						foto1.setImageBitmap(result);
						super.onPostExecute(result);
					}
            	}.execute(image);
            }
            imageDb.close();
			foto1.setImageBitmap(image);
		}
		foto2 = (ImageView) view.findViewById(R.id.imagen_otra_foto);
		foto2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Create intent to Open Image applications like Gallery, Google
				// Photos
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				IMAGEN = FOTO2;
				// Start the Intent
				startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
			}
		});
		if(camion.getFoto2() != null){
		   	Bitmap image = null;
            final String imageUrl = camion.getFoto2();
		  // final String imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + camion.getFoto2();
            final ImageDatabase imageDb = new ImageDatabase(getActivity());
            if (imageDb.exists(imageUrl)) {
                image = imageDb.getImage(imageUrl);
    			foto2.setImageBitmap(image);
            } else {
            	new AsyncTask<Bitmap, Bitmap, Bitmap>(){

					@Override
					protected Bitmap doInBackground(Bitmap... params) {
		                params[0] = Tools.getBitmapFromURL(imageUrl);
		                imageDb.addImage(imageUrl, params[0]);
						return params[0];
					}
            		
					@Override
					protected void onPostExecute(Bitmap result) {
						foto2.setImageBitmap(result);
						super.onPostExecute(result);
					}
            	}.execute(image);
            	
            }
            imageDb.close();

		}	
		logo = (ImageView) view.findViewById(R.id.imagen_logo);
		logo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Create intent to Open Image applications like Gallery, Google
				// Photos
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				IMAGEN = FOTO_LOGO;
				// Start the Intent
				startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
			}
		});
		if(camion.getLogo() != null){
		   	Bitmap image = null;
            final String imageUrl = camion.getLogo();
		  // final String imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + camion.getLogo();
            final ImageDatabase imageDb = new ImageDatabase(getActivity());
            if (imageDb.exists(imageUrl)) {
                image = imageDb.getImage(imageUrl);
            } else {
            	new AsyncTask<Bitmap, Bitmap, Bitmap>(){

					@Override
					protected Bitmap doInBackground(Bitmap... params) {
		                params[0] = Tools.getBitmapFromURL(imageUrl);
		                imageDb.addImage(imageUrl, params[0]);
						return params[0];
					}
            		
					@Override
					protected void onPostExecute(Bitmap result) {
						logo.setImageBitmap(result);
						super.onPostExecute(result);
					}
            	}.execute(image);
            }
            imageDb.close();
			logo.setImageBitmap(image);
		}
		nombre = (EditText) view.findViewById(R.id.editText_horario_dialog);
		nombre.setText(camion.getNombre());
		tipo = (Spinner) view.findViewById(R.id.spinner_tipo);
		confirmar = (Button) view.findViewById(R.id.button_create_dialog);
		confirmar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Camion newCamion = new Camion();

				if (fileName[0] != null) {
					camion.setFoto1(fileName[0]);
				}

				if (fileName[1] != null) {
					camion.setFoto2(fileName[1]);
				}

				if (fileName[2] != null) {
					camion.setLogo(fileName[2]);
				}

				camion.setNombre(nombre.getText().toString());
				camion.setTipo(tipo.getSelectedItem().toString());
				
				final ProgressDialog pDialog = new ProgressDialog(getActivity());
				pDialog.setTitle(getResources().getString(R.string.uploading));
				pDialog.setMessage(getResources().getString(R.string.progress));
				pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pDialog.setMax(50);
				pDialog.setProgress(0);
				pDialog.show();
				
				try {

					new Thread(new Runnable() {
						@Override
						public void run() {
							
							HttpParams httpParams = new BasicHttpParams();
							HttpClient client = new DefaultHttpClient(
									httpParams);
							HttpPut put = new HttpPut(
									"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.camion/put"
									/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.camion/put"*/);
							Gson gson = new Gson();

							put.setHeader("Content-Type", "application/json");

							Log.i("JSON Object", gson.toJson(newCamion)
									.toString());

							try {
								StringEntity se;
								se = new StringEntity(gson.toJson(camion),"UTF-8");
//								se.setContentEncoding("UTF-8");
								se.setContentType("application/json");
								put.setEntity(se);

							} catch (UnsupportedEncodingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							try {

								HttpResponse response = client.execute(put);
								Log.d("HTTP", response.toString());
								updateBarHandler.post(new Runnable() {

									public void run() {
										pDialog.incrementProgressBy(10);
									}
								});
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							for (int i = 0; i < bitmap.length; i++) {
								// Put converted Image string into Async Http
								// Post param
								updateBarHandler.post(new Runnable() {

									public void run() {
										pDialog.incrementProgressBy(10);
									}
								});
								// Trigger Image upload
								Imagen newImagen = new Imagen(encodedString[i],
										fileName[i]);

								client = new DefaultHttpClient();
								// Don't forget to change the IP address to your
								// LAN address. Port no as well.
								HttpPost request = new HttpPost(
										"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.camion/manipulateImage/"
										/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.camion/manipulateImage/"*/);
								try {
									StringEntity se;
									se = new StringEntity(gson
											.toJson(newImagen));
									se.setContentEncoding("UTF-8");
									se.setContentType("application/json");
									request.setEntity(se);

								} catch (UnsupportedEncodingException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								try {
									HttpResponse response = client
											.execute(request);
									Log.d("HTTP", response.toString());

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							updateBarHandler.post(new Runnable() {

								public void run() {
									pDialog.incrementProgressBy(10);
								}
							});
							try {

								url = new URL(
										"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.camion"
										/*"http://192.168.0.3:8084/FdTrcksArg/webresources/entities.camion"*/);
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

								Camion responseCamion = new Camion();
								FdTksApplication.getCamiones().clear();
								for (int i = 0; i < array.length(); i++) {
									responseCamion = gson.fromJson(array.getJSONObject(i).toString(), Camion.class);
									FdTksApplication.getCamiones().add(responseCamion);
								}

								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										ListMisCamionesFragment.getAdapter().setMiscamiones(
														FdTksApplication.getCamionesxUsuario());
										ListMisCamionesFragment.getAdapter().notifyDataSetChanged();
										getActivity().finish();
									}
								});
							} catch (Exception e) {
								// TODO: handle exception
							}
							pDialog.dismiss();
						}
						
					}).start();
					
					Log.d("HTTP", "HTTP: OK");
				} catch (Exception e) {
					Log.e("HTTP", "Error in http connection " + e.toString());
				}
			}
		});

		return view;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			// When an Image is picked
			if (requestCode == RESULT_LOAD_IMG
					&& resultCode == MainActivity.RESULT_OK && null != data) {
				// Get the Image from data

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				// Get the cursor
				Cursor cursor = getActivity().getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				// Move to first row
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imgDecodableString = cursor.getString(columnIndex);
				cursor.close();

				if (IMAGEN == EditCamionFragment.FOTO1) {
					ImageView imgView = (ImageView) getActivity().findViewById(
							R.id.imagen_foto_portada);
					// Set the Image in ImageView after decoding the String
					bitmap[0] = BitmapFactory.decodeFile(imgDecodableString);
					imgView.setImageBitmap(bitmap[0]);
					// Get the Image's file name
					imgPath[0] = imgDecodableString;
					String fileNameSegments[] = imgPath[0].split("/");
					fileName[0] = fileNameSegments[fileNameSegments.length - 1];
					// Put file name in Async Http Post Param which will used in
					// Java web app
					BitmapFactory.Options options = null;
					options = new BitmapFactory.Options();
					options.inSampleSize = 3;
					bitmap[0] = BitmapFactory.decodeFile(imgPath[0], options);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					// Must compress the Image to reduce image size to make
					// upload easy
					bitmap[0].compress(Bitmap.CompressFormat.PNG, 50, stream);
					byte[] byte_arr = stream.toByteArray();
					// Encode Image to String
					encodedString[0] = Base64.encodeToString(byte_arr, 0);

				} else if (IMAGEN == EditCamionFragment.FOTO2) {
					ImageView imgView = (ImageView) getActivity().findViewById(
							R.id.imagen_otra_foto);
					// Set the Image in ImageView after decoding the String
					bitmap[1] = BitmapFactory.decodeFile(imgDecodableString);
					imgView.setImageBitmap(bitmap[1]);
					// Get the Image's file name
					imgPath[1] = imgDecodableString;
					String fileNameSegments[] = imgPath[1].split("/");
					fileName[1] = fileNameSegments[fileNameSegments.length - 1];
					BitmapFactory.Options options = null;
					options = new BitmapFactory.Options();
					options.inSampleSize = 3;
					bitmap[1] = BitmapFactory.decodeFile(imgPath[1], options);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					// Must compress the Image to reduce image size to make
					// upload easy
					bitmap[1].compress(Bitmap.CompressFormat.PNG, 50, stream);
					byte[] byte_arr = stream.toByteArray();
					// Encode Image to String
					encodedString[1] = Base64.encodeToString(byte_arr, 0);

				} else {
					ImageView imgView = (ImageView) getActivity().findViewById(
							R.id.imagen_logo);
					// Set the Image in ImageView after decoding the String
					bitmap[2] = BitmapFactory.decodeFile(imgDecodableString);
					imgView.setImageBitmap(bitmap[2]);
					// Get the Image's file name
					imgPath[2] = imgDecodableString;
					String fileNameSegments[] = imgPath[2].split("/");
					fileName[2] = fileNameSegments[fileNameSegments.length - 1];
					BitmapFactory.Options options = null;
					options = new BitmapFactory.Options();
					options.inSampleSize = 3;
					bitmap[2] = BitmapFactory.decodeFile(imgPath[2], options);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					// Must compress the Image to reduce image size to make
					// upload easy
					bitmap[2].compress(Bitmap.CompressFormat.PNG, 50, stream);
					byte[] byte_arr = stream.toByteArray();
					// Encode Image to String
					encodedString[2] = Base64.encodeToString(byte_arr, 0);
				}

			} else {
				Toast.makeText(this.getActivity(),  getResources().getString(R.string.image_required),
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this.getActivity(), getResources().getString(R.string.something_went_wrong),
					Toast.LENGTH_LONG).show();
		}

	}

	class Imagen {
		@SerializedName("encodedString")
		private String encodedString;

		@SerializedName("filename")
		private String filename;

		public Imagen() {
			// TODO Auto-generated constructor stub
		}

		public Imagen(String encodedString, String filename) {
			super();
			this.encodedString = encodedString;
			this.filename = filename;
		}

		public String getEncodedString() {
			return encodedString;
		}

		public void setEncodedString(String encodedString) {
			this.encodedString = encodedString;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}
	}

}
