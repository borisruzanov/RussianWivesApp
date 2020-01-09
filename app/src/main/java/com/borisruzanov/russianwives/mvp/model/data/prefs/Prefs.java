package com.borisruzanov.russianwives.mvp.model.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.utils.Consts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Prefs {

    private SharedPreferences prefs;
    private Context context;

    public Prefs(Context context) {
        this.context = context;
        String PREFS_FILE_NAME = "russian_wives_app";
        prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setFirstOpenDate() {
        setDate(Consts.FIRST_OPEN_DATE);
    }

    public String getFirstOpenDate() {
        return getValue(Consts.FIRST_OPEN_DATE);
    }

    public void setFPOpenDate() {
        setDate(Consts.DIALOG_OPEN_DATE);
    }

    public void setDialogOpenDate(String value) {
        setValue(Consts.DIALOG_OPEN_DATE, value);
    }

    public void clearValue(String key) {
        setValue(key, "");
    }

    public String getUserGender() {
        return reverseGender(getGenderSearch());
    }

    public void setGenderSearch(String gender) {
        String genderSearch = reverseGender(gender);
        setValue(Consts.GENDER_SEARCH, genderSearch);
        setGender(genderSearch);
    }

    public String getGenderSearch() {
        return getValue(Consts.GENDER_SEARCH);
    }

    public String getGender() {
        return getValue(Consts.GENDER);
    }

    public void setGender(String value) {
        setValue(Consts.GENDER, value);
    }

    public String getAge() {
        return getValue(Consts.AGE);
    }

    public void setAge(String value) {
        setValue(Consts.AGE, value);
    }

    public String getCountry() {
        return getValue(Consts.COUNTRY);
    }

    public void setCountry(String value) {
        setValue(Consts.COUNTRY, value);
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

    private void setDate(String key) {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        setValue(key, date);
    }

    private String reverseGender(String gender) {
        String reverseGender = "";
        if (gender.equals(context.getString(R.string.male_option))) {
            reverseGender = context.getString(R.string.female_option);
        } else if (gender.equals(context.getString(R.string.female_option))) {
            reverseGender = context.getString(R.string.male_option);
        }
        return reverseGender;
    }

    public String getValue(String key) {
        return prefs.getString(key, Consts.DEFAULT);
    }

    public void setValue(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String getMustInfo() {
        return getValue(Consts.MUST_INFO);
    }

    public String getFullProfile(){
        return getValue(Consts.FULL_PROFILE);
    }

}
