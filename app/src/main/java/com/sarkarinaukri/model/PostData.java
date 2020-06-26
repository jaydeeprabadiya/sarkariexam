package com.sarkarinaukri.model;

public class PostData {
    private String id;
    private String postId;
    private String title;
    private String postUrl;

    public PostData(String title, String postUrl) {
        this.title = title;
        this.postUrl = postUrl;
    }

    public PostData(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }
}
