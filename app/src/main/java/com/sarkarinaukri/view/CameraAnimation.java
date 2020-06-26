package com.sarkarinaukri.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import com.sarkarinaukri.R;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * Created by tuanha00 on 1/23/2018.
 */

public class CameraAnimation extends AppCompatImageView {
    AnimationDrawable frameAnimation;

    public CameraAnimation(Context context) {
        super(context);
        initView(context);
    }

    public CameraAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CameraAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setImageResource(R.drawable.fram_camera);
        frameAnimation = (AnimationDrawable) getDrawable();
    }

    public void start() {
        frameAnimation.start();
    }

    public void stop() {
        frameAnimation.stop();
    }
}
