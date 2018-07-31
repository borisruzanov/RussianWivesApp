package com.borisruzanov.russianwives.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.Adapters.MainPagerAdapter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.ui.fragments.ChatsFragment;
import com.borisruzanov.russianwives.ui.fragments.FriendsFragment;
import com.borisruzanov.russianwives.ui.fragments.SearchFragment;
import com.borisruzanov.russianwives.mvp.model.interactor.MainInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.MainPresenter;
import com.borisruzanov.russianwives.mvp.view.MainView;
import com.borisruzanov.russianwives.ui.slider.SliderActivity;
import com.borisruzanov.russianwives.utils.FirebaseRequestManager;
import com.borisruzanov.russianwives.zTEST.UserInfoViewPager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import static com.borisruzanov.russianwives.models.Contract.RC_SIGN_IN;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter mainPresenter;

    Toolbar toolbar;

    @ProvidePresenter
    MainPresenter provideMainPresenter() {
        return new MainPresenter(new MainInteractor(new FirebaseRepository()));
    }

    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    //
//    //Tabs
//    private ViewPager mViewPager;
//    private MainPagerAdapter mSectionsPagerAdapter;
//    private TabLayout mTabLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    MainPagerAdapter mainPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        mainPagerAdapter.addFragment(new FriendsFragment(), "Friends");
        mainPagerAdapter.addFragment(new SearchFragment(), "Search");

        viewPager.setAdapter(mainPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);


        mainPresenter.checkForUserExist();
        //mainPresenter.userNeedToAddInfo();

//
        //iMainPresenter = new MainPresenter(new MainInteractor());
//        iMainPresenter.forwardInfoForLoginAuth(MainActivity.this);


//        //Toolbar


//
//        //Tabs
//        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
//        mSectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
//        mTabLayout.setupWithViewPager(mViewPager);
//        mAuth = FirebaseAuth.getInstance();
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
//            case R.id.menu_user:
//                Intent userActivityIntent = new Intent(MainActivity.this, AllUsersActivity.class);
//                startActivity(userActivityIntent);
//                return true;
            case R.id.menu_my_profile:
                Log.d("tag", "onOptionsItemSelected - menu_my_profile");
                Intent settingsIntent = new Intent(MainActivity.this, MyProfile.class);
                startActivity(settingsIntent);
//            case R.id.menu_main_all_users_btn:
//                Log.d("tag", "onOptionsItemSelected - menu_main_all_users_btn");
//                Intent userInfoViewPagerIntent = new Intent(MainActivity.this, UserInfoViewPager.class);
//                startActivity(userInfoViewPagerIntent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void callAuthWindow() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            //for checking errors
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                //add user to the firestore
                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                 //if(user != null) {
                   /* FirebaseFirestore.getInstance().collection("Users").document(user.getUid())
                            .set(FirebaseRequestManager.createNewUser(user.getDisplayName(),
                                    FirebaseInstanceId.getInstance().getToken()));*/
                   //new FirebaseRepository().saveUser();
                    mainPresenter.saveUser();
               // } else Log.d("Auth", "User id is still null");
            }
            else {
                //Sign in failed
                if(response == null){}
                //something went wrong
            }
        }
    }

    @Override
    public void checkingForUserInformation() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("You Didn't Fill The Form. Do You Want To Fill It Now?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent sliderIntent = new Intent(MainActivity.this, SliderActivity.class);
                startActivity(sliderIntent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog = builder.create();
        alertDialog.show();
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
