package com.urbanpoint.UrbanPoint.dataobject.profile;

/**
 * Created by Ibrar on 7/25/2017.
 */

public class ProfileInfo {
    public String name;
    private String email;
    private String isVodafoneCustomer;
    private String isOoredooCustomer;
    private String gender;
    private String Nationality;

    public ProfileInfo(String name, String email, String ooredoo,String vodafone ,String gender, String nationality) {
        this.name = name;
        this.email = email;
        this.isOoredooCustomer = ooredoo;
        this.isVodafoneCustomer = vodafone;
        this.gender = gender;
        Nationality = nationality;
    }
    public ProfileInfo() {
        this.name = "";
        this.email = "";
        this.isOoredooCustomer = "";
        this.isVodafoneCustomer = "";
        this.gender = "";
        this.Nationality = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }
}
