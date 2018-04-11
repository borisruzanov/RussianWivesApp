package com.borisruzanov.russianwives.zTEST;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.borisruzanov.russianwives.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUserInfoInput extends Fragment {


    public FragmentUserInfoInput() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_user_info_input, container, false);
    }

}
