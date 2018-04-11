package com.borisruzanov.russianwives.mvp.model.interactor;

public interface IMainInteractor {

    boolean checkForUserExist();
    void saveUser();

    boolean checkForUserInfo();
}
