package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.urbanpoint.UrbanPoint.utils.Constants.Request.MIXPANEL_TOKEN;

/**
 * Created by aparnasalve on 10/3/16.
 */
public class PurchaseSuccessActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private Utility utilObj;
    private ImageView mBackButton;
    String mConfirmationCode, mName, mMerchantAddress;
    TextView mConfirmationCodeTextView;
    private String mMerchantName;
    TextView mRedeemSuccessMessage, txvOffer, txvMerchnt, txvAddress, txvHome;
    Button btnRateReview;

    private final String OFFER_COMPLETE_MESSAGE = "Your offer of <font color='black'> <b>%1$s</b></font> at <font color='black'> <b>%2$s</b> </font> has been unlocked!";
    private TextView mCurrentDateOfRedeemed;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_success);
        mContext = getApplicationContext();
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        MyApplication.getInstance().trackScreenView(getString(R.string.ga_redemption_unique_code_screen));
        initViews();
        AppPreference.setSetting(mContext, "key_home_offers", "");

        MixpanelAPI mixpanel = MixpanelAPI.getInstance(mContext, MIXPANEL_TOKEN);

        JSONObject props = null;
        try {
            props = new JSONObject();
            props.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("Offer Used Confirmation screen", props);

    }

    private void initViews() {
        AppPreference.setSetting(getApplicationContext(), "key_Promotion_acessed", "Availled");
        utilObj = new Utility(mContext);
        Bundle m_buBundle = getIntent().getExtras();
        if (m_buBundle != null) {

            mConfirmationCode = m_buBundle.getString(Constants.Request.CONFIRMATION_CODE);
            mName = m_buBundle.getString(Constants.Request.NAME);
            mMerchantName = m_buBundle.getString(Constants.OfferDetails.MERCHANT_NAME);
            mMerchantAddress = m_buBundle.getString(Constants.OfferDetails.MERCHANT_ADDRESS);

            setActionBar(mName, true);

        }

        MyApplication.getInstance().trackScreenView(getString(R.string.ga_offer_redeem_success) + ":" + mConfirmationCode);

        bindViews();
    }

    private void bindViews() {

        mCurrentDateOfRedeemed = (TextView) findViewById(R.id.currentDateOfRedeemed);
        mCurrentDateOfRedeemed.setText("" + getCurrentDateToDisplay());

        mConfirmationCodeTextView = (TextView) findViewById(R.id.txtView_confirmation_code);
        txvOffer = (TextView) findViewById(R.id.act_purchase_sucess_txv_offr);
        txvMerchnt = (TextView) findViewById(R.id.act_purchase_sucess_txv_merchnt);
        txvAddress = (TextView) findViewById(R.id.act_purchase_sucess_txv_outlet);
        txvHome = (TextView) findViewById(R.id.act_purchase_sucess_txv_goto_home);
        btnRateReview = (Button) findViewById(R.id.act_purchase_sucess_btn_rate);

        txvOffer.setText(mName);
        txvMerchnt.setText(mMerchantName);
        txvMerchnt.setTypeface(Typeface.DEFAULT_BOLD);
        txvAddress.setText(mMerchantAddress);

        txvHome.setOnClickListener(this);
        btnRateReview.setOnClickListener(this);


        String formattedString = String.format(OFFER_COMPLETE_MESSAGE, mName, mMerchantName);


        mConfirmationCodeTextView.setText(mConfirmationCode);
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_color)));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_success_purchase, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setVisibility(View.GONE);
//  mBackButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToHome();
//            }
//        });

        title1.setText(title);
        getActionBar().setCustomView(customView);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private String getCurrentDateToDisplay() {
        String data = getString(R.string.offer_redeem_success_time);
        DateFormat df = new SimpleDateFormat("MMM yyyy");
        DateFormat df2 = new SimpleDateFormat("hh:mm aa");
        Date dateobj = new Date();
        Calendar instance = Calendar.getInstance();
        int day = instance.get(Calendar.DATE);
        String ordinalSuffix = Utility.getOrdinalSuffix(day);
        String resposne = String.format(day + ordinalSuffix + " " + df.format(dateobj) + " at " + df2.format(dateobj));

        return resposne;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToHome();

    }

    private void goToReview() {
        Bundle m_bundle = new Bundle();
        AppInstance.setIsSubscriptionCheckCalled(false);
        Intent intentObj = new Intent(PurchaseSuccessActivity.this, DashboardActivity.class);
        m_bundle.putString(Constants.Request.SHOW_REVIEW, "True");
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentObj.putExtras(m_bundle);
        startActivity(intentObj);
        finish();

    }


    private void goToHome() {
        //mSlideButton.toggle(true);
        finish();
        AppInstance.setIsSubscriptionCheckCalled(false);
        Intent intentObj = new Intent(PurchaseSuccessActivity.this, DashboardActivity.class);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentObj);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_purchase_sucess_txv_goto_home:
                goToHome();
                break;
            case R.id.act_purchase_sucess_btn_rate:
                goToReview();
                break;
        }
    }
}
