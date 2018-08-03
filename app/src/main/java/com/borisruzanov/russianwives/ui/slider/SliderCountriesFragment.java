package com.borisruzanov.russianwives.ui.slider;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.borisruzanov.russianwives.Adapters.CountriesAdapter;
import com.borisruzanov.russianwives.CountriesList;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.UpdateCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderCountriesFragment extends Fragment {

    private ListView countriesList;

    public SliderCountriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider_countries, container, false);

        countriesList = (ListView) view.findViewById(R.id.country_list);
        CountriesAdapter countriesAdapter = new CountriesAdapter(getActivity(), CountriesList.initData());
        countriesList.setAdapter(countriesAdapter);
        countriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String country = CountriesList.initData().get(position).getCountryName();
                Map<String, Object> map = new HashMap<>();
                map.put("country", country);
                new FirebaseRepository().updateFieldFromCurrentUser(map, new UpdateCallback() {
                    @Override
                    public void onUpdate() {

                    }
                });
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
