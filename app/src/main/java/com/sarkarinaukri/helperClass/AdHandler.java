package com.sarkarinaukri.helperClass;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by ANURAG KATIYAR on 12/18/2015.
 */
public class AdHandler {
    public static String INT_AD_ID = "ca-app-pub-1792973056979970/3743766440";
    public static InterstitialAd mInterstitialAd;

    public static void init(Context context) {
        try {
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(INT_AD_ID);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showIfLoaded() {
        try {
            if (mInterstitialAd.isLoaded())
                mInterstitialAd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
