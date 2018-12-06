package com.urbanpoint.UrbanPoint.dataobject.CategoryScreens;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;

import java.util.ArrayList;

/**
 * Created by aparnasalve on 8/3/16.
 */
public class MerchantDetailsList {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("wishlistcount")
    private String wishlistcount;

    @SerializedName("flag")
    private String flag;

    @SerializedName("vodafoneCustomerStatus")
    private String vodafoneCustomerLockStatus;


    @SerializedName("data")
    private ArrayList<MerchantItemDetails> merchantItemDetails;


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

    public String getWishlistcount() {
        return wishlistcount;
    }

    public void setWishlistcount(String wishlistcount) {
        this.wishlistcount = wishlistcount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ArrayList<MerchantItemDetails> getMerchantItemDetails() {
        return merchantItemDetails;
    }

    public void setMerchantItemDetails(ArrayList<MerchantItemDetails> merchantItemDetails) {
        this.merchantItemDetails = merchantItemDetails;
    }

    public ArrayList<MerchantItemDetails> getProductListToShow() {
        ArrayList<MerchantItemDetails> tempListData = null;
        // FOR ALL USERS DATA.
        if (this.merchantItemDetails != null) {
            if (this.merchantItemDetails.size() > 0) {
                tempListData = new ArrayList<MerchantItemDetails>();
                for (MerchantItemDetails tempData :
                        merchantItemDetails) {
                    if (tempData.getProductname() != null) {
                        tempListData.add(tempData);
                    }
                }
            }
        }
        return tempListData;
    }

    public String getVodafoneCustomerLockStatus() {
        return vodafoneCustomerLockStatus;
    }

    public void setVodafoneCustomerLockStatus(String vodafoneCustomerLockStatus) {
        this.vodafoneCustomerLockStatus = vodafoneCustomerLockStatus;
    }

    @Override
    public String toString() {
        return "MerchantList{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", merchantItemDetails=" + merchantItemDetails +
                ", flag='" + flag + '\'' +
                ", wishlistcount=" + wishlistcount +
                '}';
    }

    public String isOfferGetsLocked(Context context) {


        if(AppPreference.getSettingResturnsBoolean(context, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false)){
            status = Constants.DEFAULT_VALUES.ZERO;
        }else {
            status = Constants.DEFAULT_VALUES.ONE;
        }
        return status;
    }

}
