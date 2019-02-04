package com.borisruzanov.russianwives.mvp.ui.slider;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_BODY_TYPE_RATING;

public class SliderBodytypeFragment extends MvpAppCompatFragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    String result;


    public SliderBodytypeFragment() {
        // Required empty public constructor
    }


    public static SliderBodytypeFragment newInstance() {
        SliderBodytypeFragment fragment = new SliderBodytypeFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_bodytype, container, false);
        radioGroup = view.findViewById(R.id.fragment_slider_bodytype_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_bodytype_btn_save);

        new SliderRepository().getFieldFromCurrentUser("body_type", value -> {
            result = value;
            if (value != null && value.equals("Slender")) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_slender);
            } else if (value != null && value.equals("About average")) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_average);
            } else if (value != null && value.equals("Athletic")) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_athletic);
            } else if (value != null && value.equals("Heavyset")) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_heavyset);
            } else if (value != null && value.equals("A few extra pounds")) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_fewextra);
            } else if (value != null && value.equals("Stocky")) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_stocky);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = view.findViewById(selectedId);
            if (radioButton.getText() != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("body_type", radioButton.getText());
                new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                    if (result.equals(Consts.DEFAULT)) new RatingRepository().addRating(ADD_BODY_TYPE_RATING);
                    if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                        getActivity().onBackPressed();
                    }
                    Toast.makeText(getActivity(), R.string.bkodytype_updated, Toast.LENGTH_LONG).show();
                });
            }
        });
        return view;
    }

}