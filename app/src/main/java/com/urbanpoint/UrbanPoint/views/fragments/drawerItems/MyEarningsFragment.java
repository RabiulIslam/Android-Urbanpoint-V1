package com.urbanpoint.UrbanPoint.views.fragments.drawerItems;


import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.managers.drawerItems.MyEarningsManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.MyEarningsInfo;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.ResponseCodes;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyEarningsFragment extends Fragment implements ServiceRedirection {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ImageView mSlideButton;
    private Utility utilObj;
    private MyEarningsManager myEarningsManager;
    private TextView mFriendsRef;
    private TextView mFriendsSignedUp;
    private TextView mMyEarnings;
    private TextView mCashPaid;


    public MyEarningsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_earnings, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        initialize();
        return view;
    }


    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.my_earnings));

        setActionBar(getString(R.string.my_earnings), true);
        utilObj = new Utility(getActivity());
        myEarningsManager = new MyEarningsManager(mContext, this);

        bindViews();

        if (NetworkUtils.isConnected(mContext)) {
            utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
            myEarningsManager.doGetMyEarnings();
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }

    private void bindViews() {
        mFriendsRef = (TextView) mRootView.findViewById(R.id.friendsReferred);
        mFriendsSignedUp = (TextView) mRootView.findViewById(R.id.friendsSignedUp);
        mMyEarnings = (TextView) mRootView.findViewById(R.id.myEarnings);
        mCashPaid = (TextView) mRootView.findViewById(R.id.cashPaid);


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

        if (taskID == Constants.TaskID.GET_MY_EARNINGS_TASK_ID) {

            if (AppInstance.myEarningsInfo != null) {
                MyEarningsInfo myEarningsInfo = AppInstance.myEarningsInfo;
                if (myEarningsInfo.getStatus().equalsIgnoreCase(ResponseCodes.STATUS_ZERO)) {
                    utilObj.showToast(mContext, myEarningsInfo.getMessage(), Toast.LENGTH_LONG);
                } else {
                    mFriendsRef.setText("" + myEarningsInfo.getReferredFriend());
                    mFriendsSignedUp.setText("" + myEarningsInfo.getFriendsSignedUp());

                    Resources resources = getResources();
                    String data = String.format(resources.getString(R.string.qar), myEarningsInfo.getMyEarnings());
                    mMyEarnings.setText(data);
                    data = String.format(resources.getString(R.string.qar), myEarningsInfo.getCashPaid());
                    mCashPaid.setText(data);
                }
            }
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mActivity, errorMessage, Toast.LENGTH_LONG);
    }
}
