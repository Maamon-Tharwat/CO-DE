package com.example.wifiscan.model;

import java.io.Serializable;

public class PostModel implements Serializable {
    private String id;
    private String userID;
    private String userName;
    private String userImage;
    private String title;
    private String description;
    private String date;
    private String[] imageUrl;

    public PostModel(String id, String userID, String userName, String userImage, String title, String description, String date, String[] imageUrl) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.userImage = userImage;
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PostModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
