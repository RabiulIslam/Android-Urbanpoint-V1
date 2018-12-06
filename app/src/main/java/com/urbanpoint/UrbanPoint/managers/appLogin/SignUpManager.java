package com.urbanpoint.UrbanPoint.managers.appLogin;

import android.content.Context;
import android.util.Log;

import com.urbanpoint.UrbanPoint.dataobject.InputUser;
import com.urbanpoint.UrbanPoint.managers.CommunicationManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.SignUpUser;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anurag Sethi
 * The class will handle all the implementations related to the login operations
 */
public class SignUpManager implements CallBack {

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
    public SignUpManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }

    /**
     * Calls the Web Service of authenticateLogin
     *
     * @param signUpUser having the required data
     * @return none
     */
    public void doSignUp(SignUpUser signUpUser) {
        String jsonString = createSignUpRequestJSON(signUpUser);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.SIGN_UP_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_USER_SIGN_UP, this, jsonString, tasksID);
    }

    public void doCMSSignUp(SignUpUser signUpUser, String _pin) {
        String jsonString = createCMSSignUpRequestJSON(signUpUser, _pin);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GCM_SIGN_UP_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GCM_USER_SIGN_UP, this, jsonString, tasksID);
    }

    /**
     * Calls the Web Service of forgot password
     *
     * @param userObj having the required data
     * @return none
     */
    public void forgotPassword(InputUser userObj) {

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
        if (tasksID == Constants.TaskID.SIGN_UP_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    AppInstance.tempLocalSignUpUser = AppInstance.signUpUser;
                    AppInstance.signUpUser = gson.fromJson(data, SignUpUser.class);
                    if (AppInstance.signUpUser != null) {
                        AppPreference.setSetting(context, Constants.Request.USER_NAME, AppInstance.tempLocalSignUpUser.getFirstName());
                        AppInstance.signUpUser.setVodafonecustomer(AppInstance.tempLocalSignUpUser.getVodafonecustomer());
                        AppInstance.signUpUser.setOoredoocustomer(AppInstance.tempLocalSignUpUser.getOoredoocustomer());
                        AppInstance.signUpUser.setGender(AppInstance.tempLocalSignUpUser.getGender());
                        AppInstance.signUpUser.seteMailId(AppInstance.tempLocalSignUpUser.geteMailId());
                        AppInstance.signUpUser.setFirstName(AppInstance.tempLocalSignUpUser.getFirstName());
                    }
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == Constants.TaskID.GCM_SIGN_UP_TASK_ID) {
            serviceRedirectionObj.onSuccessRedirection(tasksID);
        } else {
            serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
        }
    }

    private String createSignUpRequestJSON(SignUpUser signUpUser) {
        String jsonParam = "";
        String cusID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
        Log.d("sadasd", "createSignUpRequestJSON: "+cusID);
        try {
            String storedDeviceID = AppPreference.getSetting(context, Constants.Request.DEVICE_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.SignUpJSONKeys.CATPREF, signUpUser.getCategoryPreference());
            jsonObject.put(Constants.Request.SignUpJSONKeys.CUST_PIN, signUpUser.getCustomerPin());
            jsonObject.put(Constants.Request.EMAIL_ID, signUpUser.geteMailId());
            jsonObject.put(Constants.Request.AGE, signUpUser.getAge());

            String gcmID = AppPreference.getSetting(context, Constants.GCM.RECEIVED_GCM_ID, "");
            jsonObject.put(Constants.Request.GCM_TOKEN_ID, gcmID);

            jsonObject.put(Constants.Request.CUSTOMER_ID_, cusID);
            jsonObject.put(Constants.Request.FIRST_NAME, signUpUser.getFirstName());
            jsonObject.put(Constants.Request.GENDER, signUpUser.getGender());
            jsonObject.put(Constants.Request.OOREDOOCUSTOMER, signUpUser.getOoredoocustomer());
            jsonObject.put(Constants.Request.VODAFONECUSTOMER, signUpUser.getVodafonecustomer());
            jsonObject.put(Constants.Request.DEVICE_ID, storedDeviceID);

            String deviceInfo = Constants.DEFAULT_VALUES.PLATFORM + "|" + android.os.Build.VERSION.RELEASE + "|" + android.os.Build.BRAND + "|" + android.os.Build.MODEL;

            jsonObject.put(Constants.Request.DEVICE_INFO, deviceInfo);

            jsonParam = jsonObject.toString();

            MyApplication.getInstance().printLogs("createSignUpRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }
    private String createCMSSignUpRequestJSON(SignUpUser signUpUser, String _pin) {
        String jsonParam = "";
        String cusID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
        Log.d("sadasd", "createSignUpRequestJSON: "+cusID);
        try {
            String storedDeviceID = AppPreference.getSetting(context, Constants.Request.DEVICE_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.SignUpJSONKeys.CATPREF, signUpUser.getCategoryPreference());
            jsonObject.put(Constants.Request.SignUpJSONKeys.CUST_PIN, _pin);
            jsonObject.put(Constants.Request.EMAIL_ID, signUpUser.geteMailId());
            jsonObject.put(Constants.Request.AGE, signUpUser.getAge());

            String gcmID = AppPreference.getSetting(context, Constants.GCM.RECEIVED_GCM_ID, "");
            jsonObject.put(Constants.Request.GCM_TOKEN_ID, gcmID);

            jsonObject.put(Constants.Request.CUSTOMER_ID_, cusID);
            jsonObject.put(Constants.Request.FIRST_NAME, signUpUser.getFirstName());
            jsonObject.put(Constants.Request.GENDER, signUpUser.getGender());
            jsonObject.put(Constants.Request.OOREDOOCUSTOMER, signUpUser.getOoredoocustomer());
            jsonObject.put(Constants.Request.VODAFONECUSTOMER, signUpUser.getVodafonecustomer());
            jsonObject.put(Constants.Request.DEVICE_ID, storedDeviceID);

            String deviceInfo = Constants.DEFAULT_VALUES.PLATFORM + "|" + android.os.Build.VERSION.RELEASE + "|" + android.os.Build.BRAND + "|" + android.os.Build.MODEL;

            jsonObject.put(Constants.Request.DEVICE_INFO, deviceInfo);

            jsonParam = jsonObject.toString();

            MyApplication.getInstance().printLogs("createSignUpRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }
}
