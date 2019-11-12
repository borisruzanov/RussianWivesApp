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
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_AGE_RATING;
import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_RELATIONSHIP_RATING;

public class SliderRelationshipsStatusFragment extends Fragment {
    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public SliderRelationshipsStatusFragment() {
        // Required empty public constructor
    }

    public static SliderRelationshipsStatusFragment newInstance() {
        SliderRelationshipsStatusFragment fragment = new SliderRelationshipsStatusFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider_relationships_status, container, false);
        radioGroup = view.findViewById(R.id.fragment_slider_relationships_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_relationships_btn_save);

        new SliderRepository().getFieldFromCurrentUser(Consts.RELATIONSHIP_STATUS, value -> {
            if (value != null && value.equals(getString(R.string.never_married))){
                radioGroup.check(R.id.fragment_slider_rbtn_never_married);
            } else if (value != null && value.equals(getString(R.string.currently_separated))){
                radioGroup.check(R.id.fragment_slider_rbtn_separated);
            } else if (value != null && value.equals(getString(R.string.divorced))){
                radioGroup.check(R.id.fragment_slider_rbtn_divorced);
            }else if (value != null && value.equals(getString(R.string.widow_widower))){
                radioGroup.check(R.id.fragment_slider_rbtn_widow);
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
                    map.put(Consts.RELATIONSHIP_STATUS, result);
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.relationship_was_updated), Toast.LENGTH_LONG).show();
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
            if (radioSlender.getText().equals(getString(R.string.never_married))) {
                radioSlender.setText(getString(R.string.ru_never_married));
            } else {
                radioSlender.setVisibility(View.GONE);
            }

            RadioButton radioAverage = (RadioButton) radioGroup.getChildAt(1);
            if (radioAverage.getText().equals(getString(R.string.currently_separated))) {
                radioAverage.setText(getString(R.string.ru_currently_separated));
            } else {
                radioAverage.setVisibility(View.GONE);
            }

            RadioButton radioAthletic = (RadioButton) radioGroup.getChildAt(2);
            if (radioAthletic.getText().equals(getString(R.string.divorced))) {
                radioAthletic.setText(getString(R.string.ru_divorced));
            } else {
                radioAthletic.setVisibility(View.GONE);
            }

            RadioButton radioHeavyset = (RadioButton) radioGroup.getChildAt(3);
            if (radioHeavyset.getText().equals(getString(R.string.widow_widower))) {
                radioHeavyset.setText(getString(R.string.ru_widow_widower));
            } else {
                radioHeavyset.setVisibility(View.GONE);
            }
        }
    }

    private String extractValueForDb() {
        String result = "";
        if (radioButton.getText().equals(getActivity().getString(R.string.ru_never_married))) {
            result = getActivity().getString(R.string.never_married);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_currently_separated))) {
            result = getActivity().getString(R.string.currently_separated);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_divorced))) {
            result = getActivity().getString(R.string.divorced);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_widow_widower))) {
            result = getActivity().getString(R.string.widow_widower);
        }  else {
            result = getActivity().getString(R.string.default_text);
        }
        return result;
    }


}
