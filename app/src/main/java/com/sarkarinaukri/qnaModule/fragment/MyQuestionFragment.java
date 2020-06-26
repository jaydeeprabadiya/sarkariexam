package com.sarkarinaukri.qnaModule.fragment;


/**
 * Created by AppsMediaz Technologies on 04/08/2017.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.QuestionData;
import com.sarkarinaukri.model.ResultEntity;
import com.sarkarinaukri.qnaModule.AnswerActivity;
import com.sarkarinaukri.qnaModule.PostMCQQuestionActivity;
import com.sarkarinaukri.qnaModule.adapter.OptionAnswerListAdapter;
import com.sarkarinaukri.qnaModule.adapter.QnaListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyQuestionFragment extends Fragment {
    private View view;
    private Bundle bundle;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout llProgress;
    private RecyclerView recyclerView;
    private LinearLayout loadMoreProgress;
    private TextView tvEmptyState;
    private TextView tvAsk;
    private List<QuestionData> questionList = new ArrayList<>();
    private QnaListAdapter qnaListAdapter;
    private int paging = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_qna_list, container, false);

        init();
        return view;
    }

    private void init() {
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        llProgress = view.findViewById(R.id.llProgress);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        loadMoreProgress = view.findViewById(R.id.loadMoreProgress);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        tvAsk = view.findViewById(R.id.tvAsk);
        tvAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostMCQQuestionActivity.class);
                startActivityForResult(intent, 0);
                //openQuestionTypeDialog();
            }
        });

        getQuestionAnswer();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (questionList != null && questionList.size() > 0) {
                    onDataRefresh();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void onDataRefresh() {
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.getQuestion("me", "0", questionList.get(0).getQuestionId(), "", PreferenceHelper.getToken(getActivity()));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.displayRequestAndResponse(response, null);

                swipeRefreshLayout.setRefreshing(false);
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
                        if (resultEntity.getQuestionData() != null && resultEntity.getQuestionData().size() > 0) {
                            questionList.addAll(0, resultEntity.getQuestionData());
                            qnaListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onDataRefresh();
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getQuestionAnswer() {
        llProgress.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.getQuestion("me", "0", "", "", PreferenceHelper.getToken(getActivity()));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                llProgress.setVisibility(View.GONE);
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
                        if (resultEntity.getQuestionData() == null || resultEntity.getQuestionData().size() <= 0) {
                            tvEmptyState.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            questionList = resultEntity.getQuestionData();
                            setQNAListInAdapter(questionList);
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getQuestionAnswer();
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                llProgress.setVisibility(View.GONE);
            }
        });
    }

    private void setQNAListInAdapter(final List<QuestionData> qnaList) {
        llProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        qnaListAdapter = new QnaListAdapter(getActivity(), qnaList, new QnaListAdapter.ItemListener() {
            @Override
            public void shareQuestion(int position) {

            }

            @Override
            public void likeQuestion(int position) {

            }

            @Override
            public void viewAnswer(int position) {
                Intent intent = new Intent(getActivity(), AnswerActivity.class);
                intent.putExtra("QUESTION", (Serializable) qnaList.get(position));
                intent.putExtra("POSITION", position);
                startActivity(intent);
            }

            @Override
            public void onDelete(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete this question?")
                        .setCancelable(false)
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteQuestion(qnaList.get(position).getQuestionId(), position);
                                dialog.dismiss();
                            }
                        });

                final AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onMCQAnswer(int position, int optionPosition, QuestionData userGivenOption) {

            }
        });
        recyclerView.setAdapter(qnaListAdapter);
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
                        paging = paging + 1;
                        getLoadMoreQuestionAnswer();
                    }
                }
            }
        });
    }

    private void getLoadMoreQuestionAnswer() {
        loadMoreProgress.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.getQuestion("me", "" + paging, "", "", PreferenceHelper.getToken(getActivity()));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                loadMoreProgress.setVisibility(View.GONE);
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
                        if (resultEntity.getQuestionData() == null || resultEntity.getQuestionData().size() <= 0) {
                            HelperClass.showSnackBar(coordinatorLayout, "No more data found");
                        } else {
                            questionList.addAll(resultEntity.getQuestionData());
                            qnaListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getLoadMoreQuestionAnswer();
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                loadMoreProgress.setVisibility(View.GONE);
            }
        });
    }

    private void deleteQuestion(final String questionId, final int position) {
        HelperClass.showProgressDialog(getActivity(), "");
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.questionDelete(questionId, PreferenceHelper.getToken(getActivity()));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.displayRequestAndResponse(response, null);

                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
                        questionList.remove(position);
                        qnaListAdapter.notifyDataSetChanged();
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteQuestion(questionId, position);
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                HelperClass.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
