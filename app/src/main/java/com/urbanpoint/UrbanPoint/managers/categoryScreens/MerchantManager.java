package com.urbanpoint.UrbanPoint.managers.categoryScreens;

import android.content.Context;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.RModel_MerchintList;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.MerchantDetailsList;
import com.urbanpoint.UrbanPoint.dataobject.DModel_Permissions;
import com.urbanpoint.UrbanPoint.managers.CommunicationManager;
import com.urbanpoint.UrbanPoint.managers.ContextualPermision_WebHit_Post_update_permission;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_UPDATE_PERMISSIONS;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_BEAUTY_MERCHANT_LIST_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_FOOD_MERCHANT_LIST_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_FUN_MERCHANT_LIST_TASK_ID;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_HEALTH_MERCHANT_LIST_TASK_ID;


public class MerchantManager implements CallBack {

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
    public MerchantManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }


    public void doUpdatePermissions() {
        ContextualPermision_WebHit_Post_update_permission contextualPermision_webHit_post_update_permission = new ContextualPermision_WebHit_Post_update_permission();
        contextualPermision_webHit_post_update_permission.updatePermission(this.context);

//        String jsonString = createPermissionUpdateRequestJSON();
//        commObj = new CommunicationManager(this.context);
//        tasksID = DO_UPDATE_PERMISSIONS;
//        commObj.CallWebService(this.context, Constants.WebServices.WS_POST_UPDATE_PERMISSIONS, this, jsonString, tasksID);
    }

    public void doGetMerchantList() {
        AppInstance.rModel_merchintList = null;
        String jsonString = createMerchantListRequestJSON("" + Constants.IntentPreference.FOOD_ID);
        commObj = new CommunicationManager(this.context);
        tasksID = GET_FOOD_MERCHANT_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_MERCHANT_LIST, this, jsonString, tasksID);
    }

    public void doGetBeautyAndHealthMerchantList() {
        AppInstance.rModel_merchintList = null;
        String jsonString = createMerchantListRequestJSON("" + Constants.IntentPreference.BEAUTY_ID);
        commObj = new CommunicationManager(this.context);
        tasksID = GET_BEAUTY_MERCHANT_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_MERCHANT_LIST, this, jsonString, tasksID);
    }

    public void doGetFunActivitiesMerchantList() {
        AppInstance.rModel_merchintList = null;
        String jsonString = createMerchantListRequestJSON("" + Constants.IntentPreference.FUN_ID);
        commObj = new CommunicationManager(this.context);
        tasksID = GET_FUN_MERCHANT_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_MERCHANT_LIST, this, jsonString, tasksID);
    }

    public void doGetRetailNServicesMerchantList() {
        AppInstance.rModel_merchintList = null;
        String jsonString = createMerchantListRequestJSON("" + Constants.IntentPreference.RetailnServices_ID);
        commObj = new CommunicationManager(this.context);
        tasksID = GET_HEALTH_MERCHANT_LIST_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_MERCHANT_LIST, this, jsonString, tasksID);
    }

    public void doGetMerchantDetails(String merchantid) {
        String jsonString = createMerchantDetailRequestJSON("" + merchantid);
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_MERCHANT_DETAIL_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_MERCHANT_DETAIL, this, jsonString, tasksID);

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
        if (tasksID == GET_FOOD_MERCHANT_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    Log.d("dadasf", "onResult: " + data.toString());
                    RModel_MerchintList rModel_merchintList = gson.fromJson(data, RModel_MerchintList.class);
                    AppPreference.setSetting(context, "key_food_drink_outlet", data);
                    if (rModel_merchintList != null) {
                        if (rModel_merchintList.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.rModel_merchintList = null;
                        } else {
                            AppInstance.rModel_merchintList = rModel_merchintList;
                        }
                    }
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.invalid_message));
                }
            } else {
                Gson gson = new GsonBuilder().create();
                String strData = AppPreference.getSetting(context, "key_food_drink_outlet", "");
                RModel_MerchintList strdModel_merchintList = gson.fromJson(strData, RModel_MerchintList.class);
                if (strdModel_merchintList != null) {
                    if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(strdModel_merchintList.getStatus())) {
                        AppInstance.rModel_merchintList = null;
                        serviceRedirectionObj.onFailureRedirection("" + tasksID);
                    } else {
                        AppInstance.rModel_merchintList = strdModel_merchintList;
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    }
                } else
                    serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == GET_BEAUTY_MERCHANT_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    RModel_MerchintList rModel_merchintList = gson.fromJson(data, RModel_MerchintList.class);
                    AppPreference.setSetting(context, "key_beauty_spa_outlet", data);
                    if (rModel_merchintList != null) {
                        if (rModel_merchintList.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.rModel_merchintList = null;
                        } else {
                            AppInstance.rModel_merchintList = rModel_merchintList;
                        }
                    }
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.invalid_message));
                }
            } else {
                Gson gson = new GsonBuilder().create();
                String strData = AppPreference.getSetting(context, "key_beauty_spa_outlet", "");
                RModel_MerchintList strdModel_merchintList = gson.fromJson(strData, RModel_MerchintList.class);
                if (strdModel_merchintList != null) {
                    if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(strdModel_merchintList.getStatus())) {
                        AppInstance.rModel_merchintList = null;
                        serviceRedirectionObj.onFailureRedirection("" + tasksID);
                    } else {
                        AppInstance.rModel_merchintList = strdModel_merchintList;
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    }
                } else
                    serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == GET_FUN_MERCHANT_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Log.d("asdfwqda", "onResult: " + data.toString());
                    Gson gson = new GsonBuilder().create();
                    RModel_MerchintList rModel_merchintList = gson.fromJson(data, RModel_MerchintList.class);
                    AppPreference.setSetting(context, "key_fun_outlet", data);
                    if (rModel_merchintList != null) {
                        if (rModel_merchintList.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.rModel_merchintList = null;
                        } else {
                            AppInstance.rModel_merchintList = rModel_merchintList;
                        }
                    }
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.invalid_message));
                }
            } else {
                Gson gson = new GsonBuilder().create();
                String strData = AppPreference.getSetting(context, "key_fun_outlet", "");
                RModel_MerchintList strdModel_merchintList = gson.fromJson(strData, RModel_MerchintList.class);
                if (strdModel_merchintList != null) {
                    if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(strdModel_merchintList.getStatus())) {
                        AppInstance.rModel_merchintList = null;
                        serviceRedirectionObj.onFailureRedirection("" + tasksID);
                    } else {
                        AppInstance.rModel_merchintList = strdModel_merchintList;
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    }
                } else
                    serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == GET_HEALTH_MERCHANT_LIST_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    RModel_MerchintList rModel_merchintList = gson.fromJson(data, RModel_MerchintList.class);
                    AppPreference.setSetting(context, "key_health_outlet", data);
                    if (rModel_merchintList != null) {
                        if (rModel_merchintList.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                            AppInstance.rModel_merchintList = null;
                            serviceRedirectionObj.onFailureRedirection("" + tasksID);
                        } else {
                            AppInstance.rModel_merchintList = rModel_merchintList;
                            serviceRedirectionObj.onSuccessRedirection(tasksID);
                        }
                    } else
                        serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.invalid_message));
                }
            } else {
                Gson gson = new GsonBuilder().create();
                String strData = AppPreference.getSetting(context, "key_health_outlet", "");
                RModel_MerchintList strdModel_merchintList = gson.fromJson(strData, RModel_MerchintList.class);
                if (strdModel_merchintList != null) {
                    if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(strdModel_merchintList.getStatus())) {
                        AppInstance.rModel_merchintList = null;
                        serviceRedirectionObj.onFailureRedirection("" + tasksID);
                    } else {
                        AppInstance.rModel_merchintList = strdModel_merchintList;
                        serviceRedirectionObj.onSuccessRedirection(tasksID);
                    }
                } else
                    serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        } else if (tasksID == Constants.TaskID.GET_MERCHANT_DETAIL_TASK_ID) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    MerchantDetailsList merchantDetailsList = gson.fromJson(data, MerchantDetailsList.class);
                    if (merchantDetailsList != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(merchantDetailsList.getStatus())) {
                            AppInstance.merchantDetailsList = null;
                        } else {
                            AppInstance.merchantDetailsList = merchantDetailsList;
                        }
                    }
                    serviceRedirectionObj.onSuccessRedirection(tasksID);
                } catch (Exception e) {
                    e.printStackTrace();
                    serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.invalid_message));
                }
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }

        } else if (tasksID == Constants.TaskID.DO_UPDATE_PERMISSIONS) {
            if (data != null) {
                try {
                    Gson gson = new GsonBuilder().create();
                    DModel_Permissions dModel_permissions = gson.fromJson(data, DModel_Permissions.class);
                    if (dModel_permissions != null) {
                        if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(dModel_permissions.getStatus())) {
                            FlurryAgent.onError(DO_UPDATE_PERMISSIONS + "", dModel_permissions.getStatus(), "");
                            FlurryAgent.logEvent(dModel_permissions.getStatus());
                            Log.d("sadasdfasd", "onResult: " + dModel_permissions.getMessage());
                        } else {
                            Log.d("sadasdfasd", "onResult: " + dModel_permissions.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FlurryAgent.logEvent(e.getMessage());
                    FlurryAgent.onError(DO_UPDATE_PERMISSIONS + "", "", e.getMessage());
                    Log.d("sadasdfasd", "onResultException: " + e.getMessage());
                }
            }
        } else {
            serviceRedirectionObj.onFailureRedirection("" + this.context.getResources().getString(R.string.no_data_received));
        }

    }


    private String createMerchantListRequestJSON(String catID) {
        String jsonParam = "";
        double latitude, longitude;
        latitude = GPSTracker.getInstance(context).getLatitude();
        longitude = GPSTracker.getInstance(context).getLongitude();

        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.LAT, latitude);
            jsonObject.put(Constants.Request.LONG, longitude);
            jsonObject.put(Constants.Request.CATEGORY_ID, catID);
            jsonObject.put(Constants.Request.CustomerJSONKeys.CUSTOMER_PARAM, "" + custID);
            jsonObject.put(Constants.Request.PAGECOUNT, "1");
            jsonObject.put(Constants.Request.PRODUCTCOUNT, "");

            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createMerchantListRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

    private String createPermissionUpdateRequestJSON() {
        String jsonParam = "";
        String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CUSTOMER_ID_, custID);
            jsonObject.put(Constants.Request.PUSH_PERMISSION, "1");
            jsonObject.put(Constants.Request.LOCATION_PERMISSION, "1");

            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createPermissionUpdateRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }


    private String createMerchantDetailRequestJSON(String merchantID) {
        String jsonParam = "";
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.MERCHANT_ID, merchantID);
            jsonObject.put(Constants.Request.CustomerJSONKeys.CUSTOMER_PARAM, "" + custID);
            jsonParam = jsonObject.toString();
            MyApplication.getInstance().printLogs("createMerchantDetailRequestJSON", jsonParam);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jsonParam;
    }

}
