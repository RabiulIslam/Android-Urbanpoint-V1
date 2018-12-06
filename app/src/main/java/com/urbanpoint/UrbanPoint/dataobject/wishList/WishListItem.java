package com.urbanpoint.UrbanPoint.dataobject.wishList;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by aparnasalve on 14/3/16.
 */
public class WishListItem {
    @SerializedName("categoryid")
    private String categoryID;

    @SerializedName("productid")
    private String productid;

    @SerializedName("productname")
    private String productname;

    @SerializedName("price")
    private String price;

    @SerializedName("merchantid")
    private String merchantid;

    @SerializedName("merchantname")
    private String merchantname;

    @SerializedName("merchantaddress")
    private String merchantaddress;

    @SerializedName("specialprice")
    private String specialprice;

    @SerializedName("Discount")
    private String Discount;

    @SerializedName("image")
    private String image;

    @SerializedName("product_count")
    private String product_count;

    @SerializedName("Expired")
    private String Expired;

    @SerializedName("prep")
    private String prep;

    @SerializedName("distance")
    private String distance;

    @SerializedName("festival")
    private String festival;

    @SerializedName("isEnabled")
    private String isEnabled;

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public String getMerchantaddress() {
        return merchantaddress;
    }

    public void setMerchantaddress(String merchantaddress) {
        this.merchantaddress = merchantaddress;
    }

    public String getSpecialprice() {
        return specialprice;
    }

    public void setSpecialprice(String specialprice) {
        this.specialprice = specialprice;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        this.Discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }

    public String getExpired() {
        return Expired;
    }

    public void setExpired(String expired) {
        this.Expired = expired;
    }

    public String getPrep() {
        return prep;
    }

    public void setPrep(String prep) {
        this.prep = prep;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public String toString() {
        return "WishListItem{" +
                "categoryID='" + categoryID + '\'' +
                ", productid='" + productid + '\'' +
                ", productname='" + productname + '\'' +
                ", price='" + price + '\'' +
                ", merchantid='" + merchantid + '\'' +
                ", merchantname='" + merchantname + '\'' +
                ", merchantaddress='" + merchantaddress + '\'' +
                ", specialprice='" + specialprice + '\'' +
                ", Discount='" + Discount + '\'' +
                ", image='" + image + '\'' +
                ", product_count='" + product_count + '\'' +
                ", Expired='" + Expired + '\'' +
                ", prep='" + prep + '\'' +
                ", distance='" + distance + '\'' +
                ", festival='" + festival + '\'' +
                ", isEnabled='" + isEnabled + '\'' +
                '}';
    }
}
