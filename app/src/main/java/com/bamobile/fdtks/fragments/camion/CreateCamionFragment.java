package com.bamobile.fdtks.fragments.camion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.bamobile.fdtks.entities.CamionPK;
import com.bamobile.fdtks.fragments.ListMisCamionesFragment;
import com.google.myjson.Gson;
import com.google.myjson.annotations.SerializedName;

public class CreateCamionFragment extends Fragment {

	public static final int ITEM_FRAGMENT = 1;

	private static final String FOTO1 = "foto1";
	private static final String FOTO2 = "foto2";
	private static final String FOTO_LOGO = "logo";
	private static String IMAGEN = "imagen";
	
	private static boolean foto1Set = false;
	private static boolean foto2Set = false;
	private static boolean fotoLogoSet = false;
	
	private ImageView foto1, foto2, logo;
	private EditText nombre;
	private Spinner tipo;
	private Button confirmar;
	private static int RESULT_LOAD_IMG = 1;

	String[] imgPath = new String[3];
	String[] fileName = new String[3];
	Bitmap[] bitmap = new Bitmap[3];
	String[] encodedString = new String[3];
	
	private Handler updateBarHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateBarHandler = new Handler();
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

		nombre = (EditText) view.findViewById(R.id.editText_horario_dialog);
		nombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(nombre.getText().toString().equals("")){
					nombre.setError(getResources().getString(R.string.nombre_camion));
				}
			}
		});
		
		tipo = (Spinner) view.findViewById(R.id.spinner_tipo);
		confirmar = (Button) view.findViewById(R.id.button_create_dialog);
		confirmar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (CreateCamionFragment.foto1Set && CreateCamionFragment.foto2Set && CreateCamionFragment.fotoLogoSet) {
					
					final Camion newCamion = new Camion();
					final CamionPK newCamionPK = new CamionPK();

					newCamionPK.setIdcamion("0");
					newCamionPK.setUsuarioIdusuario(FdTksApplication
							.getUsuario().getIdusuario());

					newCamion.setFoto1(fileName[0]);
					newCamion.setFoto2(fileName[1]);
					newCamion.setLogo(fileName[2]);
					newCamion.setNombre(nombre.getText().toString());
					newCamion.setTipo(tipo.getSelectedItem().toString());
					newCamion.setCamionPK(newCamionPK);
					newCamion.setUsuario(FdTksApplication.getUsuario());

					
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
								HttpPost request = new HttpPost(
										"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.camion/create"
								/* "http://192.168.0.3:8084/FdTrcksArg/webresources/entities.camion/create" */);
								Gson gson = new Gson();

								request.setHeader("Content-Type",
										"application/json");

								Log.i("JSON Object", gson.toJson(newCamion)
										.toString());
								// Here you should write your time consuming task...
								
								try {
									StringEntity se;
									se = new StringEntity(gson
											.toJson(newCamion), "UTF-8");
									// se.setContentEncoding("UTF-8");
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
									updateBarHandler.post(new Runnable() {

										public void run() {
											pDialog.incrementProgressBy(10);
										}
									});
									// Trigger Image upload
									Imagen newImagen = new Imagen(
											encodedString[i], fileName[i]);

									client = new DefaultHttpClient();

									request = new HttpPost(
											"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.camion/manipulateImage/"
									/* "http://192.168.0.3:8084/FdTrcksArg/webresources/entities.camion/manipulateImage/" */);
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

										ImageDatabase imageDb = new ImageDatabase(
												getActivity());
										if (imageDb.exists(fileName[i])) {

										} else {
											imageDb.addImage(fileName[i],
													bitmap[i]);
										}
										imageDb.close();
										Log.d("HTTP", response.toString());
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								updateBarHandler.post(new Runnable() {

									public void run() {
										pDialog.incrementProgressBy(10);
										pDialog.dismiss();
									}
								});
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										FdTksApplication.getCamiones().add(
												newCamion);
										ListMisCamionesFragment
												.getAdapter()
												.setMiscamiones(
														FdTksApplication
																.getCamionesxUsuario());
										ListMisCamionesFragment.getAdapter()
												.notifyDataSetChanged();
										getActivity().finish();
									}
								});
							}
							
						}).start();
						
						Log.d("HTTP", "HTTP: OK");
					} catch (Exception e) {
						Log.e("HTTP",
								"Error in http connection " + e.toString());
					}

				} else {
					Toast.makeText(CreateCamionFragment.this.getActivity(), getResources().getString(R.string.image_required),
							Toast.LENGTH_LONG).show();
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

				if (IMAGEN == CreateCamionFragment.FOTO1) {
					ImageView imgView = (ImageView) getActivity().findViewById(
							R.id.imagen_foto_portada);
					// Get the Image's file name
					imgPath[0] = imgDecodableString;
					String fileNameSegments[] = imgPath[0].split("/");
					fileName[0] = fileNameSegments[fileNameSegments.length - 1];
					BitmapFactory.Options options = null;
					options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					bitmap[0] = BitmapFactory.decodeFile(imgPath[0],options);
					bitmap[0] = Bitmap.createScaledBitmap(bitmap[0], imgView.getWidth(), imgView.getHeight(), true);
					// Set the Image in ImageView after decoding the String
					imgView.setImageBitmap(bitmap[0]);					
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					// Must compress the Image to reduce image size to make
					// upload easy
					bitmap[0].compress(Bitmap.CompressFormat.PNG, 50, stream);
					byte[] byte_arr = stream.toByteArray();
					// Encode Image to String
					encodedString[0] = Base64.encodeToString(byte_arr, 0);
					CreateCamionFragment.foto1Set = true;

				} else if (IMAGEN == CreateCamionFragment.FOTO2) {
					ImageView imgView = (ImageView) getActivity().findViewById(
							R.id.imagen_otra_foto);
					// Get the Image's file name
					imgPath[1] = imgDecodableString;
					String fileNameSegments[] = imgPath[1].split("/");
					fileName[1] = fileNameSegments[fileNameSegments.length - 1];
					BitmapFactory.Options options = null;
					options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					bitmap[1] = BitmapFactory.decodeFile(imgPath[1],options);
					bitmap[1] = Bitmap.createScaledBitmap(bitmap[1], imgView.getWidth(), imgView.getHeight(), false);
					// Set the Image in ImageView after decoding the String
					imgView.setImageBitmap(bitmap[1]);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					// Must compress the Image to reduce image size to make
					// upload easy
					bitmap[1].compress(Bitmap.CompressFormat.PNG, 50, stream);
					byte[] byte_arr = stream.toByteArray();
					// Encode Image to String
					encodedString[1] = Base64.encodeToString(byte_arr, 0);
					CreateCamionFragment.foto2Set = true;
					
				} else {
					ImageView imgView = (ImageView) getActivity().findViewById(
							R.id.imagen_logo);
					// Get the Image's file name
					imgPath[2] = imgDecodableString;
					String fileNameSegments[] = imgPath[2].split("/");
					fileName[2] = fileNameSegments[fileNameSegments.length - 1];
					BitmapFactory.Options options = null;
					options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					bitmap[2] = BitmapFactory.decodeFile(imgPath[2],options);
					bitmap[2] = Bitmap.createScaledBitmap(bitmap[2], imgView.getWidth(), imgView.getHeight(), false);
					// Set the Image in ImageView after decoding the String
					imgView.setImageBitmap(bitmap[2]);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					// Must compress the Image to reduce image size to make
					// upload easy
					bitmap[2].compress(Bitmap.CompressFormat.PNG, 50, stream);
					byte[] byte_arr = stream.toByteArray();
					// Encode Image to String
					encodedString[2] = Base64.encodeToString(byte_arr, 0);
					CreateCamionFragment.fotoLogoSet = true;
				}

			} else {
				Toast.makeText(this.getActivity(), getResources().getString(R.string.image_required),
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this.getActivity(), getResources().getString(R.string.something_went_wrong),
					Toast.LENGTH_LONG).show();
		}

	}

	public class Imagen {
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
