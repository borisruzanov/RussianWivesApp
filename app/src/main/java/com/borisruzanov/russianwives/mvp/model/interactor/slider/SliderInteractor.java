package com.borisruzanov.russianwives.mvp.model.interactor.slider;


import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;

public class SliderInteractor {


    SliderRepository repository;

    public SliderInteractor(SliderRepository repository) {
        this.repository = repository;
    }

}
