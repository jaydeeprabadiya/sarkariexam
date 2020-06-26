package com.sarkarinaukri.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForJob;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.adapter.DataListAdapter;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.model.JobData;
import com.sarkarinaukri.model.ResultEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerKeyFragment extends Fragment {
    private View view;
    private LinearLayout llProgress;
    private RecyclerView recyclerView;
    private List<JobData> jobList = new ArrayList<>();
    private DataListAdapter dataListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvViewAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        TextView label = view.findViewById(R.id.tvLabel);
        label.setText("Answer Key");

        init();
        return view;
    }

    private void init() {
        tvViewAll = view.findViewById(R.id.tvViewAll);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        llProgress = view.findViewById(R.id.llProgress);
        recyclerView = view.findViewById(R.id.recyclerView);

        if (jobList == null || jobList.size() <= 0) {
            getDataListFromFirebase();
        } else {
            setDataListInAdapter();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jobList.clear();
                getDataListFromFirebase();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.openUrlInChromeCustomTab(getActivity(), "https://www.sarkariexam.com/category/answer-keys");
            }
        });
    }

    private void getDataListFromFirebase() {
        llProgress.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClientForJob.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.getJobs("answer_keys");
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                llProgress.setVisibility(View.GONE);
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    jobList = resultEntity.getAnswer_keys();
                    setDataListInAdapter();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                llProgress.setVisibility(View.GONE);
            }
        });
    }

    private void setDataListInAdapter() {
        llProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        dataListAdapter = new DataListAdapter(getActivity(), jobList, new DataListAdapter.ItemListener() {
            @Override
            public void viewJob(int position) {
                HelperClass.openOrSaveUrlInChromeCustomTab(getActivity(), jobList.get(position).getPost_title(), jobList.get(position).getPost_url());
            }
        });
        recyclerView.setAdapter(dataListAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItem == layoutManager.getItemCount() - 1) {
                        tvViewAll.setVisibility(View.VISIBLE);
                    } else {
                        tvViewAll.setVisibility(View.GONE);
                    }
                } else {
                    tvViewAll.setVisibility(View.GONE);
                }
            }
        });
    }
}
