package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;

public class MainInteractor {

    private FirebaseRepository firebaseRepository;

    public MainInteractor(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
    }

    public boolean checkForUserExist() {
        return firebaseRepository.checkForUserExist();
    }

    public void saveUser() {
        firebaseRepository.saveUser();
    }

    public boolean checkForUserInfo() {
       return firebaseRepository.checkingForFirstNeededInformationOfUser();
    }
}
