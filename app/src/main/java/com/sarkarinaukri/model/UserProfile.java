package com.sarkarinaukri.model;

import java.io.Serializable;

/**
 * Created by anuragkatiyar on 7/26/17.
 */

public class UserProfile implements Serializable {
    private String email;
    private String fullName;
    private String mobile;
    private String photo;
    private String thumbPhoto;
    private String token;
    private String userId;
    private String referalCode;
    private String walletAmount;
    private String signupMedium;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReferalCode() {
        return referalCode;
    }

    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }

    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }

    public String getSignupMedium() {
        return signupMedium;
    }

    public void setSignupMedium(String signupMedium) {
        this.signupMedium = signupMedium;
    }

    public enum SignUpMedium {
        email,
        fb,
        gmail
    }
}
