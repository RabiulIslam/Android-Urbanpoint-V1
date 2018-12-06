package com.urbanpoint.UrbanPoint.managers.drawerItems;

import android.content.Context;
import android.util.Log;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.drawerItem.AccessFree;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.CommunicationManager;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by swapnilahirrao on 24/10/16.
 */
public class AccessFreeManager implements CallBack {

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
    public AccessFreeManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }


    /**
     * Calls the Web Service of acces code pin
     */

    public void putAccesCode(String custId, String accessCode) {
        String jsonParam = "";
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CUSTOMER_ID_, custId);
            jsonObject.put("access-code", accessCode);
            jsonParam = jsonObject.toString();

            MyApplication.getInstance().printLogs("putAccesCode", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }

        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.ACCESSCODE_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_ACCESS_CODE, this, jsonParam, tasksID);
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
        if (tasksID == Constants.TaskID.ACCESSCODE_TASK_ID) {
            if (data != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(data);
                    String status = jsonResponse.getString(Constants.Request.STATUS);
                    String message = jsonResponse.getString(Constants.Request.MESSAGE);
                    if (Constants.DEFAULT_VALUES.ONE.equalsIgnoreCase(status)) {
                        AccessFree accessFree = new AccessFree();
                        accessFree.setMessage(message);
                        AppInstance.accessFree = accessFree;
                        AppInstance.goHome = true;
                        AppInstance.getAppInstance().setIsUserSubscribed(true);
//                        utilObj.showToast(this.context, message, 1);
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    } else {
                        AppInstance.goHome = false;
                        Log.d("dasdfsa", "onResult: "+message);
                        serviceRedirectionObj.onFailureRedirection("" + message);
                    }
                } catch (JSONException e) {
                    AppInstance.goHome = false;
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.no_data_received));
                    e.printStackTrace();
                }
            } else {
                //utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        }
    }


}
