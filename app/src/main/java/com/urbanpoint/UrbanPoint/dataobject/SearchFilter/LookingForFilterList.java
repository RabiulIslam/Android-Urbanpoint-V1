package com.urbanpoint.UrbanPoint.dataobject.SearchFilter;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;

import java.util.ArrayList;

/**
 * Created by aparnasalve on 9/3/16.
 */
public class LookingForFilterList {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("flag")
    private String flag;

    @SerializedName("data")
    private ArrayList<LookingForFilterListItem> lookingForFilterListItems;

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

    public ArrayList<LookingForFilterListItem> getLookingForFilterListItems() {
        return lookingForFilterListItems;
    }

    public void setLookingForFilterListItems(ArrayList<LookingForFilterListItem> lookingForFilterListItems) {
        this.lookingForFilterListItems = lookingForFilterListItems;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public ArrayList<LookingForFilterListItem> getActiveOffers() {

        ArrayList<LookingForFilterListItem> tempListData = null;

        if (this.lookingForFilterListItems != null) {
            if (this.lookingForFilterListItems.size() > 0) {
                tempListData = new ArrayList<LookingForFilterListItem>();
                for (LookingForFilterListItem tempData :
                        lookingForFilterListItems) {
                    if (tempData.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ACTIVE)) {
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


    /**
     * THIS METHOD IS EXACT COPY OF Offer(class).isOfferGetsLocked
     *
     * @param context
     * @return
     */
    public String isOfferGetsLocked(Context context) {

        String isVodafoneCustomer = AppPreference.getSetting(context, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "");
        String isOoredooCustomer = AppPreference.getSetting(context, Constants.DEFAULT_VALUES.OPERATOR_TYPE_OOREDOO, "");
        String status = Constants.DEFAULT_VALUES.ONE;

        // CHECK IS VODA USER OR NOT
        if (isVodafoneCustomer.equalsIgnoreCase("" + Constants.Operator.VODAFONE)||
                isOoredooCustomer.equalsIgnoreCase("" + Constants.Operator.OOREDOO)) {
            // -- IF vodafoneCustomerLockStatus = null/0 then lock offer (return 1) else (return 0)
            if (vodafoneCustomerLockStatus == null || Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(vodafoneCustomerLockStatus)
                    || Constants.DEFAULT_VALUES.TWO.equalsIgnoreCase(vodafoneCustomerLockStatus)
                    || Constants.DEFAULT_VALUES.THREE.equalsIgnoreCase(vodafoneCustomerLockStatus)) {
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

    @Override
    public String toString() {
        return "LookingForFilterList{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", flag='" + flag + '\'' +
                ", lookingForFilterListItems=" + lookingForFilterListItems +
                ", vodafoneCustomerLockStatus='" + vodafoneCustomerLockStatus + '\'' +
                '}';
    }
}
