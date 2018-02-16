package com.borisruzanov.russianwives;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.borisruzanov.russianwives.Adapters.SectionPagerAdapter;
import com.borisruzanov.russianwives.Base.BaseActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    public static final int RC_SIGN_IN = 1;

    //Main
    Toolbar toolbar;

    //Tabs
    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionPagerAdapter;
    private TabLayout mTabLayout;


    //Firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthstateListener;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tab_pager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        mFirebaseAuth = FirebaseAuth.getInstance();


        //TODO AuthstateListener - вынести отдельно
        //Authstate Listener
        mAuthstateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.v(TAG, "user is not null");
                    //Create new user
                    String uid = user.getUid();
                   final String userName = user.getDisplayName();
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.child("status").getValue(String.class).equals("Your status is here..."))
                            {
                                //the image is still the default one
                                HashMap<String, String> userMap = new HashMap<>();
                                userMap.put("name", userName);
                                userMap.put("image", "default");
                                userMap.put("thumb_image", "default");
                                mDatabaseReference.setValue(userMap);
                                userMap.put("status", "Your status is here...");
                                mDatabaseReference.setValue(userMap);
                            }
                            else{
                                //the image is no longer the default
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


//                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            String status = mDatabaseReference.child("Status").toString();
//                            if (status == null) {
//                                HashMap<String, String> userMap = new HashMap<>();
//                                userMap.put("status", "Your status is here...");
//                                mDatabaseReference.setValue(userMap);
//                            }
//
//                        }
//
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                    onSignedInInitialized(user.getDisplayName());
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
//                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.menu_chat:
                Intent chatActivityIntent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(chatActivityIntent);
                return true;
            case R.id.menu_settings:
                Log.v("===>", "in menu settings button");
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
            case R.id.menu_main_all_users_btn:
                Log.v("===>", "in menu all users button");

                Intent usersIntent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(usersIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthstateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthstateListener);
        }
    }

    //
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthstateListener);
    }


}
