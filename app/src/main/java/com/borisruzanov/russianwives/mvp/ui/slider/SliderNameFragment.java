package com.borisruzanov.russianwives.mvp.ui.slider;

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
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Map;

public class SliderNameFragment extends MvpAppCompatFragment {

    EditText answer;
    Button btnSave;

    public SliderNameFragment() {
        // Required empty public constructor
    }

    public static SliderNameFragment newInstance() {
        SliderNameFragment fragment = new SliderNameFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_name, container, false);

        btnSave = view.findViewById(R.id.fragment_slider_name_btn_save);
        answer = view.findViewById(R.id.fragment_slider_name_et_answer);

        new SliderRepository().getFieldFromCurrentUser("name", value -> {
            if (value != null) {
                answer.setText(value);
                answer.setSelection(answer.getText().length());
            }
        });

        answer.setOnClickListener(v -> {
            if (answer.getText() != null) {
                answer.setText("");
            }
        });

        btnSave.setOnClickListener(view1 -> {
            if (!answer.getText().toString().trim().isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", answer.getText().toString());
                new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                    if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                        getActivity().onBackPressed();
                    }
                    Toast.makeText(getActivity(), R.string.name_updated, Toast.LENGTH_LONG).show();

                });
            }
            // else
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}