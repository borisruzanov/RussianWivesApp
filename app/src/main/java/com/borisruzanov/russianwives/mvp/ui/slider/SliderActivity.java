package com.borisruzanov.russianwives.mvp.ui.slider;

import android.content.Intent;
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
import com.borisruzanov.russianwives.eventbus.StringEvent;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.borisruzanov.russianwives.mvp.ui.main.MainScreenActivity;
import com.borisruzanov.russianwives.mvp.ui.slider.adapter.UserInfoPagerAdapter;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.utils.Consts;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.borisruzanov.russianwives.mvp.model.repository.rating.Achievements.FULL_PROFILE_ACH;

public class SliderActivity extends MvpAppCompatActivity {
    //TODO Implement MVP
    //TODO make final logic with appearing buttons

    ViewPager viewPager;
    UserInfoPagerAdapter adapter;
    Toolbar toolbar;
    Button buttonNext;
    FirebaseAnalytics firebaseAnalytics;

    final List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        toolbar = findViewById(R.id.toolbar_slider);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My title");

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        viewPager = findViewById(R.id.view_pager_add_info);
        buttonNext = findViewById(R.id.slider_button);

        Log.d("SliderDebug", "In onCreate");

        if (getIntent().getStringArrayListExtra(Consts.DEFAULT_LIST) != null) {
            addFragments();
        } else if (getIntent().getExtras().getString("field_id") != null) {
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
                    fragmentList.add(SliderFaithFragment.newInstance());
                    break;
                case "Smoke Status":
                    fragmentList.add(SliderSmokingStatusFragment.newInstance());
                    break;
                case "How Often Do You Drink Alcohol":
                    fragmentList.add(SliderDrinkStatusFragment.newInstance());
                    break;
                case "Number Of Kids":
                    fragmentList.add(SliderHaveKidsFragment.newInstance());
                    break;
                case "Do You Want Kids":
                    fragmentList.add(SliderWillingKidsFragment.newInstance());
                    break;
                case "Looking for":
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

        if (getIntent().getExtras().getString("intent") != null && getIntent().getExtras().getString("intent").equals("list")) {
            buttonNext.setVisibility(View.GONE);
        } else {
            //Show finish button
            if (fragmentList.size() == 1) {
                buttonNext.setVisibility(View.GONE);
                buttonNext.setText(R.string.finish);
            }
            Log.d("tag", "Inside extras " + getIntent().getExtras().getString("field_id"));
        }
        Log.d(Contract.SLIDER, "Inside extras " + getIntent().getExtras().getString("field_id"));

    }

    @Subscribe
    public void nextSlide(StringEvent event) {
        if (fragmentList.size() > 1) {
            slideToNext(fragmentList, viewPager.getCurrentItem());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("tag", "Back clicked");
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (fragmentList.size() == position + 1) {
                buttonNext.setVisibility(View.VISIBLE);
                buttonNext.setText(R.string.finish);
            } else {
                buttonNext.setVisibility(View.GONE);
                buttonNext.setText(R.string.next_text);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void addFragments() {
        for (String key : getIntent().getStringArrayListExtra(Consts.DEFAULT_LIST)) {
            switch (key) {
                case Consts.BODY_TYPE:
                    fragmentList.add(new SliderBodytypeFragment());
                    break;
                case Consts.AGE:
                    fragmentList.add(new SliderAgeFragment());
                    break;
                case Consts.DRINK_STATUS:
                    fragmentList.add(new SliderDrinkStatusFragment());
                    break;
                case Consts.COUNTRY:
                    fragmentList.add(new SliderCountriesFragment());
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

    public void slideToNext(List<Fragment> fragmentList, int position) {
        if (!fragmentList.isEmpty() && position != fragmentList.size()) {
            viewPager.setCurrentItem(position + 1);
        }
        if (position + 1 == fragmentList.size()) {
            //close the survey
            onBackPressed();
//            addFullProfileAchieve();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SliderActivity.this, MainScreenActivity.class);
        startActivity(intent);
        addFPAchieveIfNeeded();
    }

    private void addFPAchieveIfNeeded() {
        new RatingRepository().isAchievementExist(FULL_PROFILE_ACH, flag -> {
            if (!flag) {
                Log.d("SliderDebug", "Add achievement");
                addFullProfileAchieve();
            } else Log.d("SliderDebug", "Already exist");
        });
    }

    private void addFullProfileAchieve() {
        UserRepository userRepository = new UserRepository(new Prefs(getApplicationContext()));
        userRepository.getDefaultList(stringList -> {
            Log.d("TimerDebug", "String list emptiness is " + stringList.isEmpty());
            if (stringList.isEmpty()) {
                userRepository.clearDialogOpenDate();
                userRepository.setFullProfile();
                new RatingRepository().addAchievement(FULL_PROFILE_ACH);
                userRepository.addRating(8);
                firebaseAnalytics.logEvent("achieve_full_profile", null);
            }
        });
    }

}
