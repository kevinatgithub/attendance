package dev.kevin.app.attendance.helpers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONObject;

import dev.kevin.app.attendance.R;

public class UpdateChecker {

    public static void checkUpdate(final Activity activity){
        final Gson gson = new Gson();
        ApiCallManager apiCallManager = new ApiCallManager(activity);
        final String app_version = activity.getResources().getString(R.string.app_version);
        final double double_app_version = Double.parseDouble(app_version);
        String url = AppConstants.DEFAULT_DOMAIN + "/checkupdate";

        apiCallManager.executeApiCall(url, Request.Method.GET, null, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                if(apiResponse != null){
                    if(double_app_version < Double.parseDouble(apiResponse.getVersion())){
                        confirmUpdate(activity);
                    }
                }
            }
        },null);

    }

    private static void confirmUpdate(final Activity activity) {

        new AlertDialog.Builder(activity)
                .setTitle("Update Required!")
                .setMessage("You are using an older version of the app.\nPlease update your app to continue.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        performUpdate(activity);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        confirmUpdate(activity);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private static void performUpdate(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW ,Uri.parse(AppConstants.APP_DOWNLOAD_URL));
        activity.startActivity(intent);
    }

    private class ApiResponse{
        private String version;

        public ApiResponse(String version) {
            this.version = version;
        }

        public String getVersion() {
            return version;
        }
    }
}
