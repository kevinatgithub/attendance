package dev.kevin.app.attendance.helpers;

import android.app.Activity;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONObject;

public class GlobalSettingsManager {

    public static void getConfig(final Activity activity, final GlobalSettingsCallback callback){
        final Gson gson = new Gson();
        ApiCallManager apiCallManager = new ApiCallManager(activity);
        String url = AppConstants.DEFAULT_DOMAIN + "/globalsettings";

        apiCallManager.executeApiCall(url, Request.Method.GET, null, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                GlobalSettingsResponse apiResponse = gson.fromJson(response.toString(),GlobalSettingsResponse.class);
                if(apiResponse != null){
                    callback.apply(apiResponse);
                }
            }
        },null);

    }

    public class GlobalSettingsResponse{
        private String title;

        public GlobalSettingsResponse(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public interface GlobalSettingsCallback{
        void apply(GlobalSettingsResponse response);
    }
}
