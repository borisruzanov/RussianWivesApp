package com.borisruzanov.russianwives.zTEST;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.borisruzanov.russianwives.R;

public class TestPageActivity extends FragmentActivity {
    static final String TAG = "myLogs";
    static final int PAGE_COUNT = 10;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fragment_user_info_input);

    }
}
