package com.urbanpoint.UrbanPoint.dataobject.CategoryScreens;

import java.util.ArrayList;

/**
 * Created by Lenovo on 8/11/2017.
 */

public class RModel_MerchintList {

    public class Location {
        private String zomato_rating;

        public String getZomatoRating() { return this.zomato_rating; }

        public void setZomatoRating(String zomato_rating) { this.zomato_rating = zomato_rating; }

        private String latitude;

        public String getLatitude() { return this.latitude; }

        public void setLatitude(String latitude) { this.latitude = latitude; }

        private String longitude;

        public String getLongitude() { return this.longitude; }

        public void setLongitude(String longitude) { this.longitude = longitude; }

        private String distance;

        public String getDistance() { return this.distance; }

        public void setDistance(String distance) { this.distance = distance; }
    }

    public class Product {
        private String productid;

        public String getProductid() { return this.productid; }

        public void setProductid(String productid) { this.productid = productid; }

        private String productname;

        public String getProductname() { return this.productname; }

        public void setProductname(String productname) { this.productname = productname; }

        private String festival;

        public String getFestival() { return this.festival; }

        public void setFestival(String festival) { this.festival = festival; }

        private String gender;

        public String getGender() { return this.gender; }

        public void setGender(String gender) { this.gender = gender; }

        private String productimage;

        public String getProductimage() { return this.productimage; }

        public void setProductimage(String productimage) { this.productimage = productimage; }

        private String thumbnail;

        public String getThumbnail() { return this.thumbnail; }

        public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

        private String status;

        public String getStatus() { return this.status; }

        public void setStatus(String status) { this.status = status; }
    }

    public class Datum {
        private String is_active;

        public String getIsActive() { return this.is_active; }

        public void setIsActive(String is_active) { this.is_active = is_active; }

        private String id;

        public String getId() { return this.id; }

        public void setId(String id) { this.id = id; }

        private int merchant_count;

        public int getMerchantCount() { return this.merchant_count; }

        public void setMerchantCount(int merchant_count) { this.merchant_count = merchant_count; }

        private String merchant_name;

        public String getMerchantName() { return this.merchant_name; }

        public void setMerchantName(String merchant_name) { this.merchant_name = merchant_name; }

        private String address;

        public String getAddress() { return this.address; }

        public void setAddress(String address) { this.address = address; }

        private String rating;

        public String getRating() { return this.rating; }

        public void setRating(String rating) { this.rating = rating; }

        private String merchants_image;

        public String getMerchantsImage() { return this.merchants_image; }

        public void setMerchantsImage(String merchants_image) { this.merchants_image = merchants_image; }

        private String firstname;

        public String getFirstname() { return this.firstname; }

        public void setFirstname(String firstname) { this.firstname = firstname; }

        private String lastname;

        public String getLastname() { return this.lastname; }

        public void setLastname(String lastname) { this.lastname = lastname; }

        private String festival_merchant;

        public String getFestival_merchant() {
            return festival_merchant;
        }

        public void setFestival_merchant(String festival_merchant) {
            this.festival_merchant = festival_merchant;
        }

        private Location location;

        public Location getLocation() { return this.location; }

        public void setLocation(Location location) { this.location = location; }

        private String distance;

        public String getDistance() { return this.distance; }

        public void setDistance(String distance) { this.distance = distance; }

        private ArrayList<Product> products;

        public ArrayList<Product> getProducts() { return this.products; }

        public void setProducts(ArrayList<Product> products) { this.products = products; }
    }


        private String status;

        public String getStatus() { return this.status; }

        public void setStatus(String status) { this.status = status; }

        private String message;

        public String getMessage() { return this.message; }

        public void setMessage(String message) { this.message = message; }

        private ArrayList<Datum> data;

        public ArrayList<Datum> getData() { return this.data; }

        public void setData(ArrayList<Datum> data) { this.data = data; }

        private int wishlistcount;

        public int getWishlistcount() { return this.wishlistcount; }

        public void setWishlistcount(int wishlistcount) { this.wishlistcount = wishlistcount; }

        private int flag;

        public int getFlag() { return this.flag; }

        public void setFlag(int flag) { this.flag = flag; }


}
