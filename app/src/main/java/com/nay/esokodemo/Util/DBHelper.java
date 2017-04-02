package com.nay.esokodemo.Util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.nay.esokodemo.Models.ListResource;
import com.nay.esokodemo.Models.ListUsers;
import com.nay.esokodemo.Models.User;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;

/**
 * Created by nayram on 3/29/17.
 */

public class DBHelper {

    Context context;
    static String TAG = "DbHelper" ;
    static DB snappydb;

    public static void saveCurrentUser(User user){
        try {
            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            snappydb.put(Constants.CURRENT_USER,  (new Gson().toJson(user)));
        }catch (SnappydbException ex){
            ex.printStackTrace();

        }
    }

    public static User getCurrentUser(){
        try {
            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            String userString=snappydb.get(Constants.CURRENT_USER);
            return (new Gson().fromJson(userString,User.class));
        }catch (SnappydbException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static void saveFavUser(ListUsers.Users user){

        try {

            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            snappydb.put(Constants.FAV_USER + user.getId(),  (new Gson().toJson(user)));
        }catch (SnappydbException ex){
            ex.printStackTrace();

        }
    }

    public static ArrayList<ListUsers.Users> getFavUsers(){
        ArrayList<ListUsers.Users>results=new ArrayList<>();
        try {
            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            for (String key : snappydb.findKeys(Constants.FAV_USER)) {
                String favUsers = snappydb.get(key);
                ListUsers.Users user=(new Gson()).fromJson(favUsers,ListUsers.Users.class);
                results.add(user);
            }
            return results;
        }catch (SnappydbException e) {

            e.printStackTrace();
            return null ;
        }
    }

    public static void saveFavRes(ListResource.ResData resData){

        try {

            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            snappydb.put(Constants.FAV_RESOURCE + resData.getId(),  (new Gson().toJson(resData)));
        }catch (SnappydbException ex){
            ex.printStackTrace();

        }
    }

    public static ArrayList<ListResource.ResData> getFavRes(){

        ArrayList<ListResource.ResData>results=new ArrayList<>();
        try {
            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            for (String key : snappydb.findKeys(Constants.FAV_RESOURCE)) {
                String favRes = snappydb.get(key);
                ListResource.ResData data=(new Gson()).fromJson(favRes,ListResource.ResData.class);
                results.add(data);
            }
            return results;
        }catch (SnappydbException e) {

            e.printStackTrace();
            return null ;
        }
    }


    public static boolean isFavUser(int id){
        try {
            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            Log.d(TAG,"id "+String.valueOf(id));
            return snappydb.exists(Constants.FAV_USER + String.valueOf(id) );
        }catch (SnappydbException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean isFavRes(int id){
        try {
            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            return snappydb.exists(Constants.FAV_RESOURCE + String.valueOf(id));
        }catch (SnappydbException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static void delFavUser(String id){
        try {
            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            snappydb.del(Constants.FAV_USER + id);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public static void delFavRes(String id){
        try {
            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            snappydb.del(Constants.FAV_RESOURCE+id);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public static void destroySnappyDb(){
        try{
            if(snappydb == null){ snappydb= DBFactory.open(App.context,Constants.DB_NAME);}
            snappydb.destroy();
        }catch (SnappydbException e){
            e.printStackTrace();
        }
    }



}
