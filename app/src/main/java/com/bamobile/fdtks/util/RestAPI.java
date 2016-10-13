package com.bamobile.fdtks.util;

import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.entities.Informacion;
import com.bamobile.fdtks.entities.Ubicacion;
import com.bamobile.fdtks.entities.Usuario;
import com.bamobile.fdtks.fragments.camion.CreateCamionFragment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

public interface RestAPI {

    @GET("/webresources/entities.camion")
    public Observable<List<Camion>> getCamiones();

    @GET("/webresources/entities.ubicacion")
    public Observable<List<Ubicacion>> getUbicaciones();

    @GET("/webresources/entities.informacion")
    public Observable<List<Informacion>> getInformaciones();

    @GET("/webresources/entities.usuario")
    public Observable<List<Usuario>> getUsuarios();

    @POST("/webresources/entities.usuario/create")
    public Call<ResponseBody> register(@Body Usuario usurio);

    @POST("/webresources/entities.camion/create")
    public Call<ResponseBody> createCamion(@Body Camion camion);

    @POST("/webresources/entities.ubicacion/create")
    public Call<ResponseBody> createUbicacion(@Body Ubicacion ubicacion);

    @POST("/webresources/entities.informacion/create")
    public Call<ResponseBody> createInformacion(@Body Informacion informacion);

    @POST("/webresources/entities.camion/manipulateImage/")
    public Call<ResponseBody> manipulateImage(@Body CreateCamionFragment.Imagen imagen);

    @DELETE("/webresources/entities.camion/remove?id=")
    public void removeCamion(@Body Camion camion);

    @DELETE("/webresources/entities.ubicacion/remove?id=")
    public void removeUbicacion(@Body Ubicacion ubicacion);

    @DELETE("/webresources/entities.informacion/remove?id=")
    public void removeInformacion(@Body Informacion informacion);

    @PUT("/webresources/entities.camion/put")
    public Call<ResponseBody> editCamion(@Body Camion camion);

    @PUT("/webresources/entities.ubicacion/put")
    public Call<ResponseBody> editUbicacion(@Body Ubicacion informacion);

    @PUT("/webresources/entities.informacion/put")
    public Call<ResponseBody> editInformacion(@Body Informacion informacion);


}
