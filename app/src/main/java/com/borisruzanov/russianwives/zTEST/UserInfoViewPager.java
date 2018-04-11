package com.borisruzanov.russianwives.zTEST;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.borisruzanov.russianwives.R;

public class UserInfoViewPager extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private String[] images = {

            "https://cdn.acidcow.com/pics/20120502/miss_reef_chile_04.jpg",
            "https://i.pinimg.com/236x/56/97/67/56976729965841670aca463047864cc7--reef-girls-beach-girls.jpg",
            "https://i.pinimg.com/originals/5f/18/e0/5f18e087cb52d0f8185cca5e4d30a65b.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_view_pager);

        viewPager = (ViewPager) findViewById(R.id.view_pager_user_info);
        viewPagerAdapter = new ViewPagerAdapter(UserInfoViewPager.this, images);
        viewPager.setAdapter(viewPagerAdapter);

    }
}
