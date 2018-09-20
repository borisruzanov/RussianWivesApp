package com.borisruzanov.russianwives.mvp.view;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.UserDescriptionModel;

import java.util.List;

public interface MyProfileView extends MvpView{

    void setUserData(String name, String age, String country, String image);
    void setActionsCount(long visits, long likes);
    void setList(List<UserDescriptionModel> userDescriptionList);

}
