package com.borisruzanov.russianwives.utils.network;

import android.text.TextUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    public static final String BASE_URL="https://api.sendpulse.com"; //base url for calling api

    private static OkHttpClient.Builder httpClient=new OkHttpClient.Builder(); //httpclient for add authorizenatoion token


    //Retrofit builder object
    private static Retrofit.Builder builder=new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    //a constructor to call api without authorizenatoion token
    //the method has one parameter which is a interface class on which all request method saved
    public static <S> S createService(Class<S> serviceClass){
        return builder.build().create(serviceClass);
    }

    //a constructor to call api with authorization token
    //the method has one parameter which is a interface class on which all request method saved
    //the second parameter is authorization token value
    public static <S> S createService(Class<S> serviceClass,final String authToken){
        Retrofit retrofit=null;
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }

}
