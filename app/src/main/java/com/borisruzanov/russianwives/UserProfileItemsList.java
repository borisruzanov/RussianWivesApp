package com.borisruzanov.russianwives;

import android.util.Log;

import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.models.UserDescriptionModel;

import java.util.Arrays;
import java.util.List;

public class UserProfileItemsList {

    public static List<UserDescriptionModel> initData(FsUser fsUser){
        Log.d(Contract.TAG,"Values are -> " + fsUser.getRelationship_status());

        return Arrays.asList(new UserDescriptionModel("Gender", fsUser.getGender()),
                new UserDescriptionModel("Relationship Status", fsUser.getRelationship_status()),
                new UserDescriptionModel("Body Type", fsUser.getBody_type()),
                new UserDescriptionModel("Ethnicity", fsUser.getEthnicity()),
                new UserDescriptionModel("Faith", fsUser.getFaith()),
                new UserDescriptionModel("Smoke Status", fsUser.getSmoking_status()),
                new UserDescriptionModel("How Often Do You Drink Alcohol", fsUser.getDrink_status()),
                new UserDescriptionModel("Number Of Kids You Have", fsUser.getNumber_of_kids()),
                new UserDescriptionModel("Do You Want Kids", fsUser.getWant_children_or_not()),
                new UserDescriptionModel("Hobby", fsUser.getHobby())
        );

    }
}
