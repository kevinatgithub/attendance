package dev.kevin.app.attendance;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    TextInputLayout tlFname;
    TextView txtFname;
    TextInputLayout tlLname;
    TextView txtLname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tlFname = findViewById(R.id.tlFname);
        txtFname = findViewById(R.id.txtFname);
        tlLname = findViewById(R.id.tlLname);
        txtLname = findViewById(R.id.txtLname);

        Button btnProceedFront = findViewById(R.id.btnProceedFront);
        btnProceedFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    proceed(true);
                }
            }
        });

        Button btnProceedBack = findViewById(R.id.btnProceedBack);
        btnProceedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    proceed(false);
                }
            }
        });

    }

    private boolean validateForm(){
        if(txtFname.getText().length() > 0 && txtLname.getText().length() > 0){
            tlFname.setError(null);
            tlLname.setError(null);
            return true;
        }
        if(txtFname.getText().length() == 0){
            tlFname.setError("Please enter your Given/First name");
        }

        if(txtLname.getText().length() == 0){
            tlLname.setError("Please enter your Family/Last Name");
        }
        return false;
    }

    private void proceed(boolean isFront){
        Intent intent = new Intent(getApplicationContext(),QRScan.class);
        startActivity(intent);
        finish();
    }
}
