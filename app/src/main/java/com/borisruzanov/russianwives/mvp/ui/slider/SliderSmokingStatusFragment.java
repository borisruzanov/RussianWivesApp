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
import com.borisruzanov.russianwives.utils.ValueCallback;

import java.util.HashMap;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_SMOKE_STATUS_RATING;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderSmokingStatusFragment extends Fragment {
    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public SliderSmokingStatusFragment() {
        // Required empty public constructor
    }

    public static SliderSmokingStatusFragment newInstance() {
        SliderSmokingStatusFragment fragment = new SliderSmokingStatusFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_smoking_status, container, false);
        radioGroup = view.findViewById(R.id.fragment_slider_smoking_status_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_smokestatus_btn_save);

        new SliderRepository().getFieldFromCurrentUser(Consts.SMOKING_STATUS, value -> {
            if (value != null && value.equals(getString(R.string.no_way))){
                radioGroup.check(R.id.fragment_slider_smoking_status_rbtn_no_way);
            } else if (value != null && value.equals(getString(R.string.occasionally))){
                radioGroup.check(R.id.fragment_slider_smoking_status_rbtn_occasioally);
            } else if (value != null && value.equals(getString(R.string.daily))){
                radioGroup.check(R.id.fragment_slider_smoking_status_rbtn_daily);
            }else if (value != null && value.equals(getString(R.string.cigar_aficionado))){
                radioGroup.check(R.id.fragment_slider_smokine_status_rbtn_cigar_aficionado);
            }else if (value != null && value.equals(getString(R.string.yes_but_trying_to_quit))){
                radioGroup.check(R.id.fragment_slider_smokine_status_rbtn_quit);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId > 0) {
                radioButton = view.findViewById(selectedId);
                if (radioButton.getText() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(Consts.SMOKING_STATUS, radioButton.getText());
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.smoking_updated), Toast.LENGTH_LONG).show();
                    });
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            }
        });
        return view;    }

}
