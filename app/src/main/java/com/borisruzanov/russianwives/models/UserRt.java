package com.borisruzanov.russianwives.models;

public class UserRt {

    private String uid;
    private String name;
    private String image;
    private String online;
    private String deviceToken;

    public UserRt() {
    }

    public UserRt(String uid, String name, String image, String online, String deviceToken) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.online = online;
        this.deviceToken = deviceToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
