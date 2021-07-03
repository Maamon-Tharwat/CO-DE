package com.example.wifiscan.model;

public class DonationModel {
    private String id;
    private String userID;
    private String userName;
    private String userImage;
    private String userPhone;
    private String description;
    private String medName;
    private String image;
    private int amount;
    private double price;

    public DonationModel(String id, String userID, String userName, String userImage, String userPhone, String description, String medName, String image, int amount, double price) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.userImage = userImage;
        this.userPhone = userPhone;
        this.description = description;
        this.medName = medName;
        this.image = image;
        this.amount = amount;
        this.price = price;
    }

    public DonationModel() {
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
