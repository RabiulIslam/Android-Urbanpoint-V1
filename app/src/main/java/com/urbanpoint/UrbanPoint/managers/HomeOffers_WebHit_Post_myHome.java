package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_HomeOffers;
import com.urbanpoint.UrbanPoint.dataobject.VodafoneValidation;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.UnSubscribeAppEnterMobileDetailsFragment;

import org.apache.http.Header;

import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.FETCH_HOME_OFFERS_TASK_ID2;

/**
 * Created by indus on 1/24/2017.
 */

public class HomeOffers_WebHit_Post_myHome {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static VodafoneValidation responseModel = null;
    Intent intentHomeOfffers = new Intent(Constants.MY_BROADCAST_INTENT_HOME_OFFERS);


    public void fetchHomeOffers(final Context _context) {
        String custID = AppPreference.getSetting(_context, Constants.Request.CUSTOMER_ID, "");

        String myUrl = Constants.WebServices.WS_DO_FETCH_HOME_OFFERS;

        RequestParams params = new RequestParams();
        params.put(Constants.Request.CUSTOMER_ID_, custID);
//        params.put("apikey", Constants.DEFAULT_VALUES.BRITEVERIFY_KEY);

//        mClient.addHeader("Authorization", "Bearer 73c4e5a59721c0b3a40b97a32dec873d2e09fd97");
        Log.i("sdasfsa", "url: " + myUrl);
        Log.i("sdasfsa", "url: " + params);

        mClient.setMaxRetriesAndTimeout(0, 1500);
        mClient.post(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            Gson gson = new GsonBuilder().create();
                            String strData = "";
                            strData = new String(responseBody, "UTF-8");
                            Log.d("asdasd", "onResult:data is " + strData);
                            RModel_HomeOffers rModel_homeOffers = gson.fromJson(strData, RModel_HomeOffers.class);
                            AppInstance.rModel_homeOffers = rModel_homeOffers;
                            if (rModel_homeOffers != null) {
                                if (Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(rModel_homeOffers.getStatus())) {
                                    FlurryAgent.onError(FETCH_HOME_OFFERS_TASK_ID2 + "", rModel_homeOffers.getStatus(), "");
                                    FlurryAgent.logEvent(rModel_homeOffers.getStatus());
                                    intentHomeOfffers.putExtra(Constants.DEFAULT_VALUES.BROADCAST_STATUS, "0");
                                    _context.sendBroadcast(intentHomeOfffers);
                                    AppInstance.isBroadCastHomeCompleted = true;
                                    Log.d("asdasd", "onResult: failed0");
                                } else {
                                    AppPreference.setSetting(_context, "key_home_offers", strData);
                                    intentHomeOfffers.putExtra(Constants.DEFAULT_VALUES.BROADCAST_STATUS, "1");
                                    _context.sendBroadcast(intentHomeOfffers);
                                    AppInstance.isBroadCastHomeCompleted = true;
                                    Log.d("asdasd2", "onResult: Sucess");
                                }
                            } else {
                                AppInstance.isBroadCastHomeCompleted = true;
                                Log.d("asdasd", "onResult: failed1");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            FlurryAgent.logEvent(e.getMessage());
                            FlurryAgent.onError(FETCH_HOME_OFFERS_TASK_ID2 + "", "", e.getMessage());
                            intentHomeOfffers.putExtra(Constants.DEFAULT_VALUES.BROADCAST_STATUS, "0");
                            _context.sendBroadcast(intentHomeOfffers);
                            AppInstance.isBroadCastHomeCompleted = true;
                            Log.d("asdasd", "onResult: exception");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        AppInstance.isBroadCastHomeCompleted = true;
                        intentHomeOfffers.putExtra(Constants.DEFAULT_VALUES.BROADCAST_STATUS, "0");
                        _context.sendBroadcast(intentHomeOfffers);
                        Log.d("asdasd", "onResult: failed2");
                    }

                }

        );
    }
}
