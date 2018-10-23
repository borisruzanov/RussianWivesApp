package com.borisruzanov.russianwives.mvp.model.interactor.filter

import com.borisruzanov.russianwives.models.SearchModel
import com.borisruzanov.russianwives.mvp.model.repository.filter.FilterRepository
import javax.inject.Inject

class FilterInteractor @Inject constructor(private val filterRepository: FilterRepository) {

    val prefsValues: List<String>
        get() = filterRepository.prefsValues

    fun setPrefsValues(searchModels: List<SearchModel>) {
        filterRepository.setPrefsValues(searchModels)
    }

    /*public String setValue(String key){
        return filterRepository.setValue(key);
    }

    public void setValue(String key, String value){
        filterRepository.setValue(key, value);
    }*/

}
