package com.borisruzanov.russianwives.ui.global;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface ViewTypeDelegateAdapter {

    //TODO REMOVE OR DELETE?

    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

    void onBindViewHolder(RecyclerView.ViewHolder holder, ViewType item);

}
