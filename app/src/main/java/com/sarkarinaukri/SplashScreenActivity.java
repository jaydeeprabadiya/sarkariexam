package com.sarkarinaukri;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.VideoView;

import com.sarkarinaukri.helperClass.OnClearFromRecentService;
import com.sarkarinaukri.helperClass.PreferenceHelper;

public class SplashScreenActivity extends AppCompatActivity {
    private VideoView videoView;
    private int videoPlayCount = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash_screen);
        videoView = findViewById(R.id.videoView);

        //startService(new Intent(getBaseContext(), OnClearFromRecentService.class));


        if (!TextUtils.isEmpty(PreferenceHelper.getVideoPlayCount(this))) {
            videoPlayCount = Integer.parseInt(PreferenceHelper.getVideoPlayCount(this));
            videoPlayCount = videoPlayCount + 1;
            PreferenceHelper.saveVideoPlayCount(this, "" + videoPlayCount);

            if (videoPlayCount < 10) {
                startActivity(new Intent(this, SplashActivity.class));
                finish();
            } else {
                PreferenceHelper.saveVideoPlayCount(this, "0");
                Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.se);
                videoView.setVideoURI(video);

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        startNextActivity();
                    }
                });

                videoView.start();
            }
        } else {
            PreferenceHelper.saveVideoPlayCount(this, "0");
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.se);
            videoView.setVideoURI(video);

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    startNextActivity();
                }
            });

            videoView.start();
        }
    }

    private void startNextActivity() {
        if (isFinishing())
            return;
//        if (TextUtils.isEmpty(PreferenceHelper.getLanguageType(SplashScreenActivity.this))) {
//            Intent intent = new Intent(SplashScreenActivity.this, LanguageChooserActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
       // }
    }
}
