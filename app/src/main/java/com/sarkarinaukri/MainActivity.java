package com.sarkarinaukri;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.RetroFit.ApiclientArchi;
import com.sarkarinaukri.adapter.TabAdapter;
import com.sarkarinaukri.adapter.TabQnaAdapter;
import com.sarkarinaukri.fragment.AdmissionFragment;
import com.sarkarinaukri.fragment.AdmitCardFragment;
import com.sarkarinaukri.fragment.AnswerKeyFragment;
import com.sarkarinaukri.fragment.BtechJobsFragment;
import com.sarkarinaukri.fragment.DiplomaFragment;
import com.sarkarinaukri.fragment.HomeFragment;
import com.sarkarinaukri.fragment.OfflineFormFragment;
import com.sarkarinaukri.fragment.OnlineFormFragment;
import com.sarkarinaukri.fragment.ResultFragment;
import com.sarkarinaukri.fragment.SyllabusFragment;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.Corona;
import com.sarkarinaukri.model.ResultEntity;
import com.sarkarinaukri.qnaModule.LoginActivity;
import com.sarkarinaukri.qnaModule.NewQuestionActivity;
import com.sarkarinaukri.newsModule.QuestionCardActivity;
import com.sarkarinaukri.qnaModule.VideoActivity;
import com.sarkarinaukri.qnaModule.fragment.QNAListFragment;
import com.sarkarinaukri.saveModule.SaveNewActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sarkarinaukri.SarkariExam.context;
import static com.sarkarinaukri.SarkariExam.getContext;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    int[] categoriesList = new int[]{R.drawable.new_unselected,
            R.drawable.online_form,
            R.drawable.result,
            R.drawable.admit_card,
            R.drawable.btech_jobs,
            R.drawable.admission,
            R.drawable.offline_form,
            R.drawable.syllabus,
            R.drawable.diploma,
            R.drawable.answer_key};
    int[] categoriesSelectedList = new int[]{R.drawable.new_selected,
            R.drawable.online_form_selected,
            R.drawable.result_selected,
            R.drawable.admit_card_selected,
            R.drawable.btech_jobs_selected,
            R.drawable.admission_selected,
            R.drawable.offline_form_selected,
            R.drawable.syllabus_selected,
            R.drawable.diploma_selected,
            R.drawable.answer_key_selected};
    private RecyclerView recyclerView;
    private int selectedPosition = 0;
    private TabAdapter adapter;
    private TabQnaAdapter qnaAdapter;
    private TabLayout tabsJob;
    private TabLayout tabsQna;
    private ViewPager viewPager;

    private LinearLayout llHome;
    private LinearLayout llSaved;
    private LinearLayout llPapers;
    private LinearLayout llLiveClass;
    private RelativeLayout llQna;
    private String messageForYouTube;
    private String statusForYouTube = "0";
    private String postUrlForYouTube;
    private View rlAdditionalTab;
    private Menu menu;
    private String selectedTab;
    private TextView tvUserId;
    private String oneSignalPlayerId;
    private TextView tvCCC;
    private TextView tvPreviousPapers;
    private LinearLayout cccPre;
    private boolean doubleBackToExit = false;

    private LinearLayout rlGk;
    private RelativeLayout rlPlay;
    private ImageView ivPlay;
    private String android_id;
    View addView;
    ArrayList<TextView> buttons = new ArrayList<>();
    private CountDownTimer countDownTimer;





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-1792973056979970~8013561648");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SarkariExam.com");
        setSupportActionBar(toolbar);

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                //Toast.makeText(MainActivity.this, userId, Toast.LENGTH_LONG).show();
                oneSignalPlayerId = userId;
                Log.e("debug", "User:" + userId);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();

        View headerLayout = navigationView.getHeaderView(0);
        tvUserId = headerLayout.findViewById(R.id.tvUserId);
        ifAppIsLogedIn();

        init();

        android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        PreferenceHelper.WriteSharePrefrence(MainActivity.this, Config.deviceid, android_id);

        Log.e("MAINACTIVITY==>", "" + android_id);

    }



    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }



    private void ifAppIsLogedIn() {
        MenuItem changePassword = menu.findItem(R.id.nav_change_password);
        MenuItem logout = menu.findItem(R.id.nav_logout);
//
//        if (PreferenceHelper.getSignUpMedium(MainActivity.this).equalsIgnoreCase(UserProfile.SignUpMedium.email.name())) {
//            changePassword.setVisible(true);
//        } else {
//            changePassword.setVisible(false);
//        }

        if (!PreferenceHelper.ReadSharePrefrence(MainActivity.this, Constant.USER_ID).equals("")) {
            changePassword.setVisible(true);
            logout.setVisible(true);
        }
        else {
            changePassword.setVisible(false);
            logout.setVisible(false);

        }

//        if (TextUtils.isEmpty(PreferenceHelper.getToken(this))) {
//            logout.setVisible(false);
//        } else {
//            logout.setVisible(true);
//        }

        //  logout.setVisible(true);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (selectedTab.equalsIgnoreCase(SelectedTab.qna.name())) {
                cccPre.setVisibility(View.VISIBLE);
                setJobCategories();
            } else {
                if (doubleBackToExit) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExit = true;
                Toast.makeText(this, "बाहर जाने के लिए दुबारा क्लिक करे", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExit = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            HelperClass.shareApp(MainActivity.this);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_change_language:
                Intent intent = new Intent(MainActivity.this, LanguageChooserActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.nav_rate_us:
            case R.id.nav_update:
                HelperClass.rateUs(MainActivity.this);
                break;
            case R.id.nav_website:
                HelperClass.openUrlInChromeCustomTab(MainActivity.this, "https://www.sarkariexam.com/");
                break;
            case R.id.nav_share:
                HelperClass.shareApp(MainActivity.this);
                break;
            case R.id.nav_send:
                HelperClass.sendMail(MainActivity.this);
                break;
            case R.id.nav_join_facebook:
                HelperClass.openUrlInChromeCustomTab(MainActivity.this, "https://www.facebook.com/sarkariexam");
                break;
            case R.id.nav_join_youtube:
                HelperClass.openUrlInChromeCustomTab(MainActivity.this, "https://www.youtube.com/channel/UCzOdB9E4MwgxU0fhcHmlzHQ");
                break;
            case R.id.nav_change_password:
                changePasswordDialog();
                break;
            case R.id.nav_logout:
                logout();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init() {
        HelperClass.printKeyHash(this);
        getCorana();
        recyclerView = findViewById(R.id.recyclerView);

        llHome = findViewById(R.id.llHome);
        llSaved = findViewById(R.id.llSaved);
        llPapers = findViewById(R.id.llPapers);
        llLiveClass = findViewById(R.id.llLiveClass);
        llQna = findViewById(R.id.llQna);
        rlAdditionalTab = findViewById(R.id.rlAdditionalTab);

        rlGk = findViewById(R.id.rl_gk);
        rlPlay = findViewById(R.id.rl_play);
        ivPlay = findViewById(R.id.ivPlay);

        /*Glide.with(context)
                .asGif()
                .load(R.drawable.ic_play_gif)
                .into(ivPlay);*/


        cccPre = findViewById(R.id.cccPre);
        tvCCC = findViewById(R.id.tvCCC);
        tvPreviousPapers = findViewById(R.id.tvPreviousPapers);

        SpannableString cccExam = new SpannableString("CCC Exam");
        cccExam.setSpan(new UnderlineSpan(), 0, cccExam.length(), 0);
        tvCCC.setText(cccExam);

        SpannableString previousPapers = new SpannableString("Previous Papers");
        previousPapers.setSpan(new UnderlineSpan(), 0, previousPapers.length(), 0);
        tvPreviousPapers.setText(previousPapers);
        tvUserId.setText(oneSignalPlayerId);
        clickListener();

    }

    private void clickListener() {
        llHome.setOnClickListener(this);
        llSaved.setOnClickListener(this);
        llPapers.setOnClickListener(this);
        llLiveClass.setOnClickListener(this);
        llQna.setOnClickListener(this);
        tvCCC.setOnClickListener(this);
        tvPreviousPapers.setOnClickListener(this);
        rlGk.setOnClickListener(this);
        rlPlay.setOnClickListener(this);

        setJobCategories();

        String redirectionKey = getIntent().getStringExtra(Constant.REDIRECTION_KEY);
        if (!TextUtils.isEmpty(redirectionKey)) {
            if (redirectionKey.equalsIgnoreCase("GK")) {
                onClick(rlGk);
            } else if (redirectionKey.equalsIgnoreCase("News")) {
                onClick(llPapers);
            } else if (redirectionKey.equalsIgnoreCase("Home")) {
                setJobCategories();
            } else if (redirectionKey.equalsIgnoreCase("Play")) {
                onClick(rlPlay);
            }
        }
    }

    private void setJobCategories() {
        selectedTab = SelectedTab.job.name();
        viewPager = findViewById(R.id.viewpager);
        tabsJob = findViewById(R.id.tabsJob);
        tabsQna = findViewById(R.id.tabsQna);

        rlAdditionalTab.setVisibility(View.VISIBLE);
        tabsJob.setVisibility(View.VISIBLE);
        tabsQna.setVisibility(View.GONE);
        //tabLayout.setBackground();

        adapter = new TabAdapter(getSupportFragmentManager(), this);
        adapter.addFragment(new HomeFragment(), categoriesList[0], categoriesSelectedList[0]);
        adapter.addFragment(new OnlineFormFragment(), categoriesList[1], categoriesSelectedList[1]);
        adapter.addFragment(new ResultFragment(), categoriesList[2], categoriesSelectedList[2]);
        adapter.addFragment(new AdmitCardFragment(), categoriesList[3], categoriesSelectedList[3]);
        adapter.addFragment(new BtechJobsFragment(), categoriesList[4], categoriesSelectedList[4]);
        adapter.addFragment(new AdmissionFragment(), categoriesList[5], categoriesSelectedList[5]);
        adapter.addFragment(new OfflineFormFragment(), categoriesList[6], categoriesSelectedList[6]);
        adapter.addFragment(new SyllabusFragment(), categoriesList[7], categoriesSelectedList[7]);
        adapter.addFragment(new DiplomaFragment(), categoriesList[8], categoriesSelectedList[8]);
        adapter.addFragment(new AnswerKeyFragment(), categoriesList[9], categoriesSelectedList[9]);

        //adapter.addFragment(new ListFragment(), categoriesList[10], categoriesSelectedList[10]);


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        tabsJob.setupWithViewPager(viewPager);

        highLightCurrentTabForJob(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (selectedTab.equalsIgnoreCase(SelectedTab.job.name())) {
                    highLightCurrentTabForJob(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void highLightCurrentTabForJob(int position) {
        for (int i = 0; i < tabsJob.getTabCount(); i++) {
            TabLayout.Tab tab = tabsJob.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }

        TabLayout.Tab tab = tabsJob.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }

    private void setQNACategories() {
        selectedTab = SelectedTab.qna.name();
        viewPager = findViewById(R.id.viewpager);
        tabsJob = findViewById(R.id.tabsJob);
        tabsQna = findViewById(R.id.tabsQna);

        viewPager = findViewById(R.id.viewpager);
        rlAdditionalTab.setVisibility(View.GONE);
        tabsJob.setVisibility(View.GONE);
        tabsQna.setVisibility(View.GONE);

        qnaAdapter = new TabQnaAdapter(getSupportFragmentManager(), this);
        qnaAdapter.addFragment(new QNAListFragment(), "Ask Question");
        //qnaAdapter.addFragment(new MyQuestionFragment(), "My Questions");
        //qnaAdapter.addFragment(new MyAnswerFragment(), "My Answers");

        viewPager.setAdapter(qnaAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabsQna.setupWithViewPager(viewPager);

        highLightCurrentTabForQNA(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (selectedTab.equalsIgnoreCase(SelectedTab.qna.name())) {
                    highLightCurrentTabForQNA(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void highLightCurrentTabForQNA(int position) {
        for (int i = 0; i < tabsQna.getTabCount(); i++) {
            TabLayout.Tab tab = tabsQna.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(qnaAdapter.getTabView(i));
        }

        TabLayout.Tab tab = tabsQna.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(qnaAdapter.getSelectedTabView(position));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.llHome:
                cccPre.setVisibility(View.VISIBLE);
                setJobCategories();
                //init();
                //intent = new Intent(MainActivity.this, SavedPostsActivity.class);
                //startActivity(intent);
                break;

            case R.id.llSaved:
                intent = new Intent(MainActivity.this, SaveNewActivity.class);
                startActivity(intent);
                break;
            case R.id.llPapers:

            case R.id.tvPreviousPapers:
                //HelperClass.openUrlInChromeCustomTab(MainActivity.this, "https://papers.sarkariexam.com/");
                intent = new Intent(MainActivity.this, QuestionCardActivity.class);
                startActivity(intent);
                break;
            case R.id.tvCCC:
                HelperClass.openUrlInChromeCustomTab(MainActivity.this, "https://www.sarkariexam.com/ccc-admit-card/263859");
                break;
            case R.id.llLiveClass:
                try {
                    statusForYouTube = HomeFragment.youtubeData.get(0).getStatus();
                    postUrlForYouTube = HomeFragment.youtubeData.get(0).getPost_url();
                    messageForYouTube = HomeFragment.youtubeData.get(0).getMassage();
                    if (statusForYouTube.equalsIgnoreCase("1")) {
                        HelperClass.openUrlInChromeCustomTab(MainActivity.this, postUrlForYouTube);
                    } else {
                        openDialog(messageForYouTube);
                    }
                } catch (NullPointerException e) {

                } catch (Exception e) {

                }
                break;
            case R.id.llQna:
                cccPre.setVisibility(View.GONE);
                if (TextUtils.isEmpty(PreferenceHelper.getToken(MainActivity.this))) {

                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 0);

                } else {
                   /* intent = new Intent(MainActivity.this, QnaTabFragment.class);
                    startActivity(intent);*/
                    //setQNACategories();
                    intent = new Intent(MainActivity.this, QuestionCardActivity.class);
                    startActivity(intent);

                }
//
                break;

            case R.id.rl_gk:
                intent = new Intent(MainActivity.this, NewQuestionActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_play:
                Intent intents = new Intent(MainActivity.this, VideoActivity.class);
                startActivity(intents);
                break;
        }
    }

    private void setVersionText() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {

        }
        //mVersionNumber.setText(packageInfo.versionName);

        Log.e("TAG","VERSION==>"+packageInfo.versionName);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            ifAppIsLogedIn();
            //setQNACategories();
            Intent intent = new Intent(MainActivity.this, QuestionCardActivity.class);
            startActivity(intent);
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            ifAppIsLogedIn();
            //setQNACategories();
            Intent intent = new Intent(MainActivity.this, SaveNewActivity.class);
            startActivity(intent);
        }


    }

    private void openDialog(final String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_catalog_description);

        final TextView tvDetail = dialog.findViewById(R.id.tvDetail);
        tvDetail.setText(Html.fromHtml(message));
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ImageView tvOk = dialog.findViewById(R.id.tvOk);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(message) + " - https://play.google.com/store/apps/details?id=" + getPackageName());
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        ifAppIsLogedIn();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        ifAppIsLogedIn();
        super.onRestart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    private void logout() {

        PreferenceHelper.logout(MainActivity.this);
        PreferenceHelper.WriteSharePrefrence(context, Constant.USER_ID, "");
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

//        HelperClass.showProgressDialog(MainActivity.this, "");
//        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
//        Call<ResultEntity> call = apiInterface.logout(PreferenceHelper.getToken(MainActivity.this));
//
//
//        call.enqueue(new Callback<ResultEntity>() {
//            @Override
//            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
//                //HelperClass.displayRequestAndResponse(response, null);
//
//                HelperClass.dismissProgressDialog();
//                if (response.code() == 200) {
//                    PreferenceHelper.logout(MainActivity.this);
//                    PreferenceHelper.WriteSharePrefrence(context,Constant.USER_ID,"");
//                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResultEntity> call, Throwable t) {
//                HelperClass.dismissProgressDialog();
//            }
//        });
    }

    private void changePasswordDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_change_password);

        TextView tvSubmit = dialog.findViewById(R.id.tvSubmit);
        final EditText etOldPassword = dialog.findViewById(R.id.etOldPassword);
        final EditText etPassword = dialog.findViewById(R.id.etNewPassword);
        final EditText etCPassword = dialog.findViewById(R.id.etCPassword);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldPassword = etOldPassword.getText().toString();
                final String password = etPassword.getText().toString();
                final String cPassword = etCPassword.getText().toString();
                if (TextUtils.isEmpty(oldPassword)) {
                    Toast.makeText(MainActivity.this, "Enter Old Password", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Enter Old Password", Toast.LENGTH_LONG).show();
                } else if (!password.equals(cPassword)) {
                    Toast.makeText(MainActivity.this, "Enter Valid Password", Toast.LENGTH_LONG).show();
                } else {
                    HelperClass.showProgressDialog(MainActivity.this, "");
                    ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
                    Call<ResultEntity> call = apiInterface.changePassword(oldPassword, password, cPassword, PreferenceHelper.getToken(MainActivity.this));
                    call.enqueue(new Callback<ResultEntity>() {
                        @Override
                        public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                            //HelperClass.displayRequestAndResponse(response, null);
                            HelperClass.dismissProgressDialog();
                            if (response.code() == 200) {
                                Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultEntity> call, Throwable t) {
                            HelperClass.dismissProgressDialog();
                        }
                    });

                }
            }
        });
        dialog.show();
    }

    private enum SelectedTab {
        job,
        qna
    }

    private void getCorana() {
//        HelperClass.showProgressDialog(MainActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<Corona> call = apiInterface.getcorona(PreferenceHelper.ReadSharePrefrence(MainActivity.this, Constant.USER_ID));
        call.enqueue(new Callback<Corona>() {
            @Override
            public void onResponse(Call<Corona> call, Response<Corona> response) {
                //HelperClass.displayRequestAndResponse(response, null);
//                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    Corona resultEntity = response.body();

                    if (resultEntity.karona.flag.equalsIgnoreCase("1")) {

                        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

//                  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.dialog_commingsoon);

                        ImageView ivClose = dialog.findViewById(R.id.ivBackButton);
                        LinearLayout llState = dialog.findViewById(R.id.ll_state);
                        TextView tvTotalconfirm = dialog.findViewById(R.id.tv_totalconfirm);
                        TextView tvTotalactive = dialog.findViewById(R.id.tv_totalactive);
                        TextView tvTotalrecover = dialog.findViewById(R.id.tv_totalrecover);
                        TextView tvTotaldeath = dialog.findViewById(R.id.tv_totaldeath);
                        ImageView ivWatsapp = dialog.findViewById(R.id.iv_watsapp);
                        RelativeLayout rlWatsappshare = dialog.findViewById(R.id.rl_watsappshare);

                        TextView tvStateconfirm = dialog.findViewById(R.id.tv_stateconfirm);
                        TextView tvStateactive = dialog.findViewById(R.id.tv_stateactive);
                        TextView tvStaterecover = dialog.findViewById(R.id.tv_staterecover);
                        TextView tvStatedeath = dialog.findViewById(R.id.tv_statedeath);


                        tvTotalconfirm.setText(resultEntity.karona.totalconfirmed);
                        tvTotalactive.setText(resultEntity.karona.totalactive);
                        tvTotalrecover.setText(resultEntity.karona.totalrecovered);
                        tvTotaldeath.setText(resultEntity.karona.totaldeaths);

                        tvStateconfirm.setText(resultEntity.data.get(0).confirmed);
                        tvStateactive.setText(resultEntity.data.get(0).active);
                        tvStaterecover.setText(resultEntity.data.get(0).recovered);
                        tvStatedeath.setText(resultEntity.data.get(0).deaths);



                        rlWatsappshare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                whatsappIntent.setType("text/plain");
                                whatsappIntent.setPackage("com.whatsapp");
                                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Corona live अप्डेट्स देखें इस ऐप में. Download करें app and get corona live dashboard" + "\n" + "Current Confirm cases : " + resultEntity.karona.totalconfirmed +
                                        "\n" + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                                try {
                                    startActivity(whatsappIntent);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                        for (int i = 0; i < resultEntity.data.size(); i++) {
                            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            addView = layoutInflater.inflate(R.layout.list_item_corona, null);
                            TextView tvShortname = addView.findViewById(R.id.tv_shortname);
                            TextView tvStatename = addView.findViewById(R.id.tv_statename);
                            tvShortname.setText(resultEntity.data.get(i).state);
                            tvStatename.setText(resultEntity.data.get(i).slogan);
                            llState.addView(addView);

                            tvShortname.setId(i);
                            buttons.add(tvShortname);
                            buttons.get(0).setBackground(getResources().getDrawable(R.drawable.drawable_select_state));




                            tvShortname.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(View view) {


                                    for (int z = 0; z < buttons.size(); z++) {
                                        buttons.get(z).setBackground(getResources().getDrawable(R.drawable.drawable_normal_state));
                                    }
                                    for (int i = 0; i < resultEntity.data.size(); i++) {
                                        if (view.getId() == buttons.get(i).getId()) {
                                            view.setBackground(getResources().getDrawable(R.drawable.drawable_select_state));
                                            //selectedColor = buttons.get(i).getText().toString();

                                            tvStateconfirm.setText(resultEntity.data.get(i).confirmed);
                                            tvStateactive.setText(resultEntity.data.get(i).active);
                                            tvStaterecover.setText(resultEntity.data.get(i).recovered);
                                            tvStatedeath.setText(resultEntity.data.get(i).deaths);


                                            //Log.e(TAG, "SELECTED COLOR-" + selectedColor);
                                        }
                                    }

                                }
                            });

                        }

                        if (countDownTimer == null) {
                            countDownTimer = new CountDownTimer(Config.OTP_TIMEOUT_IN_MILLIS, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    //toggleProgressBarHorizontal(true, millisUntilFinished);
                                }

                                @Override
                                public void onFinish() {
                                    countDownTimer = null;
                                    dialog.dismiss();
                                }
                            };
                            countDownTimer.start();

                        }

                        try {
                            Glide.with(MainActivity.this)
                                    .load(R.drawable.ic_watsappgif)
                                    .asGif()
                                    .into(ivWatsapp);
                        }
                        catch (Exception e)
                        {

                        }


                        dialog.show();

                        ivClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                            }
                        });


                    }
                }

                else {

                }




            }

            @Override
            public void onFailure(Call<Corona> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Please Try again!", Toast.LENGTH_SHORT).show();
//                HelperClass.dismissProgressDialog();
            }
        });

    }



}
