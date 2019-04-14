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
            if (value != null && value.equals(getString(R.string.asian))){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_asian);
            } else if (value != null && value.equals(getString(R.string.black_african_descent))){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_black);
            } else if (value != null && value.equals(getString(R.string.east_indian))){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_indian);
            }else if (value != null && value.equals(getString(R.string.latino_hispanic))){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_latino);
            }else if (value != null && value.equals(getString(R.string.native_american))){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_native);
            }else if (value != null && value.equals(getString(R.string.white_caucasian))){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_white);
            }else if (value != null && value.equals(getString(R.string.other))){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_other);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId > 0) {
                radioButton = view.findViewById(selectedId);
                if (radioButton.getText() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(Consts.ETHNICITY, radioButton.getText());
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.ethnicity_updated), Toast.LENGTH_LONG).show();
                    });
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
