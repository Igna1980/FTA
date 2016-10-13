package com.bamobile.fdtks.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.CamionActivity;
import com.bamobile.fdtks.adapters.ListMisCamionesAdapter;
import com.bamobile.fdtks.application.FdTksApplication;

public class ListMisCamionesFragment extends SherlockFragment {

	private ListView listview;
	private View view;
	private Button button_crear_nuevo_camion;
	
	private static ListMisCamionesAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public static ListMisCamionesAdapter getAdapter(){
		return adapter;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_mis_trucks, null);
		listview = (ListView) view.findViewById(R.id.listView_mis_camiones);
		button_crear_nuevo_camion = (Button) view.findViewById(R.id.button_crear_nuevo_camion);
		button_crear_nuevo_camion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent newCamion = new Intent(getActivity(), CamionActivity.class);
				startActivity(newCamion);
			}
		});
		adapter = new ListMisCamionesAdapter(this, FdTksApplication.getCamionesxUsuario());     
		listview.setAdapter(adapter);
		return view;
	}
}
