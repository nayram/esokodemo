package com.nay.esokodemo.Util;

import com.nay.esokodemo.Models.ListResource;
import com.nay.esokodemo.Models.ListUsers;
import com.nay.esokodemo.Models.LoginResponse;
import com.nay.esokodemo.Models.User;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by nayram on 3/29/17.
 */

public interface EsokoDemoService {

    static final String api = "https://reqres.in/api";
//    static final String Authorization = App.speakApp.getToken() ;


    @POST("/login")
    void login(@Body User user, Callback<LoginResponse> callback);

    @POST("/register")
    void register(@Body User user,Callback<LoginResponse> registerCallback);

    @GET("/users")
    void getUsers(@Query("page") String page, Callback<ListUsers> listUsersCallback);

    @GET("/unknown")
    void getResource(Callback<ListResource> listResourceCallback);


}
