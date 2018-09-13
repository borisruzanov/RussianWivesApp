package com.borisruzanov.russianwives.ui.slider;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class SliderBodytypeFragment extends MvpAppCompatFragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;


    public SliderBodytypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_bodytype, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_bodytype_radiogroup);
        btnSave = (Button) view.findViewById(R.id.fragment_slider_bodytype_btn_save);

        new FirebaseRepository().getFieldFromCurrentUser("body_type", new ValueCallback() {
            @Override
            public void getValue(String value) {
                if (value != null && value.equals("Slender")){
                    radioGroup.check(R.id.fragment_slider_bodytype_rbtn_slender);
                } else if (value != null && value.equals("About average")){
                    radioGroup.check(R.id.fragment_slider_bodytype_rbtn_average);
                } else if (value != null && value.equals("Athletic")){
                    radioGroup.check(R.id.fragment_slider_bodytype_rbtn_athletic);
                }else if (value != null && value.equals("Heavyset")){
                    radioGroup.check(R.id.fragment_slider_bodytype_rbtn_heavyset);
                }else if (value != null && value.equals("A few extra pounds")){
                    radioGroup.check(R.id.fragment_slider_bodytype_rbtn_fewextra);
                }else if (value != null && value.equals("Stocky")){
                    radioGroup.check(R.id.fragment_slider_bodytype_rbtn_stocky);
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
                    map.put("body_type", radioButton.getText());
                    new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                        @Override
                        public void onUpdate() {
                            getActivity().onBackPressed();
                            Toast.makeText(getActivity(), "Bodytype was updated", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
        return view;
    }

}
