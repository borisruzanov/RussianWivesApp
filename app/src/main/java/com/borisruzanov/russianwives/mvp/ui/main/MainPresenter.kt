package com.borisruzanov.russianwives.mvp.ui.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor
import com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.MUST_INFO_ACH
import com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.MUST_INFO_LIST
import com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_AGE_RATING
import com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_GENDER_RATING
import com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_IMAGE_RATING
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository
import com.borisruzanov.russianwives.utils.*
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(private val mainInteractor: MainInteractor) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        //UserRepository().getTokens { value -> Log.d("TokensDebug", value)}

        //UserRepository().addInfo()

        if (isUserExist()) {
            //RatingRepository().addRating(10)
            showNecessaryInfoDialog()
            showAdditionalInfoDialog()
        }
    }

    fun checkForUserExist() = viewState.setAdapter(mainInteractor.isUserExist)

    fun isUserExist(): Boolean = mainInteractor.isUserExist

    fun saveUser() = mainInteractor.saveUser()

    fun setNecessaryInfo(image: String, gender: String, age: String) {
        if (image != Consts.DEFAULT) RatingRepository().addRating(ADD_IMAGE_RATING)
        if (gender != Consts.DEFAULT) RatingRepository().addRating(ADD_GENDER_RATING)
        if (age != Consts.DEFAULT) RatingRepository().addRating(ADD_AGE_RATING)
        mainInteractor.setNecessaryInfo(image, gender, age)
        RatingRepository().checkForAchieve(MUST_INFO_LIST, MUST_INFO_ACH)
    }

    fun openSliderWithDefaults() {
        mainInteractor.hasAdditionalInfo(callback = StringsCallback { strings -> viewState.openSlider(ArrayList<String>(strings)) })
    }

    private fun showNecessaryInfoDialog() {
        mainInteractor.getNecessaryInfo(callback = NecessaryInfoCallback { image, gender, age ->
            viewState.showNecessaryInfoDialog(image, gender, age)
        })
    }

    private fun showAdditionalInfoDialog() {
        mainInteractor.hasNecessaryInfo(callback = BoolCallback { flag -> if (flag) viewState.showAdditionalInfoDialog() })
    }

}
