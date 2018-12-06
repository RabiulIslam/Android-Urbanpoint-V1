package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.fragments.appLogin.LoginFragment;
import com.urbanpoint.UrbanPoint.views.fragments.appLogin.SignUpFragmentStepSix;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.ProfileFragment;

import org.apache.http.Header;

/**
 * Created by indus on 1/24/2017.
 */

public class SignUp_WebHit_Get_BriteVerifyEmail {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseModel = null;


    public void checkValidation(final Context _context, final SignUpFragmentStepSix signUpFragmentStepSix,
                                final String _email) {

        String myUrl = Constants.WebServices.WS_BRITE_VERIFY_EMAIL + _email + "&apikey=" + Constants.DEFAULT_VALUES.BRITEVERIFY_KEY;
//        RequestParams params = new RequestParams();
//        params.put("address", _email);
//        params.put("apikey", Constants.DEFAULT_VALUES.BRITEVERIFY_KEY);

//        mClient.addHeader("Authorization", "Bearer 73c4e5a59721c0b3a40b97a32dec873d2e09fd97");

        mClient.setMaxRetriesAndTimeout(0, 1500);
        mClient.get(myUrl, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseModel = gson.fromJson(strResponse, ResponseModel.class);
                            Log.i("sdasfsa", "onSuccess: OK" + strResponse);

                            if (responseModel.getStatus().equals(Constants.DEFAULT_VALUES.VALID)) {
                                signUpFragmentStepSix.showResultEmailValidation(true, "");
                            } else if (responseModel.getStatus().equals(Constants.DEFAULT_VALUES.UNKNOWN)) {
                                signUpFragmentStepSix.showResultEmailValidation(true, "");
                            } else {
                                signUpFragmentStepSix.showResultEmailValidation(false, responseModel.getStatus());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            signUpFragmentStepSix.showResultEmailValidation(false, ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        signUpFragmentStepSix.showResultEmailValidation(false, error.getMessage());

                    }

                }

        );
    }

    public class ResponseModel {

        private String address;

        public String getAddress() {
            return this.address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        private String account;

        public String getAccount() {
            return this.account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        private String domain;

        public String getDomain() {
            return this.domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        private String status;

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private String connected;

        public String getConnected() {
            return this.connected;
        }

        public void setConnected(String connected) {
            this.connected = connected;
        }

        private boolean disposable;

        public boolean getDisposable() {
            return this.disposable;
        }

        public void setDisposable(boolean disposable) {
            this.disposable = disposable;
        }

        private boolean role_address;

        public boolean getRoleAddress() {
            return this.role_address;
        }

        public void setRoleAddress(boolean role_address) {
            this.role_address = role_address;
        }

        private double duration;

        public double getDuration() {
            return this.duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }
    }
}
