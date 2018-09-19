package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.mvp.model.repository.FilterRepository;

import java.util.List;

public class FilterInteractor {

    private FilterRepository filterRepository;

    public FilterInteractor(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    public List<String> getPrefsValues(){
        return filterRepository.getPrefsValues();
    }

    public void setPrefsValues(List<SearchModel> searchModels){
        filterRepository.setPrefsValues(searchModels);
    }

    /*public String setValue(String key){
        return filterRepository.setValue(key);
    }

    public void setValue(String key, String value){
        filterRepository.setValue(key, value);
    }*/

}
