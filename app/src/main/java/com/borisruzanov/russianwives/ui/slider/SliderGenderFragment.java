package com.borisruzanov.russianwives.ui.slider;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderGenderFragment extends MvpAppCompatFragment {
    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;


    public SliderGenderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_gender, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_gender_radiogroup);
        btnSave = (Button) view.findViewById(R.id.fragment_slider_gender_btn_save);

        new FirebaseRepository().getFieldFromCurrentUser("gender", new ValueCallback() {
            @Override
            public void setValue(String value) {
                if (value != null && value.equals("Female")){
                    radioGroup.check(R.id.fragment_slider_gender_radiobutton_female);
                } else if (value != null && value.equals("Male")){
                    radioGroup.check(R.id.fragment_slider_gender_radiobutton_male);
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
                    map.put("gender", radioButton.getText());
                    new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                        @Override
                        public void onUpdate() {
                            getActivity().onBackPressed();
                            Toast.makeText(getActivity(), "Gender information was updated", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
        return view;
    }

}
