package com.urbanpoint.UrbanPoint.views.activities.categoryActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.fragments.categoryFragments.BeautyAndHealth_MerchantsListTabFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BeautyAndSpaMainActivity extends BaseActivity {

    private Context mContext;

    public BeautyAndSpaMainActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_frame);

        initData();
        bindControls();

    }

    /**
     * Default method of activity life cycle to handle the actions required once the activity starts
     * checks if the network is available or not
     *
     * @return none
     */


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    /**
     * Default activity life cycle method
     */
    @Override
    public void onStop() {
        super.onStop();

    }

    /**
     * The method to handle the data when the orientation is changed
     *
     * @param outState contains Bundle data
     */


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    private void initData() {


        this.mContext = this.getApplicationContext();

        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        BeautyAndHealth_MerchantsListTabFragment beauty_NHealth_merchantsListTabFragment = new BeautyAndHealth_MerchantsListTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Request.CATEGORYID, "" + Constants.IntentPreference.BEAUTY_ID);
        beauty_NHealth_merchantsListTabFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, beauty_NHealth_merchantsListTabFragment).commit();

    }


    public void bindControls() {

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
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
