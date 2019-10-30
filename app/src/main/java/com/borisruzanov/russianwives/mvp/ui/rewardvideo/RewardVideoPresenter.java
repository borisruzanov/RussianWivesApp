package com.borisruzanov.russianwives.mvp.ui.rewardvideo;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.mvp.model.interactor.coins.CoinsInteractor;

import javax.inject.Inject;

@InjectViewState
public class RewardVideoPresenter extends MvpPresenter<RewardVideoView> {

    private CoinsInteractor interactor;
    private int coins = 0;

    @Inject RewardVideoPresenter(CoinsInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        interactor.getUserCoins(number -> {
            coins = number;
            getViewState().showCoinsCount(coins);
        });
    }

    public void addUserCoins(int reward){
        interactor.addUserCoins(reward);
        coins += reward;
        getViewState().showCoinsCount(coins);
    }


}
