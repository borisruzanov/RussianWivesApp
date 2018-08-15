package com.borisruzanov.russianwives.models;

public class Chat {

    long timeStamp;
    boolean seen;

    public Chat(long timeStamp, boolean seen) {
        this.timeStamp = timeStamp;
        this.seen = seen;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
