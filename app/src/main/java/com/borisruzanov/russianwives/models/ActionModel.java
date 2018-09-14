package com.borisruzanov.russianwives.models;

public class ActionModel {

    private long timestamp;
    String type, uid;

    public ActionModel(long timestamp, String uid, String type) {
        this.timestamp = timestamp;
        this.uid = uid;
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
