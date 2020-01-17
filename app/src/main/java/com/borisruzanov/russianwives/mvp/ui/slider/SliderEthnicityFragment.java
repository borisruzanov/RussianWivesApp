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
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_ETHNICITY_RATING;

public class SliderEthnicityFragment extends Fragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public SliderEthnicityFragment() {
        // Required empty public constructor
    }

    public static SliderEthnicityFragment newInstance() {
        SliderEthnicityFragment fragment = new SliderEthnicityFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_slider_ethnicity, container, false);

        radioGroup = view.findViewById(R.id.fragment_slider_ethnicity_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_ethnicity_btn_save);

        new SliderRepository().getFieldFromCurrentUser(Consts.ETHNICITY, value -> {
            if (value != null && value.equals(getString(R.string.asian))) {
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_asian);
            } else if (value != null && value.equals(getString(R.string.black_african_descent))) {
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_black);
            } else if (value != null && value.equals(getString(R.string.east_indian))) {
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_indian);
            } else if (value != null && value.equals(getString(R.string.latino_hispanic))) {
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_latino);
            } else if (value != null && value.equals(getString(R.string.native_american))) {
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_native);
            } else if (value != null && value.equals(getString(R.string.white_caucasian))) {
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_white);
            } else if (value != null && value.equals(getString(R.string.other))) {
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_other);
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
                    map.put(Consts.ETHNICITY, result);
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.ethnicity_updated), Toast.LENGTH_LONG).show();
                        EventBus.getDefault().post(new StringEvent("next_page"));
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
            if (radioSlender.getText().equals(getString(R.string.asian))) {
                radioSlender.setText(getString(R.string.ru_asian));
            } else {
                radioSlender.setVisibility(View.GONE);
            }

            RadioButton radioAverage = (RadioButton) radioGroup.getChildAt(1);
            if (radioAverage.getText().equals(getString(R.string.black_african_descent))) {
                radioAverage.setText(getString(R.string.ru_black_african_descent));
            } else {
                radioAverage.setVisibility(View.GONE);
            }

            RadioButton radioAthletic = (RadioButton) radioGroup.getChildAt(2);
            if (radioAthletic.getText().equals(getString(R.string.east_indian))) {
                radioAthletic.setText(getString(R.string.ru_east_indian));
            } else {
                radioAthletic.setVisibility(View.GONE);
            }

            RadioButton radioHeavyset = (RadioButton) radioGroup.getChildAt(3);
            if (radioHeavyset.getText().equals(getString(R.string.latino_hispanic))) {
                radioHeavyset.setText(getString(R.string.ru_latino_hispanic));
            } else {
                radioHeavyset.setVisibility(View.GONE);
            }

            RadioButton radioNativeAmerican = (RadioButton) radioGroup.getChildAt(4);
            if (radioNativeAmerican.getText().equals(getString(R.string.native_american))) {
                radioNativeAmerican.setText(getString(R.string.ru_native_american));
            } else {
                radioNativeAmerican.setVisibility(View.GONE);
            }


            RadioButton radioWhite = (RadioButton) radioGroup.getChildAt(5);
            if (radioWhite.getText().equals(getString(R.string.white_caucasian))) {
                radioWhite.setText(getString(R.string.ru_white_caucasian));
            } else {
                radioWhite.setVisibility(View.GONE);
            }

            RadioButton radioOther = (RadioButton) radioGroup.getChildAt(6);
            if (radioOther.getText().equals(getString(R.string.other))) {
                radioOther.setText(getString(R.string.ru_other));
            } else {
                radioOther.setVisibility(View.GONE);
            }

        }
    }

    private String extractValueForDb() {
        String result = "";
        if (radioButton.getText().equals(getActivity().getString(R.string.ru_asian))) {
            result = getActivity().getString(R.string.asian);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_black_african_descent))) {
            result = getActivity().getString(R.string.black_african_descent);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_east_indian))) {
            result = getActivity().getString(R.string.east_indian);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_latino_hispanic))) {
            result = getActivity().getString(R.string.latino_hispanic);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_native_american))) {
            result = getActivity().getString(R.string.native_american);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_white_caucasian))) {
            result = getActivity().getString(R.string.white_caucasian);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_other))) {
            result = getActivity().getString(R.string.other);
        }  else {
            result = getActivity().getString(R.string.default_text);
        }
        return result;
    }
}
