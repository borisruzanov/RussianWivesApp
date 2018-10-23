package com.borisruzanov.russianwives.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.mvp.model.interactor.MainInteractor;
import com.borisruzanov.russianwives.mvp.view.MainView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private MainInteractor mainInteractor;

    public MainPresenter(MainInteractor mainInteractor) {
        this.mainInteractor = mainInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().setViewPager();
    }

    public void checkForUserExist() {
        /*if (mainInteractor.isUserExist()){
            getViewState().setViewPager();
        } else getViewState().callAuthWindow();*/
    }

    public void checkUserRegistered(){
        boolean checkRegistered = mainInteractor.checkUserRegistred();
        Log.d("UzverDebug", "User registered is " + checkRegistered);
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
