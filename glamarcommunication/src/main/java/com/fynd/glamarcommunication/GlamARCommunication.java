package com.fynd.glamarcommunication;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.unity3d.player.UnityPlayer;

public class GlamARCommunication {

    private static GlamARCommunication m_instance;

    private String API_KEY;
    private String activityName;
    public CameraPermission cameraPermission;
    protected String TAG = "GlamARSDK";

    private ICommunicationMessage message;

    private GlamARCommunication() {}

    public static GlamARCommunication getInstance() {
        if (m_instance == null) {
            synchronized (GlamARCommunication.class) {
                if (m_instance == null) {
                    m_instance = new GlamARCommunication();
                }
            }
        }
        return m_instance;
    }

    public void InitGlamAR(String API_KEY, String activityName, ICommunicationMessage message) {
        setAuthorizationKey(API_KEY);
        setCurrentActivityName(activityName);
        getInstance().cameraPermission = CameraPermission.getInstance();
        this.message = message;
    }

    public void setCameraActivity(Activity activity) {
        cameraPermission.setCurrentActivty(activity);
    }

    private void setAuthorizationKey(String API_KEY) {
        this.API_KEY = API_KEY;
        UnityPlayer.UnitySendMessage("GlamCommunication", "SetAuthorizationKey", this.API_KEY);
    }

    private void setCurrentActivityName(String activityName) {
        this.activityName = activityName;
        UnityPlayer.UnitySendMessage("GlamCommunication", "SetActivityName", this.activityName);
    }

    public void fetchParticularSKUId(String skuId) {
        if(skuId.isEmpty()) {
            return;
        }

//        if(!validateCalls(callback)) {
//            callback.errorMessage("Validation failed");
//        }

        UnityPlayer.UnitySendMessage("GlamCommunication", "FetchParticularSKUId", skuId);
    }

    public void launchModuleByName(String category, String currentColorVal) {

        String moduleName = null;
        switch (category) {
            case "Eye Liner":
                moduleName = "eyelinertryon";
                break;
            case "Blush":
                moduleName = "blushtryon";
                break;
            case "Eye Shadow":
                moduleName = "eyeshadowtryon";
                break;
            case "Lipstick":
                moduleName = "lipstryon";
                break;
            case "Hair":
                moduleName = "hairtryon";
            default:
        }
        assert moduleName != null;

        UnityPlayer.UnitySendMessage("GlamModuleCalls", "LaunchModuleByName", moduleName);
        UnityPlayer.UnitySendMessage("GlamModuleCalls", "ApplyColor", currentColorVal);
    }

    public void setStyleId(String styleId) throws NumberFormatException {
        try {
            int id = Integer.parseInt(styleId);
        } catch (NumberFormatException e) {
            return;
        }
        UnityPlayer.UnitySendMessage("GlamModuleCalls", "SetStyleId", styleId);
    }

    public void setColorIntensity(String currentIntensityVal) {
        try {
            int id = Integer.parseInt(currentIntensityVal);
        } catch (NumberFormatException e) {
            return;
        }
        UnityPlayer.UnitySendMessage("GlamModuleCalls", "ApplyColorIntensity", currentIntensityVal + "");
    }

    public void applyMakeupConfig(String makeupJsonString) {

        if(!validateCalls()) {
            return;
        }
        UnityPlayer.UnitySendMessage("GlamCommunication", "ApplyMakeupByConfig", makeupJsonString);
//        Gson gson = new Gson();
//        SKUConfig config = null;
//        try {
//            config = gson.fromJson(makeupJsonString, SKUConfig.class);
//
//
//
//        } catch (Exception e) {
//        }

    }

    public void tryOnModel(boolean doTry) {
        if(!validateCalls()) {
            return;
        }

         int try3d = (doTry) ? 1 : 0;

        UnityPlayer.UnitySendMessage("GlamCommunication", "ToggleTryOn", try3d + "");
    }

    public void tryOnImage()
    {
        if(!validateCalls()) {
            return;
        }
        UnityPlayer.UnitySendMessage("GlamCommunication", "OnClickChooseImage", "");
    }


    private boolean validateCalls() {
        if(API_KEY.isEmpty()) {
            this.message.onErrorMessage(MessageType.VALIDATION_FAILED, "API Key Missing");
            return false;
        }

        if(activityName.isEmpty()) {
            this.message.onErrorMessage(MessageType.VALIDATION_FAILED, "Activity Name not Provided");
            return false;
        }

        if(!CameraPermission.getInstance().hasCameraPermission()) {
            this.message.onErrorMessage(MessageType.VALIDATION_FAILED, "Camera Permission not provided");
            return false;
        }

        return true;
    }

    public void trackingLostCallback(boolean typeValue) {
        this.message.onFaceTrackingLost(typeValue);
    }

    public void receiveResponseCallback(int typeValue, String message) {
        MessageType type = MessageType.values()[typeValue - 1];
        Log.d(TAG, type.toString());
        if(type == MessageType.SKU_APPLIED) {
            this.message.onSuccessResponseModuleLaunch(message);
            return;
        }

        switch (type) {
            case CONFIG_APPLIED:
                this.message.onSuccessMessage(MessageType.CONFIG_APPLIED, message);
                break;
            case STYLE_APPLIED:
                this.message.onSuccessMessage(MessageType.STYLE_APPLIED, message);
                break;
            case COLOR_APPLIED:
                this.message.onSuccessMessage(MessageType.COLOR_APPLIED, message);
                break;
            case COLOR_INTENSITY_APPLIED:
                this.message.onSuccessMessage(MessageType.COLOR_INTENSITY_APPLIED, message);
                break;
            case CAMERA_OPENED:
                this.message.onSuccessMessage(MessageType.CAMERA_OPENED, message);
                break;
            case GLAM_LOADING:
                this.message.onSuccessMessage(MessageType.GLAM_LOADING, message);
                break;
            case GLAM_LOADED:
                this.message.onSuccessMessage(MessageType.GLAM_LOADED, message);
                break;
            case CONFIG_FAILED:
                this.message.onErrorMessage(MessageType.CONFIG_FAILED, message);
                break;
            case STYLE_FAILED:
                this.message.onErrorMessage(MessageType.STYLE_FAILED, message);
                break;
            case COLOR_FAILED:
                this.message.onErrorMessage(MessageType.COLOR_FAILED, message);
                break;
            case SKU_FAILED:
                this.message.onErrorMessage(MessageType.SKU_FAILED, message);
                break;
            case GLAM_FAILED:
                this.message.onErrorMessage(MessageType.GLAM_FAILED, message);
                break;
            case CAMERA_FAILED:
                this.message.onErrorMessage(MessageType.CAMERA_FAILED, message);
                break;
            case COLOR_INTENSITY_FAILED:
                this.message.onErrorMessage(MessageType.COLOR_INTENSITY_FAILED, message);
                break;
        }

    }

}
