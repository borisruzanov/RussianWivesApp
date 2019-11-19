package com.borisruzanov.russianwives.models;

public class OnlineUser {
    private String uid, name, image, gender, country;
    private long rating;

    public OnlineUser() {
    }

    public OnlineUser(String uid, String name, String image, String gender, String country, long rating) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.gender = gender;
        this.country = country;
        this.rating = rating;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
