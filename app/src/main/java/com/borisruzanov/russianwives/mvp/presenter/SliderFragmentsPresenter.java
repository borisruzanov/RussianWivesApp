package com.borisruzanov.russianwives.mvp.presenter;

import android.net.Uri;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.mvp.model.interactor.SliderInteractor;
import com.borisruzanov.russianwives.ui.slider.SliderImageFragment;

public class SliderFragmentsPresenter extends MvpPresenter<MvpView> {

    SliderInteractor interactor;
    SliderImageFragment sliderImageFragment;

    public SliderFragmentsPresenter(SliderInteractor interactor, SliderImageFragment sliderImageFragment) {
        this.interactor = interactor;
        this.sliderImageFragment = sliderImageFragment;
    }

    public void updateBodyTypeUserInfo(String info) {
        interactor.updateBodyTypeUserInfo(info);
    }

    public void updateEthnicityUserInfo(String info) {
        interactor.updateEthnicityTypeUserInfo(info);
    }

    public void updateHobbyUserInfo(String info) {
        interactor.updateHobbyUserInfo(info);
    }

    public void updateFaithUserInfo(String info) {
        interactor.updateFaithUserInfo(info);
    }

    public void updateDrinkingUserInfo(String info) {
        interactor.updateDrinkingUserInfo(info);
    }

    public void updateSmokingUserInfo(String info) {
        interactor.updateSmokingUserInfo(info);
    }

    public void updateRelationshipUserInfo(String info) {
        interactor.updateRelationshipUserInfo(info);
    }

    public void updateHaveKidsUserInfo(String info) {
        interactor.updateHaveKidsUserInfo(info);
    }

    public void updateWantKidsUserInfo(String info) {
        interactor.updateWantKidsUserInfo(info);
    }

    public void insertImageInStorage(Uri resultUri){
        interactor.insertImageInStorage(resultUri);
    }

}
