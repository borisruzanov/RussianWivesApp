package com.borisruzanov.russianwives.utils.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Implements Interceptor interface which
 * Implement method intercept which return header with authorization token
 */
public class AuthenticationInterceptor implements Interceptor {

    private String authToken;// store token variable

    public AuthenticationInterceptor(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken);

        Request request = builder.build();
        return chain.proceed(request);
    }
}