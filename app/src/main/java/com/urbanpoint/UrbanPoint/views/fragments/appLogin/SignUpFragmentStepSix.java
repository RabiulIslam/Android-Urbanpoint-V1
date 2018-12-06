package com.urbanpoint.UrbanPoint.views.fragments.appLogin;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.managers.IntroActivityManager;
import com.urbanpoint.UrbanPoint.managers.SignUp_WebHit_Get_BriteVerifyEmail;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.HowItWorksActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.WebViewActivity;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryView;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.SignUpUser;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.appLogin.SignUpManager;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.ResponseCodes;
import com.urbanpoint.UrbanPoint.utils.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragmentStepSix extends Fragment implements View.OnClickListener, ServiceRedirection {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mSignUpBackView;
    private Button mSignUpFinishView;
    private EditText mSignUpUserEmail;
    private Utility utilObj;
    private SignUpManager mSignUpManagerObj;

    private PinEntryView mSignUpNewPinEntry;
    private PinEntryView mSignUpConfirmPinEntry;
    private String enteredNewPin = "";
    private String enteredConfirmPin = "";
    private LinearLayout mMainParentLayout;
    /*private CustomTextView mLoginTermsAndConditions;

    private CustomTextView mLoginTermsAndConditionsTwo;*/
    private TextView mLoginTermsAndConditions;
    private TextView mLoginTermsAndConditionsTwo;
    private ScrollView mScrollViewMainLayout;
    private int currentApiVersion;
    private boolean isValidEmail;
    private RelativeLayout rlProgressBar;
    private IntroActivityManager introActivityManager;


    public SignUpFragmentStepSix() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_step_six, null);
        // View view = inflater.inflate(R.layout.fragment_change_pin, null);


        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        //  getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        initialize();
        MyApplication.getInstance().trackScreenView(getResources().getString(R.string.get_started) + ":" + getResources().getString(R.string.ga_set_up_account_screen));

//        mSignUpUserEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    Log.d("asdfwe", "onEditorAction: ");
//                   }
//                return true;
//            }
//        });


        mSignUpUserEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    rlProgressBar.setVisibility(View.VISIBLE);
//                    doValidateEmail(mSignUpUserEmail.getText().toString());
                }
            }
        });
        return view;
    }

    private void initialize() {

        utilObj = new Utility(mActivity);
        mSignUpManagerObj = new SignUpManager(mActivity, this);
        introActivityManager = new IntroActivityManager(mContext, this);

        isValidEmail = false;

        bindViews();

    }

    private void bindViews() {
        rlProgressBar = (RelativeLayout) mRootView.findViewById(R.id.frg_sign_up_rl_progrssbar);

        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        //MyApplication.getInstance().printLogs("bindViews:currentApiVersion", "currentApiVersion:" + currentApiVersion);


        /*if (currentApiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
        } else{
            // do something for phones running an SDK before lollipop
        }*/

//        mScrollViewMainLayout = (BounceScrollView) mRootView.findViewById(R.id.scrollViewMainLayout);
        mScrollViewMainLayout = (ScrollView) mRootView.findViewById(R.id.scrollViewMainLayout);


        mLoginTermsAndConditions = (TextView) mRootView.findViewById(R.id.loginTermsAndConditions);
        mLoginTermsAndConditions.setOnClickListener(this);
        mLoginTermsAndConditionsTwo = (TextView) mRootView.findViewById(R.id.loginTermsAndConditions_Two);
        mLoginTermsAndConditionsTwo.setOnClickListener(this);
        mMainParentLayout = (LinearLayout) mRootView.findViewById(R.id.mainParentLayout);
        mSignUpBackView = (Button) mRootView.findViewById(R.id.signUpStepSixBackButton);
        mSignUpBackView.setOnClickListener(this);
        mSignUpFinishView = (Button) mRootView.findViewById(R.id.signUpStepSixFinishButton);
        mSignUpFinishView.setOnClickListener(this);


        mSignUpUserEmail = (EditText) mRootView.findViewById(R.id.signUpUserEmail);

        mSignUpNewPinEntry = (PinEntryView) mRootView.findViewById(R.id.signUpNewPinEntry);
        mSignUpConfirmPinEntry = (PinEntryView) mRootView.findViewById(R.id.signUpConfirmPinEntry);
        mSignUpNewPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredNewPin = pin;
                if (currentApiVersion < Build.VERSION_CODES.LOLLIPOP) {
                    mScrollViewMainLayout.fullScroll(View.FOCUS_DOWN);
                }
                mSignUpConfirmPinEntry.requestFocus();
            }
        });
        mSignUpConfirmPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredConfirmPin = pin;
                utilObj.keyboardClose(mContext, mSignUpConfirmPinEntry);
                if (currentApiVersion < Build.VERSION_CODES.LOLLIPOP) {
                    mScrollViewMainLayout.fullScroll(View.FOCUS_UP);
                }
            }
        });

        mSignUpConfirmPinEntry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (currentApiVersion < Build.VERSION_CODES.LOLLIPOP) {
                    mScrollViewMainLayout.fullScroll(View.FOCUS_DOWN);
                }
                mSignUpConfirmPinEntry.requestFocus();
                return false;
            }
        });

        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, mSignUpConfirmPinEntry);
                if (currentApiVersion < Build.VERSION_CODES.LOLLIPOP) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollViewMainLayout.fullScroll(View.FOCUS_UP);
                        }
                    }, 100);
                }
                return false;
            }
        });

    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.signUpStepSixBackButton:
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepFive());
                fragmentTransaction.commit();
                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_email_back), getResources().getString(R.string.ga_event_action_get_started_email_back), getResources().getString(R.string.ga_event_label_get_started_email_back));
                break;

            case R.id.signUpStepSixFinishButton: {
                if (validatingRequired()) {
                    if (NetworkUtils.isConnected(mContext)) {
                       //                        doValidateEmail(mSignUpUserEmail.getText().toString());

                            String email = mSignUpUserEmail.getText().toString();
                            String enteredPin = enteredNewPin;
                            int pinCode = Integer.parseInt(enteredPin);
                            int num4 = pinCode % 10;
                            int num1 = pinCode / 1000 % 10;
                            AppPreference.setSetting(mContext, Constants.Request.PINCODE, num1 + "**" + num4);


                            if (AppInstance.signUpUser != null) {
                                utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.sign_up_loading_message), false);
                                AppInstance.signUpUser.seteMailId(email);
                                AppInstance.signUpUser.setCustomerPin(enteredPin);
//                                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_email_finish), getResources().getString(R.string.ga_event_action_get_started_email_finish), getResources().getString(R.string.ga_event_label_get_started_email_finish));
//                        utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.sign_up_loading_message), false);
                                mSignUpManagerObj.doSignUp(AppInstance.signUpUser);
                            }

                    } else {
                        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }
                }
            }
            case R.id.mainParentLayout:
                // utilObj.keyboardClose(mContext, v);
                break;
            case R.id.loginTermsAndConditions:
            case R.id.loginTermsAndConditions_Two:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(Constants.Request.PAGE, Constants.StaticHtmlPage.LOGIN_RULES);
                intent.putExtra(Constants.DEFAULT_VALUES.ACTION_BAR_HEADER, getString(R.string.privacy_and_terms));

                startActivity(intent);

        }

    }

    private void doValidateEmail(String _email) {
        if (NetworkUtils.isConnected(mContext)) {
            //            mUpdateUserProfileManager.doUpdateUserProfile(_name, _email, _nationality);
            SignUp_WebHit_Get_BriteVerifyEmail signUp_webHit_get_briteVerifyEmail = new SignUp_WebHit_Get_BriteVerifyEmail();
            signUp_webHit_get_briteVerifyEmail.checkValidation(getActivity(), this, _email);
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }


    private boolean validatingRequired() {
        String message = "";
        String email = mSignUpUserEmail.getText().toString();
        String enteredPin = enteredNewPin;

        String confirmPin = enteredConfirmPin;

        //validate the content
        if (email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);
            //utilObj.showError(this, message, textViewObj, emailObj);
            utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), message, null, null, false, null);
        } else if (!utilObj.checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), message, null, null, false, null);
        } else if (enteredPin.length() < 4) {
            message = getResources().getString(R.string.sign_up_enter_pin_message);
            utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), message, null, null, false, null);
        } else if (confirmPin.length() < 4) {
            message = getResources().getString(R.string.sign_up_enter_confirm_pin_message);
            utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), message, null, null, false, null);
        } else if (!confirmPin.equalsIgnoreCase(enteredPin)) {
            message = getResources().getString(R.string.enter_pin_not_match);
            utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), message, null, null, false, null);
            mSignUpConfirmPinEntry.clearText();
            mSignUpNewPinEntry.clearText();
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onSuccessRedirection(int taskID) {

        if (taskID == Constants.TaskID.SIGN_UP_TASK_ID) {

            if (AppInstance.signUpUser != null) {
                SignUpUser signUpUser = AppInstance.signUpUser;
                if (ResponseCodes.STATUS_ZERO.equalsIgnoreCase(signUpUser.getStatus())) {
                    // utilObj.showToast(mContext, signUpUser.getResponseMessage(), Toast.LENGTH_LONG);
                    utilObj.stopiOSLoader();
                    utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), signUpUser.getResponseMessage(), null, null, false, null);
                } else {
                    AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "" + signUpUser.getVodafonecustomer());
                    AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_OOREDOO, "" + signUpUser.getOoredoocustomer());

                    AppPreference.setSetting(mContext, Constants.Request.EMAIL_ID, signUpUser.geteMailId());
                    AppPreference.setSetting(mContext, Constants.Request.GENDER, "" + signUpUser.getGender());
                    AppPreference.setSetting(mContext, Constants.Request.FIRST_NAME, signUpUser.getFirstName());
                    AppPreference.setSetting(mContext, Constants.Request.CUSTOMER_ID, signUpUser.getUserData());
                    AppPreference.setSetting(mContext, "key_new_offer_badge", "yes");
                    AppPreference.setSetting(mContext, "key_never_ask_again_location", "");
                    mSignUpManagerObj.doCMSSignUp(AppInstance.signUpUser, enteredConfirmPin);
                    introActivityManager.doFetchHomeOffers2();
                    introActivityManager.doCheckSubscribe(signUpUser.getUserData());

                }
            } else {
                utilObj.stopiOSLoader();
                AppInstance.signUpUser = AppInstance.tempLocalSignUpUser;
                utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), getResources().getString(R.string.no_internet), null, null, false, null);
            }

        } else if (taskID == Constants.TaskID.GCM_SIGN_UP_TASK_ID) {

// -- Don't show HowItWork when user is VODAFONE USER
            utilObj.stopiOSLoader();
            Intent intentObj = null;
            AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);

            if (AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "").equalsIgnoreCase("1") ||
                    AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_OOREDOO, "").equalsIgnoreCase("1")) {

                intentObj = new Intent(getActivity(), DashboardActivity.class);
                //  intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intentObj.putExtra(Constants.DEFAULT_VALUES.SIGN_UP, true);

                startActivity(intentObj);

                mActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

                getActivity().finish();

            } else {
                //call the intent for the next activity
                intentObj = new Intent(getActivity(), HowItWorksActivity.class);
                intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentObj.putExtra(Constants.DEFAULT_VALUES.SIGN_UP, true);

                startActivity(intentObj);
                mActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
    }

    public void showResultEmailValidation(boolean b, String s) {
        rlProgressBar.setVisibility(View.GONE);
        if (b) {
            if (NetworkUtils.isConnected(mContext)) {
                String email = mSignUpUserEmail.getText().toString();
                String enteredPin = enteredNewPin;
                int pinCode = Integer.parseInt(enteredPin);
                int num4 = pinCode % 10;
                int num1 = pinCode / 1000 % 10;
                AppPreference.setSetting(mContext, Constants.Request.PINCODE, num1 + "**" + num4);


                if (AppInstance.signUpUser != null) {
                    AppInstance.signUpUser.seteMailId(email);
                    AppInstance.signUpUser.setCustomerPin(enteredPin);
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_email_finish), getResources().getString(R.string.ga_event_action_get_started_email_finish), getResources().getString(R.string.ga_event_label_get_started_email_finish));
//                        utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.sign_up_loading_message), false);
                    mSignUpManagerObj.doSignUp(AppInstance.signUpUser);
                }

            } else {
                utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
            }

        } else {
            utilObj.stopiOSLoader();
            utilObj.showCustomAlertDialog(mActivity, getString(R.string.header_setup), getResources().getString(R.string.invalid_email), null, null, false, null);
//            utilObj.showToast(mContext, getResources().getString(R.string.invalid_email), Toast.LENGTH_LONG);
        }
    }

}