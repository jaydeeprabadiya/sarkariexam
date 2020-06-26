package com.sarkarinaukri.saveModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.RetroFit.ApiclientArchi;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.QuestionList;
import com.sarkarinaukri.qnaModule.NewLoginActivity;
import com.sarkarinaukri.qnaModule.NewQuestionActivity;
import com.sarkarinaukri.newsModule.QuestionCardActivity;
import com.sarkarinaukri.qnaModule.VideoActivity;
import com.sarkarinaukri.saveModule.adapter.SaveViewPagerAdapter;

import java.util.Collections;
import java.util.List;

import cn.youngkaaa.yviewpager.YViewPager;
import q.rorbin.verticaltablayout.VerticalTabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveNewActivity extends AppCompatActivity implements SaveViewPagerAdapter.OnDeleteItemClickListener {

    private String TAG="SaveNewActivity";
    //    private List<FragmentInner> mFragmentInners;
    private List<QuestionList.data> mFragmentInners;
    private VerticalTabLayout mTabLayout;
    private YViewPager mViewPager;

    private LinearLayout llQuestion;
    private LinearLayout llImage;
    ViewPager2 viewPager2;

    /////////////////////

    private LinearLayout llHome;
    private LinearLayout llNews;
    private LinearLayout llSave;
    private RelativeLayout rlGk;
    private RelativeLayout rlPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new);
        init();
    }

    private void init() {


//        mViewPager = findViewById(R.id.yviewpager);
//        mTabLayout = findViewById(R.id.tablayout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);


        Log.e(TAG,"USERID==>"+ PreferenceHelper.getUserId(SaveNewActivity.this));

        if (PreferenceHelper.ReadSharePrefrence(SaveNewActivity.this, Constant.USER_ID).equalsIgnoreCase("")) {

            Intent intent = new Intent(SaveNewActivity.this, NewLoginActivity.class);
            startActivityForResult(intent, 102);

        }
        else
        {
            getQuestion();
        }


        llHome=findViewById(R.id.llHome);
        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        llNews=findViewById(R.id.ll_news);
        llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SaveNewActivity.this, QuestionCardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llSave=findViewById(R.id.ll_save);
        llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getQuestion();

            }
        });

        rlGk=findViewById(R.id.rl_gk);
        rlGk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SaveNewActivity.this, NewQuestionActivity.class);
                startActivity(intent);
                finish();

            }
        });

        rlPlay=findViewById(R.id.rl_play);
        rlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SaveNewActivity.this, VideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });



    }

    private void getQuestion() {
        HelperClass.showProgressDialog(SaveNewActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<QuestionList> call = apiInterface.getWhishlist(PreferenceHelper.ReadSharePrefrence(SaveNewActivity.this, Constant.USER_ID));
        call.enqueue(new Callback<QuestionList>() {
            @Override

            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                //HelperClass.displayRequestAndResponse(response, null);

                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    QuestionList resultEntity = response.body();

                    if (resultEntity.status.equalsIgnoreCase("true"))
                    {

                        Collections.reverse(resultEntity.data);
                         viewPager2.setAdapter(new SaveViewPagerAdapter(SaveNewActivity.this, resultEntity.data, viewPager2,SaveNewActivity.this));

//                        mViewPager.setAdapter(new CustomPagerAdapter(SaveNewActivity.this, resultEntity.data));
//                        mTabLayout.setupWithViewPager(mViewPager);
                    }
                    else
                    {
                        Toast.makeText(SaveNewActivity.this, "No Save Post", Toast.LENGTH_SHORT).show();
                        viewPager2.setVisibility(View.GONE);
                    }

                } else {

                    Toast.makeText(SaveNewActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable t) {
                Toast.makeText(SaveNewActivity.this, "Please Try again!", Toast.LENGTH_SHORT).show();
                HelperClass.dismissProgressDialog();
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102 && resultCode == RESULT_OK) {

            getQuestion();

        }
        else {

            finish();
        }
    }

    @Override
    public void onDeleteClicked() {

        getQuestion();

    }








}
