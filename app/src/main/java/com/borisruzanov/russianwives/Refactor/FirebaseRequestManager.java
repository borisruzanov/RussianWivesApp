package com.borisruzanov.russianwives.Refactor;

import android.support.annotation.NonNull;

import com.borisruzanov.russianwives.utils.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FirebaseRequestManager {

    //TODO Maybe put in Firebase Repository?
    public static HashMap<String, Object> createNewUser(String displayName, String deviceToken, String uid, String gender){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", displayName);
        userMap.put("age", "default");
        userMap.put("country", "default");
        userMap.put("status", "default");
        userMap.put("image", "default");
        userMap.put("thumb_image", "default");
        userMap.put("device_token", deviceToken);
        userMap.put("relationship_status", "default");
        userMap.put("gender", gender);
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
        userMap.put("rating", 1);
        //userMap.put("achievements", new String[]{});
        return userMap;
    }

}
