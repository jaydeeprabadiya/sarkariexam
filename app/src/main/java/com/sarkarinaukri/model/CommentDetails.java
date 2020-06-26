package com.sarkarinaukri.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  Created by archirayan on 17/1/20.
    Author:www.archirayan.com
    Email:info@archirayan.com
 */
public class CommentDetails implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("Data")
    public ArrayList<data> data;

    public static class data implements Serializable {

        @SerializedName("id")
        public String id;

        @SerializedName("user_id")
        public String user_id;

        @SerializedName("comment")
        public String comment;

        @SerializedName("fullName")
        public String fullName;



    }


    }
