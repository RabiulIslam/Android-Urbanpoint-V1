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
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by riteshpandhurkar on 16/3/16.
 */
public class ChangeCellularOperator implements CallBack {

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
    public ChangeCellularOperator(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }

    public void changeCellularOperator(int selectedOperatorValue) {
        String jsonString = createRequestJSON(selectedOperatorValue);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.CHANGE_CELLULAR_OPEARTOR;
        commObj.CallWebService(this.context, Constants.WebServices.WS_CREATED_CHANG_CELLULAR_OPERATOR, this, jsonString, tasksID);
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
        if (tasksID == Constants.TaskID.CHANGE_CELLULAR_OPEARTOR) {
            if (data != null) {
                serviceRedirectionObj.onSuccessRedirection(tasksID);
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else {
            serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
        }
    }

    private String createRequestJSON(int vodaValue) {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);
            jsonObject.put(Constants.Request.VODACUSTOMER, "" + vodaValue);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

}

