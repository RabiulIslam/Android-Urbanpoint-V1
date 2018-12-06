package com.urbanpoint.UrbanPoint.dataobject;

/**
 * Created by Anurag Sethi
 * The class handles all the objects associated with the InputUser entity
 */
public class InputUser {

    /**
     * FirstName of the InputUser
     */
    private String firstName;

    /**
     * LastName of the user
     */
    private String lastName;

    /**
     * UserID
     */
    private int userID;

    /**
     * InputUser's email address
     */
    private String emailid;

    /**
     * InputUser's password
     */
    private String password;

    /**
     * InputUser's deviceID
     */
    private String device_id;


    private String deviceTokenID;

    private String mode;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDeviceTokenID() {
        return deviceTokenID;
    }

    public void setDeviceTokenID(String deviceTokenID) {
        this.deviceTokenID = deviceTokenID;
    }
}
