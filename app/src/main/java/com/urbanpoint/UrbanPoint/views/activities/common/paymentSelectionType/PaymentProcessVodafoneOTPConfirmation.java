package com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.async.CommonAsyncTaskResult;
import com.urbanpoint.UrbanPoint.async.CommonAsynctask;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.MSISDNVerificationStatusInfo;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ICommonCallback;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.PaymentManager;
import com.urbanpoint.UrbanPoint.managers.vodafoneApiXMLParsing.SAXXMLParser;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.PackageSubscriptionSucessActivity;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_RESEND_OOREDOO_VALIDATION_CODE;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_RESEND_VODAFONE_VALIDATION_CODE;


public class PaymentProcessVodafoneOTPConfirmation extends Activity implements View.OnClickListener, ServiceRedirection {
    private Context mContext;
    private Utility utilObj;
    private String mReceivedTranscationID;

    private ImageView mBackButton;
    private String enteredOTPNumber = "";
    private Button mDoVodafoneOTPSubmit;
    private PinEntryView mVodafoneOTPEntry;
    private TextView mDoResendOTP;
    private String mMSISDN;
    private String mTransactionIDForMSISDNVerification;
    private LinearLayout mMainParentLayout;
    private TextView txvTryAgain, txvFree1stMonth;
    private String Msisdn;
    private static String transactionId;
    private boolean isOoredoo;
    private String mCustomerid, mCatId, mMerchantID, mProductid, mName, mPhoneNumber;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_voda_subscription_enter_otp);
        mContext = getApplicationContext();
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        //        this.overridePendingTransition(R.anim.slide_in_top, 0);
//        String tranxID = getIntent().getStringExtra(Constants.Request.TRANSACTION_ID);


        Log.d("HSDSDS", "onCreate: " + transactionId);
        //        if (tranxID != null) {
//            mReceivedTranscationID = tranxID;
//        }
//        if (transactionId != null) {
//            mMSISDN = transactionId;
//        }

        initViews();

    }

    private void initViews() {
        utilObj = new Utility(mContext);
        Bundle m_buBundle = getIntent().getExtras();
        if (m_buBundle != null) {
            mCustomerid = m_buBundle.getString(Constants.Request.CUSTOMER_ID);
            mProductid = m_buBundle.getString(Constants.Request.OFFER_ID);
            mName = m_buBundle.getString(Constants.Request.NAME);
            mCatId = m_buBundle.getString(Constants.Request.CATEGORYID);
            transactionId = m_buBundle.getString(Constants.Request.TRANSACTION_ID);
            isOoredoo = m_buBundle.getBoolean("key_customer_type");
            Msisdn = m_buBundle.getString(Constants.Request.OOREDOO_MSISDN);
//            mMerchantID = m_buBundle.getString(Constants.OfferDetails.MERCHANT_ID);
            mMerchantID = "";
            setActionBar(mName, true);
            Log.d("LOGOO", "initViews: mID " + mMerchantID + " and CID" + mCustomerid + " And name: " + mName);
        }

        MyApplication.getInstance().trackScreenView(getString(R.string.ga_susbsciption_screen_two));
        bindViews();
    }

    private void bindViews() {
        setActionBar(getString(R.string.activation_code), false);
        txvTryAgain = (TextView) findViewById(R.id.frg_voda_subscription_cnfrm_txv_try_again);
        txvTryAgain.setOnClickListener(this);
        txvFree1stMonth = (TextView) findViewById(R.id.istFreeOffertxv);

        long no = AppPreference.getSettingResturnsLong(mContext, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);
        if (no == 3) {
            Log.d("dfsfdfasf", "bindViews: V ");
            txvFree1stMonth.setVisibility(View.VISIBLE);
        } else {
            Log.d("dfsfdfasf", "bindViews: G ");
            txvFree1stMonth.setVisibility(View.INVISIBLE);
        }
        mMainParentLayout = (LinearLayout) findViewById(R.id.mainVodaParentLayout);
        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                return false;
            }
        });

        mDoResendOTP = (TextView) findViewById(R.id.doResendOTP);
        mDoResendOTP.setOnClickListener(this);

        mDoVodafoneOTPSubmit = (Button) findViewById(R.id.doVodafoneOTPSubmit);
        mDoVodafoneOTPSubmit.setOnClickListener(this);

        mVodafoneOTPEntry = (PinEntryView) findViewById(R.id.vodafoneOTPEntry);

        mVodafoneOTPEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredOTPNumber = pin;
                utilObj.keyboardClose(mContext, mVodafoneOTPEntry);


            }
        });
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSlideButton.toggle(true);
                finish();
            }
        });

        title1.setText(title);
        getActionBar().setCustomView(customView);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void onClick(View v) {
        PaymentManager paymentManager = new PaymentManager(mContext, this);

        switch (v.getId()) {
            case R.id.doVodafoneOTPSubmit:
                if (isOoredoo) {
                    if (enteredOTPNumber.length() == 6 &&
                            enteredOTPNumber.equals(transactionId)) {
                        Log.d("HSDSDS", "Verfied pin ");
                        if (NetworkUtils.isConnected(mContext)) {
                            utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.MSISDN_ID, Msisdn);
                            paymentManager.doSubscribeOoredoo(Msisdn);
                        } else {
                            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                        }
                    } else {
                        mVodafoneOTPEntry.clearText();
                        utilObj.showToast(this, getResources().getString(R.string.pin_does_not_match), 1);
                    }
                } else {
                    if (enteredOTPNumber.length() == 6) {
                        if (NetworkUtils.isConnected(mContext)) {
                            utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.MSISDN_ID, Msisdn);
                            Log.d("Assadsa", "onClick ID: " + transactionId + " enterd No: " + enteredOTPNumber);
                            paymentManager.doSubscribeVodafone(transactionId, enteredOTPNumber);

                        } else {
                            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                        }
                    } else {
                        mVodafoneOTPEntry.clearText();
                        utilObj.showToast(this, getResources().getString(R.string.pin_does_not_match), 1);
                    }
                }
                break;
            case R.id.frg_voda_subscription_cnfrm_txv_try_again:
                Log.d("HSDSDS", "MSisdn is" + Msisdn);
                if (NetworkUtils.isConnected(mContext)) {
                    utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                    if (isOoredoo) {
                        paymentManager.doResendValidationCode(Msisdn);
                    } else {
                        Log.d("Assadsa", "onClick ID: " + transactionId + " enterd No: " + enteredOTPNumber);
                        paymentManager.doResendVodafoneValidationCode(transactionId);
                    }
                } else {
                    utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                }
                break;
            case R.id.doResendOTP:
                doResendVerifyMobileNumberAndGenerateOTP(this, mMSISDN);
        }

    }

    /**
     * The method is used to validate the required fields
     *
     * @return boolean true if fields are validated else false
     **/

    private boolean validatingRequired() {
        String message = "";
        String enteredPhone = enteredOTPNumber;

        //validate the content
        if (enteredPhone.length() < 4) {
            message = getResources().getString(R.string.enter_otp);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }

    public boolean doResendVerifyMobileNumberAndGenerateOTP(Activity context, final String mobileNumber) {

        final String key = "doResendVerifyMobileNumberAndGenerateOTP";

        String userName = Constants.Request.USERNAME + "=" + Constants.API_EGYPTLINX_PARAMS_VALUES.USERNAME + "&";
        final String password = Constants.Request.PASSWORD + "=" + Constants.API_EGYPTLINX_PARAMS_VALUES.PASSWORD + "&";
        String serviceID = Constants.Request.SERVICE_ID + "=" + Constants.API_EGYPTLINX_PARAMS_VALUES.SERVICE_ID + "&";
        String actionType = Constants.Request.ACTION_TYPE + "=" + Constants.DEFAULT_VALUES.ZERO + "&";
        String enteredMobileNumber = Constants.Request.MSISDN + "=" + mobileNumber;

        String url = Constants.WebServices.VODAFONE_API_BASE_URL + userName + password + serviceID + actionType + enteredMobileNumber;

        new CommonAsynctask(new ICommonCallback() {
            @Override
            public void onTaskDone(Bundle result, CommonAsyncTaskResult taskResult) {

                if (taskResult == CommonAsyncTaskResult.OK) {
                    try {

                        String responseData = result.getString(key);

                        if (responseData != null) {
                            InputStream in = new ByteArrayInputStream(responseData.getBytes("UTF-8"));
                            List<MSISDNVerificationStatusInfo> parse = SAXXMLParser.parse(in);
                            if (parse != null) {
                                if (parse.size() > 0) {
                                    MSISDNVerificationStatusInfo msisdnVerificationStatusInfo = parse.get(0);

                                    switch (msisdnVerificationStatusInfo.getStatusCode()) {
                                        case Constants.ResponseCodes.CODE_001:
                                            mTransactionIDForMSISDNVerification = msisdnVerificationStatusInfo.getTransactionID();
                                            break;
                                        case Constants.ResponseCodes.CODE_008:
                                            utilObj.showCustomAlertDialog(PaymentProcessVodafoneOTPConfirmation.this, null, getResources().getString(R.string.invalid_message), null, null, false, null);
                                            break;
                                        case Constants.ResponseCodes.CODE_007:
                                            utilObj.showCustomAlertDialog(PaymentProcessVodafoneOTPConfirmation.this, null, getResources().getString(R.string.max_sending_otp_reached), null, null, false, null);
                                            break;
                                    }
                                }
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, context, key, "", true, false, null, false, null).execute(url);

        return false;
    }


    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();
        if (taskID == Constants.TaskID.DO_CHECK_OOREDOO_VALIDATION) {
            Log.d("HSDSDS", "onSuccessRedirection: ");
            utilObj.showToast(this, getResources().getString(R.string.request_send), 1);

        } else if (taskID == Constants.TaskID.OOREDOO_SUBSCRIBE_TASK_ID) {
            Log.d("HSDSDS", "onSuccessRedirectionOF SUBSCRIPTION: ");
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "true");
            AppInstance.getAppInstance().setIsUserSubscribed(true);
            AppInstance.canUserUnSubscribe = true;
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 1);
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, true);
            Intent intentObj = null;


            intentObj = new Intent(PaymentProcessVodafoneOTPConfirmation.this, PackageSubscriptionSucessActivity.class);

            Bundle m_bundle = new Bundle();
            m_bundle.putString(Constants.Request.CUSTOMER_ID, mCustomerid);
            m_bundle.putString(Constants.Request.OFFER_ID, mProductid);
            m_bundle.putString(Constants.Request.NAME, mName);
            m_bundle.putString(Constants.Request.CATEGORYID, mCatId);
            m_bundle.putString(Constants.DEFAULT_VALUES.PURCHASE_DONE, Constants.DEFAULT_VALUES.SUCCESS);
            intentObj.putExtras(m_bundle);
            intentObj.putExtra("PaymentMethod", "MobileServices");
            startActivity(intentObj);
            this.finish();

        } else if (taskID == Constants.TaskID.DO_SUBSCRIBE_VODAFONE_TASK_ID) {
            Log.d("HSDSDS", "onSuccessRedirectionOF SUBSCRIPTION: ");
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "true");
            AppInstance.getAppInstance().setIsUserSubscribed(true);
            AppInstance.canUserUnSubscribe = true;
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 1);
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, true);
            Intent intentObj = null;
            intentObj = new Intent(PaymentProcessVodafoneOTPConfirmation.this, PackageSubscriptionSucessActivity.class);
            Bundle m_bundle = new Bundle();
            m_bundle.putString(Constants.Request.CUSTOMER_ID, mCustomerid);
            m_bundle.putString(Constants.Request.OFFER_ID, mProductid);
            m_bundle.putString(Constants.Request.NAME, mName);
            m_bundle.putString(Constants.Request.CATEGORYID, mCatId);
            intentObj.putExtra("PaymentMethod", "MobileServices");
            m_bundle.putString(Constants.DEFAULT_VALUES.PURCHASE_DONE, Constants.DEFAULT_VALUES.SUCCESS);
            intentObj.putExtras(m_bundle);
            startActivity(intentObj);
            this.finish();

        } else if (taskID == DO_RESEND_OOREDOO_VALIDATION_CODE) {
            mVodafoneOTPEntry.clearText();
            transactionId = AppInstance.ooredooValidation.getData();
            utilObj.showToast(mContext, "Code Sent", Toast.LENGTH_LONG);

        } else if (taskID == DO_RESEND_VODAFONE_VALIDATION_CODE) {
            mVodafoneOTPEntry.clearText();
            Log.d("Assadsa", "onRESEND ID: " + AppInstance.ooredooValidation.getData());
//            transactionId = AppInstance.ooredooValidation.getData();
            utilObj.showToast(mContext, "Code Sent", Toast.LENGTH_LONG);

        } else {
            utilObj.showToast(this, getResources().getString(R.string.no_data_received), 1);
        }

    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        Log.d("asdad", "onFailureRedirection:OTP ");
        mVodafoneOTPEntry.clearText();
        utilObj.stopiOSLoader();
        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
    }
}
