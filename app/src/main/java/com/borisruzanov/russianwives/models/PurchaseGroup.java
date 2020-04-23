package com.borisruzanov.russianwives.models;

public class PurchaseGroup {
    private String mGroupTitle;
    private String mGroupDescription;
    private String mGroupPrice;

    public PurchaseGroup(String mGroupTitle, String mGroupDescription, String mGroupPrice) {
        this.mGroupTitle = mGroupTitle;
        this.mGroupDescription = mGroupDescription;
        this.mGroupPrice = mGroupPrice;
    }

    public String getmGroupTitle() {
        return mGroupTitle;
    }

    public void setmGroupTitle(String mGroupTitle) {
        this.mGroupTitle = mGroupTitle;
    }

    public String getmGroupDescription() {
        return mGroupDescription;
    }

    public void setmGroupDescription(String mGroupDescription) {
        this.mGroupDescription = mGroupDescription;
    }

    public String getmGroupPrice() {
        return mGroupPrice;
    }

    public void setmGroupPrice(String mGroupPrice) {
        this.mGroupPrice = mGroupPrice;
    }
}
