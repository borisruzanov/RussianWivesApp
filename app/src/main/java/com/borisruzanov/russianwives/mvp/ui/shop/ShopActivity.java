package com.borisruzanov.russianwives.mvp.ui.shop;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.android.vending.billing.IInAppBillingService;
import com.borisruzanov.russianwives.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    Toolbar toolbar;
    Button mFreeTokensBtn;


    private static final int BILLING_UNAVAILABLE = 3;
    private static final int DEVELOPER_ERROR = 5;
    private static final int ERROR = 6;
    private static final int FEATURE_NOT_SUPPORTED = -2;
    private static final int ITEM_ALREADY_OWNED = 7;
    private static final int ITEM_NOT_OWNED = 8;
    private static final int ITEM_UNAVAILABLE = 4;
    private static final int OK = 0;
    private static final int SERVICE_DISCONNECTED = -1;
    private static final int SERVICE_UNAVAILABLE = 2;
    private static final int USER_CANCELED = 1;

    private static final int REQUEST_CODE_BUY = 1234;

    public static final int BILLING_RESPONSE_RESULT_OK = 0;


    private static final String TAG = "billing_flow";
    private final String KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApBF2s+8xxGWq5dAtgmevor+IEP79zkNz9rkkyBDl8mPgHYNVeH1I8walKh+NVl87OeT2SdITFDxYg5s5Qrn1/GGAtqy2NbaB/WI/fBGt4f9QtpM5E0wcoIyvHX4HsKWPHwWwN4HU5PAAmMSNC2ZRSaNdX3tb5BC+b3a2LyMdjCp+syQQl64v/o7rVci3XfrnN9zW/UB5pKUqIcy9keMBzEwHVQjjSxMGuHoW+EpTfZUrkpD4kPgAALqXtztagmBOBi9R5ob+GnsUMWhnEwFOHlsKNBRLB0jrXSF3zAQ2TPWY8DAzcRe3siau0txgkPEFh0hr7l/WKk7Q7tQK+jcBvwIDAQAB";


    private BillingClient mBillingClient;
    private IInAppBillingService inAppBillingService;

    ServiceConnection serviceConnection;


    //True if billing service is connected now.
    private boolean mIsServiceConnected = false;

    // Default value of mBillingClientResponseCode until BillingManager was not yet initialized
    private static final int BILLING_MANAGER_NOT_INITIALIZED = -1;

    //<-------DONTKNOW-------->
    private int mBillingClientResponseCode = BILLING_MANAGER_NOT_INITIALIZED;
    private String mOneDollarPurchasePrice = "";

    Button button_buy;
    Button button_consume;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.shop_activity_frame_top, new PurchaseFragment());

        ft.commit();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.shop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

        mFreeTokensBtn = findViewById(R.id.shop_activity_free_btn);
        mFreeTokensBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopActivity.this, RewardVideo.class);
                startActivity(intent);
            }
        });


        billingClientInit();
        button_buy = findViewById(R.id.button_buy);
        button_buy.setVisibility(View.GONE);
        button_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryInAppProductDetails();
            }
        });
        button_consume = findViewById(R.id.button_consume);
        button_consume.setVisibility(View.GONE);
        button_consume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consumeFromLibrary();
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

    /**
     * Billing Client init
     */
    private void billingClientInit() {
        Log.d(TAG, "Client start connection...");
        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                    Log.d(TAG, "Client setup status - OK ");

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the billingClientInit() method.
                Log.d(TAG, "response Billing disconnected ");
            }
        });
    }

    /**
     * Making query for product details
     */
    private void queryInAppProductDetails() {
        Log.d(TAG, "making query ");
        List<String> skuList = new ArrayList<>();
        //Product which you added in play market
        //A one-time product that can be used indefinitely is called a non-consumable
        //A one-time product with non-infinite use is called a consumable
        skuList.add("minimum");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        //To query Google Play for in-app product details, call this method
        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                        Log.d(TAG, "response from query -> " + responseCode);
                        if (responseCode == BillingClient.BillingResponse.OK
                                && skuDetailsList != null) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                String sku = skuDetails.getSku();
                                String price = skuDetails.getPrice();
                                if ("minimum".equals(sku)) {
                                    //Retrieving a product’s price is an important step before
                                    // a user can purchase a product because the price is different
                                    // for each user based on their country of origin.
                                    mOneDollarPurchasePrice = price;
                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetails)
                                            .build();
                                    mBillingClient.launchBillingFlow(ShopActivity.this, flowParams);

                                }  //TODO Handle other scenarios of SKU

                            }

                        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED
                                && skuDetailsList != null) {
                            consumeFromLibrary();
                            //TODO Handle scenarios of other responses
                        }

                    }
                });
    }

    private void consumeFromLibrary() {
        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@BillingClient.BillingResponse int responseCode, String outToken) {
                Log.d(TAG, "Consume response -> " + responseCode);
                if (responseCode == BillingClient.BillingResponse.OK) {
                    // Handle the success of the consume operation.
                    // For example, increase the number of coins inside the user's basket.
                } else if (responseCode == BillingClient.BillingResponse.SERVICE_DISCONNECTED) {
                    Log.d(TAG, "SERVICE DISCONNECTED");

                }
            }
        };
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        List<Purchase> purchaseList = purchasesResult.getPurchasesList();
        for (Purchase purchase : purchaseList) {
            mBillingClient.consumeAsync(purchase.getPurchaseToken(), listener);
        }
    }

    private List<Purchase> getPurchasesFromLibrary() {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        return purchasesResult.getPurchasesList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        switch (responseCode) {
            // case statements
            // values must be of same type of expression
            case BILLING_UNAVAILABLE:
                // Billing API version is not supported for the type requested
                break;
            case DEVELOPER_ERROR:
                // Invalid arguments provided to the API.
                break;
            case ERROR:
                // Fatal error during the API action
                break;
            case FEATURE_NOT_SUPPORTED:
                // Requested feature is not supported by Play Store on the current device.
                break;
            case ITEM_ALREADY_OWNED:
                // Failure to purchase since item is already owned
                break;
            case ITEM_NOT_OWNED:
                // Failure to consume since item is not owned
                break;
            case ITEM_UNAVAILABLE:
                // Requested product is not available for purchase
                break;
            case OK:
                // Success
                handlePurchase(purchases.get(0));
                break;
            case SERVICE_DISCONNECTED:
                // Play Store service is not connected now - potentially transient state.
                break;
            case SERVICE_UNAVAILABLE:
                // Network connection is down
                break;
            case USER_CANCELED:
                // User pressed back or canceled a dialog
                break;
            default:
        }
    }

    private void handlePurchase(Purchase purchase) {
        consumeFromLibrary();
        Toast.makeText(this, "Purchase - SUCCESS! " + purchase.getPurchaseToken(), Toast.LENGTH_LONG).show();
    }


    //--------------------------------------INIT------------------------------------

    /**
     * Billing Service connection
     */
    private void billingClientConnection() {
        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        Log.d(TAG, "Billing client start connection...");
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                inAppBillingService = IInAppBillingService.Stub.asInterface(service);
                Log.d(TAG, "Billing client connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                inAppBillingService = null;
            }
        };

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    //--------------------------------------GET LIST OF PRODUCTS FROM PLAY MARKET ------------------------------------

    /**
     * Get list of available products from Play Market
     */
    private List<InAppProduct> getProductsFromPlayMarket() {
        Log.d(TAG, "Start getting list of purchases...");
        List<InAppProduct> products = null;
        try {
            Log.d(TAG, "Trying getting list of purchases...");
            products = getListOfProducts("inapp", "com.borisruzanov.russianwives");
        } catch (Exception e) {
            Log.d(TAG, "get products exception: " + e.getMessage());
            e.printStackTrace();
        }
        for (InAppProduct product : products) {
            Log.d(TAG, "Product Item: " + product);
        }
        return products;
    }

    /**
     * Getting list of purchases
     */
    private List<InAppProduct> getListOfProducts(String type, String... productIds) throws
            Exception {
        Log.d(TAG, "Getting list of purchases...");
        ArrayList<String> skuList = new ArrayList<>(Arrays.asList(productIds));
        skuList.add("minimum");
        Bundle query = new Bundle();
        query.putStringArrayList("ITEM_ID_LIST", skuList);
        Bundle skuDetails = inAppBillingService.getSkuDetails(
                3, getPackageName(), type, query);
        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
        List<InAppProduct> result = new ArrayList<>();
        for (String responseItem : responseList) {
            Log.d(TAG, "Loop of item: " + responseItem);
            JSONObject jsonObject = new JSONObject(responseItem);
            InAppProduct product = new InAppProduct();
            // "com.example.myapp_testing_inapp1"
            product.productId = jsonObject.getString("productId");
            // Покупка
            product.storeName = jsonObject.getString("title");
            // Детали покупки
            product.storeDescription = jsonObject.getString("description");
            // "0.99USD"
            product.price = jsonObject.getString("price");
            // "true/false"
            product.isSubscription = jsonObject.getString("type").equals("subs");
            // "990000" = цена x 1000000
            product.priceAmountMicros =
                    Integer.parseInt(jsonObject.getString("price_amount_micros"));
            // USD
            product.currencyIsoCode = jsonObject.getString("price_currency_code");
            result.add(product);
        }

        Log.d(TAG, "Size of the product list: " + result.size());
        return result;
    }

    class InAppProduct {

        public String productId;
        public String storeName;
        public String storeDescription;
        public String price;
        public boolean isSubscription;
        public int priceAmountMicros;
        public String currencyIsoCode;

        public String getSku() {
            return productId;
        }

        String getType() {
            return isSubscription ? "subs" : "inapp";
        }

    }


    //--------------------------------------BUY THE PRODUCT------------------------------------

    /**
     * Buy the product
     */
    private void buyTheProduct(InAppProduct product) throws Exception {
        String sku = product.getSku();
        String type = product.getType();
        // сюда вы можете добавить произвольные данные
        // потом вы сможете получить их вместе с покупкой
        String developerPayload = "12345";
        Bundle buyIntentBundle = inAppBillingService.getBuyIntent(
                3, getPackageName(),
                sku, type, developerPayload);
        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");

        startIntentSenderForResult(pendingIntent.getIntentSender(),
                REQUEST_CODE_BUY, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0), null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BUY) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", -1);
            if (responseCode == BILLING_RESPONSE_RESULT_OK) {
                String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
                String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
                // можете проверить цифровую подпись
                readPurchase(purchaseData);
            } else {
                // обрабатываем ответ
            }
        }
    }


    //----------------------------------------READ PURCHASES------------------------------------

    private void readPurchase(String purchaseData) {
        try {
            JSONObject jsonObject = new JSONObject(purchaseData);
            // ид покупки, для тестовой покупки будет null
            String orderId = jsonObject.optString("orderId");
            // "com.example.myapp"
            String packageName = jsonObject.getString("packageName");
            // "com.example.myapp_testing_inapp1"
            String productId = jsonObject.getString("productId");
            // unix-timestamp времени покупки
            long purchaseTime = jsonObject.getLong("purchaseTime");
            // PURCHASE_STATUS_PURCHASED
            // PURCHASE_STATUS_CANCELLED
            // PURCHASE_STATUS_REFUNDED
            int purchaseState = jsonObject.getInt("purchaseState");
            // "12345"
            String developerPayload = jsonObject.optString("developerPayload");
            // токен покупки, с его помощью можно получить
            // данные о покупке на сервере
            String purchaseToken = jsonObject.getString("purchaseToken");
            // далее вы обрабатываете покупку
            consumePurchase(purchaseToken);
        } catch (Exception e) {
        }
    }

    /**
     * Getting
     *
     * @throws Exception
     */
    private void getUserPurchases() throws Exception {
        String continuationToken = null;
        do {
            Bundle result = inAppBillingService.getPurchases(
                    3, getPackageName(), "inapp", continuationToken);
            Log.d(TAG, "Result of getting user purchases bundle: " + result.toString());
            if (result.getInt("RESPONSE_CODE", -1) != 0) {
                throw new Exception("Invalid response code");
            }
            List<String> responseList = result.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            for (String purchaseData : responseList) {
                Log.d(TAG, "Purchase data: " + purchaseData);
                readPurchase(purchaseData);
            }
            continuationToken = result.getString("purchaseToken");
            Log.d(TAG, "Final Token:  " + continuationToken);

        } while (continuationToken != null);
    }


    //---------------------------------CONSUME THE PURCHASE----------------------------------
    //После этого вы уже не сможете прочитать данные о покупке — она будет недоступна через getPurchases().
    private void consumePurchase(String purchaseToken) throws Exception {
        Log.d(TAG, "Start consuming purchase token ... " + purchaseToken);
//        int result = inAppBillingService.consumePurchase(GooglePlayBillingConstants.API_VERSION,
//                getPackageName(), purchaseToken);
//        Log.d(TAG, "Result of consuming -> " + result);

        mBillingClient.consumeAsync(purchaseToken, new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(int responseCode, String purchaseToken) {
                Log.d(TAG, "onConsumeResponse: purchased SKU  " + responseCode);
            }
        });


//        if (result == GooglePlayBillingConstants.BILLING_RESPONSE_RESULT_OK) {
//            // начисляем бонусы
//            Log.d(TAG, "Purchase consumed.");
//        } else {
//            // обработка ошибки
//            Log.d(TAG, "Purchase consuming error.");
//        }

    }


    /*
     * By default you cannot buy the same product again and again...after you purchased the item you should cosume it.
     * For testing purpose i am going to consume all the product each and everytim i buy...
     */
    public void consumePurchases(List<Purchase> purchases) {
        try {
            if (purchases != null && purchases.size() > 0) {
                for (final Purchase purchase : purchases) {
                    if (purchase.getPurchaseToken() != null && !purchase.getPurchaseToken().isEmpty()) {
                        mBillingClient.consumeAsync(purchase.getPurchaseToken(), new ConsumeResponseListener() {
                            @Override
                            public void onConsumeResponse(int responseCode, String purchaseToken) {
                                Log.d(TAG, "onConsumeResponse: purchased SKU  " + purchase.getSku());
                                Log.d(TAG, "onConsumeResponse: purchased getPurchaseToken  " + purchase.getPurchaseToken());
                            }
                        });
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startPurchaseRequest(SkuDetails skuDetails) {
        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().

    }


    /**
     * Reconnection process handling
     *
     * @param runnable
     */
    private void executeServiceRequest(Runnable runnable) {
        if (mIsServiceConnected) {
            runnable.run();
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(runnable);
        }
    }

    /**
     * Start billing service connection
     *
     * @param executeOnSuccess
     */
    public void startServiceConnection(final Runnable executeOnSuccess) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                Log.d(TAG, "Setup finished. Response code: " + billingResponseCode);

                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    mIsServiceConnected = true;
                    if (executeOnSuccess != null) {
                        executeOnSuccess.run();
                    }
                }
                mBillingClientResponseCode = billingResponseCode;
            }

            @Override
            public void onBillingServiceDisconnected() {
                mIsServiceConnected = false;
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
//                        if (skuDetailsList != null) {
//                            switch (responseCode) {
//                                case BILLING_UNAVAILABLE:
//                                    // Billing API version is not supported for the type requested
//                                    Log.d(TAG, "Response -> " + "BILLING_UNAVAILABLE");
//                                    break;
//                                case DEVELOPER_ERROR:
//                                    // Invalid arguments provided to the API.
//                                    Log.d(TAG, "Response -> " + "DEVELOPER_ERROR");
//                                    break;
//                                case ERROR:
//                                    // Fatal error during the API action
//                                    Log.d(TAG, "Response -> " + "ERROR");
//                                    break;
//                                case FEATURE_NOT_SUPPORTED:
//                                    // Requested feature is not supported by Play Store on the current device.
//                                    Log.d(TAG, "Response -> " + "FEATURE_NOT_SUPPORTED");
//                                    break;
//                                case ITEM_ALREADY_OWNED:
//                                    // Failure to purchase since item is already owned
//                                    Log.d(TAG, "Response -> " + "ITEM_ALREADY_OWNED");
//                                    break;
//                                case ITEM_NOT_OWNED:
//                                    // Failure to consume since item is not owned
//                                    Log.d(TAG, "Response -> " + "ITEM_NOT_OWNED");
//                                    break;
//                                case ITEM_UNAVAILABLE:
//                                    // Requested product is not available for purchase
//                                    Log.d(TAG, "Response -> " + "ITEM_UNAVAILABLE");
//                                    break; // break is optional
//                                case OK:
//                                    // Success
//                                    for (SkuDetails skuDetails : skuDetailsList) {
//                                        String sku = skuDetails.getSku();
//                                        String price = skuDetails.getPrice();
//                                        if ("minimum".equals(sku)) {
//                                            Log.d(TAG, "In if");
//                                            //Retrieving a product’s price is an important step before
//                                            // a user can purchase a product because the price is different
//                                            // for each user based on their country of origin.
//                                            mOneDollarPurchasePrice = price;
//                                            //Starts the billing flow with params of purchase
//                                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
//                                                    .setSkuDetails(skuDetails)
//                                                    .build();
//                                            responseCode = mBillingClient.launchBillingFlow(ShopActivity.this, flowParams);
//                                        } else {
//                                            //TODO Handle other scenarios of SKU
//                                        }
//                                    }
//                                    break;
//                                case SERVICE_DISCONNECTED:
//                                    // Play Store service is not connected now - potentially transient state.
//                                    Log.d(TAG, "Response -> " + "SERVICE_DISCONNECTED");
//                                    break;
//                                case SERVICE_UNAVAILABLE:
//                                    // Network connection is down
//                                    Log.d(TAG, "Response -> " + "SERVICE_UNAVAILABLE");
//                                    break;
//                                case USER_CANCELED:
//                                    // User pressed back or canceled a dialog
//                                    Log.d(TAG, "Response -> " + "USER_CANCELED");
//                                    break;
//                                // We can have any number of case statements
//                                // below is default statement, used when none of the cases is true.
//                                // No break is needed in the default case.
//                                default:
//                                    // Statements
//                            }
//                        }