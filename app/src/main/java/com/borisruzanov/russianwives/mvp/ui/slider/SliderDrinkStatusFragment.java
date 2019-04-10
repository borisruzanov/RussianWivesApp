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

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_DRINK_STATUS_RATING;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderDrinkStatusFragment extends Fragment {

    Button btnSave;
    RadioGroup radioGroup;
    RadioButton radioButton;

    String result;

    public SliderDrinkStatusFragment() {
        // Required empty public constructor
    }

    public static SliderDrinkStatusFragment newInstance() {
        SliderDrinkStatusFragment fragment = new SliderDrinkStatusFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_drink_status, container, false);
        radioGroup = view.findViewById(R.id.fragment_slider_drink_status_radiogroup);
        btnSave = view.findViewById(R.id.fragment_slider_drinkstatus_btn_save);

        new SliderRepository().getFieldFromCurrentUser(Consts.DRINK_STATUS, value -> {
            result = value;
            if (value != null && value.equals(getString(R.string.never))) {
                radioGroup.check(R.id.fragment_slider_drink_status_rbtn_never);
            } else if (value != null && value.equals(getString(R.string.only_with_friends))) {
                radioGroup.check(R.id.fragment_slider_drink_status_rbtn_friends);
            } else if (value != null && value.equals(getString(R.string.moderately))) {
                radioGroup.check(R.id.fragment_slider_drink_status_rbtn_moderaely);
            } else if (value != null && value.equals(getString(R.string.regularly))) {
                radioGroup.check(R.id.fragment_slider_drink_status_rbtn_regularly);
            }
        });

        btnSave.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId > 0) {
                radioButton = view.findViewById(selectedId);
                if (radioButton.getText() != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(Consts.DRINK_STATUS, radioButton.getText());
                    new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                        if (result.equals(Consts.DEFAULT))
                            new RatingRepository().addRating(ADD_DRINK_STATUS_RATING);
                        if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                            if (getActivity() != null) getActivity().onBackPressed();
                        }
                        Toast.makeText(getActivity(), getString(R.string.drink_updated), Toast.LENGTH_LONG).show();
                    });
                } else {
                    Toast.makeText(getActivity(), getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

}
