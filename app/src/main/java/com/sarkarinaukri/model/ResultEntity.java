package com.sarkarinaukri.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AppsMediaz Technologies on 7/26/2017.
 */
public class ResultEntity implements Serializable {
    //////////////////////// Notification Payload/////////////////////
    private String badge;
    private String alert;
    private String sound;
    private String vibrate;
    private String type;
    private String jobId;
    private String notificationCount;
    private QuestionData notificationQuestionData;

    private String code;
    private String status;
    private String message;
    private boolean like;
    private String likeCount;
    private String dislikeCount;
    private String walletAmount;
    private boolean follow;
    private UserProfile userData;
    private List<QuestionData> questionData = new ArrayList<>();
    private List<AnswerData> answerData = new ArrayList<>();
    private List<CategoriesData> categoryData = new ArrayList<>();
    private List<NotificationData> notificationList = new ArrayList<>();
    private String jobShareCount;
    private Home home;
    private List<JobData> admission_form;
    private List<JobData> top_online_form;
    private List<JobData> exam_result;
    private List<JobData> admit_card;
    private List<JobData> b_tech_m_tech;
    private List<JobData> offline_form;
    private List<JobData> syllabus;
    private List<JobData> diploma_iti;
    private List<JobData> answer_keys;

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getVibrate() {
        return vibrate;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(String notificationCount) {
        this.notificationCount = notificationCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(String dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public UserProfile getUserData() {
        return userData;
    }

    public void setUserData(UserProfile userData) {
        this.userData = userData;
    }

    public List<QuestionData> getQuestionData() {
        return questionData;
    }

    public void setQuestionData(List<QuestionData> questionData) {
        this.questionData = questionData;
    }

    public List<AnswerData> getAnswerData() {
        return answerData;
    }

    public void setAnswerData(List<AnswerData> answerData) {
        this.answerData = answerData;
    }

    public List<CategoriesData> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(List<CategoriesData> categoryData) {
        this.categoryData = categoryData;
    }


    public List<NotificationData> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationData> notificationList) {
        this.notificationList = notificationList;
    }


    public QuestionData getNotificationQuestionData() {
        return notificationQuestionData;
    }

    public void setNotificationQuestionData(QuestionData notificationQuestionData) {
        this.notificationQuestionData = notificationQuestionData;
    }

    public String getJobShareCount() {
        return jobShareCount;
    }

    public void setJobShareCount(String jobShareCount) {
        this.jobShareCount = jobShareCount;
    }


    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public List<JobData> getAdmission_form() {
        return admission_form;
    }

    public void setAdmission_form(List<JobData> admission_form) {
        this.admission_form = admission_form;
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

    public List<JobData> getB_tech_m_tech() {
        return b_tech_m_tech;
    }

    public void setB_tech_m_tech(List<JobData> b_tech_m_tech) {
        this.b_tech_m_tech = b_tech_m_tech;
    }

    public List<JobData> getOffline_form() {
        return offline_form;
    }

    public void setOffline_form(List<JobData> offline_form) {
        this.offline_form = offline_form;
    }

    public List<JobData> getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(List<JobData> syllabus) {
        this.syllabus = syllabus;
    }

    public List<JobData> getDiploma_iti() {
        return diploma_iti;
    }

    public void setDiploma_iti(List<JobData> diploma_iti) {
        this.diploma_iti = diploma_iti;
    }

    public List<JobData> getAnswer_keys() {
        return answer_keys;
    }

    public void setAnswer_keys(List<JobData> answer_keys) {
        this.answer_keys = answer_keys;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public enum status {
        invalid,
        success
    }
}
