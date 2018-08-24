package com.borisruzanov.russianwives.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.interactor.FilterInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FilterRepository;
import com.borisruzanov.russianwives.mvp.presenter.FilterDialogPresenter;
import com.borisruzanov.russianwives.mvp.view.FilterView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterDialogFragment extends MvpAppCompatDialogFragment implements FilterView {

    public static String TAG = "Dialog Fragment";

    public interface FilterListener {
        void onUpdate();
    }

    View view;

    @InjectPresenter
    FilterDialogPresenter presenter;

    @ProvidePresenter
    public FilterDialogPresenter provideFilterDialogPresenter(){
        return new FilterDialogPresenter(new FilterInteractor(new FilterRepository(new Prefs(getActivity()))));
    }

    @BindViews({R.id.spinner_gender, R.id.spinner_age, R.id.spinner_country, R.id.spinner_relationship_statuses,
            R.id.spinner_body_types, R.id.spinner_ethnicities, R.id.spinner_faith_types, R.id.spinner_smoke_statuses,
            R.id.spinner_drink_statuses, R.id.spinner_have_kids_statuses, R.id.spinner_want_kids_statuses})
    List<Spinner> spinners;

    private FilterListener filterListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener) {
            filterListener = (FilterListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_filters, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.getSavedValues();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.button_search)
    public void onSearchClicked(){
        presenter.saveValues(spinners);
        filterListener.onUpdate();

        dismiss();
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClicked(){
        dismiss();
    }

    @Override
    public void getSavedValues(List<String> stringList, List<Integer> resIdList){
        for (int i = 0; i < stringList.size(); i++) {
            if (presenter.isNotDefault(stringList.get(i))) {
                spinners.get(i).setSelection(getIndexOfElement(resIdList.get(i), stringList.get(i)));
            }
        }
    }

    private int getIndexOfElement(@ArrayRes int resId, String value){
        List<String> list = Arrays.asList((getResources().getStringArray(resId)));
        return list.indexOf(value);
    }

}
