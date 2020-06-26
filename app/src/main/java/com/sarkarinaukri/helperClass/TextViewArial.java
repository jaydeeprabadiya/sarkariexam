package com.sarkarinaukri.helperClass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TextViewArial extends androidx.appcompat.widget.AppCompatTextView {


    public TextViewArial(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "arial.ttf");
        this.setTypeface(face);
    }

    public TextViewArial(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "arial.ttf");
        this.setTypeface(face);
    }

    public TextViewArial(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "arial.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}