package com.sarkarinaukri.qnaModule.fragment;

/**
 * Created by AppsMediaz Technologies on 04/08/2017.
 */

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.helperClass.AdHandler;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.QuestionData;
import com.sarkarinaukri.model.ResultEntity;
import com.sarkarinaukri.qnaModule.AnswerActivity;
import com.sarkarinaukri.qnaModule.PostMCQQuestionActivity;
import com.sarkarinaukri.qnaModule.adapter.QnaListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class QNAListFragment extends Fragment {
    private View view;

    private CoordinatorLayout coordinatorLayout;
    private LinearLayout llProgress;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout loadMoreProgress;
    private TextView tvAsk;
    public static List<QuestionData> questionList = new ArrayList<>();
    private int paging = 0;
    private QnaListAdapter qnaListAdapter;
    private int shareQuestionPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_qna_list, container, false);

        init();
        return view;
    }

    private void init() {
        setRetainInstance(true);
        AdHandler.init(getActivity());
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        llProgress = view.findViewById(R.id.llProgress);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        loadMoreProgress = view.findViewById(R.id.loadMoreProgress);
        tvAsk = view.findViewById(R.id.tvAsk);
        tvAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostMCQQuestionActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        if (questionList == null || questionList.size() <= 0) {
            getQuestionAnswer();
        } else {
            setQNAListInAdapter();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onDataRefresh();
            }
        });
    }

    private void onDataRefresh() {
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.getQuestion("", "0", questionList.get(0).getQuestionId(), "", PreferenceHelper.getToken(getActivity()));
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
        Call<ResultEntity> call = apiInterface.getQuestion("", "0", "", "", PreferenceHelper.getToken(getActivity()));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
                        questionList = resultEntity.getQuestionData();
                        setQNAListInAdapter();
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

            }
        });
    }

    private void setQNAListInAdapter() {
        llProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        qnaListAdapter = new QnaListAdapter(getActivity(), questionList, new QnaListAdapter.ItemListener() {
            @Override
            public void shareQuestion(int position) {
                shareQuestionPosition = position;
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "SarkariExam Shared Question");
                share.putExtra(Intent.EXTRA_TEXT, questionList.get(position).getQuestion() + "\nhttps://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                startActivityForResult(Intent.createChooser(share, "Share link!"), 2);
            }

            @Override
            public void likeQuestion(int position) {
                likeDislikeQuestion(questionList.get(position).getQuestionId(), position);
            }

            @Override
            public void viewAnswer(int position) {
                Intent intent = new Intent(getActivity(), AnswerActivity.class);
                intent.putExtra(Constant.QUESTION, (Serializable) questionList.get(position));
                intent.putExtra(Constant.POSITION, position);
                startActivityForResult(intent, 1);
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
                                deleteQuestion(questionList.get(position).getQuestionId(), position);
                                dialog.dismiss();
                            }
                        });

                final AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onMCQAnswer(int position, int optionPosition, QuestionData questionData) {
                questionList.get(position).setGivenOption(questionData.getGivenOption());
                //questionList.set(position, questionData);
                //postMCQOptionToServer(position, questionList.get(position).getQuestionId(), questionList.get(position).getOptions().get(optionPosition).getId());
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
        Call<ResultEntity> call = apiInterface.getQuestion("", "" + paging, "", "", PreferenceHelper.getToken(getActivity()));
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

    private void likeDislikeQuestion(final String questionId, final int position) {
        //HelperClass.showProgressDialog(getActivity(), "");
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.questionLike(questionId, PreferenceHelper.getToken(getActivity()));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    questionList.get(position).setLike(resultEntity.isLike());
                    if (resultEntity.isLike()) {
                        int likeCount = questionList.get(position).getLikeCount();
                        likeCount = likeCount + 1;
                        questionList.get(position).setLikeCount(likeCount);
                    } else {
                        int likeCount = questionList.get(position).getLikeCount();
                        if (likeCount > 0) {
                            likeCount = likeCount - 1;
                            questionList.get(position).setLikeCount(likeCount);
                        }
                    }
                    qnaListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                //HelperClass.dismissProgressDialog();
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

    private void shareQuestion() {
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.shareQuestion(questionList.get(shareQuestionPosition).getQuestionId(), PreferenceHelper.getToken(getActivity()));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();

                    int shareCount = questionList.get(shareQuestionPosition).getShareCount();
                    shareCount = shareCount + 1;
                    questionList.get(shareQuestionPosition).setShareCount(shareCount);
                    qnaListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                //getQuestionAnswer();
            } else if (requestCode == 1) {
                //onDataRefresh();
                int position = data.getIntExtra(Constant.POSITION, 0);
                QuestionData questionData = (QuestionData) data.getSerializableExtra(Constant.QUESTION);
                questionList.set(position, questionData);
                qnaListAdapter.notifyItemChanged(position);
                //qnaListAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 2) {
            shareQuestion();
        }
    }

    private void postMCQOptionToServer(final int position, String questionId, String optionId) {
        //HelperClass.showProgressDialog(getActivity(), "");
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.postMCQOption(questionId, optionId, PreferenceHelper.getToken(getActivity()));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.toString().equalsIgnoreCase(resultEntity.getStatus())) {
                        //questionList.get(position).setGivenOption(userGivenOption);
                        //int addCount = Integer.parseInt(questionList.get(position).getOptions().get(optionPosition).getTotalAnswer()) + 1;
                        questionList.set(position, resultEntity.getQuestionData().get(0));
                        qnaListAdapter.notifyItemChanged(position);
                        //qnaListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                //HelperClass.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
