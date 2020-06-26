package com.sarkarinaukri.model;

import java.util.List;

public class Home {
    private List<JobData> box_value;
    private List<JobData> hot_job;
    private List<JobData> top_online_form;
    private List<JobData> exam_result;
    private List<JobData> admit_card;
    private List<JobData> youtube;

    public List<JobData> getBox_value() {
        return box_value;
    }

    public void setBox_value(List<JobData> box_value) {
        this.box_value = box_value;
    }

    public List<JobData> getHot_job() {
        return hot_job;
    }

    public void setHot_job(List<JobData> hot_job) {
        this.hot_job = hot_job;
    }

    public List<JobData> getTop_online_form() {
        return top_online_form;
    }

    public void setTop_online_form(List<JobData> top_online_form) {
        this.top_online_form = top_online_form;
    }

    public List<JobData> getExam_result() {
        return exam_result;
    }

    public void setExam_result(List<JobData> exam_result) {
        this.exam_result = exam_result;
    }

    public List<JobData> getAdmit_card() {
        return admit_card;
    }

    public void setAdmit_card(List<JobData> admit_card) {
        this.admit_card = admit_card;
    }

    public List<JobData> getYoutube() {
        return youtube;
    }

    public void setYoutube(List<JobData> youtube) {
        this.youtube = youtube;
    }
}
