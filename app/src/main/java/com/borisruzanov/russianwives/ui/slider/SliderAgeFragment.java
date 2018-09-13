package com.borisruzanov.russianwives.ui.slider;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;

import java.util.HashMap;
import java.util.Map;


public class SliderAgeFragment extends MvpAppCompatFragment {

    //    SliderFragmentsPresenter sliderFragmentsPresenter;
    RadioGroup radioGroup;
    Button btnSave;
    RadioButton radioButton;

//    String result;

    public SliderAgeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_age, container, false);
//        sliderFragmentsPresenter = new SliderFragmentsPresenter(new SliderInteractor(new FirebaseRepository()), new SliderImageFragment());


        btnSave = (Button) view.findViewById(R.id.fragment_slider_age_btn_save);
        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_age_radiogroup);

        new FirebaseRepository().getFieldFromCurrentUser("age", new ValueCallback() {
            @Override
            public void getValue(String value) {
                if (value != null && value.equals("18-21")){
                    radioGroup.check(R.id.fragment_slider_age_18_21);
                } else if (value != null && value.equals("22-26")){
                    radioGroup.check(R.id.fragment_slider_age_22_26);
                } else if (value != null && value.equals("26-35")){
                    radioGroup.check(R.id.fragment_slider_age_26_35);
                }else if (value != null && value.equals("36-45")){
                    radioGroup.check(R.id.fragment_slider_age_36_45);
                }else if (value != null && value.equals("45+")){
                    radioGroup.check(R.id.fragment_slider_age_45_plus);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) view.findViewById(selectedId);
                if (radioButton.getText() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("age", radioButton.getText());
                    new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                        @Override
                        public void onUpdate() {
                            getActivity().onBackPressed();
                            Toast.makeText(getActivity(), "Age was updated", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}