package com.borisruzanov.russianwives.models;

public class Action {
    private String action;
    private String uid;
    private long timeStamp;

    public Action(String action) {
        this.action = action;
    }

    public Action(String action, String uid, long timeStamp) {
        this.action = action;
        this.uid = uid;
        this.timeStamp = timeStamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
