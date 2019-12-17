package com.borisruzanov.russianwives.mvp.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.mvp.model.interactor.main.MainInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.hots.HotUsersRepository;
import com.borisruzanov.russianwives.mvp.model.repository.user.UserRepository;
import com.borisruzanov.russianwives.mvp.ui.filter.FilterDialogFragment;
import com.borisruzanov.russianwives.mvp.ui.gender.GenderDialogFragment;
import com.borisruzanov.russianwives.mvp.ui.main.adapter.CustomViewPager;
import com.borisruzanov.russianwives.mvp.ui.main.adapter.MainPagerAdapter;
import com.borisruzanov.russianwives.mvp.ui.myprofile.MyProfileActivity;
import com.borisruzanov.russianwives.mvp.ui.onlineUsers.OnlineUsersFragment;
import com.borisruzanov.russianwives.mvp.ui.rewardvideo.RewardVideoActivity;
import com.borisruzanov.russianwives.mvp.ui.search.SearchFragment;
import com.borisruzanov.russianwives.mvp.ui.shop.ServicesActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

import static android.view.View.GONE;
import static com.borisruzanov.russianwives.models.Contract.RC_SIGN_IN;

public class MainScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private MainScreenPresenter mPresenter;

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

    private DialogFragment mDialogFragment;
    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        mPresenter = new MainScreenPresenter(new MainInteractor(new UserRepository(new Prefs(this)), new HotUsersRepository(new Prefs(this))));

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
        mRegisterButton = findViewById(R.id.login);


        mUnregisteredTitle = findViewById(R.id.please_register_to_start_title);

        mDialogFragment = new FilterDialogFragment();
        mSearchFragment = new SearchFragment();
        tabsInit();
        drawerViewsInit();
        invalidateOptionsMenu();
        validateUserExist();

        showDialogs();
    }

    private void drawerViewsInit() {
        //Drawer инициализация и листенеры
        mNavigationView = findViewById(R.id.nav_view);
        drawerClickListeners();
        View headerView = mNavigationView.getHeaderView(0);
        Menu menu = mNavigationView.getMenu();
        mDrawerItemSafety = menu.findItem(R.id.drawer_top_menu_safety_item);
        mCloseDrawerButton = headerView.findViewById(R.id.drawer_header_close_btn);
        mCloseDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mViewProfileButton = headerView.findViewById(R.id.drawer_header_view_profile);
        mViewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainScreenActivity.this, MyProfileActivity.class);
                startActivity(settingsIntent);
            }
        });

        mPurchaseSectionButton = headerView.findViewById(R.id.drawer_header_purchase_section);
        mPurchaseSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(MainScreenActivity.this, ServicesActivity.class);
                startActivity(settingsIntent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu, menu);
        hideMenuItems(menu);
        return true;
    }

    private void hideMenuItems(Menu menu) {
        if (mIsUserExist) {
            mViewPagerAdapter.addFragment(new OnlineUsersFragment(), getString(R.string.online_users_title));
            mViewPagerAdapter.addFragment(mSearchFragment, getString(R.string.search_title));

            mFilterButton.setVisibility(View.VISIBLE);
            mUnregisteredTitle.setVisibility(GONE);
            mRegisterButton.setVisibility(GONE);
        } else {
//            menu.clear();
            mViewPagerAdapter.addFragment(new OnlineUsersFragment(), getString(R.string.online_users_title));

            mFilterButton.setVisibility(GONE);
            mUnregisteredTitle.setVisibility(View.VISIBLE);

            mTabLayout.setVisibility(GONE);
            //Hide drawer
            mToolbar.setNavigationIcon(null);
            menu.findItem(R.id.shop).setVisible(false);
            menu.findItem(R.id.chats).setVisible(false);
            mRegisterButton.setVisibility(View.VISIBLE);

        }

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void showDialogs() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPresenter.getUserGender().isEmpty() || mPresenter.getUserGender().equals("default")) {
                    GenderDialogFragment genderDialogFragment = new GenderDialogFragment();
                    genderDialogFragment.setCancelable(false);
                    getSupportFragmentManager().beginTransaction().add(genderDialogFragment, GenderDialogFragment.TAG).commit();
                } else {
//                    getSupportFragmentManager().beginTransaction().add(new MustInfoDialogFragment(), GenderDialogFragment.TAG).commit();
//                    getSupportFragmentManager().beginTransaction().add(new ConfirmDialogFragment(), GenderDialogFragment.TAG).commit();
                }
            }
        }, 100);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shop:
                Intent settingsIntent = new Intent(MainScreenActivity.this, RewardVideoActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Calling auth window to log in
     */
    public void callAuthWindow() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
//                IdpResponse response = new IdpResponse.fromResultIntent(data)
                if (resultCode == Activity.RESULT_OK) {
//                    firebaseAnalytics.logEvent("registration_completed", null)
                    mPresenter.saveUser();
                    reload();
                } else {
//                    val bundle = Bundle()
//                    bundle.putString("registration_error_type", response?.error?.errorCode.toString())
//                    firebaseAnalytics.logEvent("registration_failed", bundle)
//                    if (response == null) {
//                    }
                }
            }
        }
    }

    private void reload() {
        mPresenter.makeDialogOpenDateDefault();
        Intent mainScreenIntent = new Intent(this, MainScreenActivity.class);
        startActivity(mainScreenIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_top_menu_support_item:

                return true;
            case R.id.drawer_top_menu_safety_item:

                return true;
            case R.id.drawer_top_menu_logout_item:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reload();
                    }
                });
                return true;
            default:
                return false;
        }
    }
}
