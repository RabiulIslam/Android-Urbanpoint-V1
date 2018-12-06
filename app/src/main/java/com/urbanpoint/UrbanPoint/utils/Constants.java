package com.urbanpoint.UrbanPoint.utils;


/**
 * Created by Anurag Sethi
 * The class handles the constants used in the application
 */
public class Constants {

    /**
     * Handles the SplashScreen constants
     */

    public static class SplashScreen {
        /**
         * The parameter is used to manage the splash screen delay
         */
        public static int HOME_DELAY_LENGTH = 500;
        public static int SPLASH_DELAY_LENGTH = 2000;
    }

    /**
     * Handles webservice constants
     */

    public static class WebServices {

//              new Staging Server
//        public static String WS_BASE_URL_CMS_SERVER = "http://13.126.174.129/iosweb/urban_point_apis/api/";
//        public static String WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS = "http://13.126.174.129/iosweb/subscriptionApi/";


        //Live Server
        public static String WS_BASE_URL_CMS_SERVER = "http://13.59.183.36/urban_point_apis/api/";
        public static String WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS = "http://13.59.183.36/subscriptionApi/";


        public static String WS_BASE_URL_MAGENDO_SERVER = "https://www.urbanpoint.com/dev2/index.php/webservice/update/";
        public static String WS_BASE_URL_MAGENDO_SERVER_2 = "https://www.urbanpoint.com/dev2/index.php/webservice/";
        public static String WS_USER_AUTHENTICATION = WS_BASE_URL_MAGENDO_SERVER + "login";
        public static String WS_USER_UPDATE_PROFILE = WS_BASE_URL_MAGENDO_SERVER + "update";
        public static String WS_USER_APPLICATION_LOGOUT = WS_BASE_URL_MAGENDO_SERVER + "logout";
        public static String WS_USER_SIGN_UP = WS_BASE_URL_MAGENDO_SERVER + "signup";
        public static String WS_GCM_USER_SIGN_UP = WS_BASE_URL_CMS_SERVER + "app/site/signup";
        public static String WS_CHANGE_PIN = WS_BASE_URL_MAGENDO_SERVER + "pin";
        public static String WS_GET_MY_EARNINGS = WS_BASE_URL_MAGENDO_SERVER + "currentbalance";
        public static String WS_GET_PURCHASE_HISTORY = WS_BASE_URL_MAGENDO_SERVER + "purchasehistory";
        public static String WS_GET_OFFERS_LIST = WS_BASE_URL_MAGENDO_SERVER + "nearByProduct";
        public static String WS_GET_MERCHANT_LIST = WS_BASE_URL_MAGENDO_SERVER_2 + "vestUpdate/Merchantlist";
        public static String WS_POST_UPDATE_PERMISSIONS = WS_BASE_URL_CMS_SERVER + "app/site/update-permissions";
        //        public static String WS_GET_MERCHANT_LIST = "http://13.75.41.197/iosweb/urban_point/dev2/index.php/webservice/vest/Merchantlist";
//        public static String WS_GET_MERCHANT_LIST = WS_BASE_URL_MAGENDO_SERVER + "Merchantlist";
        public static String WS_GET_OFFERS_DETAIL = WS_BASE_URL_MAGENDO_SERVER + "Showproduct";
        public static String WS_ADD_TO_WISHLIST = WS_BASE_URL_MAGENDO_SERVER + "addwishlist";
        // for test
        // public static String WS_GET_MERCHANT_DETAIL = "http://www.urbanpoint.com/dev2/index.php/webservice/test/merchantdetail";
        //for live
        public static String WS_GET_MERCHANT_DETAIL = WS_BASE_URL_MAGENDO_SERVER + "merchantdetail";
        public static String WS_CHECK_CUSTOMER_PIN = WS_BASE_URL_MAGENDO_SERVER + "confirmcustomerpin";
        public static String WS_PURCHASE_CONFIRM = WS_BASE_URL_MAGENDO_SERVER + "purchaseconfirm";
        public static String WS_GET_WISHLIST_LIST = WS_BASE_URL_MAGENDO_SERVER + "getwishlist";
        public static String WS_GET_OFFERED_PACKAGES_LIST = WS_BASE_URL_MAGENDO_SERVER + "subscription";
        public static String WS_GET_FILTER_LIST = WS_BASE_URL_MAGENDO_SERVER + "search";
        public static String WS_REMOVE_WISHLIST_ITEM = WS_BASE_URL_MAGENDO_SERVER + "removewishlist";
        // public static String WS_GET_MY_REVIEW_LIST = WS_BASE_URL_MAGENDO_SERVER + "";
        public static String WS_GET_MY_REVIEW_LIST = WS_BASE_URL_MAGENDO_SERVER + "showreview";
        public static String WS_SAVE_REVIEW_LIST = WS_BASE_URL_MAGENDO_SERVER + "takereview";
        public static String WS_CHECK_SUBSCRIBE = WS_BASE_URL_MAGENDO_SERVER + "subscribecheck";
        public static String WS_FORGOT_PASSWORD = WS_BASE_URL_MAGENDO_SERVER + "forgetpassword";
        //        public static String WS_FORGOT_PASSWORD = "https://www.urbanpoint.com/dev2/index.php/webservice/test/forgetpassword";
        public static String WS_GCM_DEVICE_ID_TO_SERVER = WS_BASE_URL_MAGENDO_SERVER + "/notification/notifications/saveDeviceIdForGcm";
        public static String WS_CHECK_PROMO_CODE_IS_VALID = WS_BASE_URL_MAGENDO_SERVER + "checkpromocode";
        public static String WS_CREATE_PAYMENT = WS_BASE_URL_CMS_SERVER + "app/site/charge-stripe";
        //        public static String WS_CREATE_PAYMENT = "https://www.urbanpoint.com/dev2/index.php/webservice/index/chargeStripe";
        public static String WS_CREATED_OTHER_PAYMENT_SAVE_DETAILS = WS_BASE_URL_MAGENDO_SERVER + "subscriptioncomplete";
        public static String WS_CREATED_VODAFONE_PAYMENT_SAVE_DETAILS = WS_BASE_URL_MAGENDO_SERVER + "vodafoneStatus";
        public static String WS_CREATED_CHANG_CELLULAR_OPERATOR = WS_BASE_URL_MAGENDO_SERVER + "changeOperator";
        public static String WS_CREATED_GET_VODAFONE_BILL_INFO_LIST = WS_BASE_URL_MAGENDO_SERVER + "billInfopage";
        public static final String VODAFONE_API_BASE_URL = "http://vfqatar.alerting.services/ElxAlertsApiVFQ/submit.aspx?";
        public static final String VODAFONE_API_CONFIRM_OTP_SUBSCRIPTION_BASE_URL = "http://vfqatar.alerting.services/ElxAlertsApiVFQ/submit.aspx?";
        public static String WS_DO_UNSUBSCRIBE = WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS + "unsubscribe?Origin=";
        public static String WS_DO_UNSUBSCRIBE_VODAFONE = WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS + "CancelSubscriptionContractRequest?customer_id=";
        //        public static String WS_DO_UNSUBSCRIBE = WS_BASE_URL_MAGENDO_SERVER + "unsubscribe";
        public static String WS_DO_FETCH_HOME_OFFERS = WS_BASE_URL_MAGENDO_SERVER_2 + "home/myHome";
        public static String WS_DO_FETCH_NEW_OFFERS = WS_BASE_URL_MAGENDO_SERVER_2 + "home/newOffers";
        public static String WS_DO_CHECK_SUBSCRIPTION = WS_BASE_URL_CMS_SERVER + "app/site/subscribecheck";
        public static String WS_DO_IS_OOREDOO_VALID = WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS + "validatemsisdn";
        public static String WS_DO_IS_VODAFONE_VALID = WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS + "AddSubscriptionContractRequest";
        public static String WS_VODAFONE_VERIFY_SUBSCRIPTION = WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS + "VerifySubscriptionContract";
        public static String WS_RESEND_OOREDOO_VALIDATION_CODE = WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS + "sendMT";
        public static String WS_RESEND_VODAFONE_VALIDATION_CODE = WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS + "SendSubscriptionContractVerificationSMS";
        public static String WS_DO_SUBSCRIBE = WS_BASE_URL_CMS_SERVER_SUBSCRIPTIONS + "subscribe";
        public static String WS_DO_IS_VODA_NUMBER_ALREADY_SUBSCRIBE = "http://www.urbanpoint.com/dev2/index.php/webservice/index/checkVodafonenumberLocal";
        //        public static String WS_ACCESS_CODE = WS_BASE_URL_MAGENDO_SERVER + "AccesscodeApply";
        public static String WS_ACCESS_CODE = WS_BASE_URL_CMS_SERVER + "app/site/apply-access-code";
        public static String WS_GET_NOTIFICATIONS = WS_BASE_URL_CMS_SERVER + "cms/notifications/index";
        public static String WS_GET_NOTIFICATIONS_READ = WS_BASE_URL_CMS_SERVER + "cms/notifications/read-push";
        public static String WS_BRITE_VERIFY_EMAIL = "https://bpi.briteverify.com/emails.json?address=";
        public static String WS_UBER_TIME_ESTIMATION = "https://api.uber.com/v1.2/estimates/time";
        public static String WS_UBER_PRICE_ESTIMATION = "https://api.uber.com/v1.2/estimates/price";

    }

    /**
     * Handles the TaskIDs so as to differentiate the web service return values
     */

    public static class TaskID {
        public static int LOGIN_TASK_ID = 100;
        public static int FORGOT_PASSWORD_TASK_ID = 101;
        public static int SIGN_UP_TASK_ID = 103;
        public static int CHANGE_PIN_TASK_ID = 104;
        public static int GET_MY_EARNINGS_TASK_ID = 105;
        public static int SEND_GCM_DEVICE_ID_TO_SERVER_TASK_ID = 106;
        public static int GET_PURCHASE_HISTORY_TASK_ID = 107;
        public static int GET_FOOD_OFFER_LIST_TASK_ID = 108;
        public static int GET_FOOD_MERCHANT_LIST_TASK_ID = 109;
        public static int GET_BEAUTY_MERCHANT_LIST_TASK_ID = 110;
        public static int GET_FUN_MERCHANT_LIST_TASK_ID = 111;
        public static int GET_HEALTH_MERCHANT_LIST_TASK_ID = 112;
        public static int GET_FOOD_OFFER_DETAIL_TASK_ID = 200;
        public static int ADD_TO_WISHLIST_TASK_ID = 201;
        public static int GET_BEAUTY_OFFER_LIST_TASK_ID = 202;
        public static int GET_BEAUTY_OFFER_MERCHANT_LIST_TASK_ID = 205;
        public static int GET_FUN_ACTIVITIES_OFFER_LIST_TASK_ID = 203;
        public static int GET_HEALTH_AND_FITNESS_OFFER_LIST_TASK_ID = 204;
        public static int GET_MERCHANT_DETAIL_TASK_ID = 205;
        public static int GET_FILTER_LIST_TASK_ID = 206;
        public static int GET_CHECK_CUSTOMER_PIN_TASK_ID = 207;
        public static int GET_PURCHASE_CONFIRM_TASK_ID = 208;
        public static int GET_WISHLIST_LIST_TASK_ID = 209;
        public static int GET_OFFERED_PACKAGES_LIST_TASK_ID = 210;
        public static int GET_MY_REVIEW_LIST_TASK_ID = 211;
        public static int CHECK_SUBSCRIPTION_TASK_ID = 212;
        public static int GET_FORGOT_PASSWORD_TASK_ID = 213;
        public static int CHECK_PROMO_CODE_IS_VALID = 214;
        public static int CHANGE_CELLULAR_OPEARTOR = 215;
        public static int GET_BILL_INFO_DETAILS = 216;
        public static int APP_LOGOUT_TASK_ID = 217;
        public static int DO_UNSUBSCRIPTION_TASK_ID = 218;
        public static int ACCESSCODE_TASK_ID = 219;
        public static int DO_UPDATE_PROFILE = 220;
        public static int DO_CHECK_OOREDOO_VALIDATION = 221;
        public static int DO_RESEND_OOREDOO_VALIDATION_CODE = 222;
        public static int DO_CHECK_SUBSCRIPTION = 222;
        public static int GCM_SIGN_UP_TASK_ID = 223;
        public static int OOREDOO_SUBSCRIBE_TASK_ID = 224;
        public static int FETCH_HOME_OFFERS_TASK_ID2 = 225;
        public static int FETCH_HOME_OFFERS_TASK_ID = 225;
        public static int FETCH_NEW_OFFERS_TASK_ID = 226;
        public static int FETCH_FAV_OFFERS_TASK_ID = 227;
        public static int REMOVE_FAV_OFFER_TASK_ID = 228;
        public static int DO_SUBSCRIBE_VODAFONE_TASK_ID = 229;
        public static int VODOFONE_VERIFY_SUBSCRIPTION_TASK_ID = 230;
        public static int DO_RESEND_VODAFONE_VALIDATION_CODE = 231;
        public static int DO_UPDATE_PERMISSIONS = 232;
    }

    /**
     * Handels the home offers Api data
     */
    public static String MY_BROADCAST_INTENT_HOME_OFFERS = "COM.URBANPOINT.MY_BROADCAST_INTENT_HOME_OFFERS";
    public static String MY_BROADCAST_INTENT_CHECK_SUBSCRIBE = "COM.URBANPOINT.MY_BROADCAST_INTENT_CHECK_SUBSCRIBE";

    /**
     * Handles the flags list
     */
    public static String[] arrFlags = {"Afghanistan", "Aland Islands", "Albania", "Algeria", "American Samoa", "Andorra", "Angola",
            "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Ascension Island", "Australia",
            "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize",
            "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia & Herzegovina", "Botswana", "Bouvet Island", "Brazil",
            "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
            "Cambodia", "Cameroon", "Canada", "Canary Islands", "Cape Verde", "Caribbean Netherlands", "Cayman Islands",
            "Central African Republic", "Ceuta", "Chad", "Chile", "China", "Christmas Island", "Clipperton Island",
            "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo - Brazzaville", "Congo - Kinshasa", "Cook Islands",
            "Costa Rica", "Cote d'Ivoire", "Croatia", "Curacao", "Cyprus", "Czech Republic", "Denmark", "Diego Garcia Djibouti",
            "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
            "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia",
            "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland",
            "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard & McDonald Islands",
            "Honduras", "Hong Kong (China)", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man",
            "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait", "Kyrgyzstan",
            "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
            "Martinique", "Mauritania", "Mauritius", "Mayotte", "Melilla", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
            "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar (Burma)", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue",
            "Norfolk Island", "North Korea", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Territories",
            "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico",
            "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "St. Helena", "St. Pierre & Miquelon", "St. Kitts & Nevis", "St. Lucia",
            "St. Vincent & Grenadines", "Samoa", "San Marino", "Sao Tome & Prfncipe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles",
            "Sierra Leone", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "So. Georgia & So. Sandwich Isl.",
            "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "St. Barthelemy", "Sudan", "Suriname", "Svalbard & Jan Mayen",
            "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo",
            "Tokelau", "Tonga", "Trinidad & Tobago", "Tristan da Cunha", "Tunisia", "Turkey", "Turkmenistan", "Turks & Caicos Islands",
            "Tuvalu", "U.S. Virgin Islands", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "Uraguay", "United States",
            "U.S. Outlying Islands", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis & Futuna", "Western Sahara",
            "Yemen", "Zambia", "Zimbabwe"};


    /**
     * Handles the ButtonTags so as to differentiate them in showAlertDialog()
     */

    public static class ButtonTags {

        public static String TAG_NETWORK_SERVICE_ENABLE = "network services";
        public static String TAG_LOCATION_SERVICE_ENABLE = "location services";
    }

    /**
     * Handles the JSON Parsing
     */
    public static class JsonParsing {
        public static int PARSING_JSON_FOR_MESSAGE_ID = 1;
        public static int PARSING_JSON_FOR_RESULT = 2;
    }

    /**
     * Handles the Bugsense Key
     */
    public static class BugSenseConstants {
        //public static String SPLUNK_API_KEY = "963d5a30";
        public static String SPLUNK_API_KEY = "62be38b9";
    }

    /**
     * Handles the DebugLog constants
     */
    public static class DebugLog {
        /**
         * Will be the name of the project
         */
        public static String APP_TAG = "urbanpoint";
        /**
         * APP_MODE = 1 means Debug Mode
         * APP_MODE = 0 means Live Mode
         * Must change to 0 when going live
         */
        public static int APP_MODE = 1;
        /**
         * Name of the directory in which log file needs to be saved
         */
        public static String APP_ERROR_DIR_NAME = "urbanpoint";
        /**
         * Name of the log file
         */
        public static String APP_ERROR_LOG_FILE_NAME = "log.txt";
    }

    /**
     * Handles the constant for Google Play Services
     */
    public static class GooglePlayService {
        public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    }

    /**
     * Handles the responseCode
     */
    public static class ResponseCodes {

        public final static String CODE_001 = "001";
        public final static String CODE_002 = "002";
        public final static String CODE_003 = "003";
        public final static String CODE_005 = "005";
        public final static String CODE_007 = "007";
        public final static String CODE_008 = "008";
        public final static String CODE_009 = "009";
        public final static String CODE_010 = "010";

    }


    /**
     * Handles the constants for GCM (Google Cloud Messaging)
     */
    public static class GCM {
        // public static String GCM_SENDER_ID = "436986602751 ";
        public static final String RECEIVED_GCM_ID = "RECEIVED_GCM_ID";
        public static String GCM_SENDER_ID = "851294185595";
        public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    }

    public static class Request {
        public static String APP_SESSION_KEY = "APP_SESSION_KEY";

        public static final String GCM_TOKEN_ID = "deviceTokenID";
        public static final String MIXPANEL_TOKEN = "a37d3739dc37b7bfec03e2d4a7c3ff8b";

        public static final String EMAIL_ID = "emailid";
            public static final String AGE = "age";
        public static final String DOB = "dob";
        public static final String NATIONALITY = "nationality";
        public static String MODE = "mode";
        public static String PASSWORD = "password";
        public static final String LOGIN_MODE = "1";
        public static String FIRST_NAME = "firstname";
        public static String GENDER = "gender";
        public static String PINCODE = "pinCode";
        public static String PINCODE_WITHOUTSTAR = "pinCodeOrg";
        public static String CUSTOMER_ID = "CUSTOMER_ID";
        public static String MY_REVIEWS_COUNT = "MY_REVIEWS_COUNT";

        public static String CURRENT_PIN = "currentPin";
        public static String ENTITY_ID = "entityId";
        public static String NEW_PIN = "pin";

        public static String OFFER_ID = "productid";

        public static String CUSTOMER_ID_ = "customer_id";
        public static String CUSTOMERID = "customerid";
        public static String CUSTOMER_I_D = "customerId";
        public static String PUSH_PERMISSION = "push_permission";
        public static String LOCATION_PERMISSION = "location_permission";
        public static String SUBSCRIPTION_ID = "subscription_id";

        public static String MOB_NUMBER = "mobNumber";


        public static String SHOW_REVIEW = "show_review";
        public static String MERCHANT_ID = "merchantId";
        public static String SEARCH_TAG = "search_tag";
        public static String PAGE = "page";
        public static String SEARCH_COUNT = "searchCount";
        public static String IMAGE_URL = "product_image";
        public static String MERCHANT_IMAGE_URL = "merchant_image";
        public static String SPECIAL_PRICE = "special_price";

        public static String PIN_CODE = "pincode";
        public static String MERCHANT_PIN = "merchantpin";
        public static String MY_REVIEW = "review";
        public static String PIN = "pin";


        public static String NAME = "itemname";
        public static String USER_NAME = "user_name";
        public static String STATUS = "status";
        public static String MESSAGE = "message";
        public static String DATA = "data";
        public static String CONFIRMATION_CODE = "confirmation_code";

        public static String FORGOT_EMAIL_ID = "mailid";

        public static class SignUpJSONKeys {
            public static String CATPREF = "catpref";
            public static String CUST_PIN = "customerPin";
        }

        public static class CustomerJSONKeys {
            public static String CUSTOMER_PARAM = "customer_id";
        }

        public static String CATEGORY_ID = "category_id";
        public static String CATEGORY_NAME = "category_name";

        public static String CATEGORYID = "categoryid";
        public static String LAT = "lat";
        public static String LONG = "long";
        public static String PAGECOUNT = "pagecount";
        public static String PRODUCTCOUNT = "productcount";
        public static String WISHLIST_COUNT = "wishlist_count";

        public static String COUNT_REVIEW = "countReview";

        public static String PROMO_CODE = "promocode";
        public static String AMOUNT = "amount";
        public static String TOKEN = "token";
        public static String STRIPE_TOKEN = "stripe_token";

        public static String TRANSACTION_ID = "transactionid";
        public static String OFFER = "offer";
        public static String FLAG = "flag";

        public static String STATUS_CODE = "statusCode";
        public static String RESPONSE = "Response";


        public static final String VODAFONE_STATUS_API_USERNAME = "username";
        public static final String SERVICE_ID = "ServiceID";
        public static final String ACTION_TYPE = "ActionType";
        public static final String MSISDN = "MSISDN";
        public static final String PHONE_NUMBER = "phone_number";

        public static final String USERNAME = "usrname";

        public static final String OOREDOOCUSTOMER = "ooredoocustomer";
        public static final String VODAFONECUSTOMER = "vodafonecustomer";
        public static final String VODACUSTOMER = "vodacustomer";

        public static final String APP_VERSION = "app_version";
        public static final String DEVICE_ID = "device_id";

        public static final String VODANUMBER = "vodaNumber";
        public static final String OOREDOO_MSISDN = "Msisdn";
        public static final String SUBSCRIPTION_TRANSACTION_ID = "subscriptionContractId";
        public static final String PIN_CODE_ = "pinCode";
        public static final String VODAFONE_MSISDN = "msisdn";

        public static final String DEVICE_INFO = "deviceInfo";

        public static final String LAST_ACCESS_DATE = "lastAccessDate";
        public static final String ACCESS_CODE = "accessCode";

        public static final String NOTIFICATION_DATE = "notification_date";
        public static final String NOTIFICATION_TEXT_1 = "notification_text_1";
        public static final String NOTIFICATION_TEXT_2 = "notification_text_2";
        public static final String NOTIFICATION_TEXT_3 = "notification_text_3";

    }

    public static class Gender {
        public static final int MALE = 1;
        public static final int FEMALE = 2;
    }

    public static class Operator {
        public static final int VODAFONE = Integer.parseInt(DEFAULT_VALUES.ONE);
        public static final int OOREDOO = Integer.parseInt(DEFAULT_VALUES.ONE);
        public static final int OTHER = 0;

    }

    // DEFINED/FIXED CATEGORY ID ON SERVER
    public static class IntentPreference {
        public static final int SOURCE_LOCATION_INTENT_CODE = 94;
        public static final int PACKAGE_LOCATION_INTENT_CODE = 93;
        public static final int HOME_SCREEN_INTENT_CODE = 95;
        public static final int FOOD_ID = 17;
        public static final int BEAUTY_ID = 64;
        public static final int FUN_ID = 15;
        public static final int RetailnServices_ID = 65;
    }

    public static class ContactUS {
        public static final String EMAIL = " customersmiles@urbanpoint.com";
    }

    public static class DatePattern {
        public static final String YYYY_MM_DD = "yyyy-MM-dd";
        public static final String MMMM_YYYY = "MMMM yyyy";
        public static final String YYYY_MM_DD_hh_mm_ss = "yyyy-MM-dd hh:mm:ss a";

    }

    public static class DEFAULT_VALUES {
        public static final String PLATFORM = "Android";
        public static final String SUBSCRIPTION_CREDIT_CARD = "credit-card";
        //        public static final String SUBSCRIPTION_MOBILE = "Ooredoo";
        public static final String OOREDOO = "Ooredoo";
        public static final String VODAFONE = "Vodafone";
        public static final String DATE_FORMAT = "MMMM yyyy";
        public static final String SIGN_UP = "SIGN_UP";
        public static final String GAIN_ACCESS_BTN_STATUS = "gain_access_btn_status";
        public static final String IS_USER_SUBSCRIBE = "is_user_subscribe";
        public static final String BLANK = "";
        public static final String MSISDN_ID = "msisdn_id";
        public static final String ACTION_BAR_HEADER = "ACTION_BAR_HEADER";
        public static final String BROADCAST_STATUS = "broadcast_status";
        public static final String BROADCAST_SUBSCRIPTION_STATUS = "broadcast_subscription_status";
        public static final String KEY_USER_SUBSCRIBE_STATUS = "key_user_subscribe_status";
        public static final int UNDEFINED_PUSH_NOTIFICATION = -1;
        public static final String ZERO = "0";
        public static final String ONE = "1";
        public static final String TWO = "2";
        public static final String THREE = "3";
        public static final String LOGOUT = "LOGOUT";
        public static final String SUCCESS = "SUCCESS";
        public static final String MY_WISH_LIST_UPDATED = "MY_WISH_LIST_UPDATED";
        public static final String MY_WISH_LIST_COUNT = "MY_WISH_LIST_COUNT";
        public static final String MY_REVIEW_UPDATED = "MY_REVIEW_UPDATED";
        public static final String MY_REVIEW_COUNT_CHANGED = "MY_REVIEW_COUNT_CHANGED";
        public static final String NO_REVIEW_AVAIL = "NO_REVIEW_AVAIL";
        public static final String SHOW_VODA_CUSTOMER_UNSUBSCRIBE_OPTION = "SHOW_VODA_CUSTOMER_UNSUBSCRIBE_OPTION";
        public static final String HIDE_VODA_CUSTOMER_UNSUBSCRIBE_OPTION = "HIDE_VODA_CUSTOMER_UNSUBSCRIBE_OPTION";

        public static final String UPDATE_USER_SUBSCRIBE_STATUS = "UPDATE_USER_SUBSCRIBE_STATUS";

        public static final String STARTDATE = "startdate";

        public static final String BRITEVERIFY_KEY = "a179e7cb-f0d2-4b33-962f-dccf8a6260fa";
        public static final String VALID = "valid";
        public static final String UNKNOWN = "unknown";
        public static final String ACTIVE = "active";
        public static final String PURCHASE_DONE = "PURCHASE_DONE";

        public static final String OPERATOR_TYPE_VODAFONE = "OPERATOR_TYPE_VODAFONE";
        public static final String OPERATOR_TYPE_OOREDOO = "OPERATOR_TYPE_OOREDOO";

        public static class OfferSubScriptionsPackagesDetails {
            public static final String OFFER_ID_ONE = "3 Months";
            public static final String OFFER_ID_TWO = "6 Months";
            public static final String OFFER_ID_THREE = "12 Months";
        }

        public static final String REPLACE_STRING_CHARS = "$$$$";
        public static final String IS_USER_PAID_PAYMENT = "IS_USER_PAID_PAYMENT";

        public static final String SHARE_URL = "http://onelink.to/urbanpoint";

        /*public static final String IOS_APP_APPSTORE_LINK = "https://itunes.apple.com/us/app/urban-point/id1074590743";
        public static final String ANDROID_APP_PLAYSTORE_LINK = "https://play.google.com/store/apps/details?id=com.urbanpoint.UrbanPoint";*/
        public static final String IOS_APP_APPSTORE_LINK = SHARE_URL;
        public static final String ANDROID_APP_PLAYSTORE_LINK = "";

        public static String PURCHASE_COUNT = "purchased";
        public static String VODAFONE_STATUS = "purchased";
        public static String TRUE = "true";
        public static String FALSE = "false";
        public static String Product_Expired = "expired";


    }

    public static class OfferDetails {
        public static String ITEM_NAME = "name";
        public static String DESCRIPTION = "description";
        public static String FINE_PRINT = "fineprint";
        public static String EXPIRY_DATE = "Expiredate";
        public static String MERCHANT_ID = "merchantid";
        public static String MERCHANT_NAME = "merchantname";
        public static String MERCHANT_ADDRESS = "merchantaddress";
        public static String MERCHANT_IMAGE = "merchantimage";
        public static String TIMINGS = "timings";
        public static String LATITUDE = "latitude";
        public static String LONGITUDE = "longitude";
        public static String PHONE = "phone";
        public static String PRICE = "price";
        public static String IMAGE = "image";
        public static String SPECIAL_PRICE = "specialPrice";

        //public static String SPECIAL_PRICE = "specialprice";
        public static String DISCOUNT = "discount";
        public static String STATUS = "status";
        public static String WISHLIST_STATUS = "wishlist_item";
        public static String PRODUCT_ID = "id";
        public static String OFFER_LOCK = "OFFER_LOCK";
        public static String APPROXSAVING = "approximateSavings";

    }


    public static class SharedPreferenceKey {
        public static String SHARED_KEY = "sharedkey";
        public static String VODAFONE_STATUS = "vodafonestatus";
        public static String HOME_OFFERS_KEY = "key_home_offers";


    }


    //-- Request Constants
    public static final String BLANK = "";

    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String HEADER_CONTENT_TYPE = "Content-type";
    public static final String HEADER_VALUE_APPLICATION_JSON = "application/json";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_VALUE_UTF_8 = "utf-8";
    public static final String HEADER_VALUE_USER_AGENT = "User-Agent";
    public static final String LINE_SEPARATOR = "line.separator";
    public static final int CONNECTION_TIMEOUT = 60000;
    public static final String MESSAGE_TAG = "Message";
    public static final String COMMERCIAL_RESOURCE_TYPE_IMAGE_ = "image/png";

    public static final int THREE_SECONDS = 3000;
    public static final int TWO_SECONDS = 2000;
    public static final int FIVE_SECONDS = 5000;
    public static final int THIRTY_SECONDS = 30000;


    public static class API_EGYPTLINX_PARAMS_VALUES {
        public static final String USERNAME = "Discounts";
        public static final String PASSWORD = "D!$c0unts";
        public static final String SERVICE_ID = "2083";
        public static final String QATAR_COUNTRY_CODE = "974";

    }

    public static class StaticHtmlPage {
        public static final String REDEEM_RULES = "Rules_of_Purchase.html";
        public static final String LOGIN_RULES = "privacyStatementAndTermsofUseUrbanPoint.html";
    }

    public static interface UberRideEstimate{
        public static final String CLIENT_ID= "4t0OnuFW8ukSYSn3HbiX_2C7dadRbTb-";
        public static final String CLIENT_SECRET= "q2IZcOzGWg2UJADFV9uTSRgwELerJsAS6L8sfZCl";
        public static final String SERVER_TOKEN= "ItwOBcYSD5Ro85dWfwwl9O2DxVB-By-eN2C8AvjW";
    }

}