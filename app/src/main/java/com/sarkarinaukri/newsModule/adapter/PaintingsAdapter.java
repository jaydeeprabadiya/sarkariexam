package com.sarkarinaukri.newsModule.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.alexvasilkov.android.commons.ui.Views;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
import com.facebook.appevents.ml.Utils;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sarkarinaukri.BuildConfig;
import com.sarkarinaukri.MainActivity;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.RetroFit.ApiclientArchi;
import com.sarkarinaukri.newsModule.CromcastActivity;
import com.sarkarinaukri.saveModule.SaveNewActivity;
import com.sarkarinaukri.adapter.CommentAdapter;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.model.CommentDetails;
import com.sarkarinaukri.model.Commentcount;
import com.sarkarinaukri.model.Likeunlike;
import com.sarkarinaukri.model.QuestionList;
import com.sarkarinaukri.qnaModule.NewQuestionActivity;
import com.sarkarinaukri.newsModule.QuestionCardActivity;
import com.sarkarinaukri.qnaModule.VideoActivity;
import com.sarkarinaukri.util.ActivitySplitAnimationUtil;
import com.sarkarinaukri.util.GlideHelper;
import com.sarkarinaukri.util.ScreenshotUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alexvasilkov.android.commons.nav.ExternalIntents.isAppInstalled;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.sarkarinaukri.SarkariExam.getContext;
import static com.sarkarinaukri.helperClass.AdHandler.mInterstitialAd;

public class PaintingsAdapter extends ItemsAdapter<QuestionList.data, PaintingsAdapter.ViewHolder> {

    private List<QuestionList.data> dataArrayList;
    Integer pageno = 2;
    Context context;
    private CommentAdapter commentAdapter;
    private int SPLASH_TIME_OUT = 60000;
    OnDeleteItemClickListener onDeleteItemClickListener;
    OnRefereshClickListener onRefereshClickListener;
    Handler handler = new Handler();
    TemplateView adViews;
    private InterstitialAd mInterstitialAd;
    FileOutputStream out;
    Bitmap bmps;
    boolean success = false;

    public PaintingsAdapter(Context context, List<QuestionList.data> dataArrayList, OnDeleteItemClickListener monDeleteItemClickedListener, OnRefereshClickListener onRefereshClickListener) {
        this.dataArrayList = dataArrayList;
        this.context = context;
        this.onDeleteItemClickListener = monDeleteItemClickedListener;
        this.onRefereshClickListener = onRefereshClickListener;
        setItemsList(dataArrayList);

    }

    @Override
    protected ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(parent);
        return holder;
    }


    @Override
    protected void onBindHolder(ViewHolder holder, int position) {
        QuestionList.data item = getItem(position);

        Typeface face = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
        Typeface face1 = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//
                if (PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID).equalsIgnoreCase("")) {
                    onDeleteItemClickListener.onDeleteClicked();
                    //Toast.makeText(context, "login news", Toast.LENGTH_SHORT).show();
                    handler.removeMessages(0);
                }

            }
        }, SPLASH_TIME_OUT);

        handler.removeCallbacks(null);

//        ads();
//
//        if (position>0) {
//
//            if (position % 5 == 0) {
//                //TODO
//                holder.llTemplate.setVisibility(View.VISIBLE);
//                //Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();
//            } else {
//                //TODO
//                holder.llTemplate.setVisibility(View.INVISIBLE);
//            }
//        }


        ///api calling every 7 position
        if (dataArrayList.get(position) == dataArrayList.get(dataArrayList.size() - 3)) {
            ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
            Call<QuestionList> call = apiInterface.getdatapagination(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), String.valueOf(pageno));
            call.enqueue(new Callback<QuestionList>() {
                @Override
                public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                    //HelperClass.displayRequestAndResponse(response, null);
                    if (response.code() == 200) {

                        QuestionList resultEntity = response.body();

                        if (resultEntity.status.equalsIgnoreCase("true")) {
                            dataArrayList.addAll(resultEntity.data);
                            pageno++;
                            notifyDataSetChanged();
                            Log.e("TAG", "DATARRYLIST SIZE==>" + dataArrayList.size());
                            Log.e("TAG", "PAGE_NO==>" + pageno);
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


//        holder.image.setTag(R.id.list_item_image, item);
        //     GlideHelper.loadPaintingImage(holder.image, item);

        if (dataArrayList.get(position).type.equalsIgnoreCase("job")) {
            holder.tvMore.setVisibility(View.VISIBLE);

        }
//

        //full screen
        if (dataArrayList.get(position).isFull.equalsIgnoreCase("1")) {
            holder.rlContent.setVisibility(View.GONE);
            holder.flImg.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(dataArrayList.get(position).file)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.ivFull);

        } else {
            holder.rlContent.setVisibility(View.VISIBLE);
            holder.flImg.setVisibility(View.GONE);

            Glide.with(context)
                    .load(dataArrayList.get(position).file)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.image);

            holder.title.setText(dataArrayList.get(position).title);
            holder.tvDescriptin.setText(dataArrayList.get(position).content);
        }

        //holder.title.setTypeface(face);
        holder.tvDescriptin.setTypeface(face1);


        holder.tvDescriptin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    final BottomSheetDialog dialog = new BottomSheetDialog(context);
                    dialog.setContentView(R.layout.bottom_menu);
                    dialog.getWindow().setDimAmount(0.0f);

                    LinearLayout llHome = dialog.findViewById(R.id.llHome);
                    LinearLayout llSave = dialog.findViewById(R.id.ll_save);

                    llHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    });

                    llSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(context, SaveNewActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    });

                    LinearLayout llNews = dialog.findViewById(R.id.ll_news);
                    llNews.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(context, QuestionCardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            ((Activity) context).finish();

                        }
                    });

                    RelativeLayout rlGk = dialog.findViewById(R.id.rl_gk);
                    rlGk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(context, NewQuestionActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    });

                    RelativeLayout rlPlay = dialog.findViewById(R.id.rl_play);
                    rlPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(context, VideoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                    });
                    dialog.show();

                } catch (Exception e) {

                }

            }
        });

        //like for articales and job
        if (dataArrayList.get(position).userlike.equalsIgnoreCase("true")) {
            holder.ivLike.setImageResource(R.drawable.heart_red);
        } else {
            holder.ivLike.setImageResource(R.drawable.ic_like_gray);
        }

        try {

            //set count for watsapp sharing and set gif image
            Glide.with(context)
                    .load(R.drawable.ic_watsappgif)
                    .asGif()
                    .into(holder.ivWatsap);

            //set count for watsapp sharing and set gif image
            Glide.with(context)
                    .load(R.drawable.ic_watsappgif)
                    .asGif()
                    .into(holder.ivWatsapshare);
        } catch (Exception e) {

        }


        holder.ivWatsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, dataArrayList.get(position).title + "\n"
                        + " *मुझे " +
                        "SarkariExam ऐप का इस्तेमाल करने में मजा आ रहा है। अभी डाउनलोड करें*" +
                        " \uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47" + " - https://play.google.com/store/apps/details?id=" + context.getPackageName());
                try {

                    int likeCount = Integer.parseInt(holder.tvSharecount.getText().toString());
                    likeCount = likeCount + 1;
                    holder.tvSharecount.setText("" + likeCount);
                    likeWatsapp(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), dataArrayList.get(position).id, dataArrayList.get(position).type);
                    context.startActivity(whatsappIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.tvLikecount.setText(dataArrayList.get(position).likecount);
        holder.tvCommentcount.setText(dataArrayList.get(position).commentcount);
        holder.tvSharecount.setText(dataArrayList.get(position).sharecount);
        holder.tvSharecounts.setText(dataArrayList.get(position).sharecount);

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID).equalsIgnoreCase("")) {

                    onDeleteItemClickListener.onDeleteClicked();

                } else {
                    //HelperClass.showProgressDialog(context, "");
                    ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                    Call<Likeunlike> call = apiInterface.Likeunlike(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), dataArrayList.get(position).id, dataArrayList.get(position).type);
                    call.enqueue(new Callback<Likeunlike>() {
                        @Override
                        public void onResponse(Call<Likeunlike> call, Response<Likeunlike> response) {
                            //HelperClass.displayRequestAndResponse(response, null);

//                            HelperClass.dismissProgressDialog();
                            if (response.code() == 200) {
                                Likeunlike resultEntity = response.body();

                                if (resultEntity.status.equalsIgnoreCase("true")) {

                                    holder.tvLikecount.setText(resultEntity.count);
                                    holder.ivLike.setImageResource(R.drawable.heart_red);


                                } else {
                                    holder.tvLikecount.setText(resultEntity.count);
                                    holder.ivLike.setImageResource(R.drawable.ic_like_gray);

                                }
                            } else {

                                Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<Likeunlike> call, Throwable t) {
                        }
                    });


                }

            }
        });

        //click event for comment job and articales etc
        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID).equalsIgnoreCase("")) {

//                    Intent intent = new Intent(context, NewLoginActivity.class);
//                    ((Activity) context).startActivityForResult(intent, 101);
                    onDeleteItemClickListener.onDeleteClicked();


                } else {


                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.comment_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();


                    dialog.getWindow().setGravity(Gravity.BOTTOM);

                    final RecyclerView rvComment = dialog.findViewById(R.id.rv_comment);
                    final EditText etAnswer;
                    final LinearLayout llSend;


                    ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                    Call<CommentDetails> call = apiInterface.getComment(dataArrayList.get(position).id, dataArrayList.get(position).type);
                    call.enqueue(new Callback<CommentDetails>() {
                        @Override
                        public void onResponse(Call<CommentDetails> call, Response<CommentDetails> response) {
                            //HelperClass.displayRequestAndResponse(response, null);

//                        HelperClass.dismissProgressDialog();
                            if (response.code() == 200) {
                                CommentDetails resultEntity = response.body();

                                if (resultEntity.status.equalsIgnoreCase("true")) {
                                    //Toast.makeText(getApplicationContext(), resultEntity.msg, Toast.LENGTH_SHORT).show();
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    rvComment.setLayoutManager(layoutManager);
                                    commentAdapter = new CommentAdapter(context, resultEntity.data);
                                    rvComment.setAdapter(commentAdapter);


                                } else {

                                }


                            } else {

                                Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<CommentDetails> call, Throwable t) {
//                        HelperClass.dismissProgressDialog();
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
                                Call<CommentDetails> call = apiInterface.sendComment(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), dataArrayList.get(position).id, dataArrayList.get(position).type, etAnswer.getText().toString());
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


                                                                //getCommentscount(dataArrayList.get(position).id,dataArrayList.get(position).type);

                                                                ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                                                                Call<Commentcount> call = apiInterface.getCommentcount(dataArrayList.get(position).id, dataArrayList.get(position).type);
                                                                call.enqueue(new Callback<Commentcount>() {
                                                                    @Override
                                                                    public void onResponse(Call<Commentcount> call, Response<Commentcount> response) {
                                                                        //HelperClass.displayRequestAndResponse(response, null);

                                                                        if (response.code() == 200) {
                                                                            Commentcount resultEntity = response.body();

                                                                            holder.tvCommentcount.setText(resultEntity.data);


                                                                        } else {

                                                                            Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                                                                        }


                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<Commentcount> call, Throwable t) {
//                                                                    HelperClass.dismissProgressDialog();
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
//                                                    HelperClass.dismissProgressDialog();
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
                                    public void onFailure(Call<CommentDetails> call, Throwable t) {
//                                    HelperClass.dismissProgressDialog();
                                    }
                                });


                            }


                        }
                    });


                }

            }
        });

        holder.llReferesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRefereshClickListener.onRefereshClicked();

            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataArrayList.get(position).link.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please Try again Some time ", Toast.LENGTH_LONG).show();

                } else {
                    ActivitySplitAnimationUtil.startActivity((Activity) context, new Intent(context, CromcastActivity.class));
                    PreferenceHelper.WriteSharePrefrence(context, Constant.URL, dataArrayList.get(position).link);
                    PreferenceHelper.WriteSharePrefrence(context, Constant.TITLE, dataArrayList.get(position).title);
                    PreferenceHelper.WriteSharePrefrence(context, Constant.ADS, "true");
                    //HelperClass.openUrlInChromeCustomTab(context, dataArrayList.get(position).link);
                }

            }
        });


        //to click news details to open webview
        //click event for more details about news
        holder.ivWatsapshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int likeCount = Integer.parseInt(holder.tvSharecount.getText().toString());
                likeCount = likeCount + 1;
                holder.tvSharecounts.setText("" + likeCount);
                likeWatsapp(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), dataArrayList.get(position).id, dataArrayList.get(position).type);
                //shareImage(dataArrayList.get(position).file);
                bmps = ScreenshotUtils.getScreenShot(holder.ivWatsapshare);

                //If bitmap is not null
                if (bmps != null) {
                    File saveFile = ScreenshotUtils.getMainDirectoryName(context);//get the path to save screenshot
                    File file = ScreenshotUtils.store(bmps, "screenshot" + "fukk" + ".png", saveFile);//save the screenshot to selected path
                    shareScreenshot(file);//finally share screenshot
                } else {
                    //If bitmap is null show toast message
                    Toast.makeText(context, "faijj", Toast.LENGTH_SHORT).show();

                }


                Picasso.get().load(dataArrayList.get(position).file).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        Intent i = new Intent(Intent.ACTION_SEND);
//                        i.setType("image/*");
//                        i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
//                        context.startActivity(Intent.createChooser(i, "Share Image"));


                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });

//
//                Uri imageUri = Uri.parse(dataArrayList.get(position).file);
//                //shareOnWhatsapp("HHEEE",imageUri);
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                //Target whatsapp:
//                shareIntent.setPackage("com.whatsapp");
//                //Add text and then Image URI
//                shareIntent.putExtra(Intent.EXTRA_TEXT, "fgfgfgfgf");
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                shareIntent.setType("image/*");
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                try {
//                    context.startActivity(shareIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
//                }


            }

        });


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

    }

    /*  Share Screenshot  */
    private void shareScreenshot(File file) {
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        //Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.setPackage("com.whatsapp");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.sarkarinaukri");
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        context.startActivity(Intent.createChooser(intent, ""));

//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        //Target whatsapp:
//        shareIntent.setPackage("com.whatsapp");
//        //Add text and then Image URI
//        shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.app_ur_test);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        shareIntent.setType("image/png");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        try {
//            context.startActivity(shareIntent);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
//        }


    }

    //////// this method share your image
    private void shareBitmap(Bitmap bitmap, String fileName) {
        try {
            File file = new File(getContext().getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public interface OnDeleteItemClickListener {

        public void onDeleteClicked();

    }

    public interface OnRefereshClickListener {

        public void onRefereshClicked();

    }


    private void likeWatsapp(String userid, String id, String type) {
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<Likeunlike> call = apiInterface.watsapplike(userid, id, type);
        call.enqueue(new Callback<Likeunlike>() {
            @Override
            public void onResponse(Call<Likeunlike> call, Response<Likeunlike> response) {
                if (response.code() == 200) {
                    Likeunlike resultEntity = response.body();


                } else {

                    Toast.makeText(context, "FAIL", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Likeunlike> call, Throwable t) {
//                HelperClass.dismissProgressDialog();
            }
        });
    }

    private void ads() {

        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-1792973056979970/6964528014")
                .forUnifiedNativeAd(unifiedNativeAd -> {
                    NativeTemplateStyle styles = new
                            NativeTemplateStyle.Builder().build();

                    adViews.setVisibility(View.VISIBLE);
                    adViews.setStyles(styles);
                    adViews.setNativeAd(unifiedNativeAd);
                    MediaView mediaView = (MediaView) adViews.findViewById(R.id.media_view);
                    mediaView.setImageScaleType(ImageView.ScaleType.FIT_XY);

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


    class ViewHolder extends ItemsAdapter.ViewHolder {
        private ImageView image;
        private TextView title;
        private TextView tvDescriptin;
        private TextView tvLikecount;
        private TextView tvCommentcount;
        private TextView tvSharecount;
        private ImageView ivWatsap;
        private ImageView ivWatsapshare;
        private ImageView ivLike;
        private ImageView ivComment;
        private TextView tvMore;
        private LinearLayout llTemplate;
        private RelativeLayout rlContent;
        private RelativeLayout flImg;
        private ImageView ivFull;
        private ImageView ivnwats;
        private LinearLayout llReferesh;
        private TextView tvSharecounts;

        ViewHolder(ViewGroup parent) {
            super(Views.inflate(parent, R.layout.list_item));
            image = Views.find(itemView, R.id.list_item_image);
            title = Views.find(itemView, R.id.tv_description);
            tvDescriptin = Views.find(itemView, R.id.tv_content);
            tvLikecount = Views.find(itemView, R.id.tvLikeCount);
            tvCommentcount = Views.find(itemView, R.id.tvCommentCount);
            tvSharecount = itemView.findViewById(R.id.tvShareCount);
            ivWatsap = itemView.findViewById(R.id.iv_wastapp);
            ivWatsapshare = itemView.findViewById(R.id.ic_share);
            ivLike = itemView.findViewById(R.id.ivLikeIcon);
            ivComment = itemView.findViewById(R.id.ivComment);
            tvMore = itemView.findViewById(R.id.tv_clickmore);
            llTemplate = itemView.findViewById(R.id.ll_template);
            adViews = itemView.findViewById(R.id.adviews);
            rlContent = itemView.findViewById(R.id.rl_content);
            flImg = itemView.findViewById(R.id.fl_img);
            ivFull = itemView.findViewById(R.id.ic_fullimg);
            llReferesh = itemView.findViewById(R.id.ll_referesh);
            tvSharecounts = itemView.findViewById(R.id.ic_sharecount);

        }
    }

}
