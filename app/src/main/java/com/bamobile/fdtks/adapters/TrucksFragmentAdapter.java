package com.bamobile.fdtks.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.MainActivity;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.fragments.TrucksFragment;

public class TrucksFragmentAdapter extends BaseAdapter{
	
	private List<Camion> camiones;
	private Context mContext;
	private LayoutInflater inflater;
	
	public TrucksFragmentAdapter (final TrucksFragment trucksFragment, final List<Camion> camiones){
		mContext = trucksFragment.getActivity();
		this.camiones = camiones;
	}

	@Override
	public int getCount() {
		return camiones.size();
	}

	@Override
	public Object getItem(int position) {
		return camiones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.fragment_trucks_item, parent, false);

			TextView name = (TextView) v.findViewById(R.id.nombre_camion);
			ImageView avatar = (ImageView) v.findViewById(R.id.logo_camion);

			name.setText(camiones.get(position).getNombre());


		return v;
	}

}
