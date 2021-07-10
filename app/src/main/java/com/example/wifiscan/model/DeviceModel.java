package com.example.wifiscan.model;

import java.io.Serializable;

public class DeviceModel implements Serializable {
    String id;
    String date;

    public DeviceModel(String id, String date) {
        this.id = id;
        this.date = date;

    }

    public DeviceModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}