package com.borisruzanov.russianwives.models;

public class ActionItem {
    private String uid, name, image, action;
    private long timeStamp;

    public ActionItem(String uid, String name, String image, String action, long timeStamp) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.action = action;
        this.timeStamp = timeStamp;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
