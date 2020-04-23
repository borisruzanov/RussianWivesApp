package com.borisruzanov.russianwives.utils.network;

import com.google.gson.annotations.SerializedName;

/**
 * This class convert addemail error json to java PDO object
 */
public class EmailErrorResponse {
    @SerializedName("error_code")
    int error_code;

    @SerializedName("message")
    String message;

    public EmailErrorResponse() {
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmailErrorResponse{" +
                "error_code=" + error_code +
                ", message='" + message + '\'' +
                '}';
    }
}
