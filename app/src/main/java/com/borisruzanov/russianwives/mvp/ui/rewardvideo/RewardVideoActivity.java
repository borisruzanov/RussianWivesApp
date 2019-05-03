package com.borisruzanov.russianwives.mvp.ui.rewardvideo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.borisruzanov.russianwives.App;
import com.borisruzanov.russianwives.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import javax.inject.Inject;

public class RewardVideoActivity extends MvpAppCompatActivity implements
        RewardedVideoAdListener, RewardVideoView {

    @Inject
    @InjectPresenter
    RewardVideoPresenter presenter;

    @ProvidePresenter
    RewardVideoPresenter provideRewardPresenter() {
        return presenter;
    }

    Toolbar toolbar;

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String APP_ID = "ca-app-pub-5095813023957397~1146672660";

    private TextView mTitle;
    private TextView coinCountText;

    private RewardedVideoAd rewardedVideoAd;
    private Button showVideoButton;

//    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App application = (App)getApplication();
        application.getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewardvideo);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Video For Reward");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, APP_ID);

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        // Create the "show" button, which shows a rewarded video if one is loaded.
        showVideoButton = findViewById(R.id.show_video_button);
        showVideoButton.setVisibility(View.VISIBLE);
        showVideoButton.setOnClickListener(view -> showRewardedVideo());

        mTitle = findViewById(R.id.service_page_title);
        mTitle.setText("Watch video and get your coins for FREE");


        // Display current coin count to user.
        coinCountText = findViewById(R.id.coin_count_text);

    }

    @Override
    public void onPause() {
        super.onPause();
        rewardedVideoAd.pause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        rewardedVideoAd.resume(this);
    }


    private void loadRewardedVideoAd() {
        if(!rewardedVideoAd.isLoaded()) {
            //rewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    @Override
    public void showCoinsCount(int coins) {
        coinCountText.setVisibility(View.VISIBLE);
        coinCountText.setText(String.format("%s %d", getString(R.string.you_have), coins));
    }

    private void showRewardedVideo() {
//        showVideoButton.setVisibility(View.INVISIBLE);
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
//        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
//        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        // Preload the next video ad.
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
//        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
//        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
//        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem reward) {
//        Toast.makeText(this,
//                String.format(" onRewarded! currency: %s amount: %d", reward.getType(),
//                        reward.getAmount()),
//                Toast.LENGTH_SHORT).show();
        presenter.addUserCoins(reward.getAmount());
    }

    @Override
    public void onRewardedVideoStarted() {
//        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
//        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

