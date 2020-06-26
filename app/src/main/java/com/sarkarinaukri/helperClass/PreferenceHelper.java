package com.sarkarinaukri.helperClass;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.sarkarinaukri.model.UserProfile;

import java.util.ArrayList;

/**
 * Created by AppsMediaz Technologies on 7/26/2017.
 */
public class PreferenceHelper {
    private static SharedPreferences sp;


    private static void storeValueInPreference(Context context, String key, String value) {
        sp = context.getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getValueFromPreference(Context context, String keyValue) {
        sp = context.getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        String myValue = sp.getString(keyValue, "");
        return myValue;
    }

    public static String getLanguageType(Context context) {
        return getValueFromPreference(context, keyValue.languageType.name());
    }

    public static String getVideoPlayCount(Context context) {
        return getValueFromPreference(context, keyValue.videoPlayCount.name());
    }

    public static String getToken(Context context) {
        return getValueFromPreference(context, keyValue.token.name());
    }

    public static String getEmail(Context context) {
        return getValueFromPreference(context, keyValue.email.name());
    }

    public static String getUserId(Context context) {
        return getValueFromPreference(context, keyValue.userId.name());
    }

    public static String getName(Context context) {
        return getValueFromPreference(context, keyValue.name.name());
    }

    public static String getSignUpMedium(Context context) {
        return getValueFromPreference(context, keyValue.signUpMedium.name());
    }

    public static void clearPreferenceData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        preferences.edit().remove(keyValue.languageType.name()).apply();
    }

    public static void saveLanguageType(Context context, String languageType) {
        storeValueInPreference(context, keyValue.languageType.name(), languageType);
    }

    public static void saveVideoPlayCount(Context context, String languageType) {
        storeValueInPreference(context, keyValue.videoPlayCount.name(), languageType);
    }

    public static void savequestionid(Context context, String questionid) {
        storeValueInPreference(context, keyValue.questionid.name(), questionid);
    }

    public static void savequestiontype(Context context, String questiontype) {
        storeValueInPreference(context, keyValue.questiontype.name(), questiontype);
    }

    public static String getquestionid(Context context) {
        return getValueFromPreference(context, keyValue.questionid.name());
    }

    public static String getquestiontype(Context context) {
        return getValueFromPreference(context, keyValue.questiontype.name());
    }

    public static void saveProfileData(Context context, UserProfile userProfile) {
        storeValueInPreference(context, keyValue.userId.name(), userProfile.getUserId());
        storeValueInPreference(context, keyValue.email.name(), userProfile.getEmail());
        storeValueInPreference(context, keyValue.name.name(), userProfile.getFullName());
        storeValueInPreference(context, keyValue.mobile.name(), userProfile.getMobile());
        storeValueInPreference(context, keyValue.photo.name(), userProfile.getPhoto());
        storeValueInPreference(context, keyValue.token.name(), userProfile.getToken());
        storeValueInPreference(context, keyValue.referalCode.name(), userProfile.getReferalCode());
        storeValueInPreference(context, keyValue.walletAmount.name(), userProfile.getWalletAmount());
        storeValueInPreference(context, keyValue.signUpMedium.name(), userProfile.getSignupMedium());
    }

    public static void logout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        preferences.edit().remove(keyValue.userId.name()).apply();
        preferences.edit().remove(keyValue.email.name()).apply();
        preferences.edit().remove(keyValue.name.name()).apply();
        preferences.edit().remove(keyValue.mobile.name()).apply();
        preferences.edit().remove(keyValue.photo.name()).apply();
        preferences.edit().remove(keyValue.token.name()).apply();
        preferences.edit().remove(keyValue.referalCode.name()).apply();
        preferences.edit().remove(keyValue.walletAmount.name()).apply();
        preferences.edit().remove(keyValue.signUpMedium.name()).apply();
    }

    public enum keyValue {
        languageType,
        videoPlayCount,
        token,
        email,
        userId,
        name,
        mobile,
        photo,
        referalCode,
        walletAmount,
        signUpMedium,
        questionid,
        questiontype,
    }

    public static void WriteSharePrefrence(Context context, String key, String value) {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String ReadSharePrefrence(Context context, String key) {
        String data;
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        data = preferences.getString(key, "");
        editor.apply();
        return data;
    }






}
