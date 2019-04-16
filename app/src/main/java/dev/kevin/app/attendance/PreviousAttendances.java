package dev.kevin.app.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import dev.kevin.app.attendance.helpers.ApiCallManager;
import dev.kevin.app.attendance.helpers.AppConstants;
import dev.kevin.app.attendance.helpers.AttendanceRowAdapter;
import dev.kevin.app.attendance.helpers.CallbackWithResponse;
import dev.kevin.app.attendance.models.Attendance;

public class PreviousAttendances extends AppCompatActivity {

    ImageView imgNoData;
    ListView lvAttendances;
    TextView lblNoData;
    ProgressBar pbLoading;
    ApiCallManager apiCallManager;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_attendances);

        apiCallManager = new ApiCallManager(this);
        imgNoData = findViewById(R.id.imgNoData);
        lblNoData = findViewById(R.id.lblNoData);
        pbLoading = findViewById(R.id.pbLoading);
        lvAttendances = findViewById(R.id.lvAttendances);

        findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadAttendances();
    }

    private void loadAttendances() {
        Intent i = getIntent();
        String id = String.valueOf(i.getStringExtra("id"));

        String URL = AppConstants.DEFAULT_DOMAIN + "/previousAttendances/"+ id;
        apiCallManager.executeApiCall(URL, Request.Method.GET, null, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                pbLoading.setVisibility(View.GONE);
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                if(apiResponse.getAttendances() != null){
                    if(apiResponse.getAttendances().size() == 0){
                        imgNoData.setVisibility(View.VISIBLE);
                        lblNoData.setVisibility(View.VISIBLE);
                    }else{
                        displayAttendances(apiResponse.getAttendances());
                    }
                }else{
                    imgNoData.setVisibility(View.VISIBLE);
                    lblNoData.setVisibility(View.VISIBLE);
                }
            }
        },null);
    }

    private void displayAttendances(ArrayList<Attendance> attendances) {
        AttendanceRowAdapter attendanceRowAdapter = new AttendanceRowAdapter(this,attendances);
        lvAttendances.setAdapter(attendanceRowAdapter);
    }

    private class ApiResponse{

        private ArrayList<Attendance> attendances;

        public ApiResponse(ArrayList<Attendance> attendances) {
            this.attendances = attendances;
        }

        public ArrayList<Attendance> getAttendances() {
            return attendances;
        }
    }
}
