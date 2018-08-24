package com.borisruzanov.russianwives.mvp.presenter;

import android.widget.Spinner;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.SearchModel;
import com.borisruzanov.russianwives.mvp.model.interactor.FilterInteractor;
import com.borisruzanov.russianwives.mvp.view.FilterView;
import com.borisruzanov.russianwives.utils.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@InjectViewState
public class FilterDialogPresenter extends MvpPresenter<FilterView> {

    private FilterInteractor interactor;

    public FilterDialogPresenter(FilterInteractor interactor) {
        this.interactor = interactor;
    }

    public void getSavedValues() {
        List<Integer> resIdList = Arrays.asList(R.array.genders, R.array.age_types, R.array.countries,
                R.array.relationships_statuses, R.array.body_types, R.array.ethnicities, R.array.faith_types,
                R.array.smoke_statuses, R.array.drink_statuses, R.array.have_kids_statuses, R.array.want_kids_statuses);

        getViewState().getSavedValues(interactor.getPrefsValues(), resIdList);
    }

    public void saveValues(List<Spinner> spinners) {
        List<SearchModel> models = new ArrayList<>();
        List<String> keyList = Consts.keyList;
        for (int i = 0; i < spinners.size(); i++) {
            String item = (String) spinners.get(i).getSelectedItem();
            models.add(new SearchModel(keyList.get(i), item));
        }
        interactor.setPrefsValues(models);
    }

    public boolean isNotDefault(String value) {
        return !value.equals(Consts.DEFAULT);
    }

}