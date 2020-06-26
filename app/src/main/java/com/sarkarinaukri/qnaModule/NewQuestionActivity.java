package com.sarkarinaukri.qnaModule;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
import com.sarkarinaukri.newsModule.QuestionCardActivity;
import com.sarkarinaukri.qnaModule.adapter.NewQuestionViewpagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewQuestionActivity extends AppCompatActivity implements NewQuestionViewpagerAdapter.OnDeleteItemClickListener,GoogleApiClient.OnConnectionFailedListener {

    ViewPager2 viewPager2;
    SwipeRefreshLayout swipeRefresh;
    ArrayList<QuestionList.data> tempdatalist;
    private String TAG = "NewQuestionActivity";
    private LinearLayout llHome;
    private LinearLayout llNews;
    private LinearLayout llSave;
    private RelativeLayout rlGk;
    private RelativeLayout rlPlay;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 006;
    private AdView adView;
    private FrameLayout adContainerView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        init();
    }

    private void init() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initGoogle();

        //menu = navigationView.getMenu();
        //coordinatorLayout = findViewById(R.id.constraint);
        viewPager2 = findViewById(R.id.viewPager2);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        adContainerView = findViewById(R.id.ad_view_container);


            getQuestionpaginations();





        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getQuestionpull();
                // swipeRefresh.setRefreshing(false);
            }
        });

        llHome = findViewById(R.id.llHome);
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

                Intent intent = new Intent(NewQuestionActivity.this, QuestionCardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        llSave = findViewById(R.id.ll_save);
        llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NewQuestionActivity.this, SaveNewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        rlGk = findViewById(R.id.rl_gk);
        rlGk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                if (PreferenceHelper.ReadSharePrefrence(NewQuestionActivity.this, Constant.QUES_FIRST).equalsIgnoreCase("1")) {
//                    tempdatalist = getArrayList("question");
//                    getQuestiontemp();
//                } else {

                    //getQuestion();

                    getQuestionpaginations();

              //  }

            }
        });



        rlPlay = findViewById(R.id.rl_play);
        rlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NewQuestionActivity.this, VideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();


//                final Dialog dialog = new Dialog(NewQuestionActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setCancelable(true);
//                dialog.setContentView(R.layout.dialog_commingsoon);
//
//
//                ImageView ivCooming = dialog.findViewById(R.id.iv_comming);
//                ImageView ivClose = dialog.findViewById(R.id.ivBackButton);
//
//                Glide.with(NewQuestionActivity.this)
//                        .asGif()
//                        .load(R.drawable.ic_cooming)
//                        .into(ivCooming);
//
//                dialog.show();
//
//                ivClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        dialog.dismiss();
//                    }
//                });


            }
        });

//        adView = new AdView(NewQuestionActivity.this);
//        adView.setAdUnitId(getString(R.string.ad_baner_id_test));
//        adContainerView.addView(adView);
//        loadBanner();

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345")).build());

        adContainerView = findViewById(R.id.ad_view_container);

        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });



    }






//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        getQuestionpaginations();
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {

            //getQuestion();
            getQuestionpaginations();

        }

        if (requestCode == RESULT_OK) {
            // Retry initialization if user performed a recovery action
//            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
            Toast.makeText(this, "COUNT", Toast.LENGTH_SHORT).show();

        }

        else if (requestCode == RC_SIGN_IN) {
            //ifAppIsLogedIn();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            String id = acct.getId();
            signUpFromOurServer(id,email,"email",personName);

        }
    }

    private void signUpFromOurServer(String socialId,final String emailID, String type,final String name) {
        HelperClass.showProgressDialog(NewQuestionActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<Newlogin> call = apiInterface.socialsLogin(socialId,emailID,type,name);
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

                        //ifAppIsLogedIn();
                        PreferenceHelper.WriteSharePrefrence(NewQuestionActivity.this,Constant.USER_ID,resultEntity.data.id);
                        //getQuestionpaginations();

//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                        finish();
                    } else {

                        signUpFromOurServer(socialId,emailID,"email",name);

//                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.msg, Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                signUpFromOurServer(socialId,emailID,"email",name);
//                            }
//                        });
//                        snackbar.setActionTextColor(Color.RED);
//                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Newlogin> call, Throwable t) {

            }
        });
    }




    private void getQuestionpull()
    {
        HelperClass.showProgressDialog(NewQuestionActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<QuestionList> call = apiInterface.getquestionpagination(PreferenceHelper.ReadSharePrefrence(NewQuestionActivity.this, Constant.USER_ID),"1");
        HelperClass.dismissProgressDialog();
        call.enqueue(new Callback<QuestionList>() {
            @Override
            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    QuestionList resultEntity = response.body();

//                    final QuestionList loginOperator = new Gson().fromJson(String.valueOf(response), QuestionList.class);
//                    if (loginOperator.status.equals("true")) {

//                        mViewPager.setAdapter(new CustomPagerAdapter(QuestionCardActivity.this, resultEntity.data));
//                        mTabLayout.setupWithViewPager(mViewPager);
                    swipeRefresh.setRefreshing(false);
                    //saveArrayList(resultEntity.data, "question");
                    viewPager2.setAdapter(new NewQuestionViewpagerAdapter(NewQuestionActivity.this, resultEntity.data, viewPager2,NewQuestionActivity.this));


                } else {

                    Toast.makeText(NewQuestionActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(NewQuestionActivity.this, "Please Try again!", Toast.LENGTH_SHORT).show();
                HelperClass.dismissProgressDialog();
            }
        });


    }


    private void getQuestionpaginations() {
        HelperClass.showProgressDialog(NewQuestionActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<QuestionList> call = apiInterface.getquestionpagination(PreferenceHelper.ReadSharePrefrence(NewQuestionActivity.this, Constant.USER_ID),"1");
        call.enqueue(new Callback<QuestionList>() {
            @Override
            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    QuestionList resultEntity = response.body();

//                    final QuestionList loginOperator = new Gson().fromJson(String.valueOf(response), QuestionList.class);
//                    if (loginOperator.status.equals("true")) {

//                        mViewPager.setAdapter(new CustomPagerAdapter(QuestionCardActivity.this, resultEntity.data));
//                        mTabLayout.setupWithViewPager(mViewPager);

                    //PreferenceHelper.WriteSharePrefrence(NewQuestionActivity.this, Constant.QUES_FIRST, "1");
                    //saveArrayList(resultEntity.data, "question");
                    viewPager2.setAdapter(new NewQuestionViewpagerAdapter(NewQuestionActivity.this, resultEntity.data, viewPager2,NewQuestionActivity.this));


                } else {

                    Toast.makeText(NewQuestionActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable t) {
                Toast.makeText(NewQuestionActivity.this, "Please Try again!", Toast.LENGTH_SHORT).show();
                HelperClass.dismissProgressDialog();
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
        //getQuestionpull();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ad_baner_id));
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }



    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(NewQuestionActivity.this, adWidth);
    }

}
