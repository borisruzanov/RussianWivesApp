package com.borisruzanov.russianwives.mvp.model.interactor;

import android.net.Uri;

/**
 * Created by Boris on 4/10/2018.
 */

public interface ISliderInteractor {
    void updateBodyTypeUserInfo(String info);

    void updateEthnicityTypeUserInfo(String info);

    void updateHobbyUserInfo(String info);

    void updateFaithUserInfo(String info);

    void updateDrinkingUserInfo(String info);

    void updateSmokingUserInfo(String info);

    void updateRelationshipUserInfo(String info);

    void updateHaveKidsUserInfo(String info);

    void updateWantKidsUserInfo(String info);

    void  insertImageInStorage(Uri resultUri);

}
