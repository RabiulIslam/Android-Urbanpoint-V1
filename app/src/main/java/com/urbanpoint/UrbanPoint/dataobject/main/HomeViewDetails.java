package com.urbanpoint.UrbanPoint.dataobject.main;

import com.google.gson.annotations.SerializedName;

/**
 * Created by riteshpandhurkar on 6/4/16.
 */
public class HomeViewDetails {

    @SerializedName("status")
    private String status;
    @SerializedName("flag")
    private int flag;
    @SerializedName("offer")
    private String offer;
    @SerializedName("countReview")
    private String countReview;
    @SerializedName("vodafoneStatus")
    private String vodafoneStatus;
    @SerializedName("purchaseCount")
    private String purchaseCount;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getCountReview() {
        return countReview;
    }

    public void setCountReview(String countReview) {
        this.countReview = countReview;
    }

    public String getVodafoneStatus() {
        return vodafoneStatus;
    }

    public void setVodafoneStatus(String vodafoneStatus) {
        this.vodafoneStatus = vodafoneStatus;
    }

    public String getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(String purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    @Override
    public String toString() {
        return "HomeViewDetails{" +
                "status='" + status + '\'' +
                ", flag=" + flag +
                ", offer='" + offer + '\'' +
                ", countReview='" + countReview + '\'' +
                ", vodafoneStatus='" + vodafoneStatus + '\'' +
                ", purchaseCount='" + purchaseCount + '\'' +
                '}';
    }
}
