package com.borisruzanov.russianwives.ui.pagination;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.ui.global.ViewType;
import com.borisruzanov.russianwives.ui.global.ViewTypeDelegateAdapter;

/**
 * Created by Михаил on 02.05.2018.
 */

public class LoadingDelegateAdapter implements ViewTypeDelegateAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
        return new LoadingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, ViewType item) {}

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar pb;

        LoadingViewHolder(View itemView) {
            super(itemView);
            pb = itemView.findViewById(R.id.pb_loading);
        }

    }

}
