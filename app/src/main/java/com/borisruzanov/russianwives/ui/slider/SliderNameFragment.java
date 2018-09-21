package com.borisruzanov.russianwives.ui.slider;

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
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;

import java.util.HashMap;
import java.util.Map;

public class SliderNameFragment extends MvpAppCompatFragment {

    EditText answer;
    Button btnSave;

    public SliderNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_name, container, false);

        btnSave = (Button) view.findViewById(R.id.fragment_slider_name_btn_save);
        answer = (EditText) view.findViewById(R.id.fragment_slider_name_et_answer);

        new FirebaseRepository().getFieldFromCurrentUser("name", value -> {
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
                new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                    @Override
                    public void onUpdate() {
                        getActivity().onBackPressed();
                        Toast.makeText(getActivity(), "Name was updated", Toast.LENGTH_LONG).show();

                    }
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