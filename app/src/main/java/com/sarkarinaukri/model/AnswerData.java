package com.sarkarinaukri.model;

import java.io.Serializable;

/**
 * Created by anuragkatiyar on 7/26/17.
 */

public class AnswerData implements Serializable {
    private String answerId;
    private String questionId;
    private String answer;
    private String userId;
    private String likeCount;
    private String dislikeCount;
    private String status;
    private String createdDate;
    private String userQuestionCount;
    private String userAnswerCount;
    private String followerCount;
    private String fullName;
    private String photo;
    private String followingCount;
    private String voteCount;
    private String favouriteCount;
    private boolean userFollow;
    private QuestionData questionData;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserQuestionCount() {
        return userQuestionCount;
    }

    public void setUserQuestionCount(String userQuestionCount) {
        this.userQuestionCount = userQuestionCount;
    }

    public String getUserAnswerCount() {
        return userAnswerCount;
    }

    public void setUserAnswerCount(String userAnswerCount) {
        this.userAnswerCount = userAnswerCount;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
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

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(String favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public boolean isUserFollow() {
        return userFollow;
    }

    public void setUserFollow(boolean userFollow) {
        this.userFollow = userFollow;
    }

    public QuestionData getQuestionData() {
        return questionData;
    }

    public void setQuestionData(QuestionData questionData) {
        this.questionData = questionData;
    }
}
