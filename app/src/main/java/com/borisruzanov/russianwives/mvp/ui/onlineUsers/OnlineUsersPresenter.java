package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.mvp.model.interactor.OnlineUsersInteractor;

import javax.inject.Inject;

@InjectViewState
public class OnlineUsersPresenter extends MvpPresenter<OnlineUsersView> {

    private OnlineUsersInteractor interactor;

    @Inject
    public OnlineUsersPresenter(OnlineUsersInteractor interactor) {
        this.interactor = interactor;
    }

    public void getOnlineUsers(int page) {
        Log.d("RecyclerDebug", "In OnlineUsersPresenter.initRecyclerView()");
        interactor.getOnlineUsers(page, onlineUsers -> {
            // Log.d("RecyclerDebug", "In OnlineUsersPresenter.getOnlineUsers, we have " + onlineUsers.size() + "users online");
            getViewState().addUsers(onlineUsers);
        });
    }

}
