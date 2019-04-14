package com.borisruzanov.russianwives.mvp.model.interactor.mustinfo;

import android.net.Uri;

import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingManager;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.MUST_INFO_ACH;

public class MustInfoInteractor {

    private SliderRepository sliderRepository;
    private UserRepository userRepository;
    private RatingRepository ratingRepository;

    @Inject
    public MustInfoInteractor(SliderRepository sliderRepository, UserRepository userRepository, RatingRepository ratingRepository) {
        this.sliderRepository = sliderRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
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

    public void addMustInfoAchieve() {
        ratingRepository.addAchievement(MUST_INFO_ACH);
    }

    public void addRating() {
        userRepository.addRating(RatingManager.USER_MUST_INFO);
    }

}
