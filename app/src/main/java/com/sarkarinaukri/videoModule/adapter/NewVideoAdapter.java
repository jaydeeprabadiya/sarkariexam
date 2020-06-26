package com.sarkarinaukri.videoModule.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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
import com.sarkarinaukri.model.Newvideo;
import com.sarkarinaukri.model.Videolike;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class NewVideoAdapter extends RecyclerView.Adapter<NewVideoAdapter.ViewHolder> {

    private String TAG = "NewVideoAdapter";
    Context context;
    private List<Newvideo.data> dataArrayList;
    private static LayoutInflater mInflater;
    private ViewPager2 viewPager2;
    private CommentAdapter commentAdapter;
    OnDeleteItemClickListener onDeleteItemClickListener;
    OnloginClickListener onloginClickListener;
    Handler handler = new Handler();
    private int SPLASH_TIME_OUT = 60000;
    Integer pageno = 2;
    private LoadControl mLoadControl;

    public NewVideoAdapter(Context context, List<Newvideo.data> dataArrayList, ViewPager2 viewPager2, OnDeleteItemClickListener monDeleteItemClickedListener, OnloginClickListener onloginClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.viewPager2 = viewPager2;
        this.onDeleteItemClickListener = monDeleteItemClickedListener;
        this.onloginClickListener = onloginClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.page_video, parent, false);
        return new ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID).equalsIgnoreCase("")) {
//
//                    onloginClickListener.onloginClicked();
//                    handler.removeMessages(0);
//
//                }
//
//            }
//        }, SPLASH_TIME_OUT);
//        handler.removeCallbacks(null);

        if (dataArrayList.get(position) == dataArrayList.get(dataArrayList.size() - 5)) {

            onDeleteItemClickListener.onDeleteClicked(pageno);
            pageno++;

        } else {

        }

        Glide.with(context)
                .load(dataArrayList.get(position).videothumb)
                .skipMemoryCache(true)
                .override(500, 300)
                .into(holder.ivThumb);

        holder.ivPlay.setVisibility(View.GONE);
        holder.ivThumb.setVisibility(View.VISIBLE);
        //auto start playing





        if (dataArrayList.get(position).isSelected == true)
        {

//                    //initiate Player
//
                    //DefaultLoadControl loadControl = new DefaultLoadControl.Builder().setBufferDurationsMs(100, 64*1024, 100, 200).createDefaultLoadControl();
                    holder.progressBar.setVisibility(View.VISIBLE);
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                    holder.player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
                    //PlayerView playerView = findViewById(R.id.simple_player);


                    holder.playerView.setPlayer(holder.player);
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                            context, Util.getUserAgent(context, "sarkari"));

                    // Create RTMP Data Source
                    RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();

//                        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
//                                .createMediaSource(Uri.parse(mediaUrl));

                        MediaSource videoSource = new ExtractorMediaSource
                                .Factory(rtmpDataSourceFactory)
                                .createMediaSource(Uri.parse(dataArrayList.get(position).image));


                        // Prepare the player with the source.
                        holder.player.prepare(videoSource);
                        holder.player.seekTo(0);
                        holder.player.setPlayWhenReady(true);
                        holder.player.setRepeatMode(Player.REPEAT_MODE_ONE);


                        holder.player.addListener(new Player.DefaultEventListener() {
                            @Override
                            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                                Log.e(TAG, "onTimelineChanged: ");
                                //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                                Log.e(TAG, "onTracksChanged: ");
                                //Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();

                            }
                            @Override
                            public void onLoadingChanged(boolean isLoading) {
                                Log.e(TAG, "onLoadingChanged:");
                                //Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();


                            }
                            @Override
                            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                Log.e(TAG, "onPlayerStateChanged: ");
                                //Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();

                                switch (playbackState) {
                                    case Player.STATE_BUFFERING:
                                        //Player is in state State buffering show some loading progress
                                        //showProgress();
                                        //showProgress();
                                        //Toast.makeText(context, "5", Toast.LENGTH_SHORT).show();
                                        break;
                                    case Player.STATE_READY:
                                        //Player is ready to Play. Remove loading progress
                                        // hideProgress();
                                        //player.setPlayWhenReady(true);
                                        holder.ivThumb.setVisibility(View.INVISIBLE);
                                        holder.progressBar.setVisibility(View.GONE);
                                        //Toast.makeText(context, "6", Toast.LENGTH_SHORT).show();
                                        break;

                                    case Player.STATE_IDLE:
                                        //Player is ready to Play. Remove loading progress
                                        // hideProgress();
                                        //Toast.makeText(context, "7", Toast.LENGTH_SHORT).show();
                                        break;
                                    case Player.STATE_ENDED:
                                        //Player is ready to Play. Remove loading progress
                                        // hideProgress();
                                        holder.player.setRepeatMode(Player.REPEAT_MODE_ONE);
                                        //holder.progressBar.setVisibility(View.GONE);
                                        //Toast.makeText(context, "7", Toast.LENGTH_SHORT).show();
                                        break;

                                }

//
                            }
                            @Override
                            public void onRepeatModeChanged(int repeatMode) {
                                Log.e(TAG, "onRepeatModeChanged: ");
                                holder.player.setRepeatMode(Player.REPEAT_MODE_ALL);
                            }
                            @Override
                            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                                Log.e(TAG, "onShuffleModeEnabledChanged: ");

                            }
                            @Override
                            public void onPlayerError(ExoPlaybackException error) {
                                Log.e(TAG, "onPlayerError:");


                            }
                            @Override
                            public void onPositionDiscontinuity(int reason) {
                                Log.e(TAG, "onPositionDiscontinuity: ");

                            }
                            @Override
                            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                                Log.e(TAG, "onPlaybackParametersChanged: ");
                            }
                            @Override
                            public void onSeekProcessed() {
                                Log.e(TAG, "onSeekProcessed: ");
                            }
                        });

                }
                else
                {

                    if (holder.player != null) {
                        holder.player.setPlayWhenReady(false);
                        holder.player.release();
                        holder.player.stop();
                        holder.player.seekTo(0);
                        holder.player.setVolume(0f);
//                        player = null;
                        System.out.println("releasePlayer");
                        //Toast.makeText(context, "releasePlayer", Toast.LENGTH_SHORT).show();
                    }


        }


        holder.tvVideotitle.setText(dataArrayList.get(position).title);
        holder.videoframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.player.getPlayWhenReady()) {
                    holder.ivPlay.setVisibility(View.VISIBLE);
                    holder.player.setPlayWhenReady(false);
                } else {
                    holder.ivPlay.setVisibility(View.GONE);
                }

            }
        });

        holder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                holder.ivPlay.setVisibility(View.GONE);
                holder.player.setPlayWhenReady(true);


            }
        });

        //set like count and comment count and share count for artical and job
        holder.tvLikecount.setText(dataArrayList.get(position).likecount);
        holder.tvCommentcount.setText(dataArrayList.get(position).commentcount);
        holder.tvSharecount.setText(dataArrayList.get(position).sharecount);
        holder.tvSavecount.setText(dataArrayList.get(position).whishlistcount);
        holder.tvViewcount.setText(dataArrayList.get(position).viewcount);


        //like for articales and job
        if (dataArrayList.get(position).userlike.equalsIgnoreCase("true")) {

            holder.ivLike.setImageResource(R.drawable.heart_red);

        } else {
            holder.ivLike.setImageResource(R.drawable.ic_heart);
        }
//
        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID).equalsIgnoreCase("")) {

//                    Intent intent=new Intent(context, NewLoginActivity.class);
//                    ((Activity) context).startActivityForResult(intent, 101);
                    onloginClickListener.onloginClicked();

                } else {
                    //likeQuestiontype(PreferenceHelper.getUserId(QuestionCardActivity.this),dataArrayList.get(position).id,dataArrayList.get(position).type);
                    ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                    Call<Likeunlike> call = apiInterface.Likeunlike(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), dataArrayList.get(position).id, "videolink");
                    call.enqueue(new Callback<Likeunlike>() {
                        @Override
                        public void onResponse(Call<Likeunlike> call, Response<Likeunlike> response) {
                            //HelperClass.displayRequestAndResponse(response, null);

                            Likeunlike resultEntity = response.body();

                            if (resultEntity.status.equalsIgnoreCase("true")) {

                                holder.tvLikecount.setText(resultEntity.count);
                                holder.ivLike.setImageResource(R.drawable.heart_red);
                                holder.likeList.add(new Videolike(dataArrayList.get(position).id, "true", resultEntity.count));


                            } else {
                                holder.tvLikecount.setText(resultEntity.count);
                                holder.ivLike.setImageResource(R.drawable.ic_heart);
                                holder.likeList.add(new Videolike(dataArrayList.get(position).id, "false", resultEntity.count));


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


        if (holder.likeList.size() > 0) {

            for (int i = 0; i < holder.likeList.size(); i++) {

                if (holder.likeList.get(i).getQuestionid().equalsIgnoreCase(dataArrayList.get(position).id)) {

                    String answer = holder.likeList.get(i).getLike();
                    if (answer.equalsIgnoreCase("true")) {
                        holder.ivLike.setImageResource(R.drawable.heart_red);
                        holder.tvLikecount.setText(holder.likeList.get(i).getCount());

                    } else if (answer.equalsIgnoreCase("false")) {
                        holder.ivLike.setImageResource(R.drawable.ic_heart);
                        holder.tvLikecount.setText(holder.likeList.get(i).getCount());
                    }

                }
            }

        } else {

        }

        //set count for watsapp sharing and set gif image
        Glide.with(context)
                .load(R.drawable.ic_watsappgif)
                .asGif()
                .into(holder.ivWatsap);
        holder.ivWatsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, dataArrayList.get(position).title + " " + "*मुझे SarkariExam ऐप का इस्तेमाल करने में मजा आ रहा है। अभी डाउनलोड करें *\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47" + " - https://play.google.com/store/apps/details?id=" + context.getPackageName());
                try {

                    int likeCount = Integer.parseInt(holder.tvSharecount.getText().toString());
                    likeCount = likeCount + 1;
                    holder.tvSharecount.setText("" + likeCount);
                    likeWatsapp(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), dataArrayList.get(position).id, "videolink");
                    context.startActivity(whatsappIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
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
                    onloginClickListener.onloginClicked();


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
                    Call<CommentDetails> call = apiInterface.getComment(dataArrayList.get(position).id, "videolink");
                    call.enqueue(new Callback<CommentDetails>() {
                        @Override
                        public void onResponse(Call<CommentDetails> call, Response<CommentDetails> response) {
                            //HelperClass.displayRequestAndResponse(response, null);
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
                                Call<CommentDetails> call = apiInterface.sendComment(PreferenceHelper.ReadSharePrefrence(context, Constant.USER_ID), dataArrayList.get(position).id, "videolink", etAnswer.getText().toString());
                                call.enqueue(new Callback<CommentDetails>() {
                                    @Override
                                    public void onResponse(Call<CommentDetails> call, Response<CommentDetails> response) {
                                        //HelperClass.displayRequestAndResponse(response, null);
                                        CommentDetails resultEntity = response.body();


                                        if (resultEntity.status.equalsIgnoreCase("true")) {
                                            //Toast.makeText(getApplicationContext(), resultEntity.msg, Toast.LENGTH_SHORT).show();
                                            etAnswer.setText("");
                                            ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
                                            Call<CommentDetails> calls = apiInterface.getComment(dataArrayList.get(position).id, "videolink");
                                            calls.enqueue(new Callback<CommentDetails>() {
                                                @Override
                                                public void onResponse(Call<CommentDetails> calls, Response<CommentDetails> response) {
                                                    //HelperClass.displayRequestAndResponse(response, null);

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
                                                                HelperClass.dismissProgressDialog();
                                                            }
                                                        });


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
                                            Toast.makeText(context, resultEntity.msg, Toast.LENGTH_SHORT).show();
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

    }

//    @Override
//    public void onStpClicked() {
//
//        onCreateViewHolder().player.setPlayWhenReady(false);
//
//    }




    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    public interface OnDeleteItemClickListener {
        public void onDeleteClicked(int page);
    }

    public interface OnloginClickListener {
        public void onloginClicked();
    }



    private void likeWatsapp(String userid, String id, String type) {
        HelperClass.showProgressDialog(context, "");
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

        ////-------Articales and job--------
        private LinearLayout llImage;
        private RelativeLayout rlLike;
        private TextView tvLikecount;
        private TextView tvCommentcount;
        private TextView tvSharecount;
        private TextView tvSavecount;
        private TextView tvViewcount;

        ////-------Articales and job--------
        private ImageView ivLike;
        private ImageView ivWatsap;
        private ImageView ivComment;
        private ImageView ivSave;

        ////----------video-----------


        private ImageView ivPlay;
        private ImageView ivThumb;
        private TextView tvVideotitle;
        private VideoView video;
        private ProgressBar progressBar = null;
        private ImageView ivProgress;
        public ArrayList<Videolike> likeList = new ArrayList<Videolike>();
        public ConstraintLayout videoframe;
        public SimpleExoPlayer player;
        public PlayerView playerView;
        ////----------video-----------


        public ViewHolder(View itemView) {
            super(itemView);


            rlLike = itemView.findViewById(R.id.rl_like);

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


            //--------------video-------------
            videoframe = itemView.findViewById(R.id.video_frame);
            ivPlay = itemView.findViewById(R.id.play_button);
            video = itemView.findViewById(R.id.video);
            ivThumb = itemView.findViewById(R.id.iv_thumb);
            tvVideotitle = itemView.findViewById(R.id.tv_videotitle);
            progressBar = itemView.findViewById(R.id.progressbar);
            playerView = itemView.findViewById(R.id.simple_player);

        }
    }
}
