package com.borisruzanov.russianwives.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.mvp.model.interactor.MainInteractor;
import com.borisruzanov.russianwives.mvp.view.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private MainInteractor mainInteractor;

    public MainPresenter(MainInteractor mainInteractor) {
        this.mainInteractor = mainInteractor;
    }

    public void checkForUserExist() {
        if (mainInteractor.isUserExist()){
            getViewState().setViewPager();
        } else getViewState().callAuthWindow();
    }

    public void saveUser(){
        mainInteractor.saveUser();
    }

    public void userNeedToAddInfo(){
        if (mainInteractor.checkForUserInfo()){
            getViewState().checkingForUserInfo();
        }
    }
}
