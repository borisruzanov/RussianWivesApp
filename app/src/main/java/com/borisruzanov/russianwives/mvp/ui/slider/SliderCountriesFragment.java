package com.borisruzanov.russianwives.mvp.ui.slider;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.borisruzanov.russianwives.mvp.ui.slider.adapter.CountriesAdapter;
import com.borisruzanov.russianwives.CountriesList;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.slider.SliderRepository;
import com.borisruzanov.russianwives.utils.Consts;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderCountriesFragment extends Fragment {

    public SliderCountriesFragment() {
        // Required empty public constructor
    }

    public static SliderCountriesFragment newInstance() {
        SliderCountriesFragment fragment = new SliderCountriesFragment();
        Bundle args = new Bundle();
        args.putString(Consts.NEED_BACK, Consts.BACK);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider_countries, container, false);

        ListView countriesList = view.findViewById(R.id.country_list);
        CountriesAdapter countriesAdapter = new CountriesAdapter(getActivity(), CountriesList.initData());
        countriesList.setAdapter(countriesAdapter);
        countriesList.setOnItemClickListener((parent, view1, position, id) -> {
            String country = CountriesList.initData().get(position).getCountryName();
            Map<String, Object> map = new HashMap<>();
            map.put(Consts.COUNTRY, country);
            new SliderRepository().updateFieldFromCurrentUser(map, () -> {
                if (getArguments() != null && getArguments().getString(Consts.NEED_BACK) != null) {
                    if (getActivity() != null) getActivity().onBackPressed();
                }
                Toast.makeText(getActivity(), R.string.country_updated, Toast.LENGTH_LONG).show();
            });
        });

        return view;
    }

}
