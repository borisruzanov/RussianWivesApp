package com.borisruzanov.russianwives.eventbus;

public class BooleanEvent {
    boolean mResult;

    public BooleanEvent(boolean mResult) {
        this.mResult = mResult;
    }

    public boolean ismResult() {
        return mResult;
    }

    public void setmResult(boolean mResult) {
        this.mResult = mResult;
    }
}
