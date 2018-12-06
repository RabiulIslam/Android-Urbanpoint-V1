package com.urbanpoint.UrbanPoint.utils;

/**
 * Created by Anurag Sethi
 */
public class ResponseCodes {

    /** System Level Response Codes Starts Here **/

    /**
     * The Constant Success.
     */
    public static final int Success = 200;

    /**
     * The Constant InvalidJsonFormat.
     */
    public static final int InvalidJsonFormat = 201;

    /**
     * The Constant UnthorisedAccess.
     */
    public static final int UnthorisedAccess = 202;

    /**
     * The Constant NoDataFound.
     */
    public static final int NoDataFound = 203;

    /**
     * The Constant IncorrectFileFormat
     */
    public static final int IncorrectFileFormat = 204;


    /**
     * The Constant FileDoesNotExist
     */
    public static final int FileDoesNotExists = 205;


    /** Functionality Specific Response Codes Starts Here **/

    /**
     * The Constant MissingEmail
     */
    public static final int MissingEmail = 300;

    /**
     * The Constant MissingPassword
     */
    public static final int MissingPassword = 301;

    /**
     * The Constant IncorrectEmailPassword
     */
    public static final int IncorrectEmailPassword = 302;

    /**
     * The Constant InvalidEmailPassword
     */
    public static final int InvalidEmailPassword = 303;

    /**
     * The Constant Status
     */
    public static final String STATUS_ZERO = "0";


}
