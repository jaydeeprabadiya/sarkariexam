package com.sarkarinaukri.model;
/**
 *  Created by archirayan on 4/3/20.
    Author:www.archirayan.com
    Email:info@archirayan.com
 */
public class Selectedlike   {


    String questionid,like,count;

    /* Cunstructor to store data */

    public Selectedlike(String questionid,String like,String count)
    {

        this.questionid = questionid;
        this.like=like;
        this.count=count;

    }


    public String getQuestionid() {
        return questionid;
    }

    public String getLike() {
        return like;
    }

    public String getCount()
    {
        return count;
    }


}
