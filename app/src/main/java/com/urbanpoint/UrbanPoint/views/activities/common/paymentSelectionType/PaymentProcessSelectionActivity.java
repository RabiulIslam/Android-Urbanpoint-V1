package com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.async.CommonAsyncTaskResult;
import com.urbanpoint.UrbanPoint.async.CommonAsynctask;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.PaymentProcessInfo;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.MSISDNVerificationStatusInfo;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.StripePaymentStatusInfo;
import com.urbanpoint.UrbanPoint.interfaces.ICommonCallback;
import com.urbanpoint.UrbanPoint.managers.vodafoneApiXMLParsing.SAXXMLParser;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * THIS CLASS IS NOT CURRENTLY USED .
 * IMPLEMENTED BASED ON STRIPE PAYMENT ONLY WITH NO OPERATOR (VODA,OTHERS) SELECTION.
 */

public class PaymentProcessSelectionActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private Utility utilObj;
    private TextView mDoneButton;
    private EditText mCardNumber;
    private EditText mCvvNumber;
    private EditText mExpiryDate;
    private final String STRIPE_PUBLISHABLE_KEY = "pk_test_EuEqwpyxchmU4V3ch7rWq3VM";
    private double mSelectedOfferDollarValue = 0.0;
    private String mSelectedOfferPackageID;
    private EditText mPurchaseDollarValue;
    private ImageView mBackButton;
    private TextView mVodafoneSelection;
    private TextView mOthersSelection;
    private LinearLayout mMainPaymentTypeContentLayout;
    private Button mDoOtherOperatorPaymentRequest;
    private LayoutInflater mLayoutInflater;
    private EditText mVodafoneMobileNumber;
    private EditText mVodafoneInputOTP;
    private Button mVerifyVodafoneNumber;
    private Button mDoVodafoneOperatorPaymentRequest;
    private String OTHER_OPERATOR;
    private String VODAFONE_OPERATOR;
    private String mTransactionIDForMSISDNVerification;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment_process);
        mContext = getApplicationContext();
        this.overridePendingTransition(R.anim.slide_in_top, 0);
        String dollarValue = getIntent().getStringExtra(Constants.Request.AMOUNT);
        String offerID = getIntent().getStringExtra(Constants.Request.OFFER);
        if (dollarValue != null) {
            mSelectedOfferDollarValue = Math.round(Double.parseDouble(dollarValue) * 100.00) / 100.00;
        }
        if (offerID != null) {
            mSelectedOfferPackageID = offerID;
        }

        initViews();

    }

    private void initViews() {
        utilObj = new Utility(mContext);
        mLayoutInflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        OTHER_OPERATOR = getString(R.string.others);
        VODAFONE_OPERATOR = getString(R.string.vodafone);
        bindViews();
    }

    private void bindViews() {
        setActionBar(getString(R.string.payment), false);

        mVodafoneSelection = (TextView) findViewById(R.id.vodafoneSelection);
        mVodafoneSelection.setOnClickListener(this);
        mOthersSelection = (TextView) findViewById(R.id.othersSelection);
        mOthersSelection.setOnClickListener(this);

        mMainPaymentTypeContentLayout = (LinearLayout) findViewById(R.id.mainPaymentTypeContentLayout);

        setVodafoneOperatorView();

    }

    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(this);

        title1.setText(title);
        getActionBar().setCustomView(customView);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    public void generateToken(PaymentProcessInfo form) {

        Card card = new Card(
                form.getCardNumber(),
                form.getExpMonth(),
                form.getExpYear(),
                form.getCvv());
        card.setCurrency(form.getCurrency());

        boolean validation = card.validateCard();
        if (validation) {
            utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);

            new Stripe().createToken(
                    card,
                    STRIPE_PUBLISHABLE_KEY,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            utilObj.stopiOSLoader();
                            MyApplication.getInstance().printLogs("TOKEN", "" + token);
                            //getTokenList().addToList(token);
                            //finishProgress();
                            doCreatePayment(PaymentProcessSelectionActivity.this, token.getId());
                        }

                        public void onError(Exception error) {
                            utilObj.stopiOSLoader();
                            utilObj.showCustomAlertDialog(PaymentProcessSelectionActivity.this, null, error.getLocalizedMessage(), null, null, false, null);
                            //handleError(error.getLocalizedMessage());
                            //  finishProgress();
                        }
                    });
        } else if (!card.validateNumber()) {
            utilObj.showCustomAlertDialog(this, null, getString(R.string.invalid_card_number), null, null, false, null);

        } else if (!card.validateExpiryDate()) {
            utilObj.showCustomAlertDialog(this, null, getString(R.string.invalid_expiration_date), null, null, false, null);

        } else if (!card.validateCVC()) {
            utilObj.showCustomAlertDialog(this, null, getString(R.string.invalid_cvv_number), null, null, false, null);

        } else {
            utilObj.showCustomAlertDialog(this, null, getString(R.string.invalid_card_details), null, null, false, null);
        }
    }

    private PaymentProcessInfo createEnteredPaymentInfo() {
        PaymentProcessInfo paymentProcessInfo = new PaymentProcessInfo();
        paymentProcessInfo.setCardNumber(mCardNumber.getText().toString());
        paymentProcessInfo.setCvv(mCvvNumber.getText().toString());
        String date = mExpiryDate.getText().toString();
        String[] split = date.split("/");
        paymentProcessInfo.setExpMonth(Integer.parseInt(split[0]));
        paymentProcessInfo.setExpYear(Integer.parseInt(split[1]));
        paymentProcessInfo.setCurrency(getString(R.string.usd));
        return paymentProcessInfo;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.vodafoneSelection:
                setVodafoneOperatorView();
                break;
            case R.id.othersSelection:
                setOtherOperatorView();
                break;
            case R.id.doVodafoneOperatorPaymentRequest:
                doProcessForVodafoneOperator(getString(R.string.submit));
                break;
            case R.id.doOtherOperatorPaymentRequest:
                doProcessForOtherOperator();
                break;
            case R.id.verifyVodafoneNumber:
                doProcessForVodafoneOperator(getString(R.string.verify));
                break;
        }
    }


    /**
     * The method is used to validate the
     * required fields
     *
     * @return boolean true if fields are validated else false
     **/

    private boolean validatingRequired() {
        String message = "";

        // FOR OTHER OPERATOR VALIDATION
        String cardNumber = mCardNumber.getText().toString();
        String cvvNumber = mCvvNumber.getText().toString();
        String expiryDate = mExpiryDate.getText().toString();

        //validate the content
        if (cardNumber.trim().length() == 0) {
            message = getResources().getString(R.string.cardNumberErrorMessage);
            //utilObj.showError(this, message, textViewObj, emailObj);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        } else if (!expiryDate.contains("/")) {
            message = getResources().getString(R.string.date_error);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        } else if (cvvNumber.trim().length() == 0) {
            message = getResources().getString(R.string.cVVNumberErrorMessage);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }


    public boolean doCreatePayment(Activity context, String token) {

        final String key = "doCreatePayment";

        String jsonParam = Constants.BLANK;
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
//            jsonObject.put(Constants.Request.AMOUNT, "" + mSelectedOfferDollarValue);
            jsonObject.put(Constants.Request.CUSTOMER_ID_, custID);
            jsonObject.put(Constants.Request.STRIPE_TOKEN, token);
            jsonObject.put(Constants.Request.SUBSCRIPTION_ID, mSelectedOfferPackageID);
            //   jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);
            jsonParam = jsonObject.toString();

        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        new CommonAsynctask(new ICommonCallback() {
            @Override
            public void onTaskDone(Bundle result, CommonAsyncTaskResult taskResult) {

                if (taskResult == CommonAsyncTaskResult.OK) {

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(
                                new JSONTokener(result.getString(key)));
                        Gson gson = new GsonBuilder().create();
                        if (jsonObj != null) {
                            StripePaymentStatusInfo stripePaymentStatusInfo = gson.fromJson(jsonObj.toString(), StripePaymentStatusInfo.class);

                            if (stripePaymentStatusInfo.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                                utilObj.showCustomAlertDialog(PaymentProcessSelectionActivity.this, null, stripePaymentStatusInfo.getMessage(), null, null, false, null);
                            } else {
//                                utilObj.showCustomAlertDialog(PaymentProcessSelectionActivity.this, "You are now an Urban Point member!", "Enjoy access to all our offers, which you can use again every month!", null, null, false, null);
                                doSaveTransactionDetails(PaymentProcessSelectionActivity.this, stripePaymentStatusInfo.getTransactionId());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, context, key, jsonParam, true, true, null, false, null).execute(Constants.WebServices.WS_CREATE_PAYMENT);

        return false;
    }

    public boolean doSaveTransactionDetails(Activity context, String transactionID) {

        final String key = "doSaveTransactionDetails";

        String jsonParam = Constants.BLANK;
        try {
            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.CustomerJSONKeys.CUSTOMER_PARAM, "" + custID);
            jsonObject.put(Constants.DEFAULT_VALUES.STARTDATE, utilObj.getCurrentDate());
            jsonObject.put(Constants.Request.TRANSACTION_ID, transactionID);
            jsonObject.put(Constants.Request.OFFER, mSelectedOfferPackageID);
            jsonParam = jsonObject.toString();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        new CommonAsynctask(new ICommonCallback() {
            @Override
            public void onTaskDone(Bundle result, CommonAsyncTaskResult taskResult) {

                if (taskResult == CommonAsyncTaskResult.OK) {

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(
                                new JSONTokener(result.getString(key)));
                        Gson gson = new GsonBuilder().create();
                        if (jsonObj != null) {

                            MyApplication.getInstance().printLogs("RESPONSE", jsonObj.toString());
                            finish();

                            AppInstance.setIsSubscriptionCheckCalled(false);

                            Intent intentObj = new Intent(PaymentProcessSelectionActivity.this, DashboardActivity.class);

                            intentObj.putExtra(Constants.DEFAULT_VALUES.PURCHASE_DONE, Constants.DEFAULT_VALUES.SUCCESS);
                            //------------
                            int selectedID = Integer.parseInt(mSelectedOfferPackageID);

                            switch (selectedID) {
                                case 1:
                                    intentObj.putExtra(Constants.Request.OFFER, Constants.DEFAULT_VALUES.OfferSubScriptionsPackagesDetails.OFFER_ID_ONE);
                                    break;
                                case 2:
                                    intentObj.putExtra(Constants.Request.OFFER, Constants.DEFAULT_VALUES.OfferSubScriptionsPackagesDetails.OFFER_ID_TWO);
                                    break;
                                case 3:
                                    intentObj.putExtra(Constants.Request.OFFER, Constants.DEFAULT_VALUES.OfferSubScriptionsPackagesDetails.OFFER_ID_THREE);
                                    break;
                            }

                            //---------------

                            intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intentObj);

                            /*
                            String status = jsonObj.getString(Constants.Request.STATUS);
                            String message = jsonObj.getString(Constants.Request.MESSAGE);

                            if (status.equalsIgnoreCase(Constants.DEFAULT_VALUES.ONE)) {
                                utilObj.showCustomAlertDialog(PaymentProcessActivity.this, null, message, null, null, false, confirmationInterfaces);
                            }*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, context, key, jsonParam, true, true, null, false, null).execute(Constants.WebServices.WS_CREATED_OTHER_PAYMENT_SAVE_DETAILS);

        return false;
    }


    // -------- Vodafone operator working -- Start

    private void setVodafoneOperatorView() {
        mOthersSelection.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_color));
        mOthersSelection.setTextColor(ContextCompat.getColor(this, R.color.white));

        mVodafoneSelection.setBackgroundColor(Color.WHITE);
        mVodafoneSelection.setTextColor(ContextCompat.getColor(this, R.color.orange));

        mMainPaymentTypeContentLayout.removeAllViews();
        View vodafone = mLayoutInflater.inflate(R.layout.fragment_enter_mobile_info, null);

        mPurchaseDollarValue = (EditText) vodafone.findViewById(R.id.purchaseDollarValue);
        mPurchaseDollarValue.setText(getString(R.string.symbol_dollar) + mSelectedOfferDollarValue);

        mVodafoneMobileNumber = (EditText) vodafone.findViewById(R.id.vodafoneMobileNumber);

        mVodafoneInputOTP = (EditText) vodafone.findViewById(R.id.vodafoneInputOTP);

        mVerifyVodafoneNumber = (Button) vodafone.findViewById(R.id.verifyVodafoneNumber);
        mVerifyVodafoneNumber.setOnClickListener(this);

        mDoVodafoneOperatorPaymentRequest = (Button) vodafone.findViewById(R.id.doVodafoneOperatorPaymentRequest);
        mDoVodafoneOperatorPaymentRequest.setOnClickListener(this);

        mMainPaymentTypeContentLayout.addView(vodafone);
    }


    private void doProcessForVodafoneOperator(String operationType) {

        if (operationType.equalsIgnoreCase(getString(R.string.verify))) {
            String mobileNumber = mVodafoneMobileNumber.getText().toString();
            //validate the content
            if (mobileNumber.trim().length() == 0) {
                utilObj.showCustomAlertDialog(this, null, getResources().getString(R.string.enter_mobile_number), null, null, false, null);
            } else {
                doVerifyMobileNumberAndGenerateOTP(this, mVodafoneMobileNumber.getText().toString());
            }

        } else if (operationType.equalsIgnoreCase(getString(R.string.submit))) {

            // FOR VODAFONE OPERATOR VALIDATION
            String otp = mVodafoneInputOTP.getText().toString();
            if (otp.trim().length() == 0) {
                utilObj.showCustomAlertDialog(this, null, getResources().getString(R.string.enter_otp), null, null, false, null);
            } else {
                doConfirmSubscription(this, otp, mTransactionIDForMSISDNVerification);
            }

        }
    }
    // -------- Vodafone operator working -- End

    // -------- Other operator working -- Start
    private void setOtherOperatorView() {

        mVodafoneSelection.setBackgroundColor(ContextCompat.getColor(this, R.color.tab_color));
        mVodafoneSelection.setTextColor(ContextCompat.getColor(this, R.color.white));

        mOthersSelection.setBackgroundColor(Color.WHITE);
        mOthersSelection.setTextColor(ContextCompat.getColor(this, R.color.orange));

        mMainPaymentTypeContentLayout.removeAllViews();
        View others = mLayoutInflater.inflate(R.layout.fragment_enter_payment_info, null);

        mPurchaseDollarValue = (EditText) others.findViewById(R.id.purchaseDollarValue);
        mPurchaseDollarValue.setText(getString(R.string.symbol_dollar) + mSelectedOfferDollarValue);

        mCardNumber = (EditText) others.findViewById(R.id.cardNumber);
        // mExpiryDate = (EditText) others.findViewById(R.id.expiryDate);
        mCvvNumber = (EditText) others.findViewById(R.id.cvvNumber);

        mDoOtherOperatorPaymentRequest = (Button) others.findViewById(R.id.doOtherOperatorPaymentRequest);
        mDoOtherOperatorPaymentRequest.setOnClickListener(this);

        mMainPaymentTypeContentLayout.addView(others);
    }

    private void doProcessForOtherOperator() {
        if (validatingRequired()) {
            PaymentProcessInfo enteredPaymentInfo = createEnteredPaymentInfo();
            generateToken(enteredPaymentInfo);
        }
    }
    // -------- Other operator working -- End

    public boolean doVerifyMobileNumberAndGenerateOTP(Activity context, String mobileNumber) {

        final String key = "doVerifyMobileNumberAndGenerateOTP";

        String userName = Constants.Request.USERNAME + "=" + "USERNMAE" + "&";
        final String password = Constants.Request.PASSWORD + "=" + "PASSWORD" + "&";
        String serviceID = Constants.Request.SERVICE_ID + "=" + "USERNMAE" + "&";
        String actionType = Constants.Request.ACTION_TYPE + "=" + Constants.DEFAULT_VALUES.ZERO + "&";
        String enteredMobileNumber = Constants.Request.MSISDN + "=" + mobileNumber;

        String url = Constants.WebServices.VODAFONE_API_BASE_URL + userName + password + serviceID + actionType + enteredMobileNumber;

        new CommonAsynctask(new ICommonCallback() {
            @Override
            public void onTaskDone(Bundle result, CommonAsyncTaskResult taskResult) {

                if (taskResult == CommonAsyncTaskResult.OK) {
                    try {
                        List<MSISDNVerificationStatusInfo> parse = SAXXMLParser.parse(getAssets().open("msisdn_verify_response.xml"));
                        if (parse != null) {
                            if (parse.size() > 0) {
                                MSISDNVerificationStatusInfo msisdnVerificationStatusInfo = parse.get(0);
                                mTransactionIDForMSISDNVerification = msisdnVerificationStatusInfo.getTransactionID();
                            }
                        }
                        MyApplication.getInstance().printLogs("RESPONSE", "" + parse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, context, key, "", true, false, null, false, null).execute(url);

        return false;
    }

    public boolean doConfirmSubscription(Activity context, String pin, String transctionID) {

        final String key = "doConfirmSubscription";

        String userName = Constants.Request.USERNAME + "=" + "USERNMAE" + "&";
        String password = Constants.Request.PASSWORD + "=" + "PASSWORD" + "&";
        String serviceID = Constants.Request.PIN + "=" + pin + "&";
        String actionType = Constants.Request.ACTION_TYPE + "=" + Constants.DEFAULT_VALUES.ONE + "&";
        String tranxID = Constants.Request.TRANSACTION_ID + "=" + transctionID;

        String url = Constants.WebServices.VODAFONE_API_BASE_URL + userName + password + serviceID + actionType + tranxID;

        new CommonAsynctask(new ICommonCallback() {
            @Override
            public void onTaskDone(Bundle result, CommonAsyncTaskResult taskResult) {

                if (taskResult == CommonAsyncTaskResult.OK) {
                    try {
                        List<MSISDNVerificationStatusInfo> parse = SAXXMLParser.parse(getAssets().open("msisdn_confirm_response.xml"));
                        if (parse != null) {
                            if (parse.size() > 0) {
                                MSISDNVerificationStatusInfo statusInfo = parse.get(0);
                                switch (statusInfo.getStatusCode()) {
                                    case Constants.ResponseCodes.CODE_001:
                                        doSaveTransactionDetails(PaymentProcessSelectionActivity.this, statusInfo.getTransactionID());
                                        break;
                                    case Constants.ResponseCodes.CODE_002:
                                        break;
                                    case Constants.ResponseCodes.CODE_003:
                                        utilObj.showCustomAlertDialog(PaymentProcessSelectionActivity.this, null, getResources().getString(R.string.subscription_failed_try_again), null, null, false, null);
                                        break;
                                    case Constants.ResponseCodes.CODE_005:
                                        utilObj.showCustomAlertDialog(PaymentProcessSelectionActivity.this, null, getResources().getString(R.string.entered_wrong_otp), null, null, false, null);
                                        break;
                                    case Constants.ResponseCodes.CODE_008:
                                    case Constants.ResponseCodes.CODE_009:
                                    case Constants.ResponseCodes.CODE_010:
                                        utilObj.showCustomAlertDialog(PaymentProcessSelectionActivity.this, null, getResources().getString(R.string.invalid_message), null, null, false, null);
                                        break;
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


}
