package com.urbanpoint.UrbanPoint.views.fragments.drawerItems;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.UnSubscribe_WebHit_Get_Ooredoo;
import com.urbanpoint.UrbanPoint.managers.UnSubscribe_WebHit_Post_Vodafone;
import com.urbanpoint.UrbanPoint.managers.main.HomeManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.PurchaseSuccessActivity;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryView;
import com.urbanpoint.UrbanPoint.views.fragments.main.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.urbanpoint.UrbanPoint.utils.Constants.Request.MIXPANEL_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnSubscribeAppEnterMobileDetailsFragment extends Fragment implements View.OnClickListener, CustomDialogConfirmationInterfaces, ServiceRedirection {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ImageView mSlideButton;

    private Button mDoVodafoneUnSubscribeButton;
    private PinEntryView mDoVodaUnSubscribeMobileNumberEntry;
    private Utility utilObj;
    private String enteredMobileNumber = "";
    private HomeManager mHomeManager;
    private boolean isOoredoo;
    private FragmentManager mFrgmMngr;
    private ImageView mBackButton;

    public UnSubscribeAppEnterMobileDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_do_voda_unsubscribe, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);

        initialize();

        if (AppInstance.profileData.getIsOoredooCustomer() != null &&
                AppInstance.profileData.getIsOoredooCustomer().equals("1")) {
            setActionBar("Ooredoo User", false);
            Log.d("VSdsad", "Ooredo " + AppInstance.profileData.getIsOoredooCustomer());
            isOoredoo = true;
        } else if (AppInstance.profileData.getIsVodafoneCustomer() != null &&
                AppInstance.profileData.getIsVodafoneCustomer().equals("1")) {
            setActionBar("Vodafone User", false);
            Log.d("VSdsad", "Voda " + AppInstance.profileData.getIsVodafoneCustomer());
            isOoredoo = false;
        }
        return view;
    }


    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.un_subscribe_enter_mobile_no));
        utilObj = new Utility(getActivity());
        mHomeManager = new HomeManager(mContext, this);
        mFrgmMngr = getFragmentManager();
        setActionBar(getString(R.string.un_subscribe), true);

        bindViews();
    }

    private void bindViews() {

        mDoVodafoneUnSubscribeButton = (Button) mRootView.findViewById(R.id.doVodafoneUnSubscribeButton);
        mDoVodaUnSubscribeMobileNumberEntry = (PinEntryView) mRootView.findViewById(R.id.doVodaUnSubscribeMobileNumberEntry);

        mDoVodafoneUnSubscribeButton.setOnClickListener(this);

        mDoVodaUnSubscribeMobileNumberEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredMobileNumber = pin;
                utilObj.keyboardClose(mContext, mDoVodaUnSubscribeMobileNumberEntry);
            }
        });

    }

    public void setActionBar(String title, boolean showNavButton) {
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        Animation animation = AnimationUtils.loadAnimation(getContext(),
//                R.anim.right_in);
        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
//        customView.startAnimation(animation);
        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFrgmMngr.popBackStack();
                return false;
            }
        });

        title1.setText(title);
        getActivity().getActionBar().setCustomView(customView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doVodafoneUnSubscribeButton:
                String msisdn = AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.MSISDN_ID, "");
                Log.d("asfdfadsf", "onClick: " + msisdn);

                if (validatingRequired()) {
//                    mHomeManager.doUnSubscribe(enteredMobileNumber);
                    if (NetworkUtils.isConnected(mContext)) {
                        if (enteredMobileNumber.length() > 7) {
                            enteredMobileNumber = 974 + enteredMobileNumber;
                            Log.d("asfdfadsf", "enterd: " + enteredMobileNumber);
                            if (enteredMobileNumber.equals(msisdn)) {
                                utilObj.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), true);
//                        mHomeManager.doUnSubscribe(Constants.API_EGYPTLINX_PARAMS_VALUES.QATAR_COUNTRY_CODE + enteredMobileNumber);
                                if (isOoredoo) {
                                    UnSubscribe_WebHit_Get_Ooredoo unSubscribe_webHit_get_ooredoo = new UnSubscribe_WebHit_Get_Ooredoo();
                                    unSubscribe_webHit_get_ooredoo.checkValidation(getActivity(), this, msisdn);
                                } else {
                                    UnSubscribe_WebHit_Post_Vodafone unSubscribe_webHit_post_vodafone = new UnSubscribe_WebHit_Post_Vodafone();
                                    unSubscribe_webHit_post_vodafone.checkValidation(getActivity(), this, "");
                                }
                            } else {
                                mDoVodaUnSubscribeMobileNumberEntry.clearText();
                                utilObj.showCustomAlertDialog(mActivity, null, "The phone number that you have entered is incorrect. Please enter the phone number associated to your account", null, null, false, null);
                            }
                        } else {
                            mDoVodaUnSubscribeMobileNumberEntry.clearText();
                            utilObj.showCustomAlertDialog(mActivity, null, "Invalid mobile number", null, null, false, null);
                        }
                    } else {
                        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }


                }
                break;
        }
    }

    @Override
    public void callConfirmationDialogPositive() {

    }

    @Override
    public void callConfirmationDialogNegative() {

    }

    private boolean validatingRequired() {
        String message = "";

        //validate the content

        if (enteredMobileNumber.length() < 8) {
            message = getResources().getString(R.string.enter_mobile_number);
            utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, null);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }


    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();
        utilObj.stopiOSLoader();

        AppInstance.setIsSubscriptionCheckCalled(false);
        // To set that user is suscribe
        AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.IS_USER_PAID_PAYMENT, Constants.DEFAULT_VALUES.ZERO);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = null;
        fragmentTransaction = fragmentManager.beginTransaction();
        mActivity.overridePendingTransition(R.anim.slide_in_down, 0);
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentTransaction.replace(R.id.content_frame, new HomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.stopiOSLoader();
        utilObj.showToast(mActivity, errorMessage, Toast.LENGTH_LONG);

    }

    public void showResultUnSubOoredoo(boolean b, String s) {
        utilObj.stopiOSLoader();
        utilObj.stopiOSLoader();
        if (b) {
//            utilObj.showToast(mActivity, "You have been unsubscribed successfully", Toast.LENGTH_LONG);
            AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.MSISDN_ID, "");
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "false");
            AppInstance.getAppInstance().setIsUserSubscribed(false);
            AppInstance.canUserUnSubscribe = false;
            goToHome();

            MixpanelAPI mixpanel =
                    MixpanelAPI.getInstance(mContext, MIXPANEL_TOKEN);

            JSONObject props = null;
            try {
                props = new JSONObject();
                props.put("", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mixpanel.track("Unsubscribe confirmation", props);

//            utilObj.showCustomAlertDialog(mActivity, null, "You have been unsubscribed successfully", null, null, false, null);
            utilObj.showToast(mActivity, "You have been unsubscribed successfully", Toast.LENGTH_LONG);
        } else {
            utilObj.showCustomAlertDialog(mActivity, null, s, null, null, false, null);

        }
    }

    private void goToHome() {
        //mSlideButton.toggle(true);

        Intent intentObj = new Intent(mContext, DashboardActivity.class);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentObj);
    }

    public void showResultUnSubVodafone(boolean b, String s) {
        utilObj.stopiOSLoader();
        utilObj.stopiOSLoader();
        if (b) {
 //            utilObj.showToast(mActivity, "You have been unsubscribed successfully", Toast.LENGTH_LONG);
            AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.MSISDN_ID, "");
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "false");
            AppInstance.getAppInstance().setIsUserSubscribed(false);
            AppInstance.canUserUnSubscribe = false;

            MixpanelAPI mixpanel =
                    MixpanelAPI.getInstance(mContext, MIXPANEL_TOKEN);
            JSONObject props = null;
            try {
                props = new JSONObject();
                props.put("", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mixpanel.track("Unsubscribe confirmation", props);

            goToHome();

            utilObj.showToast(mActivity, "You have been unsubscribed successfully", Toast.LENGTH_LONG);
//            utilObj.showCustomAlertDialog(mActivity, null, "You have been unsubscribed successfully", null, null, false, null);
        } else {
            mDoVodaUnSubscribeMobileNumberEntry.clearText();
            utilObj.showCustomAlertDialog(mActivity, null, s, null, null, false, null);
        }

    }


}
