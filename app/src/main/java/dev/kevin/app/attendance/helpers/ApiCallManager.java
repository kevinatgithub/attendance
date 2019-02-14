package dev.kevin.app.attendance.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import dev.kevin.app.attendance.R;
import dev.kevin.app.attendance.ShowServerResponse;

public class ApiCallManager {

    final private static String API_ADDRESS = "http://att3ndanc3.getsandbox.com";
    final private static String API_ERROR = "Can't connect to server";
    final private static String API_ERROR_MESSAGE = "Please check internet connection";
    private Activity activity;
    private RequestQueue requestQueue;
    private Gson gson;
    private Session session;

    ApiCallManager(Activity activity) {
        this.activity = activity;
        this.gson = new Gson();
        this.requestQueue = Volley.newRequestQueue(activity);
        this.session = new Session(activity);
    }

    private String getDomainName(){
        String domain = session.getDomain();
        if(domain == null || domain.equals("")){
            return API_ADDRESS;
        }
        return domain;
    }

    private void executeApiCall(final String URL, final int METHOD, @Nullable final JSONObject jsonObject, final CallbackWithResponse CALLBACK, @Nullable final Callback ON_ERROR_CALLBACK){
        checkConnection(new Callback() {
            @Override
            public void execute() {

                requestQueue.add(
                        new JsonObjectRequest(METHOD, URL, jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                CALLBACK.execute(response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleAPIExceptionResponse(error);
                                if(ON_ERROR_CALLBACK != null){
                                    ON_ERROR_CALLBACK.execute();
                                }
                            }
                        }
                        )
                );
            }
        });
    }

    private void handleAPIExceptionResponse(VolleyError error){
        NetworkResponse networkResponse = error.networkResponse;

        Intent intent = new Intent(activity,ShowServerResponse.class);
        if(networkResponse == null){
            intent.putExtra("message","NETWORK ERROR " + API_ERROR);
        }else if(networkResponse.statusCode == 400){
            String json = new String(networkResponse.data);
            ApiResponse response = gson.fromJson(json,ApiResponse.class);
            intent.putExtra("message",response.Message);
        }else{
            intent.putExtra("message","ERROR      "+networkResponse.statusCode+" " + API_ERROR);
        }
        activity.startActivity(intent);
    }

    private void checkConnection(final Callback CALLBACK){

        ConnectivityManager conMgr =  (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){

            LayoutInflater inflater = LayoutInflater.from(activity);
            final AlertDialog DIALOG = new AlertDialog.Builder(activity).create();
            DIALOG.setTitle(API_ERROR);
            View customView = inflater.inflate(R.layout.network_fail,null);
            DIALOG.setView(customView);
            DIALOG.setCancelable(false);
            DIALOG.setButton(AlertDialog.BUTTON_POSITIVE, "TRY AGAIN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DIALOG.dismiss();
                    checkConnection(CALLBACK);
                }
            });

            DIALOG.setOnShowListener(new DialogInterface.OnShowListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    DIALOG.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimary);
                }
            });
            DIALOG.show();
        }else{
            CALLBACK.execute();
        }
    }
}
