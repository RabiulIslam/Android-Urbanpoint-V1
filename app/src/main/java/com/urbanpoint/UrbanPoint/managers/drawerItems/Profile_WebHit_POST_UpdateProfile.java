package com.urbanpoint.UrbanPoint.managers.drawerItems;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.ProfileFragment;

import org.apache.http.Header;

/**
 * Created by indus on 1/24/2017.
 */

public class Profile_WebHit_POST_UpdateProfile {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseModel = null;


    public void updateProfile(final Context _context, final ProfileFragment profileFragment,
                              final String _name, final String _email, final int _voda, final int _oredoo, final int _gender,
                              final String _nationality) {

        String myUrl = Constants.WebServices.WS_USER_UPDATE_PROFILE;
        String custID = AppPreference.getSetting(_context, Constants.Request.CUSTOMER_ID, "");
        RequestParams params = new RequestParams();
        params.put("customerid", custID);
        params.put("firstname", _name);
        params.put("emailid", _email);
        params.put("vodacustomer", _voda);
        params.put("ooredoocustomer", _oredoo);
        params.put("gender", _gender);
        params.put("nationality", _nationality);

        Log.i("addPatientAllergy", "Url: " + myUrl);
        Log.i("addPatientAllergy", "params: " + params);

//        mClient.addHeader("Authorization", AppConfig.getInstance().accessToken);
//        Log.i("addPatientAllergy", "onSuccess: " + AppConfig.getInstance().accessToken);
//        mClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
        mClient.setMaxRetriesAndTimeout(0, 1500);
        mClient.post(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseModel = gson.fromJson(strResponse, ResponseModel.class);
                            Log.d(TAG, "onSuccess: ");
                            if (responseModel.getStatus().equals(Constants.DEFAULT_VALUES.ONE + "")) {
                                Log.i("addPatientAllergy", "onSuccess: OK" + strResponse);
                                AppPreference.setSetting(_context, Constants.Request.USER_NAME, _name);
                                AppPreference.setSetting(_context, Constants.Request.EMAIL_ID, _email);
                                AppPreference.setSetting(_context, Constants.Request.GENDER,""+ _gender);
                                AppPreference.setSetting(_context, Constants.Request.NATIONALITY, _nationality);
                                AppPreference.setSetting(_context, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE,""+ _voda);
                                AppPreference.setSetting(_context, Constants.DEFAULT_VALUES.OPERATOR_TYPE_OOREDOO, ""+_oredoo);


                                profileFragment.showResult(true, responseModel.getMessage());
                            } else {
                                profileFragment.showResult(false, responseModel.getMessage());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            profileFragment.showResult(false, ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseModel = gson.fromJson(strResponse, ResponseModel.class);
                            profileFragment.showResult(false, responseModel.getMessage());
                        } catch (Exception ex) {
                            Log.i("CHECKCATCH", ex.getMessage());
                            ex.printStackTrace();
                            profileFragment.showResult(false, ex.getMessage());
                        }
                    }

                }

        );
    }

    public class ResponseModel {
        private String status;

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String message;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private boolean data;

        public boolean getData() {
            return this.data;
        }

        public void setData(boolean data) {
            this.data = data;
        }
    }
}
