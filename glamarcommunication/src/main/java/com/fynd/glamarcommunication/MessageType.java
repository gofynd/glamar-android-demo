package com.fynd.glamarcommunication;

public enum MessageType {
    SKU_APPLIED (1),
    CONFIG_APPLIED (2),
    GLAM_LOADED (3),
    GLAM_LOADING (4),
    CAMERA_OPENED (5),
    COLOR_INTENSITY_APPLIED (6),
    COLOR_APPLIED (7),
    STYLE_APPLIED (8),
    SKU_FAILED (9),
    CONFIG_FAILED (10),
    GLAM_FAILED (11),
    CAMERA_FAILED (12),
    COLOR_INTENSITY_FAILED (13),
    COLOR_FAILED (14),
    STYLE_FAILED (15),
    VALIDATION_FAILED (16);

    private final int value;

    MessageType(int value) {
        this.value = value;
    }
}
