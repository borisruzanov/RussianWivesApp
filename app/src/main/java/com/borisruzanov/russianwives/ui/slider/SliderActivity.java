package com.borisruzanov.russianwives.ui.slider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.ui.slider.adapter.UserInfoPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderActivity extends MvpAppCompatActivity {

    ViewPager viewPager;
    UserInfoPagerAdapter adapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        toolbar = (Toolbar) findViewById(R.id.toolbar_slider);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.view_pager_add_info);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SliderImageFragment());
        fragmentList.add(new SliderBodytypeFragment());
//        fragmentList.add(new SliderHeightFragment());
//        fragmentList.add(new SliderLanguagesFragment());
        fragmentList.add(new SliderEthnicityFragment());

        fragmentList.add(new SliderHobbyFragment());
        fragmentList.add(new SliderFaithFragment());
        fragmentList.add(new SliderDrinkStatusFragment());
        fragmentList.add(new SliderSmokingStatusFragment());
        fragmentList.add(new SliderRalationshipsStatusFragment());
        fragmentList.add(new SliderNumberOfKidsFragment());

        fragmentList.add(new SliderWillingKidsFragment());
        adapter = new UserInfoPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

    }
}
