package dev.kevin.app.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro;


public class Introduction extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SlideStep slideStep1 = new SlideStep();
        slideStep1.title = "Step 1";
        slideStep1.contentText = "Enter your First / Given Name, and your \nFamily / Last Name in the fields provided \nand tap start.";
        addSlide(slideStep1);

        SlideStep slideStep2 = new SlideStep();
        slideStep2.title = "Step 2";
        slideStep2.contentText = "Adjust your front facing camera so that the \nQR Code in the event will be captured.";
        addSlide(slideStep2);

        SlideStep slideStep3 = new SlideStep();
        slideStep3.title = "Step 3";
        slideStep3.contentText = "After scanning the QR Code, \na capture photo button will appear, \nTap to complete the process.";
        addSlide(slideStep3);

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),Main.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(),Main.class);
        startActivity(intent);
        finish();
    }

    public static class SlideStep extends Fragment {

        String title;
        String contentText;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.intro_step_1,container,false);
        }

        @Override
        public void onStart() {
            super.onStart();
            TextView lblStepNo = getView().findViewById(R.id.lblStepNo);
            TextView lblStepText = getView().findViewById(R.id.lblStepText);
            if(title != null){
                lblStepNo.setText(title);
            }

            if(contentText != null){
                lblStepText.setText(contentText);
            }
        }
    }
}
