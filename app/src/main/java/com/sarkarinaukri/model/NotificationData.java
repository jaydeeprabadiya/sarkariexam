package com.sarkarinaukri.model;

import java.io.Serializable;

/**
 * Created by AppsMediaz Technologies on 04/08/2017.
 */
public class NotificationData implements Serializable {
    private String jobId;
    private String type;
    private String createdDate;
    private String jobTitle;
    private String testName;
    private String message;
    private String userId;
    private String fullName;
    private String photo;
    private String thumbPhoto;
    private QuestionData notificationQuestionData;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumbPhoto() {
        return thumbPhoto;
    }

    public void setThumbPhoto(String thumbPhoto) {
        this.thumbPhoto = thumbPhoto;
    }

    public QuestionData getNotificationQuestionData() {
        return notificationQuestionData;
    }

    public void setNotificationQuestionData(QuestionData notificationQuestionData) {
        this.notificationQuestionData = notificationQuestionData;
    }
}
