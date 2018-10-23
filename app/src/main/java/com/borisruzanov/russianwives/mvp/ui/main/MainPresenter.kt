package com.borisruzanov.russianwives.mvp.ui.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor
import com.borisruzanov.russianwives.utils.BoolCallback
import com.borisruzanov.russianwives.utils.NecessaryInfoCallback
import com.borisruzanov.russianwives.utils.StringsCallback
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(private val mainInteractor: MainInteractor) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        if(isUserExist()) {
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
