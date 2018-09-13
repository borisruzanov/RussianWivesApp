package com.borisruzanov.russianwives.ui.slider;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.interactor.SliderInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.SliderFragmentsPresenter;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;

import java.util.HashMap;
import java.util.Map;

public class SliderHobbyFragment extends MvpAppCompatFragment{

    SliderFragmentsPresenter sliderFragmentsPresenter;
    EditText answer;
    Button btnSave;
    String result;

    public SliderHobbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_hobby, container, false);
        sliderFragmentsPresenter = new SliderFragmentsPresenter(new SliderInteractor(new FirebaseRepository()), new SliderImageFragment());


        btnSave = (Button) view.findViewById(R.id.fragment_slider_hobby_btn_save);
        answer = (EditText) view.findViewById(R.id.fragment_slider_hobby_et_answer);

        new FirebaseRepository().getFieldFromCurrentUser("hobby", new ValueCallback() {
            @Override
            public void getValue(String value) {
                if (value != null) {
                    answer.setText(value);
                    answer.setSelection(answer.getText().length());
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = answer.getText().toString();

                if(!result.isEmpty()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("hobby", answer.getText().toString());
                    new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                        @Override
                        public void onUpdate() {
                            getActivity().onBackPressed();
                            Toast.makeText(getActivity(), "Height was updated", Toast.LENGTH_LONG).show();

                        }
                    });
                }
                //sliderFragmentsPresenter.updateHobbyUserInfo(result);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
    }
}
