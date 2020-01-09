package com.borisruzanov.russianwives.mvp.ui.main;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.FsUser;

import java.util.ArrayList;

public interface MainView extends MvpView{

    void callAuthWindow();
    void setAdapter(boolean isUserExist);
    void showGenderDialog();
    void showMustInfoDialog();
    void showFullInfoDialog();
    void openSlider(ArrayList<String> stringList);
    void setUserData(FsUser user);
    void showDefaultDialogScreen(ArrayList<String> list);
}
