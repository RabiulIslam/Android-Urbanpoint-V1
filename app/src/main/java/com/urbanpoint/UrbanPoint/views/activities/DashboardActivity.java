package com.urbanpoint.UrbanPoint.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.categoryActivities.BeautyAndSpaMainActivity;
import com.urbanpoint.UrbanPoint.views.activities.categoryActivities.FoodAndDrinkMainActivity;
import com.urbanpoint.UrbanPoint.views.activities.categoryActivities.FunActivitiesMainActivity;
import com.urbanpoint.UrbanPoint.views.activities.categoryActivities.RetailAndServicesMainActivity;
import com.urbanpoint.UrbanPoint.views.activities.common.OfferDetailActivity;
import com.urbanpoint.UrbanPoint.views.activities.common.OfferPackagesActivity;
import com.urbanpoint.UrbanPoint.views.activities.common.SearchFilterActivity;
import com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType.PaymentProcessVodafoneSelectionActivity;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.MyReviewsFragment;
import com.urbanpoint.UrbanPoint.views.fragments.main.HomeFragment;
import com.urbanpoint.UrbanPoint.views.fragments.main.MenuDrawerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.urbanpoint.UrbanPoint.utils.Constants.Request.MIXPANEL_TOKEN;


public class DashboardActivity extends BaseActivity {

    private Fragment mContent;
    private Utility utilObj;
    private String isShowReview;
    public SlidingMenu mSlidingMenu;
    private MenuDrawerFragment menuDrawerFragment;
    private int completedFields = 0;
    private int completedPercentage = 0;

    public DashboardActivity() {
        super(R.string.app_name_with_space);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dashboard);
        setContentView(R.layout.content_frame);
        mSlidingMenu = new SlidingMenu(this);
        initData();
        MyApplication.getInstance().trackScreenView(getString(R.string.home_screen));
        /*MixpanelAPI mixpanel = MixpanelAPI.getInstance(getApplicationContext(), MIXPANEL_TOKEN);
        JSONObject props = null;
        try {
            props = new JSONObject();
            props.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("Subscribe Confirmation screen", props);*/
        Bundle m_buBundle = getIntent().getExtras();
        if (m_buBundle != null) {
            isShowReview = m_buBundle.getString(Constants.Request.SHOW_REVIEW);
//            Log.d("REEE", "onCreate: "+isShowReview);
            if (isShowReview != null
                    && isShowReview.equalsIgnoreCase("True")) {
                Fragment newContent = new MyReviewsFragment();
                switchContent(newContent, null, true, "");
            }
        }
//        AppPreference.setSetting(getApplicationContext(), "key_home_offers", "");

        getSlidingMenu().setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                Log.d("MY_MENU", "setOnOpenedListener a");
//                menuDrawerFragment.checkProfileCompletion();
                Fragment frg = getSupportFragmentManager().findFragmentByTag("MenuFRG");
                if (frg instanceof MenuDrawerFragment) {
                    ((MenuDrawerFragment) frg).checkProfileCompletion();
                    Log.d("MY_MENU", "setOnOpenedListener a");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void navToSubscribeActivity() {
        Intent intent = new Intent(this, OfferPackagesActivity.class);
        startActivityForResult(intent, Constants.IntentPreference.HOME_SCREEN_INTENT_CODE);
    }

    public void navToPaymentProcessVodafoneSelectionActivity() {
        Intent intent = new Intent(this, PaymentProcessVodafoneSelectionActivity.class);
        startActivityForResult(intent, Constants.IntentPreference.HOME_SCREEN_INTENT_CODE);
    }

    public void navToDetail(String custID, String productid, String itemName) {
        Bundle m_bundle = new Bundle();
        m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
        m_bundle.putString(Constants.Request.OFFER_ID, productid);
        m_bundle.putString(Constants.Request.NAME, itemName);

        Intent intent = new Intent(this, OfferDetailActivity.class);
        intent.putExtras(m_bundle);
        startActivityForResult(intent, Constants.IntentPreference.HOME_SCREEN_INTENT_CODE);
    }

    public void navToSearch() {
        Intent intent = new Intent(this, SearchFilterActivity.class);
        startActivityForResult(intent, Constants.IntentPreference.HOME_SCREEN_INTENT_CODE);
    }

    public void navToFun() {
        Intent intent = new Intent(this, FunActivitiesMainActivity.class);
        startActivityForResult(intent, Constants.IntentPreference.HOME_SCREEN_INTENT_CODE);
    }

    public void navToRetail() {
        Intent intent = new Intent(this, RetailAndServicesMainActivity.class);
        startActivityForResult(intent, Constants.IntentPreference.HOME_SCREEN_INTENT_CODE);
    }

    public void navToFood() {
        Intent intent = new Intent(this, FoodAndDrinkMainActivity.class);
        startActivityForResult(intent, Constants.IntentPreference.HOME_SCREEN_INTENT_CODE);
    }

    public void navToBeauty() {
        Intent intent = new Intent(this, BeautyAndSpaMainActivity.class);
        startActivityForResult(intent, Constants.IntentPreference.HOME_SCREEN_INTENT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.IntentPreference.HOME_SCREEN_INTENT_CODE) {
            mContent = null;
            this.initData();
            Log.d("aaaaqw", "onActivityResult: ");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
     * Initializes the objects
     *
     * @return none
     */
    private void initData() {
        utilObj = new Utility(this);

        showMessage();

        FragmentManager supportFragmentManager = getSupportFragmentManager();

        FragmentTransaction homeFragmentTransaction = supportFragmentManager.beginTransaction();
        FragmentTransaction menuFragmentTransaction = supportFragmentManager.beginTransaction();

        if (mContent == null) {
            mContent = new HomeFragment();
        }

        //  homeFragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        homeFragmentTransaction.replace(R.id.content_frame, mContent, "HOME_FRAGMENT").commit();

        //this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

        setBehindContentView(R.layout.menu_drawer_frame);
        setActionBar(getString(R.string.app_name_with_space), true);

        // menuFragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        menuFragmentTransaction.replace(R.id.menu_frame, new MenuDrawerFragment(), "MenuFRG").commit();

    }

    // Show purchase success alert when payment done successfully
    private void showMessage() {
        String stringExtra = getIntent().getStringExtra(Constants.DEFAULT_VALUES.PURCHASE_DONE);

        if (stringExtra != null && Constants.DEFAULT_VALUES.SUCCESS.equalsIgnoreCase(stringExtra)) {
            String offerID = getIntent().getStringExtra(Constants.Request.OFFER);
            if (offerID != null) {
                String format = String.format(getString(R.string.purchase_success_message), offerID);
                utilObj.showCustomAlertDialog(this, null, format, null, null, false, null);
            }
        }
    }


    public void switchContent(Fragment fragment, String tag,
                              boolean addToBackStack, String title) {

        if (fragment == null) {
            return;
        }

        mContent = fragment;
        setActionBar(title, true);
        if (!fragment.isAdded() && title != null && !title.equals("homeFragments")) {
            if (addToBackStack) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag)
                        .addToBackStack("").commit();
                Log.d("MYHOMEA1", "Tag is :" + tag + ".");

            } else {
                //-------
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                if (supportFragmentManager.getBackStackEntryCount() > 0) {
//                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,
//                            android.R.anim.slide_out_right);

                    for (int i = 0; i < supportFragmentManager.getBackStackEntryCount(); ++i) {
                        supportFragmentManager.popBackStack();
                        Log.d("MYHOMEA2", "BTag is :" + supportFragmentManager.getBackStackEntryCount() + ".");
                    }

                }
                AppInstance.isNetworkMsgShowd = false;

                //-------
                Log.d("MYHOMEA2", "BTag is :" + supportFragmentManager.getBackStackEntryCount() + ".");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment, tag).commit();
            }
        } else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.right_in, R.anim.left_out).
                    replace(R.id.content_frame, fragment, tag)
                    .addToBackStack("").commit();
            Log.d("MYHOMEA3", "Tag is :" + tag + ".");
        }


        getSlidingMenu().showContent();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
