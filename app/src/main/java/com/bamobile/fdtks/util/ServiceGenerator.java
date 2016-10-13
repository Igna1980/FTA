package com.bamobile.fdtks.util;

import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://fdtrcksarg-bamobile.rhcloud.com")
            .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor((new HttpLoggingInterceptor()).setLevel(HttpLoggingInterceptor.Level.BODY))
                            .cookieJar(new JavaNetCookieJar(new CookieManager()))
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return builder.build().create(serviceClass);
    }
}
