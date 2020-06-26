package com.sarkarinaukri.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Corona implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("stateKarona")
    public ArrayList<data> data;

    public class data implements Serializable {

        @SerializedName("id")
        public String id;

        @SerializedName("state")
        public String state;

        @SerializedName("slogan")
        public String slogan;

        @SerializedName("confirmed")
        public String confirmed;

        @SerializedName("active")
        public String active;

        @SerializedName("recovered")
        public String recovered;

        @SerializedName("deaths")
        public String deaths;

    }

    @SerializedName("karona")
    public karona karona;

    public class karona implements Serializable
    {

        @SerializedName("id")
        public String id;

        @SerializedName("total_confirmed")
        public String totalconfirmed;

        @SerializedName("total_active")
        public String totalactive;

        @SerializedName("total_recovered")
        public String totalrecovered;

        @SerializedName("total_deaths")
        public String totaldeaths;

        @SerializedName("flag")
        public String flag;


    }


}
