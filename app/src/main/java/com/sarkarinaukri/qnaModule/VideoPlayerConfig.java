package com.sarkarinaukri.qnaModule;


public class VideoPlayerConfig   {

    //Minimum Video you want to buffer while Playing
    public static final int MIN_BUFFER_DURATION = 3000;
    //Max Video you want to buffer during PlayBack
    public static final int MAX_BUFFER_DURATION = 5000;
    //Min Video you want to buffer before start Playing it
    public static final int MIN_PLAYBACK_START_BUFFER = 1500;
    //Min video You want to buffer when user resumes video
    public static final int MIN_PLAYBACK_RESUME_BUFFER = 5000;


//    //Minimum Video you want to buffer while Playing
//    public static final int MIN_BUFFER_DURATION = 100;
//    //Max Video you want to buffer during PlayBack
//    public static final int MAX_BUFFER_DURATION = 1000;
//    //Min Video you want to buffer before start Playing it
//    public static final int MIN_PLAYBACK_START_BUFFER = 100;
//    //Min video You want to buffer when user resumes video
//    public static final int MIN_PLAYBACK_RESUME_BUFFER = 1000;

    public static final String DEFAULT_VIDEO_URL = "https://play.sarkariexam.com//admin/file_img_vid/youtube/483044_67_18%20jan%20tiktok%20english%20rachit.mp4";

}
