package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.OoredooValidation;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType.PaymentProcessVodafoneSelectionActivity;

import org.apache.http.Header;

/**
 * Created by indus on 1/24/2017.
 */

public class Validate_WebHit_Post_Vodafone {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel rObject_VodaValidation = null;


    public void checkValidation(final Context _context, final PaymentProcessVodafoneSelectionActivity paymentProcessVodafoneSelectionActivity,
                                final String _number) {

        String myUrl = Constants.WebServices.WS_DO_IS_VODAFONE_VALID;

        String custID = AppPreference.getSetting(_context, Constants.Request.CUSTOMER_ID, "");
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
        params.put(Constants.Request.VODAFONE_MSISDN, _number);
        params.put(Constants.Request.CUSTOMER_ID_, custID);
        params.put("newuser", IsNewUser);
//        params.put("apikey", Constants.DEFAULT_VALUES.BRITEVERIFY_KEY);
//
//        mClient.addHeader("Authorization", "Bearer 73c4e5a59721c0b3a40b97a32dec873d2e09fd97");
        Log.i("sdasfsasas", "url" + myUrl);
        Log.i("sdasfsasas", "url" + params);

        mClient.setMaxRetriesAndTimeout(0, 1500);
        mClient.post(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            Log.i("sdasfsasas", "onSuccess: OK" + strResponse);
                            rObject_VodaValidation = gson.fromJson(strResponse, ResponseModel.class);

//                            Log.i("sdasfsasas", "onSuccess: OK" + strResponse);
                            if (rObject_VodaValidation.getStatus() == 200) {
                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(true,
                                        rObject_VodaValidation.getSubscriptionContractId());
                            } else if (rObject_VodaValidation.getStatus() == 409) {
                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false, rObject_VodaValidation.getMessage());
//                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false, "User is already UnSubscribed");
                            } else if (rObject_VodaValidation.getStatus() == 406) {
                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false, rObject_VodaValidation.getMessage());
//                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false,"Please enter a valid Vodafone Qatar number");
                            } else {
                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false,
                                        rObject_VodaValidation.getMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false, ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {

                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            Log.i("sdasfsasas", "onSuccess: OK" + strResponse);
                            rObject_VodaValidation = gson.fromJson(strResponse, ResponseModel.class);

                            if (rObject_VodaValidation.getStatus() == 409) {
                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false, rObject_VodaValidation.getMessage());
//                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false, "User is already UnSubscribed");
                            } else if (rObject_VodaValidation.getStatus() == 406) {
                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false, rObject_VodaValidation.getMessage());
//                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false,"Please enter a valid Vodafone Qatar number");
                            } else {
                                paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false,
                                        rObject_VodaValidation.getMessage());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            paymentProcessVodafoneSelectionActivity.showResultVodafoneValidation(false, ex.toString());
                        }

                    }

                }

        );
    }

    class ResponseModel {
        private int status;

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        private String message;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String subscriptionContractId;

        public String getSubscriptionContractId() {
            return this.subscriptionContractId;
        }

        public void setSubscriptionContractId(String subscriptionContractId) {
            this.subscriptionContractId = subscriptionContractId;
        }
    }
}
