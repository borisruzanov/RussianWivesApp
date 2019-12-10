package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.eventbus.ListEvent;
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.mvp.model.interactor.OnlineUsersInteractor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

@InjectViewState
public class OnlineUsersPresenter extends MvpPresenter<OnlineUsersView> {

    private OnlineUsersInteractor interactor;
    private OnlineUsersView mView;

    @Inject
    public OnlineUsersPresenter(OnlineUsersInteractor interactor, OnlineUsersView view) {
        this.interactor = interactor;
        this.mView = view;
    }

    public void getOnlineFragmentUsers(int page, boolean mIsUserExist) {
//        interactor.getOnlineFragmentUsers(page, onlineUsers -> {
//            getViewState().addUsers(onlineUsers);
//        }, mIsUserExist);

        interactor.getOnlineUsers(page, mIsUserExist);
    }

    public boolean isUserExist() {
        return interactor.isUserExist();
    }

    /**
     * Event of inflating games list
     */
    @Subscribe
    public void inflateCardList(ListEvent event) {
        mView.addUsers(event.getCardList());
    }

    @Subscribe
    public void inflateFakeUserListFirstTime(StringEvent event){
        getViewState().makeFakeUserCall();
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
