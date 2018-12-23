package com.borisruzanov.russianwives.mvp.ui.neccessaryinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.borisruzanov.russianwives.App;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.utils.Consts;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NecessaryInfoDialogFragment extends MvpAppCompatDialogFragment {

    public static String TAG = "Neccessary info";

    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.spinner_age_ni)
    Spinner ageSpinner;
    @BindView(R.id.spinner_gender_ni)
    Spinner genderSpinner;
    @BindView(R.id.confirm_button_ni)
    Button confirmButton;
    @BindView(R.id.cancel_button_ni)
    Button cancelButton;

    NecessaryInfoListener listener;

    private Tracker mTracker;

    public static NecessaryInfoDialogFragment newInstance(String gender, String age) {
        NecessaryInfoDialogFragment fragment = new NecessaryInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString(Consts.GENDER, gender);
        args.putString(Consts.AGE, age);
        fragment.setArguments(args);
        return fragment;
    }

    public interface NecessaryInfoListener {
        void setInfo(String gender, String age);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        if (context instanceof NecessaryInfoListener) {
            listener = (NecessaryInfoListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_necessary_info, container, false);
        ButterKnife.bind(this, view);
        mTracker = new App().getTracker();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments().getString(Consts.GENDER) != null && !getArguments().getString(Consts.GENDER).equals(Consts.DEFAULT)) {
            genderSpinner.setSelection(getIndexOfElement(R.array.genders, getArguments().getString(Consts.GENDER)));
            String s = getString(R.string.must_info_canceled);

        }
        if (getArguments().getString(Consts.AGE) != null && !getArguments().getString(Consts.AGE).equals(Consts.DEFAULT)) {
            ageSpinner.setSelection(getIndexOfElement(R.array.age_types, getArguments().getString(Consts.AGE)));

        }

    }

    @OnClick(R.id.confirm_button_ni)
    public void onConfirmClicked() {
        String gender = genderSpinner.getSelectedItem().toString();
        String age = ageSpinner.getSelectedItem().toString();
        listener.setInfo(gender, age);
        mFirebaseAnalytics.setUserProperty("age_param", age);
        new App().sTracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.user_information_category))
                .setAction(age)
                .build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.user_information_category))
                .setAction(gender)
                .build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.conversion_two))
                .setAction(getString(R.string.must_have_info_done))
                .build());
        dismiss();

    }

    @OnClick(R.id.cancel_button_ni)
    public void onCancelClicked() {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.conversion_break))
                .setAction(getString(R.string.must_info_canceled))
                .build());
        dismiss();
    }

    private int getIndexOfElement(@ArrayRes int resId, String value) {
        List<String> list = Arrays.asList((getResources().getStringArray(resId)));
        return list.indexOf(value);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
