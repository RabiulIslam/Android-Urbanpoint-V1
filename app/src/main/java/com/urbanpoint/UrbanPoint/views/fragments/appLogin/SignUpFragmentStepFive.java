package com.urbanpoint.UrbanPoint.views.fragments.appLogin;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragmentStepFive extends Fragment implements View.OnClickListener {

    private Utility utilObj;

    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mSignUpBackView;
    private Button mSignUpContinueView;

//    private ImageView mVodafoneNoImageView;
//    private ImageView mVodafoneYesImageView;

    private ImageView imvVodafone, imvVodafoneLarge, imvOoredoo, imvOoredooLarge, imvOoredooSelector, imvVodafoneSelector;
    private TextView txvVodofone, txvOoredoo;
    private RelativeLayout rlVodafone, rlOoredoo;

    private int isVodafoneCustomer = -1;
    private int isOoredooCustomer = -1;

    public SignUpFragmentStepFive() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_step_five, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        initialize();
        MyApplication.getInstance().trackScreenView(getResources().getString(R.string.get_started) + ":" + getResources().getString(R.string.sign_up_step_four_message));
        return view;
    }

    private void initialize() {
        utilObj = new Utility(mActivity);
        bindViews();
    }

    private void bindViews() {

        imvVodafoneSelector = (ImageView) mRootView.findViewById(R.id.frg_sign_up_five_imv_vodafone_selector_tick);
        imvOoredooSelector = (ImageView) mRootView.findViewById(R.id.frg_sign_up_five_imv_ooredoo_selector_tick);
        rlVodafone = (RelativeLayout) mRootView.findViewById(R.id.frg_sign_up_five_rl_vodofone);
        rlVodafone.setOnClickListener(this);
        rlOoredoo = (RelativeLayout) mRootView.findViewById(R.id.frg_sign_up_five_rl_ooredoo);
        rlOoredoo.setOnClickListener(this);
        imvVodafone = (ImageView) mRootView.findViewById(R.id.frg_sign_up_five_imv_vodafone);
        imvVodafoneLarge = (ImageView) mRootView.findViewById(R.id.frg_sign_up_five_imv_vodafone_large);
        imvOoredoo = (ImageView) mRootView.findViewById(R.id.frg_sign_up_five_imv_ooredoo);
        imvOoredooLarge = (ImageView) mRootView.findViewById(R.id.frg_sign_up_five_imv_ooredoo_large);
        txvVodofone = (TextView) mRootView.findViewById(R.id.frg_sign_up_five_txv_vodafone);
        txvOoredoo = (TextView) mRootView.findViewById(R.id.frg_sign_up_five_txv_ooredoo);

        mSignUpBackView = (Button) mRootView.findViewById(R.id.signUpStepFourBackButton);
        mSignUpBackView.setOnClickListener(this);
        mSignUpContinueView = (Button) mRootView.findViewById(R.id.signUpStepFourContinueButton);
        mSignUpContinueView.setOnClickListener(this);
//
//        mVodafoneNoImageView = (ImageView) mRootView.findViewById(R.id.iv_vodafoneNo);
//        mVodafoneYesImageView = (ImageView) mRootView.findViewById(R.id.iv_vodafoneYes);
//
//
//        mVodafoneNoImageView.setOnClickListener(this);
//        mVodafoneYesImageView.setOnClickListener(this);


//        isVodafoneCustomer = AppInstance.signUpUser.getVodafonecustomer();
//        if (isVodafoneCustomer == 1) {
//            imvVodafone.setVisibility(View.GONE);
//            imvVodafoneLarge.setVisibility(View.VISIBLE);
//            txvVodofone.setTypeface(null, Typeface.BOLD);
//        }
//        ;


//        if (isVodafoneCustomer != -1) {
//            if (isVodafoneCustomer == Constants.Operator.VODAFONE) {
//                mVodafoneYesImageView.setBackgroundResource(R.mipmap.vodafone_yes_selected);
//            } else {
//                mVodafoneNoImageView.setBackgroundResource(R.mipmap.vodafone_no_selected);
//            }
//        }

        if (AppInstance.signUpUser.getOoredoocustomer() == 1) {
            ooredooSelected();
        } else if (AppInstance.signUpUser.getVodafonecustomer() == 1) {
            vodafoneSelected();
        }

    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.signUpStepFourBackButton:
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepThree());
                fragmentTransaction.commit();
                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_vodafone_back), getResources().getString(R.string.ga_event_action_get_started_vodafone_back), getResources().getString(R.string.ga_event_label_get_started_vodafone_back));
                break;
            case R.id.signUpStepFourContinueButton:
                if (isVodafoneCustomer == -1 || isOoredooCustomer == -1) {
                    utilObj.showCustomAlertDialog(getActivity(), getString(R.string.header_vodafone), getString(R.string.select_is_vodafone_or_not), null, null, false, null);
                } else {
                    //-----------------
                    String toSend = getString(R.string.header_vodafone);
                    if (isVodafoneCustomer != Constants.Operator.VODAFONE) {
                        toSend = getString(R.string.others);
                    }
                    MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_get_started_vodafone_continue), getResources().getString(R.string.ga_event_action_get_started_vodafone_continue), getResources().getString(R.string.ga_event_label_get_started_vodafone_continue));

                    //-----------------
                    AppInstance.signUpUser.setVodafonecustomer(isVodafoneCustomer);
                    AppInstance.signUpUser.setOoredoocustomer(isOoredooCustomer);
                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepSix());
                    fragmentTransaction.commit();
                }
                break;
//            case R.id.iv_vodafoneNo:
//                isVodafoneCustomer = 0;
//                mVodafoneNoImageView.setBackgroundResource(R.mipmap.vodafone_no_selected);
//                mVodafoneYesImageView.setBackgroundResource(R.mipmap.vodafone_yes_default);
//                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_voda_screen),getResources().getString(R.string.ga_no_voda),getResources().getString(R.string.ga_no_voda));
//                break;
//
//            case R.id.iv_vodafoneYes:
//                isVodafoneCustomer = 1;
//                mVodafoneYesImageView.setBackgroundResource(R.mipmap.vodafone_yes_selected);
//                mVodafoneNoImageView.setBackgroundResource(R.mipmap.vodafone_no_default);
//                MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_voda_screen),getResources().getString(R.string.ga_yes_voda),getResources().getString(R.string.ga_yes_voda));
//                break;

            case R.id.frg_sign_up_five_rl_vodofone:
                vodafoneSelected();
//                utilObj.showCustomAlertDialog(getActivity(), "Vodafone","Coming Soon", null, null, false, null);
                break;
            case R.id.frg_sign_up_five_rl_ooredoo:
                ooredooSelected();
                break;
        }
    }

    private void ooredooSelected() {
        isVodafoneCustomer = 0;
        isOoredooCustomer = 1;

        imvOoredoo.setVisibility(View.GONE);
        imvOoredooLarge.setVisibility(View.VISIBLE);
        txvOoredoo.setTypeface(null, Typeface.BOLD);
        imvOoredooSelector.setVisibility(View.VISIBLE);

        imvVodafone.setVisibility(View.VISIBLE);
        imvVodafoneSelector.setVisibility(View.GONE);
        imvVodafoneLarge.setVisibility(View.GONE);
        txvVodofone.setTypeface(null, Typeface.NORMAL);
    }

    public void vodafoneSelected() {
        isVodafoneCustomer = 1;
        isOoredooCustomer = 0;
        imvVodafone.setVisibility(View.GONE);
        imvVodafoneLarge.setVisibility(View.VISIBLE);
        txvVodofone.setTypeface(null, Typeface.BOLD);
        imvVodafoneSelector.setVisibility(View.VISIBLE);

        imvOoredoo.setVisibility(View.VISIBLE);
        imvOoredooSelector.setVisibility(View.GONE);
        imvOoredooLarge.setVisibility(View.GONE);
        txvOoredoo.setTypeface(null, Typeface.NORMAL);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
