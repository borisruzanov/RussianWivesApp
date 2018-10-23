package com.borisruzanov.russianwives.mvp.ui.slider;

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
import com.borisruzanov.russianwives.mvp.ui.slider.adapter.UserInfoPagerAdapter;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.utils.Consts;

import java.util.ArrayList;
import java.util.List;

public class SliderActivity extends MvpAppCompatActivity {
    //TODO Implement MVP
    //TODO make final logic with appearing buttons

    ViewPager viewPager;
    UserInfoPagerAdapter adapter;
    Toolbar toolbar;
    Button buttonNext;

    final List<Fragment> fragmentList = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        toolbar = findViewById(R.id.toolbar_slider);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);


        viewPager = findViewById(R.id.view_pager_add_info);
        buttonNext = findViewById(R.id.slider_button);

        if (getIntent().getStringArrayListExtra(Consts.DEFAULT_LIST) != null) {
            addFragments();
        }
        else if (getIntent().getExtras().getString("field_id") != null) {
            switch (getIntent().getExtras().getString("field_id")) {
                case "Image":
                    fragmentList.add(SliderImageFragment.newInstance());
                    break;
                case "Name":
                    fragmentList.add(SliderNameFragment.newInstance());
                    break;
                case "Age":
                    fragmentList.add(SliderAgeFragment.newInstance());
                    break;
                case "Country":
                    fragmentList.add(SliderCountriesFragment.newInstance());
                    break;
                case "Gender":
                    fragmentList.add(SliderGenderFragment.newInstance());
                    break;
                case "Relationship Status":
                    fragmentList.add(SliderRelationshipsStatusFragment.newInstance());
                    break;
                case "Body Type":
                    fragmentList.add(SliderBodytypeFragment.newInstance());
                    break;
                case "Ethnicity":
                    fragmentList.add(SliderEthnicityFragment.newInstance());
                    break;
                case "Faith":
                    fragmentList.add(new SliderFaithFragment());
                    break;
                case "Smoke Status":
                    fragmentList.add(SliderSmokingStatusFragment.newInstance());
                    break;
                case "How Often Do You Drink Alcohol":
                    fragmentList.add(SliderDrinkStatusFragment.newInstance());
                    break;
                case "Do You Have Kids":
                    fragmentList.add(SliderHaveKidsFragment.newInstance());
                    break;
                case "Do You Want Kids":
                    fragmentList.add(SliderWillingKidsFragment.newInstance());
                    break;
                case "Hobby":
                    fragmentList.add(SliderHobbyFragment.newInstance());
                    break;
            }
        }

        adapter = new UserInfoPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        buttonNext.setOnClickListener(view -> {
            Log.d(Contract.SLIDER, "Inside extras " + getIntent().getExtras().getString("field_id"));
            slideToNext(fragmentList, viewPager.getCurrentItem());
        });

        viewPager.addOnPageChangeListener(onPageChangeListener);

        if (getIntent().getExtras().getString("intent") != null && getIntent().getExtras().getString("intent").equals("list")){
            buttonNext.setVisibility(View.INVISIBLE);
        }else {
            Log.d("tag", "Inside extras " + getIntent().getExtras().getString("field_id"));
        }
        Log.d(Contract.SLIDER, "Inside extras " + getIntent().getExtras().getString("field_id"));

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

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            if(fragmentList.size() == position + 1) buttonNext.setText(R.string.finish);
            else buttonNext.setText(R.string.next_text);
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    private void addFragments() {
        for (String key: getIntent().getStringArrayListExtra(Consts.DEFAULT_LIST)) {
            switch (key) {
                case Consts.BODY_TYPE:
                    fragmentList.add(new SliderBodytypeFragment());
                    break;
                case Consts.DRINK_STATUS:
                    fragmentList.add(new SliderDrinkStatusFragment());
                    break;
                case Consts.ETHNICITY:
                    fragmentList.add(new SliderEthnicityFragment());
                    break;
                case Consts.GENDER:
                    fragmentList.add(new SliderGenderFragment());
                    break;
                case Consts.HOBBY:
                    fragmentList.add(new SliderHobbyFragment());
                    break;
                case Consts.FAITH:
                    fragmentList.add(new SliderFaithFragment());
                    break;
                case Consts.HOW_TALL:
                    // maybe soon implemented
                    break;
                case Consts.IMAGE:
                    fragmentList.add(new SliderImageFragment());
                    break;
                case Consts.LANGUAGES:
                    fragmentList.add(new SliderLanguagesFragment());
                    break;
                case Consts.SMOKING_STATUS:
                    fragmentList.add(new SliderSmokingStatusFragment());
                    break;
                case Consts.RELATIONSHIP_STATUS:
                    fragmentList.add(new SliderRelationshipsStatusFragment());
                    break;
                case Consts.WANT_CHILDREN_OR_NOT:
                    fragmentList.add(new SliderWillingKidsFragment());
                    break;
                case Consts.NUMBER_OF_KIDS:
                    fragmentList.add(new SliderHaveKidsFragment());
                    break;
            }
        }
    }

    public void slideToNext(List<Fragment> fragmentList, int position){
        if(!fragmentList.isEmpty() && position != fragmentList.size()){
            viewPager.setCurrentItem(position+1);
        }
        if(position + 1 == fragmentList.size()) {
            //close the survey
            finish();
        }
    }

}
