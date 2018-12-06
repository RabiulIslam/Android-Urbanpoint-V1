package com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.merge.MergeAdapter;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.adapters.offerPackagesSubscriptions.VodaBillInfoAdapter;
import com.urbanpoint.UrbanPoint.async.CommonAsyncTaskResult;
import com.urbanpoint.UrbanPoint.async.CommonAsynctask;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.MSISDNVerificationStatusInfo;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.VodafoneBillInfoDetails;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ICommonCallback;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.CommunicationManager;
import com.urbanpoint.UrbanPoint.managers.PaymentManager;
import com.urbanpoint.UrbanPoint.managers.Validate_WebHit_Post_Ooredoo;
import com.urbanpoint.UrbanPoint.managers.Validate_WebHit_Post_Vodafone;
import com.urbanpoint.UrbanPoint.managers.vodafoneApiXMLParsing.SAXXMLParser;
import com.urbanpoint.UrbanPoint.managers.vodafoneOfferPackagesBillInfo.VodafoneBillInfoManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.OfferPackagesActivity;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.CellNoEntryView;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.GET_FOOD_OFFER_LIST_TASK_ID;


public class PaymentProcessVodafoneSelectionActivity extends Activity implements View.OnClickListener, ServiceRedirection {
    private Context mContext;
    private Utility utilObj;
    private double mSelectedOfferDollarValue = 0.0;
    private String mSelectedOfferPackageID;
    private EditText mPurchaseDollarValue;
    private ImageView mBackButton;
    private CellNoEntryView mMobileNumberEntry;
    private String enteredMobileNumber = "";
    private Button mSendOTPToPhone;
    private String mTransactionIDForMSISDNVerification;
    private ListView mVodaBillInfoContentList;
    private VodafoneBillInfoManager mVodafoneBillInfoManager;
    private MergeAdapter mMergedAdapter;
    private LayoutInflater mInflater;
    private LinearLayout mMainVodaPaymentParentLayout;
    private ImageView imvLogo;
    private CommunicationManager commObj;
    private ServiceRedirection serviceRedirectionObj;
    private Button btnSubscribe;
    private TextView txvFree1stMonth;
    int tasksID;
    private String mCustomerid, mCatId, mMerchantID, mProductid, mName, mPhoneNumber;
    private boolean isOoredoo;
    private CustomDialogConfirmationInterfaces customDialogConfirmationInterfaces;

    String status, message;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_voda_subscription_enter_mobile_info);


        mContext = getApplicationContext();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

        String dollarValue = getIntent().getStringExtra(Constants.Request.AMOUNT);
        String offerID = getIntent().getStringExtra(Constants.Request.OFFER);
        if (dollarValue != null) {
            mSelectedOfferDollarValue = Math.round(Double.parseDouble(dollarValue) * 100.00) / 100.00;
        }
        if (offerID != null) {
            mSelectedOfferPackageID = offerID;
        }


        initViews();
        customDialogConfirmationInterfaces = new CustomDialogConfirmationInterfaces() {
            @Override
            public void callConfirmationDialogPositive() {
                Intent intent = new Intent(PaymentProcessVodafoneSelectionActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void callConfirmationDialogNegative() {

            }
        };
    }

    private void initViews() {
        utilObj = new Utility(mContext);
        mVodafoneBillInfoManager = new VodafoneBillInfoManager(mContext, this);
        isOoredoo = true;
        bindViews();
        MyApplication.getInstance().trackScreenView(getString(R.string.susbsciption_screen_one));
//        doCallVodafonePackages();
    }

    private void bindViews() {
        txvFree1stMonth = (TextView) findViewById(R.id.istFreeOffertxv);
        btnSubscribe = (Button) findViewById(R.id.act_pakage_subscribe_btn_subscribe);
        btnSubscribe.setOnClickListener(this);
        mMainVodaPaymentParentLayout = (LinearLayout) findViewById(R.id.mainVodaPaymentParentLayout);
        mMainVodaPaymentParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                return false;
            }
        });

        long no = AppPreference.getSettingResturnsLong(mContext, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);
        if (no == 3) {
            txvFree1stMonth.setVisibility(View.VISIBLE);
        } else {
            txvFree1stMonth.setVisibility(View.INVISIBLE);
        }
        mVodaBillInfoContentList = (ListView) findViewById(R.id.vodaContentList);

        mSendOTPToPhone = (Button) findViewById(R.id.doVodafoneSendOTPToPhone);
        mSendOTPToPhone.setOnClickListener(this);

        imvLogo = (ImageView) findViewById(R.id.frg_payment_logo);
        mMobileNumberEntry = (CellNoEntryView) findViewById(R.id.mobileNumberEntry);

        mMobileNumberEntry.setOnPinEnteredListener(new CellNoEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredMobileNumber = pin;
                utilObj.keyboardClose(mContext, mMobileNumberEntry);
            }
        });

        Bundle m_buBundle = getIntent().getExtras();
        mCustomerid = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
        mProductid = "";
        mName = "";
        mCatId = "";
        mMerchantID = "";
        if (m_buBundle != null) {
            mCustomerid = m_buBundle.getString(Constants.Request.CUSTOMER_ID);
            mProductid = m_buBundle.getString(Constants.Request.OFFER_ID);
            mName = m_buBundle.getString(Constants.Request.NAME);
            mCatId = m_buBundle.getString(Constants.Request.CATEGORYID);
            mMerchantID = "";
            setActionBar(mName, true);
        }

        Log.d("LOGOO", "initViews: mCustID " + mCustomerid);

        if (AppInstance.profileData!=null) {
            if (AppInstance.profileData.getIsOoredooCustomer() != null &&
                    AppInstance.profileData.getIsOoredooCustomer().length() > 0 &&
                    AppInstance.profileData.getIsOoredooCustomer().equals("1")) {
                setActionBar("Ooredoo User", false);
                isOoredoo = true;
                imvLogo.setBackground(getResources().getDrawable(R.mipmap.ooredoo_logo));
            } else if (AppInstance.profileData.getIsVodafoneCustomer() != null &&
                    AppInstance.profileData.getIsVodafoneCustomer().length() > 0 &&
                    AppInstance.profileData.getIsVodafoneCustomer().equals("1")) {
                setActionBar("Vodafone User", false);
                isOoredoo = false;
                imvLogo.setBackground(getResources().getDrawable(R.mipmap.vodafone_logo));
            }
        } else {
            setActionBar("Vodafone User", false);
        }
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSlideButton.toggle(true);
                finish();
                AppInstance.offerPackagesItems = null;
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

    /**
     * The method handles the result from the Facebook
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doVodafoneSendOTPToPhone:
                if (validatingRequired()) {

                    if (NetworkUtils.isConnected(mContext)) {
                        MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_vodafone_qatar_user), getResources().getString(R.string.ga_event_action_vodafone_qatar_user), getResources().getString(R.string.ga_event_label_vodafone_qatar_user));
                        utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);

                        if (isOoredoo) {
                            Validate_WebHit_Post_Ooredoo validate_webHit_post_ooredoo = new Validate_WebHit_Post_Ooredoo();
                            validate_webHit_post_ooredoo.checkValidation(mContext, this, Constants.API_EGYPTLINX_PARAMS_VALUES.QATAR_COUNTRY_CODE + enteredMobileNumber);
                        } else {
                            Validate_WebHit_Post_Vodafone validate_webHit_post_vodafone = new Validate_WebHit_Post_Vodafone();
                            validate_webHit_post_vodafone.checkValidation(mContext, this, Constants.API_EGYPTLINX_PARAMS_VALUES.QATAR_COUNTRY_CODE + enteredMobileNumber); // Do not send country code for vodafone
                        }
//                        doCheckVodaMobileNumberAlreadySubScribed(this, Constants.API_EGYPTLINX_PARAMS_VALUES.QATAR_COUNTRY_CODE + enteredMobileNumber);
//                        PaymentManager paymentManager = new PaymentManager(mContext, this);
//                        paymentManager.doOoredooValidation("974" + enteredMobileNumber);
                    } else {
                        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }
                }
                break;
            case R.id.act_pakage_subscribe_btn_subscribe:
                Intent intent = null;
                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_gain_access_non_vodafone), getResources().getString(R.string.ga_event_action_gain_access_non_vodafone), getResources().getString(R.string.ga_event_label_gain_access_non_vodafone));
                intent = new Intent(this, OfferPackagesActivity.class);
                startActivity(intent);
                this.finish();
//                    Intent intentObj = new Intent(getActivity(), PaymentProcessVodafoneSelectionActivity.class);
//                    startActivity(intentObj);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void doCallVodafonePackages() {
        if (NetworkUtils.isConnected(mContext)) {
            utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);

            mVodafoneBillInfoManager.doGetVodafoneBillInfoDetails();
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }

    /**
     * The method is used to validate the required fields
     *
     * @return boolean true if fields are validated else false
     **/

    private boolean validatingRequired() {
        String message = "";
        String enteredPhone = enteredMobileNumber;

        //validate the content
        if (enteredPhone.length() < 8) {
            message = getResources().getString(R.string.enter_mobile_number);
            utilObj.showCustomAlertDialog(this, null, message, null, null, false, null);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }



    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.GET_BILL_INFO_DETAILS) {

            VodafoneBillInfoDetails vodafoneBillInfoDetails = AppInstance.getAppInstance().getVodafoneBillInfoDetails();
            if (vodafoneBillInfoDetails != null) {
                createAdapterViews(vodafoneBillInfoDetails);
            }
        } else if (taskID == Constants.TaskID.DO_CHECK_OOREDOO_VALIDATION) {
            Log.d("HSDSDS", "onSuccessRedirection: ");

            if (!AppInstance.ooredooValidation.getData().equals("false")) {
                AppPreference.setSetting(mContext, Constants.Request.OOREDOO_MSISDN, "974" + enteredMobileNumber);
                Intent intent = new Intent(PaymentProcessVodafoneSelectionActivity.this, PaymentProcessVodafoneOTPConfirmation.class);
                intent.putExtra(Constants.Request.TRANSACTION_ID, AppInstance.ooredooValidation.getData());
                intent.putExtra(Constants.Request.OOREDOO_MSISDN, "974" + enteredMobileNumber);
                startActivity(intent);
                finish();
            } else {
                utilObj.showCustomAlertDialog(this, null, getResources().getString(R.string.invalid_nummber), null, null, false, null);
            }
        } else {
            utilObj.showToast(this, getResources().getString(R.string.no_data_received), 1);
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
    }


    private void createAdapterViews(VodafoneBillInfoDetails vodafoneBillInfoDetails) {
        mMergedAdapter = new MergeAdapter();

        View mInflateHeader = mInflater.inflate(R.layout.fragment_voda_subscription_enter_mobile_info_header, null);

        TextView mTextViewHeader = (TextView) mInflateHeader.findViewById(R.id.vodaBillInfoHeaderText);

        mTextViewHeader.setText(vodafoneBillInfoDetails.getHeadingText());
        mMergedAdapter.addView(mInflateHeader, false);

        VodaBillInfoAdapter contentAdapter = new VodaBillInfoAdapter(mContext, vodafoneBillInfoDetails.getOffersListContents());
        mMergedAdapter.addAdapter(contentAdapter);

        mVodaBillInfoContentList.setAdapter(mMergedAdapter);
    }

    public void showResultOoredooValidation(boolean b, String msg) {
        utilObj.stopiOSLoader();
        if (b) {
            if (msg.equalsIgnoreCase("201")) {
                utilObj.showCustomAlertDialog(this, null, "Congratulations! You now have free access to all Urban Point offers!",
                        getString(R.string.ok), "", false,
                        customDialogConfirmationInterfaces);
            } else {
                if (!AppInstance.ooredooValidation.getData().equals("false")) {
                    AppPreference.setSetting(mContext, Constants.Request.OOREDOO_MSISDN, "974" + enteredMobileNumber);
                    Intent intent = new Intent(PaymentProcessVodafoneSelectionActivity.this, PaymentProcessVodafoneOTPConfirmation.class);

                    Bundle m_bundle = new Bundle();
                    m_bundle.putString(Constants.Request.CUSTOMER_ID, mCustomerid);
                    m_bundle.putString(Constants.Request.OFFER_ID, mProductid);
                    m_bundle.putString(Constants.Request.NAME, mName);
                    m_bundle.putString(Constants.Request.CATEGORYID, mCatId);
                    m_bundle.putString(Constants.Request.TRANSACTION_ID, AppInstance.ooredooValidation.getData());
                    m_bundle.putString(Constants.Request.OOREDOO_MSISDN, "974" + enteredMobileNumber);
                    m_bundle.putBoolean("key_customer_type", isOoredoo);
                    intent.putExtras(m_bundle);
                    startActivity(intent);
                    finish();
                } else {
                    utilObj.showCustomAlertDialog(this, null, getResources().getString(R.string.invalid_nummber), null, null, false, null);
                }
            }
        } else {
            utilObj.showToast(mContext, msg, Toast.LENGTH_LONG);
        }
    }

    public void showResultVodafoneValidation(boolean b, String msg) {
        utilObj.stopiOSLoader();
        if (b) {
            AppPreference.setSetting(mContext, Constants.Request.OOREDOO_MSISDN, "974" + enteredMobileNumber);
            Intent intent = new Intent(PaymentProcessVodafoneSelectionActivity.this, PaymentProcessVodafoneOTPConfirmation.class);

            Bundle m_bundle = new Bundle();
            m_bundle.putString(Constants.Request.CUSTOMER_ID, mCustomerid);
            m_bundle.putString(Constants.Request.OFFER_ID, mProductid);
            m_bundle.putString(Constants.Request.NAME, mName);
            m_bundle.putString(Constants.Request.CATEGORYID, mCatId);
            m_bundle.putString(Constants.Request.TRANSACTION_ID, msg); //msg is transaction ID send from Validation Api Response
            m_bundle.putString(Constants.Request.OOREDOO_MSISDN, "974" + enteredMobileNumber);
            m_bundle.putBoolean("key_customer_type", isOoredoo);
            intent.putExtras(m_bundle);
            startActivity(intent);
            finish();
        } else {
            utilObj.showToast(mContext, msg, Toast.LENGTH_LONG);

        }
    }
}
