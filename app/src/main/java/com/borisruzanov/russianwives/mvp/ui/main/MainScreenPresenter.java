package com.borisruzanov.russianwives.mvp.ui.main;

import com.borisruzanov.russianwives.eventbus.ListStringEvent;
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.eventbus.UserEvent;
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor;
import com.borisruzanov.russianwives.utils.BoolCallback;
import com.borisruzanov.russianwives.utils.Consts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainScreenPresenter {

    private MainInteractor mInteractor;
    private MainView mView;

    public MainScreenPresenter(MainInteractor mInteractor, MainView view) {
        this.mInteractor = mInteractor;
        this.mView = view;
    }

    /**
     * Checking user has must info
     */
    public void userHasMustInfo() {
        mInteractor.userHasMustInfo();
    }

    /**
     * Showing needed dialog window to the user
     *
     * @param result
     */
    @Subscribe
    public void showMustInfoDialog(StringEvent result) {
        if (result.getStringParameter().equals(Consts.MUST_INFO)) {
            mView.showMustInfoDialog();
        } else if (result.getStringParameter().equals(Consts.FULL_PROFILE)) {
            mView.showFullInfoDialog();
        }
    }

    /**
     * Receiving the list of default values of the user fields
     */
    @Subscribe
    public void getDefaultUserValuesList(ListStringEvent defaultList) {
        mView.showDefaultDialogScreen(defaultList.getList());
    }

    /**
     * Getting default fields list of the user
     */
    public void getDefaultList() {
        mInteractor.getDefaultFieldsList();
    }

    public boolean isUserExist() {
        return mInteractor.isUserExist();
    }

    public boolean isGenderDefault() {
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

    @Subscribe
    public void inflateCardList(UserEvent user) {
        mView.setUserData(user.getmUser());
    }



    public void unregisterSubscribers() {
        EventBus.getDefault().unregister(this);
    }

    public void showMustInfoDialog() {
        mInteractor.hasDefaultMustInfo(new BoolCallback() {
            @Override
            public void setBool(boolean flag) {
                if (flag) {
                    mView.showMustInfoDialog();
                }
            }
        });
    }


    public void showSecondaryDialogs() {
        mInteractor.getSecondaryInfo();
    }


    public void registerSubscribers() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


}
