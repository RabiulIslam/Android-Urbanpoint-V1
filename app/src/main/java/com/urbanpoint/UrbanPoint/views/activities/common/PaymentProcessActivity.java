package com.urbanpoint.UrbanPoint.views.activities.common;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.StripePaymentStatusInfo;
import com.urbanpoint.UrbanPoint.interfaces.ICommonCallback;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType.PaymentProcessSelectionActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class PaymentProcessActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private Utility utilObj;
    private TextView mDoneButton;
    private EditText mCardNumber;
    private EditText mCvvNumber;
    //private EditText mExpiryDate;

    // TEST STRIPE PUBLISHABLE KEY
    //private final String STRIPE_PUBLISHABLE_KEY = "pk_test_EuEqwpyxchmU4V3ch7rWq3VM";

    // PRODUCTION STRIPE PUBLISHABLE KEY
    private final String STRIPE_PUBLISHABLE_KEY = "pk_live_7uOsGJpYDpPgERlIPHGQwzOn";
//    private final String STRIPE_PUBLISHABLE_KEY = "pk_test_pyjTFM62WFm0SPzPtN9kY6ez";

    private double mSelectedOfferDollarValue = 0.0;
    private String mSelectedOfferDollarStringValue = "";

    private String mSelectedOfferPackageID;
    private EditText mPurchaseDollarValue;
    private ImageView mBackImage;
    private EditText mOfferPackageName;
    private LinearLayout mMainParentLayout;
    private String mSelectedOfferName;
    private Spinner mPaymentExpiryMonthSpinner;
    private Spinner mPaymentExpiryYearSpinner;
    private Button mDoOtherOperatorPaymentRequest;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_enter_payment_info);
        mContext = getApplicationContext();
//        this.overridePendingTransition(R.anim.slide_in_top, 0);
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        String dollarValue = getIntent().getStringExtra(Constants.Request.AMOUNT);
        String offerID = getIntent().getStringExtra(Constants.Request.OFFER);
        String offerName = getIntent().getStringExtra(Constants.Request.NAME);
        if (dollarValue != null) {
            mSelectedOfferDollarValue = Math.round(Double.parseDouble(dollarValue) * 100.00) / 100.00;

            //----------
            BigDecimal bd = new BigDecimal(mSelectedOfferDollarValue);
            BigDecimal bigDecimal = bd.setScale(2, RoundingMode.DOWN);
            mSelectedOfferDollarStringValue = bigDecimal.toPlainString();
            //----------
        }
        if (offerID != null) {
            mSelectedOfferPackageID = offerID;
            Log.d("sadsa", "onCreate: " + mSelectedOfferPackageID);
            ;
        }
        if (offerName != null) {
            mSelectedOfferName = offerName;
        }

        initViews();

    }

    private void initViews() {
        utilObj = new Utility(mContext);
        MyApplication.getInstance().trackScreenView(getString(R.string.ga_purchase_screen));
        bindViews();
    }

    private void bindViews() {
        setActionBar(getString(R.string.complete_payment), false);


        mDoOtherOperatorPaymentRequest = (Button) findViewById(R.id.doOtherOperatorPaymentRequest);
        mDoOtherOperatorPaymentRequest.setOnClickListener(this);


        mMainParentLayout = (LinearLayout) findViewById(R.id.mainParentLayout);
        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                return false;
            }
        });

        mOfferPackageName = (EditText) findViewById(R.id.offerPackageName);
        mOfferPackageName.setText(mSelectedOfferName);

        mPurchaseDollarValue = (EditText) findViewById(R.id.purchaseDollarValue);

        mPurchaseDollarValue.setText(getString(R.string.symbol_dollar) + mSelectedOfferDollarStringValue);

        mCardNumber = (EditText) findViewById(R.id.cardNumber);
        // mExpiryDate = (EditText) findViewById(R.id.expiryDate);
        mCvvNumber = (EditText) findViewById(R.id.cvvNumber);

        //---------------------
        mPaymentExpiryMonthSpinner = (Spinner) findViewById(R.id.expiryMonthSpinner);
        mPaymentExpiryYearSpinner = (Spinner) findViewById(R.id.expiryYearSpinner);

        // To set values for month in Expiry month date
        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext,
                R.array.payment_expiry_month, R.layout.custom_textview_centered_text);

        adapter.setDropDownViewResource(R.layout.custom_textview_centered_text);
        mPaymentExpiryMonthSpinner.setAdapter(adapter);

        // To set values for year in Expiry month date

        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.payment_expiry_year, R.layout.custom_textview_centered_text_2);

        adapter.setDropDownViewResource(R.layout.custom_textview_centered_text_2);
        mPaymentExpiryYearSpinner.setAdapter(yearAdapter);

        //---------------------
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackImage = (ImageView) customView.findViewById(R.id.backButton);
        mBackImage.setOnClickListener(this);

//        mDoneButton = (TextView) customView.findViewById(R.id.doneButton);
//        // mDoneButton.setOnClickListener(this);
//        mDoneButton.setVisibility(View.INVISIBLE);
//
 /*      mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

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
                            //    MyApplication.getInstance().printLogs("TOKEN", "" + token);
                            //getTokenList().addToList(token);
                            //finishProgress();
                            doCreatePayment(PaymentProcessActivity.this, token.getId());

                        }

                        public void onError(Exception error) {
                            utilObj.stopiOSLoader();
                            utilObj.showCustomAlertDialog(PaymentProcessActivity.this, null, error.getLocalizedMessage(), null, null, false, null);

                            //handleError(error.getLocalizedMessage());
                            //  finishProgress();
                        }
                    });
        } else if (!card.validateNumber()) {
            MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_complete_offer_package_payment_failure), getResources().getString(R.string.ga_event_action_complete_offer_package_payment_failure), getString(R.string.ga_event_label_complete_offer_package_payment_failure));
            utilObj.showCustomAlertDialog(this, null, getString(R.string.invalid_card_number), null, null, false, null);

        } else if (!card.validateExpiryDate()) {
            MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_complete_offer_package_payment_failure_invalid_expiry_date), getResources().getString(R.string.ga_event_action_complete_offer_package_payment_failure_invalid_expiry_date), getString(R.string.ga_event_label_complete_offer_package_payment_failure_invalid_expiry_date));
            utilObj.showCustomAlertDialog(this, null, getString(R.string.invalid_expiration_date), null, null, false, null);

        } else if (!card.validateCVC()) {
            MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_complete_offer_package_payment_failure_invalid_cvv), getResources().getString(R.string.ga_event_action_complete_offer_package_payment_failure_invalid_cvv), getString(R.string.ga_event_label_complete_offer_package_payment_failure_invalid_cvv));
            utilObj.showCustomAlertDialog(this, null, getString(R.string.invalid_cvv_number), null, null, false, null);

        } else {
            MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_complete_offer_package_payment_failure), getResources().getString(R.string.ga_event_action_complete_offer_package_payment_failure), getString(R.string.ga_event_label_complete_offer_package_payment_failure));
            utilObj.showCustomAlertDialog(this, null, getString(R.string.invalid_card_details), null, null, false, null);
        }
    }

    private PaymentProcessInfo createEnteredPaymentInfo() {
        PaymentProcessInfo paymentProcessInfo = new PaymentProcessInfo();
        paymentProcessInfo.setCardNumber(mCardNumber.getText().toString());
        paymentProcessInfo.setCvv(mCvvNumber.getText().toString());
        String month = (String) mPaymentExpiryMonthSpinner.getSelectedItem();
        String year = (String) mPaymentExpiryYearSpinner.getSelectedItem();
        String date = month + "/" + year; // mExpiryDate.getText().toString();
        String[] split = date.split("/");
        paymentProcessInfo.setExpMonth(Integer.parseInt(split[0]));
        paymentProcessInfo.setExpYear(Integer.parseInt(split[1]));
        paymentProcessInfo.setCurrency(getString(R.string.usd));
        return paymentProcessInfo;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doOtherOperatorPaymentRequest:
                if (validatingRequired()) {
                    PaymentProcessInfo enteredPaymentInfo = createEnteredPaymentInfo();
                    generateToken(enteredPaymentInfo);
                }
                break;
            case R.id.backButton:
                finish();
                break;


        }
    }

    /**
     * The method is used to validate the required fields
     *
     * @return boolean true if fields are validated else false
     **/

    private boolean validatingRequired() {
        String message = "";
        String cardNumber = mCardNumber.getText().toString();
        String cvvNumber = mCvvNumber.getText().toString();
        Calendar calendar = Calendar.getInstance();
        int cmonth = calendar.get(Calendar.MONTH) + 1;
        int cyear = calendar.get(Calendar.YEAR);

        String cLastTwoDigitYear = "" + cyear;
        if (cLastTwoDigitYear.length() >= 2) {
            cLastTwoDigitYear = cLastTwoDigitYear.substring(cLastTwoDigitYear.length() - 2);
        }

        String selectedMonthItem = (String) mPaymentExpiryMonthSpinner.getSelectedItem();
        String selectedYearItem = (String) mPaymentExpiryYearSpinner.getSelectedItem();


        //String expiryDate = mExpiryDate.getText().toString();

        //validate the content
        if (cardNumber.trim().length() == 0) {
            message = getResources().getString(R.string.cardNumberErrorMessage);
            //utilObj.showError(this, message, textViewObj, emailObj);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        } else if (mPaymentExpiryMonthSpinner.getSelectedItemPosition() == 0 || mPaymentExpiryYearSpinner.getSelectedItemPosition() == 0) {
            message = getResources().getString(R.string.select_date_error);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        } else if (Integer.parseInt(selectedYearItem) == Integer.parseInt(cLastTwoDigitYear)) {
            if (Integer.parseInt(selectedMonthItem) < cmonth) {
                message = getResources().getString(R.string.date_error);
                utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
            }
        } else if (Integer.parseInt(selectedYearItem) < Integer.parseInt(cLastTwoDigitYear)) {
            message = getResources().getString(R.string.date_error);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        } else if (cvvNumber.trim().length() == 0) {
            message = getResources().getString(R.string.cVVNumberErrorMessage);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        }





       /* else if (!expiryDate.contains("/")) {
            message = getResources().getString(R.string.date_error);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        }*/

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
                                MyApplication.getInstance().trackEvent(getString(R.string.ga_event_category_complete_offer_package_payment_success), getString(R.string.ga_event_action_complete_offer_package_payment_success), getString(R.string.ga_event_label_complete_offer_package_payment_success));
                                utilObj.showCustomAlertDialog(PaymentProcessActivity.this, null, stripePaymentStatusInfo.getMessage(), null, null, false, null);
                            } else {
                                //  Purchased successfully
                                Intent intentObj = new Intent(PaymentProcessActivity.this, PackageSubscriptionSucessActivity.class);
                                intentObj.putExtra(Constants.DEFAULT_VALUES.PURCHASE_DONE, Constants.DEFAULT_VALUES.SUCCESS);
                                intentObj.putExtra("PaymentMethod", "Stripe");
                                startActivity(intentObj);
                                finish();
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

}
