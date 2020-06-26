package com.sarkarinaukri.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anuragkatiyar on 7/26/17.
 */

public class QuestionData implements Serializable {
    private String questionId;
    private String question;
    private String userId;
    private String categoryId;
    private String viewCount;
    private String answerCount;
    private String userAnswerCount;
    private String userQuestionCount;
    private int shareCount;
    private String status;
    private String updatedDate;
    private String createdDate;
    private String fullName;
    private String photo;
    private String questionCount;
    private String followerCount;
    private String followingCount;
    private String voteCount;
    private String favouriteCount;
    private String questionImage;
    private String rightOption;
    private String givenOption;
    private List<Option> options;
    private boolean follow;
    private boolean userFollow;
    private boolean like;
    private int likeCount;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(String answerCount) {
        this.answerCount = answerCount;
    }

    public String getUserAnswerCount() {
        return userAnswerCount;
    }

    public void setUserAnswerCount(String userAnswerCount) {
        this.userAnswerCount = userAnswerCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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

    public String getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(String questionCount) {
        this.questionCount = questionCount;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
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

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public String getUserQuestionCount() {
        return userQuestionCount;
    }

    public void setUserQuestionCount(String userQuestionCount) {
        this.userQuestionCount = userQuestionCount;
    }

    public boolean isUserFollow() {
        return userFollow;
    }

    public void setUserFollow(boolean userFollow) {
        this.userFollow = userFollow;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getRightOption() {
        return rightOption;
    }

    public void setRightOption(String rightOption) {
        this.rightOption = rightOption;
    }

    public String getGivenOption() {
        return givenOption;
    }

    public void setGivenOption(String givenOption) {
        this.givenOption = givenOption;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
