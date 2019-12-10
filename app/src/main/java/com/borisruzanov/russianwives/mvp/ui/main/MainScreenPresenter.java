package com.borisruzanov.russianwives.mvp.ui.main;

import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor;

public class MainScreenPresenter {

    private MainInteractor mInteractor;

    public MainScreenPresenter(MainInteractor mInteractor) {
        this.mInteractor = mInteractor;
    }

    public boolean isUserExist(){
        return  mInteractor.isUserExist();
    }
}
