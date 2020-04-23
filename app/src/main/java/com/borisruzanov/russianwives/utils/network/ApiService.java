package com.borisruzanov.russianwives.utils.network;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface which have method to call a particular request
 */
public interface ApiService {

    /**
     * Token request method
     *
     * @param grant_type
     * @param client_id
     * @param client_secret
     * @return
     */
    @FormUrlEncoded()
    @POST("oauth/access_token")
    Call<TokenResponse> getAuthToken(@Field("grant_type") String grant_type,@Field("client_id") String client_id,@Field("client_secret") String client_secret);

    /**
     * Emails add request method
     *
     * @param bookid
     * @return
     */
    @POST("addressbooks/{id}/emails")
    Call<EmailResponse> addEmail(@Path("id") String bookid, @Body JsonObject jsonObject);
}
