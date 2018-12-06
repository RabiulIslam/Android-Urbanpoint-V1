package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.urbanpoint.UrbanPoint.dataobject.VodafoneValidation;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.UnSubscribeAppEnterMobileDetailsFragment;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by indus on 1/24/2017.
 */

public class ContextualPermision_WebHit_Post_update_permission {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static VodafoneValidation responseModel = null;


    public void updatePermission(final Context _context) {
        String custID = AppPreference.getSetting(_context, Constants.Request.CUSTOMER_ID, "");

        String myUrl =  Constants.WebServices.WS_POST_UPDATE_PERMISSIONS;

        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;


        try {
            jsonParams.put(Constants.Request.CUSTOMER_ID_, custID);
            jsonParams.put(Constants.Request.PUSH_PERMISSION, "1");
            jsonParams.put(Constants.Request.LOCATION_PERMISSION, "1");
            entity = new StringEntity(jsonParams.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        mClient.addHeader("Authorization", "Bearer 73c4e5a59721c0b3a40b97a32dec873d2e09fd97");
        Log.i("sdasfsa", "url: " + myUrl);

        mClient.setMaxRetriesAndTimeout(0, 1500);
        mClient.post(_context, myUrl, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseModel = gson.fromJson(strResponse, VodafoneValidation.class);
                            Log.i("sdasfsa", "onSuccess: OK" + strResponse);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.i("sdasfsa", "onFailed: OK" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        Log.i("sdasfsa", "Onfailed: " + error.getMessage());
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseModel = gson.fromJson(strResponse, VodafoneValidation.class);
                            Log.i("sdasfsa", "onFailed: OK" + strResponse);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.i("sdasfsa", "onFailed: OK" + ex.getMessage());
                        }

                    }

                }

        );
    }
}
