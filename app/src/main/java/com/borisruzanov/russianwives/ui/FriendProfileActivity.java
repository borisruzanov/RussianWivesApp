package com.borisruzanov.russianwives.ui;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.borisruzanov.russianwives.Adapters.UserDescriptionListAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.UserProfileItemsList;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FriendProfileActivity extends MvpAppCompatActivity {

    //TODO Refactor to ActivitiesActivity functionality
    Toolbar toolbar;
    FloatingActionButton fab;

    RecyclerView recyclerView;

    List<UserDescriptionModel> userDescriptionList = new ArrayList<>();
    UserDescriptionListAdapter userDescriptionListAdapter;

    String userUid;
    String friendUid;

    TextView name;
    TextView age;
    TextView country;
    ImageView imageView;


    Button btnAddFriend;
    Button btnStartChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

        fab = findViewById(R.id.friend_activity_fab);
        fab.setOnClickListener(v -> {
            //TODO Make sure user can make only 1 like
        });

        userUid = new FirebaseRepository().getUid();
        friendUid = getIntent().getStringExtra("uid");

        btnAddFriend = findViewById(R.id.friend_activity_btn_add_friend);
        btnAddFriend.setOnClickListener(v -> {

//                new FirebaseRepository().getUserDataTierTwo("FriendsLogic", "hereUserUid",
//                        "Requests", stringList -> new FirebaseRepository().getNeededUsers(stringList, fsUserList -> {
//                            for (String uid : stringList) Log.d(Contract.TAG, "Uid " + uid);
//                            for (FsUser user : fsUserList) {
//                                Log.d(Contract.TAG, "FsUser name " + user.getName());
//                            }
//                        }));

        });
        btnStartChat = findViewById(R.id.friend_activity_btn_start_chat);
        imageView = findViewById(R.id.friend_activity_image);

        recyclerView = findViewById(R.id.recycler_list_friendDescription);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        userDescriptionListAdapter = new UserDescriptionListAdapter(setOnItemClickCallback());
        recyclerView.setAdapter(userDescriptionListAdapter);

        name = findViewById(R.id.friend_activity_tv_name);
        age = findViewById(R.id.friend_activity_tv_age);
        country = findViewById(R.id.friend_activity_tv_country);

        new FirebaseRepository().getFriendsData(Consts.USERS_DB, getIntent().getStringExtra("uid"), fsUser -> {
            name.setText(fsUser.getName());
            age.setText(fsUser.getAge());
            country.setText(fsUser.getCountry());
            if (!fsUser.getImage().equals("default")) {
                Glide.with(FriendProfileActivity.this)
                        .load(fsUser.getImage())
                        .into(imageView);
            }
        });

        new FirebaseRepository().getAllCurrentUserInfo(fsUser -> {
            userDescriptionList.addAll(UserProfileItemsList.initData(fsUser));
            setList(userDescriptionList);
        });

        new FirebaseRepository().setUserVisited(friendUid);
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

    public void setList(List<UserDescriptionModel> userDescriptionList) {
        if (userDescriptionList.isEmpty()) {
            Log.d(Contract.TAG, "The list is empty");
        } else {
            Log.d(Contract.TAG, "The list is NOT empty");
        }
        //Setting data to the adapter
        userDescriptionListAdapter.setData(userDescriptionList);
    }

    private OnItemClickListener.OnItemClickCallback setOnItemClickCallback() {
        OnItemClickListener.OnItemClickCallback onItemClickCallback = (view, position) -> {

        };
        return onItemClickCallback;
    }


}
