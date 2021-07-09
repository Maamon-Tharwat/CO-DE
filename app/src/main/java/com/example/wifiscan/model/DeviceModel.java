package com.example.wifiscan.model;

import java.io.Serializable;

public class DeviceModel implements Serializable {
    String id;
    String date;
    String latitude;
    String longitude;

    public DeviceModel(String id, String date, String latitude, String longitude) {
        this.id = id;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
