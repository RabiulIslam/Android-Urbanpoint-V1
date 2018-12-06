package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.OoredooValidation;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType.PaymentProcessVodafoneSelectionActivity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Created by indus on 1/24/2017.
 */

public class Validate_WebHit_Post_Ooredoo {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static OoredooValidation ooredooValidation = null;


    public void checkValidation(final Context _context, final PaymentProcessVodafoneSelectionActivity paymentProcessVodafoneSelectionActivity,
                                final String _number) {

        String myUrl = Constants.WebServices.WS_DO_IS_OOREDOO_VALID;
        String custID = AppPreference.getSetting(_context, Constants.Request.CUSTOMER_ID, "");
//        JSONObject jsonParams = new JSONObject();
//        StringEntity entity = null;
//
//
//        try {
//            jsonParams.put(Constants.Request.OOREDOO_MSISDN, _number);
//            entity = new StringEntity(jsonParams.toString());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        RequestParams params = new RequestParams();
        params.put(Constants.Request.OOREDOO_MSISDN, _number);
        params.put(Constants.Request.CUSTOMER_ID_, custID);
//        params.put("apikey", Constants.DEFAULT_VALUES.BRITEVERIFY_KEY);
//
//        mClient.addHeader("Authorization", "Bearer 73c4e5a59721c0b3a40b97a32dec873d2e09fd97");
        Log.i("sdasfsa", "url" + myUrl);

        mClient.setMaxRetriesAndTimeout(0, 1500);
        mClient.post(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            ooredooValidation = gson.fromJson(strResponse, OoredooValidation.class);
                            AppInstance.ooredooValidation = ooredooValidation;
                            Log.i("sdasfsa", "onSuccess: OK" + strResponse);
                            if (ooredooValidation.getStatus().equals("200")) {
                                paymentProcessVodafoneSelectionActivity.showResultOoredooValidation(true, "sucess");
                            } else if (ooredooValidation.getStatus().equals("201")) {
                                paymentProcessVodafoneSelectionActivity.showResultOoredooValidation(true, ooredooValidation.getStatus());
                            } else if (ooredooValidation.getStatus().equals("409")) {
                                paymentProcessVodafoneSelectionActivity.showResultOoredooValidation(false, "User is already UnSubscribed");
                            } else if (ooredooValidation.getStatus().equals("406")) {
                                paymentProcessVodafoneSelectionActivity.showResultOoredooValidation(false, "Please enter a valid Ooredoo Qatar number");
                            } else {
                                paymentProcessVodafoneSelectionActivity.showResultOoredooValidation(false,
                                        ooredooValidation.getMessage());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            paymentProcessVodafoneSelectionActivity.showResultOoredooValidation(false, ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        paymentProcessVodafoneSelectionActivity.showResultOoredooValidation(false, error.getMessage());

                    }
                }

        );
    }

}
