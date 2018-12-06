package com.urbanpoint.UrbanPoint.dataobject;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aparnasalve on 21/3/16.
 */
public class ForgotPassword {
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

    @Override
    public String toString() {
        return "UserForgotPassword{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
