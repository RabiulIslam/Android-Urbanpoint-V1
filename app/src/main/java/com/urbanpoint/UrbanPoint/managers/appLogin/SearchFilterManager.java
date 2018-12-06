package com.urbanpoint.UrbanPoint.managers.appLogin;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.InputUser;
import com.urbanpoint.UrbanPoint.dataobject.LoggedInUser;
import com.urbanpoint.UrbanPoint.dataobject.SearchFilter.LookingForFilterList;
import com.urbanpoint.UrbanPoint.dataobject.UserChangePin;
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
 * Created by aparnasalve on 9/3/16.
 */
public class SearchFilterManager implements CallBack {

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
    public SearchFilterManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }


    public void doGetFilterList(String searchText) {
        String jsonString = createFilterRequestJSON("" + searchText);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_FILTER_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_FILTER_LIST, this, jsonString, tasksID);
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
        if (tasksID == Constants.TaskID.GET_FILTER_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    AppInstance.lookingForFilterList = gson.fromJson(data, LookingForFilterList.class);
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                }catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                AppInstance.lookingForFilterList = null;
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else {
            serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
        }
    }


    /**
     * Calls the Web Service of change user pin
     */


    private String createFilterRequestJSON(String searchText) {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            String page = "1";
            String count = "0";
            String search = "" + searchText;
            jsonObject.put(Constants.Request.PAGE, page);
            jsonObject.put(Constants.Request.SEARCH_COUNT, count);
            jsonObject.put(Constants.Request.SEARCH_TAG, search);
            jsonObject.put(Constants.Request.CustomerJSONKeys.CUSTOMER_PARAM, "" + custID);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("SearcFilterRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

}

