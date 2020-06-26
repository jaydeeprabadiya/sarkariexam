package com.sarkarinaukri.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  Created by archirayan on 14/3/20.
    Author:www.archirayan.com
    Email:info@archirayan.com
 */
public class JobNews implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;


    @SerializedName("total pages")
    public int page;

    @SerializedName("data")
    public ArrayList<data> data;


    public class data implements Serializable {


        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("type")
        public String type;

        @SerializedName("file")
        public String file;

        @SerializedName("content")
        public String content;

        @SerializedName("opt1")
        public String opt1;

        @SerializedName("opt2")
        public String opt2;

        @SerializedName("opt3")
        public String opt3;

        @SerializedName("opt4")
        public String opt4;

        @SerializedName("answer")
        public String answer;

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


        @SerializedName("video_thumb")
        public String thumb;

    }
}
