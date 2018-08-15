package com.borisruzanov.russianwives.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.borisruzanov.russianwives.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ActivitiesFragment extends Fragment {

    //TODO Implement Chats fragment functionality
    private View mMainView;


    public ActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_main_tab_chats, container, false);
        // Inflate the layout for this fragment
        return mMainView;
    }

}