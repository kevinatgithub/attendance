package dev.kevin.app.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import dev.kevin.app.attendance.helpers.ApiCallManager;
import dev.kevin.app.attendance.helpers.AppConstants;
import dev.kevin.app.attendance.helpers.Callback;
import dev.kevin.app.attendance.helpers.CallbackWithResponse;
import dev.kevin.app.attendance.helpers.Session;

public class SubmitResult extends AppCompatActivity {

    ImageView imgStatus;
    TextView txtStatus;
    ApiCallManager apiCallManager;
    Session session;
    Gson gson;
    String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_result);

        apiCallManager = new ApiCallManager(this);
        session = new Session(this);
        photo = session.getPhoto();
        gson = new Gson();
        imgStatus = findViewById(R.id.imgStatus);
        txtStatus = findViewById(R.id.txtStatus);

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Main.class);
                startActivity(i);
                finish();
            }
        });

        validateQR(new Callback() {
            @Override
            public void execute() {
                submitResult();
            }
        });
    }

    private void validateQR(final Callback successCallback){
        String domain = session.getDomain(AppConstants.DEFAULT_DOMAIN);
        String URL = domain+"/validateQR/"+session.getQRCode();

        apiCallManager.executeApiCall(URL, Request.Method.GET, null, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                ValidateQRResponse apiResponse = gson.fromJson(response.toString(),ValidateQRResponse.class);
                if(apiResponse.isValid()){
                    successCallback.execute();
                }else{
                    imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_failed));
                    txtStatus.setText("Failed to verify QR Code. \nPlease check if the QR Code is valid for the event.");
                    txtStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                    imgStatus.setVisibility(View.VISIBLE);
                    txtStatus.setVisibility(View.VISIBLE);
                }
            }
        },null);
    }

    private class ValidateQRResponse{

        private boolean valid;

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public boolean isValid() {
            return valid;
        }
    }

    private void submitResult(){
        String URL = session.getDomain(AppConstants.DEFAULT_DOMAIN)+"/submit";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("qrcode",session.getQRCode());
            jsonObject.put("fname",session.getFname());
            jsonObject.put("lname",session.getLname());
//            jsonObject.put("photo",photo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiCallManager.executeApiCall(URL, Request.Method.POST, jsonObject, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                clearSessionData();

                imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_success));
                txtStatus.setText("Your attendance has successfully been submitted!");
                txtStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                imgStatus.setVisibility(View.VISIBLE);
                txtStatus.setVisibility(View.VISIBLE);
            }
        },null);
    }

    private void clearSessionData() {
        session.removeQRCode();
        session.removeFname();
        session.removeLname();
        session.removePhoto();
    }
}
