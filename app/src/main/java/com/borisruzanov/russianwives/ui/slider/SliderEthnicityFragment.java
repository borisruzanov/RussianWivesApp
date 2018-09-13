package com.borisruzanov.russianwives.ui.slider;


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
import com.borisruzanov.russianwives.mvp.model.interactor.SliderInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.SliderFragmentsPresenter;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;

import java.util.HashMap;
import java.util.Map;

public class SliderEthnicityFragment extends Fragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;



    public SliderEthnicityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_slider_ethnicity, container, false);

        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_ethnicity_radiogroup);
        btnSave = (Button) view.findViewById(R.id.fragment_slider_ethnicity_btn_save);

        new FirebaseRepository().getFieldFromCurrentUser("ethnicity", new ValueCallback() {
            @Override
            public void getValue(String value) {
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
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) view.findViewById(selectedId);
                if(radioButton.getText() != null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("ethnicity", radioButton.getText());
                    new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                        @Override
                        public void onUpdate() {
                            getActivity().onBackPressed();
                            Toast.makeText(getActivity(), "Ethnicity was updated", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
//        sliderFragmentsPresenter = new SliderFragmentsPresenter(new SliderInteractor(new FirebaseRepository()), new SliderImageFragment());
//
//        btnSave = (Button) view.findViewById(R.id.fragment_slider_ethnicity_btn_save);
//
//        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_ethnicity_radiogroup);
//        asian = (RadioButton) view.findViewById(R.id.fragment_slider_ethnicity_radiobtn_asian);
//        black = (RadioButton) view.findViewById(R.id.fragment_slider_ethnicity_radiobtn_black);
//        indian = (RadioButton) view.findViewById(R.id.fragment_slider_ethnicity_radiobtn_indian);
//        latino = (RadioButton) view.findViewById(R.id.fragment_slider_ethnicity_radiobtn_latino);
//        nativeAmerican = (RadioButton) view.findViewById(R.id.fragment_slider_ethnicity_radiobtn_native);
//        white = (RadioButton) view.findViewById(R.id.fragment_slider_ethnicity_radiobtn_white);
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int id) {
//                switch (id){
//                    case R.id.fragment_slider_ethnicity_radiobtn_asian:
//                        black.setChecked(false);
//                        indian.setChecked(false);
//                        latino.setChecked(false);
//                        nativeAmerican.setChecked(false);
//                        white.setChecked(false);
//                        result = (String) asian.getText();
//                        break;
//                    case R.id.fragment_slider_ethnicity_radiobtn_black:
//                        indian.setChecked(false);
//                        latino.setChecked(false);
//                        nativeAmerican.setChecked(false);
//                        white.setChecked(false);
//                        asian.setChecked(false);
//                        result = (String) black.getText();
//                        break;
//                    case R.id.fragment_slider_ethnicity_radiobtn_indian:
//                        latino.setChecked(false);
//                        nativeAmerican.setChecked(false);
//                        white.setChecked(false);
//                        asian.setChecked(false);
//                        black.setChecked(false);
//                        result = (String) indian.getText();
//                        break;
//                    case R.id.fragment_slider_ethnicity_radiobtn_latino:
//                        nativeAmerican.setChecked(false);
//                        white.setChecked(false);
//                        asian.setChecked(false);
//                        black.setChecked(false);
//                        indian.setChecked(false);
//                        result = (String) latino.getText();
//                        break;
//                    case R.id.fragment_slider_ethnicity_radiobtn_native:
//                        white.setChecked(false);
//                        asian.setChecked(false);
//                        black.setChecked(false);
//                        indian.setChecked(false);
//                        latino.setChecked(false);
//                        result = (String) nativeAmerican.getText();
//                        break;
//                    case R.id.fragment_slider_ethnicity_radiobtn_white:
//                        asian.setChecked(false);
//                        black.setChecked(false);
//                        indian.setChecked(false);
//                        latino.setChecked(false);
//                        nativeAmerican.setChecked(false);
//                        result = (String) white.getText();
//                        break;
//                }
//            }
//        });
//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //sliderFragmentsPresenter.updateEthnicityUserInfo(result);
//            }
//        });

        return view;
    }

}
