package com.borisruzanov.russianwives.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.Users;

import java.util.List;

/**
 * Created by Михаил on 03.05.2018.
 */

public interface SearchView extends MvpView {

    void showLoading(boolean isLoading);
    void showEmpty(boolean show);
    void showUsers(List<Users> usersList);
    void showError();
    /*void showLoading();
    void stopLoading();*/
}
