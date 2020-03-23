package com.borisruzanov.russianwives.utils;

import java.util.HashMap;

public class FirebaseRequestManager {

    public static HashMap<String, Object> createNewUser(String displayName, String deviceToken, String uid, String gender) {
        Long tsLong = System.currentTimeMillis() / 1000;
        String timeStamp = tsLong.toString();

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
        userMap.put("full_profile", "false");
        userMap.put("must_info", "false");
        userMap.put("rating", 1);
        userMap.put("coins", 0);
        userMap.put(Consts.ID_SOC, timeStamp);
        return userMap;
    }
}
