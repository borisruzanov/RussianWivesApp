package com.borisruzanov.russianwives.ui.global;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Михаил on 02.05.2018.
 */

public interface ViewTypeDelegateAdapter {

    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

    void onBindViewHolder(RecyclerView.ViewHolder holder, ViewType item);

}
