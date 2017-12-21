package com.ecarus.userregistration;


/**
 * Created by Rohit Haryani on 26-Jan-17.
 */

public class PreviousOrderList {

    private String mOrderDate, mOrderTime, mOrderId;

    public PreviousOrderList(String mOrderId, String mOrderDate, String mOrderTime) {
        this.mOrderId = mOrderId;
        this.mOrderDate = mOrderDate;
        this.mOrderTime = mOrderTime;

    }

    public String getmOrderId() {
        return mOrderId;
    }

    public String getmOrderDate() {
        return mOrderDate;
    }

    public String getmOrderTime() {
        return mOrderTime;
    }
}
