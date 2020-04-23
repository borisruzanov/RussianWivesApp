package com.borisruzanov.russianwives.mvp.ui.rewardvideo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.borisruzanov.russianwives.App;
import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.model.data.prefs.Prefs;
import com.borisruzanov.russianwives.utils.Consts;

import java.util.ArrayList;
import java.util.List;

public class RewardVideoActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    RewardVideoPresenter presenter;

    Toolbar toolbar;

    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String APP_ID = "ca-app-pub-5095813023957397~1146672660";

    private TextView mFreeTitle;
    private TextView mFreeDescription;
    private TextView mFreePrice;
    private TextView mFreeList;
    private Button mFreeButton;
    private ImageView mFreeOpenButton;
    private RelativeLayout mFreeListContainer;
    private boolean mFreeListOpen = false;

    private TextView mRegularTitle;
    private TextView mRegularDescription;
    private TextView mRegularPrice;
    private TextView mRegularList;
    private Button mRegularButton;
    private ImageView mRegularOpenButton;
    private RelativeLayout mRegularListContainer;
    private boolean mRegularListOpen = false;

    private TextView mPremiumTitle;
    private TextView mPremiumDescription;
    private TextView mPremiumPrice;
    private TextView mPremiumList;
    private Button mPremiumButton;
    private ImageView mPremiumOpenButton;
    private RelativeLayout mPremiumListContainer;
    private boolean mPremiumListOpen = true;

    private Prefs mPrefs;

    private String mPrice;
    private String mVipPrice;

    private BillingClient billingClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App application = (App) getApplication();
        application.getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewardvideo);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Video For Reward");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        mPrefs = new Prefs(this);

        prepareUI();

    }

    private void prepareUI() {

        mFreeTitle = findViewById(R.id.purchase_free_title);
        mFreeTitle.setText(mPrefs.getValue(Consts.PUR_FREE_TITLE));
        mFreeDescription = findViewById(R.id.purchase_free_description);
        mFreeDescription.setText(mPrefs.getValue(Consts.PUR_FREE_DESCRIPTION));
        mFreePrice = findViewById(R.id.purchase_free_price);
        mFreePrice.setText(mPrefs.getValue(Consts.PUR_FREE_PRICE));
        mFreeList = findViewById(R.id.purchase_free_features_list);
        mFreeList.setText(mPrefs.getValue(Consts.PUR_FREE_LIST));
        mFreeButton = findViewById(R.id.purchase_free_activate);
        mFreeListContainer = findViewById(R.id.purchase_free_container_description);
        mFreeOpenButton = findViewById(R.id.purchase_free_img_open);
        mFreeOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mFreeListOpen) {
                    mFreeListContainer.setVisibility(View.VISIBLE);
                    mFreeListOpen = true;
                } else {
                    mFreeListContainer.setVisibility(View.GONE);
                    mFreeListOpen = false;

                }
            }
        });

        mRegularTitle = findViewById(R.id.purchase_regular_title);
        mRegularTitle.setText(mPrefs.getValue(Consts.PUR_REGULAR_TITLE));
        mRegularDescription = findViewById(R.id.purchase_regular_description);
        mRegularDescription.setText(mPrefs.getValue(Consts.PUR_REGULAR_DESCRIPTION));
        mRegularPrice = findViewById(R.id.purchase_regular_price);
        mRegularPrice.setText(mPrefs.getValue(Consts.PUR_REGULAR_PRICE));
        mRegularList = findViewById(R.id.purchase_regular_features_list);
        mRegularList.setText(mPrefs.getValue(Consts.PUR_REGULAR_LIST));
        mRegularButton = findViewById(R.id.purchase_regular_activate);
        mRegularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnection(Consts.PREMIUM);
            }
        });
        mRegularListContainer = findViewById(R.id.purchase_regular_container_description);
        mRegularOpenButton = findViewById(R.id.purchase_regular_img_open);
        mRegularOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! mRegularListOpen) {
                    mRegularListContainer.setVisibility(View.VISIBLE);
                    mRegularListOpen = true;
                } else {
                    mRegularListContainer.setVisibility(View.GONE);
                    mRegularListOpen = false;
                }
            }
        });

        mPremiumTitle = findViewById(R.id.purchase_premium_title);
        mPremiumTitle.setText(mPrefs.getValue(Consts.PUR_PREMIUM_TITLE));
        mPremiumDescription = findViewById(R.id.purchase_premium_description);
        mPremiumDescription.setText(mPrefs.getValue(Consts.PUR_PREMIUM_DESCRIPTION));
        mPremiumPrice = findViewById(R.id.purchase_premium_price);
        mPremiumPrice.setText(mPrefs.getValue(Consts.PUR_PREMIUM_PRICE));
        mPremiumList = findViewById(R.id.purchase_premium_features_list);
        mPremiumList.setText(mPrefs.getValue(Consts.PUR_PREMIUM_LIST));
        mPremiumButton = findViewById(R.id.purchase_premium_activate);
        mPremiumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnection(Consts.VIP);
            }
        });
        mPremiumListContainer = findViewById(R.id.purchase_premium_container_description);
        mPremiumOpenButton = findViewById(R.id.purchase_premium_img_open);
        mPremiumOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! mPremiumListOpen) {
                    mPremiumListContainer.setVisibility(View.VISIBLE);
                    mPremiumListOpen = true;
                } else {
                    mPremiumListContainer.setVisibility(View.GONE);
                    mPremiumListOpen = false;
                }
            }
        });
    }

    private void startConnection(String type) {

        //Call newBuilder() to create an instance of BillingClient You must also call setListener()
        //passing a reference to a PurchasesUpdatedListener to receive updates on purchases initiated
        // by your app, as well as those initiated by the Google Play Store.
        BillingClient billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();

        billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS_UPDATE);
        //Establish a connection to Google Play. The setup process is asynchronous, and you must
        //implement a BillingClientStateListener to receive a callback once the setup of the client
        //is complete and it’s ready to make further requests.

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //specifies a list of product ID strings and a SkuType
                    List<String> skuList = new ArrayList<>();
                    skuList.add(type);
//                    skuList.add("vip");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    //IMPORTANT The SkuType can be either SkuType.INAPP for one-time products or SkuType.SUBS for subscriptions
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {
                                    // Process the result.
                                    //Retrieving a product’s price is an important step before a user can purchase a product because
                                    // the price is different for each user based on their country of origin.
                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                                        for (SkuDetails skuDetails : skuDetailsList) {
                                            String sku = skuDetails.getSku();
                                            String price = skuDetails.getPrice();
                                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                    .setSkuDetails(skuDetailsList.get(0))
                                                    .build();
                                            billingClient.launchBillingFlow(RewardVideoActivity.this, flowParams);
                                        }
                                    }


                                }
                            });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                String s = "error";
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
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

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> list) {
        String result = billingResult.getDebugMessage();
        int message = billingResult.getResponseCode();
        String object = billingResult.toString();
        String s = "";
    }
}

