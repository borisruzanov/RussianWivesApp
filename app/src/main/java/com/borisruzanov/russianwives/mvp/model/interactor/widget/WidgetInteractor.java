package com.borisruzanov.russianwives.mvp.model.interactor.widget;

import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.borisruzanov.russianwives.utils.ActionsCountInfoCallback;

public class WidgetInteractor {

    private UserRepository repository;

    public WidgetInteractor(UserRepository repository){
        this.repository = repository;
    }

    public void getActionsInfo(ActionsCountInfoCallback callback){
        repository.getActionsCountInfo(callback);
    }

}
