package com.borisruzanov.russianwives.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.Toast;

import com.borisruzanov.russianwives.Adapters.UserDescriptionListAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.UserProfileItemsList;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;

public class MyProfile extends AppCompatActivity {

    @BindView(R.id.recycler_list_userDescription)
    RecyclerView recyclerView;


    List<UserDescriptionModel> userDescriptionList = new ArrayList<>();
    UserDescriptionListAdapter userDescriptionListAdapter;

    Toolbar toolbar;
    FloatingActionButton fab;
    TextView name;
    TextView age;
    TextView country;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        Log.d(Contract.TAG, "MyProfile - onCreate");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_userDescription);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        userDescriptionListAdapter = new UserDescriptionListAdapter(setOnItemClickCallback());
        recyclerView.setAdapter(userDescriptionListAdapter);
        imageView = (ImageView) findViewById(R.id.my_profile_image);





        //1 option
//        new FirebaseRepository().getDataFromUsers("name").doOnNext(s -> s.endsWith("")).subscribe();
//or

        // 2 option
//        new FirebaseRepository().getDataFromUsers("name")
//                .subscribe(s -> Toast.makeText(getApplicationContext(), "Your name is " + s, Toast.LENGTH_SHORT).show()).dispose();
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put(Consts.HOBBY, "psychology");
//        new FirebaseRepository().pushDataToUsers(hashMap)
//                .subscribe(() -> Toast.makeText(getApplicationContext(), "Successfully changed", Toast.LENGTH_SHORT).show()).dispose();


//        userDescriptionList.addAll(userDescriptionList);
//        userDescriptionListAdapter.setData(userDescriptionList);


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        collapsingToolbarLayout.setTitleEnabled(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.fab_id);
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
//        bodyType = (TextView) findViewById(R.id.my_profile_tv_your_body_type);

        new FirebaseRepository().getFieldFromCurrentUser("image", new ValueCallback() {
            @Override
            public void getValue(String value) {
                if(!value.equals("default")){
                    Glide
                            .with(MyProfile.this)
                            .load(value)
                            .into(imageView);
                }

            }
        });

        new FirebaseRepository().getFieldFromCurrentUser("name", new ValueCallback() {
            @Override
            public void getValue(String value) {
                name.setText(value);
            }
        });

        new FirebaseRepository().getFieldFromCurrentUser("country", new ValueCallback() {
            @Override
            public void getValue(String value) {
                country.setText(value);
            }
        });

        new FirebaseRepository().getFieldFromCurrentUser("age", new ValueCallback() {
            @Override
            public void getValue(String value) {
                age.setText(value);
            }
        });


        new FirebaseRepository().getAllInfoCurrentUser(new UserCallback() {
            @Override
            public void getUser(User user) {
                Log.d(Contract.TAG, "It's working and username is " + user.getName());
                Toast.makeText(getApplicationContext(), "Work, work. work!", Toast.LENGTH_SHORT).show();
                    userDescriptionList.addAll(UserProfileItemsList.initData(user));
                    setList(userDescriptionList);
            }
        });



        Log.d(Contract.TAG, "The list is empty");

    }

    public void setList(List<UserDescriptionModel> userDescriptionList) {
        if (userDescriptionList.isEmpty()) {
            Log.d(Contract.TAG, "The list is empty");
        }else {
            Log.d(Contract.TAG, "The list is NOT empty");
        }
        //Setting data to the adapter
        userDescriptionListAdapter.setData(userDescriptionList);
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

    private OnItemClickListener.OnItemClickCallback setOnItemClickCallback() {
        OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
            }
        };
        return onItemClickCallback;
    }

}
