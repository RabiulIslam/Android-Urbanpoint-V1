package com.urbanpoint.UrbanPoint.views.fragments.drawerItems;


import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.managers.appLogin.LoginManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.UserChangePin;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryView;
import com.urbanpoint.UrbanPoint.views.fragments.main.HomeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePinFragment extends Fragment implements View.OnClickListener, ServiceRedirection {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mGetStartedView;
    private Button mLoginView;
    private ImageView mSlideButton;
    private TextView mWelcomeText;
    private TextView mUserEmail;
    private LoginManager loginManagerObj;
    private Utility utilObj;
    private TextView mChangePinButton;
    private String enteredCurrentPin = "";
    private String enteredNewPin = "";
    private int mWhoHasFocus = 0;
    private PinEntryView mChangePinOldPinEntry;
    private PinEntryView mChangePinNewPinEntry;
    private PinEntryView mChangePinConfirmNewPinEntry;
    private String enteredConfirmPin = "";
    private LinearLayout mMainParentLayout;
    private ImageView mBackButton;
    private FragmentManager frgMngr;

    public ChangePinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pin, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initialize();
        return view;
    }


    private void initialize() {
        utilObj = new Utility(getActivity());
        loginManagerObj = new LoginManager(mActivity, this);
        frgMngr = getFragmentManager();
        setActionBar(getString(R.string.change_pin), false);
        bindViews();

        MyApplication.getInstance().trackScreenView(getString(R.string.change_pin));
    }

    private void bindViews() {
        mMainParentLayout = (LinearLayout) mRootView.findViewById(R.id.mainParentLayout);
        //mMainParentLayout.setOnClickListener(this);
        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                return false;
            }
        });

        mWelcomeText = (TextView) mRootView.findViewById(R.id.welcomeText);
        mUserEmail = (TextView) mRootView.findViewById(R.id.userEmail);
        mChangePinButton = (TextView) mRootView.findViewById(R.id.changePinButton);
        mChangePinButton.setOnClickListener(this);

        mChangePinOldPinEntry = (PinEntryView) mRootView.findViewById(R.id.changePinOldPinEntry);
        mChangePinNewPinEntry = (PinEntryView) mRootView.findViewById(R.id.changePinNewPinEntry);
        mChangePinConfirmNewPinEntry = (PinEntryView) mRootView.findViewById(R.id.changePinConfirmNewPinEntry);

        mChangePinOldPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredCurrentPin = pin;
                mChangePinNewPinEntry.requestFocus();
            }
        });
        mChangePinNewPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredNewPin = pin;
                mChangePinConfirmNewPinEntry.requestFocus();
            }
        });
        mChangePinConfirmNewPinEntry.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                enteredConfirmPin = pin;
                utilObj.keyboardClose(mContext, mChangePinConfirmNewPinEntry);
            }
        });
        setUserDetails();
    }

    private void setUserDetails() {

        String emailID = AppPreference.getSetting(mContext, Constants.Request.EMAIL_ID, "");
        String userName = AppPreference.getSetting(mContext, Constants.Request.FIRST_NAME, "");

        Resources resources = getResources();
        userName = String.format(resources.getString(R.string.welcome), userName);
        mWelcomeText.setText(userName);
        mUserEmail.setText(emailID);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.changePinButton:
                if (validatingRequired()) {
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_change_pin), getResources().getString(R.string.ga_event_action_change_pin), getString(R.string.ga_event_label_change_pin));
                    if (NetworkUtils.isConnected(mContext)) {
                        utilObj.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                        loginManagerObj.doChangeUserPin(enteredCurrentPin, enteredNewPin);
                    } else {
                        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }
                }
                break;
            case R.id.mainParentLayout:
                // utilObj.keyboardClose(mContext, v);
                break;
        }

    }

    public void setActionBar(String title, boolean showNavButton) {
//        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
        Animation animation = AnimationUtils.loadAnimation(mContext,
                R.anim.right_in);
        customView.startAnimation(animation);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
                frgMngr.popBackStack();
                hideMyKeyboard();
//                mActivity.finish();
                return false;
            }
        });

        title1.setText(title);
        getActivity().getActionBar().setCustomView(customView);
    }

    private void hideMyKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mChangePinNewPinEntry.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mChangePinOldPinEntry.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mChangePinConfirmNewPinEntry.getWindowToken(), 0);
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.CHANGE_PIN_TASK_ID) {

            if (AppInstance.userChangePin != null) {
                UserChangePin userChangePin = AppInstance.userChangePin;

                if (userChangePin.getStatus().equalsIgnoreCase(Constants.DEFAULT_VALUES.ZERO)) {
                    mChangePinNewPinEntry.clearText();
                    mChangePinConfirmNewPinEntry.clearText();
                    mChangePinOldPinEntry.clearText();
                    utilObj.showToast(mContext,AppInstance.userChangePin.getMessage(), Toast.LENGTH_LONG);
                } else {
                    utilObj.showToast(mContext, "PIN successfully changed", Toast.LENGTH_LONG);
                    int pinCode = Integer.parseInt(enteredConfirmPin);
                    int num4 = pinCode % 10;
                    int num1 = pinCode / 1000 % 10;
                    AppPreference.setSetting(mContext, Constants.Request.PINCODE, num1 + "**" + num4);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    mActivity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                    fragmentTransaction.replace(R.id.content_frame, new ProfileFragment());
                    fragmentTransaction.commit();
                }

            } else {
                utilObj.showToast(mContext, mContext.getResources().getString(R.string.no_data_received), Toast.LENGTH_LONG);
            }

        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
    }

    private boolean validatingRequired() {
        String message = "";


        //validate the content

        if (enteredCurrentPin.length() < 4) {
            message = getResources().getString(R.string.enter_current_pin_message);
            utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, null);
        } else if (enteredNewPin.length() < 4) {
            message = getResources().getString(R.string.enter_new_pin_message);
            utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, null);
        } else if (enteredConfirmPin.length() < 4) {
            message = getResources().getString(R.string.enter_new_confirm_pin_message);
            utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, null);
        } else if (!enteredNewPin.equalsIgnoreCase(enteredConfirmPin)) {
            message = getResources().getString(R.string.change_pin_enter_pin_not_match);
            utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, null);
            mChangePinNewPinEntry.clearText();
            mChangePinConfirmNewPinEntry.clearText();
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }

}
