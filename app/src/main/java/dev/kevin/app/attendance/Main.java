package dev.kevin.app.attendance;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
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
import dev.kevin.app.attendance.models.Member;

public class Main extends AppCompatActivity {

    ConstraintLayout cl_step2;
    ConstraintLayout cl_confirm;
    TextInputLayout tlFname;
    TextView txtFname;
    TextInputLayout tlLname;
    TextView txtLname;
    Session session;
    ProgressBar pbLoading;
    TextView lblMemberName;
    TextView lblAffiliation;
    TextView lblPrcNo;
    ApiCallManager apiCallManager;
    Gson gson;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this);
        gson = new Gson();
        apiCallManager = new ApiCallManager(this);

        cl_step2 = findViewById(R.id.cl_step2);
        cl_confirm = findViewById(R.id.cl_confirm);
        tlFname = findViewById(R.id.tlFname);
        txtFname = findViewById(R.id.txtFname);
        tlLname = findViewById(R.id.tlLname);
        txtLname = findViewById(R.id.txtLname);
        pbLoading = findViewById(R.id.pbLoading);
        lblMemberName = findViewById(R.id.lblMemberName);
        lblAffiliation = findViewById(R.id.lblAffiliation);
        lblPrcNo= findViewById(R.id.lblPrcNo);

        Button btnProceedFront = findViewById(R.id.btnProceedFront);
        btnProceedFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    verifiy();
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        txtFname.setText("");
        txtLname.setText("");
        txtLname.clearFocus();
    }

    private boolean validateForm(){
        if(txtFname.getText().length() > 1 && txtLname.getText().length() > 1){
            tlFname.setError(null);
            tlLname.setError(null);
            return true;
        }
        if(txtFname.getText().length() <= 1){
            tlFname.setError("Please enter your Given/First name");
        }

        if(txtLname.getText().length() <= 1){
            tlLname.setError("Please enter your Family/Last Name");
        }
        return false;
    }

    private void verifiy(){
        pbLoading.setVisibility(View.VISIBLE);
        String URL = session.getDomain(AppConstants.DEFAULT_DOMAIN)+"/validateName";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fname",txtFname.getText().toString());
            jsonObject.put("lname",txtLname.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        apiCallManager.executeApiCall(URL, Request.Method.POST, jsonObject, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                pbLoading.setVisibility(View.INVISIBLE);
                ApiResponse apiResponse = gson.fromJson(response.toString(),ApiResponse.class);
                if(apiResponse.member != null){
                    member = apiResponse.member;
                    hideKeyboard();
                    confirm();
                }else{
                    showVerificationFailed();
                }
            }
        },null);
    }

    private class ApiResponse{

        Member member;
    }

    private void confirm(){
        cl_step2.setVisibility(View.INVISIBLE);
        cl_confirm.setVisibility(View.VISIBLE);
        lblMemberName.setText(member.getFname() + " " + member.getLname());
        lblAffiliation.setText(member.getAffiliation());
        lblPrcNo.setText(member.getPrc_no());

        Button btnYes = findViewById(R.id.btnProceed);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed();
            }
        });

        Button btnNo = findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cl_confirm.setVisibility(View.INVISIBLE);
                cl_step2.setVisibility(View.VISIBLE);
                lblMemberName.setText("Not Set");
                lblAffiliation.setText("Not Set");
                lblPrcNo.setText("Not Set");
                txtFname.setText("");
                txtLname.setText("");
            }
        });
    }

    private void showVerificationFailed(){
        tlFname.setError("You are not in the registration list, please verify if your name is in the registration list.");
        txtFname.setText("");
        txtLname.setText("");
        tlLname.setError("");
        txtLname.clearFocus();
    }

    private void proceed(){

        Intent intent = new Intent(getApplicationContext(),QRScan.class);
        startActivity(intent);
//        finish();
        session.setMember(member);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        txtFname.setText("");
        txtLname.setText("");
        tlFname.setError(null);
        txtLname.setError(null);
        lblMemberName.setText("Not Set");
        lblAffiliation.setText("Not Set");
        lblPrcNo.setText("Not Set");
        cl_step2.setVisibility(View.VISIBLE);
        cl_confirm.setVisibility(View.INVISIBLE);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
