package com.urbanpoint.UrbanPoint.managers.main;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.main.HomeViewDetails;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.CommunicationManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by riteshpandhurkar on 17/3/16.
 */
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


    public synchronized void doCheckSubscribe() {
        String jsonString = createCheckSubscriptionJSON();
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.CHECK_SUBSCRIPTION_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_CHECK_SUBSCRIBE, this, jsonString, tasksID);
    }

    public void doUnSubscribe(String mobileNumber) {
        String jsonString = createUnSubscriptionJSON(mobileNumber);

        MyApplication.getInstance().printLogs("doUnSubscribe", "doUnSubscribe " + mobileNumber);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.DO_UNSUBSCRIPTION_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_DO_UNSUBSCRIBE, this, jsonString, tasksID);
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

        /**
         * THIS IF IS NOT USING CURRENTLY, REPLACE WITH OTHER ASYNC PATTERN,CHECK HOME_FRAGMENT.
         */
        if (tasksID == Constants.TaskID.CHECK_SUBSCRIPTION_TASK_ID) {
            if (data != null) {

                Gson gson = new GsonBuilder().create();
                HomeViewDetails homeDetails = gson.fromJson(data, HomeViewDetails.class);
                if (homeDetails != null) {

                    if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(homeDetails.getStatus())) {
                        AppInstance.getAppInstance().setHomeScreenDetails(null);
                    } else {
                        AppInstance.getAppInstance().setHomeScreenDetails(homeDetails);

                        AppInstance.myReviewsCount = Integer.parseInt(homeDetails.getCountReview());
                        AppInstance.setIsSubscriptionCheckCalled(true);
                    }
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } else {
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.no_data_received));
                }

                /*try {
                    JSONObject jsonObject = new JSONObject(data);

                    String review = jsonObject.getString(Constants.Request.COUNT_REVIEW);
                    int flag = jsonObject.getInt(Constants.Request.FLAG);

                    // TO CHECK IS USER IS SUBSCRIBE OR NOT - START

                    if (flag == Integer.parseInt(Constants.DEFAULT_VALUES.ZERO)) {
                        AppInstance.getAppInstance().setIsUserSubscribed(false);
                    } else {
                        AppInstance.getAppInstance().setIsUserSubscribed(true);
                    }

                    // TO CHECK IS USER IS SUBSCRIBE OR NOT - END

                    AppInstance.myReviewsCount = Integer.parseInt(review);
                    AppInstance.setIsSubscriptionCheckCalled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + tasksID);
                }*/

            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
            //-------------------

        } else if (tasksID == Constants.TaskID.DO_UNSUBSCRIPTION_TASK_ID) {
            if (data != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(data);
                    String status = jsonResponse.getString(Constants.Request.STATUS);
                    String message = jsonResponse.getString(Constants.Request.MESSAGE);
                    if (Constants.DEFAULT_VALUES.ONE.equalsIgnoreCase(status)) {
                        utilObj.showToast(this.context, message, 1);
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    } else {
                        serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.no_data_received));
                    }
                } catch (JSONException e) {
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.invalid_message));
                    e.printStackTrace();
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        }
    }

    private String createCheckSubscriptionJSON() {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CustomerJSONKeys.CUSTOMER_PARAM, "" + custID);
            jsonParam = jsonObject.toString();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

    private String createUnSubscriptionJSON(String mobileNumber) {
        String jsonParam = "";
        try {
            String customerId = "customerId";
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
//            jsonObject.put(customerId, "" + custID);
            jsonObject.put("Origin", "" + mobileNumber);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createUnSubscriptionJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }


}
