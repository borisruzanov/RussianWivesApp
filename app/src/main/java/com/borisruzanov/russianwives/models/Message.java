package com.borisruzanov.russianwives.models;

public class Message {

    private String message, type;
    private long time;
    private boolean seen;
    private String from;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message(String message){
        this.message = message;
    }

    public Message() {}

    public Message(String message, long time) {
        this.message = message;
        this.time = time;
    }

    public Message(String message, String type, long time, boolean seen) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String convertTimestamp(){
        return String.valueOf(time);
    }

}