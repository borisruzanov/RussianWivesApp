package com.borisruzanov.russianwives.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.Adapters.MainPagerAdapter;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.repository.CheckRepository;
import com.borisruzanov.russianwives.ui.fragments.ActionsFragment;
import com.borisruzanov.russianwives.ui.fragments.ChatsFragment;
import com.borisruzanov.russianwives.ui.fragments.FilterDialogFragment;
import com.borisruzanov.russianwives.ui.fragments.SearchFragment;
import com.borisruzanov.russianwives.mvp.model.interactor.MainInteractor;
import com.borisruzanov.russianwives.mvp.model.repository.FirebaseRepository;
import com.borisruzanov.russianwives.mvp.presenter.MainPresenter;
import com.borisruzanov.russianwives.mvp.view.MainView;
import com.borisruzanov.russianwives.ui.slider.SliderActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;

import static com.borisruzanov.russianwives.models.Contract.RC_SIGN_IN;

public class MainActivity extends MvpAppCompatActivity implements MainView, FilterDialogFragment.FilterListener {

    //MVP
    @InjectPresenter
    MainPresenter mainPresenter;

    @ProvidePresenter
    MainPresenter provideMainPresenter() {
        return new MainPresenter(new MainInteractor(new FirebaseRepository(), new CheckRepository()));
    }

    //UI
    Toolbar toolbar;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    TabLayout tabLayout;

    //Utility
    ViewPager viewPager;
    MainPagerAdapter mainPagerAdapter;

    //Fragments
    private DialogFragment dialogFragment;
    private SearchFragment searchFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialogFragment = new FilterDialogFragment();
        searchFragment = new SearchFragment();

        //UI
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.main_view_pager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment(searchFragment, getString(R.string.search_title));
        mainPagerAdapter.addFragment(new ChatsFragment(), getString(R.string.chats_title));
        mainPagerAdapter.addFragment(new ActionsFragment(), getString(R.string.actions_title));
        tabLayout = findViewById(R.id.main_tabs);

        //mainPresenter.checkForUserExist();
        mainPresenter.checkUserRegistered();

    }

    @Override
    public void setViewPager() {
        viewPager.setAdapter(mainPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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
                callAuthWindow();
                return true;
            case R.id.menu_my_profile:
                Intent settingsIntent = new Intent(MainActivity.this, MyProfileActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.menu_filter:
                if (viewPager.getCurrentItem() == 0) {
                    dialogFragment.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
                }
                return true;
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
        if (requestCode == RC_SIGN_IN) {
            //for checking errors
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                mainPresenter.checkForUserExist();
                mainPresenter.saveUser();
            } else {
                //Sign in failed
                if (response == null) {

                }
            }
        }
    }

    @Override
    public void onUpdate() {
        searchFragment.onUpdate();
    }

    //TODO Finish method for checking first needed information
    @Override
    public void checkingForUserInfo() {
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.didnt_fill_form_question);
        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            Intent sliderIntent = new Intent(MainActivity.this, SliderActivity.class);
            startActivity(sliderIntent);
        });
        builder.setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog = builder.create();
        alertDialog.show();
    }
}
