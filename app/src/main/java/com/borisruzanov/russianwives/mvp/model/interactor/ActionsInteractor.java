package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.ActionItemCallback;

public class ActionsInteractor {

    private FirebaseRepository repository;

    public ActionsInteractor(FirebaseRepository repository) {
        this.repository = repository;
    }

    public void getActions(ActionItemCallback callback){
        repository.getTransformedActions(callback);
    }

}
