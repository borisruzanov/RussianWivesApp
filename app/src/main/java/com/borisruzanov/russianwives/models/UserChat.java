package com.borisruzanov.russianwives.models;

public class UserChat {

    private String name, image, userId, online, message;
    private long messageTimestamp;
    private boolean seen;

    public UserChat(String name, String image, boolean seen, String userId,
                    String online, String message, long messageTimestamp) {
        this.name = name;
        this.image = image;
        this.seen = seen;
        this.userId = userId;
        this.online = online;
        this.message = message;
        this.messageTimestamp = messageTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(long messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }
}
