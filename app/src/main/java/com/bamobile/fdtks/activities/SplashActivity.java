package com.bamobile.fdtks.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.bamobile.fdtks.R;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.databases.ImageDatabase;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Informacion;
import com.bamobile.fdtks.entities.Ubicacion;
import com.bamobile.fdtks.entities.Usuario;
import com.bamobile.fdtks.util.RestAPI;
import com.bamobile.fdtks.util.ServiceGenerator;
import com.bamobile.fdtks.util.SimpleEULA;
import com.bamobile.fdtks.util.Tools;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.List;

import rx.Subscriber;


public class SplashActivity extends SherlockActivity {

    private static SplashActivity _this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isGooglePlayAvailable();
        getSupportActionBar().hide();
        SimpleEULA eula = new SimpleEULA(this);
        eula.show();
    }

    public static SplashActivity getInstance() {
        return _this;
    }

    private void isGooglePlayAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == ConnectionResult.SUCCESS) {
            new BackgroundAsyncTask().execute();
            _this = this;
        } else {
            ((Dialog) GooglePlayServicesUtil.getErrorDialog(status, this, 10)).show();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    public class BackgroundAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            if (FdTksApplication.getCamiones() != null && FdTksApplication.getUbicaciones() != null
                    && FdTksApplication.getInformaciones() != null && FdTksApplication.getUsuarios() != null) {
                FdTksApplication.getCamiones().clear();
                FdTksApplication.getUbicaciones().clear();
                FdTksApplication.getInformaciones().clear();
                FdTksApplication.getUsuarios().clear();
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            RestAPI restAPI = ServiceGenerator.createService(RestAPI.class);

            (restAPI.getCamiones()).subscribe(new Subscriber<List<Camion>>() {
                @Override
                public final void onCompleted() {}

                @Override
                public final void onError(Throwable e) {}

                @Override
                public final void onNext(List<Camion> response) {

                    ImageDatabase imageDb = new ImageDatabase(SplashActivity.this);

                    for (Camion cam : response) {
                        Bitmap image = null;
                        String imageUrl = cam.getFoto1();
                        // String imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + responseCamion.getFoto1();

                        if (imageDb.exists(imageUrl)) {
                            image = imageDb.getImage(imageUrl);
                            FdTksApplication.getImagen1xcamion().put(cam, image);
                        } else {
                            image = Tools.getBitmapFromURL(imageUrl);
                            imageDb.addImage(imageUrl, image);
                            FdTksApplication.getImagen1xcamion().put(cam, image);
                        }

                        imageUrl = cam.getFoto2();
                        // imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + responseCamion.getFoto2();
                        if (imageDb.exists(imageUrl)) {
                            image = imageDb.getImage(imageUrl);
                            FdTksApplication.getImagen2xcamion().put(cam, image);
                        } else {
                            image = Tools.getBitmapFromURL(imageUrl);
                            imageDb.addImage(imageUrl, image);
                            FdTksApplication.getImagen2xcamion().put(cam, image);
                        }

                        image = null;
                        imageUrl = cam.getLogo();
                        // imageUrl = "http://192.168.0.3:8084/FdTrcksArg/resources/" + responseCamion.getLogo();
                        if (imageDb.exists(imageUrl)) {
                            image = imageDb.getImage(imageUrl);
                            FdTksApplication.getImagenLogoxcamion().put(cam, image);
                        } else {
                            image = Tools.getBitmapFromURL(imageUrl);
                            imageDb.addImage(imageUrl, image);
                            FdTksApplication.getImagenLogoxcamion().put(cam, image);
                        }
                    }

                    imageDb.close();
                    FdTksApplication.setCamiones(response);
                }
            });

            (restAPI.getUbicaciones()).subscribe(new Subscriber<List<Ubicacion>>() {
                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onNext(List<Ubicacion> response) {
                    FdTksApplication.setUbicaciones(response);
                }
            });

            (restAPI.getInformaciones()).subscribe(new Subscriber<List<Informacion>>() {
                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onNext(List<Informacion> response) {
                    FdTksApplication.setInformaciones(response);
                }
            });

            (restAPI.getUsuarios()).subscribe(new Subscriber<List<Usuario>>() {
                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onNext(List<Usuario> response) {
                    FdTksApplication.setUsuarios(response);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
