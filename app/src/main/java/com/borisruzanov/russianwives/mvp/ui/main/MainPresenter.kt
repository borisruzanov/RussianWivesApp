package com.borisruzanov.russianwives.mvp.ui.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor
import com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.FULL_PROFILE_ACH
import com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.MUST_INFO_ACH
import com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.MUST_INFO_LIST
import com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_AGE_RATING
import com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_GENDER_RATING
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository
import com.borisruzanov.russianwives.utils.*
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(private val mainInteractor: MainInteractor) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        showGenderDialog()
        mainInteractor.setFirstOpenDate()

        if(isUserExist()) {
            showMustInfoDialog()
            showAdditionalInfoDialog()
            checkAchieve()
        }
    }

    fun checkForUserExist() = viewState.setAdapter(mainInteractor.isUserExist)

    fun isUserExist(): Boolean = mainInteractor.isUserExist

    fun saveUser() = mainInteractor.saveUser()

    fun setGender(gender: String) {
        mainInteractor.setGender(gender)
    }

    fun makeDialogOpenDateDefault() = mainInteractor.makeDialogOpenDateDefault()

    fun openSliderWithDefaults() {
        mainInteractor.getDefaultList(callback = StringsCallback { strings ->
            viewState.openSlider(ArrayList<String>(strings)) })
    }

    private fun checkAchieve () {
        RatingRepository().isAchievementExist(FULL_PROFILE_ACH, callback = BoolCallback {
            if (it) Log.d("AchDebug", "User can write msgs")
            else Log.d("AchDebug", "USER CAN'T WRITE")
        })
    }

    private fun showGenderDialog() {
        if (mainInteractor.isGenderDefault()) {
            viewState.showGenderDialog()
        }
    }

    private fun showMustInfoDialog() {
        mainInteractor.hasDefaultMustInfo(callback = BoolCallback {flag ->
            if (flag) viewState.showMustInfoDialog()
        })
    }

    private fun showAdditionalInfoDialog() {
        mainInteractor.hasNecessaryInfo(callback = BoolCallback { flag ->
            if (flag) {
            viewState.showAdditionalInfoDialog()
            mainInteractor.setDialogLastOpenDate()
        }
        })
    }

}
