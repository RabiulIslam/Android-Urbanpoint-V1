package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.urbanpoint.UrbanPoint.utils.Constants.Request.MIXPANEL_TOKEN;

/**
 * Created by aparnasalve on 10/3/16.
 */

/**
 * 13 - JUN - 2016
 * THE LAYOUT DESIGN (activity_redeem_purchase_confirm.xml) is change with (activity_redeem_purchase_confirm_new_design.xml)
 * ALL THE UNWANTED WIGDENT ARE SET TO VISIBILITY=GONE, NO CODE CHANGES DONE
 * MERCHANT ADDRESS FIELD EXTRA ADDED IN NEW DESIGN
 */

public class PackageSubscriptionSucessActivity extends FragmentActivity implements View.OnClickListener {
    private Context mContext;
    private Utility utilObj;
    private ImageView mBackButton;
    private TextView txv1, txv2, txv3;
    private Typeface novaThin, novaRegular;
    private ImageView imvCross;
    private Button btnLetsGo;
    private String mCustomerid, mCatId, mMerchantID, mProductid, mName, mPhoneNumber, mCreditCard,mPaymentType;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///-------- SEE COMMENT AT TOP OF FILE
        setContentView(R.layout.activity_package_subscription_sucess);

        ///-------- SEE COMMENT AT TOP OF FILE
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        mContext = getApplicationContext();
        initViews();
//        MyApplication.getInstance().trackScreenView(getString(R.string.redemption_merchant_pin));

        if (mPaymentType.equalsIgnoreCase("MobileServices")){
            txv2.setText(getResources().getString(R.string.offer_subscribe_sucess_credit_card));
            txv3.setVisibility(View.GONE);

            MixpanelAPI mixpanel = MixpanelAPI.getInstance(mContext, MIXPANEL_TOKEN);
            JSONObject props = null;
            try {
                props = new JSONObject();
                props.put("", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mixpanel.track("Subscribe Confirmation screen", props);

        }else {
            MixpanelAPI mixpanel = MixpanelAPI.getInstance(mContext, MIXPANEL_TOKEN);
            JSONObject props = null;
            try {
                props = new JSONObject();
                props.put("", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mixpanel.track("Payment Confirmation screen", props);

            }
    }

    private void initViews() {
        utilObj = new Utility(mContext);
        this.mContext = this.getApplicationContext();
        this.novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        this.novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        mCreditCard="";
        mPaymentType="";
        Bundle m_buBundle = getIntent().getExtras();
        if (m_buBundle != null) {
            mCustomerid = m_buBundle.getString(Constants.Request.CUSTOMER_ID);
            mProductid = m_buBundle.getString(Constants.Request.OFFER_ID);
            mName = m_buBundle.getString(Constants.Request.NAME);
            mCatId = m_buBundle.getString(Constants.Request.CATEGORYID);
            mCreditCard = m_buBundle.getString(Constants.DEFAULT_VALUES.PURCHASE_DONE);
            mPaymentType = m_buBundle.getString("PaymentMethod","");
            mMerchantID = "";
            setActionBar(mName, true);
            Log.d("LOGOO", "initViews: mID " + mMerchantID + " and CID" + mCustomerid + " And name: " + mName);
        }

        setActionBar(getString(R.string.subscribe), false);
        bindViews();
    }

    private void bindViews() {
        txv1 = (TextView) findViewById(R.id.subscription_sucess_txv_1);
        txv2 = (TextView) findViewById(R.id.subscription_sucess_txv_2);
        txv3 = (TextView) findViewById(R.id.subscription_sucess_txv_3);
        imvCross = (ImageView) findViewById(R.id.subscribeSuccessCross);
        btnLetsGo = (Button) findViewById(R.id.subscribeSuccessHome);

        imvCross.setOnClickListener(this);
        btnLetsGo.setOnClickListener(this);

        txv1.setTypeface(novaRegular);
        txv2.setTypeface(novaRegular);
        txv3.setTypeface(novaRegular);
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color)));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSlideButton.toggle(true);
                if (mProductid != null &&
                        mProductid.length() > 0) {
                    Log.d("sadqw", "From Offer Detail: ");
                    finish();
                } else {
                    goToHome();
                }
            }
        });

        title1.setText(title);
        getActionBar().setCustomView(customView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_package_subscription_cnfrm_txv_try_again:
                finish();
                break;
            case R.id.subscribeSuccessHome:
                if (mProductid != null &&
                        mProductid.length() > 0) {
                    Log.d("sadqw", "From Offer Detail: ");
//                    Bundle m_bundle = new Bundle();
//                    m_bundle.putString(Constants.Request.CUSTOMER_ID, mCustomerid);
//                    m_bundle.putString(Constants.Request.OFFER_ID, mProductid);
//                    m_bundle.putString(Constants.Request.NAME, mName);
//                    m_bundle.putString(Constants.Request.CATEGORYID, mCatId);
//
//                    Intent intent = new Intent(this, OfferDetailActivity.class);
//                    intent.putExtras(m_bundle);
//                    startActivity(intent);

                    finish();
                } else {
                    goToHome();
                }
                break;

            case R.id.subscribeSuccessCross:
                if (mProductid != null &&
                        mProductid.length() > 0) {
                    Log.d("sadqw", "From Offer Detail: ");
//                    Bundle m_bundle = new Bundle();
//                    m_bundle.putString(Constants.Request.CUSTOMER_ID, mCustomerid);
//                    m_bundle.putString(Constants.Request.OFFER_ID, mProductid);
//                    m_bundle.putString(Constants.Request.NAME, mName);
//                    m_bundle.putString(Constants.Request.CATEGORYID, mCatId);
//
//                    Intent intent = new Intent(this, OfferDetailActivity.class);
//                    intent.putExtras(m_bundle);
//                    startActivity(intent);
                    finish();
                } else {
                    goToHome();
                }
                break;
        }
    }

    private void goToHome() {
        //mSlideButton.toggle(true);
        finish();
        AppInstance.setIsSubscriptionCheckCalled(false);
        Intent intentObj = new Intent(this, DashboardActivity.class);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentObj);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }
}

