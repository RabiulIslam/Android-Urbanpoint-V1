package com.urbanpoint.UrbanPoint.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lenovo on 8/10/2017.
 */

public class VodafoneValidation {
    private int status;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String subscriptionContractId;

    public String getSubscriptionContractId() {
        return this.subscriptionContractId;
    }

    public void setSubscriptionContractId(String subscriptionContractId) {
        this.subscriptionContractId = subscriptionContractId;
    }
}
