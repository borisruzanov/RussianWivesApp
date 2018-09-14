package com.borisruzanov.russianwives.ui;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.borisruzanov.russianwives.Adapters.UserDescriptionListAdapter;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.UserProfileItemsList;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.UserCallback;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

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

//                new FirebaseRepository().getUserDataTierTwo("FriendsLogic", "hereUserUid",
//                        "Requests", stringList -> new FirebaseRepository().getNeededUsers(stringList, fsUserList -> {
//                            for (String uid : stringList) Log.d(Contract.TAG, "Uid " + uid);
//                            for (FsUser user : fsUserList) {
//                                Log.d(Contract.TAG, "FsUser name " + user.getName());
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

        new FirebaseRepository().getFriendsData(Consts.USERS_DB, getIntent().getStringExtra("uid"), new UserCallback() {
            @Override
            public void getUser(FsUser fsUser) {
                name.setText(fsUser.getName());
                age.setText(fsUser.getAge());
                country.setText(fsUser.getCountry());
                if (!fsUser.getImage().equals("default")) {
                    Glide
                            .with(FriendActivity.this)
                            .load(fsUser.getImage())
                            .into(imageView);
                }
            }
        });

        new FirebaseRepository().getAllInfoCurrentUser(new UserCallback() {
            @Override
            public void getUser(FsUser fsUser) {
                userDescriptionList.addAll(UserProfileItemsList.initData(fsUser));
                setList(userDescriptionList);
            }
        });

        new FirebaseRepository().setUserVisited(friendUid);
        //new FirebaseRepository().addUserToActivity(userUid, friendUid);

    }


//    private void onUiReady(String nameLink, String ageLink, String countryLink, String imageLink) {

//        }
//        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                // remove previous listener
//                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
//                // prep the scene
//                prepareScene();
//                // run the animation
//                runEnterAnimation();
//                return true;
//            }
//        });
//    }
//
//    private void prepareScene() {
//        // capture the end values in the destionation view
//        mEndValues = captureValues(mDestinationView);
//
//        // calculate the scale and positoin deltas
//        float scaleX = scaleDelta(mStartValues, mEndValues);
//        float scaleY = scaleDelta(mStartValues, mEndValues);
//        int deltaX = translationDelta(mStartValues, mEndValues);
//        int deltaY = translationDelta(mStartValues, mEndValues);
//
//        // scale and reposition the image
//        mDestinationView.setScaleX(scaleX);
//        mDestinationView.setScaleY(scaleY);
//        mDestinationView.setTranslationX(deltaX);
//        mDestinationView.setTranslationY(deltaY);
//    }
//    private void runEnterAnimation() {
//        // We can now make it visible
//        imageView.setVisibility(View.VISIBLE);
//        // finally, run the animation
//        imageView.animate()
//                .setDuration(DEFAULT_DURATION)
//                .setInterpolator(DEFAULT_INTERPOLATOR)
//                .scaleX(1f)
//                .scaleY(1f)
//                .translationX(0)
//                .translationY(0)
//                .start();
//    }
//    private void runExitAnimation() {
//        imageView.animate()
//                .setDuration(DEFAULT_DURATION)
//                .setInterpolator(DEFAULT_INTERPOLATOR)
//                .scaleX(scaleX)
//                .scaleY(scaleY)
//                .translationX(deltaX)
//                .translationY(deltaY)
//                .withEndAction(new Runnable() {
//                    @Override
//                    public void run() {
//                        finish();
//                        overridePendingTransition(0, 0);
//                    }
//                }).start();
//    }

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
        OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
            }
        };
        return onItemClickCallback;
    }


}
