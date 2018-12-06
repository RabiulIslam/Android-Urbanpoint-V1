package com.urbanpoint.UrbanPoint.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.urbanpoint.UrbanPoint.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HowItWorksActivity extends AppActivity implements View.OnClickListener {

    private Button mLetsGoToApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_work);

        //   this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        bindView();
        MyApplication.getInstance().trackScreenView(getString(R.string.here_how_it_works_header));
    }

    private void bindView() {
        mLetsGoToApp = (Button) findViewById(R.id.letsGoToApp);
        mLetsGoToApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.letsGoToApp:
                Intent intentObj = new Intent(HowItWorksActivity.this, DashboardActivity.class);
                intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentObj);
                HowItWorksActivity.this.finish();
                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_tips_screen_lets_go), getResources().getString(R.string.ga_event_action_get_started_tips_screen_lets_go), getResources().getString(R.string.ga_event_label_get_started_tips_screen_lets_go));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
