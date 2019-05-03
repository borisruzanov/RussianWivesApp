package com.borisruzanov.russianwives.models;

public class HotUser {

    private String uid, image;

    public HotUser(String uid, String image) {
        this.uid = uid;
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
