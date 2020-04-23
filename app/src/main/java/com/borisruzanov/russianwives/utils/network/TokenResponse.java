package com.borisruzanov.russianwives.utils.network;

import com.google.gson.annotations.SerializedName;

/**
 * This class convert header success json response to PDO object
 */
public class TokenResponse {


    @SerializedName("access_token")
    String access_token;
    @SerializedName("token_type")
    String token_type;
    @SerializedName("expires_in")
    String expires_in;

    public TokenResponse() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                '}';
    }
}
