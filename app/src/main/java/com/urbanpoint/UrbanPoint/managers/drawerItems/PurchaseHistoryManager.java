package com.urbanpoint.UrbanPoint.managers.drawerItems;

import android.content.Context;

import com.urbanpoint.UrbanPoint.dataobject.PurchaseHistoryDetails;
import com.urbanpoint.UrbanPoint.managers.CommunicationManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.PurchaseHistory;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Anurag Sethi
 * The class will handle all the implementations related to the login operations
 */
public class PurchaseHistoryManager implements CallBack {

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
    public PurchaseHistoryManager(Context contextObj, ServiceRedirection successRedirectionListener) {
        context = contextObj;
        utilObj = new Utility(contextObj);
        serviceRedirectionObj = successRedirectionListener;
    }


    public void doGetPurchaseHistory() {
        String jsonString = createPurchaseHistoryRequestJSON();
        commObj = new CommunicationManager(this.context);
        tasksID = Constants.TaskID.GET_PURCHASE_HISTORY_TASK_ID;
        commObj.CallWebService(this.context, Constants.WebServices.WS_GET_PURCHASE_HISTORY, this, jsonString, tasksID);
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
        if (tasksID == Constants.TaskID.GET_PURCHASE_HISTORY_TASK_ID) {
            if (data != null) {
                Gson gson = new GsonBuilder().create();

                MyApplication.getInstance().printLogs("PurchaseHistory", data);

                PurchaseHistory purchaseHistory = gson.fromJson(data, PurchaseHistory.class);

                if (purchaseHistory != null) {
                    ArrayList<PurchaseHistoryDetails> purchaseHistoryDetails = purchaseHistory.getSortedPurchaseHistoryDetails();
                    ArrayList<PurchaseHistoryDetails> actualHistoryDetails = new ArrayList<PurchaseHistoryDetails>();
                    HashSet<String> dateHashSet = new HashSet<String>();

                    for (PurchaseHistoryDetails tempPurchaseHistoryDetails :
                            purchaseHistoryDetails) {

                        Date formattedDate = Utility.convertStringToDate(tempPurchaseHistoryDetails.getDate(), Constants.DatePattern.YYYY_MM_DD);

                        Date oneDate = new Date(formattedDate.getTime());

                        DateFormat df = new SimpleDateFormat(Constants.DatePattern.MMMM_YYYY);
                        String requiredDate = df.format(oneDate);

                        tempPurchaseHistoryDetails.setFormattedDateString(requiredDate);
                        actualHistoryDetails.add(tempPurchaseHistoryDetails);
                        dateHashSet.add(requiredDate);
                    }

                    purchaseHistory.setPurchaseHistoryDetails(actualHistoryDetails);
                    purchaseHistory.setGroupWiseDate(dateHashSet);

                    AppInstance.purchaseHistory = purchaseHistory;
                }

                serviceRedirectionObj.onSuccessRedirection(tasksID);
            } else {
                serviceRedirectionObj.onFailureRedirection("" + responseEmptyErrorMessage);
            }
        }
    }


    private String createPurchaseHistoryRequestJSON() {
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
