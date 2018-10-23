package com.borisruzanov.russianwives.mvp.ui.slider

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.borisruzanov.russianwives.mvp.model.interactor.slider.SliderInteractor

class SliderFragmentsPresenter(var interactor: SliderInteractor)
    : MvpPresenter<MvpView>()
