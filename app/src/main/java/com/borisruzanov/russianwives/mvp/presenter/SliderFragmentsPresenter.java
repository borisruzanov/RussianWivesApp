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



}
