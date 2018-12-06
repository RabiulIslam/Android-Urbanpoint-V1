package com.urbanpoint.UrbanPoint.dataobject.SearchFilter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aparnasalve on 9/3/16.
 */
public class LookingForFilterListItem {
    @SerializedName("id")
    private String id;

    @SerializedName("sku")
    private String sku;

    @SerializedName("merchantaddress")
    private String merchantaddress;

    @SerializedName("name")
    private String productname;

    @SerializedName("category")
    private String category;

    @SerializedName("price")
    private String price;

    @SerializedName("specialprice")
    private String specialprice;

    @SerializedName("discount")
    private String discount;

    @SerializedName("merchant_id")
    private String merchant_id;

    @SerializedName("image")
    private String image;

    @SerializedName("merchantname")
    private String merchantname;

    @SerializedName("second_name")
    private String second_name;

    @SerializedName("merchant_name")
    private String merchant_name;

    @SerializedName("rating")
    private String rating;

    @SerializedName("products_count")
    private String products_count;

    @SerializedName("status")
    private String status;

    @SerializedName("festival")
    private String festival;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getMerchantaddress() {
        return merchantaddress;
    }

    public void setMerchantaddress(String merchantaddress) {
        this.merchantaddress = merchantaddress;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProducts_count() {
        return products_count;
    }

    public void setProducts_count(String products_count) {
        this.products_count = products_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    @Override
    public String toString() {
        return "LookingForFilterListItem{" +
                "id='" + id + '\'' +
                ", sku='" + sku + '\'' +
                ", merchantaddress='" + merchantaddress + '\'' +
                ", productname='" + productname + '\'' +
                ", category='" + category + '\'' +
                ", price='" + price + '\'' +
                ", specialprice='" + specialprice + '\'' +
                ", discount='" + discount + '\'' +
                ", merchant_id='" + merchant_id + '\'' +
                ", image='" + image + '\'' +
                ", merchantname='" + merchantname + '\'' +
                ", second_name='" + second_name + '\'' +
                ", merchant_name='" + merchant_name + '\'' +
                ", rating='" + rating + '\'' +
                ", products_count='" + products_count + '\'' +
                ", status='" + status + '\'' +
                ", festival='" + festival + '\'' +
                '}';
    }
}
