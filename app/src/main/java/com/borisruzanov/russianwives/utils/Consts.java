package com.borisruzanov.russianwives.utils;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public final class Consts {
    public static final String USERS_DB = "Users";

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
    public static final String COINS  = "coins";
    public static final String UID  = "uid";

    public static final String FIRST_OPEN_DATE  = "first open date";
    public static final String DIALOG_OPEN_DATE  = "dialog open date";
    public static final String FP_OPEN_DATE  = "full profile open date";
    public static final String COINS_AMOUNT = "coins_amount";
    public static final String GENDER_SEARCH  = "gender search";

    public static final String RATING  = "rating";
    public static final String ACTION_MODULE = "action_module";
    public static final String APP_VERSION = "app_version";
    public static final String LAST_NODE = "last_node";
    public static final int ITEM_LOAD_COUNT = 15;
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String UPDATE_MODULE = "update";
    public static final String SHOW_FULL_DIALOG = "show_full_profile";
    public static final String SHOW = "show";
    public static final String DONT_SHOW = "dont_show";
    public static final String DIALOG_FREQUENCY = "dialog_frequency";
    public static final String SOC_MED_FREQUENCY = "soc_med_frequency";
    public static final String GENDER_FEMALE = "Female";
    public static final String GENDER_MALE = "Male";
    public static final String LIKES = "likes";
    public static final String VISITS = "visits";
    public static final String ACTIONS_LENGTH = "actions_length";
    public static final String SEARCH_EVENT_OK = "search_event_ok";
    public static final String SEARCH_EVENT_FAILURE = "search_event_failure" ;
    public static final String ID_SOC = "id_soc";
    public static final String SOC_MED_STATUS = "social_media_status";
    public static final String SHOW_SOC_MED = "show_soc_med";
    public static String ACHIEVEMENTS = "achievements";

    public static final String DEVICE_TOKEN = "device_token";
    public static final String ONLINE = "online";
    public static final String CONFIRMED = "confirmed";

    public static String ACTION = "action";
    public static String VISIT = "visit";
    public static String LIKE = "like";
    public static String TIMESTAMP = "timestamp";

    public static String MODULE = "module";
    public static final String REG_MODULE = "reg_module";
    public static final String SLIDER_MODULE = "slider_module";
    public static final String FP_MODULE = "full_profile_module";

    public static final String DEFAULT_LIST = "default_list";

    public static String REG_TAB_NAME = "reg_tab_name";
    public static String CHATS_TAB_NAME = "chats";
    public static String ACTIONS_TAB_NAME = "actions";

    public static final String NEED_BACK = "need_back";
    public static final String BACK = "back";

    public static List<String> keyList = Arrays.asList(GENDER, AGE, COUNTRY, RELATIONSHIP_STATUS,
            BODY_TYPE, ETHNICITY, FAITH, SMOKING_STATUS, DRINK_STATUS, NUMBER_OF_KIDS, WANT_CHILDREN_OR_NOT);

    public static List<String> fieldKeyList = Arrays.asList(GENDER, IMAGE, AGE, COUNTRY, RELATIONSHIP_STATUS,
            BODY_TYPE, ETHNICITY, FAITH, SMOKING_STATUS, DRINK_STATUS, NUMBER_OF_KIDS, WANT_CHILDREN_OR_NOT, HOBBY);


    public static final String DEFAULT  = "default";
    public static final String IMAGE_STORAGE  = "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/";

    public static final String MUST_INFO = "must_info";
    public static final String FALSE = "false";
    public static final String FULL_PROFILE = "full_profile";
    public static final String TRUE = "true";
    public static final String CHECK_DIALOG = "check_dialog";

    //Log tags
    public static final String TAG_INFO = "app_info_tag";
    public static final String TAG_START_METHOD = "log_start_method";
    public static final String TAG_END_METHOD = "log_end_method";
    public static final String FILTER_INDEX = "filter_index_event";

    public static final String CHAT_LENGTH = "chat_length";
}
