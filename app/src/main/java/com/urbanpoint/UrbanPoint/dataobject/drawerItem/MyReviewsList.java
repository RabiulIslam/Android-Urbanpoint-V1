package com.urbanpoint.UrbanPoint.dataobject.drawerItem;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by aparnasalve on 16/3/16.
 */
public class MyReviewsList {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<MyReviewsListItem> myReviewsListItemArrayList;

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

    public ArrayList<MyReviewsListItem> getMyReviewsListItemArrayList() {
        return myReviewsListItemArrayList;
    }

    public void setMyReviewsListItemArrayList(ArrayList<MyReviewsListItem> myReviewsListItemArrayList) {
        this.myReviewsListItemArrayList = myReviewsListItemArrayList;
    }


    @Override
    public String toString() {
        return "Filter{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", myReviewsListItemArrayList=" + myReviewsListItemArrayList +
                '}';
    }
}
