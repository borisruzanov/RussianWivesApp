package com.borisruzanov.russianwives.utils.network;

import com.google.gson.annotations.SerializedName;

/**
 *This class convert addemail success json response to java PDO class
 */
public class EmailResponse {
    @SerializedName("result")
    boolean result;

    public EmailResponse() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
