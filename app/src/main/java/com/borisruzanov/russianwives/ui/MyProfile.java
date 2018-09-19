package com.borisruzanov.russianwives.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.borisruzanov.russianwives.Adapters.UserDescriptionListAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.UserProfileItemsList;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.ui.fragments.SearchFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyProfile extends AppCompatActivity {

    //MVP
    //TODO Implement MVP

    //UI
    @BindView(R.id.recycler_list_userDescription)
    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton fab;
    TextView name;
    TextView age;
    TextView country;
    ImageView imageView;


    //Utility
    List<UserDescriptionModel> userDescriptionList = new ArrayList<>();
    UserDescriptionListAdapter userDescriptionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //UI
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);

        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.getBackground().setAlpha(125);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_userDescription);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        userDescriptionListAdapter = new UserDescriptionListAdapter(setOnItemClickCallback());
        recyclerView.setAdapter(userDescriptionListAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab_id);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsProfile = new Intent(MyProfile.this, ProfileSettingsActivity.class);
                startActivity(settingsProfile);
            }
        });

        imageView = (ImageView) findViewById(R.id.my_profile_image);
        name = (TextView) findViewById(R.id.my_profile_tv_name);
        age = (TextView) findViewById(R.id.my_profile_tv_age);
        country = (TextView) findViewById(R.id.my_profile_tv_country);

        new FirebaseRepository().getFieldFromCurrentUser("image", (String value) -> {
            if(!value.equals("default")){
                Glide
                        .with(MyProfile.this)
                        .load(value)
                        .into(imageView);
            }
        });

        new FirebaseRepository().getFieldFromCurrentUser("name", value -> name.setText(value));

        new FirebaseRepository().getFieldFromCurrentUser("country", value -> country.setText(value));

        new FirebaseRepository().getFieldFromCurrentUser("age", value -> age.setText(value));

        new FirebaseRepository().getAllCurrentUserInfo(user -> {
                Log.d(Contract.TAG, "UID is -------+-+- " + user.getUid());
                userDescriptionList.addAll(UserProfileItemsList.initData(user));
                setList(userDescriptionList);
        });

        // получаем экземпляр FragmentTransaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        // добавляем фрагмент
        SearchFragment recipeListFragment = new SearchFragment();
        fragmentTransaction.add(R.id.my_profile_list_container, recipeListFragment);
        fragmentTransaction.commit();

    }

    public void setList(List<UserDescriptionModel> userDescriptionList) {
        //Setting data to the adapter
        userDescriptionListAdapter.setData(userDescriptionList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private OnItemClickListener.OnItemClickCallback setOnItemClickCallback() {
        OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
            }
        };
        return onItemClickCallback;
    }
}
