package com.borisruzanov.russianwives.mvp.ui.mustinfo;

import android.net.Uri;

import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.interactor.mustinfo.MustInfoInteractor;
import com.borisruzanov.russianwives.utils.Consts;

public class MustInfoDialogPresenter extends MvpPresenter<MustInfoDialogView> {

    private MustInfoInteractor interactor;

    public MustInfoDialogPresenter(MustInfoInteractor interactor) {
        this.interactor = interactor;
    }

    public void saveValues(String age, String country, String image) {
        if (!age.equals(Consts.DEFAULT) && !country.equals(Consts.DEFAULT) && !image.equals(Consts.DEFAULT)) {
            interactor.updateAgeCountry(age, country);
        } else if (image.equals(Consts.DEFAULT)) {
            getViewState().showMessage(R.string.add_your_photo_mi);
        } else {
            getViewState().showMessage(R.string.add_your_info_mi);
        }
    }

    public void uploadPhoto(Uri photoUri) {
        interactor.uploadPhoto(photoUri, () -> getViewState().hideProgress());
    }

}
