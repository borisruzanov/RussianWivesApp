package com.borisruzanov.russianwives.mvp.model;

import android.app.Activity;

import com.borisruzanov.russianwives.utils.AuthStateListenerUtil;

public class MainInteractor implements IMainInteractor{

    AuthStateListenerUtil authStateListenerUtil;

    public MainInteractor() {
        this.authStateListenerUtil = authStateListenerUtil;
    }


    @Override
    public void checkingForLogIn(Activity mainActivity) {
        AuthStateListenerUtil authStateListenerUtils = new AuthStateListenerUtil();
        authStateListenerUtils.checkForAuthState(mainActivity);
    }
}
