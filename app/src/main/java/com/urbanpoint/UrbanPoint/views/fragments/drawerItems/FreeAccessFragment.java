package com.urbanpoint.UrbanPoint.views.fragments.drawerItems;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.drawerItem.AccessFree;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.drawerItems.AccessFreeManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.customViews.pinEntry.PinEntryView;
import com.urbanpoint.UrbanPoint.views.fragments.main.HomeFragment;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by swapnilahirrao on 24/10/16.
 */
public class FreeAccessFragment extends Fragment implements View.OnClickListener, ServiceRedirection, CustomDialogConfirmationInterfaces {
    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private AccessFreeManager mAccessFreeManager;
    private ImageView mSlideButton;
    private Utility utilObj;
    private LinearLayout mMainParentLayout;
    private PinEntryView accescodeview;
    private Button buttonSubmit;
    private String accsessCode = "";
    private String custID = "";
    private EditText edtAccessCode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_access_free, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initialize();

        edtAccessCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                edtAccessCode.setHint("");
                }
            }
        });
        return view;
    }

    private void initialize() {
        utilObj = new Utility(getActivity());
        mAccessFreeManager = new AccessFreeManager(mActivity, this);
        setActionBar(getString(R.string.access_code), true);
        bindViews();
        MyApplication.getInstance().trackScreenView(getString(R.string.access_code));

    }

    private void bindViews() {
        edtAccessCode = (EditText) mRootView.findViewById(R.id.accessCodeEditText);
        mMainParentLayout = (LinearLayout) mRootView.findViewById(R.id.mainParentLayout);
        //mMainParentLayout.setOnClickListener(this);
        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                return false;
            }
        });

        accescodeview = (PinEntryView) mRootView.findViewById(R.id.accessCodeView);
        buttonSubmit = (Button) mRootView.findViewById(R.id.accessCodeSubmitButton);
        buttonSubmit.setOnClickListener(this);


        accescodeview.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                accsessCode = pin;
//                utilObj.keyboardClose(mContext, mChangePinConfirmNewPinEntry);
            }
        });


    }

    public void setActionBar(String title, boolean showNavButton) {
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_change_pin, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
        TextView textViewCancel = (TextView) customView.findViewById(R.id.textViewCancel);

        textViewCancel.setVisibility(View.INVISIBLE);

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager fragmentManager = getFragmentManager();

                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                mActivity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                fragmentTransaction.replace(R.id.content_frame, new HomeFragment());

                fragmentTransaction.commit();
            }
        });

        mSlideButton = (ImageView) customView.findViewById(R.id.slidingMenuChangePinButton);
        mSlideButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //mSlideButton.toggle(true);
                View view = getActivity().getCurrentFocus();
                utilObj.keyboardClose(mContext, view);

                ((BaseActivity) getActivity()).getSlidingMenu().toggle();
            }
        });


        title1.setText(title);

        if (showNavButton) {
            mSlideButton.setVisibility(View.VISIBLE);

            ((BaseActivity) getActivity()).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            mSlideButton.setVisibility(View.GONE);

            ((BaseActivity) getActivity()).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);
        }


        getActivity().getActionBar().setCustomView(customView);
    }

    @Override
    public void onSuccessRedirection(int taskID) {

        if (taskID == Constants.TaskID.ACCESSCODE_TASK_ID) {
            utilObj.stopiOSLoader();
            AccessFree accessFree = AppInstance.accessFree;
            if (accessFree != null) {
                if (accessFree.getMessage() != null) {
                    AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 1);
                    utilObj.showCustomAlertDialog(mActivity, null, "Congratulations! You now have access to all Urban Point offers!", null, null, false, this);
//                    utilObj.showCustomAlertDialog(mActivity, null, accessFree.getMessage(), null, null, false, this);
                } else {
                    utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.no_data_received), null, null, false, null);
                }
            } else {
                utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.no_data_received), null, null, false, null);
            }
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showCustomAlertDialog(mActivity, null, getString(R.string.enter_access_code_empty_2), null, null, false, this);
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.accessCodeSubmitButton:

                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_access_code), getResources().getString(R.string.ga_event_action_access_code), getString(R.string.ga_event_label_access_code));
//                if (accsessCode.length() < 6) {
//                    utilObj.showCustomAlertDialog(mActivity, null, getString(R.string.enter_access_code_empty), null, null, false, null);
//                } else {
                if (edtAccessCode.length() > 0) {
                    if (NetworkUtils.isConnected(mContext)) {

                        custID = AppPreference.getSetting(getActivity(), Constants.Request.CUSTOMER_ID, "");

                        //----- TODO : COMMENTED FOR NOW, ONCE API IS COMPLETED, UNCOMMENT IT
                        utilObj.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                        mAccessFreeManager.putAccesCode(custID, edtAccessCode.getText().toString());
                        //----- TODO : COMMENTED FOR NOW, ONCE API IS COMPLETED, UNCOMMENT IT
                    } else {
                        utilObj.showCustomAlertDialog(mActivity, null, getString(R.string.no_internet), null, null, false, null);
                    }
                } else {
                    utilObj.showCustomAlertDialog(mActivity, null, getString(R.string.enter_access_code_empty_2), null, null, false, null);
                }

//                }
                break;
            default:
                break;
        }

    }

    @Override
    public void callConfirmationDialogPositive() {
        if (AppInstance.goHome) {
            AppInstance.setIsSubscriptionCheckCalled(false);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new HomeFragment());
            fragmentTransaction.commit();
        }else {
            edtAccessCode.setText("");
        }
    }

    @Override
    public void callConfirmationDialogNegative() {

    }
}
