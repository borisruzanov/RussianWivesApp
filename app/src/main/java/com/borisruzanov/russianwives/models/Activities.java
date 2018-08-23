package com.borisruzanov.russianwives.models;

public class Activities {
    String activity;
    String uid;
    long timeStamp;

    public Activities(String activity, String uid, long timeStamp) {
        this.activity = activity;
        this.uid = uid;
        this.timeStamp = timeStamp;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
