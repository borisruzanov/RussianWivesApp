package com.borisruzanov.russianwives.mvp.model.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.borisruzanov.russianwives.utils.Consts;

public class Prefs {

    private SharedPreferences prefs;
    private String PREFS_FILE_NAME = "russian_wives_app";

    public Prefs(Context context) {
        prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getGender() {
        return getValue(Consts.GENDER);
    }

    public void setGender(String value) {
        setValue(Consts.GENDER, value);
    }

    public String getRelationshipStatus() {
        return getValue(Consts.RELATIONSHIP_STATUS);
    }

    public void setRelationshipStatus(String value) {
        setValue(Consts.RELATIONSHIP_STATUS, value);
    }

    public String getBodyType() {
        return getValue(Consts.BODY_TYPE);
    }

    public void setBodyType(String value) {
        setValue(Consts.BODY_TYPE, value);
    }

    public String getEthnicity() {
        return getValue(Consts.ETHNICITY);
    }

    public void setEthnicity(String value) {
        setValue(Consts.ETHNICITY, value);
    }

    public String getFaith() {
        return getValue(Consts.FAITH);
    }

    public void setFaith(String value) {
        setValue(Consts.FAITH, value);
    }

    public String getSmokingStatus() {
        return getValue(Consts.SMOKING_STATUS);
    }

    public void setSmokingStatus(String value) {
        setValue(Consts.SMOKING_STATUS, value);
    }

    public String getDrinkStatus() {
        return getValue(Consts.DRINK_STATUS);
    }

    public void setDrinkStatus(String value) {
        setValue(Consts.DRINK_STATUS, value);
    }

    public String getNumberOfKids() {
        return getValue(Consts.NUMBER_OF_KIDS);
    }

    public void setNumberOfKids(String value) {
        setValue(Consts.NUMBER_OF_KIDS, value);
    }

    public String getWantChilderOrNot() {
        return getValue(Consts.WANT_CHILDREN_OR_NOT);
    }

    public void setWantChildrenOrNot(String value) {
        setValue(Consts.WANT_CHILDREN_OR_NOT, value);
    }

    public String getHobby() {
        return getValue(Consts.HOBBY);
    }

    public void setHobby(String value) {
        setValue(Consts.HOBBY, value);
    }

    public String getValue(String key) {
        return prefs.getString(key, "default");
    }

    public void setValue(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

}
