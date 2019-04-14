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
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_WILL_OF_KIDS_RATING;

public class SliderWillingKidsFragment extends Fragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    String result;

    public SliderWillingKidsFragment() {
        // Required empty public constructor
    }

    public static SliderWillingKidsFragment newInstance() {
        SliderWillingKidsFragment fragment = new SliderWillingKidsFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_willing_kids, container, false);
        radioGroup = view.findViewById(R.id.fragment_slider_willingkids_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_willingkids_btn_save);

        new SliderRepository().getFieldFromCurrentUser(Consts.WANT_CHILDREN_OR_NOT, value -> {
            result = value;
            if (value != null && value.equals(getString(R.string.definitely))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_definitely);
            } else if (value != null && value.equals(getString(R.string.someday))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_someday);
            } else if (value != null && value.equals(getString(R.string.not_sure))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_not_sure);
            } else if (value != null && value.equals(getString(R.string.probably_not))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_probably);
            } else if (value != null && value.equals(getString(R.string.no_but_its_ok_if_partner_has_kids))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_no_but_ok);
            } else if (value != null && value.equals(getString(R.string.no))) {
                radioGroup.check(R.id.fragment_slider_willingkids_rbtn_no);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId > 0) {
                radioButton = view.findViewById(selectedId);
                if (radioButton.getText() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(Consts.WANT_CHILDREN_OR_NOT, radioButton.getText());
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (result.equals(Consts.DEFAULT))
                            new RatingRepository().addRating(ADD_WILL_OF_KIDS_RATING);
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.willing_kids_updated), Toast.LENGTH_LONG).show();
                    });
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
