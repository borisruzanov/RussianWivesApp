package com.borisruzanov.russianwives.mvp.ui.slider;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;

import java.util.HashMap;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_FAITH_RATING;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderFaithFragment extends Fragment {
    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    String result = "";

    public SliderFaithFragment() {
        // Required empty public constructor
    }

    public static SliderFaithFragment newInstance() {
        SliderFaithFragment fragment = new SliderFaithFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider_faith, container, false);
        radioGroup = view.findViewById(R.id.fragment_slider_faith_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_faith_btn_save);

        new SliderRepository().getFieldFromCurrentUser(Consts.FAITH, value -> {
            if (value == null || value.equals(Consts.DEFAULT)) result = Consts.DEFAULT;
            if (value != null && value.equals(getString(R.string.christian))){
                radioGroup.check(R.id.fragment_slider_faith_rbtn_christian);
            } else if (value != null && value.equals(getString(R.string.black_african_descent))){
                radioGroup.check(R.id.fragment_slider_faith_rbtn_black);
            } else if (value != null && value.equals(getString(R.string.muslim))){
                radioGroup.check(R.id.fragment_slider_faith_rbtn_muslim);
            }else if (value != null && value.equals(getString(R.string.atheist))){
                radioGroup.check(R.id.fragment_slider_faith_rbtn_atheist);
            }else if (value != null && value.equals(getString(R.string.buddhist))){
                radioGroup.check(R.id.fragment_slider_faith_rbtn_buddist);
            }else if (value != null && value.equals(getString(R.string.adventist))){
                radioGroup.check(R.id.fragment_slider_faith_rbtn_adventist);
            } else if (value != null && value.equals(getString(R.string.other))){
                radioGroup.check(R.id.fragment_slider_faith_rbtn_other);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId > 0) {
                radioButton = view.findViewById(selectedId);
                if (radioButton.getText() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(Consts.FAITH, radioButton.getText());
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (result.equals(Consts.DEFAULT))
                            new RatingRepository().addRating(ADD_FAITH_RATING);
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null)getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), R.string.faith_updated, Toast.LENGTH_LONG).show();
                    });
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
