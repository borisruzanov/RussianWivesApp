package com.borisruzanov.russianwives.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.UserProfileItemsList;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.interactor.FriendProfileInteractor;
import com.borisruzanov.russianwives.mvp.view.FriendProfileView;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class FriendProfilePresenter extends MvpPresenter<FriendProfileView> {

    private FriendProfileInteractor interactor;
    private List<UserDescriptionModel> userDescriptionList = new ArrayList<>();

    public FriendProfilePresenter(FriendProfileInteractor interactor) {
        this.interactor = interactor;
    }

    public void setAllInfo(String friendUid){
        setFriendData(friendUid);
        setUserVisited(friendUid);
    }

    private void setFriendData(String friendUid) {
        interactor.getFriendData(friendUid, fsUser -> {
            getViewState().setFriendData(fsUser.getName(), fsUser.getAge(), fsUser.getCountry(), fsUser.getImage());
            userDescriptionList.addAll(UserProfileItemsList.initData(fsUser));
            getViewState().setList(userDescriptionList);
        });
    }

    private void setUserVisited(String friendUid) {
        interactor.setUserVisited(friendUid);
    }

}
