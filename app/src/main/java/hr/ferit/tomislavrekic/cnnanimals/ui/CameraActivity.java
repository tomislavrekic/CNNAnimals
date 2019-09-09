package hr.ferit.tomislavrekic.cnnanimals.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import hr.ferit.tomislavrekic.cnnanimals.R;
import hr.ferit.tomislavrekic.cnnanimals.utils.CameraController;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import hr.ferit.tomislavrekic.cnnanimals.utils.CreateFileFromBitmap;

public class CameraActivity extends AppCompatActivity {

    static int CAMERA_REQUEST_CODE = 101;

    TextureView previewScreen;
    Button takePicture;
    Intent switchIntent;

    CameraController cameraController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_screen);

        initViews();

        initListeners();

        switchIntent = new Intent(this  , MainActivity.class);
        switchIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }

        cameraController = new CameraController(this, previewScreen);

    }

    private void initViews() {
        previewScreen = findViewById(R.id.texVCameraPreview);
        takePicture = findViewById(R.id.btnTakePicture);
        takePicture.setEnabled(false);
    }

    public void broadcastIntent(){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Constants.BROADCAST_KEY1);
        broadcastIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(broadcastIntent);
    }

    private void initListeners() {
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    takePicture.setEnabled(false);
                    try {
                        CreateFileFromBitmap.createFileFromBitmap(
                                cameraController.takePicture(),
                                CameraActivity.this,
                                Constants.TEMP_IMG_KEY);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        broadcastIntent();
                        startActivityIfNeeded(switchIntent,0);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        takePicture.setEnabled(true);
        cameraController.resumeCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraController.stopCamera();
    }

}
