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

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_FAITH_RATING;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderFaithFragment extends Fragment {
    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

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
                    String result = "";
                    if (Locale.getDefault().getLanguage().equals("ru")) {
                        result = extractValueForDb();
                    } else {
                        result = (String) radioButton.getText();
                    }
                    map.put(Consts.FAITH, result);
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.faith_updated), Toast.LENGTH_LONG).show();
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
            if (radioSlender.getText().equals(getString(R.string.christian))) {
                radioSlender.setText(getString(R.string.ru_christian));
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
            if (radioAthletic.getText().equals(getString(R.string.muslim))) {
                radioAthletic.setText(getString(R.string.ru_muslim));
            } else {
                radioAthletic.setVisibility(View.GONE);
            }

            RadioButton radioHeavyset = (RadioButton) radioGroup.getChildAt(3);
            if (radioHeavyset.getText().equals(getString(R.string.atheist))) {
                radioHeavyset.setText(getString(R.string.ru_atheist));
            } else {
                radioHeavyset.setVisibility(View.GONE);
            }

            RadioButton radioNativeAmerican = (RadioButton) radioGroup.getChildAt(4);
            if (radioNativeAmerican.getText().equals(getString(R.string.buddhist))) {
                radioNativeAmerican.setText(getString(R.string.ru_buddist));
            } else {
                radioNativeAmerican.setVisibility(View.GONE);
            }


            RadioButton radioWhite = (RadioButton) radioGroup.getChildAt(5);
            if (radioWhite.getText().equals(getString(R.string.adventist))) {
                radioWhite.setText(getString(R.string.ru_adventist));
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
        if (radioButton.getText().equals(getActivity().getString(R.string.ru_christian))) {
            result = getActivity().getString(R.string.christian);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_black_african_descent))) {
            result = getActivity().getString(R.string.black_african_descent);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_muslim))) {
            result = getActivity().getString(R.string.muslim);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_atheist))) {
            result = getActivity().getString(R.string.atheist);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_buddist))) {
            result = getActivity().getString(R.string.buddhist);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_adventist))) {
            result = getActivity().getString(R.string.adventist);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_other))) {
            result = getActivity().getString(R.string.other);
        }  else {
            result = getActivity().getString(R.string.default_text);
        }
        return result;
    }
}
