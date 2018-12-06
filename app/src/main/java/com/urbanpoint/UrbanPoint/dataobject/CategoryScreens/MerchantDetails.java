package com.urbanpoint.UrbanPoint.dataobject.CategoryScreens;

import com.google.gson.annotations.SerializedName;

/**
 * Created by riteshpandhurkar on 1/3/16.
 */
public class MerchantDetails {

    @SerializedName("id")
    private String id;

    @SerializedName("merchant_count")
    private String merchantCount;

    @SerializedName("merchant_name")
    private String merchantName;

    @SerializedName("address")
    private String merchantAddress;

    @SerializedName("rating")
    private String rating;
    @SerializedName("merchants_image")
    private String merchantsImageUrl;

    @SerializedName("firstname")
    private String firstname;
    @SerializedName("lastname")
    private String lastname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantCount() {
        return merchantCount;
    }

    public void setMerchantCount(String merchantCount) {
        this.merchantCount = merchantCount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMerchantsImageUrl() {
        return merchantsImageUrl;
    }

    public void setMerchantsImageUrl(String merchantsImageUrl) {
        this.merchantsImageUrl = merchantsImageUrl;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "MerchantDetails{" +
                "id='" + id + '\'' +
                ", merchantCount='" + merchantCount + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", merchantAddress='" + merchantAddress + '\'' +
                ", rating='" + rating + '\'' +
                ", merchantsImageUrl='" + merchantsImageUrl + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
