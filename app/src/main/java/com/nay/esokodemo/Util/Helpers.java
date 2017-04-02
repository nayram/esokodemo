package com.nay.esokodemo.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nayram on 3/29/17.
 */

public class Helpers {

    static public void setLoginStatus(Context con, String key, Boolean value){
        SharedPreferences pref = con.getSharedPreferences(Constants.SHARED_PREFERENCE_CONTAINER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value) ;
        editor.commit() ;
    }

    static public Boolean getLoginStatus(Context context,String key){
        SharedPreferences pref = context.getSharedPreferences(Constants.SHARED_PREFERENCE_CONTAINER, Context.MODE_PRIVATE);
        return pref.getBoolean(key,false);
    }


}
