package com.borisruzanov.russianwives.models;

public class ActInfo {

    int visits, likes;

    public ActInfo(int visits, int likes) {
        this.visits = visits;
        this.likes = likes;
    }

    public int getVisits() {
        return visits;
    }

    public int getLikes() {
        return likes;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

}
