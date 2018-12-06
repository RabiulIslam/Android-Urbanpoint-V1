package com.urbanpoint.UrbanPoint.managers.appLogin;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.ForgotPassword;
import com.urbanpoint.UrbanPoint.dataobject.InputUser;
import com.urbanpoint.UrbanPoint.dataobject.LoggedInUser;
import com.urbanpoint.UrbanPoint.dataobject.UserChangePin;
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
public class LoginManager implements CallBack {

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
    public LoginManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }

    /**
     * Calls the Web Service of authenticateLogin
     *
     * @param userObj having the required data
     * @return none
     */
    public void authenticateLogin(InputUser userObj) {

        String jsonString = utilObj.convertObjectToJson(userObj);

        MyApplication.getInstance().printLogs("authenticateLogin : ", jsonString);

        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.LOGIN_TASK_ID;
        Log.d("Loginn", "authenticateLogin url: " + Constants.WebServices.WS_USER_AUTHENTICATION);
        Log.d("Loginn", "authenticateLogin url: " + jsonString);
        commObj.CallWebService(this.context, Constants.WebServices.WS_USER_AUTHENTICATION, this, jsonString, tasksID);

    }

    /**
     * Calls the Web Service of forgot password
     */
    public void doForgotPassword(String mailID) {
        String jsonString = createForgotPasswordRequestJSON("" + mailID);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_FORGOT_PASSWORD_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_FORGOT_PASSWORD, this, jsonString, tasksID);

    }


    /**
     * Calls the Web Service of authenticateLogin
     *
     * @return none
     */
    public void doApplicationLogout() {
        String jsonString = "";
        String customerId = "customerId";

        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(customerId, "" + custID);
            jsonString = jsonObject.toString();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        MyApplication.getInstance().printLogs("doApplicationLogout", jsonString + "|" + Constants.WebServices.WS_USER_APPLICATION_LOGOUT);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.APP_LOGOUT_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_USER_APPLICATION_LOGOUT, this, jsonString, tasksID);
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
        if (tasksID == Constants.TaskID.LOGIN_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    AppInstance.loggedInUser = gson.fromJson(data, LoggedInUser.class);
                    Log.d("LOGINNww", "onResultNAmeIs: " + data.toString());
                    Log.d("LOGINN", "onResultNAmeIs: " + AppInstance.loggedInUser.getMessage());
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.no_data_received));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
                // utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
                //  serviceRedirectionObj.onFailureRedirection(this.context.getResources().getString(R.string.no_data_received));
            }
        } else if (tasksID == Constants.TaskID.GET_FORGOT_PASSWORD_TASK_ID) {
            if (data != null) {
                Gson gson = new GsonBuilder().create();
                try {
                    ForgotPassword forgotPassword = gson.fromJson(data, ForgotPassword.class);
                    if (forgotPassword != null) {
                        if (forgotPassword.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            serviceRedirectionObj.onFailureRedirection(forgotPassword.getMessage());
                        } else {
                            AppInstance.forgotPassword = gson.fromJson(data, ForgotPassword.class);
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception ex) {
                    serviceRedirectionObj.onFailureRedirection(this.context.getResources().getString(R.string.no_data_received));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }

        } else if (tasksID == Constants.TaskID.CHANGE_PIN_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    AppInstance.userChangePin = gson.fromJson(data, UserChangePin.class);
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.no_data_received));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }

        } else if (tasksID == Constants.TaskID.APP_LOGOUT_TASK_ID) {
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
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.no_data_received));
                    e.printStackTrace();
                }
            } else {
                //utilObj.showToast(this.context, this.context.getResources().getString(R.string.no_data_received), 1);
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        }
    }


    /**
     * Calls the Web Service of change user pin
     */

    public void doChangeUserPin(String oldPin, String newPin) {
        String jsonString = changeUserPinRequestJSON(oldPin, newPin);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.CHANGE_PIN_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_CHANGE_PIN, this, jsonString, tasksID);
    }

    private String changeUserPinRequestJSON(String currentPin, String newPin) {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CURRENT_PIN, currentPin);
            jsonObject.put(Constants.Request.NEW_PIN, newPin);
            jsonObject.put(Constants.Request.ENTITY_ID, custID);
            jsonParam = jsonObject.toString();

            MyApplication.getInstance().printLogs("changeUserPinRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

    private String createForgotPasswordRequestJSON(String mailID) {
        String jsonParam = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.FORGOT_EMAIL_ID, "" + mailID);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createForgotPasswordRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;


    }

}
