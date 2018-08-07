package com.borisruzanov.russianwives.models;

import com.borisruzanov.russianwives.ui.global.ViewType;

public class User implements ViewType {

    private String name;
    private String age;
    private String country;
    private String image;
    private String status;
    private String thumb_image;
    private String uid;
    private String gender;
    private String relationship_status;
    private String body_type;
    private String ethnicity;
    private String faith;
    private String smoking_status;
    private String number_of_kids;
    private String want_children_or_not;
    private String hobby;
    private String drink_status;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDrink_status() {
        return drink_status;
    }

    public void setDrink_status(String drink_status) {
        this.drink_status = drink_status;
    }

    public User(String name, String age, String country, String drink_status, String image, String status, String thumb_image, String uid, String gender, String relationship_status, String body_type, String ethnicity, String faith, String smoking_status, String alcohol, String number_of_kids, String want_children_or_not, String hobby) {
        this.name = name;
        this.age = age;
        this.country = country;
        this.drink_status = drink_status;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
        this.uid = uid;
        this.gender = gender;
        this.relationship_status = relationship_status;
        this.body_type = body_type;
        this.ethnicity = ethnicity;
        this.faith = faith;
        this.smoking_status = smoking_status;
        this.number_of_kids = number_of_kids;
        this.want_children_or_not = want_children_or_not;
        this.hobby = hobby;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationship_status() {
        return relationship_status;
    }

    public void setRelationship_status(String relationship_status) {
        this.relationship_status = relationship_status;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getFaith() {
        return faith;
    }

    public void setFaith(String faith) {
        this.faith = faith;
    }

    public String getSmoking_status() {
        return smoking_status;
    }

    public void setSmoking_status(String smoking_status) {
        this.smoking_status = smoking_status;
    }

    public String getNumber_of_kids() {
        return number_of_kids;
    }

    public void setNumber_of_kids(String number_of_kids) {
        this.number_of_kids = number_of_kids;
    }

    public String getWant_children_or_not() {
        return want_children_or_not;
    }

    public void setWant_children_or_not(String want_children_or_not) {
        this.want_children_or_not = want_children_or_not;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public User(){

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int getViewType() {
        return Contract.USERS;
    }
}