package com.urbanpoint.UrbanPoint.dataobject.CategoryScreens;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aparnasalve on 3/3/16.
 */
public class FoodOfferDeatils {

    @SerializedName("id")
    private String productId;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String itemName;

    @SerializedName("description")
    private String description;

    @SerializedName("fineprint")
    private String fineprint;

    @SerializedName("Expiredate")
    private String Expiredate;

    @SerializedName("merchantid")
    private String merchantId;

    @SerializedName("merchantname")
    private String merchantName;

    @SerializedName("merchantaddress")
    private String merchantAddress;

    @SerializedName("merchantimage")
    private String merchantimage;

    @SerializedName("timings")
    private String timings;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private  String longitude;

    @SerializedName("phone")
    private String phone;

    @SerializedName("price")
    private String price;

    @SerializedName("image")
    private String imageURL;

    @SerializedName("specialprice")
    private String specialPrice;

    @SerializedName("discount")
    private String discount;

    @SerializedName("status")
    private String status;

    @SerializedName("wishlist_item")
    private String wishlist_item;

    @SerializedName("wishlistcount")
    private String wishlistcount;

    @SerializedName("approximateSavings")
    private String approximateSavings;




    public String getProductId() {
        return productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFineprint() {
        return fineprint;
    }

    public void setFineprint(String fineprint) {
        this.fineprint = fineprint;
    }

    public String getExpiredate() {
        return Expiredate;
    }

    public void setExpiredate(String expiredate) {
        Expiredate = expiredate;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getMerchantimage() {
        return merchantimage;
    }

    public void setMerchantimage(String merchantimage) {
        this.merchantimage = merchantimage;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getWishlist_item() {
        return wishlist_item;
    }

    public void setWishlist_item(String wishlist_item) {
        this.wishlist_item = wishlist_item;
    }

    public String getWishlistcount() {
        return wishlistcount;
    }

    public void setWishlistcount(String wishlistcount) {
        this.wishlistcount = wishlistcount;
    }

    public String getApproximateSavings() {
        return approximateSavings;
    }

    public void setApproximateSavings(String approximateSavings) {
        this.approximateSavings = approximateSavings;
    }
}
