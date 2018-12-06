package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_CheckSubscription;
import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_FavoritesList;
import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_HomeOffers;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.AddToWishlist;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_CHECK_OOREDOO_VALIDATION;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_CHECK_SUBSCRIPTION;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.FETCH_FAV_OFFERS_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.FETCH_HOME_OFFERS_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.FETCH_NEW_OFFERS_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.REMOVE_FAV_OFFER_TASK_ID;

public class HomeManager implements CallBack {

    Context context;
    Utility utilObj;
    CommunicationManager commObj;
    ServiceRedirection serviceRedirectionObj;
    int tasksID;

    /**
     * Constructor
     *
     * @param contextObj                 The Context from where the method is called
     * @param successRedirectionListener The listener interface for receiving action events
     * @return none
     */
    public HomeManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }


//    public void doCheckSubscribe(String _customerId) {
//        String jsonString = createCheckSubscriptionRequestJSON(_customerId);
//        commObj = new CommunicationManager(this.context);
//        tasksID = DO_CHECK_SUBSCRIPTION;
//        commObj.CallWebService(this.context, Constants.WebServices.WS_DO_CHECK_SUBSCRIPTION, this, jsonString, tasksID);
//    }

    public void doFetchHomeOffers() {
        String jsonString = createWishListRequestJSON();
        commObj = new CommunicationManager(this.context);
        tasksID = FETCH_HOME_OFFERS_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_DO_FETCH_HOME_OFFERS, this, jsonString, tasksID);
    }

    public void doFetchNewOffers() {
        String jsonString = createWishListRequestJSON();
        commObj = new CommunicationManager(this.context);
        tasksID = FETCH_NEW_OFFERS_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_DO_FETCH_NEW_OFFERS, this, jsonString, tasksID);
    }

    public void doFetchFavOffers() {
        String jsonString = createWishListRequestJSON();
        commObj = new CommunicationManager(this.context);
        tasksID = FETCH_FAV_OFFERS_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_WISHLIST_LIST, this, jsonString, tasksID);
    }
    public void doRemoveFavOffer(String _productId) {
        String jsonString = createRemoveFavOfferRequestJSON(_productId);
        commObj = new CommunicationManager(this.context);
        tasksID = REMOVE_FAV_OFFER_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_REMOVE_WISHLIST_ITEM, this, jsonString, tasksID);
    }



    /**
     * The interface method implemented in the java files
     *
     * @param data the result returned by the web service
     * @return none
     * @since 2014-08-28
     */
    @Override
    public void onResult(String data, int tasksID, String responseEmptyErrorMessage) {
        String errorMessage = "";
        if (tasksID == DO_CHECK_SUBSCRIPTION) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    RModel_CheckSubscription rModel_checkSubscription = gson.fromJson(data, RModel_CheckSubscription.class);
                    AppInstance.rModel_checkSubscription = rModel_checkSubscription;
                    if (rModel_checkSubscription != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(rModel_checkSubscription.getData().getStatus())) {
                            FlurryAgent.onError(DO_CHECK_OOREDOO_VALIDATION + "", rModel_checkSubscription.getMessage(), "");
                            FlurryAgent.logEvent(rModel_checkSubscription.getMessage());
                            AppInstance.getAppInstance().setIsUserSubscribed(false);
                            AppPreference.setSetting(context,Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS,"false");
                            Log.d("CHKD", "onResultF: " + AppInstance.getAppInstance().isUserSubscribed());
                            serviceRedirectionObj.onFailureRedirection("" + tasksID);
                        } else if (Constants.DEFAULT_VALUES.ONE.equalsIgnoreCase(rModel_checkSubscription.getStatus())){
                            AppPreference.setSetting(context,Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS,"true");
                            AppInstance.getAppInstance().setIsUserSubscribed(true);
                            AppInstance.setStrUserStatus(rModel_checkSubscription.getData().getNewuser());
                            Log.d("CHKD", "onResultT: " + rModel_checkSubscription.getStatus());
                            AppPreference.setSetting(context, Constants.DEFAULT_VALUES.MSISDN_ID,
                                    rModel_checkSubscription.getData().getMsisdn());
                            Log.d("CHKD", "onResultT: and MSindnis: " + rModel_checkSubscription.getData().getMsisdn());
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(DO_CHECK_OOREDOO_VALIDATION + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }
        } else if (tasksID == FETCH_HOME_OFFERS_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    RModel_HomeOffers rModel_homeOffers = gson.fromJson(data, RModel_HomeOffers.class);
                    AppInstance.rModel_homeOffers = rModel_homeOffers;
                    if (rModel_homeOffers != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(rModel_homeOffers.getStatus())) {
                            FlurryAgent.onError(FETCH_HOME_OFFERS_TASK_ID + "", rModel_homeOffers.getStatus(), "");
                            FlurryAgent.logEvent(rModel_homeOffers.getStatus());
                            serviceRedirectionObj.onFailureRedirection("" + tasksID);
                        } else {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(FETCH_HOME_OFFERS_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }
        }else if (tasksID == FETCH_NEW_OFFERS_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    RModel_HomeOffers rModel_newOffers = gson.fromJson(data, RModel_HomeOffers.class);
                    AppInstance.rModel_newOffers = rModel_newOffers;
                    if (rModel_newOffers != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(rModel_newOffers.getStatus())) {
                            FlurryAgent.onError(FETCH_HOME_OFFERS_TASK_ID + "", rModel_newOffers.getStatus(), "");
                            FlurryAgent.logEvent(rModel_newOffers.getStatus());
                            serviceRedirectionObj.onFailureRedirection("" + tasksID);
                        } else {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(FETCH_HOME_OFFERS_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }
        }else if (tasksID == FETCH_FAV_OFFERS_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    RModel_FavoritesList rModel_favoritesList = gson.fromJson(data, RModel_FavoritesList.class);
                    AppInstance.rModel_favOffers = rModel_favoritesList;
                    if (rModel_favoritesList != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(rModel_favoritesList.getStatus())) {
                            FlurryAgent.onError(FETCH_FAV_OFFERS_TASK_ID + "", rModel_favoritesList.getStatus(), "");
                            FlurryAgent.logEvent(rModel_favoritesList.getStatus());
                            serviceRedirectionObj.onFailureRedirection("" + rModel_favoritesList.getMessage());
                        } else {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(FETCH_FAV_OFFERS_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }
        }else if (tasksID == REMOVE_FAV_OFFER_TASK_ID) {
            if (data != null) {
                try {
                    MyApplication.getInstance().printLogs("REMOVE_FAV_OFFER_TASK_ID", data);
                    Gson gson = new GsonBuilder().create();
                    AppInstance.removeFromWishlist = gson.fromJson(data, AddToWishlist.class);
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(REMOVE_FAV_OFFER_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                FlurryAgent.logEvent(responseEmptyErrorMessage);
                FlurryAgent.onError(REMOVE_FAV_OFFER_TASK_ID + "", responseEmptyErrorMessage, "");
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else {
            serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
        }
    }


    public String createCheckSubscriptionRequestJSON(String _customerId) {
        String jsonParam = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CUSTOMER_ID_, _customerId);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createCheckSubscriptionRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

    public String createWishListRequestJSON() {
        String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
        String jsonParam = "";
        double latitude, longitude;
        latitude = GPSTracker.getInstance(context).getLatitude();
        longitude = GPSTracker.getInstance(context).getLongitude();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CUSTOMERID, custID);
            jsonObject.put(Constants.Request.LAT, latitude);
            jsonObject.put(Constants.Request.LONG, longitude);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createWishListRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }
    public String createRemoveFavOfferRequestJSON(String _productId) {
        String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
        String jsonParam = "";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.OFFER_ID, "" + _productId);
            jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createRemoveFavOfferRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }


}
