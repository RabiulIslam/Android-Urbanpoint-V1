package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_CheckSubscription;
import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_HomeOffers;
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
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.FETCH_HOME_OFFERS_TASK_ID2;

public class IntroActivityManager implements CallBack {

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
    public IntroActivityManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }

    public void doCheckSubscribe(String _customerId) {
        AppInstance.isBroadCastSubscriptionCompleted = false;
        String jsonString = createCheckSubscriptionRequestJSON(_customerId);
        commObj = new CommunicationManager(this.context);
        tasksID = DO_CHECK_SUBSCRIPTION;
        commObj.CallWebService(this.context, Constants.WebServices.WS_DO_CHECK_SUBSCRIPTION, this, jsonString, tasksID);
        Log.d("asdasd", "WebCall Subscription");
    }

    public void doFetchHomeOffers2() {
        AppInstance.isBroadCastHomeCompleted = false;
        HomeOffers_WebHit_Post_myHome homeOffers_webHit_post_myHome = new HomeOffers_WebHit_Post_myHome();
        homeOffers_webHit_post_myHome.fetchHomeOffers(this.context);
//        String jsonString = createWishListRequestJSON();
//        commObj = new CommunicationManager(this.context);
//        tasksID = FETCH_HOME_OFFERS_TASK_ID2;
//        MyApplication.getInstance().printLogs("createWishListRequestJSON", Constants.WebServices.WS_DO_FETCH_HOME_OFFERS);
//
//        commObj.CallWebService(this.context, Constants.WebServices.WS_DO_FETCH_HOME_OFFERS, this, jsonString, tasksID);
        Log.d("asdasd", "WebCall Home Offers");
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
        if (tasksID == FETCH_HOME_OFFERS_TASK_ID2) {
            Intent intentHomeOfffers = new Intent(Constants.MY_BROADCAST_INTENT_HOME_OFFERS);
            if (data != null) {
                try {

                    Gson gson = new GsonBuilder().create();
                    String strData = data;
                    Log.d("asdasd", "onResult:data is " + strData);
                    RModel_HomeOffers rModel_homeOffers = gson.fromJson(data, RModel_HomeOffers.class);
                    AppInstance.rModel_homeOffers = rModel_homeOffers;
                    if (rModel_homeOffers != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(rModel_homeOffers.getStatus())) {
                            FlurryAgent.onError(FETCH_HOME_OFFERS_TASK_ID2 + "", rModel_homeOffers.getStatus(), "");
                            FlurryAgent.logEvent(rModel_homeOffers.getStatus());
                            intentHomeOfffers.putExtra(Constants.DEFAULT_VALUES.BROADCAST_STATUS, "0");
                            context.sendBroadcast(intentHomeOfffers);
                            AppInstance.isBroadCastHomeCompleted = true;
                            Log.d("asdasd", "onResult: failed0");
                        } else {
                            AppPreference.setSetting(context, "key_home_offers", strData);
                            intentHomeOfffers.putExtra(Constants.DEFAULT_VALUES.BROADCAST_STATUS, "1");
                            context.sendBroadcast(intentHomeOfffers);
                            AppInstance.isBroadCastHomeCompleted = true;
                            Log.d("asdasd2", "onResult: Sucess");
                        }
                    } else {
                        AppInstance.isBroadCastHomeCompleted = true;
                        Log.d("asdasd", "onResult: failed1");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(FETCH_HOME_OFFERS_TASK_ID2 + "", "", e.getMessage());
                    intentHomeOfffers.putExtra(Constants.DEFAULT_VALUES.BROADCAST_STATUS, "0");
                    context.sendBroadcast(intentHomeOfffers);
                    AppInstance.isBroadCastHomeCompleted = true;
                    Log.d("asdasd", "onResult: exception");
                }
            } else {
                AppInstance.isBroadCastHomeCompleted = true;
                intentHomeOfffers.putExtra(Constants.DEFAULT_VALUES.BROADCAST_STATUS, "0");
                context.sendBroadcast(intentHomeOfffers);
                Log.d("asdasd", "onResult: failed2");

//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.getMessage();
//                        }
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // Do some stuff
//                                doCheckSubscribe(AppPreference.getSetting(context,
//                                        Constants.Request.CUSTOMER_ID, ""));
//                            }
//                        });
//                    }
//                };
//                thread.start();
            }
        } else if (tasksID == DO_CHECK_SUBSCRIPTION) {
            Intent intentCheckSubscribtion = new Intent(Constants.MY_BROADCAST_INTENT_CHECK_SUBSCRIBE);
            if (data != null) {
                try {
                    Log.d("asdasd", "onResult:data is " + data);
                    Gson gson = new GsonBuilder().create();
                    RModel_CheckSubscription rModel_checkSubscription = gson.fromJson(data, RModel_CheckSubscription.class);
                    AppInstance.rModel_checkSubscription = rModel_checkSubscription;
                    if (rModel_checkSubscription != null) {
                        if (rModel_checkSubscription.getUberStatus().equalsIgnoreCase("1")) {
                            AppInstance.setIsUberRequired(true);
                        } else {
                            AppInstance.setIsUberRequired(false);
                        }

                        if (Constants.DEFAULT_VALUES.ONE.equalsIgnoreCase(rModel_checkSubscription.getStatus())) {
                            AppPreference.setSetting(context, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "true");
                            AppPreference.setSetting(context, Constants.DEFAULT_VALUES.MSISDN_ID,
                                    rModel_checkSubscription.getData().getMsisdn());
                            AppInstance.getAppInstance().setIsUserSubscribed(true);
                            AppInstance.setStrUserStatus(rModel_checkSubscription.getData().getNewuser());



                            if (rModel_checkSubscription.getFirst_weak().equals("1")) {
                                Log.d("CHKD12", "onResultS: ");
                                AppInstance.canUserUnSubscribe = false;
                            } else if (rModel_checkSubscription.getData() != null &&
                                    rModel_checkSubscription.getData().getMsisdn() != null) {
                                if (rModel_checkSubscription.getData().getOperator() != null &&
                                        rModel_checkSubscription.getData().getOperator().equalsIgnoreCase("code")) {
                                    AppInstance.canUserUnSubscribe = false;
                                } else {
                                    AppInstance.canUserUnSubscribe = true;
                                }
                            } else {
                                AppInstance.canUserUnSubscribe = false;
                            }
                            Log.d("CHKD1", "onResultT: " + rModel_checkSubscription.getStatus());
                            Log.d("CHKD1", "onResultT: and MSindnis: " + rModel_checkSubscription.getData().getMsisdn());
                            intentCheckSubscribtion.putExtra(Constants.DEFAULT_VALUES.BROADCAST_SUBSCRIPTION_STATUS, "1");
                            context.sendBroadcast(intentCheckSubscribtion);
                            AppInstance.isBroadCastSubscriptionCompleted = true;

                        } else {
                            FlurryAgent.onError(DO_CHECK_OOREDOO_VALIDATION + "", rModel_checkSubscription.getMessage(), "");
                            FlurryAgent.logEvent(rModel_checkSubscription.getMessage());
                            AppInstance.getAppInstance().setIsUserSubscribed(false);
                            if (rModel_checkSubscription.getFirst_weak().equals("1")) {
                                Log.d("CHKD12", "onResultF: ");
                                AppInstance.canUserUnSubscribe = false;
                            }
                            AppPreference.setSetting(context, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "false");
                            Log.d("CHKD1", "onResultF: " + AppInstance.getAppInstance().isUserSubscribed());
                            intentCheckSubscribtion.putExtra(Constants.DEFAULT_VALUES.BROADCAST_SUBSCRIPTION_STATUS, "0");
                            context.sendBroadcast(intentCheckSubscribtion);
                            AppInstance.isBroadCastSubscriptionCompleted = true;
                        }
//                        } else {
//                            AppInstance.getAppInstance().setIsUserSubscribed(false);
//                            AppPreference.setSetting(context, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "false");
//                            Log.d("CHKD", "onResultF: " + AppInstance.getAppInstance().isUserSubscribed());
//                            intentCheckSubscribtion.putExtra(Constants.DEFAULT_VALUES.BROADCAST_SUBSCRIPTION_STATUS, "0");
//                            context.sendBroadcast(intentCheckSubscribtion);
//                            AppInstance.isBroadCastSubscriptionCompleted = true;
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AppInstance.rModel_checkSubscription = new RModel_CheckSubscription();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(DO_CHECK_OOREDOO_VALIDATION + "", "", e.getMessage());
                    AppInstance.getAppInstance().setIsUserSubscribed(false);
                    AppPreference.setSetting(context, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "false");
                    Log.d("CHKD1", "onResultF: " + AppInstance.getAppInstance().isUserSubscribed());
                    intentCheckSubscribtion.putExtra(Constants.DEFAULT_VALUES.BROADCAST_SUBSCRIPTION_STATUS, "0");
                    context.sendBroadcast(intentCheckSubscribtion);
                    AppInstance.isBroadCastSubscriptionCompleted = true;
                }
            } else {
                AppInstance.getAppInstance().setIsUserSubscribed(false);
                AppPreference.setSetting(context, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "false");
                Log.d("CHKD1", "onResultF: " + AppInstance.getAppInstance().isUserSubscribed());
                intentCheckSubscribtion.putExtra(Constants.DEFAULT_VALUES.BROADCAST_SUBSCRIPTION_STATUS, "0");
                context.sendBroadcast(intentCheckSubscribtion);
                AppInstance.isBroadCastSubscriptionCompleted = true;

//
//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.getMessage();
//                        }
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // Do some stuff
//                                doCheckSubscribe(AppPreference.getSetting(context,
//                                        Constants.Request.CUSTOMER_ID, ""));
//                            }
//                        });
//                    }
//                };
//                thread.start();
            }
        } else {
            serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
        }
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

    public String createCheckSubscriptionRequestJSON(String _customerId) {
        String jsonParam = "";
        String version = "";
        String gcmID = AppPreference.getSetting(context, Constants.GCM.RECEIVED_GCM_ID, "");

        String storedDeviceID = AppPreference.getSetting(context, Constants.Request.DEVICE_ID, "");
        String deviceInfo = Constants.DEFAULT_VALUES.PLATFORM + "|" + android.os.Build.VERSION.RELEASE + "|" + android.os.Build.BRAND + "|" + android.os.Build.MODEL;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CUSTOMER_ID_, _customerId);
            jsonObject.put(Constants.Request.DEVICE_INFO, deviceInfo);
            jsonObject.put(Constants.Request.DEVICE_ID, storedDeviceID);
            jsonObject.put(Constants.Request.APP_VERSION, version);
            jsonObject.put(Constants.Request.GCM_TOKEN_ID, gcmID);

            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createCheckSubscriptionRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }


}
