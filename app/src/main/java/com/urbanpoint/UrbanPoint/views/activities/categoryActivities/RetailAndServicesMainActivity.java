package com.urbanpoint.UrbanPoint.views.activities.categoryActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.fragments.categoryFragments.RetailAndServices_MerchantsListTabFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aparnasalve on 7/3/16.
 */
public class RetailAndServicesMainActivity extends BaseActivity {
    private Context mContext;

    public RetailAndServicesMainActivity() {
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

        RetailAndServices_MerchantsListTabFragment retailAndServices_merchantsListTabFragment = new RetailAndServices_MerchantsListTabFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constants.Request.CATEGORYID,""+ Constants.IntentPreference.RetailnServices_ID);
        retailAndServices_merchantsListTabFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, retailAndServices_merchantsListTabFragment).commit();

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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}


