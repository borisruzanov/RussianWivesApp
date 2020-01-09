package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.eventbus.ChatEvent;
import com.borisruzanov.russianwives.eventbus.ListEvent;
import com.borisruzanov.russianwives.eventbus.ListStringEvent;
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

    public void getOnlineFragmentUsers(int page, boolean mIsUserExist) {
//        mInteractor.getOnlineFragmentUsers(page, onlineUsers -> {
//            getViewState().addUsers(onlineUsers);
//        }, mIsUserExist);

        mInteractor.getOnlineUsers(page, mIsUserExist);
    }

    public boolean isUserExist() {
        return mInteractor.isUserExist();
    }

    /**
     * Event of inflating games list
     */
    @Subscribe
    public void inflateCardList(ListEvent event) {
        mView.addUsers(event.getCardList());
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
}
