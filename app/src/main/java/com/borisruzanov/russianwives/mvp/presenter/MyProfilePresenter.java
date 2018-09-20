package com.borisruzanov.russianwives.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.UserProfileItemsList;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.interactor.MyProfileInteractor;
import com.borisruzanov.russianwives.mvp.view.MyProfileView;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class MyProfilePresenter extends MvpPresenter<MyProfileView> {

    private MyProfileInteractor interactor;
    private List<UserDescriptionModel> userDescriptionList = new ArrayList<>();

    public MyProfilePresenter(MyProfileInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        setAllCurrentUserInfo();
        setActionsCount();
    }

    public void setAllCurrentUserInfo(){
        interactor.getAllCurrentUserInfo(fsUser -> {
            getViewState().setUserData(fsUser.getName(), fsUser.getAge(), fsUser.getCountry(), fsUser.getImage());
            userDescriptionList.addAll(UserProfileItemsList.initData(fsUser));
            getViewState().setList(userDescriptionList);
        });
    }

    public void setActionsCount(){
        interactor.getActionsCountInfo((visits, likes) -> getViewState().setActionsCount(visits, likes));
    }

}
