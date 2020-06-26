package com.sarkarinaukri.qnaModule.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.RetroFit.ApiclientArchi;
import com.sarkarinaukri.adapter.CommentAdapter;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.CommentDetails;
import com.sarkarinaukri.model.Commentcount;
import com.sarkarinaukri.model.Likeunlike;
import com.sarkarinaukri.model.QuestionList;
import com.sarkarinaukri.model.Selectedanswer;
import com.sarkarinaukri.model.Selectedcount;
import com.sarkarinaukri.model.Selectedlike;
import com.sarkarinaukri.model.Selectedsave;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewQuestionViewpagerAdapter extends RecyclerView.Adapter<NewQuestionViewpagerAdapter.ViewHolder> {

    Context context;
    private List<QuestionList.data> dataArrayList;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;
    private CommentAdapter commentAdapter;
    private ViewHolder videostore;
    OnDeleteItemClickListener onDeleteItemClickListener;
    ArrayList<Selectedanswer> bookList = new ArrayList<Selectedanswer>();
    Integer pageno = 2;
    Handler handler=  new Handler();
    private int SPLASH_TIME_OUT = 60000;

    private UnifiedNativeAd nativeAd;
    FrameLayout frameLayout;
    private CheckBox startVideoAdsMuted;
    private AdView adView;
    TemplateView adViews;
    AdRequest adRequest;


    public NewQuestionViewpagerAdapter(Context context, List<QuestionList.data> dataArrayList, ViewPager2 viewPager2,OnDeleteItemClickListener monDeleteItemClickedListener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.viewPager2 = viewPager2;
        this.onDeleteItemClickListener = monDeleteItemClickedListener;
    }


    @Override
    public NewQuestionViewpagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.pager_row_questions, parent, false);
        return new NewQuestionViewpagerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final NewQuestionViewpagerAdapter.ViewHolder holder, final int position) {

        //Log.e("TAG","DEVICE_ID==>"+PreferenceHelper.ReadSharePrefrence(context,Config.deviceid));
        MobileAds.initialize(context, "ca-app-pub-1792973056979970~8013561648");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//
                if (PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID).equalsIgnoreCase("")) {

                    onDeleteItemClickListener.onDeleteClicked();
                    //Toast.makeText(context, "login question", Toast.LENGTH_SHORT).show();
                    handler.removeMessages(0);
                }

                }
        }, SPLASH_TIME_OUT);
        handler.removeCallbacks(null);

        ads();
        if (position==0)
        {
            holder.llTemplate.setVisibility(View.INVISIBLE);
        }

        if (position>0) {
            if (position % 5 == 0) {
                //TODO
                holder.llTemplate.setVisibility(View.VISIBLE);
                //Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();
            } else {
                //TODO
                holder.llTemplate.setVisibility(View.INVISIBLE);
            }
        }



        if (dataArrayList.get(position)==dataArrayList.get(dataArrayList.size()-3))
        {

            ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
            Call<QuestionList> call = apiInterface.getquestionpagination(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), String.valueOf(pageno));
            call.enqueue(new Callback<QuestionList>() {
                @Override
                public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                    //HelperClass.displayRequestAndResponse(response, null);
                    if (response.code() == 200) {

                        QuestionList resultEntity = response.body();

                        if (resultEntity.status.equalsIgnoreCase("true"))
                        {
                            dataArrayList.addAll(resultEntity.data);
                            pageno++;

                            notifyDataSetChanged();

                            Log.e("TAG","DATARRYLIST SIZE==>"+dataArrayList.size());
                            Log.e("TAG","PAGE_NO==>"+pageno);


                        }



                    } else {

                        Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<QuestionList> call, Throwable t) {
                    Toast.makeText(context, "Please Try again!", Toast.LENGTH_SHORT).show();
                    HelperClass.dismissProgressDialog();
                }
            });

        }


        //load banner ads
//        adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);


            //tvQuestions.setVisibility(View.VISIBLE);
            holder.tvQuestion.setVisibility(View.VISIBLE);
            holder.tvQuestion.setText(dataArrayList.get(position).title);
//            tvQuestions.setText(dataArrayList.get(position).title);

            holder.tvOpetion1.setText(dataArrayList.get(position).opt1);
            holder.tvOpetion2.setText(dataArrayList.get(position).opt2);
            holder.tvOpetion3.setText(dataArrayList.get(position).opt3);
            holder.tvOpetion4.setText(dataArrayList.get(position).opt4);



        //to set questions like counts and comment counts and watsapp sharing count etc

        //set QUESTIONS LIKE AND COMMENT AND SHARE COUNT
        holder.tvQueslikecount.setText(dataArrayList.get(position).likecount);
        holder.tvQuescommentcount.setText(dataArrayList.get(position).commentcount);
        holder.tvQueswatsappcount.setText(dataArrayList.get(position).sharecount);
        holder.tvQuessavecount.setText(dataArrayList.get(position).whishlistcount);
        holder.tvQuesViewcount.setText(dataArrayList.get(position).viewcount);
        Glide.with(context)
                .load(R.drawable.ic_watsappgif)
                .asGif()
                .into(holder.ivQuesWatsappgif);
        if (dataArrayList.get(position).userlike.equalsIgnoreCase("true")) {
            holder.ivQueslike.setImageResource(R.drawable.heart_red);

        } else {
            holder.ivQueslike.setImageResource(R.drawable.ic_new_like);
        }

        //click event for questions like
        holder.llQueslike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID).equalsIgnoreCase("")) {

//                    Intent intent=new Intent(context, NewLoginActivity.class);
//                    ((Activity) context).startActivityForResult(intent, 101);
                    onDeleteItemClickListener.onDeleteClicked();

                }
                else
                    {
                    //likeQuestiontype(PreferenceHelper.getUserId(QuestionCardActivity.this),dataArrayList.get(position).id,dataArrayList.get(position).type);
                    ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                    Call<Likeunlike> call = apiInterface.Likeunlike(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), dataArrayList.get(position).id, dataArrayList.get(position).type);
                    call.enqueue(new Callback<Likeunlike>() {
                        @Override
                        public void onResponse(Call<Likeunlike> call, Response<Likeunlike> response) {
                            //HelperClass.displayRequestAndResponse(response, null);

                            if (response.code() == 200) {
                                Likeunlike resultEntity = response.body();
                                if (resultEntity.status.equalsIgnoreCase("true"))
                                {
                                    holder.tvQueslikecount.setText(resultEntity.count);
                                    holder.ivQueslike.setImageResource(R.drawable.heart_red);

                                    holder.likeList.add(new Selectedlike(dataArrayList.get(position).id,"true",resultEntity.count));

                                    //onDeleteItemClickListener.onDeleteClicked();

//                                    boolean isInserted = db.insertProduct(dataArrayList.get(position).id,"true",resultEntity.count);
//                                    if (isInserted) {
//                                        Log.e("TAG", "RECORD INSERTED==>" + isInserted);
//                                        Toast.makeText(context, "Item Added In cart", Toast.LENGTH_SHORT).show();
//                                    }

                                }
                                else
                                {
                                    holder.tvQueslikecount.setText(resultEntity.count);
                                    holder.ivQueslike.setImageResource(R.drawable.ic_new_like);
                                    holder.likeList.add(new Selectedlike(dataArrayList.get(position).id,"false",resultEntity.count));

                                    //onDeleteItemClickListener.onDeleteClicked();

//                                    boolean isUpdate = db.updateProduct(dataArrayList.get(position).id,"false",resultEntity.count);
//                                    if (isUpdate) {
//                                        Log.e("TAG", "RECORD UPDATED==>" + isUpdate);
//                                    }

                                }

                                //Toast.makeText(context, resultEntity.msg, Toast.LENGTH_SHORT).show();
//                                click[0] = false;


                            } else {

                                Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<Likeunlike> call, Throwable t) {
                            HelperClass.dismissProgressDialog();
                        }
                    });




                    }


            }
        });

        //click events for QUESTIONS comments
        holder.llQuescomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID).equalsIgnoreCase("")) {

//                    Intent intent = new Intent(context, NewLoginActivity.class);
//                    ((Activity) context).startActivityForResult(intent, 101);
                    onDeleteItemClickListener.onDeleteClicked();


                } else {

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.comment_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                    final RecyclerView rvComment = dialog.findViewById(R.id.rv_comment);
                    final EditText etAnswer;
                    final LinearLayout llSend;


//                HelperClass.showProgressDialog(context, "");
                    ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                    Call<CommentDetails> call = apiInterface.getComment(dataArrayList.get(position).id, dataArrayList.get(position).type);
                    Log.e("NEWQUESTIONS==>","id and type"+dataArrayList.get(position).id+dataArrayList.get(position).type);
                    call.enqueue(new Callback<CommentDetails>() {
                        @Override
                        public void onResponse(Call<CommentDetails> call, Response<CommentDetails> response) {
                            //HelperClass.displayRequestAndResponse(response, null);

//                        HelperClass.dismissProgressDialog();
                            if (response.code() == 200) {
                                CommentDetails resultEntity = response.body();
                                if (resultEntity.status.equals("true")) {
                                    //Toast.makeText(getApplicationContext(), resultEntity.msg, Toast.LENGTH_SHORT).show();
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    rvComment.setLayoutManager(layoutManager);
//                                    layoutManager.setReverseLayout(true);
                                    commentAdapter = new CommentAdapter(context, resultEntity.data);
                                    rvComment.setAdapter(commentAdapter);

                                } else {
                                   // Toast.makeText(context, resultEntity.msg, Toast.LENGTH_SHORT).show();
                                }


                            } else {

                                Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<CommentDetails> call, Throwable t) {
                            HelperClass.dismissProgressDialog();
                        }
                    });


                    etAnswer = dialog.findViewById(R.id.etAnswer);
                    //answer=etAnswer.getText().toString();
                    llSend = dialog.findViewById(R.id.llSend);
                    llSend.setEnabled(false);

                    etAnswer.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            if (etAnswer.getText().length() > 0) {
                                llSend.setEnabled(true);
                            } else {
                                llSend.setEnabled(false);
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });


                    llSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (etAnswer.getText().toString().isEmpty()) {
                                Toast.makeText(context, "Please Write Comment!", Toast.LENGTH_LONG).show();

                            } else {

                                //sendComments(PreferenceHelper.getUserId(getApplicationContext()),questionid,questiontype,etAnswer.getText().toString());
                                ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                                Call<CommentDetails> call = apiInterface.sendComment(PreferenceHelper.ReadSharePrefrence(context,Constant.USER_ID), dataArrayList.get(position).id, dataArrayList.get(position).type, etAnswer.getText().toString());
                                call.enqueue(new Callback<CommentDetails>() {
                                    @Override
                                    public void onResponse(Call<CommentDetails> call, Response<CommentDetails> response) {
                                        //HelperClass.displayRequestAndResponse(response, null);

//                                    HelperClass.dismissProgressDialog();
                                        if (response.code() == 200) {
                                            CommentDetails resultEntity = response.body();

                                            if (resultEntity.status.equalsIgnoreCase("true")) {
                                                //Toast.makeText(getApplicationContext(), resultEntity.msg, Toast.LENGTH_SHORT).show();
                                                etAnswer.setText("");




                                                ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                                                Call<CommentDetails> calls = apiInterface.getComment(dataArrayList.get(position).id, dataArrayList.get(position).type);
                                                calls.enqueue(new Callback<CommentDetails>() {
                                                    @Override
                                                    public void onResponse(Call<CommentDetails> calls, Response<CommentDetails> response) {
                                                        //HelperClass.displayRequestAndResponse(response, null);

//                                                    HelperClass.dismissProgressDialog();
                                                        if (response.code() == 200) {
                                                            CommentDetails resultEntity = response.body();


                                                            if (resultEntity.status.equalsIgnoreCase("true")) {
                                                                //Toast.makeText(getApplicationContext(), resultEntity.msg, Toast.LENGTH_SHORT).show();
                                                                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                                                rvComment.setLayoutManager(layoutManager);
                                                                commentAdapter = new CommentAdapter(context, resultEntity.data);
                                                                rvComment.setAdapter(commentAdapter);

                                                                commentAdapter.notifyDataSetChanged();


                                                                //getCommentscount(dataArrayList.get(position).id,dataArrayList.get(position).type);


                                                                ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                                                                Call<Commentcount> call = apiInterface.getCommentcount(dataArrayList.get(position).id, dataArrayList.get(position).type);
                                                                call.enqueue(new Callback<Commentcount>() {
                                                                    @Override
                                                                    public void onResponse(Call<Commentcount> call, Response<Commentcount> response) {
                                                                        //HelperClass.displayRequestAndResponse(response, null);

//                                                                        HelperClass.dismissProgressDialog();
                                                                        if (response.code() == 200) {
                                                                            Commentcount resultEntity = response.body();

                                                                            holder.tvQuescommentcount.setText(resultEntity.data);
                                                                            holder.countList.add(new Selectedcount(dataArrayList.get(position).id,resultEntity.data));




                                                                        } else {

                                                                            Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                                                                        }


                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<Commentcount> call, Throwable t) {
                                                                        HelperClass.dismissProgressDialog();
                                                                    }
                                                                });


                                                            } else {
                                                                Toast.makeText(context, resultEntity.msg, Toast.LENGTH_SHORT).show();
                                                            }


                                                        } else {

                                                            Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
                                                        }


                                                    }

                                                    @Override
                                                    public void onFailure(Call<CommentDetails> calls, Throwable t) {
                                                        HelperClass.dismissProgressDialog();
                                                    }
                                                });


                                            } else {
//                                            Toast.makeText(context, resultEntity.msg, Toast.LENGTH_SHORT).show();
                                            }


                                        } else {

                                            Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
                                        }


                                    }

                                    @Override
                                    public void onFailure(Call<CommentDetails> call, Throwable t) {
                                        HelperClass.dismissProgressDialog();
                                    }
                                });

                            }


                        }
                    });

                }

            }

        });

        //click event for questions watsapp
        holder.llQueswatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, dataArrayList.get(position).title +"\n"
                        +" *मुझे" +
                        " SarkariExam ऐप का इस्तेमाल करने में मजा आ रहा है। अभी डाउनलोड करें*" +
                        " \uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47" + " - https://play.google.com/store/apps/details?id=" + context.getPackageName());
                try {

                    int likeCount = Integer.parseInt(holder.tvQueswatsappcount.getText().toString());
                    likeCount = likeCount + 1;
                    holder.tvQueswatsappcount.setText("" + likeCount);
                    likeWatsapp(PreferenceHelper.ReadSharePrefrence(context,Constant.USER_ID), dataArrayList.get(position).id, dataArrayList.get(position).type);
                    context.startActivity(whatsappIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
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




        if (holder.likeList.size()>0) {

            for (int i = 0; i < holder.likeList.size(); i++) {

                if (holder.likeList.get(i).getQuestionid().equalsIgnoreCase(dataArrayList.get(position).id)) {

                    String answer=holder.likeList.get(i).getLike();


                    if (answer.equalsIgnoreCase("true"))
                    {
                        holder.ivQueslike.setImageResource(R.drawable.heart_red);
                        holder.tvQueslikecount.setText(holder.likeList.get(i).getCount());

                    }
                    else if(answer.equalsIgnoreCase("false"))
                    {
                        holder.ivQueslike.setImageResource(R.drawable.ic_new_like);
                        holder.tvQueslikecount.setText(holder.likeList.get(i).getCount());
                    }

                }
            }

        }


//        if (holder.saveList.size()>0) {
//
//            for (int i = 0; i < holder.saveList.size(); i++) {
//
//                if (holder.saveList.get(i).getQuestionid().equalsIgnoreCase(dataArrayList.get(position).id)) {
//
//                    String save=holder.saveList.get(i).getLike();
//                    if (save.equalsIgnoreCase("true"))
//                    {
//                        holder.ivQuesave.setImageResource(R.drawable.ic_save_done);
//                        holder.tvQuessavecount.setText(holder.saveList.get(i).getCount());
//
//                    }
//                    else if(save.equalsIgnoreCase("false"))
//                    {
//                        holder.ivQuesave.setImageResource(R.drawable.ic_newsave);
//                        holder.tvQuessavecount.setText(holder.saveList.get(i).getCount());
//                    }
//
//                }
//            }
//
//        }


        if (holder.countList.size()>0) {

            for (int i = 0; i < holder.countList.size(); i++) {

                if (holder.countList.get(i).getQuestionid().equalsIgnoreCase(dataArrayList.get(position).id)) {

                    holder.tvQuescommentcount.setText(holder.countList.get(i).getCount());

                }
            }

        }





        if (bookList.size()>0)
        {

            for (int i=0;i<bookList.size();i++)
            {

                if(bookList.get(i).getQuestionid().equalsIgnoreCase(dataArrayList.get(position).id))
                {

                    String answer=bookList.get(i).getRightanswer();

                    if (dataArrayList.get(position).answer.equalsIgnoreCase(answer))
                    {
                        if (answer.equalsIgnoreCase("opt1"))
                        {
                            holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                            holder.ivRightans1.setVisibility(View.VISIBLE);

                            holder.tvOpetion1.setEnabled(false);
                            holder.tvOpetion2.setEnabled(false);
                            holder.tvOpetion3.setEnabled(false);
                            holder.tvOpetion4.setEnabled(false);

                        }
                        else if (answer.equalsIgnoreCase("opt2"))
                        {
                            holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                            holder.ivRightans2.setVisibility(View.VISIBLE);

                            holder.tvOpetion1.setEnabled(false);
                            holder.tvOpetion2.setEnabled(false);
                            holder.tvOpetion3.setEnabled(false);
                            holder.tvOpetion4.setEnabled(false);

                        }
                        else if(answer.equalsIgnoreCase("opt3"))
                        {
                            holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                            holder.ivRightans3.setVisibility(View.VISIBLE);

                            holder.tvOpetion1.setEnabled(false);
                            holder.tvOpetion2.setEnabled(false);
                            holder.tvOpetion3.setEnabled(false);
                            holder.tvOpetion4.setEnabled(false);
                        }
                        else if(answer.equalsIgnoreCase("opt4"))
                        {
                            holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                            holder.ivRightans4.setVisibility(View.VISIBLE);

                            holder.tvOpetion1.setEnabled(false);
                            holder.tvOpetion2.setEnabled(false);
                            holder.tvOpetion3.setEnabled(false);
                            holder.tvOpetion4.setEnabled(false);
                        }


                    }

                    else
                    {

                        if (answer.equalsIgnoreCase("opt1"))
                        {
                            holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_red);
                            holder.tvYourans1.setVisibility(View.VISIBLE);

                            holder.tvOpetion1.setEnabled(false);
                            holder.tvOpetion2.setEnabled(false);
                            holder.tvOpetion3.setEnabled(false);
                            holder.tvOpetion4.setEnabled(false);

                            if (dataArrayList.get(position).answer.equalsIgnoreCase("opt2"))
                            {
                                holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans2.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);
                            }
                            else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt3"))
                            {
                                holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans3.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);

                            }
                            else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt4"))
                            {
                                holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans4.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);

                            }

                        }

                       else if (answer.equalsIgnoreCase("opt2"))
                        {
                            holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_red);
                            holder.tvYourans2.setVisibility(View.VISIBLE);

                            holder.tvOpetion1.setEnabled(false);
                            holder.tvOpetion2.setEnabled(false);
                            holder.tvOpetion3.setEnabled(false);
                            holder.tvOpetion4.setEnabled(false);

                            if (dataArrayList.get(position).answer.equalsIgnoreCase("opt1"))
                            {
                                holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans1.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);
                            }
                            else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt3"))
                            {
                                holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans3.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);

                            }
                            else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt4"))
                            {
                                holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans4.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);

                            }

                        }
                        else if (answer.equalsIgnoreCase("opt3"))
                        {
                            holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_red);
                            holder.tvYourans3.setVisibility(View.VISIBLE);

                            holder.tvOpetion1.setEnabled(false);
                            holder.tvOpetion2.setEnabled(false);
                            holder.tvOpetion3.setEnabled(false);
                            holder.tvOpetion4.setEnabled(false);

                            if (dataArrayList.get(position).answer.equalsIgnoreCase("opt1"))
                            {
                                holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans1.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);
                            }
                            else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt2"))
                            {
                                holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans2.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);

                            }
                            else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt4"))
                            {
                                holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans4.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);

                            }
                        }

                        else if (answer.equalsIgnoreCase("opt4"))
                        {


                            holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_red);
                            holder.tvYourans4.setVisibility(View.VISIBLE);

                            holder.tvOpetion1.setEnabled(false);
                            holder.tvOpetion2.setEnabled(false);
                            holder.tvOpetion3.setEnabled(false);
                            holder.tvOpetion4.setEnabled(false);

                            if (dataArrayList.get(position).answer.equalsIgnoreCase("opt1"))
                            {
                                holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans1.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);
                            }
                            else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt2"))
                            {
                                holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans2.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);

                            }
                            else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt3"))
                            {
                                holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                                holder.ivRightans3.setVisibility(View.VISIBLE);

                                holder.tvOpetion1.setEnabled(false);
                                holder.tvOpetion2.setEnabled(false);
                                holder.tvOpetion3.setEnabled(false);
                                holder.tvOpetion4.setEnabled(false);

                            }
                        }





                    }




                }


            }


        }









        // to check right and wrong answer
        //click event for answer1
        holder.tvOpetion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt1"));

                if (dataArrayList.get(position).answer.equalsIgnoreCase("opt1")) {

                    holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                    holder.ivRightans1.setVisibility(View.VISIBLE);
                    holder.tvOpetion2.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);


                }
                else {

                    //bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt1"));

                    holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_red);
                    holder.tvYourans1.setVisibility(View.VISIBLE);

                    holder.tvOpetion2.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);


                    if (dataArrayList.get(position).answer.equalsIgnoreCase("opt2")) {

                        holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans2.setVisibility(View.VISIBLE);
                        //bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt2"));



                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt3")) {
                        holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans3.setVisibility(View.VISIBLE);
                        //bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt3"));


                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt4")) {
                        holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans4.setVisibility(View.VISIBLE);
                        //bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt4"));


                    }


                }

            }
        });


        holder.tvOpetion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt2"));

                if (dataArrayList.get(position).answer.equalsIgnoreCase("opt2")) {

                    holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_green);
                    holder.ivRightans2.setVisibility(View.VISIBLE);
                    holder.tvOpetion1.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);


                }
                else {
                    holder.tvOpetion2.setBackgroundResource(R.drawable.drawable_red);
                    holder.tvYourans2.setVisibility(View.VISIBLE);

                    holder.tvOpetion1.setEnabled(false);
                    holder.tvOpetion3.setEnabled(false);
                    holder.tvOpetion4.setEnabled(false);


                    if (dataArrayList.get(position).answer.equalsIgnoreCase("opt1")) {

                        holder.tvOpetion1.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans1.setVisibility(View.VISIBLE);
                        //bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt1"));


                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt3")) {
                        holder.tvOpetion3.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans3.setVisibility(View.VISIBLE);
                        //bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt3"));

                    } else if (dataArrayList.get(position).answer.equalsIgnoreCase("opt4")) {
                        holder.tvOpetion4.setBackgroundResource(R.drawable.drawable_green);
                        holder.ivRightans4.setVisibility(View.VISIBLE);
                        //bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt3"));

                    }


                }


            }
        });
//
//                                //click event for answer3
        holder.tvOpetion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt3"));

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

                bookList.add(new Selectedanswer(dataArrayList.get(position).id,"opt4"));

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

        if (dataArrayList.get(position).wishlistlike.equalsIgnoreCase("true")) {
            holder.ivQuesave.setImageResource(R.drawable.ic_save_done);
            holder.ivQueslike.setEnabled(false);
        } else {
            holder.ivQuesave.setImageResource(R.drawable.ic_newsave);
        }

        holder.llQuesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID).equalsIgnoreCase("")) {

//                    Intent intent = new Intent(context, NewLoginActivity.class);
//                    ((Activity) context).startActivityForResult(intent, 101);
                    onDeleteItemClickListener.onDeleteClicked();

                }
                else {

                    ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                    Call<Likeunlike> call = apiInterface.Likesave(PreferenceHelper.ReadSharePrefrence(context,Constant.USER_ID), dataArrayList.get(position).id, dataArrayList.get(position).type);
                    call.enqueue(new Callback<Likeunlike>() {
                        @Override
                        public void onResponse(Call<Likeunlike> call, Response<Likeunlike> response) {
                            //HelperClass.displayRequestAndResponse(response, null);

//                            HelperClass.dismissProgressDialog();
                            if (response.code() == 200) {
                                Likeunlike resultEntity = response.body();

                                if (resultEntity.status.equalsIgnoreCase("true")) {

                                    //click[0] = false;

                                    holder.ivQuesave.setImageResource(R.drawable.ic_save_done);
                                    holder.tvQuessavecount.setText(resultEntity.count);

                                    holder.saveList.add(new Selectedsave(dataArrayList.get(position).id,"true",resultEntity.count));



                                } else {
                                    //Toast.makeText(context, resultEntity.msg, Toast.LENGTH_SHORT).show();

                                    holder.ivQuesave.setImageResource(R.drawable.ic_newsave);
                                    holder.tvQuessavecount.setText(resultEntity.count);

                                    holder.saveList.add(new Selectedsave(dataArrayList.get(position).id,"false",resultEntity.count));


                                }


                            } else {

                                Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<Likeunlike> call, Throwable t) {
                            HelperClass.dismissProgressDialog();
                        }
                    });


//
                }

            }
        });


    }

    private void ads()
    {
        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-1792973056979970/6964528014")
                .forUnifiedNativeAd(unifiedNativeAd -> {
                    NativeTemplateStyle styles = new
                            NativeTemplateStyle.Builder().build();

                    adViews.setVisibility(View.VISIBLE);
                    adViews.setStyles(styles);
                    adViews.setNativeAd(unifiedNativeAd);
                    MediaView mediaView = (MediaView) adViews.findViewById(R.id.media_view);
                    mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY);
//
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        adViews.setVisibility(View.GONE);
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void refreshAd()
    {
        //refresh.setEnabled(false);
        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(R.string.ad_interstial));

//        AdRequest adRequest = new AdRequest.Builder().build();
////        adRequest = new AdRequest.Builder()
////                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
////                //.addTestDevice("8898c1508982c852")
////                .addTestDevice("7791b510d5088076")
////                .build();

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.

                if (nativeAd != null) {
                    nativeAd.destroy();
                }

                nativeAd = unifiedNativeAd;
                UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity) context).getLayoutInflater()
                        .inflate(R.layout.ad_unified, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);


                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(startVideoAdsMuted.isChecked())
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                //refresh.setEnabled(true);
                Toast.makeText(context, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        //adLoader.loadAds(new AdRequest.Builder().build(),NUMBER_OF_ADS);
        adLoader.loadAd(new AdRequest.Builder().build());

        //videoStatus.setText("");
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
        //adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        MediaView mediaView = (MediaView) adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null)
        {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        }
        else
            {

            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

//        if (nativeAd.getCallToAction() == null) {
////            adView.getCallToActionView().setVisibility(View.INVISIBLE);
////        } else {
////            adView.getCallToActionView().setVisibility(View.VISIBLE);
////            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
////        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY);
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
//            videoStatus.setText(String.format(Locale.getDefault(),
//                    "Video status: Ad contains a %.2f:1 video asset.",vc.getAspectRatio()));

            vc.getAspectRatio();

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    //refreshing or replacing them with another ad in the same UI location.
                    //refresh.setEnabled(true);
                    //videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        }

        else
            {
            //videoStatus.setText("Video status: Ad does not contain a video asset.");
            //refresh.setEnabled(true);
        }

    }


    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public interface OnDeleteItemClickListener {

        public void onDeleteClicked();

    }

    private void likeWatsapp(String userid, String id, String type) {
//        HelperClass.showProgressDialog(context, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<Likeunlike> call = apiInterface.watsapplike(userid, id, type);
        call.enqueue(new Callback<Likeunlike>() {
            @Override
            public void onResponse(Call<Likeunlike> call, Response<Likeunlike> response) {
                //HelperClass.displayRequestAndResponse(response, null);

                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    Likeunlike resultEntity = response.body();


                } else {

                    Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Likeunlike> call, Throwable t) {
                HelperClass.dismissProgressDialog();
            }
        });


    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {




        ////-------QUESTIONS--------
        RelativeLayout rlTitle;
        TextView tvTitle;
        TextView tvQuestions;
        JustifiedTextView tvQuestion;
        TextView tvOpetion1, tvOpetion2, tvOpetion3, tvOpetion4;



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

        private RelativeLayout rlQueslike;
        private LinearLayout llTemplate;

        ArrayList<Selectedlike> likeList = new ArrayList<Selectedlike>();
        ArrayList<Selectedsave> saveList = new ArrayList<Selectedsave>();
        ArrayList<Selectedcount> countList = new ArrayList<Selectedcount>();

        //private Boolean click = true;


        ViewHolder(View itemView) {
            super(itemView);

            adView = (AdView) itemView.findViewById(R.id.ad_view);
            adViews = itemView.findViewById(R.id.adviews);
            frameLayout = itemView.findViewById(R.id.fl_adplaceholder);
            startVideoAdsMuted = itemView.findViewById(R.id.cb_start_muted);
            llTemplate = itemView.findViewById(R.id.ll_template);

            rlTitle = itemView.findViewById(R.id.rl_title);


            tvQuestions = itemView.findViewById(R.id.tv_question);
            tvQuestion = itemView.findViewById(R.id.tv_que);
            tvOpetion1 = itemView.findViewById(R.id.tv_opetion1);
            tvOpetion2 = itemView.findViewById(R.id.tv_opetion2);
            tvOpetion3 = itemView.findViewById(R.id.tv_opetion3);
            tvOpetion4 = itemView.findViewById(R.id.tv_opetion4);





            rlQueslike = itemView.findViewById(R.id.rl_questionlike);



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




        }
    }
}
