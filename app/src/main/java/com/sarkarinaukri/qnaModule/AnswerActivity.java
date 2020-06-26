package com.sarkarinaukri.qnaModule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sarkarinaukri.BuildConfig;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.AnswerData;
import com.sarkarinaukri.model.Option;
import com.sarkarinaukri.model.QuestionData;
import com.sarkarinaukri.model.ResultEntity;
import com.sarkarinaukri.qnaModule.adapter.AnswerListAdapter;
import com.sarkarinaukri.qnaModule.adapter.OptionAnswerListAdapter;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout llEmptyText;
    private LinearLayout llProgress;
    private NestedScrollView scrollView;
    private RecyclerView recyclerView;
    private TextView tvTitle;
    private ImageView ivImageView;
    private QuestionData questionData;
    private ImageView ivProfilePhoto;
    private TextView tvName;
    private TextView tvPostedDate;
    private TextView tvQuestionDetail;
    private ImageView ivQuestionImage;
    private ImageView ivLikeIcon;
    private TextView tvShareCount;
    private TextView tvLikeCount;
    private TextView tvCommentCount;
    private TextView etAnswer;
    private LinearLayout llSend;
    private LinearLayout llShare;
    private LinearLayout llLike;
    private List<AnswerData> answerList = new ArrayList<>();
    private int position;
    private boolean updatePreviousDataForFollow = false;
    private boolean updatePreviousDataForMCQ = false;
    private boolean followUnfollow = false;
    private RecyclerView recyclerViewOptions;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        init();
    }

    private void init() {
        questionData = (QuestionData) getIntent().getSerializableExtra(Constant.QUESTION);
        position = getIntent().getIntExtra(Constant.POSITION, 0);

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("9E6EE1D85F039ECB9E321B5F100CC57D").build();
        adView.loadAd(adRequest);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.title);
        tvTitle.setText("Answer");
        ivImageView = findViewById(R.id.ivImageView);

        llEmptyText = findViewById(R.id.llEmptyText);
        llProgress = findViewById(R.id.llProgress);
        recyclerView = findViewById(R.id.recyclerView);

        scrollView = findViewById(R.id.scrollView);
        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.setSmoothScrollingEnabled(false);

        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        tvName = findViewById(R.id.tvName);
        tvPostedDate = findViewById(R.id.tvPostedDate);
        tvQuestionDetail = findViewById(R.id.tvQuestionDetail);
        ivQuestionImage = findViewById(R.id.ivQuestionImage);
        ivLikeIcon = findViewById(R.id.ivLikeIcon);
        tvShareCount = findViewById(R.id.tvShareCount);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        tvCommentCount = findViewById(R.id.tvCommentCount);
        recyclerViewOptions = findViewById(R.id.recyclerViewOptions);


        etAnswer = findViewById(R.id.etAnswer);
        llSend = findViewById(R.id.llSend);
        llShare = findViewById(R.id.llShare);
        llLike = findViewById(R.id.llLike);

        llSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAnswerToServer();
            }
        });

        ivImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressedTask();
            }
        });

        ivQuestionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnswerActivity.this, ViewQuestionImageActivity.class);
                intent.putExtra(Constant.QUESTION_IMAGE, questionData.getQuestionImage());
                startActivity(intent);
            }
        });

        llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeDislikeQuestion();
            }
        });


        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_TEXT, questionData.getQuestion() + "\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
                startActivityForResult(Intent.createChooser(share, "Share link!"), 2);
            }
        });

        setQuestionInView();
    }


    private void setQuestionInView() {
        if (!TextUtils.isEmpty(questionData.getPhoto())) {
            String profilePic = questionData.getPhoto();
            String profileImageURL = BuildConfig.IMAGE_URL + profilePic;
            Picasso.get().load(profileImageURL).into(ivProfilePhoto);
        }
        if (TextUtils.isEmpty(questionData.getQuestionImage())) {
            ivQuestionImage.setVisibility(View.GONE);
        } else {
            ivQuestionImage.setVisibility(View.VISIBLE);
            String questionImage = questionData.getQuestionImage();
            String questionImageUrl = BuildConfig.IMAGE_URL + questionImage;
            Picasso.get().load(questionImageUrl).into(ivQuestionImage);
        }


        tvName.setText("SarkariExam");
        tvPostedDate.setText(HelperClass.parseDateToddMMyyyy(questionData.getCreatedDate()));
        tvQuestionDetail.setText(questionData.getQuestion());
        tvShareCount.setText("" + questionData.getShareCount());
        tvLikeCount.setText("" + questionData.getLikeCount());
        tvCommentCount.setText(questionData.getAnswerCount());

        if (questionData.isLike()) {
            ivLikeIcon.setImageResource(R.drawable.liked_icon);
        } else {
            ivLikeIcon.setImageResource(R.drawable.like_icon);
        }

        setOptionsListInAdapter(recyclerViewOptions, questionData.getRightOption(), questionData.getGivenOption(), questionData.getOptions());
        getAnswerList();
    }

    private void setOptionsListInAdapter(RecyclerView recyclerViewOptions, String rightOption, String givenOption, List<Option> options) {
        recyclerViewOptions.setVisibility(View.VISIBLE);
        recyclerViewOptions.setNestedScrollingEnabled(false);
        recyclerViewOptions.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewOptions.setLayoutManager(layoutManager);
        OptionAnswerListAdapter optionAnswerListAdapter = new OptionAnswerListAdapter(this, rightOption, givenOption, options, new OptionAnswerListAdapter.ProgressListener() {
            @Override
            public void onProgressClickListener(int position, String userGivenOption) {
                postMCQOptionToServer(questionData.getQuestionId(), questionData.getOptions().get(position).getId());

            }
        });
        recyclerViewOptions.setAdapter(optionAnswerListAdapter);
    }

    private void getAnswerList() {
        llProgress.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.getAnswerOfQuestion(questionData.getQuestionId(), PreferenceHelper.getToken(this));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.displayRequestAndResponse(response, null);

                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
                        if (resultEntity.getAnswerData() == null || resultEntity.getAnswerData().size() <= 0) {
                            llProgress.setVisibility(View.GONE);
                            llEmptyText.setVisibility(View.VISIBLE);
                        } else {
                            answerList = resultEntity.getAnswerData();
                            Collections.reverse(answerList);
                            setAnswerInList(answerList);
                        }
                    } else {
                        llProgress.setVisibility(View.GONE);
                        llEmptyText.setVisibility(View.VISIBLE);
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getAnswerList();
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

    private void setAnswerInList(final List<AnswerData> answerData) {
        llProgress.setVisibility(View.GONE);
        llEmptyText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AnswerActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        AnswerListAdapter answerListAdapter = new AnswerListAdapter(AnswerActivity.this, answerData);
        recyclerView.setAdapter(answerListAdapter);
    }

    private void postAnswerToServer() {
        final String answer = etAnswer.getText().toString();
        if (TextUtils.isEmpty(answer)) {
            HelperClass.showSnackBar(coordinatorLayout, "You can't submit blank answer");
            return;
        }

        HelperClass.showProgressDialog(this, "");
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.postAnswer(questionData.getQuestionId(), answer, PreferenceHelper.getToken(this));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.displayRequestAndResponse(response, null);

                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.toString().equalsIgnoreCase(resultEntity.getStatus())) {
                        answerList.addAll(resultEntity.getAnswerData());
                        //Collections.reverse(answerList);
                        setAnswerInList(answerList);
                        etAnswer.setText("");


                        updatePreviousDataForMCQ = true;

                        int commentCount = Integer.parseInt(questionData.getAnswerCount());
                        commentCount = commentCount + 1;
                        questionData.setAnswerCount("" + commentCount);
                        tvCommentCount.setText("" + questionData.getAnswerCount());
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                postAnswerToServer();
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
    public void onBackPressed() {
        backPressedTask();
    }

    private void backPressedTask() {
        if (updatePreviousDataForFollow) {
            Intent intent = new Intent();
            intent.putExtra(Constant.POSITION, position);
            intent.putExtra(Constant.FOLLOW_STATUS, followUnfollow);
            intent.putExtra(Constant.MCQ_UPDATE, "FALSE");
            setResult(RESULT_OK, intent);
            finish();
        } else if (updatePreviousDataForMCQ) {
            Intent intent = new Intent();
            intent.putExtra(Constant.POSITION, position);
            intent.putExtra(Constant.QUESTION, (Serializable) questionData);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            finish();
        }
    }

    private void postMCQOptionToServer(String questionId, String optionId) {
        //HelperClass.showProgressDialog(this, "");
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.postMCQOption(questionId, optionId, PreferenceHelper.getToken(this));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.toString().equalsIgnoreCase(resultEntity.getStatus())) {
                        updatePreviousDataForMCQ = true;
                        questionData = resultEntity.getQuestionData().get(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                HelperClass.dismissProgressDialog();
            }
        });
    }

    private void likeDislikeQuestion() {
        //HelperClass.showProgressDialog(this, "");
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.questionLike(questionData.getQuestionId(), PreferenceHelper.getToken(this));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    updatePreviousDataForMCQ = true;
                    ResultEntity resultEntity = response.body();
                    questionData.setLike(resultEntity.isLike());
                    if (resultEntity.isLike()) {
                        int likeCount = questionData.getLikeCount();
                        likeCount = likeCount + 1;
                        questionData.setLikeCount(likeCount);
                        ivLikeIcon.setImageResource(R.drawable.liked_icon);
                    } else {
                        int likeCount = questionData.getLikeCount();
                        if (likeCount > 0) {
                            likeCount = likeCount - 1;
                            questionData.setLikeCount(likeCount);
                            ivLikeIcon.setImageResource(R.drawable.like_icon);
                        }
                    }
                    tvLikeCount.setText("" + questionData.getLikeCount());
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                //HelperClass.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            shareQuestion();
        }
    }

    private void shareQuestion() {
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.shareQuestion(questionData.getQuestionId(), PreferenceHelper.getToken(this));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                if (response.code() == 200) {
                    updatePreviousDataForMCQ = true;

                    int shareCount = questionData.getShareCount();
                    shareCount = shareCount + 1;
                    questionData.setShareCount(shareCount);
                    tvShareCount.setText("" + questionData.getShareCount());
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

}


