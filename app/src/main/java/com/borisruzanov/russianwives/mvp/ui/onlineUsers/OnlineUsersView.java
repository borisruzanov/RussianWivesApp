package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.OnlineUser;

import java.util.List;

public interface OnlineUsersView extends MvpView {

    void addUsers(List<OnlineUser> onlineUsers);
    void setRefreshProgress(boolean progress);

    void makeFakeUserCall();
}
