package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;

import javax.inject.Inject;

public class OnlineUsersInteractor {

    private UserRepository mRepository;

    @Inject
    public OnlineUsersInteractor(UserRepository mRepository) {
        this.mRepository = mRepository;
    }

    public void getOnlineUsers(String uid, boolean isUserExist,long lastRating) {
        mRepository.getOnlineUsers(uid, isUserExist,lastRating);
    }

    public boolean isUserExist() {
        return mRepository.isUserExist();
    }
}
