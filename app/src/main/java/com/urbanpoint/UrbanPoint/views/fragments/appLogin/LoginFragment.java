package com.urbanpoint.UrbanPoint.views.fragments.appLogin;


import android.content.Context;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.dataobject.InputUser;
import com.urbanpoint.UrbanPoint.dataobject.LoggedInUser;
import com.urbanpoint.UrbanPoint.dataobject.drawerItem.MyReviewsList;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.managers.IntroActivityManager;
import com.urbanpoint.UrbanPoint.managers.appLogin.LoginManager;
import com.urbanpoint.UrbanPoint.managers.drawerItems.MyReviewsManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.ForgotPasswordActivity;
import com.urbanpoint.UrbanPoint.views.fragments.main.GetStartedFragment;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.ResponseCodes;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements ServiceRedirection, View.OnClickListener, CustomDialogConfirmationInterfaces {//, TextWatcher, View.OnFocusChangeListener {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mBackView;
    private Button mLoginToAppView;
    private LoginManager loginManagerObj;
    private Utility utilObj;
    private InputUser inputUserObj;
    private EditText mUserEmail;
    private TextView mForgotPassword;

    private PinEntryView mLoginPinEntry;
    private String enteredLoginPin = "";
    private MyReviewsManager mMyReviewManager;
    private LinearLayout mMainLoginLayout;
    private String mStoredDeviceID;
    private String gcmID;
    private IntroActivityManager introActivityManager;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initialize();
        MyApplication.getInstance().trackScreenView(getResources().getString(R.string.ga_login_screen));


        return view;
    }

    private void initialize() {
        utilObj = new Utility(mActivity);
        loginManagerObj = new LoginManager(mActivity, this);
        inputUserObj = new InputUser();
        introActivityManager = new IntroActivityManager(mContext, this);

        mMyReviewManager = new MyReviewsManager(mActivity, this);
        bindViews();

        gcmID = AppPreference.getSetting(mContext, Constants.GCM.RECEIVED_GCM_ID, "");
        mStoredDeviceID = AppPreference.getSetting(mContext, Constants.Request.DEVICE_ID, "");

    }

    private void bindViews() {
        mMainLoginLayout = (LinearLayout) mRootView.findViewById(R.id.mainLoginLayout);
        mMainLoginLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                return false;
            }
        });

        mBackView = (Button) mRootView.findViewById(R.id.loginBackButton);
        mBackView.setOnClickListener(this);

        mForgotPassword = (TextView) mRootView.findViewById(R.id.forgotPassword);
        mForgotPassword.setOnClickListener(this);

        mLoginToAppView = (Button) mRootView.findViewById(R.id.loginToApp);
        mLoginToAppView.setOnClickListener(this);

        mUserEmail = (EditText) mRootView.findViewById(R.id.userEmail);

        mLoginPinEntry = (PinEntryView) mRootView.findViewById(R.id.loginPinEntry);

        mLoginPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredLoginPin = pin;
                utilObj.keyboardClose(mContext, mLoginPinEntry);
            }
        });

    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.loginBackButton:
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new GetStartedFragment());
                fragmentTransaction.commit();
                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_login_screen_back), getResources().getString(R.string.ga_event_action_login_screen_back), getResources().getString(R.string.ga_event_label_login_screen_back));
                break;

            case R.id.forgotPassword:
                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.loginToApp:
                if (validatingRequired()) {

                    if (NetworkUtils.isConnected(mContext)) {
                        utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.logging_your_account), false);
                        //assigning the data to the user object
                        inputUserObj.setEmailid(mUserEmail.getText().toString());
                        String enteredPin = enteredLoginPin;
                        AppPreference.setSetting(mContext, Constants.Request.PINCODE_WITHOUTSTAR, enteredLoginPin);
                        Log.d("Aasda", "onClick: " + enteredPin);
                        inputUserObj.setPassword(enteredPin);
                        inputUserObj.setMode(Constants.Request.LOGIN_MODE);

                        inputUserObj.setDeviceTokenID(gcmID);

                        inputUserObj.setDevice_id(mStoredDeviceID);
                        String setting = AppPreference.getSetting(getActivity(), Constants.GCM.RECEIVED_GCM_ID, "");

                        inputUserObj.setDeviceTokenID(setting);
                        loginManagerObj.authenticateLogin(inputUserObj);


                        MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_login_screen_login_button), getResources().getString(R.string.ga_event_action_login_screen_login_button), getResources().getString(R.string.ga_event_label_login_screen_login_button));

                    } else {
                        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }

                }
                break;
        }
    }


    /**
     * The method is used to validate the required fields
     *
     * @return boolean true if fields are validated else false
     **/

    private boolean validatingRequired() {
        String message = "";
        String email = mUserEmail.getText().toString();
        String enteredPin = enteredLoginPin;

        //validate the content
        if (email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);
            //utilObj.showError(this, message, textViewObj, emailObj);
            utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, this);
        } else if (!utilObj.checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, this);
        } else if (enteredPin.length() < 4) {
            message = getResources().getString(R.string.PasswordErrorMessage);
            utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, this);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * The interface method implemented in the java files
     *
     * @param taskID the id based on which the relevant action is performed
     * @return none
     */
    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.LOGIN_TASK_ID) {

            if (AppInstance.loggedInUser != null) {
                LoggedInUser loggedInUser = AppInstance.loggedInUser;
                if (ResponseCodes.STATUS_ZERO.equalsIgnoreCase(loggedInUser.getStatus())) {
                    //  utilObj.showCustomAlertDialog(mActivity, null, getString(R.string.wrong_email_pin), null, null, false, this);
                    Log.d("LoginTOAST", "onSuccessRedirection: " + loggedInUser.getMessage());
                    utilObj.showCustomAlertDialog(mActivity, getResources().getString(R.string.ga_login_unsuccess_popup), loggedInUser.getMessage(), null, null, false, this);
                    mLoginPinEntry.clearText();
                } else {
                    int pinCode = Integer.parseInt(AppPreference.getSetting(mContext, Constants.Request.PINCODE_WITHOUTSTAR, ""));
                    int num4 = pinCode % 10;
                    int num1 = pinCode / 1000 % 10;
                    AppPreference.setSetting(mContext, Constants.Request.USER_NAME, loggedInUser.getUserInfo().get(0).getFirstName());
                    AppPreference.setSetting(mContext, Constants.Request.EMAIL_ID, loggedInUser.getUserInfo().get(0).getEmailId());
                    AppPreference.setSetting(mContext, Constants.Request.GENDER, loggedInUser.getGender());
                    AppPreference.setSetting(mContext, Constants.Request.PINCODE, num1 + "**" + num4);
                    AppPreference.setSetting(mContext, Constants.Request.FIRST_NAME, loggedInUser.getUserInfo().get(0).getFirstName());
                    AppPreference.setSetting(mContext, Constants.Request.CUSTOMER_ID, "" + loggedInUser.getUserInfo().get(0).getUserId());
                    AppPreference.setSetting(mContext, Constants.Request.AGE, "" + loggedInUser.getUserInfo().get(0).getAge());
                    AppPreference.setSetting(mContext, Constants.Request.DOB, "" + loggedInUser.getUserInfo().get(0).getDob());
                    AppPreference.setSetting(mContext, "key_never_ask_again_location", "");
                    AppPreference.setSetting(mContext, "key_new_offer_badge", "yes");
                    Log.d("SUBSCRB1", "Login2..isUserSubscribed:nationality " + loggedInUser.getNationality());
                    if (loggedInUser.getNationality() != null) {
                        AppPreference.setSetting(mContext, Constants.Request.NATIONALITY, "" + loggedInUser.getNationality());
                        AppPreference.setSetting(mContext, "key_country_list", "Showed");
                    } else {
                        AppPreference.setSetting(mContext, Constants.Request.NATIONALITY, "");
                        AppPreference.setSetting(mContext, "key_country_list", "");
                    }
//                    Log.d("SUBSCRB", "Login..isUserSubscribed: "+loggedInUser.getIsVodafoneCustomer());
//                    AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, ""+1);
                    AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "" + loggedInUser.getIsVodafoneCustomer());
                    AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_OOREDOO, "" + loggedInUser.getIsOoredooCustomer());
                    Log.d("SUBSCRB", "Login2..isUserOoredo: " + loggedInUser.getIsOoredooCustomer());
                    Log.d("SUBSCRB", "Login2..isUserVoda: " + loggedInUser.getIsVodafoneCustomer());
                    Log.d("SUBSCRB1", "Login2..isUserSubscribed:nationality " + loggedInUser.getNationality());

                    //mMyReviewManager.doGetReviewList();
                    String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                    if (custID.length() > 0) {
                        if (NetworkUtils.isConnected(mContext)) {
                            introActivityManager.doCheckSubscribe(AppPreference.getSetting(mContext,
                                    Constants.Request.CUSTOMER_ID, ""));
                            AppPreference.setSetting(mContext, "key_home_offers", "");
                            introActivityManager.doFetchHomeOffers2();
                        } else {
                            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                        }
                    }

                    //call the intent for the next activity
                    Intent intentObj = new Intent(mActivity, DashboardActivity.class);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intentObj);
                    mActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    mActivity.finish();

                }
            }

        } else if (taskID == Constants.TaskID.GET_MY_REVIEW_LIST_TASK_ID) {
            if (AppInstance.myReviewsList != null) {
                MyReviewsList myReviewsList = AppInstance.myReviewsList;
                if (myReviewsList.getStatus().equalsIgnoreCase(ResponseCodes.STATUS_ZERO)) {
                    AppPreference.setSetting(mContext, Constants.Request.MY_REVIEWS_COUNT, "0");

                } else {
                    if (myReviewsList.getMyReviewsListItemArrayList().size() > 0) {
                        AppPreference.setSetting(mContext, Constants.Request.MY_REVIEWS_COUNT, Integer.toString(myReviewsList.getMyReviewsListItemArrayList().size()));
                    } else {
                        AppPreference.setSetting(mContext, Constants.Request.MY_REVIEWS_COUNT, "0");
                    }
                }
            }
        }
    }

    /**
     * The interface method implemented in the java files
     *
     * @param errorMessage the error message to be displayed
     * @return none
     */
    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
    }

    @Override
    public void callConfirmationDialogPositive() {

    }

    @Override
    public void callConfirmationDialogNegative() {

    }


}
