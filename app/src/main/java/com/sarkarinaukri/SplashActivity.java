package com.sarkarinaukri;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //startService(new Intent(getBaseContext(), OnClearFromRecentService.class));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash);

//        if (TextUtils.isEmpty(PreferenceHelper.getLanguageType(SplashActivity.this))) {
//            Intent intent = new Intent(SplashActivity.this, LanguageChooserActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    if (!TextUtils.isEmpty(PreferenceHelper.getLanguageType(SplashActivity.this))) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                   // }
                }
            }, SPLASH_TIME_OUT);
        //}
    }



}
