package com.bamobile.fdtks.fragments;

import java.util.List;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.MainActivity;
import com.bamobile.fdtks.adapters.TrucksFragmentAdapter;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.entities.Ubicacion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class TrucksFragment extends Fragment{
	
	private GridView gridView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public GridView getGridView() {
		return gridView;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_trucks, null);
		gridView =  (GridView) view.findViewById(R.id.grid_view);
		gridView.setAdapter(new TrucksFragmentAdapter(TrucksFragment.this, FdTksApplication.getCamiones())); // uses the view to get the context instead of getActivity().
		gridView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Toast.makeText(getActivity(),
								FdTksApplication.getCamiones().get(arg2).getNombre(),
								Toast.LENGTH_SHORT).show();
						((MainActivity)getActivity()).setCamionSeleccionado(getCamionLocacion(FdTksApplication.getCamiones().get(arg2).getCamionPK().getIdcamion()));

					}
				});
		return view;
	}
	
	private Ubicacion getCamionLocacion(String idCamion){
		Ubicacion ubicacion = new Ubicacion();
		List<Ubicacion> ubicaciones = FdTksApplication.getUbicaciones();
		for(Ubicacion ubicacionTemp : ubicaciones){
			if(ubicacionTemp.getCamion().getCamionPK().getIdcamion().equals(idCamion)){
				ubicacion = ubicacionTemp;
			}
		}
		return ubicacion;
	}

}
