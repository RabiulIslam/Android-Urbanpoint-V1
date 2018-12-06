package com.urbanpoint.UrbanPoint.views.activities.categoryActivities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.views.fragments.categoryFragments.FoodAndDrinks_MerchantsListTabFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class FoodAndDrinkMainActivity extends BaseActivity {

    private Context mContext;

    public FoodAndDrinkMainActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_frame);
        initData();
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

        FoodAndDrinks_MerchantsListTabFragment food_AndDrinks_merchantsListTabFragment = new FoodAndDrinks_MerchantsListTabFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constants.Request.CATEGORYID,""+ Constants.IntentPreference.FOOD_ID);
        food_AndDrinks_merchantsListTabFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        fragmentTransaction.replace(R.id.content_frame, food_AndDrinks_merchantsListTabFragment).commit();

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
