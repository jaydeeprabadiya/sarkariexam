package com.sarkarinaukri.model;
/**
 *  Created by archirayan on 5/3/20.
    Author:www.archirayan.com
    Email:info@archirayan.com
 */
public class Selectedcount   {

    String questionid,like,count;

    /* Cunstructor to store data */

    public Selectedcount(String questionid,String count)
    {

        this.questionid = questionid;
        this.count=count;

    }


    public String getQuestionid() {
        return questionid;
    }


    public String getCount()
    {
        return count;
    }


}
