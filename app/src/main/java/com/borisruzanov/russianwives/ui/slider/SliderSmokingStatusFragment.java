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
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;

import java.util.HashMap;
import java.util.Map;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_smoking_status, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_smokine_status_radiogroup);
        btnSave = (Button) view.findViewById(R.id.fragment_slider_smokestatus_btn_save);

        new FirebaseRepository().getFieldFromCurrentUser("smoking_status", new ValueCallback() {
            @Override
            public void getValue(String value) {
                if (value != null && value.equals("No way")){
                    radioGroup.check(R.id.fragment_slider_smoking_status_rbtn_no_way);
                } else if (value != null && value.equals("Occasionally")){
                    radioGroup.check(R.id.fragment_slider_smoking_status_rbtn_occasioally);
                } else if (value != null && value.equals("Daily")){
                    radioGroup.check(R.id.fragment_slider_smoking_status_rbtn_daily);
                }else if (value != null && value.equals("Cigar aficionado")){
                    radioGroup.check(R.id.fragment_slider_smokine_status_rbtn_cigar_aficionado);
                }else if (value != null && value.equals("Yes, but trying to quit")){
                    radioGroup.check(R.id.fragment_slider_smokine_status_rbtn_quit);
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
                    map.put("smoking_status", radioButton.getText());
                    new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                        @Override
                        public void onUpdate() {
                            getActivity().onBackPressed();
                            Toast.makeText(getActivity(), "Smoking status was updated", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
        return view;    }

}
