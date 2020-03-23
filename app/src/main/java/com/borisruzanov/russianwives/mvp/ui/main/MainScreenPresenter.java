package com.borisruzanov.russianwives.mvp.ui.main;

import com.borisruzanov.russianwives.eventbus.ListStringEvent;
import com.borisruzanov.russianwives.eventbus.SearchEvent;
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.eventbus.UpdateVersionEvent;
import com.borisruzanov.russianwives.eventbus.UserEvent;
import com.borisruzanov.russianwives.models.FsUser;
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
            //1 step fill first needed info
            mView.showMustInfoDialog();
        } else if (result.getStringParameter().equals(Consts.FULL_PROFILE)) {
            //2 step - if we have first needed info - fill full profile info
            mView.showFullInfoDialog();
        } else if (result.getStringParameter().equals(Consts.UPDATE_MODULE)){
            //3 step if profile is full - check for updates
            mInteractor.checkForUpdateVersion();
        }
    }

    /**
     * Receiving the list of default values of the user fields
     */
    @Subscribe
    public void getDefaultUserValuesList(ListStringEvent defaultList) {
        mView.showDefaultDialogScreen(defaultList.getList());
    }

    @Subscribe
    public void getSearchResult(SearchEvent event){
        if (event.getmType().equals(Consts.SEARCH_EVENT_OK)){
            mView.openSearchedUser(event.getmMessage());
        } else {
            mView.showErrorPopup(event.getmMessage());
        }
    }

    /**
     * Getting default fields list of the user
     */
    public void getDefaultList() {
        mInteractor.getDefaultFieldsList();
    }

    /**
     * Changing user online status
     * @param user
     */
    public void changeUserOnlineStatus(FsUser user) {
        mInteractor.changeUserOnlineStatus(user);
    }

    /**
     * Checking for version of the app
     */
    public void checkForUpdateVersion() {
        mInteractor.checkForUpdateVersion();
    }

    /**
     * Event calling dialog for update application
     */
    @Subscribe
    public void updateAppDialogCall(UpdateVersionEvent event){
        mView.showUpdateDialog();
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


    public void getConfig() {
        mInteractor.getConfig();
    }

    public void saveInSocMed(FsUser mFsUser) {
        mInteractor.saveInSocMed(mFsUser);
    }
}
