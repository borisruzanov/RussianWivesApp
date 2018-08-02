package com.borisruzanov.russianwives.ui.slider;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.interactor.SliderInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.SliderFragmentsPresenter;

public class SliderBodytypeFragment extends Fragment {

    SliderFragmentsPresenter sliderFragmentsPresenter;
    Button btnSave;
    Button btnClose;
    Button btnNext;
    RadioGroup radioGroup;
    RadioButton slender;
    RadioButton aboutAverage;
    RadioButton athletic;
    RadioButton heavyset;
    RadioButton fewExtra;
    RadioButton stocky;
    String result;


    public SliderBodytypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_bodytype, container, false);
        sliderFragmentsPresenter = new SliderFragmentsPresenter(new SliderInteractor(new FirebaseRepository()), new SliderImageFragment());


        btnSave = (Button) view.findViewById(R.id.fragment_slider_bodytype_btn_save);
        btnClose = (Button) view.findViewById(R.id.fragment_slider_bodytype_btn_close);
        btnNext = (Button) view.findViewById(R.id.fragment_slider_bodytype_btn_next);


        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_bodytype_radiogroup);
        slender = (RadioButton) view.findViewById(R.id.fragment_slider_bodytype_linear_slender);
        aboutAverage = (RadioButton) view.findViewById(R.id.fragment_slider_bodytype_linear_average);
        athletic = (RadioButton) view.findViewById(R.id.fragment_slider_bodytype_linear_athletic);
        heavyset = (RadioButton) view.findViewById(R.id.fragment_slider_bodytype_linear_heavyset);
        fewExtra = (RadioButton) view.findViewById(R.id.fragment_slider_bodytype_linear_fewextra);
        stocky = (RadioButton) view.findViewById(R.id.fragment_slider_bodytype_linear_stocky);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.fragment_slider_bodytype_linear_slender:
                        aboutAverage.setChecked(false);
                        athletic.setChecked(false);
                        heavyset.setChecked(false);
                        fewExtra.setChecked(false);
                        stocky.setChecked(false);
                        result = (String) slender.getText();
                        break;
                    case R.id.fragment_slider_bodytype_linear_average:
                        slender.setChecked(false);
                        athletic.setChecked(false);
                        heavyset.setChecked(false);
                        fewExtra.setChecked(false);
                        stocky.setChecked(false);
                        result = (String) aboutAverage.getText();
                        break;
                    case R.id.fragment_slider_bodytype_linear_athletic:
                        aboutAverage.setChecked(false);
                        slender.setChecked(false);
                        heavyset.setChecked(false);
                        fewExtra.setChecked(false);
                        stocky.setChecked(false);
                        result = (String) athletic.getText();
                        break;
                    case R.id.fragment_slider_bodytype_linear_heavyset:
                        aboutAverage.setChecked(false);
                        athletic.setChecked(false);
                        slender.setChecked(false);
                        fewExtra.setChecked(false);
                        stocky.setChecked(false);
                        result = (String) heavyset.getText();
                        break;
                    case R.id.fragment_slider_bodytype_linear_fewextra:
                        aboutAverage.setChecked(false);
                        athletic.setChecked(false);
                        heavyset.setChecked(false);
                        slender.setChecked(false);
                        stocky.setChecked(false);
                        result = (String) fewExtra.getText();
                        break;
                    case R.id.fragment_slider_bodytype_linear_stocky:
                        aboutAverage.setChecked(false);
                        athletic.setChecked(false);
                        heavyset.setChecked(false);
                        fewExtra.setChecked(false);
                        slender.setChecked(false);
                        result = (String) stocky.getText();
                        break;
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sliderFragmentsPresenter.updateBodyTypeUserInfo(result);
            }
        });

        return view;
    }

}
