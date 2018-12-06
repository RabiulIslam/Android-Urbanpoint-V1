package com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class PaymentProcessVodafoneSuccessActivity extends Activity implements View.OnClickListener {
    private Context mContext;

    private ImageView mBackButton;
    private Button mDoCLose;
    private String mCalledFromOfferDetailActivity;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_voda_subscription_success);
        mContext = getApplicationContext();
        this.overridePendingTransition(R.anim.slide_in_top, 0);

        initViews();
    }

    private void initViews() {

        mCalledFromOfferDetailActivity = getIntent().getStringExtra(Constants.Request.VODACUSTOMER);
        MyApplication.getInstance().trackScreenView(getString(R.string.ga_susbsciption_screen_three));
        bindViews();
    }

    private void bindViews() {
        setActionBar(getString(R.string.sub_complete), false);
        mDoCLose = (Button) findViewById(R.id.doClose);
        mDoCLose.setOnClickListener(this);

    }

    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_change_pin, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
        title1.setText(title);

        TextView textViewCancel = (TextView) customView.findViewById(R.id.textViewCancel);
        textViewCancel.setVisibility(View.INVISIBLE);

        ImageView slidingMenuChangePinButton = (ImageView) customView.findViewById(R.id.slidingMenuChangePinButton);
        slidingMenuChangePinButton.setVisibility(View.INVISIBLE);

        getActionBar().setCustomView(customView);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.doClose:
                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_subscription_screen_for_voda_users_confirmation_screen),getResources().getString(R.string.ga_event_action_subscription_screen_for_voda_users_confirmation_screen),getResources().getString(R.string.ga_event_label_subscription_screen_for_voda_users_confirmation_screen));
                if (mCalledFromOfferDetailActivity != null || Constants.DEFAULT_VALUES.SUCCESS.equalsIgnoreCase(mCalledFromOfferDetailActivity)) {
                    finish();
                } else {
                    Intent intentObj = new Intent(PaymentProcessVodafoneSuccessActivity.this, DashboardActivity.class);
                    intentObj.putExtra(Constants.DEFAULT_VALUES.PURCHASE_DONE, Constants.DEFAULT_VALUES.SUCCESS);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentObj);
                }
        }
    }
}
