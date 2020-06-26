package com.sarkarinaukri.qnaModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.sarkarinaukri.RetroFit.ApiclientVideo;
import com.sarkarinaukri.saveModule.SaveNewActivity;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.MediaObject;
import com.sarkarinaukri.model.Newlogin;
import com.sarkarinaukri.model.Newvideo;
import com.sarkarinaukri.model.QuestionList;
import com.sarkarinaukri.newsModule.QuestionCardActivity;
import com.sarkarinaukri.videoModule.adapter.NewVideoAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends AppCompatActivity implements NewVideoAdapter.OnDeleteItemClickListener,NewVideoAdapter.OnloginClickListener,GoogleApiClient.OnConnectionFailedListener {

    private String TAG = "VideoActivity";
    ViewPager2 viewPager2;
    SwipeRefreshLayout swipeRefresh;

    private ArrayList<MediaObject> mediaObjectList = new ArrayList<>();
    ArrayList<Newvideo.data> tempdatalist;

    private LinearLayout llHome;
    private LinearLayout llSave;
    private LinearLayout llNews;
    private RelativeLayout rlGk;
    private RelativeLayout rlPlay;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 006;
    private NewVideoAdapter newVideoAdapter=null;
    int curentposition;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        init();

        //initView();
    }




    private void init() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initGoogle();

        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                curentposition=position;

                for(int i=0;i<tempdatalist.size();i++)
                {
                    if (i==position)
                    {
                        tempdatalist.get(i).isSelected=true;

                    }
                    else
                    {
                        tempdatalist.get(i).isSelected=false;


                    }
                }

                newVideoAdapter.notifyDataSetChanged();


            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

        });

            Newgetvideopagination();




        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // swipeRefresh.setRefreshing(false);
                tempdatalist.get(0).isSelected=false;
                newVideoAdapter.notifyDataSetChanged();
                Newgetvideopagination();
            }
        });


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

                Intent intent = new Intent(VideoActivity.this, QuestionCardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });


        llSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(VideoActivity.this, SaveNewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        rlGk = findViewById(R.id.rl_gk);
        rlGk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(VideoActivity.this, NewQuestionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        rlPlay = findViewById(R.id.rl_play);
        rlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Newgetvideopagination();
            }
        });


    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.e(TAG, "ONSTOP==>" + "ONSTOP CALLED");
//        tempdatalist.get(curentposition).isSelected=false;
//        newVideoAdapter.notifyDataSetChanged();
//        finish();
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tempdatalist.get(curentposition).isSelected=false;
        newVideoAdapter.notifyDataSetChanged();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tempdatalist.get(curentposition).isSelected=false;
        newVideoAdapter.notifyDataSetChanged();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        getvideopagination();
//
//
////        if (recycleView != null) {
////            recycleView.onRestartPlayer();
////        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
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
        HelperClass.showProgressDialog(VideoActivity.this, "");
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
                        PreferenceHelper.WriteSharePrefrence(VideoActivity.this,Constant.USER_ID,resultEntity.data.id);
                    } else {

                        signUpFromOurServer(socialId,emailID,"email",name);

                    }
                }
            }


            @Override
            public void onFailure(Call<Newlogin> call, Throwable t) {

            }
        });
    }
    private void getQuestionpull() {
        HelperClass.showProgressDialog(VideoActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<QuestionList> call = apiInterface.getvideopagination(PreferenceHelper.ReadSharePrefrence(VideoActivity.this, Constant.USER_ID),"1");
        HelperClass.dismissProgressDialog();
        call.enqueue(new Callback<QuestionList>() {
            @Override
            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                //HelperClass.displayRequestAndResponse(response, null);


                if (response.code() == 200) {
                    QuestionList resultEntity = response.body();

                    swipeRefresh.setRefreshing(false);
                    //viewPager2.setAdapter(new NewVideoAdapter(VideoActivity.this, resultEntity.data, viewPager2,VideoActivity.this));


                } else {

                    Toast.makeText(VideoActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable t) {
                Toast.makeText(VideoActivity.this, "Please Try again!", Toast.LENGTH_SHORT).show();
                HelperClass.dismissProgressDialog();
            }

        });

    }




    private void Newgetvideopagination() {
        HelperClass.showProgressDialog(VideoActivity.this, "");
        ApiInterface apiInterface = ApiclientVideo.getClient().create(ApiInterface.class);
        Call<Newvideo> call = apiInterface.Newgetvideopagination("1",PreferenceHelper.ReadSharePrefrence(VideoActivity.this, Constant.USER_ID));
        call.enqueue(new Callback<Newvideo>() {
            @Override
            public void onResponse(Call<Newvideo> call, Response<Newvideo> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                    HelperClass.dismissProgressDialog();
                    Newvideo resultEntity = response.body();
                    swipeRefresh.setRefreshing(false);
                    if (resultEntity.msg.equalsIgnoreCase("Data Found"))
                    {
                        tempdatalist=resultEntity.data;
                        newVideoAdapter=new NewVideoAdapter(VideoActivity.this, resultEntity.data, viewPager2,VideoActivity.this,VideoActivity.this);
                        viewPager2.setAdapter(newVideoAdapter);
                    }


            }

            @Override
            public void onFailure(Call<Newvideo> call, Throwable t) {
                Toast.makeText(VideoActivity.this, "Please Try again!", Toast.LENGTH_SHORT).show();
                HelperClass.dismissProgressDialog();
            }
        });

    }



    private void initGoogle()
    {
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
    public void onDeleteClicked(int page)
    {
        ApiInterface apiInterface = ApiclientVideo.getClient().create(ApiInterface.class);
        Call<Newvideo> call = apiInterface.Newgetvideopagination(String.valueOf(page),PreferenceHelper.ReadSharePrefrence(VideoActivity.this, Constant.USER_ID));
        call.enqueue(new Callback<Newvideo>() {
            @Override
            public void onResponse(Call<Newvideo> call, Response<Newvideo> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                HelperClass.dismissProgressDialog();
                Newvideo resultEntity = response.body();
                tempdatalist.addAll(resultEntity.data);


            }

            @Override
            public void onFailure(Call<Newvideo> call, Throwable t) {

            }
        });

    }

    @Override
    public void onloginClicked() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public interface OnStpClickListener {
        public void onStpClicked();
    }




}
