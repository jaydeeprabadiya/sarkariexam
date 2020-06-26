package com.sarkarinaukri.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Newvideo implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("data")
    public ArrayList<data> data;

    public class data implements Serializable {

        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("type")
        public String type;

        @SerializedName("sticky")
        public String sticky;

        @SerializedName("image")
        public String image;

        @SerializedName("video_thumb")
        public String videothumb;

        @SerializedName("likecount")
        public String likecount;

        @SerializedName("commentcount")
        public String commentcount;

        @SerializedName("userlike")
        public String userlike;

        @SerializedName("sharecount")
        public String sharecount;

        @SerializedName("Whishlistcount")
        public String whishlistcount;

        @SerializedName("wishlist_like")
        public String wishlistlike;

        @SerializedName("incriment")
        public String viewcount;

        public boolean isSelected=false;

    }
}
