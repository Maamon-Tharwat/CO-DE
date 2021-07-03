package com.example.wifiscan.model;


public class CommentModel {
    private String id;
    private String comment;
    private String userID;
    private String userName;
    private String userImage;
    private String postID;
    private String date;

    public CommentModel(String id, String comment, String userID, String userName, String userImage, String postID) {
        this.id = id;
        this.comment = comment;
        this.userID = userID;
        this.userName = userName;
        this.userImage = userImage;
        this.postID = postID;
    }

    public CommentModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
