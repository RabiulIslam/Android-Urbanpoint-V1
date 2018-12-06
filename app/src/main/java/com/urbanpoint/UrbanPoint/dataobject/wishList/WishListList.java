package com.urbanpoint.UrbanPoint.dataobject.wishList;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;

import java.util.ArrayList;

/**
 * Created by aparnasalve on 14/3/16.
 */
public class WishListList {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("flag")
    private String flag;


    @SerializedName("data")
    private ArrayList<WishListItem> wishListItemArrayList;

    @SerializedName("vodafoneCustomerStatus")
    private String vodafoneCustomerLockStatus;


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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ArrayList<WishListItem> getWishListItemArrayList() {
        return wishListItemArrayList;
    }

    public void setWishListItemArrayList(ArrayList<WishListItem> wishListItemArrayList) {
        this.wishListItemArrayList = wishListItemArrayList;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", wishListItemArrayList=" + wishListItemArrayList +
                ", flag='" + flag + '\'' +
                '}';
    }


    public String getVodafoneCustomerLockStatus() {
        return vodafoneCustomerLockStatus;
    }

    public void setVodafoneCustomerLockStatus(String vodafoneCustomerLockStatus) {
        this.vodafoneCustomerLockStatus = vodafoneCustomerLockStatus;
    }


    public String isOfferGetsLocked(Context context) {

        String isVodafoneCustomer = AppPreference.getSetting(context, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "");
        String isOoredooeCustomer = AppPreference.getSetting(context, Constants.DEFAULT_VALUES.OPERATOR_TYPE_OOREDOO, "");
        String status = Constants.DEFAULT_VALUES.ONE;

        // CHECK IS VODA USER OR NOT
        if (isVodafoneCustomer.equalsIgnoreCase("" + Constants.Operator.VODAFONE) ||
                isOoredooeCustomer.equalsIgnoreCase("" + Constants.Operator.OOREDOO)) {
            // -- IF vodafoneCustomerLockStatus = null/0 then lock offer (return 1) else (return 0)
            if (vodafoneCustomerLockStatus == null || Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(vodafoneCustomerLockStatus)
                    || Constants.DEFAULT_VALUES.TWO.equalsIgnoreCase(vodafoneCustomerLockStatus)
                    || Constants.DEFAULT_VALUES.THREE.equalsIgnoreCase(vodafoneCustomerLockStatus)) {
                //IF PAYMENT IN-PROGRESS : (vodafoneCustomerLockStatus =2)
                status = Constants.DEFAULT_VALUES.ONE;
            } else {
                if (!AppInstance.isUserSubscribeStatusServiceCalled) {
                    new Utility(context).generateEvent(context, Constants.DEFAULT_VALUES.UPDATE_USER_SUBSCRIBE_STATUS, null, null);
                }
                status = Constants.DEFAULT_VALUES.ZERO;
            }

        } else {
            // -- FOR NON VODA USER IF lockOffersStatusFlag = 1 then lock offer
            status = getFlag();
        }

        return status;
    }

}
