package com.urbanpoint.UrbanPoint.managers.drawerItems;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.MyEarningsInfo;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.CommunicationManager;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anurag Sethi
 * The class will handle all the implementations related to the login operations
 */
public class MyEarningsManager implements CallBack {

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
    public MyEarningsManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }


    public void doGetMyEarnings() {
        String jsonString = createMyEarningRequestJSON();
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_MY_EARNINGS_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_MY_EARNINGS, this, jsonString, tasksID);
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
                Gson gson = new GsonBuilder().create();
                AppInstance.myEarningsInfo = gson.fromJson(data, MyEarningsInfo.class);
                serviceRedirectionObj.onSuccessRedirection(tasksID);
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        }
    }


    private String createMyEarningRequestJSON() {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CustomerJSONKeys.CUSTOMER_PARAM, custID);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createMyEarningRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

}
