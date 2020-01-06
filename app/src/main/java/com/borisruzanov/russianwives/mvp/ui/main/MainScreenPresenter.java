package com.borisruzanov.russianwives.mvp.ui.main;

import com.borisruzanov.russianwives.eventbus.BooleanEvent;
import com.borisruzanov.russianwives.eventbus.ListEvent;
import com.borisruzanov.russianwives.eventbus.UserEvent;
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor;
import com.borisruzanov.russianwives.utils.BoolCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainScreenPresenter {

    private MainInteractor mInteractor;
    private MainView mView;

    public MainScreenPresenter(MainInteractor mInteractor, MainView view) {
        this.mInteractor = mInteractor;
        this.mView = view;
    }

    public boolean isUserExist(){
        return  mInteractor.isUserExist();
    }

    public boolean isGenderDefault(){
        return mInteractor.isGenderDefault();
    }

    public String getUserGender() {
        return mInteractor.getUserGender();
    }

    public void saveUser() {
        mInteractor.saveUser();
    }

    public void makeDialogOpenDateDefault() {
        mInteractor.makeDialogOpenDateDefault();
    }

    /**
     * Event of inflating games list
     */
    @Subscribe
    public void inflateCardList(UserEvent user) {
        mView.setUserData(user.getmUser());
    }

    public void registerSubscribers() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void unregisterSubscribers() {
        EventBus.getDefault().unregister(this);
    }

    public void showMustInfoDialog() {
        mInteractor.hasDefaultMustInfo(new BoolCallback() {
            @Override
            public void setBool(boolean flag) {
                if (flag){
                    mView.showMustInfoDialog();
                }
            }
        });
    }

    public boolean userHasMustInfo() {
       return mInteractor.userHasMustInfo();
    }

    @Subscribe
    public void showMustInfoDialog(BooleanEvent result) {
        if (result.ismResult()){
            mView.showMustInfoDialog();
        }

    }

    public void showSecondaryDialogs() {
        mInteractor.getSecondaryInfo();
    }
}
