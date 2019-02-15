package dev.kevin.app.attendance.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class Session {

    private SharedPreferences prefs;
    private Gson gson;

    public Session(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
    }

    public void setDomain(String domain){
        prefs.edit().putString("domain",domain).commit();
    }

    public String getDomain(){
        String domain = prefs.getString("domain","");
        if(domain.equals("")){
            return null;
        }
        return domain;
    }

    public void removeDomain(){
        prefs.edit().remove("domain").commit();
    }
}
