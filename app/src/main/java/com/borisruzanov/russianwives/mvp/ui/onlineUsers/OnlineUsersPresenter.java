package com.borisruzanov.russianwives.mvp.ui.onlineUsers;

import android.os.Handler;

import com.borisruzanov.russianwives.eventbus.ChatEvent;
import com.borisruzanov.russianwives.eventbus.FakeUsersListEvent;
import com.borisruzanov.russianwives.eventbus.LastKeyEvent;
import com.borisruzanov.russianwives.eventbus.ListStringEvent;
import com.borisruzanov.russianwives.eventbus.RealUsersListEvent;
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.mvp.model.interactor.OnlineUsersInteractor;
import com.borisruzanov.russianwives.utils.Consts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class OnlineUsersPresenter {

    private OnlineUsersInteractor mInteractor;
    private OnlineUsersView mView;
    private boolean listAdded = false;

    public OnlineUsersPresenter(OnlineUsersInteractor mInteractor, OnlineUsersView view) {
        this.mInteractor = mInteractor;
        this.mView = view;
    }

    /**
     * Get the real list of the users
     */
    @Subscribe
    public void inflateReaUserList(RealUsersListEvent event) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listAdded = true;
            }
        }, 1000);
        if (!listAdded){
            mView.addRealUsers(event.getRealUserList());
        }
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



    @Subscribe
    public void openChat(ChatEvent event) {
        if (event.getmName().equals("profile")) {
            mView.showFullProfileDialog();
        } else if (event.getmName().equals("registration")) {
            mView.showRegistrationDialog();
        } else {
            mView.openChats(event.getmUid(), event.getmImage(), event.getmName());
        }
    }

    @Subscribe
    public void inflateFakeUserListFirstTime(StringEvent event) {
       if (event.getStringParameter().equals(Consts.MALE) || event.getStringParameter().equals(Consts.FEMALE)) {
           mView.makeFakeUserCall(event.getStringParameter());
       }
    }

    //TODO remove since we dont need last key in fragment
    @Subscribe
    public void setLastKey(LastKeyEvent event) {
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


    public void resetPaginationValues() {
        mInteractor.resetPaginationValues();
    }
}
