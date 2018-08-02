package com.borisruzanov.russianwives.mvp.model.interactor;


import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;

public class SliderInteractor {


    FirebaseRepository repository;

    public SliderInteractor(FirebaseRepository repository) {
        this.repository = repository;
    }

}
