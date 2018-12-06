package com.urbanpoint.UrbanPoint.dataobject.drawerItem;

import com.google.gson.annotations.SerializedName;

/**
 * Created by swapnilahirrao on 24/10/16.
 */
public class AccessFree {

    @SerializedName("status")
    private String status;


    @SerializedName("message")
    private String message;

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
}
