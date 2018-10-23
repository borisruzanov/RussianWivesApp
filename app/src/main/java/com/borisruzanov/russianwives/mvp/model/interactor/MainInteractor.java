package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.CheckRepository;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;

public class MainInteractor {

    private FirebaseRepository firebaseRepository;
    private CheckRepository checkRepository;

    public MainInteractor(FirebaseRepository firebaseRepository, CheckRepository checkRepository) {
        this.firebaseRepository = firebaseRepository;
        this.checkRepository = checkRepository;
    }

    public boolean isUserExist() {
        return firebaseRepository.isUserExist();
    }

    public void saveUser() {
        firebaseRepository.saveUser();
    }

    public boolean checkForUserInfo() {
       return firebaseRepository.checkingForFirstNeededInformationOfUser();
    }

    public boolean checkUserRegistred(){
        return checkRepository.checkUserRegistered();
    }

}
