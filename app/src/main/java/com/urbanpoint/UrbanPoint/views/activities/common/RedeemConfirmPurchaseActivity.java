package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.async.CommonAsyncTaskResult;
import com.urbanpoint.UrbanPoint.async.CommonAsynctask;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ICommonCallback;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.categoryScreens.OfferManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryFilled;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Handler;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.urbanpoint.UrbanPoint.utils.Constants.Request.MIXPANEL_TOKEN;
import static com.urbanpoint.UrbanPoint.utils.Constants.TaskID.DO_UPDATE_PERMISSIONS;

/**
 * Created by aparnasalve on 10/3/16.
 */

/**
 * 13 - JUN - 2016
 * THE LAYOUT DESIGN (activity_redeem_purchase_confirm.xml) is change with (activity_redeem_purchase_confirm_new_design.xml)
 * ALL THE UNWANTED WIGDENT ARE SET TO VISIBILITY=GONE, NO CODE CHANGES DONE
 * MERCHANT ADDRESS FIELD EXTRA ADDED IN NEW DESIGN
 */

public class RedeemConfirmPurchaseActivity extends FragmentActivity implements ServiceRedirection, CustomDialogConfirmationInterfaces, View.OnClickListener {
    private Context mContext;
    private Utility utilObj;
    String mCustomerid, mProductid, mName, mProductImage, mMerchantImage, mProductDetail, mProductPrice, mMerchantID, mConfirmationCode, mImageUrl;
    private ImageView mBackButton;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ImageView mImageViewProduct, mImageViewMerchant;
    TextView mDetailsHeader, mDetailsPrice, mOffrName, mMerchant, mOutlet;
    private PinEntryFilled mRedeemPinEntry;
    Button mReedemButton;
    private String enteredPurchasePin = "";
    private OfferManager mOfferManager;
    Handler mHandler;
    String status, message;
    CustomDialogConfirmationInterfaces customDialogConfirmationInterfacesCustomerPIN, customDialogConfirmationInterfacesDirection;
    private String mMerchantName;

    Bundle mConfirmSuccessBundle = new Bundle();
    private RelativeLayout mMainRedeemConfirmParentLayout;
    private TextView mDetailsMerchantName;
    private LinearLayout mOldLayoutObjects;
    private String mMerchantAddress;
    private TextView mDetailsMerchantAddress;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///-------- SEE COMMENT AT TOP OF FILE
        //setContentView(R.layout.activity_redeem_purchase_confirm);
        setContentView(R.layout.activity_redeem_purchase_confirm_new_design);

        ///-------- SEE COMMENT AT TOP OF FILE
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        mContext = getApplicationContext();
        initViews();
        MyApplication.getInstance().trackScreenView(getString(R.string.redemption_merchant_pin));

    }

    private void initViews() {
        mCustomerid = "";
        mProductid = "";
        mImageUrl = "";
        mMerchantImage = "" ;
        mProductPrice = "0";
        mProductDetail = "";
        mName = "";
        mMerchantName = "";
        mMerchantID = "";
        mMerchantAddress = "";
        utilObj = new Utility(mContext);
        mOfferManager = new OfferManager(mContext, this);
        Bundle m_buBundle = getIntent().getExtras();
        if (m_buBundle != null) {
            mCustomerid = m_buBundle.getString(Constants.Request.CUSTOMER_ID);
            mProductid = m_buBundle.getString(Constants.Request.OFFER_ID);
            mImageUrl = m_buBundle.getString(Constants.Request.IMAGE_URL);
            mMerchantImage = m_buBundle.getString(Constants.Request.MERCHANT_IMAGE_URL);
            mProductPrice = m_buBundle.getString(Constants.Request.SPECIAL_PRICE);
            mProductDetail = m_buBundle.getString(Constants.Request.NAME);
            mName = m_buBundle.getString(Constants.Request.NAME);
            mMerchantName = m_buBundle.getString(Constants.OfferDetails.MERCHANT_NAME);
            mMerchantID = m_buBundle.getString(Constants.Request.MERCHANT_ID);
            mMerchantAddress = m_buBundle.getString(Constants.OfferDetails.MERCHANT_ADDRESS);
            setActionBar(getResources().getString(R.string.merchant_pin), true);
        }
        bindViews();
    }

    private void bindViews() {

        //------- THIS IS DONE TO HIDE OLD LAYOUT OBJECT, SEE COMMENT ON TOP
        mOldLayoutObjects = (LinearLayout) findViewById(R.id.oldLayoutObjects);
        mOldLayoutObjects.setVisibility(View.GONE);
        //------- THIS IS DONE TO HIDE OLD LAYOUT OBJECT, SEE COMMENT ON TOP

        mOffrName = (TextView) findViewById(R.id.act_redeem_purchase_cnfrm_new_design_txv_offr);
        mMerchant = (TextView) findViewById(R.id.act_redeem_purchase_cnfrm_new_design_txv_merchnt);
        mOutlet = (TextView) findViewById(R.id.act_redeem_purchase_cnfrm_new_design_txv_outlet);

        mOffrName.setText(mName);
        mMerchant.setText(mMerchantName);
        mOutlet.setText(mMerchantAddress);

        mMainRedeemConfirmParentLayout = (RelativeLayout) findViewById(R.id.mainRedeemConfirmParentLayout);
        mMainRedeemConfirmParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                return false;
            }
        });


        mImageViewProduct = (ImageView) findViewById(R.id.imageView_product);
        mImageViewMerchant = (ImageView) findViewById(R.id.imageView_merchant);
        mRedeemPinEntry = (PinEntryFilled) findViewById(R.id.redeemUserPinEntry);
        mDetailsHeader = (TextView) findViewById(R.id.detailsHeader);
        mDetailsPrice = (TextView) findViewById(R.id.detailsPrice);

        mDetailsMerchantName = (TextView) findViewById(R.id.detailsMerchantName);
        mDetailsMerchantAddress = (TextView) findViewById(R.id.detailsMerchantAddress);

        // Reedem Button visibitiy is set to Gone and its click is performed on line 167
        mReedemButton = (Button) findViewById(R.id.redeemConfirmPurchaseButton);
        mReedemButton.setOnClickListener(this);


//        mRedeemPinEntry.requestFocus();
//
//        // Show keyboard
//        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInput(mRedeemPinEntry, 0);
//
        mRedeemPinEntry.setOnPinEnteredListener(new PinEntryFilled.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredPurchasePin = pin;
                utilObj.keyboardClose(mContext, mRedeemPinEntry);
                utilObj.keyboardClose(mContext, mRedeemPinEntry);
                mReedemButton.setVisibility(View.VISIBLE);

            }
        });


        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.no_image_white)
                .showImageForEmptyUri(R.mipmap.no_image_white)
                .showImageOnFail(R.mipmap.no_image_white).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        if (mMerchantImage != null) {
            imageLoader.displayImage(mMerchantImage, mImageViewMerchant, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    mImageViewMerchant.setImageResource(R.mipmap.no_image_white);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }
            });
        } else {
            mImageViewMerchant.setImageResource(R.mipmap.no_image_white);
        }
        if (!mImageUrl.equalsIgnoreCase("")) {
            imageLoader.displayImage(mImageUrl, mImageViewProduct, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    mImageViewProduct.setImageResource(R.mipmap.no_image_white);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }
            });
        } else {
            mImageViewProduct.setImageResource(R.mipmap.no_image_white);
        }

        mDetailsHeader.setText(mProductDetail);
        /*double a = Double.parseDouble(mProductPrice);
        BigDecimal bd = new BigDecimal(a);
        BigDecimal res = bd.setScale(2, RoundingMode.DOWN);
        mDetailsPrice.setText(getString(R.string.price_unit) + res.toPlainString());*/

        if (mProductPrice != "" && !mProductPrice.equalsIgnoreCase("null")) {

            if (mProductPrice.contains("-")) {
                mDetailsPrice.setText(getString(R.string.price_unit) + " " + mProductPrice);
            } else {
                double a = Double.parseDouble(mProductPrice);
                BigDecimal bd = new BigDecimal(a);
                BigDecimal res = bd.setScale(0, RoundingMode.DOWN);
                mDetailsPrice.setText(getString(R.string.price_unit) + " " + res.toPlainString());
            }


        } else {
            mDetailsPrice.setText(Constants.DEFAULT_VALUES.ZERO);
        }


        // ---- TO set merchant Name
        if (mMerchantName != null) {
            mDetailsMerchantName.setText("" + mMerchantName);
        }

        if (mMerchantAddress != null) {
            mDetailsMerchantAddress.setText("" + mMerchantAddress);
        }

        customDialogConfirmationInterfacesCustomerPIN = new CustomDialogConfirmationInterfaces() {
            @Override
            public void callConfirmationDialogPositive() {

            }

            @Override
            public void callConfirmationDialogNegative() {

            }
        };

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
            }
        });

        title1.setText(title);
        getActionBar().setCustomView(customView);
    }

    @Override
    public void callConfirmationDialogPositive() {

    }

    @Override
    public void callConfirmationDialogNegative() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.redeemConfirmPurchaseButton:
                if (validatingRequired()) {
                    if (NetworkUtils.isConnected(mContext)) {
                        doPurchaseConfirm(RedeemConfirmPurchaseActivity.this);
                    } else {
                        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }

                }
                break;

        }
    }


    private boolean validatingRequired() {
        String message = "";

        String enteredPin = enteredPurchasePin;

        //validate the content
        if (enteredPin.length() < 4) {
            message = getResources().getString(R.string.enter_merchant_four_digit_pin);
            utilObj.showCustomAlertDialog(RedeemConfirmPurchaseActivity.this, getString(R.string.redeem), message, null, null, false, this);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }

    public boolean doPurchaseConfirm(Activity context) {

        final String key = "doPurchaseConfirm";


        String jsonParam = Constants.BLANK;
        try {

            String custID = AppPreference.getSetting(context, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.OFFER_ID, "" + mProductid);
            jsonObject.put(Constants.Request.MERCHANT_PIN, "" + enteredPurchasePin);
            jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);
            jsonParam = jsonObject.toString();


        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        new CommonAsynctask(new ICommonCallback() {
            @Override
            public void onTaskDone(Bundle result, CommonAsyncTaskResult taskResult) {

                if (taskResult == CommonAsyncTaskResult.OK) {

                    JSONObject jsonObj = null;
                    JSONObject reader = null;
                    try {
                        String jsonString = result.getString(key);
                        jsonObj = new JSONObject(
                                new JSONTokener(result.getString(key)));
                        Gson gson = new GsonBuilder().create();
                        if (jsonString != null) {
                            reader = new JSONObject(jsonString);
                            status = reader.getString(Constants.Request.STATUS);
                            message = reader.getString(Constants.Request.MESSAGE);
                        }
                        if (Constants.DEFAULT_VALUES.ONE.equals(status)) {
                            /*CustomDialogConfirmationInterfaces customDialogConfirmationInterfaces = new CustomDialogConfirmationInterfaces() {
                                @Override
                                public void callConfirmationDialogPositive() {

                                    mConfirmSuccessBundle.putString(Constants.Request.CONFIRMATION_CODE, mConfirmationCode);
                                    mConfirmSuccessBundle.putString(Constants.Request.NAME, mName);
                                    mConfirmSuccessBundle.putString(Constants.OfferDetails.MERCHANT_NAME, mMerchantName);

                                    Intent intent = new Intent(RedeemConfirmPurchaseActivity.this, PurchaseSuccessActivity.class);
                                    intent.putExtras(mConfirmSuccessBundle);
                                    startActivity(intent);
                                }

                                @Override
                                public void callConfirmationDialogNegative() {

                                }
                            };
                            utilObj.showCustomAlertDialog(RedeemConfirmPurchaseActivity.this, null, getString(R.string.redeem_confirm_success), null, null, false, customDialogConfirmationInterfaces);*/
                            mConfirmationCode = reader.getString(Constants.Request.DATA);
                            mConfirmSuccessBundle.putString(Constants.Request.CONFIRMATION_CODE, mConfirmationCode);
                            mConfirmSuccessBundle.putString(Constants.Request.NAME, mName);
                            mConfirmSuccessBundle.putString(Constants.OfferDetails.MERCHANT_NAME, mMerchantName);
                            mConfirmSuccessBundle.putString(Constants.OfferDetails.MERCHANT_ADDRESS, mMerchantAddress);


//                            try {
//                                if (AppInstance.rModel_checkSubscription!=null) {
//                                    if (AppInstance.rModel_checkSubscription.getFirst_weak() != null &&
//                                            AppInstance.rModel_checkSubscription.getFirst_weak().length() > 0) {
//                                        if (AppInstance.rModel_checkSubscription.getFirst_weak().equals("1")) {
//                                            AppInstance.rModel_checkSubscription.setFirst_weak("0");
//                                            AppInstance.getAppInstance().setIsUserSubscribed(false);
//                                            Log.d("asdasd", "On purchase 1st Offers availed and is now going to lock ");
//                                        }
//                                    }else {
//                                        AppInstance.rModel_checkSubscription.setFirst_weak("0");
//                                    }
//                                } else {
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                MixpanelAPI mixpanel = MixpanelAPI.getInstance(mContext, MIXPANEL_TOKEN);
//                                JSONObject props = null;
//                                try {
//                                    props = new JSONObject();
//                                    props.put("Exception", e.getMessage());
//                                    props.put("Merchant ID", mMerchantID);
//                                    props.put("Product ID", mProductid);
//                                    props.put("Customer ID", AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID,""));
//                                    props.put("Customer Email", AppPreference.getSetting(mContext, Constants.Request.EMAIL_ID,""));
//                                    props.put("Merchant Pin", enteredPurchasePin);
//                                } catch (JSONException ex) {
//                                    ex.printStackTrace();
//                                }
//                                mixpanel.track("Redeem Offer Exception", props);
//                            }

//                            FlurryAgent.onError("Success On Redeem offer", message, "");
//                            FlurryAgent.logEvent(message);

                            AppInstance.myReviewsCount++;

                            try {
                                Intent intent = new Intent(RedeemConfirmPurchaseActivity.this, PurchaseSuccessActivity.class);
                                intent.putExtras(mConfirmSuccessBundle);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                FlurryAgent.onError("Exception On PurchaseSuccessActivity", e.getMessage(), "");
                                FlurryAgent.logEvent(e.getMessage());
                            }

                        } else {
                            mRedeemPinEntry.clearText();
                            String tempMessage = message;
                            FlurryAgent.onError("Failed On Redeem offer", message, "");
                            FlurryAgent.logEvent(message);


                            if (tempMessage.toLowerCase().contains(Constants.DEFAULT_VALUES.Product_Expired)) {
                                utilObj.showCustomAlertDialog(RedeemConfirmPurchaseActivity.this, getString(R.string.redeem), message, getString(R.string.Ok), null, false, customDialogConfirmationInterfacesCustomerPIN);
                            } else {

                                utilObj.showCustomAlertDialog(RedeemConfirmPurchaseActivity.this, getString(R.string.redeem), getString(R.string.error_merchant_pin), getString(R.string.Ok), null, false, customDialogConfirmationInterfacesCustomerPIN);

                            }

                        }


                    } catch (JSONException e) {
                        // b.putString(JSONConstants.RESPONSE_CODE, JSONConstants.RESPONSE_CODE_ZERO);
                        FlurryAgent.onError("Exception On Redeem offer", e.getMessage(), "");
                        FlurryAgent.logEvent(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }, context, key, jsonParam, true, true, mContext.getString(R.string.redeem_progress_message), true, null).execute(Constants.WebServices.WS_PURCHASE_CONFIRM);

        return false;
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.GET_PURCHASE_CONFIRM_TASK_ID) {

            Bundle m_bundle = new Bundle();
            m_bundle.putString(Constants.Request.CONFIRMATION_CODE, mConfirmationCode);
            m_bundle.putString(Constants.Request.NAME, mName);
            MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_redemption_correct_merchant_pin), getResources().getString(R.string.ga_event_action_redemption_correct_merchant_pin), getResources().getString(R.string.ga_event_label_redemption_correct_merchant_pin));
            Intent intent = new Intent(RedeemConfirmPurchaseActivity.this, RedeemConfirmPurchaseActivity.class);
            intent.putExtras(m_bundle);
            startActivity(intent);

        }

    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        if (errorMessage.equalsIgnoreCase(getResources().getString(R.string.incorrect_merchant_pin_msg))) {
            MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_redemption_incorrect_merchant_pin), getResources().getString(R.string.ga_event_action_redemption_incorrect_merchant_pin), getResources().getString(R.string.ga_event_label_redemption_incorrect_merchant_pin));
        }
        utilObj.showCustomAlertDialog(RedeemConfirmPurchaseActivity.this, null, getString(R.string.error_merchant_pin), getString(R.string.Ok), null, false, customDialogConfirmationInterfacesCustomerPIN);

    }

    @Override

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }
}

