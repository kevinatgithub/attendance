package dev.kevin.app.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SubmitResult extends AppCompatActivity {

    ImageView imgStatus;
    TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_result);

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

        
    }
}
