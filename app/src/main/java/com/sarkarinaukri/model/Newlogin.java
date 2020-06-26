package com.sarkarinaukri.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Newlogin implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("data")
    public data data;

    public class data implements Serializable {

        @SerializedName("userId")
        public String id;

        @SerializedName("fullName")
        public String fullName;

        @SerializedName("email")
        public String email;

        @SerializedName("mobile")
        public String mobile;

    }
}
