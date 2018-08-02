package com.borisruzanov.russianwives.ui.slider;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;

import java.util.HashMap;
import java.util.Map;

public class SliderNameFragment extends MvpAppCompatFragment {

//    SliderFragmentsPresenter sliderFragmentsPresenter;
    EditText answer;
    Button btnSave;
//    String result;

    public SliderNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_name, container, false);
//        sliderFragmentsPresenter = new SliderFragmentsPresenter(new SliderInteractor(new FirebaseRepository()), new SliderImageFragment());


        btnSave = (Button) view.findViewById(R.id.fragment_slider_name_btn_save);
        answer = (EditText) view.findViewById(R.id.fragment_slider_name_et_answer);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answer.getText().toString().trim().isEmpty()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", answer.getText().toString());

                    new FirebaseRepository().updateDataOfCurrentUser(map).subscribe();
                }
                // else
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}