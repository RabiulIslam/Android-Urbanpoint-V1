package com.urbanpoint.UrbanPoint.dataobject.main;

/**
 * Created by Lenovo on 8/8/2017.
 */

public class DModelHomeGrdVw {
    String strImgUrl;
    String strOfferName;
    String strProductId;
    String strMerchantName;
    String strFestival;
    int strDistance;


    public DModelHomeGrdVw(String strImgUrl, String strOfferName, String strProductId, String strMerchantName, String strFestival, int strDistance) {
        this.strImgUrl = strImgUrl;
        this.strOfferName = strOfferName;
        this.strProductId = strProductId;
        this.strMerchantName = strMerchantName;
        this.strFestival = strFestival;
        this.strDistance = strDistance;
    }

    public DModelHomeGrdVw() {
        this.strImgUrl = "";
        this.strOfferName = "";
        this.strProductId = "";
        this.strMerchantName = "";
        this.strFestival = "";
        this.strDistance = 0;
    }

    public String getStrImgUrl() {
        return strImgUrl;
    }

    public void setStrImgUrl(String strImgUrl) {
        this.strImgUrl = strImgUrl;
    }

    public String getStrOfferName() {
        return strOfferName;
    }

    public void setStrOfferName(String strOfferName) {
        this.strOfferName = strOfferName;
    }

    public String getStrProductId() {
        return strProductId;
    }

    public void setStrProductId(String strProductId) {
        this.strProductId = strProductId;
    }

    public String getStrMerchantName() {
        return strMerchantName;
    }

    public void setStrMerchantName(String strMerchantName) {
        this.strMerchantName = strMerchantName;
    }

    public String getStrFestival() {
        return strFestival;
    }

    public void setStrFestival(String strFestival) {
        this.strFestival = strFestival;
    }

    public int getStrDistance() {
        return strDistance;
    }

    public void setStrDistance(int strDistance) {
        this.strDistance = strDistance;
    }
}
