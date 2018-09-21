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

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.Adapters.UserDescriptionListAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.UserProfileItemsList;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.interactor.MyProfileInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.MyProfilePresenter;
import com.borisruzanov.russianwives.mvp.view.MyProfileView;
import com.borisruzanov.russianwives.ui.fragments.SearchFragment;
import com.borisruzanov.russianwives.utils.Consts;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyProfileActivity extends MvpAppCompatActivity implements MyProfileView {

    //UI
    @BindView(R.id.recycler_list_userDescription)
    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton fab;
    TextView nameText, ageText, countryText, numberOfLikes, numberOfVisits;
    ImageView imageView;

    UserDescriptionListAdapter userDescriptionListAdapter;

    @InjectPresenter
    MyProfilePresenter presenter;

    @ProvidePresenter
    public MyProfilePresenter provideMyProfilePresenter(){
        return new MyProfilePresenter(new MyProfileInteractor(new FirebaseRepository()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //UI
        toolbar = findViewById(R.id.toolbar);
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
        fab.setOnClickListener(view -> {
            Intent settingsProfile = new Intent(MyProfileActivity.this, ProfileSettingsActivity.class);
            startActivity(settingsProfile);
        });

        imageView = (ImageView) findViewById(R.id.my_profile_image);
        nameText = (TextView) findViewById(R.id.my_profile_tv_name);
        ageText = (TextView) findViewById(R.id.my_profile_tv_age);
        countryText = findViewById(R.id.my_profile_tv_country);
        numberOfLikes = findViewById(R.id.number_of_likes);
        numberOfVisits = findViewById(R.id.number_of_visits);

        getSupportFragmentManager().beginTransaction().add(R.id.my_profile_list_container, new SearchFragment()).commit();

    }

    @Override
    public void setUserData(String name, String age, String country, String image) {
        nameText.setText(name);
        ageText.setText(age);
        countryText.setText(country);
        if (!image.equals(Consts.DEFAULT)) {
            Glide.with(this)
                    .load(image)
                    .into(imageView);
        }
    }

    @Override
    public void setActionsCount(long visits, long likes){
        numberOfVisits.setText(String.valueOf(visits));
        numberOfLikes.setText(String.valueOf(likes));
    }

    @Override
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
        return (view, position) -> {
        };
    }
}
