package com.borisruzanov.russianwives.mvp.ui.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.NecessaryInfoCallback
import com.borisruzanov.russianwives.utils.StringsCallback
import com.borisruzanov.russianwives.utils.ValueCallback
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(private val mainInteractor: MainInteractor) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        //UserRepository().getTokens { value -> Log.d("TokensDebug", value)}

        //UserRepository().addInfo()

        if(isUserExist()) {
            /*RatingRepository().addRating(10, "Necessary Info")
            RatingRepository().getRatingAch {rating, achievements ->
                run {
                    Log.d("RatingDebug", "Rating is $rating")
                    for(achievement in achievements) Log.d("RatingDebug", "Achievement is $achievement")
                }
            }*/
            showNecessaryInfoDialog()
            showAdditionalInfoDialog()
        }
    }

    fun checkForUserExist() = viewState.setAdapter(mainInteractor.isUserExist)

    fun isUserExist(): Boolean = mainInteractor.isUserExist

    fun saveUser() = mainInteractor.saveUser()

    fun setNecessaryInfo(gender: String, age: String) {
        mainInteractor.setNecessaryInfo(gender, age)
    }

    fun openSliderWithDefaults() {
        mainInteractor.hasAdditionalInfo(callback = StringsCallback { strings -> viewState.openSlider(ArrayList<String>(strings)) })
    }

    private fun showNecessaryInfoDialog() {
            mainInteractor.getNecessaryInfo(callback = NecessaryInfoCallback { gender, age ->
                viewState.showNecessaryInfoDialog(gender, age)
            })
        }

    private fun showAdditionalInfoDialog() {
        mainInteractor.hasNecessaryInfo(callback = BoolCallback { flag -> if (flag) viewState.showAdditionalInfoDialog() })
    }

}
