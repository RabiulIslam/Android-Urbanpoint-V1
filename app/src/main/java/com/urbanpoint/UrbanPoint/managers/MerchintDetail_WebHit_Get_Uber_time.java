package com.urbanpoint.UrbanPoint.managers;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.common.MerchantDetailActivity;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * Created by indus on 1/24/2017.
 */

public class MerchintDetail_WebHit_Get_Uber_time {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseModel = null;


    public void getUberTime(final MerchantDetailActivity merchantDetailActivity,
                            double start_lat, double start_lng, double end_lat, double end_lng) {

        String myUrl = Constants.WebServices.WS_UBER_TIME_ESTIMATION;

        RequestParams params = new RequestParams();
        params.put("start_latitude", start_lat);
        params.put("start_longitude", start_lng);
        params.put("end_latitude", end_lat);
        params.put("end_longitude", end_lng);

        mClient.addHeader("Authorization", "Token " + Constants.UberRideEstimate.SERVER_TOKEN);
        mClient.addHeader("Accept-Language", "en_US");
        mClient.setMaxRetriesAndTimeout(0, 1500);
        mClient.get(myUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String strResponse;
                        try {
                            Gson gson = new Gson();
                            strResponse = new String(responseBody, "UTF-8");
                            responseModel = gson.fromJson(strResponse, ResponseModel.class);
                            Log.i("sdfsa", "onSuccess: OK" + strResponse);

                            if (statusCode == 200) {
                                merchantDetailActivity.showResultTime(true, "");
                            } else {
                                merchantDetailActivity.showResultTime(false, "");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            merchantDetailActivity.showResultTime(false, ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        merchantDetailActivity.showResultTime(false, error.getMessage());
                    }
                }
        );
    }

    public class ResponseModel {

        public class Time {
            private String localized_display_name;

            public String getLocalizedDisplayName() {
                return this.localized_display_name;
            }

            public void setLocalizedDisplayName(String localized_display_name) {
                this.localized_display_name = localized_display_name;
            }

            private int estimate;

            public int getEstimate() {
                return this.estimate;
            }

            public void setEstimate(int estimate) {
                this.estimate = estimate;
            }

            private String display_name;

            public String getDisplayName() {
                return this.display_name;
            }

            public void setDisplayName(String display_name) {
                this.display_name = display_name;
            }

            private String product_id;

            public String getProductId() {
                return this.product_id;
            }

            public void setProductId(String product_id) {
                this.product_id = product_id;
            }
        }

        private ArrayList<Time> times;

        public ArrayList<Time> getTimes() {
            return this.times;
        }

        public void setTimes(ArrayList<Time> times) {
            this.times = times;
        }


    }

}
