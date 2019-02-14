package dev.kevin.app.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowServerResponse extends AppCompatActivity {

    private TextView txt_message;
    private Button btn_change_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_server_response);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        txt_message = findViewById(R.id.txt_message);
        String message = getIntent().getStringExtra("message");
        txt_message.setText(message);
        btn_change_settings = findViewById(R.id.btn_change_settings);
        btn_change_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(getApplicationContext(),Settings.class);
                startActivity(settings);
                finish();
            }
        });
    }
}
