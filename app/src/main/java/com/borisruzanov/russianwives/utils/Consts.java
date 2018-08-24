package com.borisruzanov.russianwives.utils;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

public final class Consts {
    public static final String COLLECTION_USERS = "Users";
    public static final String NAME = "name";
    public static final String BODY_TYPE = "body_type";
    public static final String ETHNICITY = "ethnicity";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String COUNTRY = "country";
    public static final String HOBBY  = "hobby";
    public static final String FAITH  = "faith";
    public static final String HOW_TALL  = "how_tall";
    public static final String IMAGE  = "image";
    public static final String LANGUAGES  = "languages";
    public static final String DRINK_STATUS  = "drink_status";
    public static final String SMOKING_STATUS  = "smoking_status";
    public static final String RELATIONSHIP_STATUS  = "relationship_status";
    public static final String NUMBER_OF_KIDS  = "number_of_kids";
    public static final String WANT_CHILDREN_OR_NOT  = "want_children_or_not";
    public static final String UID  = "uid";

    public static List<String> keyList = Arrays.asList(GENDER, AGE, COUNTRY, RELATIONSHIP_STATUS,
            BODY_TYPE, ETHNICITY, FAITH, SMOKING_STATUS, DRINK_STATUS, NUMBER_OF_KIDS, WANT_CHILDREN_OR_NOT);

    public static final String DEFAULT  = "default";
}
