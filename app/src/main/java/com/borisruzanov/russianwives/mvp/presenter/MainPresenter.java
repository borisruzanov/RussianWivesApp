package com.borisruzanov.russianwives.mvp.presenter;

import android.app.Activity;

import com.borisruzanov.russianwives.mvp.model.IMainInteractor;

public class MainPresenter implements IMainPresenter{

        IMainInteractor iMainInteractor;

        public MainPresenter(IMainInteractor iMainInteractor) {
            this.iMainInteractor = iMainInteractor;
        }

        @Override
        public void forwardInfoForLoginAuth(Activity mainActivity) {
            iMainInteractor.checkingForLogIn(mainActivity);
        }
    }
