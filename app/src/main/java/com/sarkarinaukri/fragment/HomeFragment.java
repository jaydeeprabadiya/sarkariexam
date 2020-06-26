package com.sarkarinaukri.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForJob;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.SarkariExam;
import com.sarkarinaukri.adapter.DataListHomeAdapter;
import com.sarkarinaukri.helperClass.ConnectivityReceiver;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.JobData;
import com.sarkarinaukri.model.ResultEntity;
import com.sarkarinaukri.newsModule.CromcastActivity;
import com.sarkarinaukri.util.ActivitySplitAnimationUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private View view;
    private FloatingActionButton fabRefresh;
    private RecyclerView recyclerViewNewUpdate;
    private RecyclerView recyclerViewResult;
    private RecyclerView recyclerViewAdmitCard;
    private RecyclerView recyclerViewOnlineForm;
    private TextView tvViewNewUpdate;
    private TextView tvViewResult;
    private TextView tvViewAdmitCard;
    private TextView tvViewOnlineForm;

    private TextView tvFirstJob;
    private TextView tvSecondJob;
    private TextView tvThirdJob;
    private TextView tvFourthJob;
    private TextView tvFifthJob;
    private TextView tvSixthJob;
    private TextView tvSeventhJob;
    private TextView tvEightJob;

    private DataListHomeAdapter dataListHomeNewAdapter;
    private DataListHomeAdapter dataListHomeResultAdapter;
    private DataListHomeAdapter dataListHomeAdmitCardAdapter;
    private DataListHomeAdapter dataListHomeOnlineFormAdapter;

    public static List<JobData> resultsList = new ArrayList<>();
    public static List<JobData> admitCardList = new ArrayList<>();
    public static List<JobData> onlineFormList = new ArrayList<>();
    public static List<JobData> boxDataList = new ArrayList<>();
    public static List<JobData> youtubeData = new ArrayList<>();
    public static List<JobData> hotJobList = new ArrayList<>();

    private CallBackListener callBackListener;
    private TabLayout tabLayout;
    private String currentLanguage;
    private ProgressBar progressBar;
    private NestedScrollView scroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        init();
        return view;
    }

    private void init() {
        SarkariExam.firebaseAnalytics.setCurrentScreen(getActivity(), "Home Fragment", this.getClass().getSimpleName());

        tabLayout = getActivity().findViewById(R.id.tabsJob);

        currentLanguage = PreferenceHelper.getLanguageType(getActivity());


        fabRefresh = view.findViewById(R.id.fabRefresh);
        recyclerViewNewUpdate = view.findViewById(R.id.recyclerViewNewUpdate);
        recyclerViewResult = view.findViewById(R.id.recyclerViewResult);
        recyclerViewAdmitCard = view.findViewById(R.id.recyclerViewAdmitCard);
        recyclerViewOnlineForm = view.findViewById(R.id.recyclerViewOnlineForm);

        tvViewNewUpdate = view.findViewById(R.id.tvViewNewUpdate);
        tvViewResult = view.findViewById(R.id.tvViewResult);
        tvViewAdmitCard = view.findViewById(R.id.tvViewAdmitCard);
        tvViewOnlineForm = view.findViewById(R.id.tvViewOnlineForm);

        tvFirstJob = view.findViewById(R.id.tvFirstJob);
        tvSecondJob = view.findViewById(R.id.tvSecondJob);
        tvThirdJob = view.findViewById(R.id.tvThirdJob);
        tvFourthJob = view.findViewById(R.id.tvFourthJob);
        tvFifthJob = view.findViewById(R.id.tvFifthJob);
        tvSixthJob = view.findViewById(R.id.tvSixthJob);
        tvSeventhJob = view.findViewById(R.id.tvSeventhJob);
        tvEightJob = view.findViewById(R.id.tvEightJob);
        progressBar = view.findViewById(R.id.progressBar);
        scroll = view.findViewById(R.id.scrollView);

        clickListener();

        getDataForHome();
    }

    private void clickListener() {
        fabRefresh.setOnClickListener(this);
        tvFirstJob.setOnClickListener(this);
        tvSecondJob.setOnClickListener(this);
        tvThirdJob.setOnClickListener(this);
        tvFourthJob.setOnClickListener(this);
        tvFifthJob.setOnClickListener(this);
        tvSixthJob.setOnClickListener(this);
        tvSeventhJob.setOnClickListener(this);
        tvEightJob.setOnClickListener(this);
        tvViewNewUpdate.setOnClickListener(this);
        tvViewResult.setOnClickListener(this);
        tvViewAdmitCard.setOnClickListener(this);
        tvViewOnlineForm.setOnClickListener(this);
    }

    private void getDataForHome() {
        progressBar.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.GONE);
        ApiInterface apiInterface = ApiClientForJob.getClient().create(ApiInterface.class);
        Call<ResultEntity> call;
        if (currentLanguage.equalsIgnoreCase(Constant.ENGLISH)) {
            call = apiInterface.getJobsForHome("e");
        } else {
            call = apiInterface.getJobsForHome("e");
        }
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                progressBar.setVisibility(View.GONE);
                scroll.setVisibility(View.VISIBLE);
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    boxDataList = resultEntity.getHome().getBox_value();
                    admitCardList = resultEntity.getHome().getAdmit_card();
                    resultsList = resultEntity.getHome().getExam_result();
                    onlineFormList = resultEntity.getHome().getTop_online_form();
                    youtubeData = resultEntity.getHome().getYoutube();
                    hotJobList = resultEntity.getHome().getHot_job();


                    setDataForBox();
                    setDataForHomeNewUpdate();
                    setDataForHomeExamResult();
                    setDataForHomeAdmitCard();
                    setDataForHomeOnlineForm();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    private void setDataForBox() {
        tvFirstJob.setText(Html.fromHtml(boxDataList.get(0).getPost_title()));
        tvSecondJob.setText(Html.fromHtml(boxDataList.get(1).getPost_title()));
        tvThirdJob.setText(Html.fromHtml(boxDataList.get(2).getPost_title()));
        tvFourthJob.setText(Html.fromHtml(boxDataList.get(3).getPost_title()));
        tvFifthJob.setText(Html.fromHtml(boxDataList.get(4).getPost_title()));
        tvSixthJob.setText(Html.fromHtml(boxDataList.get(5).getPost_title()));
        tvSeventhJob.setText(Html.fromHtml(boxDataList.get(6).getPost_title()));
        tvEightJob.setText(Html.fromHtml(boxDataList.get(7).getPost_title()));
    }

    private void setDataForHomeNewUpdate() {
        recyclerViewNewUpdate.setVisibility(View.VISIBLE);
        recyclerViewNewUpdate.setHasFixedSize(true);
        recyclerViewNewUpdate.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewNewUpdate.setLayoutManager(layoutManager);
        dataListHomeNewAdapter = new DataListHomeAdapter(getActivity(), hotJobList, new DataListHomeAdapter.ItemListener() {
            @Override
            public void viewJob(int position) {
                //HelperClass.openOrSaveUrlInChromeCustomTab(getActivity(), hotJobList.get(position).getPost_title(), hotJobList.get(position).getPost_url());
                ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, hotJobList.get(position).getPost_url());
                PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, hotJobList.get(position).getPost_title());
                if (position==0)
                {
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.ADS, "true");
                }
                else
                {
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.ADS, "false");
                }
            }
        });
        recyclerViewNewUpdate.setAdapter(dataListHomeNewAdapter);
    }

    private void setDataForHomeExamResult() {
        recyclerViewResult.setVisibility(View.VISIBLE);
        recyclerViewResult.setHasFixedSize(true);
        recyclerViewResult.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewResult.setLayoutManager(layoutManager);
        dataListHomeResultAdapter = new DataListHomeAdapter(getActivity(), resultsList, new DataListHomeAdapter.ItemListener() {
            @Override
            public void viewJob(int position) {
                //HelperClass.openOrSaveUrlInChromeCustomTab(getActivity(), resultsList.get(position).getPost_title(), resultsList.get(position).getPost_url());
                ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, hotJobList.get(position).getPost_url());
                PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, hotJobList.get(position).getPost_title());
                if (position==0)
                {
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.ADS, "true");
                }
                else
                {
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.ADS, "false");
                }
            }
        });
        recyclerViewResult.setAdapter(dataListHomeResultAdapter);
    }

    private void setDataForHomeAdmitCard() {
        recyclerViewAdmitCard.setVisibility(View.VISIBLE);
        recyclerViewAdmitCard.setHasFixedSize(true);
        recyclerViewAdmitCard.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAdmitCard.setLayoutManager(layoutManager);
        dataListHomeAdmitCardAdapter = new DataListHomeAdapter(getActivity(), admitCardList, new DataListHomeAdapter.ItemListener() {
            @Override
            public void viewJob(int position) {
                //HelperClass.openOrSaveUrlInChromeCustomTab(getActivity(), admitCardList.get(position).getPost_title(), admitCardList.get(position).getPost_url());
                ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, hotJobList.get(position).getPost_url());
                PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, hotJobList.get(position).getPost_title());
                if (position==0)
                {
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.ADS, "true");
                }
                else
                {
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.ADS, "false");
                }
            }
        });
        recyclerViewAdmitCard.setAdapter(dataListHomeAdmitCardAdapter);
    }

    private void setDataForHomeOnlineForm() {
        recyclerViewOnlineForm.setVisibility(View.VISIBLE);
        recyclerViewOnlineForm.setHasFixedSize(true);
        recyclerViewOnlineForm.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewOnlineForm.setLayoutManager(layoutManager);
        dataListHomeOnlineFormAdapter = new DataListHomeAdapter(getActivity(), onlineFormList, new DataListHomeAdapter.ItemListener() {
            @Override
            public void viewJob(int position) {
                //HelperClass.openOrSaveUrlInChromeCustomTab(getActivity(), onlineFormList.get(position).getPost_title(), onlineFormList.get(position).getPost_url());
                ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, hotJobList.get(position).getPost_url());
                PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, hotJobList.get(position).getPost_title());
                if (position==0)
                {
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.ADS, "true");
                }
                else
                {
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.ADS, "false");
                }
            }
        });
        recyclerViewOnlineForm.setAdapter(dataListHomeOnlineFormAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabRefresh:
                getDataForHome();
                break;
            case R.id.tvFirstJob:
                if (boxDataList != null && boxDataList.size() > 0) {
                    //HelperClass.openUrlInChromeCustomTab(getActivity(), boxDataList.get(0).getPost_url());
                    ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, boxDataList.get(0).getPost_url());
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, "sarkariexam.com");


                }
                break;
            case R.id.tvSecondJob:
                if (boxDataList != null && boxDataList.size() > 0) {
                    //HelperClass.openUrlInChromeCustomTab(getActivity(), boxDataList.get(1).getPost_url());
                    ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, boxDataList.get(1).getPost_url());
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, "sarkariexam.com");
                }
                break;
            case R.id.tvThirdJob:
                if (boxDataList != null && boxDataList.size() > 0) {
                    //HelperClass.openUrlInChromeCustomTab(getActivity(), boxDataList.get(2).getPost_url());
                    ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, boxDataList.get(2).getPost_url());
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, "sarkariexam.com");
                }
                break;
            case R.id.tvFourthJob:
                if (boxDataList != null && boxDataList.size() > 0) {
                    //HelperClass.openUrlInChromeCustomTab(getActivity(), boxDataList.get(3).getPost_url());
                    ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, boxDataList.get(3).getPost_url());
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, "sarkariexam.com");
                }
                break;
            case R.id.tvFifthJob:
                if (boxDataList != null && boxDataList.size() > 0) {
                    //HelperClass.openUrlInChromeCustomTab(getActivity(), boxDataList.get(4).getPost_url());
                    ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, boxDataList.get(4).getPost_url());
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, "sarkariexam.com");
                }
                break;
            case R.id.tvSixthJob:
                if (boxDataList != null && boxDataList.size() > 0) {
                    //HelperClass.openUrlInChromeCustomTab(getActivity(), boxDataList.get(5).getPost_url());
                    ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, boxDataList.get(5).getPost_url());
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, "sarkariexam.com");
                }
                break;
            case R.id.tvSeventhJob:
                if (boxDataList != null && boxDataList.size() > 0) {
                    //HelperClass.openUrlInChromeCustomTab(getActivity(), boxDataList.get(6).getPost_url());
                    ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, boxDataList.get(6).getPost_url());
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, "sarkariexam.com");
                }
                break;
            case R.id.tvEightJob:
                if (boxDataList != null && boxDataList.size() > 0) {
                    //HelperClass.openUrlInChromeCustomTab(getActivity(), boxDataList.get(7).getPost_url());
                    ActivitySplitAnimationUtil.startActivity(getActivity(), new Intent(getActivity(), CromcastActivity.class));
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.URL, boxDataList.get(7).getPost_url());
                    PreferenceHelper.WriteSharePrefrence(getActivity(), Constant.TITLE, "sarkariexam.com");
                }
                break;
            case R.id.tvViewNewUpdate:
                HelperClass.openUrlInChromeCustomTab(getActivity(), "https://www.sarkariexam.com/category/hot-job");
                break;
            case R.id.tvViewOnlineForm:
                tabLayout.getTabAt(1).select();
                break;
            case R.id.tvViewResult:
                tabLayout.getTabAt(2).select();
                break;
            case R.id.tvViewAdmitCard:
                tabLayout.getTabAt(3).select();
                break;

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof CallBackListener)
            callBackListener = (CallBackListener) getActivity();
    }

    public interface CallBackListener {
        void onCallBack(int position);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        //Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        SarkariExam.getInstance().setConnectivityListener(this);
    }
}
