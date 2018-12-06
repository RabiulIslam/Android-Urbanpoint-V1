package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.urbanpoint.UrbanPoint.dataobject.VodafoneValidation;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.UnSubscribeAppConfirmationFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.UnSubscribeAppEnterMobileDetailsFragment;

import org.apache.http.Header;

/**
 * Created by indus on 1/24/2017.
 */

public class UnSubscribe_WebHit_Post_Vodafone {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static VodafoneValidation responseModel = null;


    public void checkValidation(final Context _context, final UnSubscribeAppEnterMobileDetailsFragment unSubscribeAppEnterMobileDetailsFragment,
                                final String _number) {
        String custID = AppPreference.getSetting(_context, Constants.Request.CUSTOMER_ID, "");

        String myUrl = Constants.WebServices.WS_DO_UNSUBSCRIBE_VODAFONE + custID;

//        RequestParams params = new RequestParams();
//        params.put("address", _number);
//        params.put("apikey", Constants.DEFAULT_VALUES.BRITEVERIFY_KEY);

//        mClient.addHeader("Authorization", "Bearer 73c4e5a59721c0b3a40b97a32dec873d2e09fd97");
        Log.i("sdasfsa", "url: " + myUrl);

        mClient.setMaxRetriesAndTimeout(0, 1500);
        mClient.post(myUrl, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseModel = gson.fromJson(strResponse, VodafoneValidation.class);
                            Log.i("sdasfsa", "onSuccess: OK" + strResponse);
                            if (responseModel.getStatus()==200) {
                                unSubscribeAppEnterMobileDetailsFragment.showResultUnSubVodafone(true, "UnSubscribe Sucessfully");
                            }  else {
                                unSubscribeAppEnterMobileDetailsFragment.showResultUnSubVodafone(false,
                                        responseModel.getMessage());

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            unSubscribeAppEnterMobileDetailsFragment.showResultUnSubVodafone(false, ex.toString());
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
                            if (responseModel!=null) {
                                unSubscribeAppEnterMobileDetailsFragment.showResultUnSubVodafone(false, responseModel.getMessage());
                            }  else {
                                unSubscribeAppEnterMobileDetailsFragment.showResultUnSubVodafone(false, error.getMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            unSubscribeAppEnterMobileDetailsFragment.showResultUnSubVodafone(false, ex.toString());
                        }

                    }

                }

        );
    }
}
