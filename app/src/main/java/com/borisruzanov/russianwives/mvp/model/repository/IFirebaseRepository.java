package com.borisruzanov.russianwives.mvp.model.repository;

import android.net.Uri;

import com.borisruzanov.russianwives.Refactor.garbage.IsDataLoadingCallback;
import com.borisruzanov.russianwives.utils.UsersListCallback;
import com.google.firebase.auth.FirebaseUser;

public interface IFirebaseRepository {
    FirebaseUser getUser();

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

    void getUsers(String nodeId, UsersListCallback usersListCallback, IsDataLoadingCallback isDataLoadingCallback);

    boolean isDataLoading();

    void setDataLoading(boolean isDataLoading);

    void insertImageInStorage(Uri resultUri);
}
