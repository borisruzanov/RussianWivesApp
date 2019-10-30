package com.borisruzanov.russianwives.mvp.ui.friendprofile;

import com.arellomobile.mvp.MvpView;
import com.borisruzanov.russianwives.models.UserDescriptionModel;

import java.util.ArrayList;
import java.util.List;

public interface FriendProfileView  extends MvpView{

    void setFriendData(String name, String age, String country, String image);
    void setList(List<UserDescriptionModel> userDescriptionList);
    void openRegDialog();
    void setLikeHighlighted();
    void openChatMessage(String name, String image);
    void showMustInfoDialog();
    void openSlider(ArrayList<String> sliderList);
}
