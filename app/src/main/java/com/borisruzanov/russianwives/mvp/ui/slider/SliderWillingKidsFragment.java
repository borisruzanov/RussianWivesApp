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

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_WILL_OF_KIDS_RATING;

public class SliderWillingKidsFragment extends Fragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public SliderWillingKidsFragment() {
        // Required empty public constructor
    }

    public static SliderWillingKidsFragment newInstance() {
        SliderWillingKidsFragment fragment = new SliderWillingKidsFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_willing_kids, container, false);
        radioGroup = view.findViewById(R.id.fragment_slider_willingkids_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_willingkids_btn_save);

        new SliderRepository().getFieldFromCurrentUser(Consts.WANT_CHILDREN_OR_NOT, value -> {
            if (value != null && value.equals(getString(R.string.definitely))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_definitely);
            } else if (value != null && value.equals(getString(R.string.someday))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_someday);
            } else if (value != null && value.equals(getString(R.string.not_sure))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_not_sure);
            } else if (value != null && value.equals(getString(R.string.probably_not))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_probably);
            } else if (value != null && value.equals(getString(R.string.no_but_its_ok_if_partner_has_kids))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_no_but_ok);
            } else if (value != null && value.equals(getString(R.string.no))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_no);
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
                    map.put(Consts.WANT_CHILDREN_OR_NOT, result);
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.willing_kids_updated), Toast.LENGTH_LONG).show();
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
            if (radioSlender.getText().equals(getString(R.string.definitely))) {
                radioSlender.setText(getString(R.string.ru_definitely));
            } else {
                radioSlender.setVisibility(View.GONE);
            }

            RadioButton radioAverage = (RadioButton) radioGroup.getChildAt(1);
            if (radioAverage.getText().equals(getString(R.string.someday))) {
                radioAverage.setText(getString(R.string.ru_someday));
            } else {
                radioAverage.setVisibility(View.GONE);
            }

            RadioButton radioAthletic = (RadioButton) radioGroup.getChildAt(2);
            if (radioAthletic.getText().equals(getString(R.string.not_sure))) {
                radioAthletic.setText(getString(R.string.ru_not_sure));
            } else {
                radioAthletic.setVisibility(View.GONE);
            }

            RadioButton radioHeavyset = (RadioButton) radioGroup.getChildAt(3);
            if (radioHeavyset.getText().equals(getString(R.string.probably_not))) {
                radioHeavyset.setText(getString(R.string.ru_probably_not));
            } else {
                radioHeavyset.setVisibility(View.GONE);
            }

            RadioButton radioNativeAmerican = (RadioButton) radioGroup.getChildAt(4);
            if (radioNativeAmerican.getText().equals(getString(R.string.no))) {
                radioNativeAmerican.setText(getString(R.string.ru_no));
            } else {
                radioNativeAmerican.setVisibility(View.GONE);
            }


            RadioButton radioWhite = (RadioButton) radioGroup.getChildAt(5);
            if (radioWhite.getText().equals(getString(R.string.no_but_its_ok_if_partner_has_kids))) {
                radioWhite.setText(getString(R.string.ru_no_but_its_ok_if_partner_has_kids));
            } else {
                radioWhite.setVisibility(View.GONE);
            }
        }
    }

    private String extractValueForDb() {
        String result = "";
        if (radioButton.getText().equals(getActivity().getString(R.string.ru_definitely))) {
            result = getActivity().getString(R.string.definitely);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_someday))) {
            result = getActivity().getString(R.string.someday);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_not_sure))) {
            result = getActivity().getString(R.string.not_sure);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_probably_not))) {
            result = getActivity().getString(R.string.probably_not);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_no))) {
            result = getActivity().getString(R.string.no);
        } else if (radioButton.getText().equals(getActivity().getString(R.string.ru_no_but_its_ok_if_partner_has_kids))) {
            result = getActivity().getString(R.string.no_but_its_ok_if_partner_has_kids);
        } else {
            result = getActivity().getString(R.string.default_text);
        }
        return result;
    }

}
