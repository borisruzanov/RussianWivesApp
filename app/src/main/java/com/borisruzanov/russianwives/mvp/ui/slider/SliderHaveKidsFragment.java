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
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingManager;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderHaveKidsFragment extends Fragment {
    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public SliderHaveKidsFragment() {
        // Required empty public constructor
    }

    public static SliderHaveKidsFragment newInstance() {
        SliderHaveKidsFragment fragment = new SliderHaveKidsFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_number_of_kids, container, false);
        radioGroup = view.findViewById(R.id.fragment_slider_number_of_kids_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_numberofkids_btn_save);

        new SliderRepository().getFieldFromCurrentUser(Consts.NUMBER_OF_KIDS, value -> {
            if (value != null && value.equals(getString(R.string.no))){
                radioGroup.check(R.id.fragment_slider_number_of_kids_rbtn_no);
            } else if (value != null && value.equals(getString(R.string.yes_they_sometimes_live_at_home))){
                radioGroup.check(R.id.fragment_slider_number_of_kids_rbtn_sometimes_at_home);
            } else if (value != null && value.equals(getString(R.string.yes_they_live_away_from_home))){
                radioGroup.check(R.id.fragment_slider_number_of_kids_rbtn_away);
            }else if (value != null && value.equals(getString(R.string.yes_they_live_at_home))){
                radioGroup.check(R.id.fragment_slider_number_of_kids_rbtn_at_home);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId > 0) {
                radioButton = view.findViewById(selectedId);
                if (radioButton.getText() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("number_of_kids", radioButton.getText());
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.number_of_kids_updated), Toast.LENGTH_LONG).show();
                    });
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
