package com.borisruzanov.russianwives;

import android.util.Log;

import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.models.UserDescriptionModel;

import java.util.Arrays;
import java.util.List;

public class UserProfileItemsList {

    public static List<UserDescriptionModel> initData(User user){
        Log.d(Contract.TAG,"Values are -> " + user.getRelationship_status());

        return Arrays.asList(new UserDescriptionModel("Gender", user.getGender()),
                new UserDescriptionModel("Relationship Status", user.getRelationship_status()),
                new UserDescriptionModel("Body Type", user.getBody_type()),
                new UserDescriptionModel("Ethnicity", user.getEthnicity()),
                new UserDescriptionModel("Faith", user.getFaith()),
                new UserDescriptionModel("Smoke Status", user.getSmoking_status()),
                new UserDescriptionModel("How Often Do You Drink Alcohol", user.getDrink_status()),
                new UserDescriptionModel("Number Of Kids You Have", user.getNumber_of_kids()),
                new UserDescriptionModel("Do You Want Kids", user.getWant_children_or_not()),
                new UserDescriptionModel("Hobby", user.getHobby())
        );

    }
}
