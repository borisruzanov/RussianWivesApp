package com.borisruzanov.russianwives.ui;

import android.content.DialogInterface;
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
import com.borisruzanov.russianwives.ui.fragments.ActivitiesFragment;
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

    //TODO Everywhere check that user is authorized

    //MVP
    @InjectPresenter
    MainPresenter mainPresenter;
    @ProvidePresenter
    MainPresenter provideMainPresenter() {
        return new MainPresenter(new MainInteractor(new FirebaseRepository()));
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment(searchFragment, "Search");
        mainPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        mainPagerAdapter.addFragment(new ActivitiesFragment(), "Activity");
        viewPager.setAdapter(mainPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);

        //MVP
        //mainPresenter.checkForUserExist();
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
            case R.id.menu_my_profile:
                Intent settingsIntent = new Intent(MainActivity.this, MyProfile.class);
                startActivity(settingsIntent);
                return true;
            case R.id.menu_filter:
                if(viewPager.getCurrentItem() == 0){
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
        if(requestCode == RC_SIGN_IN){
            //for checking errors
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                    mainPresenter.saveUser();
            }
            else {
                //Sign in failed
                if(response == null){}
            }
        }
    }

    @Override
    public void onUpdate() {
        searchFragment.onUpdate();
    }

    //TODO Finish method for checking first needed information
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
}
