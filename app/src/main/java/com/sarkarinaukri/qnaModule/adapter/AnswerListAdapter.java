package com.sarkarinaukri.qnaModule.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarkarinaukri.BuildConfig;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.AnswerData;
import com.sarkarinaukri.model.ResultEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anurag on 7/26/2016.
 */

public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.ViewHolder> {
    private List<AnswerData> answerDataList;
    private Context context;

    public AnswerListAdapter(Context context, List<AnswerData> answerDataList) {
        this.context = context;
        this.answerDataList = answerDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AnswerData answerData = answerDataList.get(position);
        holder.tvName.setText(answerData.getFullName());
        holder.tvPostedDate.setText(HelperClass.parseDateToddMMyyyy(answerData.getCreatedDate()));
        holder.tvAnswerDetail.setText(answerData.getAnswer());
        int likeDislikeCount = Integer.parseInt(answerData.getLikeCount()) - Integer.parseInt(answerData.getDislikeCount());
        holder.tvLikeDislikeCount.setText("" + likeDislikeCount);

        if (!TextUtils.isEmpty(answerData.getPhoto())) {
            String profilePic = answerData.getPhoto();
            String profileImageURL = BuildConfig.IMAGE_URL + profilePic;
            Picasso.get().load(profileImageURL).into(holder.ivProfilePhoto);
        }
    }

    @Override
    public int getItemCount() {
        return answerDataList.size();
    }

    private void likeDislikeQuestion(final int position, likeDislike likeDislike) {
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.likeDislikeAnswer(answerDataList.get(position).getAnswerId(), answerDataList.get(position).getAnswer(), answerDataList.get(position).getQuestionId(), answerDataList.get(position).getUserId(), likeDislike.name(), PreferenceHelper.getToken(context));
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                //HelperClass.displayRequestAndResponse(response, null);
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();
                    if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
                        answerDataList.get(position).setLikeCount(resultEntity.getLikeCount());
                        answerDataList.get(position).setDislikeCount(resultEntity.getDislikeCount());
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {
                HelperClass.dismissProgressDialog();
            }
        });
    }

    public enum likeDislike {
        like,
        dislike
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivProfilePhoto;
        private final TextView tvName;
        private final TextView tvPostedDate;
        private final TextView tvAnswerDetail;
        private final TextView tvLikeDislikeCount;
        private final ImageView ivLike;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProfilePhoto = itemView.findViewById(R.id.ivProfilePhoto);
            tvName = itemView.findViewById(R.id.tvName);
            tvPostedDate = itemView.findViewById(R.id.tvPostedDate);
            tvAnswerDetail = itemView.findViewById(R.id.tvAnswerDetail);
            tvLikeDislikeCount = itemView.findViewById(R.id.tvLikeDislikeCount);
            ivLike = itemView.findViewById(R.id.ivLike);

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeDislikeQuestion(getAdapterPosition(), likeDislike.like);
                }
            });
        }
    }
}