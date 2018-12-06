package com.urbanpoint.UrbanPoint.managers.drawerItems;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.MyEarningsInfo;
import com.urbanpoint.UrbanPoint.dataobject.drawerItem.MyReviewsList;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.CommunicationManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aparnasalve on 16/3/16.
 */
public class MyReviewsManager implements CallBack {

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
    public MyReviewsManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }

    public void doGetReviewList() {
        String jsonString = createReviewListRequestJSON();
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_MY_REVIEW_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_MY_REVIEW_LIST, this, jsonString, tasksID);
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
        if (tasksID == Constants.TaskID.GET_MY_EARNINGS_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    AppInstance.myEarningsInfo = gson.fromJson(data, MyEarningsInfo.class);
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                }catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == Constants.TaskID.GET_MY_REVIEW_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    AppInstance.myReviewsList = gson.fromJson(data, MyReviewsList.class);
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                }catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else {
            serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.no_data_received));
        }
    }

    private String createReviewListRequestJSON() {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);

            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createReviewListRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

}

