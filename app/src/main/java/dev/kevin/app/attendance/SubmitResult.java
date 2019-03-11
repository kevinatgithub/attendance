package dev.kevin.app.attendance;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dev.kevin.app.attendance.helpers.ApiCallManager;
import dev.kevin.app.attendance.helpers.AppConstants;
import dev.kevin.app.attendance.helpers.AttendanceRowAdapter;
import dev.kevin.app.attendance.helpers.Callback;
import dev.kevin.app.attendance.helpers.CallbackWithResponse;
import dev.kevin.app.attendance.helpers.Session;
import dev.kevin.app.attendance.models.Attendance;
import dev.kevin.app.attendance.models.Member;

public class SubmitResult extends AppCompatActivity {

    ImageView imgStatus;
    TextView txtStatus;
    ListView lvHistory;
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
        lvHistory = findViewById(R.id.lvHistory);

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

        successCallback.execute();

//        apiCallManager.executeApiCall(URL, Request.Method.GET, null, new CallbackWithResponse() {
//            @Override
//            public void execute(JSONObject response) {
//                ValidateQRResponse apiResponse = gson.fromJson(response.toString(),ValidateQRResponse.class);
//                if(apiResponse.isValid()){
//                    successCallback.execute();
//                }else{
//                    imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_failed));
//                    txtStatus.setText("Failed to verify QR Code. \nPlease check if the QR Code is valid for the event.");
//                    txtStatus.setTextColor(getResources().getColor(R.color.colorAccent));
//                    imgStatus.setVisibility(View.VISIBLE);
//                    txtStatus.setVisibility(View.VISIBLE);
//                }
//            }
//        },null);
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
        String qrcode[] = session.getQRCode().split("\\|");
        String URL = session.getDomain(AppConstants.DEFAULT_DOMAIN)+"/submit";
        Member member = session.getMember();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("event_id",qrcode[0]);
            jsonObject.put("day",qrcode[1]);
            jsonObject.put("member_id",member.getId());
            jsonObject.put("photo",photo);
            jsonObject.put("session",qrcode[2]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiCallManager.executeApiCall(URL, Request.Method.POST, jsonObject, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                clearSessionData();
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                ConstraintLayout cl_status = findViewById(R.id.cl_status);
                cl_status.setVisibility(View.VISIBLE);
                if(apiResponse.getStatus().toUpperCase().equals("OK")){
                    if(apiResponse.getAttendances().size() > 0){
                        populateAttendancesListView(apiResponse.getAttendances());
                    }
                    imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_success));
                    txtStatus.setText("Your attendance has successfully been submitted!");
                    txtStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else{
                    imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_failed));
                    txtStatus.setText("Validation failed, Please check QR Code!");
                    txtStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                imgStatus.setVisibility(View.VISIBLE);
                txtStatus.setVisibility(View.VISIBLE);
            }
        },null);
    }

    private void populateAttendancesListView(ArrayList<Attendance> attendances) {
        AttendanceRowAdapter attendanceRowAdapter = new AttendanceRowAdapter(getApplicationContext(),attendances);
        lvHistory.setAdapter(attendanceRowAdapter);

    }

    private void clearSessionData() {
        session.removeQRCode();
        session.removeMember();
        session.removePhoto();
    }

    private class ApiResponse{

        private String status;

        private ArrayList<Attendance> attendances;

        public ApiResponse(String status, ArrayList<Attendance> attendances) {
            this.status = status;
            this.attendances = attendances;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public ArrayList<Attendance> getAttendances() {
            return attendances;
        }

        public void setAttendances(ArrayList<Attendance> attendances) {
            this.attendances = attendances;
        }
    }
}
