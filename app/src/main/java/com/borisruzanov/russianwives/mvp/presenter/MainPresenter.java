package com.borisruzanov.russianwives.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.mvp.model.interactor.IMainInteractor;
import com.borisruzanov.russianwives.mvp.view.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    IMainInteractor iMainInteractor;

    public MainPresenter(IMainInteractor iMainInteractor) {
        this.iMainInteractor = iMainInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        //userNeedToAddInfo();
    }

    public void checkForUserExist() {
        if (iMainInteractor.checkForUserExist()) {
            iMainInteractor.saveUser();
        } else {
            getViewState().callAuthWindow();
        }
    }

    public void userNeedToAddInfo(){
        if (iMainInteractor.checkForUserInfo()){
            getViewState().checkingForUserInformation();
        } else {
        }
    }
}
