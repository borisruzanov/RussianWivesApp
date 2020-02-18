package com.borisruzanov.russianwives.eventbus;

public class UpdateVersionEvent {
    boolean mIsNeedUpdate;

    public UpdateVersionEvent(boolean mResult) {
        this.mIsNeedUpdate = mResult;
    }

    public boolean isNeedUpdate() {
        return mIsNeedUpdate;
    }

    public void setIsNeedUpdate(boolean mResult) {
        this.mIsNeedUpdate = mResult;
    }
}
