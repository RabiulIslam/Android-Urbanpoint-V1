package com.urbanpoint.UrbanPoint.views.activities.common;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.uber.sdk.android.core.Deeplink;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.android.rides.RideRequestDeeplink;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.core.client.ServerTokenSession;
import com.uber.sdk.core.client.SessionConfiguration;
import com.uber.sdk.rides.client.error.ApiError;
import com.uber.sdk.rides.client.model.UserProfile;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.AddToWishlist;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.FoodOfferDeatils;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.categoryScreens.OfferManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.ResponseCodes;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType.PaymentProcessVodafoneSelectionActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aparnasalve on 3/3/16.
 */
public class OfferDetailActivity extends Activity implements ServiceRedirection, View.OnClickListener {

    private String mCustomerid, mMerchantID, mProductid, mName, mPhoneNumber;
    private Context mContext;
    private Utility utilObj;
    private OfferManager mOfferManager;
    private ImageView mImageViewProduct, mImageViewMerchant;
    private TextView mApproxSavingText, mItemName, mItemAddress, mDescription, mPrice, mSpecialPrice, mItemDiscount, mSavingText, mExpiry, mMerchantName, mTitleDescription, mTimingText, mTimingValue, mWhatNeedText, mOfferSubjectTo, mFinePrint, mApproxSaving;
    private Button mBtnCall, mBtnDirection, mBtnShare, mBtnRedeem, mBtnAddToList, mBtnSavedToList, mBtnOutOfStock;
    private LinearLayout mLLyt_action_buttons;
    private TextView mBtnRuleOfPurchase;
    private ProgressBar mItemImageProgress;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ImageView mBackButton;
    private FoodOfferDeatils foodOfferDeatil;
    private CustomDialogConfirmationInterfaces customDialogConfirmationInterfacesCall, customDialogConfirmationInterfacesDirection;
    private RelativeLayout rlRedBar, rlProductDetail;
    private String mOfferLockStatus;
    private ImageView mBtnRedeemLockIcon;
    private CustomDialogConfirmationInterfaces customDialogConfirmationInterfacesRedeem;
    private SessionConfiguration uberConfig;
    private RideRequestButton requestButton;
    RideRequestButtonCallback callback;
    private FrameLayout mBtnRedeemLayout;
    private String mCatId;
    private String strPromotionalPermision;


    private TextView txvDuration, txvEstimate;
    private LinearLayout rlUberEstimate, rlUberEstimate2;
    private RideRequestDeeplink mDeepLink;

    private final int REQUEST_CODE_ASK_PERMISSIONS_CALL = 0;
    private final int REQUEST_CODE_ASK_PERMISSIONS_GMAP = 1;

    private Thread thread;
    private String catName = "";
    private boolean isSubscribed;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.offer_detail);
        setContentView(R.layout.offer_detail_new);
        mContext = getApplicationContext();
        isSubscribed = AppPreference.getSettingResturnsBoolean(mContext, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        initViews();
        // MyApplication.getInstance().trackScreenView(getString(R.string.food_and_drink_offer_details));
        if (!isSubscribed) {
            Log.d("SSSS", "lock On Resume: ");
            mBtnRedeemLockIcon.setVisibility(View.VISIBLE);
        } else {
            Log.d("SSSS", "unlock On Resume: ");
            mBtnRedeemLockIcon.setVisibility(View.GONE);
            mBtnRedeem.setBackgroundResource(R.mipmap.change);
        }


        // this is for setting product detail imgHeight container
        int width = getScreenWidth();
        int imgHeight = (int) (width * 0.78); // 0.78 is the image ratio
        int parentHeight = (imgHeight + dpToPx(10));

        mImageViewProduct.getLayoutParams().height = imgHeight;
        rlProductDetail.getLayoutParams().height = parentHeight;

        rlUberEstimate = findViewById(R.id.uber_estimate_rl_container);
        txvDuration = findViewById(R.id.uber_duration_txv);
        txvEstimate = findViewById(R.id.uber_estimate_txv);
        rlUberEstimate.setOnClickListener(this);

//       updateUberSDK();

    }

    private void updateUberSDK() {
        uberConfig = new SessionConfiguration.Builder()
                // mandatory
                .setClientId(Constants.UberRideEstimate.CLIENT_ID)
                // required for enhanced button features
                .setServerToken(Constants.UberRideEstimate.SERVER_TOKEN)
                // required for implicit grant authentication
//                .setRedirectUri("<REDIRECT_URI>")
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                // optional: set sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();
        UberSdk.initialize(uberConfig);

        requestButton = new RideRequestButton(OfferDetailActivity.this);

//        RelativeLayout layout = new RelativeLayout(this);
        final RelativeLayout layout = findViewById(R.id.relative_layout);
        layout.addView(requestButton);

        double pickUp_latitude, pickUp_longitude, dropoff_lat, dropoff_lng;
        pickUp_latitude = GPSTracker.getInstance(mContext).getLatitude();
        pickUp_longitude = GPSTracker.getInstance(mContext).getLongitude();
//        pickUp_latitude = 25.325164;
//        pickUp_longitude = 51.533079;
        dropoff_lat = Double.parseDouble(foodOfferDeatil.getLatitude());
        dropoff_lng = Double.parseDouble(foodOfferDeatil.getLongitude());

        RideParameters rideParams = new RideParameters.Builder()
                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
//                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
//                .setDropoffLocation(33.647418, 73.018556, "Uber HQ", "1455 Market Street, San Francisco")
                .setDropoffLocation(dropoff_lat,
                        dropoff_lng,
                        foodOfferDeatil.getMerchantName(),
                        foodOfferDeatil.getMerchantAddress())
                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
                .setPickupLocation(pickUp_latitude,
                        pickUp_longitude,
                        "", "")
                .build();

        mDeepLink = new RideRequestDeeplink.Builder(this)
                .setSessionConfiguration(uberConfig)
                .setFallback(Deeplink.Fallback.APP_INSTALL)
                .setRideParameters(rideParams)
                .build();
        // set parameters for the RideRequestButton instance
        requestButton.setRideParameters(rideParams);

        ServerTokenSession session = new ServerTokenSession(uberConfig);
        requestButton.setSession(session);

        callback = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {
                // react to the displayed estimates
                layout.setVisibility(View.VISIBLE);
                Log.d("UberButn", "onRideInformationLoaded: ");
            }

            @Override
            public void onError(ApiError apiError) {
                // API error details: /docs/riders/references/api#section-errors
                Log.d("UberButn", "onErrorAPI: " + apiError.getClientErrors().get(0).getCode());
            }

            @Override
            public void onError(Throwable throwable) {
                // Unexpected error, very likely an IOException
                Log.d("UberButn", "onError: ");
            }
        };
        requestButton.setCallback(callback);
        requestButton.loadRideInformation();

    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void initViews() {
        utilObj = new Utility(mContext);
        AppInstance.merchantDetailsList = null;
        foodOfferDeatil = new FoodOfferDeatils();
        mOfferManager = new OfferManager(mContext, this);
        strPromotionalPermision = AppPreference.getSetting(mContext, "key_Promotion_acessed", "");
        Log.d("asdasdsasasd", "OnDetail: " + isSubscribed);
        Bundle m_buBundle = getIntent().getExtras();
        if (m_buBundle != null) {
            mCustomerid = m_buBundle.getString(Constants.Request.CUSTOMER_ID);
            mProductid = m_buBundle.getString(Constants.Request.OFFER_ID);
            mName = m_buBundle.getString(Constants.Request.NAME);
            mCatId = m_buBundle.getString(Constants.Request.CATEGORYID);
            mOfferLockStatus = m_buBundle.getString(Constants.OfferDetails.OFFER_LOCK);
//            mMerchantID = m_buBundle.getString(Constants.OfferDetails.MERCHANT_ID);
            mMerchantID = "";
            setActionBar(mName, false);
            Log.d("LOGOO", "initViews: mID " + mMerchantID + " and CID" + mCustomerid + " And name: " + mName);
        } else {
            setActionBar("Offer Detail", false);
        }
        bindViews();

        if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FOOD_ID + "")) {
            catName = getString(R.string.ga_food_drink_offer_details);
        } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.BEAUTY_ID + "")) {
            catName = getString(R.string.ga_beauty_spa_offer_details);
        } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FUN_ID + "")) {
            catName = getString(R.string.ga_fun_activities_offer_details);
        } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.RetailnServices_ID + "")) {
            catName = getString(R.string.ga_health_fitness_offer_details);
        }

        MyApplication.getInstance().trackScreenView(catName);
        MyApplication.getInstance().trackEvent(catName, mName, mName);
        doFetchOfferDetails();
    }

    private void bindViews() {

        mBtnRedeemLayout = (FrameLayout) findViewById(R.id.btnRedeemLayout);


        /*ImageView intialization*/
        rlProductDetail = findViewById(R.id.rl_productdetailImage);
        mImageViewProduct = (ImageView) findViewById(R.id.imageView_product);
        mImageViewMerchant = (ImageView) findViewById(R.id.imageView_merchant);
        mImageViewMerchant.setOnClickListener(this);

         /*TextView intialization*/
        mItemName = (TextView) findViewById(R.id.txtView_itemName);
        mItemAddress = (TextView) findViewById(R.id.txtView_address);
        mDescription = (TextView) findViewById(R.id.txtView_description);
        mPrice = (TextView) findViewById(R.id.txtView_itemPrice);

        mSpecialPrice = (TextView) findViewById(R.id.txtView_itemSpecialPrice);

        rlRedBar = (RelativeLayout) findViewById(R.id.frameLayout_red_bar);
        mItemDiscount = (TextView) findViewById(R.id.txtView_itemDiscount);
        mSavingText = (TextView) findViewById(R.id.txtView_saving);
        mExpiry = (TextView) findViewById(R.id.txtView_expiry);
        mMerchantName = (TextView) findViewById(R.id.txtView_merchantname);
        mTitleDescription = (TextView) findViewById(R.id.txtView_title_description);
        mTimingText = (TextView) findViewById(R.id.txtView_timing_text);
        mTimingValue = (TextView) findViewById(R.id.txtView_timing_value);
        mWhatNeedText = (TextView) findViewById(R.id.txtView_what_need_to_know);
        mOfferSubjectTo = (TextView) findViewById(R.id.txtView_offers_subject_to);
        mFinePrint = (TextView) findViewById(R.id.txtView_fineprint);

        mApproxSaving = (TextView) findViewById(R.id.txtView_approx_saving_price);
        mApproxSavingText = (TextView) findViewById(R.id.txtView_approx_saving);

         /*Button intialization*/
        mBtnCall = (Button) findViewById(R.id.btnCall);
        mBtnCall.setOnClickListener(this);

        mBtnDirection = (Button) findViewById(R.id.btnDirection);
        mBtnDirection.setOnClickListener(this);

        mBtnOutOfStock = (Button) findViewById(R.id.btnOutOfStock);

        mBtnShare = (Button) findViewById(R.id.btnShare);
        mBtnShare.setOnClickListener(this);


        mBtnRedeem = (Button) findViewById(R.id.btnRedeem);
        mBtnRedeem.setOnClickListener(this);
        mBtnRedeemLockIcon = (ImageView) findViewById(R.id.btnRedeemLockIcon);

        mBtnAddToList = (Button) findViewById(R.id.btnAddToList);
        mBtnAddToList.setOnClickListener(this);
        mBtnRuleOfPurchase = (TextView) findViewById(R.id.btnRulesOfPurchase);
        mBtnRuleOfPurchase.setOnClickListener(this);

        mBtnSavedToList = (Button) findViewById(R.id.btnSavedToList);

        /*ProgressBar intialization*/
        mItemImageProgress = (ProgressBar) findViewById(R.id.progress_bar);
        /*Linear Layout initialization*/
        mLLyt_action_buttons = (LinearLayout) findViewById(R.id.lLyt_action_buttons);

        customDialogConfirmationInterfacesCall = new CustomDialogConfirmationInterfaces() {
            @Override
            public void callConfirmationDialogPositive() {

                //THIS IS DONE BCAZ,TO SET PERMISSION FOR CALL IN >=23 SDK VERSION
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                int hasCallPermission = ContextCompat.checkSelfPermission(OfferDetailActivity.this, Manifest.permission.CALL_PHONE);
                if (currentApiVersion > 22 && hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OfferDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CODE_ASK_PERMISSIONS_CALL);
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mPhoneNumber));
                    startActivity(callIntent);
                }
            }

            @Override
            public void callConfirmationDialogNegative() {

            }
        };


        customDialogConfirmationInterfacesDirection = new CustomDialogConfirmationInterfaces() {
            @Override
            public void callConfirmationDialogPositive() {
                callMapView();
            }

            @Override
            public void callConfirmationDialogNegative() {

            }
        };


        // -- For Redeem confirm -- start
        customDialogConfirmationInterfacesRedeem = new CustomDialogConfirmationInterfaces() {
            @Override
            public void callConfirmationDialogPositive() {


                if (catName.equals(getString(R.string.ga_food_drink_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_food_and_drink_offers_detail_redeem_yes), getResources().getString(R.string.ga_event_action_food_and_drink_offers_detail_redeem_yes), getResources().getString(R.string.ga_event_label_food_and_drink_offers_detail_redeem_yes));
                } else if (catName.equals(getString(R.string.ga_beauty_spa_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_beauty_and_spa_offers_details_yes), getResources().getString(R.string.ga_event_action_beauty_and_spa_offers_details_yes), getResources().getString(R.string.ga_event_label_beauty_and_spa_offers_details_yes));
                } else if (catName.equals(getString(R.string.ga_fun_activities_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_fun_activities_offers_details_yes), getResources().getString(R.string.ga_event_action_fun_activities_offers_details_yes), getResources().getString(R.string.ga_event_label_fun_activities_offers_details_yes));
                } else if (catName.equals(getString(R.string.ga_health_fitness_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_health_and_fitness_offers_details_yes), getResources().getString(R.string.ga_event_action_health_and_fitness_offers_details_yes), getResources().getString(R.string.ga_event_label_health_and_fitness_offers_details_yes));
                }
                doRedeemOffer();
            }

            @Override
            public void callConfirmationDialogNegative() {


                if (catName.equals(getString(R.string.ga_food_drink_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_food_and_drink_offers_detail_redeem_no), getResources().getString(R.string.ga_event_action_food_and_drink_offers_detail_redeem_no), getResources().getString(R.string.ga_event_label_food_and_drink_offers_detail_redeem_no));
                } else if (catName.equals(getString(R.string.ga_beauty_spa_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_beauty_and_spa_offers_details_no), getResources().getString(R.string.ga_event_action_beauty_and_spa_offers_details_no), getResources().getString(R.string.ga_event_label_beauty_and_spa_offers_details_no));
                } else if (catName.equals(getString(R.string.ga_fun_activities_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_fun_activities_offers_details_no), getResources().getString(R.string.ga_event_action_fun_activities_offers_details_no), getResources().getString(R.string.ga_event_label_fun_activities_offers_details_no));
                } else if (catName.equals(getString(R.string.ga_health_fitness_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_health_and_fitness_offers_details_no), getResources().getString(R.string.ga_event_action_health_and_fitness_offers_details_no), getResources().getString(R.string.ga_event_label_health_and_fitness_offers_details_no));
                }

                utilObj.showCustomiOSMessageBox(OfferDetailActivity.this, getString(R.string.redeem_confirm_no_message), Constants.TWO_SECONDS);
            }
        };

        //---- For Redeem confirm --end


       /* mBtnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(mContext)) {
                    utilObj.startiOSLoader(OfferDetailActivity.this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                    //assigning the data to the user object
                    String jsonParam = "";
                    try {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(Constants.Request.CUSTOMERID,mCustomerid);
                        jsonObject.put(Constants.Request.OFFER_ID,mProductid);

                        jsonParam = jsonObject.toString();
                        MyApplication.getInstance().printLogs("AddToWishllistJSON", jsonParam);
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }

                    mOfferManager.doAddToWishlist(jsonParam);
                } else {
                    utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                }
            }
        });*/

        rlRedBar.bringToFront();

    }


    @Override
    public void onResume() {
        // When user subscribe from detail Activity and come backs to detail it will update the lock status
        isSubscribed = AppPreference.getSettingResturnsBoolean(mContext, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
        if (!isSubscribed) {
            Log.d("SSSS", "lock On Resume: ");
            mBtnRedeemLockIcon.setVisibility(View.VISIBLE);
        } else {
            Log.d("SSSS", "unlock On Resume: ");
            mBtnRedeemLockIcon.setVisibility(View.GONE);
            mBtnRedeem.setBackgroundResource(R.mipmap.change);
        }

//        if (mName != null && mName.length() > 0) {
//            setActionBar(mName, false);
//            Log.d("LOGOO", "On Resume: mPID " + mProductid + " and CID" + mCustomerid + " And name: " + mName);
//        } else {
//            setActionBar("Offer Detail", false);
//        }

        super.onResume();

    }

    private synchronized void doFetchOfferDetails() {
        if (AppInstance.foodOfferDeatils == null) {

            if (NetworkUtils.isConnected(mContext)) {
                utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                //assigning the data to the user object
                String jsonParam = "";
                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.Request.CUSTOMERID, mCustomerid);
                    jsonObject.put(Constants.Request.OFFER_ID, mProductid);

                    jsonParam = jsonObject.toString();
                    MyApplication.getInstance().printLogs("createOfferDetailJSON", jsonParam);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                mOfferManager.doGetOffersDetails(jsonParam);
            } else {
                utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
            }
        } else {
            setDataInFields();
        }
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppInstance.foodOfferDeatils = null;
                finish();
                return false;
            }
        });

        title1.setText(title);
        getActionBar().setCustomView(customView);
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.GET_FOOD_OFFER_DETAIL_TASK_ID) {

            if (AppInstance.foodOfferDeatils != null) {
                setDataInFields();
            } else {
                hideAllViews();
                utilObj.showToast(mContext, getString(R.string.no_data_received), Toast.LENGTH_LONG);
            }


        } else if (taskID == Constants.TaskID.ADD_TO_WISHLIST_TASK_ID) {

            AddToWishlist addToWishlist = AppInstance.addToWishlist;
            if (addToWishlist != null) {
                if (addToWishlist.getStatus().equalsIgnoreCase(ResponseCodes.STATUS_ZERO)) {
                    utilObj.showCustomAlertDialog(OfferDetailActivity.this, null, getString(R.string.invalid_message), null, null, false, null);
                } else {
                    mBtnAddToList.setVisibility(View.GONE);
                    mBtnSavedToList.setVisibility(View.VISIBLE);
                    AppInstance.foodOfferDeatils.setWishlist_item(Constants.DEFAULT_VALUES.ONE);
                    AppInstance.wishListList = null;
                    Bundle bundle = new Bundle();
                    int wishListCount = AppInstance.getOffersMyWishListCountToDisplay() + 1;
                    AppInstance.setOffersMyWishListCountToDisplay(wishListCount);
                    bundle.putInt(Constants.DEFAULT_VALUES.MY_WISH_LIST_COUNT, (wishListCount));
                    utilObj.generateEvent(this, Constants.DEFAULT_VALUES.MY_WISH_LIST_UPDATED, bundle, getString(R.string.tab_my_wish_list_dynamic));
                    utilObj.showCustomiOSMessageBox(this, getString(R.string.added_to_ur_wish_list), Constants.TWO_SECONDS);
                }
            } else {
                utilObj.showToast(this, getResources().getString(R.string.no_data_received), 1);
            }

        }
    }


    private void setDataInFields() {


        showAllViews();

        foodOfferDeatil = AppInstance.foodOfferDeatils;
//        if (!utilObj.isLocationEnabled(mContext) || !utilObj.checkPermission(mContext)) {
//            Log.d("PERMISSS", "Not allowed ");
//        } else {
//            Log.d("PERMISSS", "Allowed ");
//            updateUberSDK();
//        }

        mPhoneNumber = foodOfferDeatil.getPhone();
        //call the intent for the next activity
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.no_image_white)
                .showImageForEmptyUri(R.mipmap.no_image_white)
                .showImageOnFail(R.mipmap.no_image_white).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        if (foodOfferDeatil.getImageURL() != null) {
            imageLoader.displayImage(foodOfferDeatil.getImageURL(), mImageViewProduct, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    mItemImageProgress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    mItemImageProgress.setVisibility(View.GONE);
                    mImageViewProduct.setImageResource(R.mipmap.no_image_white);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mItemImageProgress.setVisibility(View.GONE);
                    Log.d("dsfsadfjdsjf", "onCreate: Imgevw width" + mImageViewProduct.getWidth());
                    Log.d("dsfsadfjdsjf", "onCreate: Imgevw height" + mImageViewProduct.getHeight());
                }
            });
        } else {
            mImageViewProduct.setImageResource(R.mipmap.no_image_white);
        }

        if (foodOfferDeatil.getMerchantimage() != null) {
            imageLoader.displayImage(foodOfferDeatil.getMerchantimage(), mImageViewMerchant, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    //mItemImageProgress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    //mItemImageProgress.setVisibility(View.GONE);
                    mImageViewMerchant.setImageResource(R.mipmap.no_image_white);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    // mItemImageProgress.setVisibility(View.GONE);
                }
            });
        } else {
            mImageViewMerchant.setImageResource(R.mipmap.no_image_white);
        }
        if (foodOfferDeatil.getMerchantId() != null) {
            mMerchantID = foodOfferDeatil.getMerchantId();
        }

                /*TO Display OR Hide the Add to List button  */

        if (foodOfferDeatil.getWishlist_item().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
            mBtnAddToList.setVisibility(View.VISIBLE);
            mBtnSavedToList.setVisibility(View.GONE);
        } else {
            mBtnAddToList.setVisibility(View.GONE);
            mBtnSavedToList.setVisibility(View.VISIBLE);
        }

        Log.d("sdasdasdqw", "setDataInFields: " + foodOfferDeatil.getStatus());
        if (foodOfferDeatil.getStatus().equals("expired")) {
            mBtnRedeem.setText("Expired");
            mBtnRedeem.setBackground(getResources().getDrawable(R.mipmap.active_btn));
            mBtnRedeem.setClickable(false);
        }

        if (!foodOfferDeatil.getItemName().equalsIgnoreCase("null"))
            mItemName.setText(foodOfferDeatil.getItemName());

        if (!foodOfferDeatil.getMerchantAddress().equalsIgnoreCase("null"))
            mItemAddress.setText(foodOfferDeatil.getMerchantAddress());

        if (!foodOfferDeatil.getSpecialPrice().equalsIgnoreCase("null")) {
            mSpecialPrice.setVisibility(View.VISIBLE);

            String receivedSpecialPrice = foodOfferDeatil.getSpecialPrice();

            String receivedItemPrice = foodOfferDeatil.getPrice();
            String formattedReceivedItemPrice = "";
            //------------
            if (receivedSpecialPrice.contains("-")) {
                formattedReceivedItemPrice = getString(R.string.price_unit) + "" + receivedSpecialPrice;
                mSpecialPrice.setText(formattedReceivedItemPrice);
            } else {
                double a = Double.parseDouble(receivedSpecialPrice);
                BigDecimal bd = new BigDecimal(a);
                BigDecimal bigDecimal = bd.setScale(0, RoundingMode.DOWN);
                formattedReceivedItemPrice = getString(R.string.price_unit) + "" + bigDecimal.toPlainString();
                mSpecialPrice.setText(formattedReceivedItemPrice);
            }
            //------------

            if (!receivedItemPrice.equalsIgnoreCase("null")) {
                mSpecialPrice.setVisibility(View.VISIBLE);

                //THIS IS DONE TO SHOW SPECIAL PRICE IN NEXT LINE WHEN BOTH PRICE(ITEM & SPECIAL) CONTAINS '-'.
               /* if (receivedSpecialPrice.contains("-") && receivedItemPrice.contains("-")) {
                    mSpecialPrice.setVisibility(View.GONE);
                    mSpecialPrice_NewLine.setVisibility(View.VISIBLE);
                    mSpecialPrice_NewLine.setText(formattedReceivedItemPrice);
                }*/

                //------------
                if (receivedItemPrice.contains("-")) {
                    mPrice.setText(getString(R.string.price_unit) + receivedItemPrice);
                } else {
                    double a1 = Double.parseDouble(receivedItemPrice);
                    BigDecimal bd1 = new BigDecimal(a1);
                    BigDecimal res1 = bd1.setScale(0, RoundingMode.DOWN);
                    mPrice.setText(getString(R.string.price_unit) + res1.toPlainString());
                }
                mPrice.setPaintFlags(mPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //----------


            } else {
                // mPrice.setVisibility(View.GONE);
            }
        } else {
            //mSpecialPrice.setVisibility(View.GONE);
            if (!foodOfferDeatil.getPrice().equalsIgnoreCase("null")) {

                if (foodOfferDeatil.getPrice().contains("-")) {
                    mPrice.setText(getString(R.string.price_unit) + foodOfferDeatil.getPrice());
                } else {
                    double a = Double.parseDouble(foodOfferDeatil.getPrice());
                    BigDecimal bd = new BigDecimal(a);
                    BigDecimal res = bd.setScale(0, RoundingMode.DOWN);
                    mPrice.setText(getString(R.string.price_unit) + res.toPlainString());
                }

            } else {
                //mPrice.setVisibility(View.GONE);
            }
        }

        if (!foodOfferDeatil.getDiscount().equalsIgnoreCase("null")) {
            mSavingText.setVisibility(View.VISIBLE);
            mItemDiscount.setVisibility(View.VISIBLE);
            mItemDiscount.setText(foodOfferDeatil.getDiscount() + "%");
        } else {
            mSavingText.setVisibility(View.GONE);
            mItemDiscount.setVisibility(View.GONE);
        }

        if (!foodOfferDeatil.getDescription().equalsIgnoreCase("null")) {
            mDescription.setText(foodOfferDeatil.getDescription());
        }

        if (!foodOfferDeatil.getExpiredate().equalsIgnoreCase("null")) {
            String date = foodOfferDeatil.getExpiredate();
            String[] parts = date.split(" ");//2015-12-08 07:02:23
            String date1 = parts[0];
            String time1 = parts[1];
            String date2 = Utility.convertDate(date1, "yyyy-MM-dd", "dd MMM, yyyy");


            // mExpiry.setText("Expires On: "+date2);
            mExpiry.setText(getString(R.string.txt_expiry) + " " + date2);
        } else {
            mExpiry.setText("");
        }

        if (!foodOfferDeatil.getMerchantName().equalsIgnoreCase("null")) {
            mMerchantName.setText(foodOfferDeatil.getMerchantName());
            mName = foodOfferDeatil.getMerchantName();
        } else {
            mMerchantName.setText("");
            mName = "";
        }
        if (!foodOfferDeatil.getMerchantAddress().equalsIgnoreCase("null")) {
            mItemAddress.setText(foodOfferDeatil.getMerchantAddress());
        } else {
            mItemAddress.setText("");
        }

        if (!foodOfferDeatil.getTimings().equalsIgnoreCase("null")) {
            //mTimingValue.setText(foodOfferDeatil.getTimings());
            mTimingValue.setText(Html.fromHtml(foodOfferDeatil.getTimings()));
        } else {
            mTimingValue.setText("");
        }

        if (foodOfferDeatil.getStatus().equalsIgnoreCase(getString(R.string.out_of_stock))) {
            mBtnRedeem.setVisibility(View.GONE);
            mBtnRedeemLayout.setVisibility(View.GONE);
            mBtnOutOfStock.setText(getString(R.string.btn_out_of_stock));
            mBtnOutOfStock.setVisibility(View.VISIBLE);
        } else if (foodOfferDeatil.getStatus().equalsIgnoreCase(getString(R.string.purchased))) {
            mBtnRedeem.setVisibility(View.GONE);
            mBtnRedeemLayout.setVisibility(View.GONE);
            mBtnOutOfStock.setText(getString(R.string.btn_sold_out));
            mBtnOutOfStock.setVisibility(View.VISIBLE);
        } else {
            mBtnRedeemLayout.setVisibility(View.VISIBLE);
            mBtnRedeem.setVisibility(View.VISIBLE);
            mBtnOutOfStock.setVisibility(View.GONE);

            if (!isSubscribed) {
                Log.d("SSSS", "lock On Sucess: ");

                // mBtnRedeem.setBackgroundResource(R.mipmap.redeem_lock_icon);
                mBtnRedeemLockIcon.setVisibility(View.VISIBLE);
            } else {
                Log.d("SSSS", "unlock On Sucess: ");

                mBtnRedeem.setBackgroundResource(R.mipmap.change);
            }
        }

        if (foodOfferDeatil.getWishlist_item().equalsIgnoreCase("0")) {
            mBtnAddToList.setVisibility(View.VISIBLE);
            mBtnSavedToList.setVisibility(View.GONE);
        } else {
            mBtnAddToList.setVisibility(View.GONE);
            mBtnSavedToList.setVisibility(View.VISIBLE);
        }

        if (foodOfferDeatil.getFineprint().equalsIgnoreCase("null")) {
            mFinePrint.setVisibility(View.GONE);
        } else {
            mFinePrint.setVisibility(View.VISIBLE);
            mFinePrint.setText(foodOfferDeatil.getFineprint());
        }

        if (foodOfferDeatil.getApproximateSavings().equalsIgnoreCase("null")) {
            mApproxSavingText.setVisibility(View.GONE);
            mApproxSaving.setVisibility(View.GONE);
        } else {
            mApproxSavingText.setVisibility(View.VISIBLE);
            mApproxSaving.setVisibility(View.VISIBLE);
            mApproxSaving.setText(getString(R.string.price_unit) + foodOfferDeatil.getApproximateSavings().trim());
        }


    }

    private void hideAllViews() {

        mBtnCall.setEnabled(false);
        mBtnDirection.setEnabled(false);
        mBtnShare.setEnabled(false);

               /*Set Visiblity GONE*/
        mLLyt_action_buttons.setVisibility(View.GONE);
        mImageViewProduct.setVisibility(View.GONE);
        mImageViewMerchant.setVisibility(View.GONE);
        mItemName.setVisibility(View.GONE);
        mItemAddress.setVisibility(View.GONE);
        mDescription.setVisibility(View.GONE);
        mPrice.setVisibility(View.GONE);
        mSpecialPrice.setVisibility(View.GONE);

        mItemDiscount.setVisibility(View.GONE);
        mSavingText.setVisibility(View.GONE);
        mExpiry.setVisibility(View.GONE);
        mMerchantName.setVisibility(View.GONE);
        mItemDiscount.setVisibility(View.GONE);
        mSavingText.setVisibility(View.GONE);
        mExpiry.setVisibility(View.GONE);
        mMerchantName.setVisibility(View.GONE);
        mItemImageProgress.setVisibility(View.GONE);
        mTitleDescription.setVisibility(View.GONE);
        mTimingText.setVisibility(View.INVISIBLE);
        mTimingValue.setVisibility(View.INVISIBLE);
        mWhatNeedText.setVisibility(View.GONE);
        mBtnRuleOfPurchase.setVisibility(View.GONE);
        mOfferSubjectTo.setVisibility(View.GONE);
        mFinePrint.setVisibility(View.GONE);
                /*End of Set Visibility GONE*/


    }

    private void showAllViews() {

        mBtnCall.setEnabled(true);
        mBtnDirection.setEnabled(true);
        mBtnShare.setEnabled(true);

         /*Set Visiblity*/
        mLLyt_action_buttons.setVisibility(View.VISIBLE);
        mImageViewProduct.setVisibility(View.VISIBLE);
        mImageViewMerchant.setVisibility(View.VISIBLE);
        mItemName.setVisibility(View.VISIBLE);
        mItemAddress.setVisibility(View.VISIBLE);
        mDescription.setVisibility(View.VISIBLE);
        mPrice.setVisibility(View.VISIBLE);
        mSpecialPrice.setVisibility(View.VISIBLE);

        mItemDiscount.setVisibility(View.VISIBLE);
        mSavingText.setVisibility(View.VISIBLE);
        mExpiry.setVisibility(View.VISIBLE);
        mMerchantName.setVisibility(View.VISIBLE);
        mItemDiscount.setVisibility(View.VISIBLE);
        mSavingText.setVisibility(View.VISIBLE);
        mExpiry.setVisibility(View.VISIBLE);
        mMerchantName.setVisibility(View.VISIBLE);
        mItemImageProgress.setVisibility(View.VISIBLE);
        mTitleDescription.setVisibility(View.VISIBLE);

        mTimingText.setVisibility(View.VISIBLE);
        mTimingValue.setVisibility(View.VISIBLE);
        mWhatNeedText.setVisibility(View.VISIBLE);

        mBtnRuleOfPurchase.setVisibility(View.VISIBLE);
        mOfferSubjectTo.setVisibility(View.VISIBLE);

                /*Ens of Set Visibility*/
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        mLLyt_action_buttons.setVisibility(View.VISIBLE);
        utilObj.stopiOSLoader();
        FlurryAgent.onError("", errorMessage, "");
        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uber_estimate_rl_container:
                boolean isAppInstalled = appInstalledOrNot("com.ubercab");
                if (isAppInstalled) {
                    //This intent will help you to launch if the package is already installed
                    Intent LaunchIntent = getPackageManager()
                            .getLaunchIntentForPackage("com.ubercab");
                    startActivity(LaunchIntent);

                    Log.d("APPINSTALLED", "Application is already installed.");
                } else {
                    // Do whatever we want to do if application not installed
                    // Redirect to play store
//                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ubercab"));
                    startActivity(new Intent(Intent.ACTION_VIEW, mDeepLink.getUri()));
//                    startActivity(i);

                    Log.d("APPINSTALLED", "Application is not currently installed.");
                }
                break;
            case R.id.btnRulesOfPurchase: {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra(Constants.Request.PAGE, Constants.StaticHtmlPage.REDEEM_RULES);
                intent.putExtra(Constants.DEFAULT_VALUES.ACTION_BAR_HEADER, getString(R.string.rules_of_purchase));

                startActivity(intent);
                break;
            }
            case R.id.btnCall: {

                String category_name = "";
                if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FOOD_ID + "")) {
                    category_name = "Food and Drink";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_food_and_drink_offers_detail_CallFromApp), getResources().getString(R.string.ga_event_action_food_and_drink_offers_detail_CallFromApp), getResources().getString(R.string.ga_event_label_food_and_drink_offers_detail_CallFromApp));
                } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.BEAUTY_ID + "")) {
                    category_name = "Beauty and Spa";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_beauty_and_spa_offers_details_CallFromApp), getResources().getString(R.string.ga_event_action_beauty_and_spa_offers_details_CallFromApp), getResources().getString(R.string.ga_event_label_beauty_and_spa_offers_details_CallFromApp));
                } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FUN_ID + "")) {
                    category_name = "Fun Activities";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_fun_activities_offers_details_CallFromApp), getResources().getString(R.string.ga_event_action_fun_activities_offers_details_CallFromApp), getResources().getString(R.string.ga_event_label_fun_activities_offers_details_CallFromApp));
                } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.RetailnServices_ID + "")) {
                    category_name = "Health and Fitness";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_health_and_fitness_offers_details_CallFromApp), getResources().getString(R.string.ga_event_action_health_and_fitness_offers_details_CallFromApp), getResources().getString(R.string.ga_event_label_health_and_fitness_offers_details_CallFromApp));
                }


                utilObj.showCustomAlertDialog(OfferDetailActivity.this, null, mPhoneNumber, getString(R.string.call), getString(R.string.cancel), true, customDialogConfirmationInterfacesCall);
                break;
            }
            case R.id.btnDirection: {
                // utilObj.showCustomAlertDialog(OfferDetailActivity.this, null, getString(R.string.direction_confirmation), getString(R.string.open), getString(R.string.cancel), true, customDialogConfirmationInterfacesDirection);


                String category_name = "";
                if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FOOD_ID + "")) {
                    category_name = "Food and Drink";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_food_and_drink_offers_detail_NavigateToMap), getResources().getString(R.string.ga_event_action_food_and_drink_offers_detail_NavigateToMap), getResources().getString(R.string.ga_event_label_food_and_drink_offers_detail_NavigateToMap));
                } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.BEAUTY_ID + "")) {
                    category_name = "Beauty and Spa";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_beauty_and_spa_offers_details_NavigateToMap), getResources().getString(R.string.ga_event_action_beauty_and_spa_offers_details_NavigateToMap), getResources().getString(R.string.ga_event_label_beauty_and_spa_offers_details_NavigateToMap));
                } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FUN_ID + "")) {
                    category_name = "Fun Activities";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_fun_activities_offers_details_NavigateToMap), getResources().getString(R.string.ga_event_action_fun_activities_offers_details_NavigateToMap), getResources().getString(R.string.ga_event_label_fun_activities_offers_details_NavigateToMap));
                } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.RetailnServices_ID + "")) {
                    category_name = "Health and Fitness";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_health_and_fitness_offers_details_NavigateToMap), getResources().getString(R.string.ga_event_action_health_and_fitness_offers_details_NavigateToMap), getResources().getString(R.string.ga_event_label_health_and_fitness_offers_details_NavigateToMap));
                }


                callMapView();
                break;
            }
            case R.id.btnShare: {
                String category_name = "";
                if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FOOD_ID + "")) {
                    category_name = "Food and Drink";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_food_and_drink_offers_detail_share), getResources().getString(R.string.ga_event_action_food_and_drink_offers_detail_share), getResources().getString(R.string.ga_event_label_food_and_drink_offers_detail_share));
                } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.BEAUTY_ID + "")) {
                    category_name = "Beauty and Spa";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_beauty_and_spa_offers_details_Share), getResources().getString(R.string.ga_event_action_beauty_and_spa_offers_details_Share), getResources().getString(R.string.ga_event_label_beauty_and_spa_offers_details_Share));
                } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FUN_ID + "")) {
                    category_name = "Fun Activities";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_fun_activities_offers_details_Share), getResources().getString(R.string.ga_event_action_fun_activities_offers_details_Share), getResources().getString(R.string.ga_event_label_fun_activities_offers_details_Share));
                } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.RetailnServices_ID + "")) {
                    category_name = "Health and Fitness";
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_health_and_fitness_offers_details_Share), getResources().getString(R.string.ga_event_action_health_and_fitness_offers_details_Share), getResources().getString(R.string.ga_event_label_health_and_fitness_offers_details_Share));
                }


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, foodOfferDeatil.getItemName());
                //  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, foodOfferDeatil.getImageURL());
                String formattedString = String.format(getString(R.string.offer_share_message_text), foodOfferDeatil.getItemName(), foodOfferDeatil.getMerchantName(), foodOfferDeatil.getDiscount() + "%", Constants.DEFAULT_VALUES.SHARE_URL);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, formattedString);
                Log.d("sadsadsdsad", "onClick: " + formattedString);
                startActivity(Intent.createChooser(sharingIntent, "Select"));
                break;
            }

            case R.id.btnRedeem: {
                Log.d("asdasdqw", "OnOfferDetail main:" + AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD, ""));

                if (isSubscribed) {
                    doConfirmToRedeemOption();
                } else {
                    Log.d("asdasdqw", "OnOfferDetail :" + AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD, ""));

                    if (AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD, "").length() > 0) {
                        Intent intent = new Intent(this, OfferPackagesActivity.class);
                        startActivity(intent);
                    } else {
                        Bundle m_bundle = new Bundle();
                        m_bundle.putString(Constants.Request.CUSTOMER_ID, mCustomerid);
                        m_bundle.putString(Constants.Request.OFFER_ID, mProductid);
                        m_bundle.putString(Constants.Request.NAME, mName);
                        m_bundle.putString(Constants.Request.CATEGORYID, mCatId);
                        Intent intent = new Intent(this, PaymentProcessVodafoneSelectionActivity.class);
                        intent.putExtras(m_bundle);
                        startActivity(intent);
                    }
                }
                break;
            }

            case R.id.btnAddToList: {
                if (NetworkUtils.isConnected(mContext)) {
                    utilObj.startiOSLoader(OfferDetailActivity.this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                    String category_name = "";
                    if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FOOD_ID + "")) {
                        category_name = "Food and Drink";
                        MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_food_and_drink_offers_detail_add_fav), getResources().getString(R.string.ga_event_action_food_and_drink_offers_detail_add_fav), getResources().getString(R.string.ga_event_label_food_and_drink_offers_detail_add_fav));
                    } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.BEAUTY_ID + "")) {
                        category_name = "Beauty and Spa";
                        MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_beauty_and_spa_offers_details_AddtoFav), getResources().getString(R.string.ga_event_action_beauty_and_spa_offers_details_AddtoFav), getResources().getString(R.string.ga_event_label_beauty_and_spa_offers_details_AddtoFav));
                    } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.FUN_ID + "")) {
                        category_name = "Fun Activities";
                        MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_fun_activities_offers_details_AddtoFav), getResources().getString(R.string.ga_event_action_fun_activities_offers_details_AddtoFav), getResources().getString(R.string.ga_event_label_fun_activities_offers_details_AddtoFav));
                    } else if (mCatId != null && !mCatId.isEmpty() && mCatId.equalsIgnoreCase(Constants.IntentPreference.RetailnServices_ID + "")) {
                        category_name = "Health and Fitness";
                        MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_health_and_fitness_offers_details_AddtoFav), getResources().getString(R.string.ga_event_action_health_and_fitness_offers_details_AddtoFav), getResources().getString(R.string.ga_event_label_health_and_fitness_offers_details_AddtoFav));
                    }

                    mOfferManager.doAddToWishlist(mProductid);
                } else {
                    utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                }
                break;
            }

            case R.id.imageView_merchant:
                Log.d("sfddfwqerc", "onClick: " + mMerchantID);
                Bundle m_bundle = new Bundle();
                m_bundle.putString(Constants.Request.CUSTOMER_ID, mCustomerid);
                m_bundle.putString(Constants.Request.MERCHANT_ID, mMerchantID);
                m_bundle.putString(Constants.Request.NAME, mName);
                m_bundle.putString(Constants.Request.CATEGORYID, mCatId);
                Intent intent = new Intent(mContext, MerchantDetailActivity.class);
                intent.putExtras(m_bundle);
                startActivity(intent);

                break;
        }
    }


    public void callMapView() {
        if (!foodOfferDeatil.getLatitude().equals("")) {
            //mLatitude = Double.parseDouble(mEventDetailsModel.getmLatitude());
            try {
                String url = "http://maps.google.com/maps?f=d&daddr=" + foodOfferDeatil.getLatitude()
                        + "," + foodOfferDeatil.getLongitude() + "&dirflg=d";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");

                MyApplication.getInstance().printLogs("MAP", url);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?f=d&daddr=" + foodOfferDeatil.getLatitude()
                                + "," + foodOfferDeatil.getLongitude()));
                MyApplication.getInstance().printLogs("MAP EXCEPTION", e.getMessage());
                startActivity(intent);
            }


        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppInstance.foodOfferDeatils = null;
        MyApplication.getInstance().printLogs("OfferDetailActivity BackPressed Called", " AppInstance.foodOfferDeatils : " + AppInstance.foodOfferDeatils);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    // ------------- Redeem Functionality -- START
    private void doConfirmToRedeemOption() {
        utilObj.showCustomAlertDialog(this, "Redeem", getString(R.string.redeem_confirm_message), getString(R.string.yes), getString(R.string.no), true, customDialogConfirmationInterfacesRedeem);
    }

    private void doRedeemOffer() { // IF USER ALREADY REDEEM(OFFER_LOCK_STATUS =1 ), THEN SHOW PAYMENT SCREEN
        // OPEN REDEEM ACTIVITY IF NO REDEEM PREVIOUSLY
        String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
        String productid = mProductid;
        String merchantName = foodOfferDeatil.getMerchantName();
        String merchantAddress = foodOfferDeatil.getMerchantAddress();
        String itemName = foodOfferDeatil.getItemName();
        String productImage = foodOfferDeatil.getImageURL();
        String merchantImage = foodOfferDeatil.getMerchantimage();
        String specialprice = foodOfferDeatil.getSpecialPrice();
        String merchantID = foodOfferDeatil.getMerchantId();
        String status = foodOfferDeatil.getStatus();

        Bundle m_bundle = new Bundle();
        m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
        m_bundle.putString(Constants.Request.OFFER_ID, productid);
        m_bundle.putString(Constants.Request.IMAGE_URL, productImage);
        m_bundle.putString(Constants.Request.MERCHANT_IMAGE_URL, merchantImage);
        m_bundle.putString(Constants.Request.SPECIAL_PRICE, specialprice);
        m_bundle.putString(Constants.OfferDetails.MERCHANT_NAME, merchantName);
        m_bundle.putString(Constants.OfferDetails.MERCHANT_ADDRESS, merchantAddress);
        m_bundle.putString(Constants.Request.NAME, itemName);
        m_bundle.putString(Constants.Request.MERCHANT_ID, mMerchantID);
        m_bundle.putString(Constants.Request.STATUS, status);
        Log.d("LOGOO", "doREDEEM: mID " + mMerchantID + " and CID" + mCustomerid + " And name: " + mName);

//            Intent intent = new Intent(OfferDetailActivity.this, RedeemCreateActivity.class);
//            intent.putExtras(m_bundle);
//            startActivity(intent);


        MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_redemption_correct_user_pin), getResources().getString(R.string.ga_event_action_redemption_correct_user_pin), getResources().getString(R.string.ga_event_label_redemption_correct_user_pin));
        Intent intent = new Intent(OfferDetailActivity.this, RedeemConfirmPurchaseActivity.class);
//        Intent intent = new Intent(OfferDetailActivity.this, PurchaseSuccessActivity.class);
        intent.putExtras(m_bundle);
        startActivity(intent);


    }
    // ------------- Redeem Functionality -- END


    // THIS IS USE TO CHECK PERMISSION ON <= 23 SDK VERSION.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS_CALL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mPhoneNumber));
                    startActivity(callIntent);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
