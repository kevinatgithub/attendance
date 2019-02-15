package dev.kevin.app.attendance.helpers;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import dev.kevin.app.attendance.ShowServerResponse;

public class ApiCallManager {

    private Activity activity;
    private RequestQueue requestQueue;
    private Gson gson;
    private Session session;

    public ApiCallManager(Activity activity) {
        this.activity = activity;
        this.gson = new Gson();
        this.requestQueue = Volley.newRequestQueue(activity);
        this.session = new Session(activity);
    }

    public void executeApiCall(final String URL, final int METHOD, @Nullable final JSONObject jsonObject, final CallbackWithResponse CALLBACK, @Nullable final Callback ON_ERROR_CALLBACK){
        dev.kevin.app.attendance.helpers.ConnectivityManager.checkConnection(activity,new Callback() {
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
            intent.putExtra("message","NETWORK ERROR \n Can't Connect to server" );
        }else if(networkResponse.statusCode == 400){
            String json = new String(networkResponse.data);
            ApiResponse response = gson.fromJson(json,ApiResponse.class);
            intent.putExtra("message","ERROR CODE 400");
        }else{
            intent.putExtra("message","ERROR      "+networkResponse.statusCode+" Can't Connect to server");
        }
        activity.startActivity(intent);
    }


}
