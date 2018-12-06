package com.urbanpoint.UrbanPoint.dataobject.CategoryScreens;

import com.google.gson.annotations.SerializedName;

/**
 * Created by riteshpandhurkar on 1/3/16.
 */
public class OffersDetails {

    @SerializedName("id")
    private String offerId;

    @SerializedName("sku")
    private String offerSKU;

    @SerializedName("merchantaddress")
    private String merchantAddress;

    @SerializedName("merchant_latlong")
    private String merchantLocation;

    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;

    @SerializedName("specialprice")
    private String specialPrice;
    @SerializedName("gender")
    private String gender;
    @SerializedName("discount")
    private String discount;

    @SerializedName("merchant_id")
    private String merchantId;

    @SerializedName("image")
    private String imageURL;

    @SerializedName("merchantname")
    private String merchantName;

    @SerializedName("second_name")
    private String secondName;

    @SerializedName("merchant_name")
    private String merchant_Name;

    @SerializedName("rating")
    private String rating;

    @SerializedName("status")
    private String status;

    @SerializedName("distance")
    private double distance;

    @SerializedName("festival")
    private String festival;


    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferSKU() {
        return offerSKU;
    }

    public void setOfferSKU(String offerSKU) {
        this.offerSKU = offerSKU;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantLocation() {
        return merchantLocation;
    }

    public void setMerchantLocation(String merchantLocation) {
        this.merchantLocation = merchantLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getMerchant_Name() {
        return merchant_Name;
    }

    public void setMerchant_Name(String merchant_Name) {
        this.merchant_Name = merchant_Name;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    @Override
    public String toString() {
        return "OffersDetails{" +
                "offerId='" + offerId + '\'' +
                ", offerSKU='" + offerSKU + '\'' +
                ", merchantAddress='" + merchantAddress + '\'' +
                ", merchantLocation='" + merchantLocation + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", specialPrice='" + specialPrice + '\'' +
                ", gender='" + gender + '\'' +
                ", discount='" + discount + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", merchant_Name='" + merchant_Name + '\'' +
                ", rating='" + rating + '\'' +
                ", status='" + status + '\'' +
                ", distance=" + distance +
                ", festival=" + festival +
                '}';
    }
}
