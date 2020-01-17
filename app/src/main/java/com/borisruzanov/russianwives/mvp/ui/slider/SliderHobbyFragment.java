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
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.mvp.model.interactor.slider.SliderInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Rating.ADD_HOBBY_RATING;

public class SliderHobbyFragment extends MvpAppCompatFragment{

    SliderFragmentsPresenter sliderFragmentsPresenter;
    EditText answer;
    Button btnSave;
    String result;

    public SliderHobbyFragment() {
        // Required empty public constructor
    }

    public static SliderHobbyFragment newInstance() {
        SliderHobbyFragment fragment = new SliderHobbyFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider_hobby, container, false);
        //todo: inject
        sliderFragmentsPresenter = new SliderFragmentsPresenter(new SliderInteractor(new SliderRepository()));


        btnSave = view.findViewById(R.id.fragment_slider_hobby_btn_save);
        answer = view.findViewById(R.id.fragment_slider_hobby_et_answer);

        new SliderRepository().getFieldFromCurrentUser(Consts.HOBBY, value -> {
            if (value != null && !value.equals(Consts.DEFAULT)) {
                answer.setText(value);
                answer.setSelection(answer.getText().length());
            }
        });

        btnSave.setOnClickListener(view1 -> {
            result = answer.getText().toString();

            if(!result.isEmpty()){
                Map<String, Object> map = new HashMap<>();
                map.put(Consts.HOBBY, answer.getText().toString());
                new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                    if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                        if (getActivity() != null) getActivity().onBackPressed();
                    }
                    Toast.makeText(getActivity(), getString(R.string.hobby_updated), Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new StringEvent("next_page"));
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
    }
}
