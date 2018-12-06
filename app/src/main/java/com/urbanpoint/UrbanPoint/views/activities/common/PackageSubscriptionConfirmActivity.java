package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.managers.categoryScreens.OfferManager;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryFilled;

import java.util.logging.Handler;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aparnasalve on 10/3/16.
 */

/**
 * 13 - JUN - 2016
 * THE LAYOUT DESIGN (activity_redeem_purchase_confirm.xml) is change with (activity_redeem_purchase_confirm_new_design.xml)
 * ALL THE UNWANTED WIGDENT ARE SET TO VISIBILITY=GONE, NO CODE CHANGES DONE
 * MERCHANT ADDRESS FIELD EXTRA ADDED IN NEW DESIGN
 */

public class PackageSubscriptionConfirmActivity extends FragmentActivity implements View.OnClickListener {
    private Context mContext;
    private Utility utilObj;
    private ImageView mBackButton;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ImageView mImageViewProduct, mImageViewMerchant;
    TextView mDetailsHeader, mDetailsPrice, mOffrName, mMerchant, mOutlet;
    private PinEntryFilled mRedeemPinEntry;
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
    private TextView txvTryAgain;
    private Button btnSubscribe;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///-------- SEE COMMENT AT TOP OF FILE
        setContentView(R.layout.activity_package_subscription_confirm);

        ///-------- SEE COMMENT AT TOP OF FILE
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        mContext = getApplicationContext();
        initViews();
//        MyApplication.getInstance().trackScreenView(getString(R.string.redemption_merchant_pin));

    }

    private void initViews() {
        utilObj = new Utility(mContext);
        this.mContext = this.getApplicationContext();
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        setActionBar(getString(R.string.subscribe), false);
        bindViews();
    }

    private void bindViews() {
        txvTryAgain = (TextView) findViewById(R.id.frg_package_subscription_cnfrm_txv_try_again);
        txvTryAgain.setOnClickListener(this);
        btnSubscribe = (Button) findViewById(R.id.act_pakage_subscribe_cnfrm_btn_subscribe);
        btnSubscribe.setOnClickListener(this);
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
                finish();
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
            case R.id.act_pakage_subscribe_cnfrm_btn_subscribe:
                navToPackageSubscriptionSucessActivity();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }
    private void navToPackageSubscriptionSucessActivity(){
        Intent intent = new Intent(this,PackageSubscriptionSucessActivity.class);
        startActivity(intent);
        finish();
    }
}

