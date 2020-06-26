package com.sarkarinaukri.helperClass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 *  Created by archirayan on 6/3/20.
    Author:www.archirayan.com
    Email:info@archirayan.com
 */
public class OnClearFromRecentService extends Service   {

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.e("ClearFromRecentService", "Service Started");
            return START_NOT_STICKY;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            PreferenceHelper.WriteSharePrefrence(OnClearFromRecentService.this,Constant.NEWS_FIRST,"");
            PreferenceHelper.WriteSharePrefrence(OnClearFromRecentService.this,Constant.QUES_FIRST,"");
            PreferenceHelper.WriteSharePrefrence(OnClearFromRecentService.this,Constant.VIDEO_FIRST,"");

            Log.e("ClearFromRecentService", "Service Destroyed");
        }

        @Override
        public void onTaskRemoved(Intent rootIntent) {
            Log.e("ClearFromRecentService", "END");
            //Code here
            stopSelf();
        }

}
