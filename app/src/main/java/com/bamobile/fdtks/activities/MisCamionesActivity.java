package com.bamobile.fdtks.activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.bamobile.fdtks.R;
import com.bamobile.fdtks.fragments.ListMisCamionesFragment;

public class MisCamionesActivity extends SherlockFragmentActivity implements	TabListener {

	private ActionBar ab;
	private ViewPager mPager;
	private static MyAdapter mAdapter;

	public ViewPager getMPager(){
		return mPager;	
	}
	
		
	public static MyAdapter getmAdapter() {
		return mAdapter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_create);

		ab = getSupportActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.celeste_oscuro_color)));
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);

		ab.addTab(ab.newTab().setText(getResources().getString(R.string.mis_camiones)).setTabListener(this));
        ab.setStackedBackgroundDrawable(getResources().getDrawable(
                R.color.celeste_oscuro_color));
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(1);

		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				ab.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

		});
		
		mPager.setOnTouchListener(new View.OnTouchListener() {           
	        @Override
	        public boolean onTouch(View v, MotionEvent event){
	        	mPager.setCurrentItem(mPager.getCurrentItem());
	            return true;
	        }
	    });
		// overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		// mPager.setPageTransformer(true, new DepthPageTransformer());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, (com.actionbarsherlock.view.Menu) menu);

		return super.onCreateOptionsMenu(menu);
	}

	OnMenuItemClickListener loginButtonClickListener = new OnMenuItemClickListener() {

		public boolean onMenuItemClick(MenuItem item) {
			// Builder del login y registro.
			return false;
		}
	};

	@SuppressLint("UseSparseArrays")
	public static class MyAdapter extends FragmentStatePagerAdapter {

		public FragmentManager fm;
		public FragmentTransaction ft;
		public ListMisCamionesFragment homeFragment;

		public MyAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
			homeFragment = new ListMisCamionesFragment();
		}


		@Override
		public Fragment getItem(int arg0) {
			return homeFragment;
		}

		@Override
		public int getCount() {
			return 1;
		}

	}
}