package com.bamobile.fdtks.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.databases.ImageDatabase;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.util.Tools;

import java.util.List;

public class ListFavCamionesAdapter extends BaseAdapter{
	
	private Activity mAct;
	private List<Camion> favcamiones;
	private LayoutInflater inflater;
	
	public ListFavCamionesAdapter(Activity mAct, List<Camion> favcamiones) {
		this.mAct = mAct;
		this.favcamiones = favcamiones;
	}
	
	
	public List<Camion> getFavcamiones() {
		return favcamiones;
	}


	public void setFavcamiones(List<Camion> favcamiones) {
		this.favcamiones = favcamiones;
	}


	@Override
	public int getCount() {
		return favcamiones.size();
	}

	@Override
	public Object getItem(int position) {
		return favcamiones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(((Camion) favcamiones.get(position)).getCamionPK().getIdcamion());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		inflater = (LayoutInflater) mAct.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.nav_window_list_item, null);
		
		Camion camion = favcamiones.get(position);
		
		for(Camion cam : FdTksApplication.getCamiones()){
			if(cam.getNombre().equals(camion.getNombre())){
				camion = cam;
			}
		}
		
		final TextView nombre = (TextView) view.findViewById(R.id.textView_nombre);
		nombre.setText(camion.getNombre());
		
		final ImageView logo = (ImageView) view.findViewById(R.id.imageView_logo);
		
		if(camion.getLogo() != null){
		   	Bitmap image = null;
           // final String imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + camion.getLogo();
            final String imageUrl = camion.getLogo();
            final ImageDatabase imageDb = new ImageDatabase(mAct);
            if (imageDb.exists(imageUrl)) {
                image = imageDb.getImage(imageUrl);
                logo.setImageBitmap(Tools.getRoundedCornerBitmap(image));
            } else {
        		new AsyncTask<Bitmap,Bitmap,Bitmap>(){

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
            imageDb.close();

            }
		}
		
		return view;
	}

}
