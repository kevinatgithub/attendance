package dev.kevin.app.attendance;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONObject;

import dev.kevin.app.attendance.helpers.ApiCallManager;
import dev.kevin.app.attendance.helpers.AppConstants;
import dev.kevin.app.attendance.helpers.Callback;
import dev.kevin.app.attendance.helpers.CallbackWithResponse;
import dev.kevin.app.attendance.helpers.DialogHelper;

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
        DialogHelper.confirm(activity, "Update Required!", "You are using an older version of the app.\nPlease update your app to continue.", new Callback() {
            @Override
            public void execute() {
                performUpdate(activity);
            }
        });
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
