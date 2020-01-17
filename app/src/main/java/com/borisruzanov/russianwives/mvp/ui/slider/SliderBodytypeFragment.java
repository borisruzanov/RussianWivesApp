package com.borisruzanov.russianwives.mvp.ui.slider;


import android.content.Context;
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
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_BODY_TYPE_RATING;

public class SliderBodytypeFragment extends MvpAppCompatFragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;


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
                    String result = "";
                    if (Locale.getDefault().getLanguage().equals("ru")) {
                        result = extractValueForDb();
                    } else {
                        result = (String) radioButton.getText();
                    }
                    map.put(Consts.BODY_TYPE, result);
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.bkodytype_updated), Toast.LENGTH_LONG).show();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void translateButtonsToRu() {
        if (Locale.getDefault().getLanguage().equals("ru")) {
            RadioButton radioSlender = (RadioButton) radioGroup.getChildAt(0);
            if (radioSlender.getText().equals(getString(R.string.slender))) {
                radioSlender.setText(getString(R.string.ru_slender));
            } else {
                radioSlender.setVisibility(View.GONE);
            }

            RadioButton radioAverage = (RadioButton) radioGroup.getChildAt(1);
            if (radioAverage.getText().equals(getString(R.string.about_average))) {
                radioAverage.setText(getString(R.string.ru_about_average));
            } else {
                radioAverage.setVisibility(View.GONE);
            }

            RadioButton radioAthletic = (RadioButton) radioGroup.getChildAt(2);
            if (radioAthletic.getText().equals(getString(R.string.athletic))) {
                radioAthletic.setText(getString(R.string.ru_athletic));
            } else {
                radioAthletic.setVisibility(View.GONE);
            }

            RadioButton radioHeavyset = (RadioButton) radioGroup.getChildAt(3);
            if (radioHeavyset.getText().equals(getString(R.string.heavyset))) {
                radioHeavyset.setText(getString(R.string.ru_heavyset));
            } else {
                radioHeavyset.setVisibility(View.GONE);
            }

            RadioButton radioFewExtraPounds = (RadioButton) radioGroup.getChildAt(4);
            if (radioFewExtraPounds.getText().equals(getString(R.string.a_few_extra_pounds))) {
                radioFewExtraPounds.setText(getString(R.string.ru_a_few_extra_pounds));
            } else {
                radioFewExtraPounds.setVisibility(View.GONE);
            }

            RadioButton radioStocky = (RadioButton) radioGroup.getChildAt(5);
            if (radioStocky.getText().equals(getString(R.string.stocky))) {
                radioStocky.setText(getString(R.string.ru_stocky));
            } else {
                radioStocky.setVisibility(View.GONE);
            }
        }
    }

    private String extractValueForDb() {
        String result = "";
        if (radioButton.getText().equals(getActivity().getString(R.string.ru_slender))) {
            result = getActivity().getString(R.string.slender);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_about_average))) {
            result = getActivity().getString(R.string.about_average);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_athletic))) {
            result = getActivity().getString(R.string.athletic);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_heavyset))) {
            result = getActivity().getString(R.string.heavyset);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_a_few_extra_pounds))) {
            result = getActivity().getString(R.string.a_few_extra_pounds);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_stocky))) {
            result = getActivity().getString(R.string.stocky);
        } else {
            result = getActivity().getString(R.string.default_text);
        }
        return result;
    }

}