package com.urbanpoint.UrbanPoint.views.fragments.drawerItems;


import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.drawerItems.ChangeCellularOperator;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeCellularOperatorFragment extends Fragment implements View.OnClickListener, ServiceRedirection {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;

    private ChangeCellularOperator mChangeOperatorManagerObj;
    private Utility utilObj;
    private ImageView mChangeOperatorVodafoneYes;
    private ImageView mChangeOperatorVodafoneNo;
    private Button mChangeOperatorSubmitButton;
    private String mIsVodafoneCustomer;
    private ImageView mSlideButton;
    private int isVodafoneCustomer;


    public ChangeCellularOperatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_operator, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        initialize();
        return view;
    }


    private void initialize() {
        utilObj = new Utility(getActivity());
        mChangeOperatorManagerObj = new ChangeCellularOperator(mActivity, this);
        setActionBar(getString(R.string.change_cellular_operator), true);
        bindViews();
    }

    private void bindViews() {

        mChangeOperatorVodafoneNo = (ImageView) mRootView.findViewById(R.id.changeOperatorVodafoneNo);
        mChangeOperatorVodafoneYes = (ImageView) mRootView.findViewById(R.id.changeOperatorVodafoneYes);
        mChangeOperatorSubmitButton = (Button) mRootView.findViewById(R.id.changeOperatorSubmitButton);


        mChangeOperatorVodafoneNo.setOnClickListener(this);
        mChangeOperatorVodafoneYes.setOnClickListener(this);
        mChangeOperatorSubmitButton.setOnClickListener(this);

        mIsVodafoneCustomer = AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "");

        if (mIsVodafoneCustomer.equalsIgnoreCase("" + Constants.Operator.VODAFONE)) {
            mChangeOperatorVodafoneYes.setBackgroundResource(R.mipmap.vodafone_yes_selected);
        } else {
            mChangeOperatorVodafoneNo.setBackgroundResource(R.mipmap.vodafone_no_selected);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.changeOperatorSubmitButton:

                if (NetworkUtils.isConnected(mContext)) {
                    utilObj.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                    mChangeOperatorManagerObj.changeCellularOperator(isVodafoneCustomer);
                } else {
                    utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                }

                break;
            case R.id.changeOperatorVodafoneNo:
                isVodafoneCustomer = 0;
                mChangeOperatorVodafoneNo.setBackgroundResource(R.mipmap.vodafone_no_selected);
                mChangeOperatorVodafoneYes.setBackgroundResource(R.mipmap.vodafone_yes_default);
                break;

            case R.id.changeOperatorVodafoneYes:
                isVodafoneCustomer = 1;
                mChangeOperatorVodafoneYes.setBackgroundResource(R.mipmap.vodafone_yes_selected);
                mChangeOperatorVodafoneNo.setBackgroundResource(R.mipmap.vodafone_no_default);
                break;
        }

    }

    public void setActionBar(String title, boolean showNavButton) {
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_contact_us, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mSlideButton = (ImageView) customView.findViewById(R.id.slidingMenuHowToUseButton);
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
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.CHANGE_CELLULAR_OPEARTOR) {
            AppPreference.setSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE_VODAFONE, "" + isVodafoneCustomer);
            utilObj.showCustomAlertDialog(getActivity(), null, getString(R.string.operator_change_success), null, null, false, null);
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mActivity, errorMessage, Toast.LENGTH_LONG);
    }


}
