package com.urbanpoint.UrbanPoint.dataobject.CategoryScreens;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ibrar on 1/2/2017.
 */
public class DModelMerchintList {

    String id;
    int merchantCount;
    String merchantName;
    String merchantAddress;
    String merchantDistance;
    String rating;
    String festival;
    String merchantsImageUrl;
    String firstname;
    String lastname;
    String isActive;
    List<Child> child;


    public DModelMerchintList(String id, int merchantCount, String merchantName,
                              String merchantAddress, String merchantDistance, String rating,
                              String merchantsImageUrl, String firstname, String lastname,
                              String isActive) {
        this.id = id;
        this.merchantCount = merchantCount;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.merchantDistance = merchantDistance;
        this.rating = rating;
        this.merchantsImageUrl = merchantsImageUrl;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActive = isActive;
        this.child = new ArrayList<>();
    }
    public DModelMerchintList(String id, int merchantCount, String merchantName,
                              String merchantAddress, String merchantDistance, String rating,
                              String merchantsImageUrl, String firstname, String lastname,
                              String isActive, String festival, List<Child> child ) {
        this.id = id;
        this.merchantCount = merchantCount;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.merchantDistance = merchantDistance;
        this.rating = rating;
        this.merchantsImageUrl = merchantsImageUrl;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActive = isActive;
        this.festival = festival;
        this.child = child;
    }

    public static class Child {
        String ProductId;
        String ImgUrl;
        String Name;
        String Festival;
        String Status;
        String Gender;
        String Thumbnail;

        public Child(String productId, String imgUrl, String name, String festival, String status, String gender, String thumbnail) {
            ProductId = productId;
            ImgUrl = imgUrl;
            Name = name;
            Festival = festival;
            Status = status;
            Gender = gender;
            Thumbnail = thumbnail;
        }
        public Child() {
            ProductId = "";
            ImgUrl = "";
            Name = "";
            Festival = "";
            Status = "";
            Gender = "";
            Thumbnail = "";
        }

        public String getProductId() {
            return ProductId;
        }

        public void setProductId(String productId) {
            ProductId = productId;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String imgUrl) {
            ImgUrl = imgUrl;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getFestival() {
            return Festival;
        }

        public void setFestival(String festival) {
            Festival = festival;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getThumbnail() {
            return Thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            Thumbnail = thumbnail;
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMerchantCount() {
        return merchantCount;
    }

    public void setMerchantCount(int merchantCount) {
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

    public String getMerchantDistance() {
        return merchantDistance;
    }

    public void setMerchantDistance(String merchantDistance) {
        this.merchantDistance = merchantDistance;
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

    public String isActive() {
        return isActive;
    }

    public void setActive(String active) {
        isActive = active;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public List<Child> getChild() {
        return child;
    }

    public void setChild(List<Child> child) {
        this.child = child;
    }
}
