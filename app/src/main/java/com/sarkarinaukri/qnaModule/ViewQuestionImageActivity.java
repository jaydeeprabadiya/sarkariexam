package com.sarkarinaukri.qnaModule;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.sarkarinaukri.BuildConfig;
import com.sarkarinaukri.R;
import com.sarkarinaukri.helperClass.Constant;
import com.squareup.picasso.Picasso;


public class ViewQuestionImageActivity extends AppCompatActivity {
    private String questionImage;
    private ImageButton ivBackButton;
    private ImageView ivImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question_image);

        questionImage = getIntent().getStringExtra(Constant.QUESTION_IMAGE);

        ivBackButton = findViewById(R.id.ivBackButton);
        ivImageView = findViewById(R.id.ivImageView);

        String questionImageUrl = BuildConfig.IMAGE_URL + questionImage;
        Picasso.get().load(questionImageUrl).into(ivImageView);

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
