package com.urbanpoint.UrbanPoint.dataobject.CategoryScreens;

import android.content.Context;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by riteshpandhurkar on 1/3/16.
 */
public class Offer {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<OffersDetails> offersDetails;

    @SerializedName("wishlistcount")
    private int wishListCount;

    @SerializedName("flag")
    private String lockOffersStatusFlag;

    @SerializedName("vodafoneCustomerStatus")
    private String vodafoneCustomerLockStatus;

    public int getWishListCount() {
        return wishListCount;
    }

    public void setWishListCount(int wishListCount) {
        this.wishListCount = wishListCount;
    }

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

    public ArrayList<OffersDetails> getOffersDetails() {
        if (offersDetails != null) {
            Collections.sort(offersDetails, new DistanceBasedComparator());
        }
        return offersDetails;
    }

    public void setOffersDetails(ArrayList<OffersDetails> offersDetails) {
        this.offersDetails = offersDetails;
    }

    public String getVodafoneCustomerLockStatus() {
        return vodafoneCustomerLockStatus;
    }

    public void setVodafoneCustomerLockStatus(String vodafoneCustomerLockStatus) {
        this.vodafoneCustomerLockStatus = vodafoneCustomerLockStatus;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", offersDetails=" + offersDetails +
                '}';
    }


    public ArrayList<OffersDetails> getGenderSpecificDetails(String gender) {

        ArrayList<OffersDetails> tempListData = null;
        // FOR ALL USERS DATA.
        if (gender == null) {
            tempListData = this.offersDetails;
        } else {
            ArrayList<OffersDetails> activeOffers = getActiveOffers();
            if (activeOffers != null) {
                if (activeOffers.size() > 0) {
                    tempListData = new ArrayList<OffersDetails>();
                    for (OffersDetails tempData :
                            activeOffers) {
                        if (tempData.getGender() == null || Constants.DEFAULT_VALUES.THREE.equalsIgnoreCase(tempData.getGender())) {
                            tempListData.add(tempData);
                        } else if (tempData.getGender().equalsIgnoreCase("" + gender)) {
                            tempListData.add(tempData);
                        }
                    }
                }
            }
        }
        return tempListData;
    }

    public class DistanceBasedComparator implements Comparator<OffersDetails> {
        @Override
        public int compare(OffersDetails o1, OffersDetails o2) {
            //     MyApplication.getInstance().printLogs("DistanceBasedComparator", "DistanceBasedComparator " + o1 + "|" + o2);
            if (o1.getDistance() == o2.getDistance()) {
                return 0;
            }
            if (o1.getDistance() > o2.getDistance()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public ArrayList<OffersDetails> getActiveOffers() {

        ArrayList<OffersDetails> tempListData = null;

        if (this.offersDetails != null) {
            if (this.offersDetails.size() > 0) {
                tempListData = new ArrayList<OffersDetails>();
                for (OffersDetails tempData :
                        offersDetails) {
                    if (tempData.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ACTIVE)) {
                        tempListData.add(tempData);
                    }
                }
            }
        }
        return tempListData;
    }

    public String getLockOffersStatusFlag() {
        return lockOffersStatusFlag;
    }

    public void setLockOffersStatusFlag(String lockOffersStatusFlag) {
        this.lockOffersStatusFlag = lockOffersStatusFlag;
    }

    public String isOfferGetsLocked(Context context) {

        String isVodafoneCustomer = AppPreference.getSetting(context, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "");
        String status = Constants.DEFAULT_VALUES.ONE;

        // CHECK IS VODA USER OR NOT
        if (isVodafoneCustomer.equalsIgnoreCase("" + Constants.Operator.VODAFONE)) {
            // -- IF vodafoneCustomerLockStatus = null/0 then lock offer (return 1) else (return 0)
            Log.d("SUBSCRB", "isOfferGetsLocked: "+vodafoneCustomerLockStatus);
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
            status = getLockOffersStatusFlag();
        }

        return status;
    }
 
    public ArrayList<OffersDetails> getFilterFestivalOffers(ArrayList<OffersDetails> genderBasedFilterArray) {

        ArrayList<OffersDetails> tempListData = null;

        tempListData = new ArrayList<OffersDetails>();
        for (OffersDetails tempData :
                genderBasedFilterArray) {
            if (tempData.getFestival() != null) {
                tempListData.add(tempData);
            }
        }
        return tempListData;
    }
}
