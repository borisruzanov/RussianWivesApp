package com.borisruzanov.russianwives.mvp.ui.search;

import android.os.Bundle;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.FsUser;

import java.util.List;


public interface SearchView extends MvpView {

    void setProgressBar(boolean isLoading);
    void showEmpty(boolean show);
    void addUsers(List<FsUser> usersList);
    void onUpdate();
    void showError();
    void openFriend(String uid, String transitionName, Bundle args);
    void openChat(String uid, String name, String image);
}
