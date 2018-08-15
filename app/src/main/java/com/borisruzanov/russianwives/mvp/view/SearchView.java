package com.borisruzanov.russianwives.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.User;

import java.util.List;

/**
 * Created by Михаил on 03.05.2018.
 */

public interface SearchView extends MvpView {

    void showLoading(boolean isLoading);
    void showEmpty(boolean show);
    void showUsers(List<User> userList);
    void showError();
    void openFriend(String uid, String name, String image);
    /*void showLoading();
    void stopLoading();*/
}
