package com.sarkarinaukri.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.sarkarinaukri.MainActivity;
import com.sarkarinaukri.helperClass.Constant;

import org.json.JSONObject;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    private Context context;

    public MyNotificationOpenedHandler(Context context) {
        this.context = context;
    }


    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        if (result.action.type == OSNotificationAction.ActionType.Opened) {
            if (TextUtils.isEmpty(result.notification.payload.launchURL)) {
                String redirectionValue = result.notification.payload.collapseId;

                Intent intent = new Intent(context, MainActivity.class);
                if (!TextUtils.isEmpty(redirectionValue)) {
                    intent.putExtra(Constant.REDIRECTION_KEY, redirectionValue);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.notification.payload.launchURL));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            }

        }
    }
}
