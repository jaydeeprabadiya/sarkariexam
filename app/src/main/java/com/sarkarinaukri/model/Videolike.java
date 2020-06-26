package com.sarkarinaukri.model;
/**
 *  Created by archirayan on 16/3/20.
    Author:www.archirayan.com
    Email:info@archirayan.com
 */
public class Videolike   {

    String questionid,like,count;

    /* Cunstructor to store data */

    public Videolike(String questionid,String like,String count)
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
