package com.bamobile.fdtks.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.bamobile.fdtks.R;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Informacion;
import com.bamobile.fdtks.fragments.informacion.CreateInformacionFragment;
import com.bamobile.fdtks.fragments.informacion.EditInformacionFragment;

public class InformacionActivity extends SherlockFragmentActivity implements	TabListener{
	
	private ActionBar ab;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_create);

		ab = getSupportActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.celeste_oscuro_color)));
		ab.setStackedBackgroundDrawable(getResources().getDrawable(
				R.color.celeste_oscuro_color));
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);

		Bundle extras = getIntent().getExtras();
		
		Camion camion = (Camion)extras.get("camion");
		Informacion menu = (Informacion) extras.get("menu");
		
		if (menu == null) {
			ab.addTab(ab.newTab().setText(getResources().getString(R.string.crear_info_label)).setTabListener(this));
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			CreateInformacionFragment createMenuFragment = new CreateInformacionFragment(camion);
			fragmentTransaction.setCustomAnimations(R.anim.slide_left,
					R.anim.slide_left_out, R.anim.slide_right,
					R.anim.slide_right_out);
			fragmentTransaction.add(R.id.container, createMenuFragment).commit();

		} else {
			ab.addTab(ab.newTab().setText(getResources().getString(R.string.editar_info_label)).setTabListener(this));
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			EditInformacionFragment editMenuFragment = new EditInformacionFragment(menu, camion);
			fragmentTransaction.setCustomAnimations(R.anim.slide_left,
					R.anim.slide_left_out, R.anim.slide_right,
					R.anim.slide_right_out);
			fragmentTransaction	.add(R.id.container, editMenuFragment).commit();
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
