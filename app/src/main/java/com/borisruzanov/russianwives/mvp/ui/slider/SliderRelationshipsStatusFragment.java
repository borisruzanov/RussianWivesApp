package com.borisruzanov.russianwives.mvp.ui.slider;


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
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Map;

public class SliderRelationshipsStatusFragment extends Fragment {
    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    public SliderRelationshipsStatusFragment() {
        // Required empty public constructor
    }

    public static SliderRelationshipsStatusFragment newInstance() {
        SliderRelationshipsStatusFragment fragment = new SliderRelationshipsStatusFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider_relationships_status, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_slider_relationships_radiogroup);
        btnSave = (Button) view.findViewById(R.id.fragment_slider_relationships_btn_save);

        new SliderRepository().getFieldFromCurrentUser("relationship_status", value -> {
            if (value != null && value.equals("Never married")){
                radioGroup.check(R.id.fragment_slider_rbtn_never_married);
            } else if (value != null && value.equals("Currently separated")){
                radioGroup.check(R.id.fragment_slider_rbtn_separated);
            } else if (value != null && value.equals("Divorced")){
                radioGroup.check(R.id.fragment_slider_rbtn_divorced);
            }else if (value != null && value.equals("Widow / Widower")){
                radioGroup.check(R.id.fragment_slider_rbtn_widow);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) view.findViewById(selectedId);
            if(radioButton.getText() != null){
                Map<String, Object> map = new HashMap<>();
                map.put("relationship_status", radioButton.getText());
                new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                    if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                        getActivity().onBackPressed();
                    }
                    Toast.makeText(getActivity(), R.string.relationship_was_updated, Toast.LENGTH_LONG).show();

                });
            }
        });
        return view;
    }

}
