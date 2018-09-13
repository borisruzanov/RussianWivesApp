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

public class SliderWillingKidsFragment extends Fragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public SliderWillingKidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_willing_kids, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_willingkids_radiogroup);
        btnSave = (Button) view.findViewById(R.id.fragment_slider_willingkids_btn_save);

        new FirebaseRepository().getFieldFromCurrentUser("want_children_or_not", new ValueCallback() {
            @Override
            public void getValue(String value) {
                if (value != null && value.equals("Definitely")){
                    radioGroup.check(R.id.fragment_slider_willingkids_rbtn_definitely);
                } else if (value != null && value.equals("Someday")){
                    radioGroup.check(R.id.fragment_slider_willingkids_rbtn_someday);
                } else if (value != null && value.equals("Not sure")){
                    radioGroup.check(R.id.fragment_slider_willingkids_rbtn_not_sure);
                }else if (value != null && value.equals("Probably not")){
                    radioGroup.check(R.id.fragment_slider_willingkids_rbtn_probably);
                }else if (value != null && value.equals("No")){
                    radioGroup.check(R.id.fragment_slider_willingkids_rbtn_no);
                }else if (value != null && value.equals("No, but its ok if partner has kids")){
                    radioGroup.check(R.id.fragment_slider_willingkids_rbtn_no_but_ok);
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
                    map.put("want_children_or_not", radioButton.getText());
                    new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                        @Override
                        public void onUpdate() {
                            getActivity().onBackPressed();
                            Toast.makeText(getActivity(), "Willing kids status was updated", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
        return view;    }

}
