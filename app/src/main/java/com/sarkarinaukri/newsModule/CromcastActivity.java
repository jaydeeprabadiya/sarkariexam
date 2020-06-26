package com.sarkarinaukri.newsModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.sarkarinaukri.R;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.util.ActivitySplitAnimationUtil;

public class CromcastActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private WebView web;
    private TextView tvTite;
    private ImageView ivClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplitAnimationUtil.prepareAnimation(this);
        setContentView(R.layout.activity_cromcast);
        init();
    }
    private void init()
    {
        // Animation duration of 1 second
        ActivitySplitAnimationUtil.animate(this, 1000);

        if (PreferenceHelper.ReadSharePrefrence(this, Constant.ADS).equalsIgnoreCase("true"))
        {

            mInterstitialAd = new InterstitialAd(this);
            //test ad
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            //ive ads
            //mInterstitialAd.setAdUnitId("ca-app-pub-1792973056979970/2885358048");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.e("Ads", "onAdLoaded");
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.e("TAG", "The interstitial wasn't loaded yet.");
                    }
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.e("Ads", "onAdFailedToLoad");
                }

                @Override
                public void onAdOpened() {
                    Log.e("Ads", "onAdOpened");
                }

                @Override
                public void onAdLeftApplication() {
                    Log.e("Ads", "onAdLeftApplication");
                }

                @Override
                public void onAdClosed() {

                }
            });
        }

        web=findViewById(R.id.web);
        web.getSettings().setLoadsImagesAutomatically(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web.loadUrl(PreferenceHelper.ReadSharePrefrence(CromcastActivity.this, Constant.URL));

        //HelperClass.openUrlInChromeCustomTab(CromcastActivity.this, PreferenceHelper.ReadSharePrefrence(CromcastActivity.this, Constant.URL));
        web.setWebViewClient(new myWebViewClient());
        ivClose=findViewById(R.id.ic_close);
        tvTite=findViewById(R.id.tv_title);
        tvTite.setText(PreferenceHelper.ReadSharePrefrence(CromcastActivity.this, Constant.TITLE));
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }



    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onStop() {
        // If we're currently running the entrance animation - cancel it
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}