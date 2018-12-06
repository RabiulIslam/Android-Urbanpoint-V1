package com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by riteshpandhurkar on 15/3/16.
 */
public class OfferPackagesItems {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Map<String, OfferPackagesItemsDetails> offerPackagesList;

    @SerializedName("Limited_offer_text")
    private String limitedOfferText;


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

    public Map<String, OfferPackagesItemsDetails> getOfferPackagesList() {
        return offerPackagesList;
    }

    public void setOfferPackagesList(Map<String, OfferPackagesItemsDetails> offerPackagesList) {
        this.offerPackagesList = offerPackagesList;
    }

    public ArrayList<OfferPackagesItemsDetails> getAllPackages() {
        ArrayList<OfferPackagesItemsDetails> list = null;
        if (offerPackagesList != null) {
            list = new ArrayList<OfferPackagesItemsDetails>(offerPackagesList.values());
        }

        return list;
    }

    public String getLimitedOfferText() {
        return limitedOfferText;
    }

    public void setLimitedOfferText(String limitedOfferText) {
        this.limitedOfferText = limitedOfferText;
    }
}
