package com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions;

import com.google.gson.annotations.SerializedName;

/**
 * Created by riteshpandhurkar on 23/3/16.
 */
public class MSISDNVerificationStatusInfo {

    private String statusCode;

    private String transactionID;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @Override
    public String toString() {
        return "MSISDNVerificationStatusInfo{" +
                "statusCode='" + statusCode + '\'' +
                ", transactionID='" + transactionID + '\'' +
                '}';
    }
}
