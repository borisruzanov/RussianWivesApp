package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.ActionsCountInfoCallback;

public class WidgetInteractor {

    private FirebaseRepository repository;

    public WidgetInteractor(FirebaseRepository repository){
        this.repository = repository;
    }

    public void getActionsInfo(ActionsCountInfoCallback callback){
        repository.getActionsCountInfo(callback);
    }

}
