package com.urbanpoint.UrbanPoint.managers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.UnSubscribeAppConfirmationFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.UnSubscribeAppEnterMobileDetailsFragment;

import org.apache.http.Header;

/**
 * Created by indus on 1/24/2017.
 */

public class UnSubscribe_WebHit_Get_Ooredoo {
    private static final String TAG = "UPDATEPROFLE";
    private AsyncHttpClient mClient = new AsyncHttpClient();
    public static ResponseModel responseModel = null;


    public void checkValidation(final Context _context, final UnSubscribeAppEnterMobileDetailsFragment unSubscribeAppEnterMobileDetailsFragment,
                                final String _number) {

        String myUrl = Constants.WebServices.WS_DO_UNSUBSCRIBE + _number;
//        RequestParams params = new RequestParams();
//        params.put("address", _number);
//        params.put("apikey", Constants.DEFAULT_VALUES.BRITEVERIFY_KEY);

//        mClient.addHeader("Authorization", "Bearer 73c4e5a59721c0b3a40b97a32dec873d2e09fd97");
        Log.i("sdasfsa", "url" + myUrl);

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
                                if (responseModel.getData().getResponse().getResponsestatus().getCode().equals("1")) {
                                    unSubscribeAppEnterMobileDetailsFragment.showResultUnSubOoredoo(true, "UnSubscribe Sucessfully");
                                } else if (responseModel.getData().getResponse().getResponsestatus().getCode().equals("-77")) {
                                    unSubscribeAppEnterMobileDetailsFragment.showResultUnSubOoredoo(false, "User is already UnSubscribed");
                                } else {
                                    unSubscribeAppEnterMobileDetailsFragment.showResultUnSubOoredoo(false,
                                            responseModel.getData().getResponse().getResponsestatus().getDescription());

                                }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            unSubscribeAppEnterMobileDetailsFragment.showResultUnSubOoredoo(false, ex.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                            error) {
                        unSubscribeAppEnterMobileDetailsFragment.showResultUnSubOoredoo(false, error.getMessage());

                    }

                }

        );
    }

    public class ResponseModel {

        public class Responsestatus {
            private String code;

            public String getCode() {
                return this.code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            private String description;

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public class Attributes {
            private String id;

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public class Attributes2 {
            private String id;

            public String getId() {
                return this.id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private String clubid;

            public String getClubid() {
                return this.clubid;
            }

            public void setClubid(String clubid) {
                this.clubid = clubid;
            }
        }

        public class Product {
            @SerializedName("@attributes")

            private Attributes2 attributes;

            public Attributes2 getAttributes() {
                return this.attributes;
            }

            public void setAttributes(Attributes2 attributes) {
                this.attributes = attributes;
            }
        }

        public class Service {
            @SerializedName("@attributes")
            private Attributes attributes;

            public Attributes getAttributes() {
                return this.attributes;
            }

            public void setAttributes(Attributes attributes) {
                this.attributes = attributes;
            }

            private Product product;

            public Product getProduct() {
                return this.product;
            }

            public void setProduct(Product product) {
                this.product = product;
            }
        }

        public class Response {
            private String msisdn;

            public String getMsisdn() {
                return this.msisdn;
            }

            public void setMsisdn(String msisdn) {
                this.msisdn = msisdn;
            }

            private String opid;

            public String getOpid() {
                return this.opid;
            }

            public void setOpid(String opid) {
                this.opid = opid;
            }

            private Responsestatus responsestatus;

            public Responsestatus getResponsestatus() {
                return this.responsestatus;
            }

            public void setResponsestatus(Responsestatus responsestatus) {
                this.responsestatus = responsestatus;
            }

            private Service service;

            public Service getService() {
                return this.service;
            }

            public void setService(Service service) {
                this.service = service;
            }
        }

        public class Data {
            private Response response;

            public Response getResponse() {
                return this.response;
            }

            public void setResponse(Response response) {
                this.response = response;
            }
        }


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

        private Data data;

        public Data getData() {
            return this.data;
        }

        public void setData(Data data) {
            this.data = data;
        }


    }
}
