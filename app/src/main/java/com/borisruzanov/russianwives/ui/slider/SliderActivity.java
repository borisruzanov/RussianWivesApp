package com.borisruzanov.russianwives.ui.slider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.ui.slider.adapter.UserInfoPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderActivity extends MvpAppCompatActivity {

    ViewPager viewPager;
    UserInfoPagerAdapter adapter;
    Toolbar toolbar;
    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        toolbar = (Toolbar) findViewById(R.id.toolbar_slider);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.view_pager_add_info);
        buttonNext = findViewById(R.id.slider_button);

        final List<Fragment> fragmentList = new ArrayList<>();
        if(getIntent().getExtras().getString("field_id").equals("Image")){
            fragmentList.add(new SliderImageFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Name")){
            fragmentList.add(new SliderNameFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Age")){
            fragmentList.add(new SliderAgeFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Country")){
            fragmentList.add(new SliderCountriesFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Gender")){
//            fragmentList.add(new SliderGenderFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Relationship Status")){
            fragmentList.add(new SliderRalationshipsStatusFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Body Type")){
            fragmentList.add(new SliderBodytypeFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Ethnicity")){
            fragmentList.add(new SliderEthnicityFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Faith")){
            fragmentList.add(new SliderFaithFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Smoke Status")){
            fragmentList.add(new SliderSmokingStatusFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("How Often Do You Drink Alcohol")){
            fragmentList.add(new SliderDrinkStatusFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Number Of Kids You Have")){
            fragmentList.add(new SliderNumberOfKidsFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Do You Want Kids")){
            fragmentList.add(new SliderWillingKidsFragment());
        }else if (getIntent().getExtras().getString("field_id").equals("Hobby")){
            fragmentList.add(new SliderHobbyFragment());
        }




        adapter = new UserInfoPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideToNext(fragmentList, viewPager.getCurrentItem());
            }
        });
        if (getIntent().getExtras().containsKey("field_id")){
            buttonNext.setVisibility(View.GONE);
        }else {
            Log.d("tag", "Inside ELSE " + getIntent().getExtras().getString("field_id"));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d("tag", "Back clicked");
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void slideToNext(List<Fragment> fragmentList, int position){
        if(!fragmentList.isEmpty() && position != fragmentList.size()){
            viewPager.setCurrentItem(position+1);
        } else {
            //close the survey
        }
    }

}
