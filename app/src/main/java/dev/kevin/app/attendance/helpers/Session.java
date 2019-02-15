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

    public void setQRCode(String qrCode){
        prefs.edit().putString("qrcode",qrCode).commit();
    }

    public String getQRCode(){
        String qrCode = prefs.getString("qrcode","");
        if(qrCode.equals("")){
            return null;
        }
        return qrCode;
    }

    public void removeQRCode(){
        prefs.edit().remove("qrcode").commit();
    }

    public void setFname(String fname){
        prefs.edit().putString("fname",fname).commit();
    }

    public String getFname(){
        String fname = prefs.getString("fname","");
        if(fname.equals("")){
            return null;
        }
        return fname;
    }

    public void removeFname(){
        prefs.edit().remove("fname").commit();
    }

    public void setLname(String lname){
        prefs.edit().putString("lname",lname).commit();
    }

    public String getLname(){
        String lname = prefs.getString("lname","");
        if(lname.equals("")){
            return null;
        }
        return lname;
    }

    public void removeLname(){
        prefs.edit().remove("lname").commit();
    }

    public void setPhoto(String photo){
        prefs.edit().putString("photo",photo).commit();
    }

    public String getPhoto(){
        String photo = prefs.getString("photo","");
        if(photo.equals("")){
            return null;
        }
        return photo;
    }

    public void removePhoto(){
        prefs.edit().remove("photo").commit();
    }

    public void setDomain(String domain){
        prefs.edit().putString("domain",domain).commit();
    }

    public String getDomain(String defaultDomain){
        String domain = prefs.getString("domain","");
        if(domain.equals("")){
            setDomain(defaultDomain);
            return defaultDomain;
        }
        return domain;
    }

    public void removeDomain(){
        prefs.edit().remove("domain").commit();
    }
}
