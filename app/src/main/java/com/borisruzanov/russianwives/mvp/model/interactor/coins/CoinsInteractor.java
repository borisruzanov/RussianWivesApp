package com.borisruzanov.russianwives.mvp.model.interactor.coins;

import com.borisruzanov.russianwives.mvp.model.repository.coins.CoinsRepository;
import com.borisruzanov.russianwives.utils.BoolCallback;
import com.borisruzanov.russianwives.utils.NumCallback;

import javax.inject.Inject;

public class CoinsInteractor {

    private CoinsRepository repository;
    public static int HOT_PURCHASE = 10;

    @Inject
    public CoinsInteractor(CoinsRepository coinsRepository){
        repository = coinsRepository;
    }

    public void getUserCoins(NumCallback callback) {
        repository.getCoins(callback);
    }

    public void addUserCoins(int addCoins) {
        repository.addCoins(addCoins);
    }

    public void reduceUserCoins(int deleteCoins, BoolCallback enoughCallback) {
        repository.deleteCoins(deleteCoins, enoughCallback);
    }

    public void hasEnoughMoneyForHots(BoolCallback enoughCallback) {
        repository.hasEnoughCoins(HOT_PURCHASE, enoughCallback);
    }

    public void purchaseHotOption(BoolCallback enoughCallback) {
        repository.deleteCoins(HOT_PURCHASE, enoughCallback);
    }

}
