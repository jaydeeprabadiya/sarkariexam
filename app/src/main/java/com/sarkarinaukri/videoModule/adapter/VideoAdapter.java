package com.sarkarinaukri.videoModule.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.sarkarinaukri.R;
import com.sarkarinaukri.model.Newvideo;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter {

        Context context;
        ArrayList<Newvideo.data> videoItems;

        DataSource.Factory factory;
        //ProgressiveMediaSource.Factory mediaFactory;

        public VideoAdapter(Context context, ArrayList<Newvideo.data> videoItems) {
        this.context = context;
        this.videoItems = videoItems;
//        factory= new DefaultDataSourceFactory(context,"Ex90ExoPlayer"); // 매개 두번째는 임의로 그냥 적음
//        mediaFactory= new ProgressiveMediaSource.Factory(factory);
        }

@NonNull
@Override
public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent, false);
        VH holder= new VH(itemView);
        return holder;
        }

@Override
public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH)holder;

        Newvideo.data videoItem=videoItems.get(position);
        vh.tvTitle.setText(videoItem.title);
        vh.tvSubTitle.setText(videoItem.title);
        vh.tvDesc.setText(videoItem.title);

        //플레이어가 실행할 비디오데이터 소스 객체 생성( CD or LP ) 미디어 팩토리로부터..
        //ProgressiveMediaSource mediaSource= mediaFactory.createMediaSource(Uri.parse(videoItem.file));
    // Create Simple ExoPlayer
    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
    vh.player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

    //holder.playerView.setPlayer(player);
    vh.pv.setPlayer(vh.player);


    // Create RTMP Data Source
    RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();
    MediaSource videoSource = new ExtractorMediaSource
            .Factory(rtmpDataSourceFactory)
            .createMediaSource(Uri.parse("rtmp://188.165.25.224:1935/vod2/1587196839_image.mp4"));
    vh.player.prepare(videoSource);


    //위에서 만든 비디오 데이터 소스를 플레이어에게 로딩하도록....
        //vh.player.prepare(mediaSource);
//        vh.player.setPlayWhenReady(true);
        }

@Override
public int getItemCount() {
        return videoItems.size();
        }

//inner class..
class VH extends RecyclerView.ViewHolder {

    TextView tvTitle;
    TextView tvSubTitle;
    TextView tvDesc;

    PlayerView pv;
    SimpleExoPlayer player;

    public VH(@NonNull View itemView) {
        super(itemView);

        tvTitle = itemView.findViewById(R.id.tv_title);
        tvSubTitle = itemView.findViewById(R.id.tv_subtitle);
        tvDesc = itemView.findViewById(R.id.tv_desc);
        pv = itemView.findViewById(R.id.pv);






        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        pv.setPlayer(player);
    }
}
}
