package com.borisruzanov.russianwives.eventbus;

public class LastKeyEvent {
    String lastKey;

    public LastKeyEvent(String lastKey) {
        this.lastKey = lastKey;
    }

    public String getLastKey() {
        return lastKey;
    }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }
}
