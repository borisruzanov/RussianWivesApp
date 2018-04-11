package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.IFirebaseRepository;

public class MainInteractor implements IMainInteractor{

    IFirebaseRepository iFirebaseRepository;

    public MainInteractor(IFirebaseRepository iFirebaseRepository) {
        this.iFirebaseRepository = iFirebaseRepository;
    }

    @Override
    public boolean checkForUserExist() {
        return iFirebaseRepository.checkForUserExist();
    }

    @Override
    public void saveUser() {
        iFirebaseRepository.saveUser();
    }

    @Override
    public boolean checkForUserInfo() {
       return iFirebaseRepository.checkingForFirstNeededInformationOfUser();
    }
}
