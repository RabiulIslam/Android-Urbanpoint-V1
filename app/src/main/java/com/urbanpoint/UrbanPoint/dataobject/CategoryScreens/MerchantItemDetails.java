package com.urbanpoint.UrbanPoint.dataobject.CategoryScreens;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aparnasalve on 8/3/16.
 */
public class MerchantItemDetails {
    @SerializedName("productid")
    private String productid;

    @SerializedName("productname")
    private String productname;

    @SerializedName("price")
    private String price;

    @SerializedName("specialprice")
    private String specialprice;


    @SerializedName("productimage")
    private String productimage;

    @SerializedName("rating")
    private String rating;

    @SerializedName("zomato_rating")
    private String zomato_rating;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("number")
    private String number;

    @SerializedName("merchantsprofile_image")
    private String merchantsprofile_image;

    @SerializedName("merchantsbaseimage")
    private String merchantsbaseimage;

    @SerializedName("Discount")
    private String Discount;

    @SerializedName("status")
    private String status;

    @SerializedName("merchantdescription")
    private String merchantdescription;

    @SerializedName("merchantaddress")
    private String merchantaddress;

    @SerializedName("merchantname")
    private String merchantname;

    @SerializedName("timings")
    private String timings;

    @SerializedName("festival")
    private String festival;

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

    public String getZomato_rating() {
        return zomato_rating;
    }

    public void setZomato_rating(String zomato_rating) {
        this.zomato_rating = zomato_rating;
    }

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

    public String getSpecialprice() {
        return specialprice;
    }

    public void setSpecialprice(String specialprice) {
        this.specialprice = specialprice;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMerchantsprofile_image() {
        return merchantsprofile_image;
    }

    public void setMerchantsprofile_image(String merchantsprofile_image) {
        this.merchantsprofile_image = merchantsprofile_image;
    }

    public String getMerchantsbaseimage() {
        return merchantsbaseimage;
    }

    public void setMerchantsbaseimage(String merchantsbaseimage) {
        this.merchantsbaseimage = merchantsbaseimage;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerchantdescription() {
        return merchantdescription;
    }

    public void setMerchantdescription(String merchantdescription) {
        this.merchantdescription = merchantdescription;
    }

    public String getMerchantaddress() {
        return merchantaddress;
    }

    public void setMerchantaddress(String merchantaddress) {
        this.merchantaddress = merchantaddress;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }


    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    @Override
    public String toString() {
        return "MerchantItemDetails{" +
                "productid='" + productid + '\'' +
                ", productname='" + productname + '\'' +
                ", price='" + price + '\'' +
                ", specialprice='" + specialprice + '\'' +
                ", productimage='" + productimage + '\'' +
                ", rating='" + rating + '\'' +
                ", number='" + number + '\'' +
                ", merchantsprofile_image='" + merchantsprofile_image + '\'' +
                ", merchantsbaseimage='" + merchantsbaseimage + '\'' +
                ", Discount='" + Discount + '\'' +
                ", status='" + status + '\'' +
                ", merchantdescription='" + merchantdescription + '\'' +
                ", merchantaddress='" + merchantaddress + '\'' +
                ", merchantname='" + merchantname + '\'' +
                ", timings='" + timings + '\'' +
                '}';
    }
}