package com.borisruzanov.russianwives.utils;

import android.content.SharedPreferences;

public class Prefs {

    private SharedPreferences prefs;
    private SharedPreferences.Editor mEditor;
    private boolean mBulkUpdate = false;

    /**
     * Class for keeping all the keys in one place for prefs
     */

    public class Key {
        public static final String BODY_TYPE = "body_type";
        public static final String DRINK_STATUS = "drink_status";
        public static final String FAITH = "faith";
        public static final String GENDER = "gender";
        public static final String HOBBY = "hobby";
        public static final String HOW_TALL = "how_tal";
        public static final String LANGUAGES = "languages";
        public static final String NUMBER_OF_KIDS = "number_of_kids";
        public static final String RELATIONSHIP_STATUS = "relationship_status";
        public static final String SMOKING_STATUS = "smoking status";
        public static final String WANT_CHILDREN = "want children or not";

    }

    public void put(String key, String val) {
        doEdit();
        mEditor.putString(key, val);
        doCommit();
    }

    public void put(String  key, int val) {
        doEdit();
        mEditor.putInt(key, val);
        doCommit();
    }

    public void put(String key, boolean val) {
        doEdit();
        mEditor.putBoolean(key, val);
        doCommit();
    }

    public void put(String key, float val) {
        doEdit();
        mEditor.putFloat(key, val);
        doCommit();
    }

    public void put(String key, double val) {
        doEdit();
        mEditor.putString(key, String.valueOf(val));
        doCommit();
    }

    public void put(String key, long val) {
        doEdit();
        mEditor.putLong(key, val);
        doCommit();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public String getString(String key) {
        return prefs.getString(key, null);
    }

    public int getInt(String key) {
        return prefs.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return prefs.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        return prefs.getLong(key, defaultValue);
    }

    public float getFloat(String key) {
        return prefs.getFloat(key, 0);
    }

    public float getFloat(String key, float defaultValue) {
        return prefs.getFloat(key, defaultValue);
    }


    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public double getDouble(String key, double defaultValue) {
        try {
            return Double.valueOf(prefs.getString(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    public void remove(String... keys){
        doEdit();
        for (String key : keys){
            mEditor.remove(key);
        }
        doCommit();
    }

    public void clear(){
        doEdit();
        mEditor.clear();
        doCommit();
    }

    public void edit(){
        mBulkUpdate = true;
        mEditor = prefs.edit();
    }

    private void doEdit(){
        if (!mBulkUpdate && mEditor == null){
            mEditor = prefs.edit();
        }
    }

    private void doCommit(){
        if(!mBulkUpdate && mEditor != null){
            mEditor.commit();
            mEditor = null;
        }
    }

}
