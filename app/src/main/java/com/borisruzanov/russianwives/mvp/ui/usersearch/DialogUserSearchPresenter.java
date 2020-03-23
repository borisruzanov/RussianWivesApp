package com.borisruzanov.russianwives.mvp.ui.usersearch;

import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor;

public class DialogUserSearchPresenter {
    private MainInteractor mMainInteractor;

    public DialogUserSearchPresenter(MainInteractor mMainInteractor) {
        this.mMainInteractor = mMainInteractor;
    }

    public void searchUser(String idSoc){
        mMainInteractor.searhUser(idSoc);
    }
}
