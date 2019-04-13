package com.example.quanlidiemrenluyen.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences preferences;
    Context context;
    SharedPreferences.Editor editor;
    private int PRE_MODE = 1;
    private  static final  String NAME = "linh";
    private  static final  String KEY_LOGIN = "isLogin";

    public SessionManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void SetLogin(boolean isLogin){
        editor.putBoolean(KEY_LOGIN, isLogin);
        editor.commit();
    }

    public boolean CheckLogin(){
        return preferences.getBoolean(KEY_LOGIN, false);
    }

}
