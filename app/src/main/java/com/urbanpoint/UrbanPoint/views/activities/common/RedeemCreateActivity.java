package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.categoryScreens.OfferManager;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by riteshpandhurkar on 9/3/16.
 */


/**
 * 13 - JUN - 2016
 * THE LAYOUT DESIGN (activity_redeem.xml) is change with (activity_redeem_new_design.xml)
 * ALL THE UNWANTED WIGDENT ARE SET TO VISIBILITY=GONE, NO CODE CHANGES DONE
 * MERCHANT ADDRESS FIELD EXTRA ADDED IN NEW DESIGN
 */

public class RedeemCreateActivity extends Activity implements ServiceRedirection, CustomDialogConfirmationInterfaces, View.OnClickListener {
    private Context mContext;
    private Utility utilObj;
    String mCustomerid, mProductid, mName, mProductImage, mMerchantImage, mProductDetail, mProductPrice, mMerchantID, mImageUrl;
    private ImageView mBackButton;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ImageView mImageViewProduct, mImageViewMerchant;
    TextView mDetailsHeader;

    TextView mDetailsPrice;// NOT CURRENTLY USED.

    private PinEntryView mRedeemPinEntry;
    Button mReedemButton;
    private String enteredPurchasePin = "";
    private OfferManager mOfferManager;
    CustomDialogConfirmationInterfaces customDialogConfirmationInterfacesCustomerPIN, customDialogConfirmationInterfacesDirection;
    private RelativeLayout mMainParentLayout;
    private String mMerchantName;
    private TextView mDetailsMerchantName;
    private TextView mDetailsMerchantAddress;
    private String mMerchantAddress;

    private LinearLayout mOldLayoutObjects;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///-------- SEE COMMENT AT TOP OF FILE
        //setContentView(R.layout.activity_redeem);
        setContentView(R.layout.activity_redeem_new_design);
        ///-------- SEE COMMENT AT TOP OF FILE

        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        mContext = getApplicationContext();
        initViews();
        MyApplication.getInstance().trackScreenView(getString(R.string.redemption_user_pin));

    }

    private void initViews() {
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
            mMerchantAddress = m_buBundle.getString(Constants.OfferDetails.MERCHANT_ADDRESS);


            mMerchantID = m_buBundle.getString(Constants.Request.MERCHANT_ID);
            setActionBar(mName, true);

        }
        bindViews();
    }

    private void bindViews() {

        //------- THIS IS DONE TO HIDE OLD LAYOUT OBJECT, SEE COMMENT ON TOP
        mOldLayoutObjects = (LinearLayout) findViewById(R.id.oldLayoutObjects);
        mOldLayoutObjects.setVisibility(View.GONE);
        //------- THIS IS DONE TO HIDE OLD LAYOUT OBJECT, SEE COMMENT ON TOP

        mMainParentLayout = (RelativeLayout) findViewById(R.id.mainParentLayout);
        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                return false;
            }
        });


        mImageViewProduct = (ImageView) findViewById(R.id.imageView_product);
        mImageViewMerchant = (ImageView) findViewById(R.id.imageView_merchant);
        mRedeemPinEntry = (PinEntryView) findViewById(R.id.redeemUserPinEntry);
        mDetailsHeader = (TextView) findViewById(R.id.detailsHeader);


        //--------- NOT CURRENTLY USED -- START
        mDetailsPrice = (TextView) findViewById(R.id.detailsPrice);
        //--------- NOT CURRENTLY USED -- END

        mDetailsMerchantName = (TextView) findViewById(R.id.detailsMerchantName);
        mDetailsMerchantAddress = (TextView) findViewById(R.id.detailsMerchantAddress);

        mReedemButton = (Button) findViewById(R.id.redeemConfirmPurchaseButton);
        mReedemButton.setOnClickListener(this);

        mRedeemPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredPurchasePin = pin;
                utilObj.keyboardClose(mContext, mRedeemPinEntry);
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
                        utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                        String pin = "";
                        String productID = "";
                        String merchantid = "";
                        pin = enteredPurchasePin;
                        productID = mProductid;
                        mOfferManager.doCheckCustomerPin(pin, productID);
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
            message = getResources().getString(R.string.enter_four_digit_pin);
            utilObj.showCustomAlertDialog(RedeemCreateActivity.this, getString(R.string.redeem), message, null, null, false, this);
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

        if (taskID == Constants.TaskID.GET_CHECK_CUSTOMER_PIN_TASK_ID) {


            Bundle m_bundle = new Bundle();
            m_bundle.putString(Constants.Request.CUSTOMER_ID, mCustomerid);
            m_bundle.putString(Constants.Request.OFFER_ID, mProductid);
            m_bundle.putString(Constants.Request.IMAGE_URL, mImageUrl);
            m_bundle.putString(Constants.Request.MERCHANT_IMAGE_URL, mMerchantImage);
            m_bundle.putString(Constants.Request.SPECIAL_PRICE, mProductPrice);
            m_bundle.putString(Constants.Request.NAME, mName);
            m_bundle.putString(Constants.OfferDetails.MERCHANT_NAME, mMerchantName);
            m_bundle.putString(Constants.OfferDetails.MERCHANT_ADDRESS, mMerchantAddress);
            m_bundle.putString(Constants.Request.MERCHANT_ID, mMerchantID);
            MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_redemption_correct_user_pin),getResources().getString(R.string.ga_event_action_redemption_correct_user_pin),getResources().getString(R.string.ga_event_label_redemption_correct_user_pin));
            Intent intent = new Intent(RedeemCreateActivity.this, RedeemConfirmPurchaseActivity.class);
            intent.putExtras(m_bundle);
            startActivity(intent);

        }

    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        mRedeemPinEntry.clearText();
        if(errorMessage.equalsIgnoreCase(getResources().getString(R.string.incorrect_pin_msg))){
            MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_redemption_incorrect_user_pin),getResources().getString(R.string.ga_event_action_redemption_incorrect_user_pin),getResources().getString(R.string.ga_event_label_redemption_incorrect_user_pin));
        }
        utilObj.showCustomAlertDialog(RedeemCreateActivity.this, getString(R.string.redeem), getString(R.string.error_customer_pin), getString(R.string.Ok), null, false, customDialogConfirmationInterfacesCustomerPIN);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }
}
