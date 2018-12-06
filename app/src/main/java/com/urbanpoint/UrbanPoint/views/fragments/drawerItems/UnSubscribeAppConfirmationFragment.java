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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.managers.UnSubscribe_WebHit_Get_Ooredoo;
import com.urbanpoint.UrbanPoint.managers.UnSubscribe_WebHit_Post_Vodafone;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.fragments.main.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.urbanpoint.UrbanPoint.utils.Constants.Request.MIXPANEL_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnSubscribeAppConfirmationFragment extends Fragment implements View.OnClickListener, CustomDialogConfirmationInterfaces {


    private Utility utilObj;
    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ImageView mSlideButton;

    private Button mDoVodaUnSubscribeYesButton;
    private Button mDoVodaUnSubscribeNoButton;

    public UnSubscribeAppConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_do_voda_unsubscribe_confirm, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);

        initialize();

        return view;
    }


    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.un_subscribe));
        utilObj = new Utility(getActivity());
        setActionBar(getString(R.string.un_subscribe), true);
        bindViews();
    }

    private void bindViews() {

        mDoVodaUnSubscribeYesButton = (Button) mRootView.findViewById(R.id.doVodaUnSubscribeYesButton);
        mDoVodaUnSubscribeNoButton = (Button) mRootView.findViewById(R.id.doVodaUnSubscribeNoButton);

        mDoVodaUnSubscribeYesButton.setOnClickListener(this);
        mDoVodaUnSubscribeNoButton.setOnClickListener(this);

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
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = null;
        switch (v.getId()) {
            case R.id.doVodaUnSubscribeYesButton:
                fragmentTransaction = fragmentManager.beginTransaction();
                mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.content_frame, new UnSubscribeAppEnterMobileDetailsFragment());
                fragmentTransaction.commit();
                break;
            case R.id.doVodaUnSubscribeNoButton:
                fragmentTransaction = fragmentManager.beginTransaction();
                mActivity.overridePendingTransition(R.anim.slide_in_down, 0);
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                fragmentTransaction.replace(R.id.content_frame, new HomeFragment());
                fragmentTransaction.commit();
                break;

        }
    }

    @Override
    public void callConfirmationDialogPositive() {
        Log.d("dsads", "callConfirmationDialogPositive: ");
    }

    @Override
    public void callConfirmationDialogNegative() {
        Log.d("dsads", "callConfirmationDialogNegative: ");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
