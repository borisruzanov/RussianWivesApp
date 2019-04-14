package com.borisruzanov.russianwives.mvp.ui.friendprofile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.App;
import com.borisruzanov.russianwives.OnItemClickListener;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.models.Contract;
import com.borisruzanov.russianwives.models.UserDescriptionModel;
import com.borisruzanov.russianwives.mvp.model.repository.rating.RatingManager;
import com.borisruzanov.russianwives.mvp.ui.chatmessage.ChatMessageActivity;
import com.borisruzanov.russianwives.mvp.ui.confirm.ConfirmDialogFragment;
import com.borisruzanov.russianwives.mvp.ui.global.adapter.UserDescriptionListAdapter;
import com.borisruzanov.russianwives.mvp.ui.mustinfo.MustInfoDialogFragment;
import com.borisruzanov.russianwives.mvp.ui.slider.SliderActivity;
import com.borisruzanov.russianwives.utils.Consts;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.borisruzanov.russianwives.models.Contract.RC_SIGN_IN;

public class FriendProfileActivity extends MvpAppCompatActivity implements FriendProfileView,
        ConfirmDialogFragment.ConfirmListener {

    //TODO change view initializations to ButterKnife binds
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;

    TextView nameText, ageText, countryText;
    ImageView imageView, messageIv, likeIv;

    Button btnAddFriend, btnStartChat;

    RecyclerView recyclerView;
    UserDescriptionListAdapter userDescriptionListAdapter;

    private String friendUid;

    @Inject
    @InjectPresenter
    FriendProfilePresenter presenter;

    @ProvidePresenter
    public FriendProfilePresenter provideFriendProfilePresenter(){
        return presenter;
    }

    private FirebaseAnalytics mFirebaseAnalytics;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App application = (App)getApplication();
        application.getComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        MobileAds.initialize(this, getString(R.string.mob_app_id));
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.mob_unit_banner_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener());

        RatingManager.getInstance().setUserMsgPts();


        //Transition
        //supportPostponeEnterTransition();
        imageView = findViewById(R.id.friend_activity_image);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = getIntent().getStringExtra("transitionName");
            imageView.setTransitionName(imageTransitionName);
        }*/

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.darkColor));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

        likeIv = findViewById(R.id.friend_activity_like_img);
        messageIv = findViewById(R.id.friend_activity_message_img);

        friendUid = getIntent().getStringExtra("uid");
        presenter.setLikeHighlighted(friendUid);

        fab = findViewById(R.id.friend_activity_fab);

        // listeners for action buttons
        likeIv.setOnClickListener(v -> {
            mFirebaseAnalytics.logEvent("like_from_friend_activity", null);
            Log.d("ClickedImg", "Image Like was clicked");
            presenter.setFriendLiked(friendUid);
        });
        messageIv.setOnClickListener(v -> {
            mFirebaseAnalytics.logEvent("start_chat_from_friend_activity", null);
            Log.d("ClickedImg", "Image Message was clicked");
            presenter.openChatMessage(friendUid);
        });

        btnAddFriend = findViewById(R.id.friend_activity_btn_add_friend);
        btnAddFriend.setOnClickListener(v -> presenter.setFriendLiked(friendUid));
        btnStartChat = findViewById(R.id.friend_activity_btn_start_chat);

        recyclerView = findViewById(R.id.recycler_list_friendDescription);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        userDescriptionListAdapter = new UserDescriptionListAdapter(setOnItemClickCallback());
        recyclerView.setAdapter(userDescriptionListAdapter);

        nameText = findViewById(R.id.friend_activity_tv_name);
        ageText = findViewById(R.id.friend_activity_tv_age);
        countryText = findViewById(R.id.friend_activity_tv_country);

        presenter.setAllInfo(friendUid);

        mFirebaseAnalytics.logEvent("friend_profile_viewed", null);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFriendData(String name, String age, String country, String image) {
        collapsingToolbarLayout.setTitle(name);
        nameText.setText(name);
        ageText.setText(age);
        countryText.setText(country);
        loadImage(image);
    }

    @Override
    public void setList(List<UserDescriptionModel> userDescriptionList) {
        if (userDescriptionList.isEmpty()) {
            Log.d(Contract.TAG, "The list is empty");
        } else {
            Log.d(Contract.TAG, "The list is NOT empty");
        }
        //Setting data to the adapter
        userDescriptionListAdapter.setData(userDescriptionList);
    }

    @Override
    public void setLikeHighlighted() {
        likeIv.setImageResource(R.drawable.ic_favorite_48);
    }

    @Override
    public void openRegDialog() {
        getSupportFragmentManager().beginTransaction()
                .add(ConfirmDialogFragment.newInstance(Consts.REG_MODULE), ConfirmDialogFragment.TAG)
                .commit();
    }

    public void callAuthWindow() {
        Log.d("RegDebug", "In callAuthWindow()");
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
                Log.d("RegDebug", "OnActivityResult with ok result");
                presenter.saveUser();
            } else {
                //Sign in failed
                if (response == null) {

                }
            }
        }
    }

    private void loadImage(String image) {
        RequestBuilder<Bitmap> requestBuilder = Glide.with(this).asBitmap();

        if (!image.equals(Consts.DEFAULT)) requestBuilder = requestBuilder.load(image);
        else requestBuilder = requestBuilder.load(R.drawable.default_avatar);

        requestBuilder.listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                supportPostponeEnterTransition();
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                supportPostponeEnterTransition();
                return false;
            }
        }).into(imageView);
    }

    private OnItemClickListener.OnItemClickCallback setOnItemClickCallback() {
        return (view, position) -> {};
    }

    @Override
    public void showMustInfoDialog() {
        getSupportFragmentManager().beginTransaction()
                .add(ConfirmDialogFragment.newInstance(Consts.FP_MODULE), ConfirmDialogFragment.TAG)
                .commit();
    }

    @Override
    public void openSlider(ArrayList<String> sliderList) {
        Intent intent = new Intent(this, SliderActivity.class);
        intent.putStringArrayListExtra(Consts.DEFAULT_LIST, sliderList);
        startActivity(intent);
    }

    @Override
    public void onConfirm() {
        getSupportFragmentManager().beginTransaction().add(new MustInfoDialogFragment(), MustInfoDialogFragment.TAG).commit();
    }

    @Override
    public void openChatMessage(String name, String image) {
        Intent chatMessageIntent = new Intent(this, ChatMessageActivity.class);
        chatMessageIntent.putExtra(Consts.UID, friendUid);
        chatMessageIntent.putExtra(Consts.NAME, name);
        chatMessageIntent.putExtra("photo_url", image);
        startActivity(chatMessageIntent);
    }

    private void onBack() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
       onBack();
    }
}
