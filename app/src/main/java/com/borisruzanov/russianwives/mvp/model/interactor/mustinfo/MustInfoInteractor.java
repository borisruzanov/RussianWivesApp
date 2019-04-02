package com.borisruzanov.russianwives.mvp.model.interactor.mustinfo;

import android.net.Uri;

import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class MustInfoInteractor {

    private SliderRepository sliderRepository;

    @Inject
    public MustInfoInteractor(SliderRepository sliderRepository) {
        this.sliderRepository = sliderRepository;
    }

    public void updateAgeCountry(String age, String country){
        Map<String, Object> ageCountryMap = new HashMap<>();
        ageCountryMap.put(Consts.AGE, age);
        ageCountryMap.put(Consts.COUNTRY, country);
        sliderRepository.updateFieldFromCurrentUser(ageCountryMap, () -> {});
    }

    public void uploadPhoto(Uri photoUri, UpdateCallback endCallback) {
        sliderRepository.uploadUserPhoto(photoUri, endCallback);
    }

}
