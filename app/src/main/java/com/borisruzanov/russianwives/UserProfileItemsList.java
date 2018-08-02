package com.borisruzanov.russianwives;

import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.models.UserDescriptionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserProfileItemsList {

    public static List<UserDescriptionModel> initData(User user){

        return Arrays.asList(new UserDescriptionModel("Gender", "Male"),
                new UserDescriptionModel("Relationship Status", "Married"),
                new UserDescriptionModel("Body Type", "Athletic"),
                new UserDescriptionModel("Ethnicity", "White"),
                new UserDescriptionModel("Faith", user.getName()),
                new UserDescriptionModel("Smoke Status", "Never"),
                new UserDescriptionModel("How Often Do You Drink Alcohol", "Never"),
                new UserDescriptionModel("Number Of Kids You Have", "No"),
                new UserDescriptionModel("Do You Want Kids", "Yes"),
                new UserDescriptionModel("Hobby", "Swimming")
        );

    }
}
