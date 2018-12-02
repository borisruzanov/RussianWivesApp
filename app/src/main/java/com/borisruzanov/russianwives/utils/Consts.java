package com.borisruzanov.russianwives.utils;

import java.util.Arrays;
import java.util.List;

public final class Consts {
    //TODO rename to USERS_FS and add USERS_RT
    public static final String USERS_DB = "Users";
    public static final String ACTIONS_DB = "Actions";

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

    public static final String RATING  = "rating";

    public static final String DEVICE_TOKEN = "device_token";

    public static String ACTION = "action";
    public static String VISIT = "visit";
    public static String LIKE = "like";
    public static String TIMESTAMP = "timestamp";

    public static String MODULE = "module";
    public static final String REG_MODULE = "reg_module";
    public static final String SLIDER_MODULE = "slider_module";
    public static final String DEFAULT_LIST = "default_list";

    public static String REG_TAB_NAME = "reg_tab_name";
    public static String CHATS_TAB_NAME = "chats";
    public static String ACTIONS_TAB_NAME = "actions";

    public static final String NEED_BACK = "need_back";
    public static final String BACK = "back";

    public static List<String> keyList = Arrays.asList(GENDER, AGE, COUNTRY, RELATIONSHIP_STATUS,
            BODY_TYPE, ETHNICITY, FAITH, SMOKING_STATUS, DRINK_STATUS, NUMBER_OF_KIDS, WANT_CHILDREN_OR_NOT);

    public static List<String> fieldKeyList = Arrays.asList(Consts.BODY_TYPE, Consts.DRINK_STATUS, Consts.ETHNICITY,Consts.GENDER,
            Consts.HOBBY, Consts.FAITH, Consts.IMAGE, Consts.LANGUAGES, Consts.SMOKING_STATUS,
            Consts.RELATIONSHIP_STATUS, Consts.WANT_CHILDREN_OR_NOT);

    public static final String DEFAULT  = "default";
}
