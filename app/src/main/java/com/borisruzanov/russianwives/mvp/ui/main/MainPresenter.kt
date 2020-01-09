package com.borisruzanov.russianwives.mvp.ui.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.StringsCallback
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(private val mainInteractor: MainInteractor) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        //mainInteractor.setUserInHot()
//        mainInteractor.getHotUsers(callback = HotUsersCallback { hotUsers ->
//            if (hotUsers.isEmpty()) Log.d("HotUsersDebug", "users is empty")
//            hotUsers.forEach {
//                Log.d("HotUsersDebug", "Uid is ${it.uid}")
//            }
//        })
    }


    fun showGenderFirstDialog(){
        showGenderDialog()
        mainInteractor.setFirstOpenDate()
    }
    fun showDialogs() {
        if (isUserExist()) {
            showMustInfoDialog()
            showAdditionalInfoDialog()
        } else{
            Log.d("AchDebug", "User can write msgs")
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

    /*private fun checkAchieve () {
        RatingRepository().isAchievementExist(FULL_PROFILE_ACH, callback = BoolCallback {
            if (it) Log.d("AchDebug", "User can write msgs")
            else Log.d("AchDebug", "USER CAN'T WRITE")
        })
    }*/

    private fun showGenderDialog() {
        if (mainInteractor.isGenderDefault()) {
//            mView.showGenderDialog()
        }
    }

    fun showMustInfoDialog() {
        mainInteractor.hasDefaultMustInfo(callback = BoolCallback {flag ->
            if (flag) viewState.showMustInfoDialog()
        })
    }

    private fun showAdditionalInfoDialog() {
        mainInteractor.hasNecessaryInfo(callback = BoolCallback { flag ->
            if (flag) {
            viewState.showFullInfoDialog()
            mainInteractor.setFPDialogLastOpenDate()
        }
        })
    }

}
