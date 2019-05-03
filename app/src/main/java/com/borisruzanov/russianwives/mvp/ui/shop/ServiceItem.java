package com.borisruzanov.russianwives.mvp.ui.shop;

public class ServiceItem {

    private String mTitle, mImage, mPrice;

    public ServiceItem(String mTitle, String mImage, String mPrice) {
        this.mTitle = mTitle;
        this.mImage = mImage;
        this.mPrice = mPrice;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }
}
