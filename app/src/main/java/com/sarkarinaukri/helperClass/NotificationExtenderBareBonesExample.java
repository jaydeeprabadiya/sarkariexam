package com.sarkarinaukri.helperClass;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

/**
 * Created by anuragkatiyar on 12/8/17.
 */

public class NotificationExtenderBareBonesExample extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        if (receivedResult.payload.additionalData == null) {
            return false;
        }
        return true;
    }
}