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
public class SliderDrinkStatusFragment extends Fragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public SliderDrinkStatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_drink_status, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_drink_status_radiogroup);
        btnSave = (Button) view.findViewById(R.id.fragment_slider_drinkstatus_btn_save);

        new FirebaseRepository().getFieldFromCurrentUser("drink_status", value -> {
            if (value != null && value.equals("Never")){
                radioGroup.check(R.id.fragment_slider_drink_status_rbtn_never);
            } else if (value != null && value.equals("Only with friends")){
                radioGroup.check(R.id.fragment_slider_drink_status_rbtn_friends);
            } else if (value != null && value.equals("Moderately")){
                radioGroup.check(R.id.fragment_slider_drink_status_rbtn_moderaely);
            }else if (value != null && value.equals("Regularly")){
                radioGroup.check(R.id.fragment_slider_drink_status_rbtn_regularly);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) view.findViewById(selectedId);
                if(radioButton.getText() != null){
                    Map<String, Object> map = new HashMap<>();
                    map.put("drink_status", radioButton.getText());
                    new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                        @Override
                        public void onUpdate() {
                            getActivity().onBackPressed();
                            Toast.makeText(getActivity(), R.string.drink_updated, Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
        return view;    }

}
