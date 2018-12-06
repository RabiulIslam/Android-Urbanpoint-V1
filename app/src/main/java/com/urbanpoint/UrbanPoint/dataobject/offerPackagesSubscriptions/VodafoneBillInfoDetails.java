package com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by riteshpandhurkar on 7/4/16.
 */
public class VodafoneBillInfoDetails {

    @SerializedName("Heading")
    private String headingText;
    @SerializedName("Offers")
    private ArrayList<String> offersListContents;


    public String getHeadingText() {
        return headingText;
    }

    public void setHeadingText(String headingText) {
        this.headingText = headingText;
    }

    public ArrayList<String> getOffersListContents() {
        return offersListContents;
    }

    public void setOffersListContents(ArrayList<String> offersListContents) {
        this.offersListContents = offersListContents;
    }
}
