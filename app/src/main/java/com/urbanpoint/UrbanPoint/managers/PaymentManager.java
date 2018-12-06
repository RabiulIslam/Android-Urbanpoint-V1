package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.OoredooValidation;
import com.urbanpoint.UrbanPoint.dataobject.VodafoneValidation;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_CHECK_OOREDOO_VALIDATION;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_RESEND_VODAFONE_VALIDATION_CODE;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.OOREDOO_SUBSCRIBE_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_RESEND_OOREDOO_VALIDATION_CODE;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_SUBSCRIBE_VODAFONE_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.VODOFONE_VERIFY_SUBSCRIPTION_TASK_ID;

public class PaymentManager implements CallBack {

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
    public PaymentManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }


//    public void doOoredooValidation(String _mobileNumber) {
//        String jsonString = createOoredooValidationRequestJSON(_mobileNumber);
//        commObj = new CommunicationManager(this.context);
//        tasksID = DO_CHECK_OOREDOO_VALIDATION;
//        commObj.CallWebService(this.context, Constants.WebServices.WS_DO_IS_OOREDOO_VALID, this, jsonString, tasksID);
//    }

    public void doSubscribeOoredoo(String _mobileNumber) {
        String jsonString = createDoSubscribeRequestJSON(_mobileNumber);
        commObj = new CommunicationManager(this.context);
        tasksID = OOREDOO_SUBSCRIBE_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_DO_SUBSCRIBE, this, jsonString, tasksID);
    }

    public void doSubscribeVodafone(String _transctionId, String _pinCode) {
        String jsonString = createDoSubscribeVodafoneRequestJSON(_transctionId, _pinCode);
        commObj = new CommunicationManager(this.context);
        tasksID = DO_SUBSCRIBE_VODAFONE_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_VODAFONE_VERIFY_SUBSCRIPTION, this, jsonString, tasksID);
    }

    public void doResendValidationCode(String _mobileNumber) {
        String jsonString = createOoredooValidationRequestJSON(_mobileNumber);
        commObj = new CommunicationManager(this.context);
        tasksID = DO_RESEND_OOREDOO_VALIDATION_CODE;
        commObj.CallWebService(this.context, Constants.WebServices.WS_RESEND_OOREDOO_VALIDATION_CODE, this, jsonString, tasksID);
    }

    public void doResendVodafoneValidationCode(String _transactionId) {
        String jsonString = createVodafoneResendValidationRequestJSON(_transactionId);
        commObj = new CommunicationManager(this.context);
        tasksID = DO_RESEND_VODAFONE_VALIDATION_CODE;
        commObj.CallWebService(this.context, Constants.WebServices.WS_RESEND_VODAFONE_VALIDATION_CODE, this, jsonString, tasksID);
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
        JSONObject jsonObj;
        JSONObject responseDataObj;
        JSONObject responseObj;
        if (tasksID == DO_CHECK_OOREDOO_VALIDATION) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    OoredooValidation ooredooValidation = gson.fromJson(data, OoredooValidation.class);
                    AppInstance.ooredooValidation = ooredooValidation;
                    if (ooredooValidation != null) {
                        if ("200".equalsIgnoreCase(ooredooValidation.getStatus())) {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        } else {
                            FlurryAgent.onError(DO_CHECK_OOREDOO_VALIDATION + "", ooredooValidation.getMessage(), "");
                            FlurryAgent.logEvent(ooredooValidation.getMessage());
                            serviceRedirectionObj.onFailureRedirection("" + ooredooValidation.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(DO_CHECK_OOREDOO_VALIDATION + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }
        } else if (tasksID == DO_SUBSCRIBE_VODAFONE_TASK_ID) {
            if (data != null) {
                try {
                    String strData = data;
                    Log.d("saddqw", "onResultVerify: " + strData);
                    Gson gson = new GsonBuilder().create();
                    VodafoneValidation vodafoneValidation = gson.fromJson(data, VodafoneValidation.class);
                    AppInstance.vodafoneValidation = vodafoneValidation;
                    if (vodafoneValidation != null) {
                        if (vodafoneValidation.getStatus() == 200) {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        } else {
                            FlurryAgent.onError(DO_SUBSCRIBE_VODAFONE_TASK_ID + "", vodafoneValidation.getMessage(), "");
                            FlurryAgent.logEvent(vodafoneValidation.getMessage());
                            serviceRedirectionObj.onFailureRedirection("" + vodafoneValidation.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(DO_SUBSCRIBE_VODAFONE_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }
        } else if (tasksID == DO_RESEND_OOREDOO_VALIDATION_CODE) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    OoredooValidation ooredooValidation = gson.fromJson(data, OoredooValidation.class);
                    AppInstance.ooredooValidation = ooredooValidation;
                    if (ooredooValidation != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(ooredooValidation.getStatus())) {
                            FlurryAgent.onError(DO_RESEND_OOREDOO_VALIDATION_CODE + "", ooredooValidation.getMessage(), "");
                            FlurryAgent.logEvent(ooredooValidation.getMessage());

                            serviceRedirectionObj.onFailureRedirection("" + ooredooValidation.getMessage());
                        } else if (ooredooValidation.getStatus().equals("200")) {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        } else {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(DO_RESEND_OOREDOO_VALIDATION_CODE + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }
        } else if (tasksID == DO_RESEND_VODAFONE_VALIDATION_CODE) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    OoredooValidation ooredooValidation = gson.fromJson(data, OoredooValidation.class);
                    AppInstance.ooredooValidation = ooredooValidation;
                    if (ooredooValidation != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(ooredooValidation.getStatus())) {
                            FlurryAgent.onError(DO_RESEND_VODAFONE_VALIDATION_CODE + "", ooredooValidation.getMessage(), "");
                            FlurryAgent.logEvent(ooredooValidation.getMessage());

                            serviceRedirectionObj.onFailureRedirection("" + ooredooValidation.getMessage());
                        } else if (ooredooValidation.getStatus().equals("200")) {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        } else {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(DO_RESEND_VODAFONE_VALIDATION_CODE + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }
        } else if (tasksID == OOREDOO_SUBSCRIBE_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    OoredooValidation ooredooValidation = gson.fromJson(data, OoredooValidation.class);
                    AppInstance.ooredooValidation = ooredooValidation;
                    if (ooredooValidation != null) {
                        if (ooredooValidation.getStatus().equalsIgnoreCase("200")) {
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        } else if (ooredooValidation.getStatus().equalsIgnoreCase("409")) {
                            serviceRedirectionObj.onFailureRedirection(ooredooValidation.getMessage());
//                            serviceRedirectionObj.onFailureRedirection("Phone Number already exists");
                        } else {
                            FlurryAgent.onError(OOREDOO_SUBSCRIBE_TASK_ID + "", ooredooValidation.getMessage(), "");
                            FlurryAgent.logEvent(ooredooValidation.getMessage());
                            serviceRedirectionObj.onFailureRedirection("" + ooredooValidation.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(OOREDOO_SUBSCRIBE_TASK_ID + "", "", e.getMessage());
                    serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.invalid_message));
                }
            }
        } else {
            serviceRedirectionObj.onFailureRedirection("" + context.getString(R.string.no_data_received));
        }
    }


    public String createOoredooValidationRequestJSON(String _mobileNumber) {
        String jsonParam = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.OOREDOO_MSISDN, "" + _mobileNumber);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createCheckSubscriptionRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

    public String createVodafoneResendValidationRequestJSON(String _transactionId) {
        String jsonParam = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.SUBSCRIPTION_TRANSACTION_ID, _transactionId);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createVodafoneResendValidationRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

    public String createDoSubscribeRequestJSON(String _mobileNumber) {
        String jsonParam = "";
        String cusID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
        String IsNewUser = "";
        try {
            if (AppInstance.rModel_checkSubscription != null){
                    if (AppInstance.rModel_checkSubscription.getNew_customer() != null &&
                            AppInstance.rModel_checkSubscription.getNew_customer().length() > 0 &&
                            AppInstance.rModel_checkSubscription.getNew_customer().equals("1")) {
                        IsNewUser = "new";
                    } else if (AppInstance.rModel_checkSubscription.getNew_customer() != null &&
                            AppInstance.rModel_checkSubscription.getNew_customer().length() > 0 &&
                            AppInstance.rModel_checkSubscription.getNew_customer().equals("0")) {
                        IsNewUser = "old";
                    } else {
                        IsNewUser = "old";
                    }
                }else {
                IsNewUser = "old";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.Request.CUSTOMER_ID_, "" + cusID);
                jsonObject.put(Constants.Request.OOREDOO_MSISDN, "" + _mobileNumber);
                jsonObject.put("newuser", IsNewUser);
                jsonParam = jsonObject.toString();
                MyApplication.getInstance().printLogs("createDoSubscribeRequestJSON", jsonParam);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            return jsonParam;
        }

    public String createDoSubscribeVodafoneRequestJSON(String _transactionId, String _pinCode) {
        String jsonParam = "";
        String IsNewUser = "old";
        try {
            if (AppInstance.rModel_checkSubscription != null) {
                if (AppInstance.rModel_checkSubscription.getNew_customer() != null &&
                        AppInstance.rModel_checkSubscription.getNew_customer().length() > 0 &&
                        AppInstance.rModel_checkSubscription.getNew_customer().equals("1")) {
                    IsNewUser = "new";
                } else if (AppInstance.rModel_checkSubscription.getNew_customer() != null &&
                        AppInstance.rModel_checkSubscription.getNew_customer().length() > 0 &&
                        AppInstance.rModel_checkSubscription.getNew_customer().equals("0")) {
                    IsNewUser = "old";
                }
            } else {
                IsNewUser = "old";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.SUBSCRIPTION_TRANSACTION_ID, _transactionId);
            jsonObject.put(Constants.Request.PIN_CODE_, _pinCode);
            jsonObject.put("newuser", IsNewUser);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createDoSubscribeVodafoneRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }
}
