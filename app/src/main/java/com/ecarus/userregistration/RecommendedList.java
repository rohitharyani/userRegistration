package com.ecarus.userregistration;

import android.util.Base64;

/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class RecommendedList {

    private String mProductName, mProductPrice;
    private String mProductWeight;
    private String mProductImage, mProductId;
    private String mProductImageRaw;
    private String mMfg,mExp;
    private String mCategory;

    public RecommendedList(String mCategory, String mProductId, String mProductName, String mProductPrice,
                           String mProductWeight, String mProductImage,
                           String mMfg, String mExp) {
        this.mCategory = mCategory;
        this.mProductName = mProductName;
        this.mProductPrice = mProductPrice;
        this.mProductWeight = mProductWeight;
        this.mProductImage = mProductImage;
        this.mProductId = mProductId;
        this.mProductImageRaw = mProductImage;
        this.mMfg = mMfg;
        this.mExp = mExp;

    }

    public String getmCategory(){
        return mCategory;
    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmProductPrice() {
        return mProductPrice;
    }

    public String getmProductWeight() {
        return mProductWeight;
    }

    public byte[] getmProductImage () {
        final byte[] decodedBytes = Base64.decode(mProductImage, Base64.DEFAULT);
        return decodedBytes;
    }

    public String getmProductImageRaw () {
        return mProductImageRaw;
    }

    public String getmProductId() { return mProductId; }

    public String getmMfg() { return mMfg; }

    public String getmExp() { return mExp; }
}
