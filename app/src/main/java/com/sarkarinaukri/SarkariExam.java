package com.sarkarinaukri;

import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OSInAppMessageAction;
import com.onesignal.OneSignal;
import com.sarkarinaukri.firebase.MyNotificationOpenedHandler;
import com.sarkarinaukri.helperClass.ConnectivityReceiver;

public class SarkariExam extends MultiDexApplication {

    private static SarkariExam mApp;
    public static Context context;
    public static FirebaseAnalytics firebaseAnalytics;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        context = getApplicationContext();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(context))
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

    }

    public static synchronized SarkariExam getInstance() {
        return mApp;
    }

    public static Context getContext() {
        return context;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
