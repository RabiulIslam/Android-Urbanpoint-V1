package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.Manifest;

import com.uber.sdk.android.core.Deeplink;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.android.rides.RideRequestDeeplink;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.core.client.SessionConfiguration;
import com.urbanpoint.UrbanPoint.BuildConfig;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.adapters.categoryScreensAdapters.MerchantOfferListAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.MerchantDetailsList;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.MerchantItemDetails;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.MerchintDetail_WebHit_Get_Uber_price;
import com.urbanpoint.UrbanPoint.managers.MerchintDetail_WebHit_Get_Uber_time;
import com.urbanpoint.UrbanPoint.managers.categoryScreens.MerchantManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import java.util.ArrayList;
import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aparnasalve on 8/3/16.
 */
public class MerchantDetailActivity extends Activity implements ServiceRedirection, CustomDialogConfirmationInterfaces, View.OnClickListener {
    private Context mContext;
    private Utility utilObj;
    ImageView mImageViewProduct, mImageViewMerchant;
    private TextView mItemName, mItemAddress, mPhone, mDescription, mSpecialPrice, mItemDiscount, mSavingText, mExpiry, mMerchantName, mTitleDescription, mTimingText, mTimingValue, mWhatNeedText, mOfferSubjectTo;
    DisplayImageOptions options;
    Button mBtnCall, mBtnDirection, mBtnShare;
    private LinearLayout mLayoutZomatoRating;
    private Button mBtnOffer;
    private ImageView mBackButton;
    private final int REQUEST_CODE_ASK_PERMISSIONS_CALL = 0;
    private final int REQUEST_CODE_ASK_PERMISSIONS_GMAP = 1;
    private String mLatitude, mLongitude;
    private ListView mMerchantDetailListView;
    CustomDialogConfirmationInterfaces customDialogConfirmationInterfacesCall, customDialogConfirmationInterfacesDirection;
    LinearLayout mItemDetailLayout;
    MerchantManager mMerchantManager;
    String mCustomerid, mName, mPhoneNumber;
    private static String mMMerchantid;
    private MerchantDetailsList merchantDetailsList;
    ImageLoader imageLoader;
    ListView mOfferListView;
    private ArrayList<MerchantItemDetails> mProductListToShow;
    private TextView mZomatoRating;
    private String mCatID;
    String catName = "";
    private String strPromotionalPermision;
    private boolean isSubscribed, isUberLoaded, isPriceLoaded, isTimeLoaded;
    private RelativeLayout layout;
    private SessionConfiguration uberConfig;
    private RideRequestButton requestButton;
    private RideRequestButtonCallback callback;
    private RideRequestDeeplink mDeepLink;
    private LinearLayout llUberEstimate, llUberEstimationCost;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Typeface novaThin, novaRegular;
    private CustomDialogConfirmationInterfaces contextualDialogConfirmationInterfacesLocation;
    private TextView txvDuration, txvEstimate;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_detail_listview);
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        mContext = getApplicationContext();
        initViews();
        // MyApplication.getInstance().trackScreenView(getString(R.string.fun_activity_outlet));
        Intent intent = getIntent();
        AppInstance.foodOfferDeatils = null;
        if (intent != null) {
            mCatID = intent.getStringExtra(Constants.Request.CATEGORYID);
        } else {
            mCatID = "";
        }


        if (mCatID != null && !mCatID.isEmpty() && mCatID.equalsIgnoreCase(Constants.IntentPreference.FOOD_ID + "")) {
            catName = getString(R.string.food_and_drink_outlets);
        } else if (mCatID != null && !mCatID.isEmpty() && mCatID.equalsIgnoreCase(Constants.IntentPreference.BEAUTY_ID + "")) {
            catName = getString(R.string.beauty_spa_outlet);
        } else if (mCatID != null && !mCatID.isEmpty() && mCatID.equalsIgnoreCase(Constants.IntentPreference.FUN_ID + "")) {
            catName = getString(R.string.fun_activity_outlet);
        } else if (mCatID != null && !mCatID.isEmpty() && mCatID.equalsIgnoreCase(Constants.IntentPreference.RetailnServices_ID + "")) {
            catName = getString(R.string.health_fitness_outlet);
        }

        MyApplication.getInstance().trackScreenView(catName);
        MyApplication.getInstance().trackEvent(catName, mName, mName);

    }

    private void initViews() {
        utilObj = new Utility(mContext);
        mLatitude = "";
        mLongitude = "";
        isUberLoaded = true;
        isPriceLoaded = false;
        isTimeLoaded = false;
        mMerchantManager = new MerchantManager(mContext, this);
        strPromotionalPermision = AppPreference.getSetting(mContext, "key_Promotion_acessed", "");
        isSubscribed = AppPreference.getSettingResturnsBoolean(mContext, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
        novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        Bundle m_buBundle = getIntent().getExtras();
        if (m_buBundle != null) {
            mCustomerid = m_buBundle.getString(Constants.Request.CUSTOMER_ID);
            mMMerchantid = m_buBundle.getString(Constants.Request.MERCHANT_ID);
            mName = m_buBundle.getString(Constants.Request.NAME);
            setActionBar(mName, true);
        }
        bindViews();


        contextualDialogConfirmationInterfacesLocation = new CustomDialogConfirmationInterfaces() {
            @Override
            public void callConfirmationDialogPositive() {
                if (NetworkUtils.isConnected(mContext)) {
                    if (utilObj.isLocationEnabled(mContext)) {
                        requestPermission();
                    } else {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.IntentPreference.SOURCE_LOCATION_INTENT_CODE);
                    }
                } else {
                    utilObj.showCustomAlertDialog(mContext, null, getString(R.string.no_internet), null, null, false, null);
                }
            }

            @Override
            public void callConfirmationDialogNegative() {

            }
        };

        doCallMerchantDetail();
    }

    private void bindViews() {
        //mBtnCall.setOnClickListener(this);

        mOfferListView = (ListView) findViewById(R.id.merchant_list);
        listHeaderSetting();
        mOfferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MerchantDetailsList merchantDetailsList1 = AppInstance.merchantDetailsList;
                MerchantItemDetails merchantItemDetails = mProductListToShow.get(position - 1);
                if (merchantDetailsList1 != null) {
                    if (merchantDetailsList1.getMerchantItemDetails() != null && merchantDetailsList1.getMerchantItemDetails().size() > 0) {
                        boolean contains = merchantDetailsList1.getMerchantItemDetails().contains(merchantItemDetails);
                        if (contains) {
                            String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                            //String productid=AppPreference.getSetting(mContext,Constants.Request.OFFER_ID,"");
                            String productId = merchantItemDetails.getProductid();
                            String itemName = merchantItemDetails.getProductname();
                            Bundle m_bundle = new Bundle();
                            m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
                            m_bundle.putString(Constants.Request.OFFER_ID, productId);
                            m_bundle.putString(Constants.Request.NAME, itemName);
                            m_bundle.putString(Constants.Request.CATEGORYID, mCatID);
//                            Log.d("SSSS", "onItemClick: "+AppInstance.offers.isOfferGetsLocked(mContext));
                            if (AppInstance.offers != null) {
                                String lockOffersStatusFlag = AppInstance.offers.isOfferGetsLocked(mContext); //AppInstance.offers.getLockOffersStatusFlag();
                                m_bundle.putString(Constants.OfferDetails.OFFER_LOCK, lockOffersStatusFlag);
                            }
                            if (strPromotionalPermision.length() > 0) {
                                m_bundle.putString(Constants.OfferDetails.OFFER_LOCK, "1");
                            }

                            if (catName.equals(getString(R.string.ga_food_drink_offer_details))) {
                                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_food_and_drink_outlet_detail_SeeOffer), getResources().getString(R.string.ga_event_action_food_and_drink_outlet_detail_SeeOffer), getResources().getString(R.string.ga_event_label_food_and_drink_outlet_detail_SeeOffer));
                            } else if (catName.equals(getString(R.string.ga_beauty_spa_offer_details))) {

                                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_beauty_and_spa_outlet_detail_SeeOffer), getResources().getString(R.string.ga_event_action_beauty_and_spa_outlet_detail_SeeOffer), getResources().getString(R.string.ga_event_label_beauty_and_spa_outlet_detail_SeeOffer));
                            } else if (catName.equals(getString(R.string.ga_fun_activities_offer_details))) {
                                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_fun_activities_outlet_detail_SeeOffer), getResources().getString(R.string.ga_event_action_fun_activities_outlet_detail_SeeOffer), getResources().getString(R.string.ga_event_label_fun_activities_outlet_detail_SeeOffer));
                            } else if (catName.equals(getString(R.string.ga_health_fitness_offer_details))) {
                                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_health_and_fitness_outlet_detail_SeeOffer), getResources().getString(R.string.ga_event_action_health_and_fitness_outlet_detail_SeeOffer), getResources().getString(R.string.ga_event_label_health_and_fitness_outlet_detail_SeeOffer));
                            }

                            isUberLoaded = true;
                            Intent intent = new Intent(MerchantDetailActivity.this, OfferDetailActivity.class);
                            intent.putExtras(m_bundle);
                            startActivity(intent);
                        }
                    } else {
                        utilObj.showToast(mContext, mContext.getResources().getString(R.string.invalid_message), Toast.LENGTH_LONG);
                    }
                } else {
                    utilObj.showToast(mContext, mContext.getResources().getString(R.string.invalid_message), Toast.LENGTH_LONG);
                }

            }
        });
        customDialogConfirmationInterfacesCall = new CustomDialogConfirmationInterfaces() {
            @Override
            public void callConfirmationDialogPositive() {

                //THIS IS DONE BCAZ,TO SET PERMISSION FOR CALL IN >=23 SDK VERSION
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                int hasCallPermission = ContextCompat.checkSelfPermission(MerchantDetailActivity.this, Manifest.permission.CALL_PHONE);
                if (currentApiVersion > 22 && hasCallPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MerchantDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE},
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
                AppInstance.merchantDetailsList = null;
                finish();
            }
        });

        title1.setText(title);
        getActionBar().setCustomView(customView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppInstance.merchantDetailsList = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //doCallMerchantDetail();
        if (merchantDetailsList != null && isUberLoaded) {
            doCallMerchantDetail();
        } else {
            Log.d("PERMISSS", "already updatng: ");

        }
    }

    private void doCallMerchantDetail() {
//        if (AppInstance.merchantDetailsList == null) {
        if (NetworkUtils.isConnected(mContext)) {
            AppInstance.merchantDetailsList = null;
            utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
            mMerchantManager.doGetMerchantDetails(mMMerchantid);
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
//        } else {
//            if (merchantDetailsList != null) {
//
//                if (merchantDetailsList.getMerchantItemDetails() != null) {
//                    ArrayList<MerchantItemDetails> merchantItemDetails = merchantDetailsList.getMerchantItemDetails();
//                    if (merchantItemDetails != null) {
//                        if (merchantItemDetails.size() > 0) {
//                            String merchantName = merchantItemDetails.get(0).getMerchantname();
//                            if (merchantName.equalsIgnoreCase(mName)) {
//                                setDataInFields();
//                            } else {
//                                utilObj.showToast(mContext, getString(R.string.invalid_message), Toast.LENGTH_LONG);
//                 /*Set Visibility*/
//                                mOfferListView.setVisibility(View.GONE);
//                                mItemDetailLayout.setVisibility(View.GONE);
//                                mImageViewMerchant.setVisibility(View.GONE);
//                                mImageViewProduct.setVisibility(View.GONE);
//                                mDescription.setVisibility(View.GONE);
//                /*End of Visibility Code*/
//                            }
//                        }
//                    }
//                } else {
//                    utilObj.showToast(mContext, mContext.getResources().getString(R.string.invalid_message), Toast.LENGTH_LONG);
//                }
//            } else {
//                utilObj.showToast(mContext, mContext.getResources().getString(R.string.invalid_message), Toast.LENGTH_LONG);
//            }
//        }
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
            case R.id.uber_estimate_rl_container:

                break;

            default:
                break;
        }
    }

    public void callMapView() {
        if (mLatitude.length() > 0 && mLongitude.length() > 0) {
            //mLatitude = Double.parseDouble(mEventDetailsModel.getmLatitude());
            try {
                String url = "http://maps.google.com/maps?f=d&daddr=" + mLatitude
                        + "," + mLongitude + "&dirflg=d";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");

                MyApplication.getInstance().printLogs("MAP", url);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?f=d&daddr=" + mLatitude
                                + "," + mLongitude));
                MyApplication.getInstance().printLogs("MAP EXCEPTION", e.getMessage());
                startActivity(intent);
            }


        }
    }

    @Override
    public void onSuccessRedirection(int taskID) {

        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.GET_MERCHANT_DETAIL_TASK_ID) {
            setDataInFields();
        }

    }


    private void setDataInFields() {

        if (AppInstance.merchantDetailsList != null) {

            merchantDetailsList = AppInstance.merchantDetailsList;
            if (merchantDetailsList.getMerchantItemDetails() != null) {
                if (merchantDetailsList.getMerchantItemDetails().size() > 0) {
                    if (AppInstance.isUberRequired) {
                        if (!utilObj.isLocationEnabled(mContext) || !utilObj.checkPermission(mContext)) {
                            Log.d("PERMISSS", "Not allowed ");
                            utilObj.showUberAlertDialog(this, contextualDialogConfirmationInterfacesLocation);
                        } else {
                            if (merchantDetailsList.getMerchantItemDetails().get(0).getLatitude().length() > 0) {
                                Log.d("PERMISSS", "Allowed:" + AppInstance.isUberRequired);
                                updateUberSDK();
                            } else {
                                Log.d("PERMISSS", "Not allowed bcoz no offers");
                            }
                        }
                    }
                /*Set Visibility*/
                    mOfferListView.setVisibility(View.VISIBLE);
                    mItemDetailLayout.setVisibility(View.VISIBLE);
                    mImageViewMerchant.setVisibility(View.VISIBLE);
                    mImageViewProduct.setVisibility(View.VISIBLE);
                    mDescription.setVisibility(View.VISIBLE);
                /*End of Visibility Code*/


                    imageLoader = ImageLoader.getInstance();
                    imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

                    options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.no_image_white)
                            .showImageForEmptyUri(R.mipmap.no_image_white)
                            .showImageOnFail(R.mipmap.no_image_white).cacheInMemory(true)
                            .cacheOnDisk(true).considerExifParams(true)
                            .bitmapConfig(Bitmap.Config.RGB_565).build();


                    if (merchantDetailsList.getMerchantItemDetails().get(0).getMerchantsbaseimage() != null) {
                        imageLoader.displayImage(merchantDetailsList.getMerchantItemDetails().get(0).getMerchantsbaseimage(), mImageViewProduct, options, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                // mItemImageProgress.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                //mItemImageProgress.setVisibility(View.GONE);
                                mImageViewProduct.setImageResource(R.mipmap.no_image_white);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                //  mItemImageProgress.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        mImageViewProduct.setImageResource(R.mipmap.no_image_white);
                    }

                    if (merchantDetailsList.getMerchantItemDetails().get(0).getMerchantsprofile_image() != null) {
                        imageLoader.displayImage(merchantDetailsList.getMerchantItemDetails().get(0).getMerchantsprofile_image(), mImageViewMerchant, options, new SimpleImageLoadingListener() {
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

                    //if (!merchantDetailsList.getMerchantItemDetails().get(0).getMerchantname().equalsIgnoreCase("null")) {
                    if (!(merchantDetailsList.getMerchantItemDetails().get(0).getMerchantname() == null)) {
                        mItemName.setText(merchantDetailsList.getMerchantItemDetails().get(0).getMerchantname());
                    } else {

                    }

                    if (!(merchantDetailsList.getMerchantItemDetails().get(0).getMerchantaddress() == null)) {
                        mItemAddress.setText(merchantDetailsList.getMerchantItemDetails().get(0).getMerchantaddress());
                    } else {

                    }

                    if (!(merchantDetailsList.getMerchantItemDetails().get(0).getNumber() == null)) {
                        mPhoneNumber = merchantDetailsList.getMerchantItemDetails().get(0).getNumber();
                        //    mPhone.setText(merchantDetailsList.getMerchantItemDetails().get(0).getNumber());
                    } else {

                    }

                    if (!(merchantDetailsList.getMerchantItemDetails().get(0).getTimings() == null)) {
                        mTimingValue.setText(merchantDetailsList.getMerchantItemDetails().get(0).getTimings());
                    } else {

                    }
                    //------THIS IS FOR CALL DATA CONFIGURATION :START
                    if (mCatID == null || Constants.BLANK.equalsIgnoreCase(mCatID) ||
                            !mCatID.equalsIgnoreCase(Constants.IntentPreference.FOOD_ID + "")) {
                        mLayoutZomatoRating.setVisibility(View.GONE);
                    } else {
                        mLayoutZomatoRating.setVisibility(View.VISIBLE);
                    }
                    String zomato_rating = merchantDetailsList.getMerchantItemDetails().get(0).getZomato_rating();
                    String zomatoMaxRatingValue = "/" + getString(R.string.zomato_max_rating_value);
                    if (zomato_rating != null && !(Constants.BLANK.equalsIgnoreCase(zomato_rating))) {
                        if (Double.parseDouble(zomato_rating) > 0.0) {
                            mZomatoRating.setText("" + zomato_rating + zomatoMaxRatingValue);
                        } else {
                            mZomatoRating.setText("-/-");
                        }
                    } else {
                        mZomatoRating.setText("-/-");
                    }
                    //------THIS IS FOR CALL DATA CONFIGURATION :END
                    if (!(merchantDetailsList.getMerchantItemDetails().get(0).getMerchantdescription() == null)) {
                        mDescription.setText(merchantDetailsList.getMerchantItemDetails().get(0).getMerchantdescription());
                    } else {

                    }
                    if (!(merchantDetailsList.getMerchantItemDetails().get(0).getLatitude() == null)) {
                        mLatitude = merchantDetailsList.getMerchantItemDetails().get(0).getLatitude();
                    } else {

                    }
                    if (!(merchantDetailsList.getMerchantItemDetails().get(0).getLongitude() == null)) {
                        mLongitude = merchantDetailsList.getMerchantItemDetails().get(0).getLongitude();
                    } else {

                    }
                    mProductListToShow = AppInstance.merchantDetailsList.getProductListToShow();
                    if (mProductListToShow != null) {
                        String lockOffersStatusFlag = AppInstance.merchantDetailsList.isOfferGetsLocked(mContext); //AppInstance.offers.getLockOffersStatusFlag();

                        MyApplication.getInstance().printLogs("productListToShow", mProductListToShow.toString());
                        MerchantOfferListAdapter merchantOfferListAdapter = new MerchantOfferListAdapter(mContext, R.layout.fragment_listview, mProductListToShow, lockOffersStatusFlag);
                        mOfferListView.setAdapter(merchantOfferListAdapter);
                    }
                }
            } else {
                utilObj.showToast(mContext, getString(R.string.invalid_message), Toast.LENGTH_LONG);
                 /*Set Visibility*/
                mOfferListView.setVisibility(View.GONE);
                mItemDetailLayout.setVisibility(View.GONE);
                mImageViewMerchant.setVisibility(View.GONE);
                mImageViewProduct.setVisibility(View.GONE);
                mDescription.setVisibility(View.GONE);
                /*End of Visibility Code*/
            }
        } else {
            utilObj.showToast(mContext, getString(R.string.invalid_message), Toast.LENGTH_LONG);
                 /*Set Visibility*/
            mOfferListView.setVisibility(View.GONE);
            mItemDetailLayout.setVisibility(View.GONE);
            mImageViewMerchant.setVisibility(View.GONE);
            mImageViewProduct.setVisibility(View.GONE);
            mDescription.setVisibility(View.GONE);
                /*End of Visibility Code*/
        }
    }

    private void updateUberSDK() {
        isUberLoaded = false;
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
        Log.d("PERMISSS", "latitude:" + merchantDetailsList.getMerchantItemDetails().get(0).getLatitude());

        double pickUp_latitude, pickUp_longitude, dropoff_lat, dropoff_lng;
        pickUp_latitude = GPSTracker.getInstance(mContext).getLatitude();
        pickUp_longitude = GPSTracker.getInstance(mContext).getLongitude();
//        pickUp_latitude = 33.651962;
//        pickUp_longitude = 73.046379;
        dropoff_lat = Double.parseDouble(merchantDetailsList.getMerchantItemDetails().get(0).getLatitude());
        dropoff_lng = Double.parseDouble(merchantDetailsList.getMerchantItemDetails().get(0).getLongitude());
//        dropoff_lat = 33.713487;
//        dropoff_lng = 73.063656;

        RideParameters rideParams = new RideParameters.Builder()
                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
                //                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
                //                .setDropoffLocation(33.647418, 73.018556, "Uber HQ", "1455 Market Street, San Francisco")
                .setDropoffLocation(dropoff_lat,
                        dropoff_lng,
                        AppInstance.merchantDetailsList.getMerchantItemDetails().get(0).getMerchantname(),
                        AppInstance.merchantDetailsList.getMerchantItemDetails().get(0).getMerchantaddress())
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

        requestUberPrice(pickUp_latitude,pickUp_longitude,dropoff_lat,dropoff_lng);
        requestUberTime(pickUp_latitude,pickUp_longitude,dropoff_lat,dropoff_lng);

//        mDeepLink.execute();
        // set parameters for the RideRequestButton instance
//        requestButton.setRideParameters(rideParams);
//
//        ServerTokenSession session = new ServerTokenSession(uberConfig);
//        requestButton.setSession(session);
//
//        callback = new RideRequestButtonCallback() {
//
//            @Override
//            public void onRideInformationLoaded() {
//                // react to the displayed estimates
//                isUberLoaded = true;
//                layout.setVisibility(View.VISIBLE);
//                llUberEstimate.setVisibility(View.GONE);
//                Log.d("PERMISSS", "onRideInformationLoaded: ");
//            }
//
//            @Override
//            public void onError(ApiError apiError) {
//                // API error details: /docs/riders/references/api#section-errors
//                isUberLoaded = true;
//                llUberEstimate.setVisibility(View.VISIBLE);
//                Log.d("PERMISSS", "onErrorAPI: " + apiError.getClientErrors().get(0).getCode());
////                layout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                // Unexpected error, very likely an IOException
//                isUberLoaded = true;
//                Log.d("PERMISSS", "onError: ");
//            }
//        };
//        requestButton.setCallback(callback);
//        requestButton.loadRideInformation();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
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

    /**
     * List header setting
     */
    private void listHeaderSetting() {
        /** List header view */
        View headerView = LayoutInflater.from(MerchantDetailActivity.this)
                .inflate(R.layout.merchant_detail_header, null);
        /*ImageView intialization*/
        mImageViewProduct = (ImageView) headerView.findViewById(R.id.imageView_product);
        mImageViewMerchant = (ImageView) headerView.findViewById(R.id.imageView_merchant);
         /*TextView intialization*/
        mItemName = (TextView) headerView.findViewById(R.id.txtView_itemName);
        mItemAddress = (TextView) headerView.findViewById(R.id.txtView_address);
        //  mPhone = (TextView) headerView.findViewById(R.id.txtView_phone);
        mTimingValue = (TextView) headerView.findViewById(R.id.txtView_timing_value);
        mDescription = (TextView) headerView.findViewById(R.id.txtView_description);
          /*Button intialization*/
        mBtnOffer = (Button) headerView.findViewById(R.id.mBtnOffer);
        mBtnCall = (Button) headerView.findViewById(R.id.btnCall);
        mBtnDirection = (Button) headerView.findViewById(R.id.btnDirection);
        mZomatoRating = (TextView) headerView.findViewById(R.id.txtView_zomato_rating);
        mZomatoRating.setVisibility(View.GONE);

        requestButton = new RideRequestButton(MerchantDetailActivity.this);
//        requestButton.setDeeplinkFallback(Deeplink.Fallback.MOBILE_WEB);
        layout = headerView.findViewById(R.id.relative_layout);
        layout.addView(requestButton);

        llUberEstimate = headerView.findViewById(R.id.uber_estimate_rl_container);
        llUberEstimationCost = headerView.findViewById(R.id.uber_estimatin_ll_cntner);
        txvDuration = headerView.findViewById(R.id.uber_duration_txv);
        txvEstimate = headerView.findViewById(R.id.uber_estimate_txv);
        llUberEstimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAppInstalled = appInstalledOrNot("com.ubercab");
                if (isAppInstalled) {
                    //This intent will help you to launch if the package is already installed
//                    Intent LaunchIntent = getPackageManager()
//                            .getLaunchIntentForPackage("com.ubercab");
//                    startActivity(LaunchIntent);
                    startActivity(new Intent(Intent.ACTION_VIEW, mDeepLink.getUri()));

                    Log.d("APPINSTALLED", "Application is already installed.");
                } else {
                    // Do whatever we want to do if application not installed
                    // Redirect to play store
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ubercab"));
                    startActivity(i);

                    Log.d("APPINSTALLED", "Application is not currently installed.");
                }
            }
        });


        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold_0.ttf");
        mZomatoRating.setTypeface(tf, Typeface.NORMAL);
        mItemDetailLayout = (LinearLayout) headerView.findViewById(R.id.lLyt_itemDetail);
        mZomatoRating = (TextView) headerView.findViewById(R.id.txtView_zomato_rating);
        mOfferListView.addHeaderView(headerView, null, false);
        mLayoutZomatoRating = (LinearLayout) headerView.findViewById(R.id.ll_zomato_rating);
        mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (catName.equals(getString(R.string.ga_food_drink_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_food_and_drink_outlet_detail_CallFromApp), getResources().getString(R.string.ga_event_action_food_and_drink_outlet_detail_CallFromApp), getResources().getString(R.string.ga_event_label_food_and_drink_outlet_detail_CallFromApp));
                } else if (catName.equals(getString(R.string.ga_beauty_spa_offer_details))) {

                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_beauty_and_spa_outlet_detail_CallFromApp), getResources().getString(R.string.ga_event_action_beauty_and_spa_outlet_detail_CallFromApp), getResources().getString(R.string.ga_event_label_beauty_and_spa_outlet_detail_CallFromApp));
                } else if (catName.equals(getString(R.string.ga_fun_activities_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_fun_activities_outlet_detail_CallFromApp), getResources().getString(R.string.ga_event_action_fun_activities_outlet_detail_CallFromApp), getResources().getString(R.string.ga_event_label_fun_activities_outlet_detail_CallFromApp));
                } else if (catName.equals(getString(R.string.ga_health_fitness_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_health_and_fitness_outlet_detail_CallFromApp), getResources().getString(R.string.ga_event_action_health_and_fitness_outlet_detail_CallFromApp), getResources().getString(R.string.ga_event_label_health_and_fitness_outlet_detail_CallFromApp));
                }

                utilObj.showCustomAlertDialog(MerchantDetailActivity.this, null, mPhoneNumber, getString(R.string.call), getString(R.string.cancel), true, customDialogConfirmationInterfacesCall);
            }
        });
        mBtnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (catName.equals(getString(R.string.ga_food_drink_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_food_and_drink_outlet_detail_NavigateToMap), getResources().getString(R.string.ga_event_action_food_and_drink_outlet_detail_NavigateToMap), getResources().getString(R.string.ga_event_label_food_and_drink_outlet_detail_NavigateToMap));
                } else if (catName.equals(getString(R.string.ga_beauty_spa_offer_details))) {

                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_beauty_and_spa_outlet_detail_NavigateToMap), getResources().getString(R.string.ga_event_action_beauty_and_spa_outlet_detail_NavigateToMap), getResources().getString(R.string.ga_event_label_beauty_and_spa_outlet_detail_NavigateToMap));
                } else if (catName.equals(getString(R.string.ga_fun_activities_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_fun_activities_outlet_detail_NavigateToMap), getResources().getString(R.string.ga_event_action_beauty_and_spa_outlet_detail_NavigateToMap), getResources().getString(R.string.ga_event_label_fun_activities_outlet_detail_NavigateToMap));
                } else if (catName.equals(getString(R.string.ga_health_fitness_offer_details))) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_health_and_fitness_outlet_detail_NavigateToMap), getResources().getString(R.string.ga_event_action_health_and_fitness_outlet_detail_NavigateToMap), getResources().getString(R.string.ga_event_label_health_and_fitness_outlet_detail_NavigateToMap));
                }

                callMapView();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //was crashing here
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Log.d("aserwqer", "onRequestPermissionsResult: " + requestCode);
                    if (GPSTracker.gpsTracker != null) {
                        GPSTracker.gpsTracker = null;
                    }
                    updateUberSDK();

                } else {
                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        Log.d("aserwqer", "onRequestPermissionsResult----: " + requestCode);
                        if (AppPreference.getSetting(mContext, "key_never_ask_again_location", "").length() > 0) {
                            startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)), Constants.IntentPreference.PACKAGE_LOCATION_INTENT_CODE);
                        } else {
                            AppPreference.setSetting(mContext, "key_never_ask_again_location", "yes");
                        }
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.IntentPreference.PACKAGE_LOCATION_INTENT_CODE == requestCode) {
            if (!utilObj.checkPermission(this)) {
                updateUberSDK();
            }
        } else if (Constants.IntentPreference.SOURCE_LOCATION_INTENT_CODE == requestCode) {

            if (!utilObj.isLocationEnabled(this)) {
                if (!utilObj.checkPermission(this)) {
                    requestPermission();
                } else {
                    updateUberSDK();
                }
            } else {
                Log.d("sadSDSAASD", "onCreateView:5520 ");
            }

        }
    }

    private void requestUberTime(double start_lat, double start_lng, double end_lat, double end_lng) {
        MerchintDetail_WebHit_Get_Uber_time merchintDetail_webHit_get_uber_time = new MerchintDetail_WebHit_Get_Uber_time();
        merchintDetail_webHit_get_uber_time.getUberTime(this, start_lat, start_lng, end_lat, end_lng);
    }

    private void requestUberPrice(double start_lat, double start_lng, double end_lat, double end_lng) {
        MerchintDetail_WebHit_Get_Uber_price merchintDetail_webHit_get_uber_price = new MerchintDetail_WebHit_Get_Uber_price();
        merchintDetail_webHit_get_uber_price.getUberPrice(this, start_lat, start_lng, end_lat, end_lng);
    }

    private void updateUberValues(boolean _isSuccess) {
        if (_isSuccess) {
            if (isPriceLoaded && isTimeLoaded) {
                llUberEstimationCost.setVisibility(View.VISIBLE);
                llUberEstimate.setVisibility(View.VISIBLE);
            }
        } else {
            llUberEstimate.setVisibility(View.VISIBLE);
        }
    }

    public void showResultTime(boolean isSuccess, String strMsg) {
        if (isSuccess) {
            if (MerchintDetail_WebHit_Get_Uber_time.responseModel != null &&
                    MerchintDetail_WebHit_Get_Uber_time.responseModel.getTimes() != null &&
                    MerchintDetail_WebHit_Get_Uber_time.responseModel.getTimes().size() > 0) {
                isTimeLoaded = true;
                Log.d("UBERSUCCESSS", "showResultTime: true ");
                int mins = (int) (MerchintDetail_WebHit_Get_Uber_time.responseModel.getTimes().get(0).getEstimate() / 60);
                if (mins > 1) {
                    txvDuration.setText(mins + " mins away");
                } else {
                    txvDuration.setText(mins + " min away");
                }
                txvDuration.setVisibility(View.VISIBLE);
            } else {
                txvDuration.setVisibility(View.GONE);
            }
            updateUberValues(true);
        } else {
            Log.d("UBERSUCCESSS", "showResultTime: false ");
            updateUberValues(false);
        }
    }

    public void showResultPrice(boolean isSuccess, String strMsg) {
        if (isSuccess) {
            if (MerchintDetail_WebHit_Get_Uber_price.responseModel != null &&
                    MerchintDetail_WebHit_Get_Uber_price.responseModel.getPrices() != null &&
                    MerchintDetail_WebHit_Get_Uber_price.responseModel.getPrices().size() > 0) {
                Log.d("UBERSUCCESSS", "showResultPrice: true ");
                 isPriceLoaded = true;
                txvEstimate.setVisibility(View.VISIBLE);
                txvEstimate.setText(MerchintDetail_WebHit_Get_Uber_price.responseModel.getPrices().get(0).getEstimate() + " on " +
                        MerchintDetail_WebHit_Get_Uber_price.responseModel.getPrices().get(0).getDisplayName());
            } else {
                txvEstimate.setVisibility(View.GONE);
            }
            updateUberValues(true);
        } else {
            Log.d("UBERSUCCESSS", "showResultPrice: false ");
            updateUberValues(false);
        }
    }
}
