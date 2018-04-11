package com.borisruzanov.russianwives.mvp.model.interactor;

import android.net.Uri;

import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;

public class SliderInteractor implements ISliderInteractor{


    FirebaseRepository repository;

    public SliderInteractor(FirebaseRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateBodyTypeUserInfo(String info) {
        repository.updateBodyTypeUserInfo(info);
    }

    @Override
    public void updateEthnicityTypeUserInfo(String info) {
        repository.updateEthnicityTypeUserInfo(info);
    }

    @Override
    public void updateHobbyUserInfo(String info) {
        repository.updateHobbyUserInfo(info);
    }

    @Override
    public void updateFaithUserInfo(String info) {
        repository.updateFaithUserInfo(info);
    }

    @Override
    public void updateDrinkingUserInfo(String info) {
        repository.updateDrinkingUserInfo(info);
    }

    @Override
    public void updateSmokingUserInfo(String info) {
        repository.updateSmokingUserInfo(info);
    }

    @Override
    public void updateRelationshipUserInfo(String info) {
        repository.updateRelationshipUserInfo(info);
    }

    @Override
    public void updateHaveKidsUserInfo(String info) {
        repository.updateHaveKidsUserInfo(info);
    }

    @Override
    public void updateWantKidsUserInfo(String info) {
        repository.updateWantKidsUserInfo(info);
    }

    @Override
    public void insertImageInStorage(Uri resultUri) {
        repository.insertImageInStorage(resultUri);
    }
}
