package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.OnlineUser;

import java.util.ArrayList;
import java.util.List;

public interface OnlineUsersView extends MvpView {

    void addUsers(List<OnlineUser> onlineUsers);
    void setRefreshProgress(boolean progress);

    void makeFakeUserCall(String gender);

    void openChats(String getmUid, String getmImage, String getmName);

    void showRegistrationDialog();

    void showFullProfileDialog();

    void openSlider(ArrayList<String> list);
}
