package com.urbanpoint.UrbanPoint.dataobject.Home;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/16/2017.
 */

public class RModel_HomeOffers {

    public class Recentproduct {
        private String id;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String sku;

        public String getSku() {
            return this.sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        private String festival;

        public String getFestival() {
            return this.festival;
        }

        public void setFestival(String festival) {
            this.festival = festival;
        }

        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String price;

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        private int specialprice;

        public int getSpecialprice() {
            return this.specialprice;
        }

        public void setSpecialprice(int specialprice) {
            this.specialprice = specialprice;
        }

        private String gender;

        public String getGender() {
            return this.gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        private String discount;

        public String getDiscount() {
            return this.discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        private String merchant_id;

        public String getMerchantId() {
            return this.merchant_id;
        }

        public void setMerchantId(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        private String merchantpin;

        public String getMerchantpin() {
            return this.merchantpin;
        }

        public void setMerchantpin(String merchantpin) {
            this.merchantpin = merchantpin;
        }

        private String merchantname;

        public String getMerchantname() {
            return this.merchantname;
        }

        public void setMerchantname(String merchantname) {
            this.merchantname = merchantname;
        }

        private String distance;

        public String getDistance() {
            return this.distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        private String image;

        public String getImage() {
            return this.image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        private String rating;

        public String getRating() {
            return this.rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        private String status;

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class Membersfavourite {
        private String id;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String sku;

        public String getSku() {
            return this.sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        private String festival;

        public String getFestival() {
            return this.festival;
        }

        public void setFestival(String festival) {
            this.festival = festival;
        }

        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String price;

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        private String specialprice;

        public String getSpecialprice() {
            return this.specialprice;
        }

        public void setSpecialprice(String specialprice) {
            this.specialprice = specialprice;
        }

        private String gender;

        public String getGender() {
            return this.gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        private int discount;

        public int getDiscount() {
            return this.discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        private String merchant_id;

        public String getMerchantId() {
            return this.merchant_id;
        }

        public void setMerchantId(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        private String merchantpin;

        public String getMerchantpin() {
            return this.merchantpin;
        }

        public void setMerchantpin(String merchantpin) {
            this.merchantpin = merchantpin;
        }

        private String merchantname;

        public String getMerchantname() {
            return this.merchantname;
        }

        public void setMerchantname(String merchantname) {
            this.merchantname = merchantname;
        }

        private String distance;

        public String getDistance() {
            return this.distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        private String image;

        public String getImage() {
            return this.image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        private String rating;

        public String getRating() {
            return this.rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        private String status;

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class Datum {
        private String id;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String merchantname;

        public String getMerchantname() {
            return this.merchantname;
        }

        public void setMerchantname(String merchantname) {
            this.merchantname = merchantname;
        }

        private String image;

        public String getImage() {
            return this.image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public class Review {
        private String message;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private ArrayList<Datum> data;

        public ArrayList<Datum> getData() {
            return this.data;
        }

        public void setData(ArrayList<Datum> data) {
            this.data = data;
        }
    }

    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private int recentproductcount;

    public int getRecentproductcount() {
        return this.recentproductcount;
    }

    public void setRecentproductcount(int recentproductcount) {
        this.recentproductcount = recentproductcount;
    }

    private ArrayList<Recentproduct> recentproducts;

    public ArrayList<Recentproduct> getRecentproducts() {
        return this.recentproducts;
    }

    public void setRecentproducts(ArrayList<Recentproduct> recentproducts) {
        this.recentproducts = recentproducts;
    }

    private ArrayList<Membersfavourite> membersfavourite;

    public ArrayList<Membersfavourite> getMembersfavourite() {
        return this.membersfavourite;
    }

    public void setMembersfavourite(ArrayList<Membersfavourite> membersfavourite) {
        this.membersfavourite = membersfavourite;
    }

    private Review review;

    public Review getReview() {
        return this.review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    private int wishlistcount;

    public int getWishlistcount() {
        return this.wishlistcount;
    }

    public void setWishlistcount(int wishlistcount) {
        this.wishlistcount = wishlistcount;
    }

}
