package com.urbanpoint.UrbanPoint.views.fragments.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.profile.ProfileInfo;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.appLogin.LoginManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MainActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.fragments.appLogin.SignUpFragmentStepSix;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.ChangeCellularOperatorFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.ChangePinFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.ContactUsFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.FreeAccessFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.HowToUseFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.MyEarningsFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.MyReviewsFragment;
import com.urbanpoint.UrbanPoint.views.fragments.HomeFragments.NotificationFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.ProfileFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.PurchaseHistoryFragment;
import com.urbanpoint.UrbanPoint.views.fragments.drawerItems.UnSubscribeAppConfirmationFragment;


public class MenuDrawerFragment extends Fragment implements OnClickListener, CustomDialogConfirmationInterfaces, ServiceRedirection {

    private Utility utilObj;
    private View mRootView;
    private FragmentActivity mActivity;
    private Context mContext;
    private Button mDrawerHome;
    private Button mDrawerProfile;
    private Button mDrawerNotifications;
    private Button mDrawerChangePin;
    private Button mDrawerPurchasedHistory;
    private Button mDrawerMyEarnings;
    private Button mDrawerHowToUse;
    private Button mDrawerUnSubscribeNew;
    private Button mDrawerContactUs;
    private Button mDrawerLogout;
    private Button mDrawerMyReviews;
    private UpdateMyReviewCount mUpdateMyReviewCount;
    private TextView mDrawerChangeOperator;
    private Button mDrawerTest;
    private Button mDrawerShareApp;
    public TextView mDrawerUnSubscribeSeperaterView, txvUserName, txvProfile, txvReview, txvNotification;
    private Button mDrawerUnSubscribe;
    private LoginManager loginManagerObj;
    private ShowUnSubscribeOption mShowUnSubscribeOption;
    private HideUnSubscribeOption mHideUnSubscribeOption;
    private Button mDrawerFreeAccessCode;
    public int completedFields, completedPercentage;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        utilObj = new Utility(getActivity());
        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        mRootView = inflater.inflate(R.layout.menu_list, null);

        mUpdateMyReviewCount = new UpdateMyReviewCount();
        mActivity.registerReceiver(mUpdateMyReviewCount, new IntentFilter(
                Constants.DEFAULT_VALUES.MY_REVIEW_UPDATED));

        //------------

        mShowUnSubscribeOption = new ShowUnSubscribeOption();
        mActivity.registerReceiver(mShowUnSubscribeOption, new IntentFilter(
                Constants.DEFAULT_VALUES.SHOW_VODA_CUSTOMER_UNSUBSCRIBE_OPTION));

        //---------
        mHideUnSubscribeOption = new HideUnSubscribeOption();
        mActivity.registerReceiver(mHideUnSubscribeOption, new IntentFilter(
                Constants.DEFAULT_VALUES.HIDE_VODA_CUSTOMER_UNSUBSCRIBE_OPTION));

        initViews();


        MyApplication.getInstance().trackScreenView(getResources().getString(R.string.ga_menu_drawer_open));


        return mRootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews() {

        utilObj = new Utility(mActivity);
        loginManagerObj = new LoginManager(mActivity, this);

        mDrawerUnSubscribe = (Button) mRootView.findViewById(R.id.drawerUnSubscribe);
        mDrawerUnSubscribeSeperaterView = (TextView) mRootView.findViewById(R.id.drawerUnSubscribeSeperaterView);

        mDrawerHome = (Button) mRootView.findViewById(R.id.drawerHome);
        mDrawerProfile = (Button) mRootView.findViewById(R.id.drawerMyProfile);
        mDrawerUnSubscribeNew = (Button) mRootView.findViewById(R.id.drawerUnSubscribeNew);
        mDrawerNotifications = (Button) mRootView.findViewById(R.id.drawerMyNotification);
        mDrawerChangePin = (Button) mRootView.findViewById(R.id.drawerChangePin);
        mDrawerPurchasedHistory = (Button) mRootView.findViewById(R.id.drawerPurchasedHistory);
        mDrawerMyReviews = (Button) mRootView.findViewById(R.id.drawerMyReviews);
        txvProfile = (TextView) mRootView.findViewById(R.id.menu_drawer_txv_profile);
        txvNotification = (TextView) mRootView.findViewById(R.id.menu_drawer_txv_notification);
        txvReview = (TextView) mRootView.findViewById(R.id.menu_drawer_txv_review);
        txvUserName = (TextView) mRootView.findViewById(R.id.menuDrawerTxvName);


        /**
         //-- CURRENTLY NOT USING VISIBILITY = GONE
         */
        mDrawerChangeOperator = (Button) mRootView.findViewById(R.id.drawerChangeOperator);

        mDrawerMyEarnings = (Button) mRootView.findViewById(R.id.drawerMyEarnings);
        mDrawerHowToUse = (Button) mRootView.findViewById(R.id.drawerHowToUse);
        mDrawerContactUs = (Button) mRootView.findViewById(R.id.drawerContactUs);
        mDrawerLogout = (Button) mRootView.findViewById(R.id.drawerLogout);
        mDrawerShareApp = (Button) mRootView.findViewById(R.id.drawerShareApp);
        mDrawerFreeAccessCode = (Button) mRootView.findViewById(R.id.drawerFreeAccessCode);
        ///------------
        mDrawerTest = (Button) mRootView.findViewById(R.id.drawerTest);
        mDrawerTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = getString(R.string.app_name);
                Fragment newContent = new SignUpFragmentStepSix();

                if (newContent != null) {
                    DashboardActivity mainActivity = (DashboardActivity) getActivity();
                    mainActivity.switchContent(newContent, null, false, tag);
                }
            }
        });
        //----------


        mDrawerHome.setOnClickListener(this);
        mDrawerProfile.setOnClickListener(this);
        mDrawerNotifications.setOnClickListener(this);
        mDrawerChangePin.setOnClickListener(this);
        mDrawerPurchasedHistory.setOnClickListener(this);
        mDrawerMyReviews.setOnClickListener(this);
        mDrawerMyEarnings.setOnClickListener(this);
        mDrawerMyEarnings.setOnClickListener(this);
        mDrawerChangeOperator.setOnClickListener(this);
        mDrawerHowToUse.setOnClickListener(this);

        mDrawerContactUs.setOnClickListener(this);
        mDrawerLogout.setOnClickListener(this);
        mDrawerShareApp.setOnClickListener(this);
        mDrawerUnSubscribe.setOnClickListener(this);
        mDrawerUnSubscribeNew.setOnClickListener(this);
        mDrawerFreeAccessCode.setOnClickListener(this);

      /*  String my_review_count = AppPreference.getSetting(mContext, Constants.Request.MY_REVIEWS_COUNT, "");
        if (my_review_count.equalsIgnoreCase("0") || my_review_count.equalsIgnoreCase("Constants.Request.MY_REVIEWS_COUNT") || my_review_count.equalsIgnoreCase("")) {
            mDrawerMyReviews.setText(getString(R.string.my_reviews));
        } else {

            int review = Integer.parseInt(my_review_count);
            if (review > 5)
                mDrawerMyReviews.setText(getString(R.string.my_reviews) + "" + "(5+)");
            else
                mDrawerMyReviews.setText(getString(R.string.my_reviews) + "" + "(" + my_review_count + ")");
        }*/

        AppInstance.profileData = new ProfileInfo();
        loadProfileInfo();
        checkProfileCompletion();


    }

    public void loadProfileInfo() {
        if (AppInstance.profileData != null) {
            AppInstance.setName(AppPreference.getSetting(mContext, Constants.Request.USER_NAME, ""));
            AppInstance.profileData.setName(AppPreference.getSetting(mContext, Constants.Request.USER_NAME, ""));
            AppInstance.profileData.setEmail(AppPreference.getSetting(mContext, Constants.Request.EMAIL_ID, ""));
            AppInstance.profileData.setGender(AppPreference.getSetting(mContext, Constants.Request.GENDER, ""));
            AppInstance.profileData.setIsOoredooCustomer(AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_OOREDOO, ""));
            AppInstance.profileData.setIsVodafoneCustomer(AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, ""));
            AppInstance.profileData.setNationality(AppPreference.getSetting(mContext, Constants.Request.NATIONALITY, ""));

            Log.d("PROFD1", "loadProfileInfo:gender :\"" + AppInstance.profileData.getGender() + "\"");
            Log.d("PROFD1", "loadProfileInfo:nationlty:\"" + AppInstance.profileData.getNationality() + "\"");
            Log.d("PROFD1", "loadProfileInfo:voda :\"" + AppInstance.profileData.getIsVodafoneCustomer() + "\"");
            Log.d("PROFD1", "loadProfileInfo:oredo :\"" + AppInstance.profileData.getIsOoredooCustomer() + "\"");
        }
    }
//    }

    public void checkProfileCompletion() {
        completedFields = 4;
        completedPercentage = 0;
        if (AppInstance.profileData != null) {
            if (AppInstance.profileData.getName().length() > 0) {
                txvUserName.setText("Hi, " + AppInstance.profileData.getName() + "!");
            }
//            if (AppInstance.profileData.getName().length() > 0) {
//                completedFields++;
//            }
//            if (AppInstance.profileData.getEmail().length() > 0) {
//                completedFields++;
//            }
//            if (AppInstance.profileData.getGender().length() > 0) {
//                completedFields++;
//            }
//            if (AppInstance.profileData.getIsOoredooCustomer().equalsIgnoreCase("1")) {
//                completedFields++;
//            } else if (AppInstance.profileData.getIsVodafoneCustomer().equalsIgnoreCase("1")) {
//                completedFields++;
//            }
            if (AppInstance.profileData.getNationality().length() > 0) {
                completedFields++;
                Log.d("PROFD", "complete: " + AppInstance.profileData.getNationality() + completedFields);


            }
        }
        Log.d("sadfafdsG", "onSuccessRedirection1: " + AppInstance.canUserUnSubscribe);

        if (AppInstance.canUserUnSubscribe) {
            Log.d("sadfafdsG", "onSuccessRedirection: ");
            mDrawerUnSubscribeNew.setVisibility(View.VISIBLE);
        } else {
            mDrawerUnSubscribeNew.setVisibility(View.GONE);
            Log.d("sadfafdsG", "onSuccessRedirection: ");
        }
        Log.d("PROFD", "FIELDS: " + completedFields);
        completedPercentage = (completedFields) * 20;
        Log.d("PROFD", "percentge: " + completedPercentage);
        if (completedPercentage == 100) {
            txvProfile.setVisibility(View.GONE);
            AppInstance.isProfileCompleted = true;
        } else {
            AppInstance.isProfileCompleted = false;
            txvProfile.setText(completedPercentage + " %");
        }


        if (AppInstance.myReviewsCount > 0 && AppInstance.myReviewsCount < 6) {
            Log.d("PROFD", "Review Count is: " + AppInstance.myReviewsCount);
            mDrawerMyReviews.setText(getString(R.string.my_reviews));
            txvReview.setVisibility(View.VISIBLE);
            txvReview.setText("" + AppInstance.myReviewsCount);
        } else if (AppInstance.myReviewsCount > 5) {
            Log.d("PROFD", "Review Count is+: " + AppInstance.myReviewsCount);
            mDrawerMyReviews.setText(getString(R.string.my_reviews));
            txvReview.setVisibility(View.VISIBLE);
            txvReview.setText("5+");
        } else {
            txvReview.setVisibility(View.GONE);
            Log.d("PROFD", "Review Count is-: " + AppInstance.myReviewsCount);

        }

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        Fragment newContent = null;
        String title = null;
        String tag = null;
        boolean addToBackStack = true;

        switch (v.getId()) {
            case R.id.drawerHome: {
                title = getString(R.string.app_name);
                tag = "HOME_FRAGMENT";
                AppPreference.setSetting(mContext, "key_home_offers", "");
//                Fragment frg = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
//                if (frg instanceof HomeFragment) {
//                    Log.d("MYHOMEA", "setOnOpenedListener a");
//                }
                addToBackStack = false;
                newContent = new HomeFragment();
                break;
            }
            case R.id.drawerMyProfile: {
                title = "Hi, " + AppInstance.profileData.getName();
                newContent = new ProfileFragment();
                break;
            }
            case R.id.drawerUnSubscribeNew: {
                title = getString(R.string.un_subscribe);
                newContent = new UnSubscribeAppConfirmationFragment();

                break;
            }
            case R.id.drawerMyNotification: {
                title = getString(R.string.my_notification);
                newContent = new NotificationFragment();
                break;
            }
            case R.id.drawerChangePin: {
                title = getString(R.string.change_pin);
                newContent = new ChangePinFragment();
                break;
            }
            case R.id.drawerHowToUse: {
                title = getString(R.string.how_to_use);
                newContent = new HowToUseFragment();
                break;
            }
            case R.id.drawerMyEarnings: {
                title = getString(R.string.my_earnings);
                newContent = new MyEarningsFragment();
                break;
            }
            case R.id.drawerContactUs: {
                title = getString(R.string.contact_us);
                newContent = new ContactUsFragment();
                break;
            }
            case R.id.drawerPurchasedHistory: {
                title = getString(R.string.purchase_history);
                newContent = new PurchaseHistoryFragment();
                break;
            }
            case R.id.drawerMyReviews: {
                AppInstance.myReviewsList = null;
                title = getString(R.string.my_reviews);
                newContent = new MyReviewsFragment();
                break;
            }
            case R.id.drawerLogout: {
                utilObj.showCustomAlertDialog(getActivity(), getString(R.string.logout), getString(R.string.logout_confirm_message), getString(R.string.yes), getString(R.string.no), true, this);

                break;
            }
            case R.id.drawerChangeOperator: {
                title = getString(R.string.change_cellular_operator);
                newContent = new ChangeCellularOperatorFragment();
                break;
            }

            case R.id.drawerShareApp: {
                title = getString(R.string.share);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String formattedString = String.format(getString(R.string.app_share_message_text), Constants.DEFAULT_VALUES.SHARE_URL, "");

                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, formattedString);
                startActivity(Intent.createChooser(sharingIntent, "Select"));
                break;
            }

            case R.id.drawerUnSubscribe: {
                title = getString(R.string.un_subscribe);
                newContent = new UnSubscribeAppConfirmationFragment();

                break;
            }
            case R.id.drawerFreeAccessCode: {
                title = getString(R.string.access_code);
                newContent = new FreeAccessFragment();
                break;
            }

        }


        if (newContent != null) {
            DashboardActivity mainActivity = (DashboardActivity) getActivity();
            //mainActivity.switchContent(newContent, null, false, title);
            mainActivity.switchContent(newContent, tag, addToBackStack, title);

        }
    }


    @Override
    public void callConfirmationDialogPositive() {
        if (NetworkUtils.isConnected(mContext)) {
            utilObj.setTextColorOfMessage("" + ContextCompat.getColor(mContext, R.color.white));
            utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
            MyApplication.getInstance().trackEvent(getString(R.string.ga_event_category_logout_yes), getString(R.string.ga_event_action_logout_yes), getString(R.string.ga_event_label_logout_yes));
            loginManagerObj.doApplicationLogout();
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void callConfirmationDialogNegative() {
        MyApplication.getInstance().trackEvent(getString(R.string.ga_event_category_logout_no), getString(R.string.ga_event_action_logout_no), getString(R.string.ga_event_label_logout_no));
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        //------ Get gcm ID
        String gcmID = AppPreference.getSetting(getActivity(), Constants.GCM.RECEIVED_GCM_ID, "");
        String mStoredDeviceID = AppPreference.getSetting(mContext, Constants.Request.DEVICE_ID, "");

        //------ Clear Preference
        AppPreference.clearSharedPreference(getActivity().getApplicationContext());

        //------ Save gcm ID
        AppPreference.setSetting(getActivity(), Constants.GCM.RECEIVED_GCM_ID, gcmID);
        AppPreference.setSetting(mContext, Constants.Request.DEVICE_ID, mStoredDeviceID);

        //call the intent for the next activity
        AppInstance.getAppInstance().setIsUserSubscribed(false);
        AppInstance.getAppInstance().setIsSubscriptionCheckCalled(false);
        AppInstance.getAppInstance().setIsSubscriptionUpdateCheckCalled(false);
        AppInstance.signUpUser = null;
        AppInstance.rModel_merchintList = null;
        AppInstance.offers = null;
//        AppInstance.rModel_homeOffers = null;
        AppInstance.rModel_checkSubscription = null;
        AppInstance.myReviewsCount = 0;
        AppInstance.isProfileCompleted = false;
        AppInstance.loggedInUser = null;
        AppInstance.myReviewsList = null;
        AppInstance.isMyFavesDeleteMessageShown = false;
        AppPreference.setSetting(mContext, "key_country_list", "");
        Intent intentObj = new Intent(getActivity(), MainActivity.class);
       /* intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intentObj.putExtra(Constants.DEFAULT_VALUES.LOGOUT, Constants.DEFAULT_VALUES.SUCCESS);
        startActivity(intentObj);
        getActivity().finish();
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
    }


    private class UpdateMyReviewCount extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Constants.DEFAULT_VALUES.MY_REVIEW_UPDATED)) {
                if (mActivity != null && isAdded()) {
                    String highestMyReviews = "5+";
                    Bundle bundleExtra = intent.getBundleExtra(Constants.DEFAULT_VALUES.MY_REVIEW_COUNT_CHANGED);
                    if (bundleExtra != null) {
                        if (bundleExtra.getBoolean(Constants.DEFAULT_VALUES.MY_REVIEW_COUNT_CHANGED)) {
                            AppInstance.myReviewsCount = AppInstance.myReviewsCount - 1;
                            if (AppInstance.myReviewsCount == 0) {
                                mDrawerMyReviews.setText(getString(R.string.my_reviews));
                            } else if (AppInstance.myReviewsCount > 5) {
                                mDrawerMyReviews.setText(getString(R.string.my_reviews) + highestMyReviews);
                            }
//                            else {
//                                if (AppInstance.myReviewsCount != -1) {
//                                    mDrawerMyReviews.setText(getString(R.string.my_reviews) + "(" + AppInstance.myReviewsCount + ")");
//                                }
//                            }
                        }
                    } else {
                        if (AppInstance.myReviewsCount > 0 && AppInstance.myReviewsCount < 6) {
                            mDrawerMyReviews.setText(getString(R.string.my_reviews));
                            txvReview.setVisibility(View.VISIBLE);
                            txvReview.setText("" + AppInstance.myReviewsCount);
                        } else if (AppInstance.myReviewsCount > 5) {
                            mDrawerMyReviews.setText(getString(R.string.my_reviews));
                            txvReview.setVisibility(View.VISIBLE);
                            txvReview.setText("" + highestMyReviews);
                        }
                    }
                }

            }
        }
    }

    private class ShowUnSubscribeOption extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            MyApplication.getInstance().printLogs("ShowUnSubscribeOption", "ShowUnSubscribeOption called :" + intent.getAction());
            if (intent.getAction().equalsIgnoreCase(Constants.DEFAULT_VALUES.SHOW_VODA_CUSTOMER_UNSUBSCRIBE_OPTION)) {
                // To show voda customer unSubscribe option.
                if (mActivity != null && isAdded()) {
                    mDrawerUnSubscribe.setVisibility(View.VISIBLE);
                    mDrawerUnSubscribeSeperaterView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private class HideUnSubscribeOption extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            MyApplication.getInstance().printLogs("HideUnSubscribeOption", "HideUnSubscribeOption called :" + intent.getAction());
            if (intent.getAction().equalsIgnoreCase(Constants.DEFAULT_VALUES.HIDE_VODA_CUSTOMER_UNSUBSCRIBE_OPTION)) {
                // To show voda customer unSubscribe option.
                if (mActivity != null && isAdded()) {
                    mDrawerUnSubscribe.setVisibility(View.GONE);
                    mDrawerUnSubscribeSeperaterView.setVisibility(View.GONE);
                }
            }
        }
    }
}
