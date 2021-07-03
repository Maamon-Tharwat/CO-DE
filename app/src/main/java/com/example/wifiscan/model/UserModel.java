package com.example.wifiscan.model;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String id;
    private String name;
    private String email;
    private String address;
    private String nationalID;
    private String job;
    private String phone;
    private String image;
    private String password;
    private String type;
    private boolean infected=false;


    public UserModel(String id, String name, String email, String address, String nationalID, String job, String phone, String image, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.nationalID = nationalID;
        this.job = job;
        this.phone = phone;
        this.image = image;
        this.password = password;
    }

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }
}
