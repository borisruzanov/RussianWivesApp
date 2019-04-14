package com.borisruzanov.russianwives.mvp.ui.shop;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.android.vending.billing.IInAppBillingService;
import com.borisruzanov.russianwives.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test extends AppCompatActivity {


    IInAppBillingService inAppBillingService;
    private static final int REQUEST_CODE_BUY = 1234;

    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final int BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2;
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;

    public static final int PURCHASE_STATUS_PURCHASED = 0;
    public static final int PURCHASE_STATUS_CANCELLED = 1;
    public static final int PURCHASE_STATUS_REFUNDED = 2;

    // Присоединяемся к сервису
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            inAppBillingService = IInAppBillingService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            inAppBillingService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        //Подключение к сервису
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
    }

    //Получаем список продуктов
    private List getProducts (){
        // для покупок
        List<InAppProduct> purchases =
                null;
        try {
            purchases = getInAppPurchases("inapp", "com.example.myapp_testing_inapp1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // для продписок
        try {
            List<InAppProduct> subscriptions =
                    getInAppPurchases("subs", "com.example.myapp_testing_subs1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchases;
    }

    private class InAppProduct {

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
    public List<InAppProduct> getInAppPurchases(String type, String... productIds) throws Exception {
        ArrayList<String> skuList = new ArrayList<>(Arrays.asList(productIds));
        Bundle query = new Bundle();
        query.putStringArrayList("ITEM_ID_LIST", skuList);
        Bundle skuDetails = inAppBillingService.getSkuDetails(
                3, getPackageName(), type, query);
        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
        List<InAppProduct> result = new ArrayList<>();
        for (String responseItem : responseList) {
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
        return result;
    }

    @SuppressLint("RestrictedApi")
    public void purchaseProduct(InAppProduct product) throws Exception {
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

        } catch (Exception e) {
        }
    }

    private void readMyPurchases() throws Exception {
        readMyPurchases("inapp"); // для покупок
        readMyPurchases("subs"); // для подписок
    }

    private void readMyPurchases(String type) throws Exception {
        String continuationToken = null;
        do {
            Bundle result = inAppBillingService.getPurchases(
                    3, getPackageName(), type, continuationToken);
            if (result.getInt("RESPONSE_CODE", -1) != 0) {
                throw new Exception("Invalid response code");
            }
            List<String> responseList = result.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            for (String purchaseData : responseList) {
                readPurchase(purchaseData);
            }
            continuationToken = result.getString("INAPP_CONTINUATION_TOKEN");
        } while (continuationToken != null);
    }

    private void consumePurchase(String purchaseToken) throws Exception {
        int result = inAppBillingService.consumePurchase(GooglePlayBillingConstants.API_VERSION,
                getPackageName(), purchaseToken);
        if (result == GooglePlayBillingConstants.BILLING_RESPONSE_RESULT_OK) {
            // начисляем бонусы

        } else {
            // обработка ошибки
        }

    }
}
