package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.eventbus.ChatEvent;
import com.borisruzanov.russianwives.eventbus.LastKeyEvent;
import com.borisruzanov.russianwives.eventbus.FakeUsersListEvent;
import com.borisruzanov.russianwives.eventbus.ListStringEvent;
import com.borisruzanov.russianwives.eventbus.RealUsersListEvent;
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.mvp.model.interactor.OnlineUsersInteractor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

@InjectViewState
public class OnlineUsersPresenter extends MvpPresenter<OnlineUsersView> {

    private OnlineUsersInteractor mInteractor;
    private OnlineUsersView mView;

    @Inject
    public OnlineUsersPresenter(OnlineUsersInteractor mInteractor, OnlineUsersView view) {
        this.mInteractor = mInteractor;
        this.mView = view;
    }

    /**
     * Getting users list fake or real based on user exist
     */
    public void getOnlineFragmentUsers(boolean mIsUserExist) {
        mInteractor.getOnlineUsers(mIsUserExist);
    }

    public boolean isUserExist() {
        return mInteractor.isUserExist();
    }

    public void getLastKeyNodeFromFirebase() {
        mInteractor.getLastKeyNodeFromFirebase();
    }

    public void getRealUsers() {
    }
    /**
     * Get the fake list of the users
     */
    @Subscribe
    public void inflateFakeUserList(FakeUsersListEvent event) {
        mView.addFakeUsers(event.getFakeUserList());
    }

    /**
     * Get the real list of the users
     */
    @Subscribe
    public void inflateReaUserList(RealUsersListEvent event) {
        mView.addRealUsers(event.getRealUserList());
    }

    @Subscribe
    public void openChat(ChatEvent event) {
        if (event.getmName().equals("profile")) {
            mView.showFullProfileDialog();
        } else if (event.getmName().equals("registration")) {
            mView.showRegistrationDialog();
        } else {
            mView.openChats(event.getmUid(),event.getmImage(),event.getmName());
        }
    }

    @Subscribe
    public void inflateFakeUserListFirstTime(StringEvent event) {
        mView.makeFakeUserCall(event.getStringParameter());
    }

    @Subscribe
    public void setLastKey(LastKeyEvent event){
        mView.setLastNodeForPagination(event.getLastKey());
    }

    @Subscribe
    public void intentToInfoDialog(ListStringEvent event) {
        mView.openSlider(event.getList());
    }


    public void registerSubscribers() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void unregisterSubscribers() {
        EventBus.getDefault().unregister(this);
    }

    public void callRealUserList() {
        mInteractor.callRealUserList();
    }


}
