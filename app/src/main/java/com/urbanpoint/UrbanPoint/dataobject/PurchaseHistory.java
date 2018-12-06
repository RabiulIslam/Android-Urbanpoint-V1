package com.urbanpoint.UrbanPoint.dataobject;

import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Ritesh
 * The class handles all the objects associated with the Purchase History entity
 */
public class PurchaseHistory {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<PurchaseHistoryDetails> purchaseHistoryDetails;

    private HashSet<String> groupWiseDate;

    public HashSet<String> getGroupWiseDate() {
        return groupWiseDate;
    }

    public void setGroupWiseDate(HashSet<String> groupWiseDate) {
        this.groupWiseDate = groupWiseDate;
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

    public ArrayList<PurchaseHistoryDetails> getPurchaseHistoryDetails() {
        return purchaseHistoryDetails;
    }

    public void setPurchaseHistoryDetails(ArrayList<PurchaseHistoryDetails> purchaseHistoryDetails) {
        this.purchaseHistoryDetails = purchaseHistoryDetails;
    }

    public ArrayList<PurchaseHistoryDetails> getSortedPurchaseHistoryDetails() {
        ArrayList<PurchaseHistoryDetails> temp = new ArrayList<PurchaseHistoryDetails>();
        if (purchaseHistoryDetails != null) {
            temp.addAll(purchaseHistoryDetails);
        }
        Collections.sort(temp, new DateWiseComparator());
        return temp;
    }

    @Override
    public String toString() {
        return "PurchaseHistory{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", purchaseHistoryDetails=" + purchaseHistoryDetails +
                ", groupWiseDate=" + groupWiseDate +
                '}';
    }

    private class DateWiseComparator implements Comparator<PurchaseHistoryDetails> {
        public int compare(PurchaseHistoryDetails m1, PurchaseHistoryDetails m2) {
            //possibly check for nulls to avoid NullPointerException
            Date m1Date = Utility.convertStringToDate(m1.getDate(), Constants.DatePattern.YYYY_MM_DD);
            Date m2Date = Utility.convertStringToDate(m2.getDate(), Constants.DatePattern.YYYY_MM_DD);

            return m1Date.compareTo(m2Date);
        }
    }


    public ArrayList<PurchaseHistoryDetails> getPurchaseHistoryList(String formattedString) {
        ArrayList<PurchaseHistoryDetails> tempList = null;
        ArrayList<PurchaseHistoryDetails> sortedPurchaseHistoryDetails = purchaseHistoryDetails;
        if (sortedPurchaseHistoryDetails != null) {
            if (sortedPurchaseHistoryDetails.size() > 0) {
                tempList = new ArrayList<PurchaseHistoryDetails>();
                for (PurchaseHistoryDetails temp :
                        sortedPurchaseHistoryDetails) {
                    if (formattedString.equalsIgnoreCase(temp.getFormattedDateString())) {
                        tempList.add(temp);
                    }
                }
            }
        }
        return tempList;
    }
}
