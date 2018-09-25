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
public class SliderHaveKidsFragment extends Fragment {
    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public SliderHaveKidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_number_of_kids, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_number_of_kids_radiogroup);
        btnSave = (Button) view.findViewById(R.id.fragment_slider_numberofkids_btn_save);

        new FirebaseRepository().getFieldFromCurrentUser("number_of_kids", value -> {
            if (value != null && value.equals("No")){
                radioGroup.check(R.id.fragment_slider_number_of_kids_rbtn_no);
            } else if (value != null && value.equals("Yes, they sometimes live at home")){
                radioGroup.check(R.id.fragment_slider_number_of_kids_rbtn_sometimes_at_home);
            } else if (value != null && value.equals("Yes, they live away from home")){
                radioGroup.check(R.id.fragment_slider_number_of_kids_rbtn_away);
            }else if (value != null && value.equals("Yes, they live at home")){
                radioGroup.check(R.id.fragment_slider_number_of_kids_rbtn_at_home);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) view.findViewById(selectedId);
            if(radioButton.getText() != null){
                Map<String, Object> map = new HashMap<>();
                map.put("number_of_kids", radioButton.getText());
                new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                    @Override
                    public void onUpdate() {
                        getActivity().onBackPressed();
                        Toast.makeText(getActivity(), R.string.number_of_kids_updated, Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
        return view;    }

}
