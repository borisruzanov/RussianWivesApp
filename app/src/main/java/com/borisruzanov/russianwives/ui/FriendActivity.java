package com.borisruzanov.russianwives.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.borisruzanov.russianwives.Adapters.UserDescriptionListAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.UserProfileItemsList;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.User;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.StringsCallback;
import com.borisruzanov.russianwives.utils.UpdateCallback;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.borisruzanov.russianwives.utils.UsersListCallback;
import com.borisruzanov.russianwives.utils.ValueCallback;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class FriendActivity extends MvpAppCompatActivity {

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.friend_activity_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Make sure user can make only 1 like
            }
        });

        userUid = new FirebaseRepository().getUid();
        friendUid = getIntent().getStringExtra("uid");

        btnAddFriend = (Button) findViewById(R.id.friend_activity_btn_add_friend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("status", "Declined");
                // map.put("image", "https://firebasestorage.googleapis.com/v0/b/russianwives.appspot.com/o/profile_images%2FqPMqWY8js0NVkaphZL5mMX7LWR73%2Fprofile_photo?alt=media&token=a519ffe5-7228-41c5-ad00-c03527f222ac");
                new FirebaseRepository().updateUserDataTierTwo("FriendsLogic", userUid, "Requests", friendUid, map, new UpdateCallback() {
                    @Override
                    public void onUpdate() {
                    }
                });
//                new FirebaseRepository().getUserDataTierTwo("FriendsLogic", "hereUserUid",
//                        "Requests", stringList -> new FirebaseRepository().getNeededUsers(stringList, userList -> {
//                            for (String uid : stringList) Log.d(Contract.TAG, "Uid " + uid);
//                            for (User user : userList) {
//                                Log.d(Contract.TAG, "User name " + user.getName());
//                            }
//                        }));

            }

        });
        btnStartChat = (Button) findViewById(R.id.friend_activity_btn_start_chat);
        imageView = (ImageView) findViewById(R.id.friend_activity_image);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_friendDescription);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        userDescriptionListAdapter = new UserDescriptionListAdapter(setOnItemClickCallback());
        recyclerView.setAdapter(userDescriptionListAdapter);


        name = (TextView) findViewById(R.id.friend_activity_tv_name);
        age = (TextView) findViewById(R.id.friend_activity_tv_age);
        country = (TextView) findViewById(R.id.friend_activity_tv_country);

        new FirebaseRepository().getFriendsData(Consts.COLLECTION_USERS, getIntent().getStringExtra("uid"), new UserCallback() {
            @Override
            public void getUser(User user) {
                name.setText(user.getName());
                age.setText(user.getAge());
                country.setText(user.getCountry());
                if (!user.getImage().equals("default")) {
                    Glide
                            .with(FriendActivity.this)
                            .load(user.getImage())
                            .into(imageView);
                }
            }
        });

        new FirebaseRepository().getAllInfoCurrentUser(new UserCallback() {
            @Override
            public void getUser(User user) {
                userDescriptionList.addAll(UserProfileItemsList.initData(user));
                setList(userDescriptionList);
            }
        });
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
        OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
            }
        };
        return onItemClickCallback;
    }
}
