package com.borisruzanov.russianwives.mvp.model.repository;

import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.utils.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterRepository {

    private Prefs prefs;

    public FilterRepository(Prefs prefs) {
        this.prefs = prefs;
    }

    public List<SearchModel> getSearchResult(){
        List<SearchModel> searchList = getUnfilteredSearchResult();
        for (int i = 0; i < searchList.size(); i++) {
            if(isDefault(searchList.get(i).getValue())) searchList.remove(i);
        }
        return searchList;
    }

    public List<String> getPrefsValues(){
        return Arrays.asList(prefs.getGender(), prefs.getRelationshipStatus(), prefs.getBodyType(), prefs.getEthnicity(),
                prefs.getFaith(), prefs.getSmokingStatus(), prefs.getDrinkStatus(), prefs.getNumberOfKids(), prefs.getWantChilderOrNot());
    }

    public void setPrefsValues(List<SearchModel> searchModels){
        for (SearchModel model: searchModels){
            prefs.setValue(model.getKey(), model.getValue());
        }
    }

    public void setValue(String key, String value){
        prefs.setValue(key, value);
    }

    public String getValue(String key){
        return prefs.getValue(key);
    }

    private List<SearchModel> getUnfilteredSearchResult(){
        return new ArrayList<>(Arrays.asList(new SearchModel(Consts.GENDER, prefs.getGender()),
                new SearchModel(Consts.RELATIONSHIP_STATUS, prefs.getRelationshipStatus()),
                new SearchModel(Consts.BODY_TYPE, prefs.getBodyType()),
                new SearchModel(Consts.ETHNICITY, prefs.getEthnicity()),
                new SearchModel(Consts.FAITH, prefs.getFaith()),
                new SearchModel(Consts.SMOKING_STATUS, prefs.getSmokingStatus()),
                new SearchModel(Consts.DRINK_STATUS, prefs.getDrinkStatus()),
                new SearchModel(Consts.NUMBER_OF_KIDS, prefs.getNumberOfKids()),
                new SearchModel(Consts.WANT_CHILDREN_OR_NOT, prefs.getWantChilderOrNot())));
        /* new SearchModel(Consts.HOBBY, prefs.getHobby()));*/
    }

    private boolean isDefault(String value){
        return value.equals(Consts.DEFAULT);
    }

}