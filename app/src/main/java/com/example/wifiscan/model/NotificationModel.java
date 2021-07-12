package com.example.wifiscan.model;

public class NotificationModel {
    String date;
    String message = "We are sorry to inform you that you passed by an infected person on ";

    public NotificationModel() {
    }

    public NotificationModel(String date) {
        this.date = date;
        message = message.concat(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
