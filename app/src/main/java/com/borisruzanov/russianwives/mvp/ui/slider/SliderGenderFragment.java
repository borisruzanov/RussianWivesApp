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

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderGenderFragment extends MvpAppCompatFragment {
    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;


    public SliderGenderFragment() {
        // Required empty public constructor
    }

    public static SliderGenderFragment newInstance() {
        SliderGenderFragment fragment = new SliderGenderFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_gender, container, false);
        radioGroup = view.findViewById(R.id.fragment_slider_gender_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_gender_btn_save);

        new SliderRepository().getFieldFromCurrentUser(Consts.GENDER, value -> {
            if (value != null && value.equals(getString(R.string.female_option))) {
                radioGroup.check(R.id.fragment_slider_gender_radiobutton_female);
            } else if (value != null && value.equals(getString(R.string.male_option))) {
                radioGroup.check(R.id.fragment_slider_gender_radiobutton_male);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId > 0) {
                radioButton = view.findViewById(selectedId);
                if (radioButton.getText() != null) {
                    Map<String, Object> map = new HashMap<>();
                    String result = "";
                    if (Locale.getDefault().getLanguage().equals("ru")) {
                        result = extractValueForDb();
                    } else {
                        result = (String) radioButton.getText();
                    }
                    map.put(Consts.GENDER, result);
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.gender_updated), Toast.LENGTH_LONG).show();

                    });
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            }
        });
        translateButtonsToRu();
        return view;
    }


    private void translateButtonsToRu() {
        if (Locale.getDefault().getLanguage().equals("ru")) {
            RadioButton radioSlender = (RadioButton) radioGroup.getChildAt(0);
            if (radioSlender.getText().equals(getString(R.string.male_option))) {
                radioSlender.setText(getString(R.string.ru_male_option));
            } else {
                radioSlender.setVisibility(View.GONE);
            }

            RadioButton radioAverage = (RadioButton) radioGroup.getChildAt(1);
            if (radioAverage.getText().equals(getString(R.string.female_option))) {
                radioAverage.setText(getString(R.string.ru_female_option));
            } else {
                radioAverage.setVisibility(View.GONE);
            }
        }
    }

    private String extractValueForDb() {
        String result = "";
        if (radioButton.getText().equals(getActivity().getString(R.string.ru_male_option))) {
            result = getActivity().getString(R.string.male_option);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_female_option))) {
            result = getActivity().getString(R.string.female_option);
        } else {
            result = getActivity().getString(R.string.default_text);
        }
        return result;
    }


}
