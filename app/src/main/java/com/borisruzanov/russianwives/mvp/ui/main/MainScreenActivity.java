package com.borisruzanov.russianwives.mvp.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.FsUser;
import com.borisruzanov.russianwives.mvp.model.data.SystemRepository;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.interactor.chats.ChatsInteractor;
import com.borisruzanov.russianwives.mvp.model.interactor.coins.CoinsInteractor;
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor;
import com.borisruzanov.russianwives.mvp.model.interactor.myprofile.MyProfileInteractor;
import com.borisruzanov.russianwives.mvp.model.interactor.search.SearchInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.chats.ChatsRepository;
import com.borisruzanov.russianwives.mvp.model.repository.coins.CoinsRepository;
import com.borisruzanov.russianwives.mvp.model.repository.filter.FilterRepository;
import com.borisruzanov.russianwives.mvp.model.repository.friend.FriendRepository;
import com.borisruzanov.russianwives.mvp.model.repository.hots.HotUsersRepository;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingRepository;
import com.borisruzanov.russianwives.mvp.model.repository.search.SearchRepository;
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.borisruzanov.russianwives.mvp.ui.actions.ActionsActivity;
import com.borisruzanov.russianwives.mvp.ui.chats.ChatsActivity;
import com.borisruzanov.russianwives.mvp.ui.chats.ChatsPresenter;
import com.borisruzanov.russianwives.mvp.ui.confirm.ConfirmDialogFragment;
import com.borisruzanov.russianwives.mvp.ui.filter.FilterDialogFragment;
import com.borisruzanov.russianwives.mvp.ui.gender.GenderDialogFragment;
import com.borisruzanov.russianwives.mvp.ui.main.adapter.CustomViewPager;
import com.borisruzanov.russianwives.mvp.ui.main.adapter.MainPagerAdapter;
import com.borisruzanov.russianwives.mvp.ui.mustinfo.MustInfoDialogFragment;
import com.borisruzanov.russianwives.mvp.ui.myprofile.MyProfileActivity;
import com.borisruzanov.russianwives.mvp.ui.myprofile.MyProfilePresenter;
import com.borisruzanov.russianwives.mvp.ui.onlineUsers.OnlineUsersFragment;
import com.borisruzanov.russianwives.mvp.ui.search.SearchFragment;
import com.borisruzanov.russianwives.mvp.ui.search.SearchPresenter;
import com.borisruzanov.russianwives.mvp.ui.slider.SliderActivity;
import com.borisruzanov.russianwives.utils.Consts;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static com.borisruzanov.russianwives.models.Contract.RC_SIGN_IN;

public class MainScreenActivity extends AppCompatActivity implements FilterDialogFragment.FilterListener, MainView, ConfirmDialogFragment.ConfirmListener {

    private static final String TAG_CLASS_NAME = "MainScreenActivity";
    private FirebaseAnalytics firebaseAnalytics;


    private MainScreenPresenter mPresenter;
    private ChatsPresenter mChatsPresenter;
    private SearchPresenter mSearchPresenter;
    private MyProfilePresenter mMyProfilePresenter;

    private boolean mIsUserExist;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private CustomViewPager mViewPager;
    private MainPagerAdapter mViewPagerAdapter;

    private NavigationView mNavigationView;
    private MenuItem mDrawerItemSafety;
    private ImageView mCloseDrawerButton;
    private TextView mViewProfileButton;
    private LinearLayout mPurchaseSectionButton;
    private RelativeLayout mFilterButton;
    private TextView mUnregisteredTitle;
    private ImageView mRegisterButton;
    private ImageView mChatsButton;
    private ImageView mActionsButton;
    private View mHeaderView;

    private DialogFragment mDialogFragment;
    private SearchFragment mSearchFragment;
    private FsUser mFsUser = new FsUser();
    private Prefs mPrefs;
    private int counter;

    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        mPresenter = new MainScreenPresenter(new MainInteractor(new UserRepository(new Prefs(this)), new HotUsersRepository(new Prefs(this)), new SystemRepository(new Prefs(this))), this);
        mChatsPresenter = new ChatsPresenter(new ChatsInteractor(new ChatsRepository()));
        mSearchPresenter = new SearchPresenter(new SearchInteractor(new SearchRepository(), new FilterRepository(new Prefs(this)), new FriendRepository(), new RatingRepository(), new UserRepository(new Prefs(this)), new HotUsersRepository(new Prefs(this))), new CoinsInteractor(new CoinsRepository()));
        mMyProfilePresenter = new MyProfilePresenter(new MyProfileInteractor(new UserRepository(new Prefs(this))));
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mPrefs = new Prefs(this);

        mIsUserExist = mPresenter.isUserExist();
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mTabLayout = findViewById(R.id.main_tabs);
        mViewPager = findViewById(R.id.main_view_pager);
        mViewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

        mFilterButton = findViewById(R.id.toolbar_filter_btn);
        mRegisterButton = findViewById(R.id.toolbar_login);
        mChatsButton = findViewById(R.id.toolbar_chats);
        mActionsButton = findViewById(R.id.toolbar_actions);


        mUnregisteredTitle = findViewById(R.id.please_register_to_start_title);
        mTabLayout.setupWithViewPager(mViewPager);
        mDialogFragment = new FilterDialogFragment();
        mSearchFragment = new SearchFragment();

        mAdView = findViewById(R.id.adView);

        drawerViewsInit();
        invalidateOptionsMenu();
        validateUserExist();
        mPresenter.getConfig();
        buttonsListeners();
        //Get info for inflating in drawer
        getUserInfo();
        mPresenter.registerSubscribers();

        callUserInfoDialogs();
        makeGenderCheck();

        check_values();
        chat_notification_change();
        action_notification_change();
        adsRequest();
    }

    private void adsRequest() {
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d("adMob","onAdLoaded");
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d("adMob","onAdFailedToLoad " + errorCode);
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Log.d("adMob","onAdOpened");
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                Log.d("adMob","onAdClicked");
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                Log.d("adMob","onAdLeftApplication");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Log.d("adMob","onAdClosed");
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideMenuItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Making first check when user is not registered for gender
     */
    private void makeGenderCheck() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mIsUserExist) {
                    if (mPresenter.getUserGender().isEmpty() || mPresenter.getUserGender().equals("default")) {
                        GenderDialogFragment genderDialogFragment = new GenderDialogFragment();
                        genderDialogFragment.setCancelable(false);
                        getSupportFragmentManager().beginTransaction().add(genderDialogFragment, GenderDialogFragment.TAG).commit();
                    }
                }
            }
        }, 100);
    }

    /**
     * Chain of logic checks must and secondary info to show needed dialog to  registered user
     */
    private void callUserInfoDialogs() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsUserExist && mPrefs.getValue(Consts.MUST_INFO).equals(Consts.DEFAULT)) {
                    mPresenter.userHasMustInfo();
                } else if (mIsUserExist && !mPrefs.getValue(Consts.MUST_INFO).equals(Consts.DEFAULT)) {
                    if (mPrefs.getValue(Consts.SHOW_FULL_DIALOG).equals(Consts.DEFAULT)) {
                        counter = 0;
                    } else {
                        counter = Integer.valueOf(mPrefs.getValue(Consts.SHOW_FULL_DIALOG));
                    }
                    if (counter >= Integer.valueOf(mPrefs.getValue(Consts.DIALOG_FREQUENCY))) {
                        mPrefs.setValue(Consts.SHOW_FULL_DIALOG, "0");
                        mPresenter.userHasMustInfo();
                    } else {
                        counter++;
                        mPrefs.setValue(Consts.SHOW_FULL_DIALOG, String.valueOf(counter));

                    }
                }
            }
        }, 5000);
    }

    /**
     * Showing must info dialog in main screen activity
     */
    @Override
    public void showMustInfoDialog() {
        Log.d(TAG_CLASS_NAME, "showMustInfoDialog");
        MustInfoDialogFragment mustInfoDialogFragment = new MustInfoDialogFragment();
        mustInfoDialogFragment.setCancelable(false);
        getSupportFragmentManager().beginTransaction().add(mustInfoDialogFragment, MustInfoDialogFragment.TAG).commit();
    }

    /**
     * Showing full info dialog in main screen activity
     */
    @Override
    public void showFullInfoDialog() {
        getSupportFragmentManager().beginTransaction()
                .add(ConfirmDialogFragment.newInstance(Consts.SLIDER_MODULE), ConfirmDialogFragment.TAG)
                .commit();
    }

    /**
     * Full profile info dialog listener calls to get default values list for intent
     */
    @Override
    public void onConfirm() {
        mPresenter.getDefaultList();
    }

    /**
     * Sending user to the playmarket to update the app
     */
    @Override
    public void onUpdateDialog() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.borisruzanov.russianwives")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.borisruzanov.russianwives")));
        }
    }

    /**
     * Showing slider with the list of default values fields of the user
     *
     * @param list
     */
    @Override
    public void showDefaultDialogScreen(ArrayList<String> list) {
        Intent intent = new Intent(MainScreenActivity.this, SliderActivity.class);
        intent.putStringArrayListExtra(Consts.DEFAULT_LIST, list);
        startActivity(intent);
    }

    /**
     * Showing the update app dialog
     */
    @Override
    public void showUpdateDialog() {
        getSupportFragmentManager().beginTransaction().add(ConfirmDialogFragment.newInstance(Consts.UPDATE_MODULE), ConfirmDialogFragment.TAG).commit();
    }

    /**
     * Calling auth window to log in
     */
    public void callAuthWindow() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());
        firebaseAnalytics.logEvent("registration_starts", null);

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    /**
     * Result after calling registration form ui
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                if (resultCode == Activity.RESULT_OK) {
                    firebaseAnalytics.logEvent("registration_completed", null);
                    mPresenter.saveUser();
                    reload();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("registration_error_type", "registration failed");
                    firebaseAnalytics.logEvent("registration_failed", bundle);
                }
            }
        }
    }

    /**
     * Back button pressed in main screen listener
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Close application
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unregisterSubscribers();
    }

    private void getUserInfo() {
        mMyProfilePresenter.setAllCurrentUserInfo();
    }

    private void buttonsListeners() {
        mChatsButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("newMessage").setValue("no");
                mChatsButton.setImageDrawable(getDrawable(R.drawable.ic_chat_black_24dp));
                Intent chatsIntent = new Intent(MainScreenActivity.this, ChatsActivity.class);
                startActivity(chatsIntent);
            }
        });

        mActionsButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("newVisit").setValue("no");
                FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("newLike").setValue("no");
                mActionsButton.setImageDrawable(getDrawable(R.drawable.ic_notifications_active_black_24dp));

                Intent chatsIntent = new Intent(MainScreenActivity.this, ActionsActivity.class);
                startActivity(chatsIntent);
            }
        });

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
                mTabLayout.getTabAt(1).select();
                FilterDialogFragment filterDialog = new FilterDialogFragment();
                getSupportFragmentManager().beginTransaction().add(filterDialog, "Dialog Fragment").commit();
            }
        });
    }

    private void drawerViewsInit() {
        //Drawer инициализация и листенеры
        mNavigationView = findViewById(R.id.nav_view);
        drawerClickListeners();
        mHeaderView = mNavigationView.getHeaderView(0);
        Menu menu = mNavigationView.getMenu();
//        mDrawerItemSafety = menu.findItem(R.id.drawer_top_menu_safety_item);
        mCloseDrawerButton = mHeaderView.findViewById(R.id.drawer_header_close_btn);
        mCloseDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });

        mViewProfileButton = mHeaderView.findViewById(R.id.drawer_header_view_profile);
        mViewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainScreenActivity.this, MyProfileActivity.class);
                startActivity(settingsIntent);
            }
        });

        mPurchaseSectionButton = mHeaderView.findViewById(R.id.drawer_header_purchase_section);
        mPurchaseSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainScreenActivity.this, R.string.this_is_not_ready,
                        Toast.LENGTH_LONG).show();
//                Intent settingsIntent = new Intent(MainScreenActivity.this, ServicesActivity.class);
//                startActivity(settingsIntent);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAuthWindow();
            }
        });
    }

    private void drawerClickListeners() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.drawer_top_menu_logout_item:
                        AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                        break;
                }
                return false;
            }
        });
    }

    private void tabsInit() {

        if (mIsUserExist) {

        } else {

        }


    }

    private void validateUserExist() {
        if (mIsUserExist) {

        } else {

        }
    }


    private void hideMenuItems() {
        if (mIsUserExist) {
            mViewPagerAdapter.addFragment(new OnlineUsersFragment(), getString(R.string.online_users_title));
            mViewPagerAdapter.addFragment(mSearchFragment, getString(R.string.search_title));

            mFilterButton.setVisibility(View.VISIBLE);
            mChatsButton.setVisibility(View.VISIBLE);
            mActionsButton.setVisibility(View.VISIBLE);

            mUnregisteredTitle.setVisibility(GONE);
            mRegisterButton.setVisibility(GONE);
        } else {
            mViewPagerAdapter.addFragment(new OnlineUsersFragment(), getString(R.string.online_users_title));

            mFilterButton.setVisibility(GONE);
            mUnregisteredTitle.setVisibility(View.VISIBLE);

            mTabLayout.setVisibility(GONE);
            //Hide drawer
            mToolbar.setNavigationIcon(null);
            mChatsButton.setVisibility(GONE);
            mActionsButton.setVisibility(GONE);
            mRegisterButton.setVisibility(View.VISIBLE);

        }

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                mTabLayout.getTabAt(tab.getPosition()).select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mViewPager.setAdapter(mViewPagerAdapter);

        mViewPager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void setAdapter(boolean isUserExist) {

    }

    @Override
    public void showGenderDialog() {
        getSupportFragmentManager().beginTransaction().add(new GenderDialogFragment(), GenderDialogFragment.TAG).commit();

    }


    @Override
    public void openSlider(ArrayList<String> stringList) {

    }

    @Override
    public void setUserData(FsUser user) {
        TextView mDrawerName = mHeaderView.findViewById(R.id.drawer_header_name);
        ImageView mDrawerImage = mHeaderView.findViewById(R.id.drawer_header_photo);
        if (user != null) {
            mDrawerName.setText(user.getName());
            if (!user.getImage().equals("default")) {
                Glide.with(this).load(user.getImage()).into(mDrawerImage);
            } else {
                Glide.with(this).load(this.getResources().getDrawable(R.drawable.default_avatar)).into(mDrawerImage);
            }

            mPresenter.changeUserOnlineStatus(user);
            mFsUser = user;
        }
    }


    private void reload() {
        mPresenter.makeDialogOpenDateDefault();
        Intent mainScreenIntent = new Intent(this, MainScreenActivity.class);
        startActivity(mainScreenIntent);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
////        switch (menuItem.getItemId()) {
//////            case R.id.drawer_top_menu_support_item:
//////
//////                return true;
//////            case R.id.drawer_top_menu_safety_item:
//////
//////                return true;
////            case R.id.drawer_top_menu_logout_item:
////                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        reload();
////                    }
////                });
////                break;
////        }
////        return false;
//    }

    @Override
    public void onUpdate() {
        mSearchFragment.onUpdate();
    }


    @SuppressLint("NewApi")
    private void chat_notification_change() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("newMessage").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (String.valueOf(dataSnapshot.getValue()).equals("yes")) {
                        mChatsButton.setImageDrawable(getDrawable(R.drawable.message_red));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @SuppressLint("NewApi")
    private void action_notification_change() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("newVisit").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (String.valueOf(dataSnapshot.getValue()).equals("yes")) {
                        mActionsButton.setImageDrawable(getDrawable(R.drawable.bell_red));
                        System.out.println(">>>>>>>>>>>>>>>>>>>>> VALUE CHANGED");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("newLike").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (String.valueOf(dataSnapshot.getValue()).equals("yes")) {
                        mActionsButton.setImageDrawable(getDrawable(R.drawable.bell_red));
                        System.out.println(">>>>>>>>>>>>>>>>>>>>> VALUE CHANGED");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void check_values() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("newMessage").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        HashMap<String, Object> x = new HashMap<>();
                        x.put("newMessage", "no");
                        x.put("newLike", "no");
                        x.put("newVisit", "no");
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(x);
                        System.out.println(">>>>>>>>>>>>>>>>>> VALUES WRITTEN");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
