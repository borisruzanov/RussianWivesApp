package com.borisruzanov.russianwives.models;

public class ActionItem {
    private String image;
    private String name;
    private String action;
    private long timeStamp;

    public ActionItem(String name, String action, long timeStamp, String image) {
        this.name = name;
        this.action = action;
        this.timeStamp = timeStamp;
        this.image = image;
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
