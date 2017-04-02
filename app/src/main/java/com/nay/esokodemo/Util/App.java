package com.nay.esokodemo.Util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by nayram on 3/29/17.
 */

public class App extends Application {

    public static Context context;
    public static App esokoDemoApp;
    public static EsokoDemoService esokoDemoService;

    @Override
    public void onCreate() {
        super.onCreate();

        context=getApplicationContext();
        esokoDemoApp=this;

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                if(getToken()!=null){
                    request.addHeader("Authorization",getToken());
                }
            }
        };

        //rest
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(EsokoDemoService.api)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        esokoDemoService = restAdapter.create(EsokoDemoService.class);
    }

    public String getToken(){
        SharedPreferences pref = this.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        return pref.getString("auth_token", "39d91271b3262611728d207728f0914a8e71da78");

    }



}
