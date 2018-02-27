package com.borisruzanov.russianwives.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.borisruzanov.russianwives.ChatUdacityActivity;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.SettingsActivity;
import com.borisruzanov.russianwives.UsersActivity;
import com.borisruzanov.russianwives.base.BaseActivity;
import com.borisruzanov.russianwives.mvp.model.MainInteractor;
import com.borisruzanov.russianwives.mvp.presenter.IMainPresenter;
import com.borisruzanov.russianwives.mvp.presenter.MainPresenter;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BaseActivity {

    //Firebase
    FirebaseAuth mFirebaseAuth;

    IMainPresenter iMainPresenter;
//
//    //Main
    Toolbar toolbar;
//
//    //Tabs
//    private ViewPager mViewPager;
//    private SectionsPagerAdapter mSectionsPagerAdapter;
//    private TabLayout mTabLayout;
//
//
//    //Firebase
//    private FirebaseAuth mFirebaseAuth;
//    private FirebaseAuth.AuthStateListener mAuthstateListener;
//    private DatabaseReference mDatabaseReference;
//    private DatabaseReference mUserDatabase;
//    private DatabaseReference mUserRef;
//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mFirebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser currentUserInstance = firebaseAuth.getCurrentUser();
//                if(currentUserInstance != null){
//                    Log.v("RW", "MainActivity - onAuthStateChanged = NOT NULL");
//
//                }else {
//                    Log.v("RW", "MainActivity - onAuthStateChanged = NULL");
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setAvailableProviders(Arrays.asList(
//                                            new AuthUI.IdpConfig.EmailBuilder().build(),
////                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
//                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
//                                    .build(),
//                            RC_SIGN_IN);
//
//                }
//            }
//        });

//
        iMainPresenter = new MainPresenter(new MainInteractor());
        iMainPresenter.forwardInfoForLoginAuth(MainActivity.this);


//        //Toolbar


//
//        //Tabs
//        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
//        mTabLayout.setupWithViewPager(mViewPager);
//        mAuth = FirebaseAuth.getInstance();
//
//
//        //Firebase
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//        if (mAuth.getCurrentUser() != null) {
//            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
//        }
//
//
////        TODO AuthstateListener - вынести отдельно
////        Authstate Listener
//        mAuthstateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    Log.v("Tag", "user is not null");
//                    //Create new user
//                    String uid = user.getUid();
//                    final String userName = user.getDisplayName();
//                    mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                //the image is still the default one
//                                HashMap<String, String> userMap = new HashMap<>();
//                                userMap.put("name", userName);
//                                userMap.put("image", "default");
//                                userMap.put("thumb_image", "default");
//                                userMap.put("status", "Your status is here...");
//                                mDatabaseReference.setValue(userMap);
//
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//                    //Saving Token Id
//                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
//                    Log.v("=====>>>", "UID" + uid + deviceToken);
//                    mUserDatabase.child(uid).child("device_token").setValue(deviceToken);
//
//                    //Changing online status of the user
////                    mUserRef.child("online").setValue(true);
//                } else {
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setAvailableProviders(Arrays.asList(
//                                            new AuthUI.IdpConfig.EmailBuilder().build(),
////                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
//                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
//                                    .build(),
//                            RC_SIGN_IN);
//                }
//            }
//        };
//
//



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
                Intent chatActivityIntent = new Intent(MainActivity.this, ChatUdacityActivity.class);
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

//    //
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mAuthstateListener != null) {
//            mFirebaseAuth.removeAuthStateListener(mAuthstateListener);
//        }
//    }
//
//    //
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mFirebaseAuth.addAuthStateListener(mAuthstateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        FirebaseUser currentUserInstance = mAuth.getCurrentUser();
//        if (currentUserInstance != null) {
////            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
//
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUserInstance = mAuth.getCurrentUser();
//        if (currentUserInstance != null) {
////            mUserRef.child("online").setValue("true");
//        }
//    }
}