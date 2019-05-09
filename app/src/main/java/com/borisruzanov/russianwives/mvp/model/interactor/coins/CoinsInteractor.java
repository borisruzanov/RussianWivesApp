package com.borisruzanov.russianwives.mvp.model.interactor.coins;

import com.borisruzanov.russianwives.mvp.model.repository.coins.CoinsRepository;
import com.borisruzanov.russianwives.utils.BoolCallback;
import com.borisruzanov.russianwives.utils.NumCallback;

import javax.inject.Inject;

public class CoinsInteractor {

    private CoinsRepository repository;

    @Inject CoinsInteractor(CoinsRepository coinsRepository){
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

    public void purchaseHotOption(BoolCallback enoughCallback) {
        repository.deleteCoins(10, enoughCallback);
    }

}
