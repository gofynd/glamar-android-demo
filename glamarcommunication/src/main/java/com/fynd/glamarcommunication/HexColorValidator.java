package com.fynd.glamarcommunication;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class HexColorValidator {
    private static final String HEX_COLOR_PATTERN = "^#([A-Fa-f0-9]{6})$";

    public static boolean isValidHexColor(String colorCode) {
        Pattern pattern = Pattern.compile(HEX_COLOR_PATTERN);
        Matcher matcher = pattern.matcher(colorCode);
        return matcher.matches();
    }
}
