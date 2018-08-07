package com.borisruzanov.russianwives.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FirebaseRequestManager {

    public static HashMap<String, String> createNewUser(String displayName, String deviceToken, String uid){
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name", displayName);
        userMap.put("age", "default");
        userMap.put("country", "default");
        userMap.put("status", "default");
        userMap.put("image", "default");
        userMap.put("thumb_image", "default");
        userMap.put("device_token", deviceToken);
        userMap.put("relationship_status", "default");
        userMap.put("gender", "default");
        userMap.put("how_tall", "default");
        userMap.put("body_type", "default");
        userMap.put("ethnicity", "default");
        userMap.put("faith", "default");
        userMap.put("languages", "default");
        userMap.put("smoking_status", "default");
        userMap.put("drink_status", "default");
        userMap.put("number_of_kids", "default");
        userMap.put("want_children_or_not", "default");
        userMap.put("hobby", "default");
        userMap.put("uid", uid);
        return userMap;
    }

    public static List<String> createFieldsList(){
        return Arrays.asList(Consts.BODY_TYPE, Consts.DRINK_STATUS, Consts.ETHNICITY,Consts.GENDER,
                Consts.HOBBY, Consts.FAITH, Consts.HOW_TALL, Consts.IMAGE, Consts.LANGUAGES, Consts.DRINK_STATUS,
                Consts.SMOKING_STATUS, Consts.RELATIONSHIP_STATUS, Consts.WANT_CHILDREN_OR_NOT, Consts.NUMBER_OF_KIDS);
    }

}
