package com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions;

import com.google.gson.annotations.SerializedName;

/**
 * Created by riteshpandhurkar on 15/3/16.
 */
public class OfferPackagesItemsDetails {

    private int offerPackageID;

    @SerializedName("status")
    private String status;

    @SerializedName("Offer")
    private String offer;

    @SerializedName("Offer_SubTitle")
    private String Offer_SubTitle;


    @SerializedName("frommonth")
    private String fromMonth;

    @SerializedName("tomonth")
    private String toMonth;

    @SerializedName("Qar value")
    private String QarValue;

    @SerializedName("dollar value")
    private String dollarValue;

    @SerializedName("text")
    private String text;
    @SerializedName("approximateSavings")
    private String approximateSavings;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getOffer_SubTitle() {
        return Offer_SubTitle;
    }

    public void setOffer_SubTitle(String offer_SubTitle) {
        Offer_SubTitle = offer_SubTitle;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    public String getToMonth() {
        return toMonth;
    }

    public void setToMonth(String toMonth) {
        this.toMonth = toMonth;
    }

    public String getQarValue() {
        return QarValue;
    }

    public void setQarValue(String qarValue) {
        QarValue = qarValue;
    }

    public String getDollarValue() {
        return dollarValue;
    }

    public void setDollarValue(String dollarValue) {
        this.dollarValue = dollarValue;
    }

    public int getOfferPackageID() {
        return offerPackageID;
    }

    public void setOfferPackageID(int offerPackageID) {
        this.offerPackageID = offerPackageID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getApproximateSavings() {
        return approximateSavings;
    }

    public void setApproximateSavings(String approximateSavings) {
        this.approximateSavings = approximateSavings;
    }

    @Override
    public String toString() {
        return "OfferPackagesItemsDetails{" +
                "offerPackageID=" + offerPackageID +
                ", status='" + status + '\'' +
                ", offer='" + offer + '\'' +
                ", fromMonth='" + fromMonth + '\'' +
                ", toMonth='" + toMonth + '\'' +
                ", QarValue='" + QarValue + '\'' +
                ", dollarValue='" + dollarValue + '\'' +
                ", text='" + text + '\'' +
                ", approximateSavings='" + approximateSavings + '\'' +
                '}';
    }


}
