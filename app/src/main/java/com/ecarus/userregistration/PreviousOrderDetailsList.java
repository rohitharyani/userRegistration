package com.ecarus.userregistration;



/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class PreviousOrderDetailsList {

    private String mProductName, mProductBill, mProductQuantity;

    public PreviousOrderDetailsList(String mProductName, String mProductBill,
                                    String mProductQuantity) {
        this.mProductName = mProductName;
        this.mProductBill = mProductBill;
        this.mProductQuantity = mProductQuantity;
    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmProductBill() {
        return mProductBill;
    }

    public String getmProductQuantity() { return mProductQuantity; }

}
