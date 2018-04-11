package com.borisruzanov.russianwives.mvp.model.repository;

import android.net.Uri;

public interface IFirebaseRepository {
    boolean checkingForFirstNeededInformationOfUser();

    boolean checkForUserExist();

    void saveUser();

    void updateBodyTypeUserInfo(String info);

    void updateEthnicityTypeUserInfo(String info);

    void updateHobbyUserInfo(String info);

    void updateFaithUserInfo(String info);

    void updateDrinkingUserInfo(String info);

    void updateSmokingUserInfo(String info);

    void updateRelationshipUserInfo(String info);

    void updateHaveKidsUserInfo(String info);

    void updateWantKidsUserInfo(String info);

    void insertImageInStorage(Uri resultUri);
}
