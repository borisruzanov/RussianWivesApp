package com.borisruzanov.russianwives.eventbus;

public class ChatEvent {
    String mName;
    String mUid;
    String mImage;

    public ChatEvent(String mName, String mUid, String mImage) {
        this.mName = mName;
        this.mUid = mUid;
        this.mImage = mImage;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
