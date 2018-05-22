package com.box8.login.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.box8.login.MainActivity;
import com.box8.login.R;
import com.box8.login.base.BaseActivity;

public class SessionHelper {

    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Box8LoginPreference";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_TOKEN = "token";

    public SessionHelper(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String token){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public void logoutUser(BaseActivity activity){
        editor.clear();
        editor.commit();
        Intent newIntent = new Intent(activity,MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        (activity).startActivity(newIntent);
        activity.overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right );
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void CheckLogin(BaseActivity activity) {
        if(!isLoggedIn()) {
            Intent newIntent = new Intent(activity,MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            (activity).startActivity(newIntent);
        }
    }

}
