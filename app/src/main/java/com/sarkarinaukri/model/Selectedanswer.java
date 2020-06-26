package com.sarkarinaukri.model;
/**
 *  Created by archirayan on 3/3/20.
    Author:www.archirayan.com
    Email:info@archirayan.com
 */
public class Selectedanswer   {


        String questionid,rightanswer,wronganswer;

        /* Cunstructor to store data */

        public Selectedanswer(String questionid,String rightanswer)
        {

            this.questionid = questionid;
            this.rightanswer=rightanswer;

        }


    public String getQuestionid() {
        return questionid;
    }

    public String getRightanswer() {
        return rightanswer;
    }




}
