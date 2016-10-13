package com.bamobile.fdtks.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.actionbarsherlock.ActionBarSherlock.OnCreateOptionsMenuListener;
import com.actionbarsherlock.ActionBarSherlock.OnOptionsItemSelectedListener;
import com.actionbarsherlock.ActionBarSherlock.OnPrepareOptionsMenuListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.view.menu.MenuItemWrapper;
import com.actionbarsherlock.internal.view.menu.MenuWrapper;
import com.actionbarsherlock.view.MenuItem;
import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.MainActivity;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.databases.TrucksDataBase;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Ubicacion;
import com.bamobile.fdtks.ui.MiniProfile;
import com.bamobile.fdtks.util.GetDirectionsAsyncTask;
import com.bamobile.fdtks.util.Tools;
import com.google.android.gms.common.ConnectionResult;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapFragment extends SupportMapFragment implements
		OnCreateOptionsMenuListener, OnPrepareOptionsMenuListener,
		OnOptionsItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
		OnMyLocationButtonClickListener, OnMarkerClickListener{

	private SherlockFragmentActivity mActivity;
	private GoogleMap mMap;
	private GoogleApiClient mLocationClient;
	private List<Marker> markers;
	private MiniProfile profile;
    private Polyline newPolyline;

	private Ubicacion ubicacion;

	public SearchView searchView;
	static List<Camion> temp = new ArrayList<Camion>();
	public MenuItem.OnMenuItemClickListener favListener;

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public SherlockFragmentActivity getSherlockActivity() {
		return mActivity;
	}

	public List<Marker> getMarkers() {
		return markers;
	}

	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		markers = new ArrayList<Marker>();


		searchView = new SearchViewImpl(getSherlockActivity()
				.getSupportActionBar().getThemedContext());
		searchView.setOnQueryTextListener(queryListener);
		searchView.setOnCloseListener(closeListener);
		searchView.setIconifiedByDefault(true);

		int searchPlateId = searchView.getContext().getResources()
				.getIdentifier("android:id/search_plate", null, null);
		View searchPlate = searchView.findViewById(searchPlateId);
		if (searchPlate != null) {
			searchPlate.setBackgroundColor(Color.TRANSPARENT);
		}

        int searchIconId= searchView.getContext().getResources().
                getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = (ImageView)searchView.findViewById(searchIconId);
        if (searchIcon != null) {
            searchIcon.setImageDrawable(getActivity().getResources()
                    .getDrawable(R.drawable.icon_lupa_blanca));
        }

		int searchTextViewId = searchView.getResources().getIdentifier(
				"android:id/search_src_text", null, null);
		TextView searchTextView = (TextView) searchView
				.findViewById(searchTextViewId);

		searchTextView.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		searchTextView.setBackgroundColor(Color.TRANSPARENT);
		searchTextView.setTextSize(20);

        SpannableStringBuilder ssb = new SpannableStringBuilder("   "); // for the icon
        ssb.append(" ");
        Drawable searchIconNew = getResources().getDrawable(R.drawable.icon_lupa_blanca);
        int textSize = (int) (searchTextView.getTextSize() * 1.25);
        searchIconNew.setBounds(0, 0, textSize, textSize);
        ssb.setSpan(new ImageSpan(searchIconNew), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        searchTextView.setHint(ssb);

        int searchCloseId= searchView.getContext().getResources().
                getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchCloseIcon = (ImageView)searchView.findViewById(searchCloseId);
        if (searchCloseIcon != null) {
            searchCloseIcon.setImageDrawable(getActivity().getResources()
                    .getDrawable(R.drawable.icon_close_search));
        }

        mMap = this.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerClickListener(this);
        setUpMapIfNeeded();
        setUpLocationClientIfNeeded();
        centrar_plano();

		favListener = new MenuItem.OnMenuItemClickListener() {

			boolean selected = true;
			public boolean onMenuItemClick(MenuItem item) {
				if (selected) {
					TrucksDataBase trucksDb = new TrucksDataBase(getActivity());
					List<Camion> fav_trucks = trucksDb.findAll();
					trucksDb.close();
					List<Marker> fav_trucks_marker = new ArrayList<Marker>();

					for (Camion cam : fav_trucks) {
						for (int i = 0; i < markers.size(); i++) {
							if (markers.get(i).getTitle().equals(cam.getNombre())) {
								fav_trucks_marker.add(markers.get(i));
							}else{
								markers.get(i).setVisible(false);
							}
						}
					}

					for (Marker marker : fav_trucks_marker){
						marker.setVisible(true);
					}

					item.setIcon(R.drawable.ic_action_star);
					selected = false;
				} else {
					item.setIcon(R.drawable.icon_estrella_color);
					for (int i = 0; i < markers.size(); i++) {
						markers.get(i).setVisible(true);
					}
					selected = true;
				}
				return false;
			}
		};
		super.onActivityCreated(savedInstanceState);
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
/*			mLocationClient =new GoogleApiClient.Builder(mActivity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();// OnConnectionFailedListener*/

            mLocationClient = new GoogleApiClient.Builder(mActivity)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = this.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
                UiSettings settings = mMap.getUiSettings();
				settings.setAllGesturesEnabled(true);
				settings.setMyLocationButtonEnabled(true);
				settings.setCompassEnabled(true);
				settings.setScrollGesturesEnabled(true);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mLocationClient.connect();
		centrar_plano();
		Log.v("TAG", "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
		Log.v("TAG", "onPause");
	}

	@Override
	public void onAttach(Activity activity) {
		if (!(activity instanceof SherlockFragmentActivity)) {
			throw new IllegalStateException(getClass().getSimpleName()
					+ " must be attached to a SherlockFragmentActivity.");
		}
		mActivity = (SherlockFragmentActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		mActivity = null;
		mMap.clear();
		// centrar_plano();
		super.onDetach();
	}

	@Override
	public final void onCreateOptionsMenu(android.view.Menu menu,
			android.view.MenuInflater inflater) {
		MenuItem infoItem = (MenuItem) menu.findItem(R.id.action_search);
		infoItem.setVisible(false);
		infoItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		  int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_image", null, null);
	      ImageView searchview = (ImageView)searchView.findViewById(searchPlateId);
	      searchview.setBackgroundResource(R.drawable.ic_action_search_blue);
		infoItem.setActionView(searchView);

		MenuItem favItem = (MenuItem) menu.findItem(R.id.action_fav);
		favItem.setVisible(true);
		favItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		favItem.setOnMenuItemClickListener(favListener);
	}

	@Override
	public final void onPrepareOptionsMenu(android.view.Menu menu) {
		onPrepareOptionsMenu(new MenuWrapper(menu));

	}

	@Override
	public final boolean onOptionsItemSelected(android.view.MenuItem item) {
		return onOptionsItemSelected(new MenuItemWrapper(item));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Nothing to see here.
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	public void centrar_plano() {
		mMap.clear();
		dibujar_posiciones();
	}

	public void dibujar_posicion(final Ubicacion ubicacion) {

		if (!ubicacion.getLatitud().equals("")
				|| !ubicacion.getLongitud().equals("")) {
			LatLng latlng = new LatLng(Double.parseDouble(ubicacion
					.getLatitud()), Double.parseDouble(ubicacion.getLongitud()));
			Bitmap logoTemp;

			mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker marker) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {

					TextView view = new TextView(getActivity());
					view.setText(ubicacion.getCamion().getNombre());
					marker.showInfoWindow();
					return view;
				}
			});

			if(ubicacion.getCamion().getFoto1() != null){

				BitmapDrawable background = new BitmapDrawable(
						getResources(), FdTksApplication
								.getImagen1xcamion().get(
										ubicacion.getCamion()));

				TextView nombre = new TextView(mActivity);
				nombre.setText(ubicacion.getCamion().getNombre());

				Marker mark = mMap.addMarker(new MarkerOptions()
						.position(latlng)
						.title(ubicacion.getCamion().getNombre())
						.icon(BitmapDescriptorFactory.fromBitmap(Bitmap
								.createScaledBitmap(Tools.getCircularBitmapWithWhiteBorder(background.getBitmap(), 5), 96, 96, false))));
//				mark.showInfoWindow();
				markers.add(mark);

			} else if (ubicacion.getCamion().getFoto2() != null ) {

				BitmapDrawable background = new BitmapDrawable(
						getResources(), FdTksApplication
								.getImagen2xcamion().get(
										ubicacion.getCamion()));

				Marker mark = mMap.addMarker(new MarkerOptions()
						.position(latlng)
						.title(ubicacion.getCamion().getNombre())
						.icon(BitmapDescriptorFactory.fromBitmap(Bitmap
								.createScaledBitmap(Tools.getRoundedCornerBitmap(background.getBitmap()), 96, 96, false))));
//				mark.showInfoWindow();
				markers.add(mark);

			} else if (ubicacion.getCamion().getLogo() != null ) {

				BitmapDrawable background = new BitmapDrawable(
						getResources(), FdTksApplication
								.getImagenLogoxcamion().get(
										ubicacion.getCamion()));

				Marker mark = mMap.addMarker(new MarkerOptions()
						.position(latlng)
						.title(ubicacion.getCamion().getNombre())
						.icon(BitmapDescriptorFactory.fromBitmap(Bitmap
						.createScaledBitmap(Tools.getRoundedCornerBitmap(background.getBitmap()), 96, 96, false))));
//				mark.showInfoWindow();
				markers.add(mark);
			}

			LocationManager service = (LocationManager) getActivity()
					.getSystemService(MainActivity.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String provider = service.getBestProvider(criteria, false);
			Location location = service.getLastKnownLocation(provider);

			if (location == null) {
				double ltd = -34.558244;
				double lng = -58.444777;
				LatLng userLocation = new LatLng(ltd, lng);
			} else {
				LatLng userLocation = new LatLng(location.getLatitude(),
						location.getLongitude());
			}

			// GroundOverlayOptions overlayitem = new
			// GroundOverlayOptions().image(
			// BitmapDescriptorFactory
			// .fromResource(R.drawable.paranoid_android)).position(
			// latlng, 8600f, 6500f);

			// mMap.addGroundOverlay(overlayitem);

			// googleMap.addMarker(new
			// MarkerOptions().position(latlng)).setVisible(
			// true);
			// Move the camera instantly to location with a zoom of 15.
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));

			// Zoom in, animating the camera.
		//	mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

			LatLng userLocation;
			if (location == null) {
				double ltd = -34.558244;
				double lng = -58.444777;
				userLocation = new LatLng(ltd, lng);
			} else {
				userLocation = new LatLng(location.getLatitude(),
						location.getLongitude());
			}

			// GroundOverlayOptions overlayitem = new
			// GroundOverlayOptions().image(
			// BitmapDescriptorFactory
			// .fromResource(R.drawable.paranoid_android)).position(
			// latlng, 8600f, 6500f);
			//
			// mMap.addGroundOverlay(overlayitem);

			// googleMap.addMarker(new
			// MarkerOptions().position(latlng)).setVisible(
			// true);
			// Move the camera instantly to location with a zoom of 15.

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));

			// Zoom in, animating the camera.
			mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
		}
	}

	public void dibujar_posiciones() {
		for (Ubicacion ubicaion : FdTksApplication.getUbicaciones()) {
			dibujar_posicion(ubicaion);
		}
	}

	public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
	    PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.BLUE);
	    for(int i = 0 ; i < directionPoints.size() ; i++) {
	        rectLine.add(directionPoints.get(i));
	    }
	    newPolyline = mMap.addPolyline(rectLine);
	}

	public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
	{
	    Map<String, String> map = new HashMap<String, String>();
	    map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
	    map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
	    map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
	    map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
	    map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

	    GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
	    asyncTask.execute(map);
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	/**
	 * Callback called when connected to GCore. Implementation of
	 *
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		//mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	/**
	 * Callback called when disconnected from GCore. Implementation of
	 *
	 */
	//@Override
	public void onDisconnected() {
		// Do nothing
	}

	/**
	 * Implementation of
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	@Override
	public boolean onMyLocationButtonClick() {
		// Return false so that we don't consume the event and the default
		// behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}

	@SuppressLint("NewApi")
	private OnCloseListener closeListener = new OnCloseListener() {

		@Override
		public boolean onClose() {
			searchView.setQuery(null, true);
			searchView.onActionViewCollapsed();
			return true;
		}
	};

	private LoaderCallbacks<List<Camion>> loaderCallbak = new LoaderCallbacks<List<Camion>>() {

		@Override
		public Loader<List<Camion>> onCreateLoader(int arg0, Bundle arg1) {
			ContactPageLoader cpl = new ContactPageLoader(getActivity());
			cpl.setFilter(currentFilter);
			return cpl;
		}

		@Override
		public void onLoadFinished(Loader<List<Camion>> arg0,
				List<Camion> camiones) {

			if (currentFilter.equals("")) {
				for (Marker m : markers) {
					m.setVisible(true);
				}
			} else {
				for (Marker m : markers) {
					if (m.getTitle().equals(currentFilter)) {
						m.setVisible(true);
					} else {
						m.setVisible(false);
					}
				}
			}

		}

		@Override
		public void onLoaderReset(Loader<List<Camion>> arg0) {
		}

	};

	private String currentFilter = null;
	private OnQueryTextListener queryListener = new OnQueryTextListener() {

		@Override
		public boolean onQueryTextSubmit(String query) {
			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			// Called when the action bar search text has changed. Update
			// the search filter, and restart the loader to do a new query
			// with this filter.
			String newFilter = newText;

			// Don't do anything if the filter hasn't actually changed.
			// Prevents restarting the loader when restoring state.
			if (currentFilter == null && newFilter == null) {
				return true;
			}
			if (currentFilter != null && currentFilter.equals(newFilter)) {
				return true;
			}
			currentFilter = newFilter;
			getLoaderManager().restartLoader(0, null, loaderCallbak);
			return true;
		}
	};

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static class SearchViewImpl extends SearchView {
		public SearchViewImpl(Context context) {
			super(context);
		}

		// The normal SearchView doesn't clear its search text when
		// collapsed, so we will do this for it.
		@Override
		public void onActionViewCollapsed() {
			setQuery("", false);
			super.onActionViewCollapsed();
		}
	}

	public static class ContactPageLoader extends AsyncTaskLoader<List<Camion>> {

		private String filter = null;

		public ContactPageLoader(Context context) {
			super(context);
		}

		public void setFilter(String filter) {
			this.filter = filter;
		}

		@Override
		public List<Camion> loadInBackground() {
			if (temp.size() > 0) {
				temp.clear();
			}
			for (Camion c : FdTksApplication.getCamiones()) {
				if (c.getNombre().matches("(.*)" + filter + "(.*)")) {
					temp.add(c);
				}
			}
			return temp;
		}

		@Override
		protected void onStartLoading() {
			super.onStartLoading();
			forceLoad();
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {

		if(newPolyline != null){
			newPolyline.remove();
		}

		// get the map container height
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int container_height = display.getHeight();

		Projection projection = mMap.getProjection();

		LatLng markerLatLng = new LatLng(marker.getPosition().latitude,
				marker.getPosition().longitude);
		Point markerScreenPosition = projection.toScreenLocation(markerLatLng);
		Point pointHalfScreenAbove = new Point(markerScreenPosition.x,
				markerScreenPosition.y - (container_height / 3));

		LatLng aboveMarkerLatLng = projection
				.fromScreenLocation(pointHalfScreenAbove);

		mMap.moveCamera(CameraUpdateFactory.newLatLng(aboveMarkerLatLng));

		for(Camion cam : FdTksApplication.getCamiones()){
			if(marker.getTitle().equals(cam.getNombre())){
				profile = new MiniProfile(MapFragment.this,
						cam,
						FdTksApplication.getInfoxCamion(cam),
						FdTksApplication.getUbicacionxCamion(cam));
				profile.show();
			}
		}
		return true;
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

}
