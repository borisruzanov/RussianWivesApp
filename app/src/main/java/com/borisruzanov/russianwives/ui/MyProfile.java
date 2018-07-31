package com.borisruzanov.russianwives.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.ValueCallback;

public class MyProfile extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    TextView name;
    TextView age;
    TextView country;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("tag", "in MyProfile");
        setContentView(R.layout.activity_my_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        collapsingToolbarLayout.setTitleEnabled(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton)findViewById(R.id.fab_id);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsProfile = new Intent(MyProfile.this, ProfileSettingsActivity.class);
                startActivity(settingsProfile);
            }
        });

        name = (TextView) findViewById(R.id.my_profile_tv_name);
        age = (TextView) findViewById(R.id.my_profile_tv_age);
        country = (TextView) findViewById(R.id.my_profile_tv_country);

        new FirebaseRepository().getValueFromUsers("name", new ValueCallback() {
            @Override
            public void getValue(String value) {
                name.setText(value);
            }
        });
        new FirebaseRepository().getValueFromUsers("name", new ValueCallback() {
            @Override
            public void getValue(String value) {
                name.setText(value);
            }
        });

    }
}
