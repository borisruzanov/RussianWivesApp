package com.borisruzanov.russianwives.models;

public class UserChat {

    private String name, image;
    private long timestamp;
    private boolean seen;

    public UserChat(String name, String image, long timestamp, boolean seen) {
        this.name = name;
        this.image = image;
        this.timestamp = timestamp;
        this.seen = seen;
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
}
