package com.borisruzanov.russianwives.mvp.ui.slider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Country;

import java.util.List;

public class CountriesAdapter extends BaseAdapter {


    private List<Country> countries;
    private LayoutInflater layoutInflater;

    public CountriesAdapter(Context context, List<Country> countries) {
        this.countries = countries;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_country, viewGroup, false);
        }

        Country country = getCountry(position);
        TextView textView = view.findViewById(R.id.item_country_title);
        textView.setText(country.getCountryName());
        return view;
    }

    private Country getCountry(int position) {
        return (Country) getItem(position);
    }

}
