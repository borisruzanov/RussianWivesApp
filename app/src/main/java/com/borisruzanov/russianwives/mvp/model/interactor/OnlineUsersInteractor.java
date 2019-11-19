package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.borisruzanov.russianwives.utils.OnlineUsersCallback;

import javax.inject.Inject;

public class OnlineUsersInteractor {

    private UserRepository repository;

    @Inject
    public OnlineUsersInteractor(UserRepository repository) {
        this.repository = repository;
    }

    public void getOnlineUsers(int page, boolean isUserExist) {
        repository.getOnlineUsers(page, isUserExist);
    }

    public boolean isUserExist() {
        return repository.isUserExist();
    }
}
