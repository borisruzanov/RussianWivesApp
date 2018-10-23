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
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Map;

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

        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_ethnicity_radiogroup);
        btnSave = (Button) view.findViewById(R.id.fragment_slider_ethnicity_btn_save);

        new SliderRepository().getFieldFromCurrentUser("ethnicity", value -> {
            if (value != null && value.equals("Asian")){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_asian);
            } else if (value != null && value.equals("Black / African descent")){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_black);
            } else if (value != null && value.equals("East Indian")){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_indian);
            }else if (value != null && value.equals("Latino / Hispanic")){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_latino);
            }else if (value != null && value.equals("Native American")){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_native);
            }else if (value != null && value.equals("White / Caucasian")){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_white);
            }else if (value != null && value.equals("Other")){
                radioGroup.check(R.id.fragment_slider_ethnicity_radiobtn_other);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) view.findViewById(selectedId);
            if(radioButton.getText() != null){
                Map<String, Object> map = new HashMap<>();
                map.put("ethnicity", radioButton.getText());
                new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                    if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                        getActivity().onBackPressed();
                    }
                    Toast.makeText(getActivity(), R.string.ethnicity_updated, Toast.LENGTH_LONG).show();
                });
            }
        });

        return view;
    }

}
