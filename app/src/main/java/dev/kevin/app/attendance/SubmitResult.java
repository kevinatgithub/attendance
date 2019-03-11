package dev.kevin.app.attendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
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
import com.google.gson.JsonObject;

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

        submitResult();

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
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                handleApiResponse(apiResponse);
            }
        },null);
    }

    private void submitSignOut() {
        String qrcode[] = session.getQRCode().split("\\|");
        String url = session.getDomain(AppConstants.DEFAULT_DOMAIN)+"/signout";
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

        apiCallManager.executeApiCall(url, Request.Method.POST, jsonObject, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                clearSessionData();
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                handleApiResponse(apiResponse);
            }
        },null);
    }

    private void handleApiResponse(ApiResponse apiResponse) {

        ConstraintLayout cl_status = findViewById(R.id.cl_status);
        ConstraintLayout cl_exists = findViewById(R.id.cl_exists);

        cl_status.setVisibility(View.VISIBLE);
        if(apiResponse.getStatus().toUpperCase().equals("OK")) {
            clearSessionData();
            cl_exists.setVisibility(View.GONE);
            if (apiResponse.getAttendances().size() > 0) {
                populateAttendancesListView(apiResponse.getAttendances());
            }
            imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_success));
            txtStatus.setText(apiResponse.getMessage());
            txtStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }else if(apiResponse.getStatus().toUpperCase().equals("EXISTS")){
            TextView lblStatus = findViewById(R.id.lblStatus);
            lblStatus.setText(apiResponse.getMessage());

            Button btnYes = findViewById(R.id.btnYes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitSignOut();
                }
            });

            Button btnNo = findViewById(R.id.btnCancel);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSessionData();
                    finish();
                }
            });
            cl_status.setVisibility(View.GONE);
            cl_exists.setVisibility(View.VISIBLE);

        }else if(apiResponse.getStatus().toUpperCase().equals("FAILED")){
            clearSessionData();
            imgStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_failed));
            txtStatus.setText(apiResponse.getMessage());
            txtStatus.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        imgStatus.setVisibility(View.VISIBLE);
        txtStatus.setVisibility(View.VISIBLE);
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

        private String message;

        private int request_code;

        public ApiResponse(String status, ArrayList<Attendance> attendances, String message, int request_code) {
            this.status = status;
            this.attendances = attendances;
            this.message = message;
            this.request_code = request_code;
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

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getRequest_code() {
            return request_code;
        }

        public void setRequest_code(int request_code) {
            this.request_code = request_code;
        }
    }
}
