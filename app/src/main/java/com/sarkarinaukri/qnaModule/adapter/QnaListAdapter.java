package com.sarkarinaukri.qnaModule.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sarkarinaukri.BuildConfig;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.Option;
import com.sarkarinaukri.model.QuestionData;
import com.sarkarinaukri.model.ResultEntity;
import com.sarkarinaukri.qnaModule.ViewQuestionImageActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anurag on 7/26/2016.
 */

public class QnaListAdapter extends RecyclerView.Adapter<QnaListAdapter.ViewHolder> {
    private List<QuestionData> qnaList;
    private Context context;
    private ItemListener listener;
    private OptionAnswerListAdapter optionAnswerListAdapter;

    public QnaListAdapter(Context context, List<QuestionData> qnaList, ItemListener listener) {
        this.context = context;
        this.qnaList = qnaList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.qna_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final QuestionData questionData = qnaList.get(position);

        if (TextUtils.isEmpty(questionData.getQuestionImage())) {
            holder.ivQuestionImage.setVisibility(View.GONE);
        } else {
            holder.ivQuestionImage.setVisibility(View.VISIBLE);
            String questionImage = questionData.getQuestionImage();

            String questionImageUrl = BuildConfig.IMAGE_URL + questionImage;
            Picasso.get().load(questionImageUrl).into(holder.ivQuestionImage);
        }

        holder.tvName.setText("SarkariExam");
        holder.tvPostedDate.setText(HelperClass.parseDateToddMMyyyy(questionData.getCreatedDate()));

        holder.tvShareCount.setText("" + questionData.getShareCount());
        holder.tvLikeCount.setText("" + questionData.getLikeCount());
        holder.tvCommentCount.setText(questionData.getAnswerCount());

        if (!TextUtils.isEmpty(questionData.getPhoto())) {
            String profilePic = "profilePhoto/" + questionData.getPhoto();
            String profileImageURL = BuildConfig.IMAGE_URL + profilePic;
            Picasso.get().load(profileImageURL).into(holder.ivProfilePhoto);
        } else {
            holder.ivProfilePhoto.setImageDrawable(context.getResources().getDrawable(R.mipmap.logo));
        }

        if (TextUtils.isEmpty(questionData.getQuestion())) {
            holder.tvQuestionDetail.setVisibility(View.GONE);
        } else {
            holder.tvQuestionDetail.setVisibility(View.VISIBLE);
            holder.tvQuestionDetail.setText(questionData.getQuestion());
        }

        if (questionData.isLike()) {
            holder.ivLikeIcon.setImageResource(R.drawable.liked_icon);
        } else {
            holder.ivLikeIcon.setImageResource(R.drawable.like_icon);
        }

        if (questionData.getUserId().equalsIgnoreCase(PreferenceHelper.getUserId(context))) {
            holder.flDelete.setVisibility(View.VISIBLE);
        } else {
            holder.flDelete.setVisibility(View.GONE);
        }

        holder.ivQuestionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewQuestionImageActivity.class);
                intent.putExtra(Constant.QUESTION_IMAGE, questionData.getQuestionImage());
                context.startActivity(intent);
            }
        });

        if (!TextUtils.isEmpty(questionData.getRightOption()) || questionData.getOptions().size() > 0) {
            setOptionsListInAdapter(position, questionData.getQuestionId(), holder.recyclerViewOptions, questionData.getRightOption(), questionData.getGivenOption(), questionData.getOptions());
        } else {
            holder.recyclerViewOptions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return qnaList.size();
    }

    public interface ItemListener {
        void shareQuestion(int position);

        void likeQuestion(int position);

        void viewAnswer(int position);

        void onDelete(int position);

        void onMCQAnswer(int position, int optionPosition, QuestionData userGivenOption);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llLike;
        private final ImageView ivLikeIcon;
        private final TextView tvLikeCount;
        private final LinearLayout llComment;
        private final TextView tvCommentCount;
        private final LinearLayout llShare;
        private final TextView tvShareCount;
        private final TextView tvName;
        private final TextView tvPostedDate;
        private final TextView tvQuestionDetail;
        private final ImageView ivProfilePhoto;
        private final ImageView ivQuestionImage;
        private final FrameLayout flDelete;
        private final RecyclerView recyclerViewOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            llLike = itemView.findViewById(R.id.llLike);
            ivLikeIcon = itemView.findViewById(R.id.ivLikeIcon);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            llComment = itemView.findViewById(R.id.llComment);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            llShare = itemView.findViewById(R.id.llShare);
            tvShareCount = itemView.findViewById(R.id.tvShareCount);
            tvName = itemView.findViewById(R.id.tvName);
            tvPostedDate = itemView.findViewById(R.id.tvPostedDate);
            tvQuestionDetail = itemView.findViewById(R.id.tvQuestionDetail);
            ivProfilePhoto = itemView.findViewById(R.id.ivProfilePhoto);
            ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
            flDelete = itemView.findViewById(R.id.flDelete);
            recyclerViewOptions = itemView.findViewById(R.id.recyclerViewOptions);

            llLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.likeQuestion(getAdapterPosition());
                }
            });

            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.viewAnswer(getAdapterPosition());
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.viewAnswer(getAdapterPosition());
                }
            });

            flDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDelete(getAdapterPosition());
                }
            });

            llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.shareQuestion(getAdapterPosition());
                }
            });
        }
    }

    private void setOptionsListInAdapter(final int position, final String questionId, RecyclerView recyclerViewOptions, String rightOption, String givenOption, final List<Option> options) {
        recyclerViewOptions.setVisibility(View.VISIBLE);
        recyclerViewOptions.setNestedScrollingEnabled(false);
        recyclerViewOptions.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewOptions.setLayoutManager(layoutManager);
        optionAnswerListAdapter = new OptionAnswerListAdapter(context, rightOption, givenOption, options, new OptionAnswerListAdapter.ProgressListener() {
            @Override
            public void onProgressClickListener(int optionPosition, String userGivenOption) {
                postMCQOptionToServer(position, optionPosition, questionId, options.get(optionPosition).getId());

                //listener.onMCQAnswer(position, optionPosition, userGivenOption);


            }
        });
        recyclerViewOptions.setAdapter(optionAnswerListAdapter);
    }

    private void postMCQOptionToServer(final int position, final int optionPosition, String questionId, String optionId) {
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.postMCQOption(questionId, optionId, PreferenceHelper.getToken(context));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.toString().equalsIgnoreCase(resultEntity.getStatus())) {
                        listener.onMCQAnswer(position, optionPosition, resultEntity.getQuestionData().get(0));
                        optionAnswerListAdapter.notifyItemChanged(optionPosition);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                //HelperClass.dismissProgressDialog();
            }
        });
    }

}