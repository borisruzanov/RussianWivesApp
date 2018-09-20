package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.ActionsCountInfoCallback;
import com.borisruzanov.russianwives.utils.UserCallback;

public class MyProfileInteractor {

    private FirebaseRepository repository;

    public MyProfileInteractor(FirebaseRepository repository) {
        this.repository = repository;
    }

    public void getAllCurrentUserInfo(UserCallback callback){
        repository.getAllCurrentUserInfo(callback);
    }

    public void getActionsCountInfo(ActionsCountInfoCallback callback){
        repository.getActionsCountInfo(callback);
    }


}
