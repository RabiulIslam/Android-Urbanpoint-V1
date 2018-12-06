package com.urbanpoint.UrbanPoint.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ritesh
 * The class handles all the objects associated with the MyEarnings entity
 */
public class PurchaseHistoryDetails {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String message;

    @SerializedName("purchase_price")
    private String purchasePrice;

    @SerializedName("org_price")
    private String originalPrice;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("merchantaddress")
    private String merchantAddress;

    @SerializedName("merchantname")
    private String merchantName;

    @SerializedName("date")
    private String date;

    @SerializedName("approximateSavings")
    private String approximateSavings;

    private String formattedDateString;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormattedDateString() {
        return formattedDateString;
    }

    public void setFormattedDateString(String formattedDateString) {
        this.formattedDateString = formattedDateString;
    }


    public String getApproximateSavings() {
        return approximateSavings;
    }

    public void setApproximateSavings(String approximateSavings) {
        this.approximateSavings = approximateSavings;
    }

    @Override
    public String toString() {
        return "PurchaseHistoryDetails{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", purchasePrice='" + purchasePrice + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", merchantAddress='" + merchantAddress + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", date='" + date + '\'' +
                ", approximateSavings='" + approximateSavings + '\'' +
                ", formattedDateString='" + formattedDateString + '\'' +
                '}';
    }
}
