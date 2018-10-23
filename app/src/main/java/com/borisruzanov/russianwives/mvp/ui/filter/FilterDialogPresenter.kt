package com.borisruzanov.russianwives.mvp.ui.filter

import android.widget.Spinner

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.borisruzanov.russianwives.R
import com.borisruzanov.russianwives.models.SearchModel
import com.borisruzanov.russianwives.mvp.model.interactor.filter.FilterInteractor
import com.borisruzanov.russianwives.utils.Consts

import java.util.ArrayList
import java.util.Arrays
import javax.inject.Inject

@InjectViewState
class FilterDialogPresenter @Inject constructor(private val interactor: FilterInteractor) : MvpPresenter<FilterView>() {

    fun getSavedValues() {
        val resIdList = Arrays.asList(R.array.genders, R.array.age_types, R.array.countries,
                R.array.relationships_statuses, R.array.body_types, R.array.ethnicities, R.array.faith_types,
                R.array.smoke_statuses, R.array.drink_statuses, R.array.have_kids_statuses, R.array.want_kids_statuses)

        viewState.getSavedValues(interactor.prefsValues, resIdList)
    }

    fun saveValues(spinners: List<Spinner>) {
        val models = ArrayList<SearchModel>()
        val keyList = Consts.keyList
        for (i in spinners.indices) {
            val item = spinners[i].selectedItem as String
            models.add(SearchModel(keyList[i], item))
        }
        interactor.setPrefsValues(models)
    }

    fun isNotDefault(value: String): Boolean {
        return value != Consts.DEFAULT
    }

}