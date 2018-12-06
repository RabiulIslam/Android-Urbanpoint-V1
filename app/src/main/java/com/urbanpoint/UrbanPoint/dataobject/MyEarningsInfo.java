package com.urbanpoint.UrbanPoint.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ritesh
 * The class handles all the objects associated with the MyEarnings entity
 */
public class MyEarningsInfo {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("friendmailcount")
    private int friendsSignedUp;

    @SerializedName("moneypaid")
    private int cashPaid;

    @SerializedName("Referedamount")
    private int myEarnings;

    @SerializedName("Referredfriend")
    private int referredFriend;

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

    public int getFriendsSignedUp() {
        return friendsSignedUp;
    }

    public void setFriendsSignedUp(int friendsSignedUp) {
        this.friendsSignedUp = friendsSignedUp;
    }

    public int getCashPaid() {
        return cashPaid;
    }

    public void setCashPaid(int cashPaid) {
        this.cashPaid = cashPaid;
    }

    public int getMyEarnings() {
        return myEarnings;
    }

    public void setMyEarnings(int myEarnings) {
        this.myEarnings = myEarnings;
    }

    public int getReferredFriend() {
        return referredFriend;
    }

    public void setReferredFriend(int referredFriend) {
        this.referredFriend = referredFriend;
    }

    @Override
    public String toString() {
        return "MyEarningsInfo{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", friendsSignedUp=" + friendsSignedUp +
                ", cashPaid=" + cashPaid +
                ", myEarnings=" + myEarnings +
                ", referredFriend=" + referredFriend +
                '}';
    }
}
