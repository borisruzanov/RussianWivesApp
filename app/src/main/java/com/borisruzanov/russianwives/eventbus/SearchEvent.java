package com.borisruzanov.russianwives.eventbus;

public class SearchEvent {
    String mType;
    String mMessage;

    public SearchEvent(String mType, String mMessage) {
        this.mType = mType;
        this.mMessage = mMessage;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
