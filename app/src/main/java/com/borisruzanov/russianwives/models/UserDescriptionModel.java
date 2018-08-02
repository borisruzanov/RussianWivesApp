package com.borisruzanov.russianwives.models;

public class UserDescriptionModel {
    String title;
    String answerDescription;

    public UserDescriptionModel(String title, String answerDescription) {
        this.title = title;
        this.answerDescription = answerDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswerDescription() {
        return answerDescription;
    }

    public void setAnswerDescription(String answerDescription) {
        this.answerDescription = answerDescription;
    }
}
