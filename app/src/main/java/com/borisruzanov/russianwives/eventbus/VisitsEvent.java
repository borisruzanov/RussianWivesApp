package com.borisruzanov.russianwives.eventbus;

public class VisitsEvent {
    long mVisits;
    long mLikes;

    public long getmLikes() {
        return mLikes;
    }

    public void setmLikes(long mLikes) {
        this.mLikes = mLikes;
    }

    public VisitsEvent(long mVisits, long mLikes) {
        this.mVisits = mVisits;
        this.mLikes = mLikes;
    }

    public long getmVisits() {
        return mVisits;
    }

    public void setmVisits(long mVisits) {
        this.mVisits = mVisits;
    }
}
