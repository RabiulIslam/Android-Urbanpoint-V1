package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_Notifications;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.fragments.HomeFragments.NotificationFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.ProfileFragment;

import org.apache.http.Header;

/**
 * Created by indus on 1/24/2017.
 */

public class Home_WebHit_Get_Notifications {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static RModel_Notifications responseModel = null;


    public void getNotification(final Context _context, final NotificationFragment notificationFragment,
                                final int pageCount) {

        String myUrl = Constants.WebServices.WS_GET_NOTIFICATIONS;
        String custID = AppPreference.getSetting(_context, Constants.Request.CUSTOMER_ID, "");
        RequestParams params = new RequestParams();
        params.put("customer_id", custID);
        params.put("page", pageCount);

        mClient.addHeader("Authorization", "Bearer 73c4e5a59721c0b3a40b97a32dec873d2e09fd97");
        mClient.setMaxRetriesAndTimeout(0, 1500);
        mClient.get(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseModel = gson.fromJson(strResponse, RModel_Notifications.class);
                            Log.i("sdfsa", "onSuccess: OK" + strResponse);

                            if (responseModel.getStatusCode() == 200) {
                                notificationFragment.showResult(true, "");
                            } else {
                                notificationFragment.showResult(false, responseModel.getStatusText());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            notificationFragment.showResult(false, ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                       notificationFragment.showResult(false, error.getMessage());
                    }
                }
        );
    }

}
