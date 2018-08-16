package com.borisruzanov.russianwives.mvp.view;

import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface FilterView extends MvpView {

    void getSavedValues(List<String> stringList, List<Integer> resIdList);

}