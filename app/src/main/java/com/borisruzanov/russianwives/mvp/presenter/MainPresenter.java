package com.borisruzanov.russianwives.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.mvp.model.interactor.IMainInteractor;
import com.borisruzanov.russianwives.mvp.view.MainView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private IMainInteractor iMainInteractor;

    public MainPresenter(IMainInteractor iMainInteractor) {
        this.iMainInteractor = iMainInteractor;
    }

    public void checkForUserExist() {
        if(!iMainInteractor.checkForUserExist()){
            Log.d("Auth", "Call google auth ui");
            getViewState().callAuthWindow();
        }
    }

    public void saveUser(){
        iMainInteractor.saveUser();
    }

    public void userNeedToAddInfo(){
        if (iMainInteractor.checkForUserInfo()){
            getViewState().checkingForUserInformation();
        }
    }
}
