package com.borisruzanov.russianwives.mvp.view;

import com.arellomobile.mvp.MvpView;

public interface MainView extends MvpView{
    void setViewPager();
    void callAuthWindow();
    void checkingForUserInformation();
}
