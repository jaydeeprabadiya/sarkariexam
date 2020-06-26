package com.sarkarinaukri.newsModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;


import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.RetroFit.ApiclientArchi;
import com.sarkarinaukri.saveModule.SaveNewActivity;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.Newlogin;
import com.sarkarinaukri.model.QuestionList;
import com.sarkarinaukri.qnaModule.NewQuestionActivity;
import com.sarkarinaukri.qnaModule.VideoActivity;
import com.sarkarinaukri.newsModule.adapter.PaintingsAdapter;
import com.sarkarinaukri.util.ActivitySplitAnimationUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionCardActivity extends AppCompatActivity implements PaintingsAdapter.OnDeleteItemClickListener,PaintingsAdapter.OnRefereshClickListener, GoogleApiClient.OnConnectionFailedListener {

    private String TAG = "QuestionCardActivity";
    ViewPager2 viewPager2;
    SwipeRefreshLayout swipeRefresh;

    private LinearLayout llHome;
    private LinearLayout llSave;
    private LinearLayout llNews;
    private RelativeLayout rlGk;
    private RelativeLayout rlPlay;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 006;
    ArrayList<QuestionList.data> jobnewsdatalist;
    private AdView adView;
    private FrameLayout adContainerView;
    FoldableListLayout foldableListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_card);
        init();

        viewPager2 = findViewById(R.id.viewPager2);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        adContainerView = findViewById(R.id.ad_view_container);


        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
         foldableListLayout = findViewById(R.id.foldable_list);


//        viewPager2.setAdapter(new ViewPagerAdapter(this, list, viewPager2));

        getJobnews();


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getQuestionpull();

            }
        });

    }

    private void init() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.e(TAG, "USERID==>" + PreferenceHelper.ReadSharePrefrence(QuestionCardActivity.this, Constant.USER_ID));
        initGoogle();

        llHome = findViewById(R.id.llHome);
        llSave = findViewById(R.id.ll_save);

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        llNews = findViewById(R.id.ll_news);
        llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getJobnews();
            }
        });


        llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(QuestionCardActivity.this, SaveNewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        rlGk = findViewById(R.id.rl_gk);
        rlGk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(QuestionCardActivity.this, NewQuestionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        rlPlay = findViewById(R.id.rl_play);
        rlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(QuestionCardActivity.this, VideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


            }
        });


    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        getJobnews();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {

            //getQuestion();
            getJobnews();

        }

        if (requestCode == RESULT_OK) {
            // Retry initialization if user performed a recovery action
//            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
            Toast.makeText(this, "COUNT", Toast.LENGTH_SHORT).show();

        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void getQuestionpull() {
        HelperClass.showProgressDialog(QuestionCardActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<QuestionList> call = apiInterface.getdatapagination(PreferenceHelper.ReadSharePrefrence(QuestionCardActivity.this, Constant.USER_ID), "1");
        HelperClass.dismissProgressDialog();
        call.enqueue(new Callback<QuestionList>() {
            @Override
            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    QuestionList resultEntity = response.body();
                    swipeRefresh.setRefreshing(false);
                    //saveArrayList(resultEntity.data,"news");
                    //viewPager2.setAdapter(new ViewPagerAdapter(QuestionCardActivity.this, resultEntity.data, viewPager2, QuestionCardActivity.this));
                } else {

                    Toast.makeText(QuestionCardActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable t) {
                Toast.makeText(QuestionCardActivity.this, "Please Try again!", Toast.LENGTH_SHORT).show();
                HelperClass.dismissProgressDialog();
                swipeRefresh.setRefreshing(false);
            }
        });


    }

    private void getJobnews() {
        HelperClass.showProgressDialog(QuestionCardActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<QuestionList> call = apiInterface.getdatapagination(PreferenceHelper.ReadSharePrefrence(QuestionCardActivity.this, Constant.USER_ID), "1");
        call.enqueue(new Callback<QuestionList>() {
            @Override
            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    QuestionList resultEntity = response.body();
                    jobnewsdatalist = resultEntity.data;
                    //PreferenceHelper.WriteSharePrefrence(QuestionCardActivity.this,Constant.NEWS_FIRST,"1");
                    //saveArrayList(resultEntity.data,"news");

                    //viewPager2.setAdapter(new RecyclerViewAdapter(QuestionCardActivity.this, jobnewsdatalist, viewPager2,mRecyclerViewItems));

                    //viewPager2.setAdapter(new ViewPagerAdapter(QuestionCardActivity.this, jobnewsdatalist, viewPager2, QuestionCardActivity.this));
                    //viewPager2.setPageTransformer(new VerticalFlipTransformation());

                    foldableListLayout.setAdapter(new PaintingsAdapter(QuestionCardActivity.this,jobnewsdatalist,QuestionCardActivity.this,QuestionCardActivity.this));

                } else {

                    Toast.makeText(QuestionCardActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable t) {
                Toast.makeText(QuestionCardActivity.this, "Please Try again!", Toast.LENGTH_SHORT).show();
                HelperClass.dismissProgressDialog();
            }
        });

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            String id = acct.getId();
            signUpFromOurServer(id, email, "email", personName);

        }
    }

    private void signUpFromOurServer(String socialId, final String emailID, String type, final String name) {
        HelperClass.showProgressDialog(QuestionCardActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<Newlogin> call = apiInterface.socialsLogin(socialId, emailID, type, name);
        call.enqueue(new Callback<Newlogin>() {
            @Override
            public void onResponse(Call<Newlogin> call, Response<Newlogin> response) {
                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    Newlogin resultEntity = response.body();

                    if (resultEntity.status.equalsIgnoreCase("true")) {
                        //PreferenceHelper.saveProfileData(NewLoginActivity.this, resultEntity.getUserData());
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
//                            updateUI(false);
                                    }
                                });

                        PreferenceHelper.WriteSharePrefrence(QuestionCardActivity.this, Constant.USER_ID, resultEntity.data.id);

                    } else {

                        signUpFromOurServer(socialId, emailID, "email", name);

                    }
                }
            }

            @Override
            public void onFailure(Call<Newlogin> call, Throwable t) {

            }
        });
    }

    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GOOGLE", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onDeleteClicked() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onRefereshClicked() {

        Intent intent = new Intent(QuestionCardActivity.this, QuestionCardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();


    }

    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(QuestionCardActivity.this, adWidth);
    }


}









