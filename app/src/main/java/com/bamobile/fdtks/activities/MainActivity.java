package com.bamobile.fdtks.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.bamobile.fdtks.R;
import com.bamobile.fdtks.adapters.ListFavCamionesAdapter;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.databases.TrucksDataBase;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Ubicacion;
import com.bamobile.fdtks.entities.Usuario;
import com.bamobile.fdtks.fragments.MapFragment;
import com.bamobile.fdtks.services.WebSocketIntentService;
import com.bamobile.fdtks.util.GMapV2Direction;
import com.bamobile.fdtks.util.RestAPI;
import com.bamobile.fdtks.util.ServiceGenerator;
import com.google.myjson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class MainActivity extends SherlockFragmentActivity implements
		TabListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CheckBox mCheckBox;
	private ActionBarDrawerToggle mDrawerToggle;
	public ListFavCamionesAdapter mFavAdapter;
	private LinearLayout mLinear;
	private ImageView mImage;
	
	private static MyAdapter mAdapter;

	private ViewPager mPager;
	private ActionBar ab;

	private String user;
	private String pass;
	public static WebSocketIntentService wsis;

	private EditText user_nombre, user_password, user_mail, user_tel;

	public static MainActivity _this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mLinear = (LinearLayout) findViewById(R.id.container_drawer);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mImage = (ImageView)findViewById(R.id.imageView_drawer);
		mImage.setImageDrawable(getResources().getDrawable(R.drawable.logo_90x90));
		mCheckBox = (CheckBox) findViewById(R.id.checkBox_drawer);
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		boolean receive = prefs.getBoolean("notifications", false);
		
		if(receive){
			mCheckBox.setSelected(true);
		} else {
			mCheckBox.setSelected(false);
		}
		
		mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this.getApplicationContext());
				prefs.edit().putBoolean("notifications", isChecked).commit();
			}
		});
		
		TrucksDataBase trucksDb = new TrucksDataBase(this);
		final List<Camion> fav_trucks = trucksDb.findAll();
		trucksDb.close();
		
		mFavAdapter = new ListFavCamionesAdapter(this, fav_trucks) ;
		mDrawerList.setAdapter(mFavAdapter);

 
		// Set a custom shadow that overlays the main content when the drawer
		// opens
	    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
					GravityCompat.START);
	    

	     // Set the default content area to item 0
        // when the app opens for the first time
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

		ab = getSupportActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.celeste_oscuro_color)));
		ab.setDisplayShowTitleEnabled(true);
		ab.setIcon(R.drawable.logo_90x90);
		//ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);
		ab.setHomeButtonEnabled(true);
		ab.setDisplayHomeAsUpEnabled(true);
		// ab.addTab(ab.newTab().setText("Ubicaciones").setTabListener(this));
		//ab.setText("Camiones").setTabListener(this));

		// overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		// mPager.setPageTransformer(true, new DepthPageTransformer());
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Camion camion = fav_trucks.get(position);
				
				for(Camion cam : FdTksApplication.getCamiones()){
					if(cam.getNombre().equals(camion.getNombre())){
						camion = cam;
					}
				}
				LocationManager service = (LocationManager) getSystemService(MainActivity.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				String provider = service.getBestProvider(criteria, false);
				Location location = service.getLastKnownLocation(provider);
				
				mAdapter.getMapFragment(0).findDirections(location.getLatitude(), 
						location.getLongitude(), 
						Double.parseDouble(FdTksApplication.getUbicacionxCamion(camion).getLatitud()), 
						Double.parseDouble(FdTksApplication.getUbicacionxCamion(camion).getLongitud()), 
						GMapV2Direction.MODE_WALKING);
				mDrawerLayout.closeDrawer(mLinear);
			}
			
		});
		wsis = new WebSocketIntentService(this);
		_this = this;

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mPager.setCurrentItem(tab.getPosition());
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

		MenuItem favItem = (MenuItem) menu.findItem(R.id.action_fav);
		favItem.setVisible(true);
		favItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		favItem.setIcon(R.drawable.icon_estrella_blanca);
		favItem.setOnMenuItemClickListener(((MapFragment) mAdapter.getMapFragment(0)).favListener);

		MenuItem searchItem = (MenuItem) menu.findItem(R.id.action_search);
		searchItem.setVisible(true);
		searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//		searchItem.setIcon(R.drawable.ic_action_search_blue);
		searchItem.setActionView(((MapFragment) mAdapter.getMapFragment(0)).searchView);

		MenuItem exitItem = menu.findItem(R.id.action_login);
		exitItem.setOnMenuItemClickListener(this.loginButtonClickListener);
		exitItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
 
		if (item.getItemId() == android.R.id.home) {
 
			if (mDrawerLayout.isDrawerOpen(mLinear)) {
				mDrawerLayout.closeDrawer(mLinear);
			} else {
				mDrawerLayout.openDrawer(mLinear);
			}
		}
 
		return super.onOptionsItemSelected(item);
	}

	OnMenuItemClickListener loginButtonClickListener = new OnMenuItemClickListener() {

		public boolean onMenuItemClick(MenuItem item) {

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(MainActivity.this
							.getApplicationContext());

			user = prefs.getString("userid", null);
			pass = prefs.getString("password", null);

			final Dialog dialog = new Dialog(MainActivity.this);

			if (user != null && pass != null) {
				Intent intent = new Intent(MainActivity.this,
						MisCamionesActivity.class);

				for (Usuario usuario : FdTksApplication.getUsuarios()) {
					if (usuario.getNombre().equals(user)
							&& usuario.getPassword().equals(pass)) {
						FdTksApplication.setUsuario(usuario);
						startActivity(intent);
					}
				}

				dialog.dismiss();
			} else {
				// custom dialog

				dialog.setContentView(R.layout.dialog_login);
				dialog.setTitle(getResources().getString(R.string.login));

				// set the custom dialog components - text, image and button
				user_nombre = (EditText) dialog
						.findViewById(R.id.editTextUserNameToLogin);
				user_password = (EditText) dialog
						.findViewById(R.id.editTextPasswordToLogin);

				final Button loginButton = (Button) dialog
						.findViewById(R.id.buttonSignIn);

				loginButton.setEnabled(false);

				user_nombre	.setOnFocusChangeListener(new View.OnFocusChangeListener() {

							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (hasFocus) {

								} else {
									boolean isReal = false;
									for (Usuario usu : FdTksApplication
											.getUsuarios()) {
										if (user_nombre.getText().toString()
												.equals(usu.getNombre())) {
											isReal = true;
										}
									}
									if (!isReal) {
										user_nombre.setError(getResources().getString(R.string.user_not_found));
										loginButton.setEnabled(false);
									} else {
										loginButton.setEnabled(true);
									}
								}

							}
						});

				// if button is clicked, close the custom dialog
				loginButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						user = user_nombre.getText().toString();
						pass = user_password.getText().toString();

						for (Usuario usuario : FdTksApplication.getUsuarios()) {
							if (usuario.getNombre().equals(user)
									&& usuario.getPassword().equals(pass)) {
								FdTksApplication.setUsuario(usuario);
								Intent intent = new Intent(MainActivity.this,
										MisCamionesActivity.class);
								startActivity(intent);
								SharedPreferences prefs = PreferenceManager
										.getDefaultSharedPreferences(MainActivity.this
												.getApplicationContext());
								prefs.edit().putString("userid", user).commit();
								prefs.edit().putString("password", pass).commit();
								dialog.dismiss();
							} else {
								dialog.dismiss();
							}
						}
					}
				});

				Button registerButton = (Button) dialog
						.findViewById(R.id.button_register);
				registerButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.setContentView(R.layout.dialog_register);
						dialog.setTitle(getResources().getString(R.string.user_register));

						final Button register = (Button) dialog.findViewById(R.id.button_create_dialog);
						register.setEnabled(false);

						final Usuario usuario = new Usuario();
						// set the custom dialog components - text, image and
						// button
						user_nombre = (EditText) dialog.findViewById(R.id.editText_nombre);
						user_nombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {

									@Override
									public void onFocusChange(View v,boolean hasFocus) {
										if (user_nombre.getText().toString().equals("")) {
											user_nombre.setError(getResources().getString(R.string.nombre_requerido));
										} else {
											usuario.setNombre(user_nombre.getText().toString());
										}
									}
								});

						user_password = (EditText) dialog.findViewById(R.id.editText_pass);
						user_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

									@Override
									public void onFocusChange(View v, boolean hasFocus) {
										if (user_password.getText().toString().equals("")) {
											user_password.setError(getResources().getString(R.string.password_requerido));
										} else {
											usuario.setPassword(user_password.getText().toString());
										}
									}

								});
						user_mail = (EditText) dialog.findViewById(R.id.editText_mail);
						user_mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

									@Override
									public void onFocusChange(View v, boolean hasFocus) {
										if (user_mail.getText().toString().equals("") && isValidEmail(user_mail.getText().toString())) {
											user_mail.setError(getResources().getString(R.string.email_requerido));
										} else {
											usuario.setMail(user_mail.getText().toString());
										}
									}
								});
						user_tel = (EditText) dialog.findViewById(R.id.editText_tel);
						user_tel.setOnFocusChangeListener(new View.OnFocusChangeListener() {

									@Override
									public void onFocusChange(View v, boolean hasFocus) {
										if (user_tel.getText().toString().equals("")) {
											user_tel.setError(getResources().getString(R.string.tel_requerido));
											register.setEnabled(true);
										} else {
											usuario.setTelefono(user_tel.getText().toString());
										}
									}
								});


						register.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								if (!usuario.getNombre().toString().equals("")
										&& !usuario.getPassword().toString()
												.equals("")
										&& !usuario.getMail().toString()
												.equals("")
										&& !usuario.getTelefono().toString()
												.equals("")) {
									try {

										new Thread(new Runnable() {
											@Override
											public void run() {

												RestAPI restAPI = ServiceGenerator.createService(RestAPI.class);
												Call<ResponseBody> call = restAPI.register(usuario);

                                                try {
                                                    ResponseBody obj = call.execute().body();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                               /* HttpParams httpParams = new BasicHttpParams();
												HttpClient client = new DefaultHttpClient(
														httpParams);
												HttpPost request = new HttpPost(
														"http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.usuario/create"
														*//*"http://192.168.0.3:8084/StreetFood/webresources/entities.usuario/create"*//*);
												Gson gson = new Gson();

												request.setHeader("Content-Type", "application/json");

												Log.i("JSON Object", gson.toJson(usuario).toString());

												try {
													StringEntity se;
													se = new StringEntity(gson.toJson(usuario));
													se.setContentEncoding("UTF-8");
													se.setContentType("application/json");
													request.setEntity(se);

												} catch (UnsupportedEncodingException e1) {
													e1.printStackTrace();
												}*/

												//	HttpResponse response = client.execute(request);
												//	Log.d("HTTP", response.toString());

													FdTksApplication.setUsuario(usuario);
													Intent intent = new Intent(MainActivity.this, MisCamionesActivity.class);
													startActivity(intent);
													SharedPreferences prefs = PreferenceManager
															.getDefaultSharedPreferences(MainActivity.this
																	.getApplicationContext());
													prefs.edit().putString("userid", user).commit();
													prefs.edit().putString("password", pass).commit();
													dialog.dismiss();

											}
										}).start();

										Log.d("HTTP", "HTTP: OK");
									} catch (Exception e) {
										Log.e("HTTP",
												"Error in http connection "
														+ e.toString());
									}
								}
							}
						});

					}
				});

				dialog.show();
			}

			return false;
		}
	};

	public void setCamionSeleccionado(Ubicacion ubicacion) {
		int index = mPager.getCurrentItem();
		MyAdapter adapter = ((MyAdapter) mPager.getAdapter());
		MapFragment fragment = adapter.getMapFragment(index - 1);
		// fragment.dibujar_posicion(ubicacion);
		mPager.setCurrentItem(0);
	}

	public static MainActivity getInstance() {
		return _this;
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// validating email id
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	@SuppressLint("UseSparseArrays")
	public static class MyAdapter extends FragmentStatePagerAdapter {

		public FragmentManager fm;
		public FragmentTransaction ft;
		// public Map<Integer, Fragment> mReferenceTrucks = new HashMap<Integer,
		// Fragment>();
		public Map<Integer, Fragment> mReferenceLocations = new HashMap<Integer, Fragment>();

		public MyAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		// public TrucksFragment getCamionFragment(int key) {
		// return (TrucksFragment) mReferenceTrucks.get(key);
		// }

		public MapFragment getMapFragment(int key) {
			return (MapFragment) mReferenceLocations.get(key);
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				MapFragment newFragment2 = new MapFragment();
				mReferenceLocations.put(arg0, newFragment2);
				return newFragment2;
				// case 1:
				// TrucksFragment newFragment = new TrucksFragment();
				// mReferenceTrucks.put(arg0, newFragment);
				// return newFragment;
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			return 1;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void destroyItem(View container, int position, Object object) {
			super.destroyItem(container, position, object);
			// mReferenceTrucks.remove(position);
			mReferenceLocations.remove(position);
		}
	}
}