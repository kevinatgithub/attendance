package dev.kevin.app.attendance.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;

import dev.kevin.app.attendance.NetworkFailDialog;

public class ConnectivityManager {

    public static void checkConnection(final Activity activity, final Callback CALLBACK){

        android.net.ConnectivityManager conMgr =  (android.net.ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){

            NetworkFailDialog.show(activity, new Callback() {
                @Override
                public void execute() {
                    checkConnection(activity,CALLBACK);
                }
            });
        }else{
            CALLBACK.execute();
        }
    }
}
