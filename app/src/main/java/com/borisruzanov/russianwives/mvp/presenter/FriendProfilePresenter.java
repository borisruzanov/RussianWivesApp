package com.borisruzanov.russianwives.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.mvp.model.interactor.FriendProfileInteractor;
import com.borisruzanov.russianwives.mvp.view.FriendProfileView;

@InjectViewState
public class FriendProfilePresenter extends MvpPresenter<FriendProfileView> {

    private FriendProfileInteractor interactor;

    public void setFriendData(String friendUid){

    }

    public void setAllCurrentUserInfo(){

    }

    public void setUserVisited(String friendUid){

    }

}
