package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;

import javax.inject.Inject;

public class OnlineUsersInteractor {

    private UserRepository mRepository;

    @Inject
    public OnlineUsersInteractor(UserRepository mRepository) {
        this.mRepository = mRepository;
    }

    public void getOnlineUsers(boolean isUserExist) {
        mRepository.addFullProfileUsers();
        mRepository.getOnlineUsers(isUserExist);
    }

    public boolean isUserExist() {
        return mRepository.isUserExist();
    }

    public void callRealUserList() {
//        mRepository.callRealUserList();
    }

    public void getLastKeyNodeFromFirebase() {
        mRepository.getLastKeyNodeFromFirebase();
    }

    public void resetPaginationValues() {
        mRepository.resetPaginationValues();
    }
}
