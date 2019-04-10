package com.borisruzanov.russianwives.mvp.ui.slider;


import android.os.Bundle;
import android.util.Log;
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

        new SliderRepository().getFieldFromCurrentUser(Consts.BODY_TYPE, value -> {
            result = value;
            if (value != null && value.equals(getString(R.string.slender))) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_slender);
            } else if (value != null && value.equals(getString(R.string.about_average))) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_average);
            } else if (value != null && value.equals(getString(R.string.athletic))) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_athletic);
            } else if (value != null && value.equals(getString(R.string.heavyset))) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_heavyset);
            } else if (value != null && value.equals(getString(R.string.a_few_extra_pounds))) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_fewextra);
            } else if (value != null && value.equals(getString(R.string.stocky))) {
                radioGroup.check(R.id.fragment_slider_bodytype_rbtn_stocky);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId > 0) {
                radioButton = view.findViewById(selectedId);
                if (radioButton.getText() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(Consts.BODY_TYPE, radioButton.getText());
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (result.equals(Consts.DEFAULT))
                            new RatingRepository().addRating(ADD_BODY_TYPE_RATING);
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.bkodytype_updated), Toast.LENGTH_LONG).show();
                    });
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}