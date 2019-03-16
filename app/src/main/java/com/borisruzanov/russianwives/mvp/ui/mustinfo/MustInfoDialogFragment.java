package com.borisruzanov.russianwives.mvp.ui.mustinfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MustInfoDialogFragment extends MvpAppCompatDialogFragment implements MustInfoDialogView {

    @Inject
    @InjectPresenter
    MustInfoDialogPresenter presenter;

    @ProvidePresenter
    MustInfoDialogPresenter provideFilterDialogPresenter() {
        return presenter;
    }

    @BindView(R.id.spinner_age_mi)
    Spinner ageSpinner;

    @BindView(R.id.spinner_country_mi)
    Spinner countrySpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.must_info_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button_ok_mi)
    public void onButtonClicked() {
        //TODO replace with separate values of age, country and image
        String age = ageSpinner.getSelectedItem().toString();
        String country = countrySpinner.getSelectedItem().toString();
        presenter.saveValues(age, country);
        dismiss();
    }

}
