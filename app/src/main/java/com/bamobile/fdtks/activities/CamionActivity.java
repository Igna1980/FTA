package com.bamobile.fdtks.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.bamobile.fdtks.R;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.fragments.camion.CreateCamionFragment;
import com.bamobile.fdtks.fragments.camion.EditCamionFragment;

public class CamionActivity extends SherlockFragmentActivity implements	TabListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create);

		ActionBar ab = getSupportActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.celeste_oscuro_color)));
        ab.setStackedBackgroundDrawable(getResources().getDrawable(
                R.color.celeste_oscuro_color));
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Camion camion = (Camion) extras.get("camion");
			ab.addTab(ab.newTab().setText(getResources().getString(R.string.editar_camion_label)).setTabListener(this));
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			EditCamionFragment edittrucksFragment = new EditCamionFragment(camion);
			fragmentTransaction.setCustomAnimations(R.anim.slide_left,
					R.anim.slide_left_out, R.anim.slide_right,
					R.anim.slide_right_out);
			fragmentTransaction.add(R.id.container, edittrucksFragment).commit();
			
		} else {
			
			ab.addTab(ab.newTab().setText(getResources().getString(R.string.crear_camion_label)).setTabListener(this));
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			CreateCamionFragment createtrucksFragment = new CreateCamionFragment();
			fragmentTransaction.setCustomAnimations(R.anim.slide_left,
					R.anim.slide_left_out, R.anim.slide_right,
					R.anim.slide_right_out);
			fragmentTransaction.add(R.id.container, createtrucksFragment).commit();
		}

	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
}
