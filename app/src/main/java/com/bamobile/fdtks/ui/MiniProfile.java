package com.bamobile.fdtks.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.MainActivity;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.databases.TrucksDataBase;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Informacion;
import com.bamobile.fdtks.entities.Ubicacion;
import com.bamobile.fdtks.fragments.MapFragment;
import com.google.myjson.Gson;

public class MiniProfile extends Dialog{
	
	private RelativeLayout dialog_close;
	private LinearLayout mLinearLayout;
	private TextView nombre_text, barrio_text, calle_text, desde_text, hasta_text, horario_text;
	private TextView descrip_text, menu_text, info_text;
	private ImageView fcbk_button, twt_button, insta_button, contact_button, web_button, report_button;
	private ImageView starred;
	
	private Camion camion;

	public MiniProfile(final MapFragment context, final Camion camion, final Informacion info, final Ubicacion ubicacion) {
		super(context.getActivity());
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCancelable(false);
		this.camion = camion;
		this.setContentView(R.layout.dialog_mini_profile);
		
		mLinearLayout = (LinearLayout) findViewById(R.id.mLinearLayout);
		BitmapDrawable background = new BitmapDrawable(context.getResources(), FdTksApplication
						.getImagen1xcamion().get(
								camion));
		mLinearLayout.setBackgroundDrawable(background);
		
		dialog_close = (RelativeLayout) findViewById(R.id.dialog_close);
		dialog_close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MiniProfile.this.cancel();
			}
		});
		
		starred = (ImageView) findViewById(R.id.imageView2);
		

        
        final TrucksDataBase trucksDb = new TrucksDataBase(context.getActivity());
        if (trucksDb.exists(camion)) {
            final Camion camionLocal = trucksDb.getCamion(camion);
			starred.setImageResource(R.drawable.ico_favorite_user_profile);
			starred.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				        trucksDb.addRenoveFavCam(camionLocal);	   
						MiniProfile.this.starred.setImageResource(R.drawable.ico_favorite_user_profile_unselected);
						MainActivity.getInstance().mFavAdapter.getFavcamiones().remove(camionLocal);
						MainActivity.getInstance().mFavAdapter.notifyDataSetChanged();
				}
			});
        } else {
			starred.setImageResource(R.drawable.ico_favorite_user_profile_unselected);
			starred.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				        trucksDb.addRenoveFavCam(camion);    
						MiniProfile.this.starred.setImageResource(R.drawable.ico_favorite_user_profile);
						MainActivity.getInstance().mFavAdapter.getFavcamiones().add(camion);
						MainActivity.getInstance().mFavAdapter.notifyDataSetChanged();
				}
			});
        }
        trucksDb.close();
		
		nombre_text = (TextView)findViewById(R.id.textView_nombre);
		nombre_text.setText(camion.getNombre());
		
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		
		barrio_text = (TextView)findViewById(R.id.textView_barrio);
		barrio_text.setText(ubicacion.getBarrio());
		calle_text = (TextView)findViewById(R.id.textView_calle);
		calle_text.setText(ubicacion.getCalle());		
		desde_text = (TextView) findViewById(R.id.textView_desde);
		desde_text.setText(ubicacion.getDesde());
		hasta_text = (TextView) findViewById(R.id.textView_hasta);
		hasta_text.setText(ubicacion.getHasta());
		horario_text = (TextView)findViewById(R.id.textView_horario);
		horario_text.setText(ubicacion.getHorario() +" hs.");
		
		descrip_text = (TextView)findViewById(R.id.TextView_descrip);
		descrip_text.setText(info.getDescripcion());
		menu_text = (TextView)findViewById(R.id.TextView_comidas); 
		menu_text.setText(info.getMenu());		
		info_text = (TextView)findViewById(R.id.TextView_informacion);
		info_text.setText(info.getInformacion());
		
		fcbk_button = (ImageView) findViewById(R.id.imageButton_facebook);
		if(info.getFacebook() != null){
			fcbk_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					   try {
						    context.getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0);
						    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(info.getFacebook())));
						   } catch (Exception e) {
							context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(info.getFacebook())));
						   }			
				}
			});
		}		
		twt_button = (ImageView) findViewById(R.id.imageButton_twitter);
		if(info.getTwitter() != null){
			twt_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = null;
					try {
					    // get the Twitter app if possible
					    context.getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
					    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
					    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					} catch (Exception e) {
					    // no Twitter app, revert to browser
					    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/PROFILENAME"));
					}
					context.startActivity(intent);			
				}
			});
		}
		insta_button = (ImageView) findViewById(R.id.imageButton_instragram);
		if(info.getInstagram() != null){
			insta_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					   Uri uri = Uri.parse("http://instagram.com/_u/xxx");
					    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
	
					    likeIng.setPackage("com.instagram.android");
	
					    try {
					        context.startActivity(likeIng);
					    } catch (ActivityNotFoundException e) {
					    	context.startActivity(new Intent(Intent.ACTION_VIEW,
					                Uri.parse(info.getInstagram())));
					    }		
				}
			});
		}
		
		web_button = (ImageView) findViewById(R.id.button_web);
		if(info.getWeb() != null){
			web_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(info.getWeb()));
					context.startActivity(intent);
					MiniProfile.this.cancel();
				}
			});
		}
		
		contact_button = (ImageView) findViewById(R.id.imageButton_contacto);
		if(info.getContacto() != null){
			contact_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_SENDTO);
					intent.setData(Uri.parse("mailto:" + info.getContacto()));
					context.startActivity(intent);		
				}
			});
		}
		
		report_button = (ImageView) findViewById(R.id.button_report);
		report_button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context.getActivity());
				      
				      alertDialogBuilder.setMessage(R.string.decision);
				      alertDialogBuilder.setPositiveButton(R.string.positive_button, 
				      
				     new DialogInterface.OnClickListener() {
						
				         @Override
				         public void onClick(DialogInterface arg0, int arg1) {
				        	 MiniProfile.this.camion.setReports(MiniProfile.this.camion.getReports() + 1);
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

										try {
											StringEntity se;
											se = new StringEntity(gson.toJson(MiniProfile.this.camion),"UTF-8");
//											se.setContentEncoding("UTF-8");
											se.setContentType("application/json");
											put.setEntity(se);

										} catch (UnsupportedEncodingException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}

										try {

											HttpResponse response = client.execute(put);
											Log.d("HTTP", response.toString());
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
				        	 }).start();
				         }
				      });
				      
				      alertDialogBuilder.setNegativeButton(R.string.negative_button, 
				      new DialogInterface.OnClickListener() {
							
				         @Override
				         public void onClick(DialogInterface dialog, int which) {
						 }
				      });
				      
				      final AlertDialog alertDialog = alertDialogBuilder.create();
				      alertDialog.show();	
				}
		});
	}
	
}
