package com.sarkarinaukri.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by anuragkatiyar on 8/5/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String FRIENDLY_ENGAGE_TOPIC = "friendly_engage";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d(token, "FCM Token: " + token);
        FirebaseMessaging.getInstance().subscribeToTopic(FRIENDLY_ENGAGE_TOPIC);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    }
}
