package com.borisruzanov.russianwives.mvp.ui.mustinfo;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;

public interface MustInfoDialogView extends MvpView {

    void showProgress();
    void hideProgress();
    void showMessage(@StringRes int resId);

}
