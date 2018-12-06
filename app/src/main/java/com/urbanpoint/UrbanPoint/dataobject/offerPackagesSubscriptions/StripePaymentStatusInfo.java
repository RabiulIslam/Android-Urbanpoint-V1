package com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions;

import com.google.gson.annotations.SerializedName;

/**
 * Created by riteshpandhurkar on 23/3/16.
 */
public class StripePaymentStatusInfo {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("charge_status")
    private String chargeStatus;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    @Override
    public String toString() {
        return "StripePaymentStatusInfo{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", chargeStatus='" + chargeStatus + '\'' +
                '}';
    }
}
