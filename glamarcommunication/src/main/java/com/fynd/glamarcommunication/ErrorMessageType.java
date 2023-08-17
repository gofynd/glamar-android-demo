package com.fynd.glamarcommunication;

public enum ErrorMessageType {
    SKU_FAILED (1),
    CONFIG_FAILED (2),
    GLAM_FAILED (3),
    CAMERA_FAILED (4),
    COLOR_INTENSITY_FAILED (5),
    COLOR_FAILED (6),
    STYLE_FAILED (7);

    private final int value;

    ErrorMessageType(int value) {
        this.value = value;
    }
}
