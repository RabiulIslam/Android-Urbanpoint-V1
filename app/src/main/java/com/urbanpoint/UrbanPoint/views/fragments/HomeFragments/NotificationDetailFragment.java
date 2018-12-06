package com.urbanpoint.UrbanPoint.views.fragments.HomeFragments;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.SplashScreenActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationDetailFragment extends Fragment implements View.OnClickListener {
    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ImageView mSlideButton;
    private Utility utilObj;
    private ImageView imvCross;
    private Button btnHome;
    private ImageView mBackButton;
    private FragmentManager mFragMgr;
    private TextView txvDay, txvDayUnit, txvYear, txv1, txv2, txv3;
    private Bundle mBundle;
    private Typeface novaThin, novaRegular;


    public NotificationDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_detail, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initialize();
        if (SplashScreenActivity.notificationId > 0) {
            SplashScreenActivity.notificationId = Constants.DEFAULT_VALUES.UNDEFINED_PUSH_NOTIFICATION;
        }
        Log.d("dsfdsafasf", "onCreateView ID: "+ SplashScreenActivity.notificationId);
        return view;
    }


    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.my_notification));
        utilObj = new Utility(getActivity());
        mBundle = getArguments();
        mFragMgr = getActivity().getSupportFragmentManager();
        novaThin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        novaRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        setActionBar(getResources().getString(R.string.my_notification), true);
        bindViews();
    }

    private void bindViews() {
        txvDay = (TextView) mRootView.findViewById(R.id.frg_notification_detail_txv_day);
        txvDayUnit = (TextView) mRootView.findViewById(R.id.frg_notification_detail_txv_day_unit);
        txvYear = (TextView) mRootView.findViewById(R.id.frg_notification_detail_txv_year);
        txv1 = (TextView) mRootView.findViewById(R.id.frg_notification_detail_txv_1);
        txv2 = (TextView) mRootView.findViewById(R.id.frg_notification_detail_txv_2);
        txv3 = (TextView) mRootView.findViewById(R.id.frg_notification_detail_txv_3);

        txvDay.setTypeface(novaThin);
        txvDayUnit.setTypeface(novaThin);
        txvYear.setTypeface(novaThin);
        txv1.setTypeface(novaRegular);
        txv2.setTypeface(novaRegular);
        txv3.setTypeface(novaRegular);
        imvCross = (ImageView) mRootView.findViewById(R.id.frg_notification_detail_imv_close);
        btnHome = (Button) mRootView.findViewById(R.id.frg_notification_detail_btn_home);


        if (mBundle != null) {
            txvDay.setText(mBundle.getString(Constants.Request.NOTIFICATION_DATE));
            String string = getCustomDateString(mBundle.getString(Constants.Request.NOTIFICATION_DATE));
            String strMessage = mBundle.getString(Constants.Request.NOTIFICATION_TEXT_1);
            String strTitle = mBundle.getString(Constants.Request.NOTIFICATION_TEXT_2);
            Log.d("sadsadsadqq", "bindViews: "+string);
            Log.d("sadsadsadqq", "bindViews: "+strMessage);
            String[] parts = string.split(",");
            String part1 = parts[0]; // 004
            String part2 = parts[1];
            String part3 = parts[2];
            txvDay.setText(part1);
            txvDayUnit.setText(part2);
            txvYear.setText(part3);
            txv2.setVisibility(View.VISIBLE);
            txv2.setText(strMessage);
            txv1.setVisibility(View.VISIBLE);
            txv1.setText(strTitle);

//            txv1.setText(mBundle.getString(Constants.Request.NOTIFICATION_TEXT_1));
//            txv2.setText(mBundle.getString(Constants.Request.NOTIFICATION_TEXT_2));
//            txv3.setText(mBundle.getString(Constants.Request.NOTIFICATION_TEXT_3));
        }
        imvCross.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

//        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
//        Animation animation = AnimationUtils.loadAnimation(mContext,
//                R.anim.right_in);
//        customView.startAnimation(animation);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
                mFragMgr.popBackStack();
                return false;
            }
        });

        title1.setText(title);
        title1.setTypeface(novaRegular);
        getActivity().getActionBar().setCustomView(customView);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.frg_notification_detail_btn_home:
                goToHome();
                break;
            case R.id.frg_notification_detail_imv_close:
                mFragMgr.popBackStack();
                break;
        }
    }

    public static String getCustomDateString(String strDate) {
        Date date = new Date();
        try {
//            String string = "2 January 2010";
            DateFormat format = new SimpleDateFormat("d MMMM yyyy");
            date = format.parse(strDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SimpleDateFormat tmp = new SimpleDateFormat("d");
        String a = "";
        String str = tmp.format(date);
        str = str.substring(0, 1).toUpperCase() + str.substring(1);

        if (date.getDate() > 10 && date.getDate() < 14)
            a = "th ";
        else {
            if (str.endsWith("1")) a = "st ";
            else if (str.endsWith("2")) a = "nd ";
            else if (str.endsWith("3")) a = "rd ";
            else a = "th ";
        }
        tmp = new SimpleDateFormat("MMMM yyyy");
        str = str + "," + a + "," + tmp.format(date);
        return str;
    }

    private void goToHome() {
        //mSlideButton.toggle(true);
        getActivity().finish();
        AppInstance.setIsSubscriptionCheckCalled(false);
        Intent intentObj = new Intent(getActivity(), DashboardActivity.class);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentObj);
    }
}
