package com.urbanpoint.UrbanPoint.dataobject.SearchFilter;

/**
 * Created by Lenovo on 11/28/2017.
 */

public class FilteredSearchItem {

    public String id;
    public String sku;
    public String merchantaddress;
    public String name;
    public String category;
    public String price;
    public String specialprice;
    public String discount;
    public String merchant_id;
    public String image;
    public String merchantname;
    public String second_name;
    public String merchant_name;
    public String rating;
    public String products_count;
    public String status;
    public String festival;


    public FilteredSearchItem(String id, String sku, String merchantaddress, String name, String category, String price, String specialprice, String discount, String merchant_id, String image, String merchantname, String second_name, String merchant_name, String rating, String products_count, String status, String festival) {
        this.id = id;
        this.sku = sku;
        this.merchantaddress = merchantaddress;
        this.name = name;
        this.category = category;
        this.price = price;
        this.specialprice = specialprice;
        this.discount = discount;
        this.merchant_id = merchant_id;
        this.image = image;
        this.merchantname = merchantname;
        this.second_name = second_name;
        this.merchant_name = merchant_name;
        this.rating = rating;
        this.products_count = products_count;
        this.status = status;
        this.festival = festival;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
