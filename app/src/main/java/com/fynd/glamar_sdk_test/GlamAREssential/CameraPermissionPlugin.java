package com.fynd.glamar_sdk_test.GlamAREssential;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fynd.glamar_sdk_test.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;

public class CameraPermissionPlugin {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private static CameraPermissionPlugin instance;

    public static CameraPermissionPlugin getInstance() {
        if (instance == null) {
            instance = new CameraPermissionPlugin();
        }
        return instance;
    }

    public boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(UnityPlayerActivity.currentActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestCameraPermission() {
        //Log.d("UnityPlayerCheck", (UnityPlayer.currentActivity == null) + "");
        ActivityCompat.requestPermissions(UnityPlayerActivity.currentActivity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted
                //UnityPlayer.UnitySendMessage("CameraPermissionCallback", "OnCameraPermissionGranted", "");
            } else {
                // Camera permission denied
                //UnityPlayer.UnitySendMessage("CameraPermissionCallback", "OnCameraPermissionDenied", "");
            }
        }
    }
}
