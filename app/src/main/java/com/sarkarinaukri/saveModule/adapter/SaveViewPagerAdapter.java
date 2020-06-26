package com.sarkarinaukri.saveModule.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;

import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.RetroFit.ApiclientArchi;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.Likeunlike;
import com.sarkarinaukri.model.QuestionList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SaveViewPagerAdapter extends RecyclerView.Adapter<SaveViewPagerAdapter.ViewHolder> {

    Context context;
    OnDeleteItemClickListener onDeleteItemClickListener;
    private List<QuestionList.data> dataArrayList;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;



    public SaveViewPagerAdapter(Context context, List<QuestionList.data> dataArrayList, ViewPager2 viewPager2, OnDeleteItemClickListener monDeleteItemClickedListener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.viewPager2 = viewPager2;
        this.onDeleteItemClickListener = monDeleteItemClickedListener;
    }


    @Override
    public SaveViewPagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.page_row_save, parent, false);
        return new SaveViewPagerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final SaveViewPagerAdapter.ViewHolder holder, final int position) {

//        if (holder.player == null) {
        // 1. Create a default TrackSelector

        //      }

        if (dataArrayList.get(position).type.equalsIgnoreCase("question")) {
            holder.llImage.setVisibility(View.GONE);
            holder.rlLike.setVisibility(View.GONE);
            holder.videoframe.setVisibility(View.GONE);

            holder.rlQueslike.setVisibility(View.GONE);
            holder.rlTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.llQuestion.setVisibility(View.VISIBLE);
            //tvQuestions.setVisibility(View.VISIBLE);
            holder.tvQuestion.setVisibility(View.VISIBLE);
            holder.tvQuestion.setText(dataArrayList.get(position).title);
//            tvQuestions.setText(dataArrayList.get(position).title);

            holder.tvOpetion1.setText(dataArrayList.get(position).opt1);
            holder.tvOpetion2.setText(dataArrayList.get(position).opt2);
            holder.tvOpetion3.setText(dataArrayList.get(position).opt3);
            holder.tvOpetion4.setText(dataArrayList.get(position).opt4);


        } else if (dataArrayList.get(position).type.equalsIgnoreCase("artical")) {

            holder.llQuestion.setVisibility(View.GONE);
            holder.rlTitle.setVisibility(View.GONE);
            holder.tvQuestions.setVisibility(View.GONE);
            holder.rlQueslike.setVisibility(View.GONE);
            holder.tvJobcontent.setVisibility(View.GONE);
            holder.tvMore.setVisibility(View.GONE);
            holder.rlLike.setVisibility(View.VISIBLE);
            holder.llImage.setVisibility(View.VISIBLE);
            holder.videoframe.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(dataArrayList.get(position).title);
            holder.tvContent.setText(dataArrayList.get(position).content);
            Glide.with(context)
                    .load(dataArrayList.get(position).file)
                    .skipMemoryCache(true)
                    .override(500,300)
                    .placeholder(R.drawable.ic_no_img)
                    .into(holder.imageView);


        } else if (dataArrayList.get(position).type.equalsIgnoreCase("job")) {

            holder.llQuestion.setVisibility(View.GONE);
            holder.rlTitle.setVisibility(View.GONE);
            holder.tvQuestions.setVisibility(View.GONE);
            holder.rlQueslike.setVisibility(View.GONE);
            holder.videoframe.setVisibility(View.GONE);
            holder.tvContent.setVisibility(View.GONE);
            holder.rlLike.setVisibility(View.VISIBLE);
            holder.llImage.setVisibility(View.VISIBLE);
            holder.tvJobcontent.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(dataArrayList.get(position).title);
            holder.tvJobcontent.setText(dataArrayList.get(position).content);
            Glide.with(context)
                    .load(dataArrayList.get(position).file)
                    .skipMemoryCache(true)
                    .override(500,300)
                    .into(holder.imageView);
            holder.tvMore.setVisibility(View.VISIBLE);

        }

        //viewCount(PreferenceHelper.ReadSharePrefrence(context, Config.deviceid),dataArrayList.get(position).id);



        //to click news details to open webview
        //click event for more details about news
        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataArrayList.get(position).answer.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please Try again Some time ", Toast.LENGTH_LONG).show();

                } else {
                    HelperClass.openUrlInChromeCustomTab(context, dataArrayList.get(position).answer);

                }

            }
        });


        holder.tvOpetion1.setBackgroundResource(0);
        holder.tvOpetion2.setBackgroundResource(0);
        holder.tvOpetion3.setBackgroundResource(0);
        holder.tvOpetion4.setBackgroundResource(0);

        holder.tvYourans1.setVisibility(View.GONE);
        holder.tvYourans2.setVisibility(View.GONE);
        holder.tvYourans3.setVisibility(View.GONE);
        holder.tvYourans4.setVisibility(View.GONE);

        holder.ivRightans1.setVisibility(View.GONE);
        holder.ivRightans2.setVisibility(View.GONE);
        holder.ivRightans3.setVisibility(View.GONE);
        holder.ivRightans4.setVisibility(View.GONE);

        holder.tvOpetion1.setEnabled(true);
        holder.tvOpetion2.setEnabled(true);
        holder.tvOpetion3.setEnabled(true);
        holder.tvOpetion4.setEnabled(true);



        // to check right and wrong answer
        //click event for answer1
        holder.tvOpetion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataArrayList.get(position).answer.equalsIgnoreCase("opt1")) {

                    holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                    holder.ivRightans1.setVisibility(View.VISIBLE);
                    holder.tvOpetion2.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);


                } else {
                    holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_red);
                    holder.tvYourans1.setVisibility(View.VISIBLE);

                    holder.tvOpetion2.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);


                    if (dataArrayList.get(position).answer.equalsIgnoreCase("opt2")) {

                        holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans2.setVisibility(View.VISIBLE);


                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt3")) {
                        holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans3.setVisibility(View.VISIBLE);

                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt4")) {
                        holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans4.setVisibility(View.VISIBLE);

                    }


                }

            }
        });


        holder.tvOpetion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataArrayList.get(position).answer.equalsIgnoreCase("opt2")) {

                    holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                    holder.ivRightans2.setVisibility(View.VISIBLE);
                    holder.tvOpetion1.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);


                } else {
                    holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_red);
                    holder.tvYourans2.setVisibility(View.VISIBLE);

                    holder.tvOpetion1.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);


                    if (dataArrayList.get(position).answer.equalsIgnoreCase("opt1")) {

                        holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans1.setVisibility(View.VISIBLE);

                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt3")) {
                        holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans3.setVisibility(View.VISIBLE);
                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt4")) {
                        holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans4.setVisibility(View.VISIBLE);
                    }


                }


            }
        });
//
//                                //click event for answer3
        holder.tvOpetion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataArrayList.get(position).answer.equalsIgnoreCase("opt3")) {

                    holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                    holder.ivRightans3.setVisibility(View.VISIBLE);
                    holder.tvOpetion1.setEnabled(false);
                    holder.tvOpetion2.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);


                } else {
                    holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_red);
                    holder.tvYourans3.setVisibility(View.VISIBLE);

                    holder.tvOpetion1.setEnabled(false);
                    holder.tvOpetion2.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);

                    if (dataArrayList.get(position).answer.equalsIgnoreCase("opt1")) {

                        holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans1.setVisibility(View.VISIBLE);

                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt2")) {
                        holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans2.setVisibility(View.VISIBLE);
                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt4")) {
                        holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans4.setVisibility(View.VISIBLE);
                    }


                }


            }
        });
//
//                                //click event for answer4
        holder.tvOpetion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataArrayList.get(position).answer.equalsIgnoreCase("opt4")) {

                    holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                    holder.ivRightans4.setVisibility(View.VISIBLE);
                    holder.tvOpetion1.setEnabled(false);
                    holder.tvOpetion2.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);


                } else {

                    holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_red);
                    holder.tvYourans4.setVisibility(View.VISIBLE);

                    holder.tvOpetion1.setEnabled(false);
                    holder.tvOpetion2.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);

                    if (dataArrayList.get(position).answer.equalsIgnoreCase("opt1")) {

                        holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans1.setVisibility(View.VISIBLE);

                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt2")) {
                        holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans2.setVisibility(View.VISIBLE);
                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt3")) {
                        holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans3.setVisibility(View.VISIBLE);
                    }


                }

            }
        });


        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_delete);

                TextView tvDelete = dialog.findViewById(R.id.tvSubmit);
                TextView tvCancel = dialog.findViewById(R.id.tv_cancel);

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        HelperClass.showProgressDialog(context, "");
                        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                        Call<Likeunlike> call = apiInterface.Deleteidwish(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), dataArrayList.get(position).id, dataArrayList.get(position).type);
                        call.enqueue(new Callback<Likeunlike>() {
                            @Override
                            public void onResponse(Call<Likeunlike> call, Response<Likeunlike> response) {
                                //HelperClass.displayRequestAndResponse(response, null);
                                HelperClass.dismissProgressDialog();
                                if (response.code() == 200) {
                                    Likeunlike resultEntity = response.body();
                                    onDeleteItemClickListener.onDeleteClicked();

//
                                } else {

                                    Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<Likeunlike> call, Throwable t) {
                                Toast.makeText(context, "Please Try again!", Toast.LENGTH_SHORT).show();
                                HelperClass.dismissProgressDialog();
                            }
                        });

                    }
                });


                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }



    public interface OnDeleteItemClickListener {

        public void onDeleteClicked();

    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView myTextView;
        RelativeLayout relativeLayout;

        ////-------QUESTIONS--------
        RelativeLayout rlTitle;
        TextView tvTitle;
        TextView tvQuestions;
        JustifiedTextView tvQuestion;
        TextView tvOpetion1, tvOpetion2, tvOpetion3, tvOpetion4;
        TextView tvDescription;
        ImageView imageView;
        TextView tvContent;
        ////-------QUESTIONS--------
        TextView tvMore;
        String videoUri;

        Handler mHandler;
        Runnable mRunnable;
        private TextView tvQueslikecount;
        private TextView tvQuescommentcount;
        private TextView tvQueswatsappcount;
        private TextView tvQuessavecount;
        private TextView tvQuesViewcount;
        private ImageView ivQuesWatsappgif;
        private ImageView ivQueslike;
        private LinearLayout llQueslike;
        private LinearLayout llQuescomment;
        private LinearLayout llQueswatsapp;
        private LinearLayout llQuesave;
        private TextView tvYourans1;
        private TextView tvYourans2;
        private TextView tvYourans3;
        private TextView tvYourans4;
        private ImageView ivRightans1;
        private ImageView ivRightans2;
        private ImageView ivRightans3;
        private ImageView ivRightans4;
        private ImageView ivQuesave;
        private LinearLayout llQuestion;
        private RelativeLayout rlQueslike;
        ////-------Articales and job--------
        private LinearLayout llImage;
        private RelativeLayout rlLike;
        private TextView tvLikecount;
        private TextView tvCommentcount;
        private TextView tvSharecount;
        private TextView tvSavecount;
        private TextView tvViewcount;
        private TextView tvJobcontent;
        ////-------Articales and job--------
        private ImageView ivLike;
        private ImageView ivWatsap;
        private ImageView ivComment;
        private ImageView ivSave;
        private Boolean click = true;
        ////----------video-----------
        private RelativeLayout videoframe;
        private ImageView ivPlay;
        private ImageView ivThumb;
        private TextView tvVideotitle;
        private VideoView video;
        private ProgressBar progressBar = null;
        private ImageView ivProgress;

        private TextView tvDelete;



        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvTitle);
            relativeLayout = itemView.findViewById(R.id.container);


            llQuestion = itemView.findViewById(R.id.ll_question);
            llImage = itemView.findViewById(R.id.ll_image);
            rlTitle = itemView.findViewById(R.id.rl_title);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvQuestions = itemView.findViewById(R.id.tv_question);
            tvQuestion = itemView.findViewById(R.id.tv_que);
            tvOpetion1 = itemView.findViewById(R.id.tv_opetion1);
            tvOpetion2 = itemView.findViewById(R.id.tv_opetion2);
            tvOpetion3 = itemView.findViewById(R.id.tv_opetion3);
            tvOpetion4 = itemView.findViewById(R.id.tv_opetion4);


            tvDescription = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.iv_artical);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvJobcontent = itemView.findViewById(R.id.tv_jobcontent);

            tvMore = itemView.findViewById(R.id.tv_clickmore);

            rlLike = itemView.findViewById(R.id.rl_like);
            rlQueslike = itemView.findViewById(R.id.rl_questionlike);

            //job abd artical like and comment
            tvLikecount = itemView.findViewById(R.id.tvLikeCount);
            tvCommentcount = itemView.findViewById(R.id.tvCommentCount);
            tvSharecount = itemView.findViewById(R.id.tvShareCount);
            tvSavecount = itemView.findViewById(R.id.tv_save);
            tvViewcount = itemView.findViewById(R.id.tv_viewcount);
            ivLike = itemView.findViewById(R.id.ivLikeIcon);
            ivWatsap = itemView.findViewById(R.id.iv_wastapp);
            ivComment = itemView.findViewById(R.id.ivComment);
            ivSave = itemView.findViewById(R.id.iv_save);

            //questions like and comments and share etc
            tvQueslikecount = itemView.findViewById(R.id.tv_queslikecount);
            tvQuescommentcount = itemView.findViewById(R.id.tv_quescommentcount);
            tvQueswatsappcount = itemView.findViewById(R.id.tv_watsappcount);
            tvQuessavecount = itemView.findViewById(R.id.tv_quessavecount);
            tvQuesViewcount = itemView.findViewById(R.id.tv_quesviewcount);
            ivQuesWatsappgif = itemView.findViewById(R.id.iv_queswatsapp);
            ivQueslike = itemView.findViewById(R.id.iv_queslike);
            llQueslike = itemView.findViewById(R.id.ll_queslike);
            llQuescomment = itemView.findViewById(R.id.ll_quescomment);
            llQueswatsapp = itemView.findViewById(R.id.ll_queswatsapp);
            ivQuesave = itemView.findViewById(R.id.iv_quessave);
            llQuesave = itemView.findViewById(R.id.ll_quessave);

            tvYourans1 = itemView.findViewById(R.id.tv_yourans1);
            tvYourans2 = itemView.findViewById(R.id.tv_yourans2);
            tvYourans3 = itemView.findViewById(R.id.tv_yourans3);
            tvYourans4 = itemView.findViewById(R.id.tv_yourans4);


            ivRightans1 = itemView.findViewById(R.id.iv_yourrightans1);
            ivRightans2 = itemView.findViewById(R.id.iv_yourrightans2);
            ivRightans3 = itemView.findViewById(R.id.iv_yourrightans3);
            ivRightans4 = itemView.findViewById(R.id.iv_yourrightans4);

            //--------------video-------------

            videoframe = itemView.findViewById(R.id.video_frame);
            ivPlay = itemView.findViewById(R.id.play_button);
            video = itemView.findViewById(R.id.video);
            ivThumb = itemView.findViewById(R.id.iv_thumb);
            tvVideotitle = itemView.findViewById(R.id.tv_videotitle);
            progressBar = itemView.findViewById(R.id.progressbar);

            tvDelete = itemView.findViewById(R.id.tv_delete);


        }
    }
}
