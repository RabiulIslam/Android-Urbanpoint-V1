package com.urbanpoint.UrbanPoint.managers.drawerItems;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.VodafoneBillInfoDetails;
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
public class UpdateUserProfileManager implements CallBack {

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
    public UpdateUserProfileManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }

    public void doUpdateUserProfile(String _name, String _email, String _nationality) {
        String jsonString = createUpdateProfileRequestJSON( _name, _email, _nationality);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.DO_UPDATE_PROFILE;
        commObj.CallWebService(this.context, Constants.WebServices.WS_USER_UPDATE_PROFILE, this, jsonString, tasksID);
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
        if (tasksID == Constants.TaskID.DO_UPDATE_PROFILE) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    AppInstance.getAppInstance().setVodafoneBillInfoDetails(gson.fromJson(data, VodafoneBillInfoDetails.class));
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                }catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else {
            serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.no_data_received));
        }
    }
    private String createUpdateProfileRequestJSON(String _name, String _email, String _nationality) {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CUSTOMERID, custID );
            jsonObject.put(Constants.Request.FIRST_NAME, _name );
            jsonObject.put(Constants.Request.EMAIL_ID, _email );
//            jsonObject.put(Constants.Request.GENDER,  );
            jsonObject.put(Constants.Request.NATIONALITY, _nationality);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createUpdateProfileRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

}

