package com.fynd.glamarcommunication;

public interface ICommunicationMessage {
    public void onErrorMessage(MessageType type, String message);

    public void onSuccessMessage(MessageType type, String message);

    public void onSuccessResponseModuleLaunch(String message);

    public void onFaceTrackingLost(boolean trackingLost);

}
