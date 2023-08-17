package com.fynd.glamarcommunication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public  class CameraPermission {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private static CameraPermission instance;

    private static Activity currentActivity;

    public static CameraPermission getInstance() {
        if (instance == null) {
            instance = new CameraPermission();
        }
        return instance;
    }

    public void setCurrentActivty(Activity activty) {
        this.currentActivity = activty;
    }

    public boolean hasCameraPermission() {

        if(currentActivity == null)
        {
            Log.e("SDK_CameraPermission", "No Context defined; use setCurrentActivity().");
            return false;
        }

        return ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestCameraPermission() {
        if(currentActivity == null)
        {
            Log.e("SDK_CameraPermission", "No Context defined; use setCurrentActivity().");
            return;
        }
        ActivityCompat.requestPermissions(currentActivity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted

            } else {
                // Camera permission denied

            }
        }
    }
}
