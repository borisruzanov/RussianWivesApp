package com.borisruzanov.russianwives.mvp.ui.main;

import com.arellomobile.mvp.MvpView;

import java.util.ArrayList;

public interface MainView extends MvpView{

    void callAuthWindow();
    void setAdapter(boolean isUserExist);
    void showGenderDialog();
    void showAdditionalInfoDialog();
    void openSlider(ArrayList<String> stringList);
}
