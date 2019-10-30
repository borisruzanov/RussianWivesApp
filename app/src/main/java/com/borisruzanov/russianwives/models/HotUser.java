package com.borisruzanov.russianwives.models;

public class HotUser {

    private String uid, image, timestamp, reversedTimestamp;

    public HotUser() {}

    public HotUser(String uid, String image, String timestamp, String reversedTimestamp) {
        this.uid = uid;
        this.image = image;
        this.timestamp = timestamp;
        this.reversedTimestamp = reversedTimestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReversedTimestamp() {
        return reversedTimestamp;
    }

    public void setReversedTimestamp(String reversedTimestamp) {
        this.reversedTimestamp = reversedTimestamp;
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
