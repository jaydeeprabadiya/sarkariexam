package com.sarkarinaukri.helperClass;

/**
 * Created by anuragkatiyar on 7/27/17.
 */

public class ValidationHelperClass {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null || target.equals("")) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static final boolean isValidPhoneNumber(String target) {
        if (target == null || target.equals("") || target.length() != 10) {
            return false;
        } else {
            return isInteger(target);
        }
    }

    public static boolean isInteger(String integerString) {
        if (integerString.matches("[0-9]+")) {
            return true;
        } else {
            return false;
        }
    }
}
