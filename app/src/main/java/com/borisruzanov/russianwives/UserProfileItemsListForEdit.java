package com.borisruzanov.russianwives;

import com.borisruzanov.russianwives.models.UserDescriptionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserProfileItemsListForEdit {
    public static List<UserDescriptionModel> initData(){

        ArrayList<UserDescriptionModel> languages = new ArrayList<UserDescriptionModel>();

        return Arrays.asList(
                new UserDescriptionModel("Image", "Male"),
                new UserDescriptionModel("Name", "Male"),
                new UserDescriptionModel("Age", "Male"),
                new UserDescriptionModel("Country", "Male"),
                new UserDescriptionModel("Gender", "Male"),
                new UserDescriptionModel("Relationship Status", "Married"),
                new UserDescriptionModel("Body Type", "Athletic"),
                new UserDescriptionModel("Ethnicity", "White"),
                new UserDescriptionModel("Faith", "Christian"),
                new UserDescriptionModel("Smoke Status", "Never"),
                new UserDescriptionModel("How Often Do You Drink Alcohol", "Never"),
                new UserDescriptionModel("Do You Have Kids", "No"),
                new UserDescriptionModel("Do You Want Kids", "Yes"),
                new UserDescriptionModel("Looking for", "Swimming")
        );

    }
}
