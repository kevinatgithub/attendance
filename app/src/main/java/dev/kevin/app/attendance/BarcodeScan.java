package dev.kevin.app.attendance;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import dev.kevin.app.attendance.helpers.Session;

import static com.google.android.gms.vision.CameraSource.CAMERA_FACING_BACK;
import static dev.kevin.app.attendance.helpers.AppConstants.RESULT_BARCODE_SCAN_SUCCESS;

public class BarcodeScan extends Activity {

    SurfaceView cameraPreview;
    TextView txtPreview;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        session = new Session(this);

        cameraPreview = findViewById(R.id.cameraPreview);
        txtPreview = findViewById(R.id.txtResult);
        txtPreview.setText("Scan Barcode");
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.CODE_128).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BarcodeScan.this,
                            new String[]{android.Manifest.permission.CAMERA}, RequestCameraPermissionID
                    );
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0){

                    txtPreview.post(new Runnable() {
                        @Override
                        public void run() {
                            assignBarcode(qrcodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
    }

    private void assignBarcode(String barcode) {
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("BARCODE",barcode);
        setResult(RESULT_BARCODE_SCAN_SUCCESS,resultIntent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}

