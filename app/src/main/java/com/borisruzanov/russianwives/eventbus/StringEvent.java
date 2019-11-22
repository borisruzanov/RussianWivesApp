package com.borisruzanov.russianwives.eventbus;

public class StringEvent {
    String stringParameter;

    public StringEvent(String stringParameter) {
        this.stringParameter = stringParameter;
    }

    public String getStringParameter() {
        return stringParameter;
    }

    public void setStringParameter(String stringParameter) {
        this.stringParameter = stringParameter;
    }
}
