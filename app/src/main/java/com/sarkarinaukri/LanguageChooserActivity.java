package com.sarkarinaukri;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.sarkarinaukri.fragment.HomeFragment;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;

public class LanguageChooserActivity extends AppCompatActivity implements OnClickListener {
    private ImageView ivEnglish;
    private ImageView ivHindi;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        setContentView(R.layout.activity_language_chooser);
        init();
    }

    private void init() {
        ivEnglish = findViewById(R.id.ivEnglish);
        ivHindi = findViewById(R.id.ivHindi);

        clickListener();
    }

    private void clickListener() {
        this.ivEnglish.setOnClickListener(this);
        this.ivHindi.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEnglish:
                HelperClass.showProgressDialog(this, "");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivEnglish.setClickable(false);
                        ivEnglish.setFocusable(false);

                        HomeFragment.boxDataList.clear();
                        HomeFragment.hotJobList.clear();
                        HomeFragment.resultsList.clear();
                        HomeFragment.admitCardList.clear();
                        HomeFragment.onlineFormList.clear();

                        PreferenceHelper.saveLanguageType(LanguageChooserActivity.this, Constant.ENGLISH);
                        Intent intent = new Intent(LanguageChooserActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 500);

                return;
            case R.id.ivHindi:
                HelperClass.showProgressDialog(this, "");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivHindi.setClickable(false);
                        ivHindi.setFocusable(false);

                        HomeFragment.boxDataList.clear();
                        HomeFragment.hotJobList.clear();
                        HomeFragment.resultsList.clear();
                        HomeFragment.admitCardList.clear();
                        HomeFragment.onlineFormList.clear();

                        PreferenceHelper.saveLanguageType(LanguageChooserActivity.this, Constant.HINDI);
                        Intent intent = new Intent(LanguageChooserActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 500);
                return;
            default:
                return;
        }
    }
}
