package com.borisruzanov.russianwives.models;

public class UserChat {

    private String name, image, userId;
    private long timestamp;
    private boolean seen;
    private String online;
    private String message;

    public UserChat(String name, String image, long timestamp, boolean seen, String userId,
                    String online, String message) {
        this.name = name;
        this.image = image;
        this.timestamp = timestamp;
        this.seen = seen;
        this.userId = userId;
        this.online = online;
        this.message = message;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
}
