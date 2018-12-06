package com.urbanpoint.UrbanPoint.dataobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ritesh
 * The class handles all the objects associated with the InputUser entity
 */
public class LoggedInUser {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<UserInfo> userInfo;

    @SerializedName("offer")
    private String offer;

    @SerializedName("gender")
    private String gender;

    @SerializedName("nationality")
    public String nationality;

    @SerializedName("isvodacustomer")
    private String isVodafoneCustomer;

    @SerializedName("isooredoocustomer")
    private String isOoredooCustomer;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public ArrayList<UserInfo> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(ArrayList<UserInfo> userInfo) {
        this.userInfo = userInfo;
    }

    public String getIsVodafoneCustomer() {
        return isVodafoneCustomer;
    }

    public void setIsVodafoneCustomer(String isVodafoneCustomer) {
        this.isVodafoneCustomer = isVodafoneCustomer;
    }

    public String getIsOoredooCustomer() {
        return isOoredooCustomer;
    }

    public void setIsOoredooCustomer(String isOoredooCustomer) {
        this.isOoredooCustomer = isOoredooCustomer;
    }
}
