package com.borisruzanov.russianwives.models;

public class ActivitiesListItem {
    String image;
    String name;
    String activity;
    long timeStamp;

    public ActivitiesListItem(String name, String activity, long timeStamp, String image) {
        this.name = name;
        this.activity = activity;
        this.timeStamp = timeStamp;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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
