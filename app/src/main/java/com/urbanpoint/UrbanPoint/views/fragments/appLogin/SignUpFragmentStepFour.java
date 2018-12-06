package com.urbanpoint.UrbanPoint.views.fragments.appLogin;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Utility;

/**
 * THIS STEP OF REGISTRATION IS REMOVED, MOVED DIRECTLY TO FOUR STEP
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragmentStepFour extends Fragment implements View.OnClickListener {

    private Utility utilObj;

    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private Button mSignUpBackView;
    private Button mSignUpContinueView;

    private ImageView mFoodImageView;
    private ImageView mBeautyImageView;
    private ImageView mFunImageView;
    private ImageView mHealthImageView;

    private boolean mFoodImageSelectState = false;
    private boolean mBeautyImageSelectState = false;
    private boolean mFunImageSelectState = false;
    private boolean mHealthImageSelectState = false;

    private int mFoodValue, mBeautyValue, mFunValue, mHealthValue;

    public SignUpFragmentStepFour() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_step_four, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        initialize();

        return view;
    }

    private void initialize() {
        mFoodValue = mBeautyValue = mFunValue = mHealthValue = 0;
        utilObj = new Utility(mActivity);
        bindViews();
    }

    private void bindViews() {
        mSignUpBackView = (Button) mRootView.findViewById(R.id.signUpStepThreeBackButton);
        mSignUpBackView.setOnClickListener(this);
        mSignUpContinueView = (Button) mRootView.findViewById(R.id.signUpStepThreeContinueButton);
        mSignUpContinueView.setOnClickListener(this);

        mFoodImageView = (ImageView) mRootView.findViewById(R.id.iv_food);
        mBeautyImageView = (ImageView) mRootView.findViewById(R.id.iv_beauty);
        mFunImageView = (ImageView) mRootView.findViewById(R.id.iv_fun);
        mHealthImageView = (ImageView) mRootView.findViewById(R.id.iv_health);

        mFoodImageView.setOnClickListener(this);
        mBeautyImageView.setOnClickListener(this);
        mFunImageView.setOnClickListener(this);
        mHealthImageView.setOnClickListener(this);

        String categoryPreference = AppInstance.signUpUser.getCategoryPreference();

        if (categoryPreference != null) {

            if (categoryPreference.contains("" + Constants.IntentPreference.FOOD_ID)) {
                mFoodValue = Constants.IntentPreference.FOOD_ID;
                mFoodImageSelectState = true;
                mFoodImageView.setBackgroundResource(R.mipmap.food_icon_selected);
            }
            if (categoryPreference.contains("" + Constants.IntentPreference.BEAUTY_ID)) {
                mBeautyImageSelectState = true;
                mBeautyValue = Constants.IntentPreference.BEAUTY_ID;
                mBeautyImageView.setBackgroundResource(R.mipmap.beauty_spa_icon_selected);
            }
            if (categoryPreference.contains("" + Constants.IntentPreference.FUN_ID)) {
                mFunImageSelectState = true;

                mFunValue = Constants.IntentPreference.FUN_ID;
                mFunImageView.setBackgroundResource(R.mipmap.fun_icon_selected);
            }
            if (categoryPreference.contains("" + Constants.IntentPreference.RetailnServices_ID)) {
                mHealthImageSelectState = true;

                mHealthValue = Constants.IntentPreference.RetailnServices_ID;
                mHealthImageView.setBackgroundResource(R.mipmap.health_icon_selected);
            }
        }
    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.signUpStepThreeBackButton:
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
                fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepTwo());
                fragmentTransaction.commit();

                break;
            case R.id.signUpStepThreeContinueButton:
                if ((mFoodImageSelectState == false) && (mBeautyImageSelectState == false)
                        && (mFunImageSelectState == false) && (mHealthImageSelectState == false)) {
                    utilObj.showCustomAlertDialog(getActivity(), getString(R.string.header_interest), getString(R.string.select_at_least_one_category), null, null, false, null);
                } else {
                    String data = mFoodValue + "," + mBeautyValue + "," + mFunValue + "," + mHealthValue;
                    AppInstance.signUpUser.setCategoryPreference(data);
                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragmentTransaction.replace(R.id.containerIntroFragments, new SignUpFragmentStepFive());
                    fragmentTransaction.commit();

                }

                break;

            case R.id.iv_food:
                if (mFoodImageSelectState == false) {
                    mFoodImageSelectState = true;
                    mFoodValue = Constants.IntentPreference.FOOD_ID;
                    mFoodImageView.setBackgroundResource(R.mipmap.food_icon_selected);
                } else {
                    mFoodImageSelectState = false;
                    mFoodValue = 0;
                    mFoodImageView.setBackgroundResource(R.mipmap.food_icon);
                }
                break;

            case R.id.iv_beauty:


                if (mBeautyImageSelectState == false) {
                    mBeautyImageSelectState = true;
                    mBeautyValue = Constants.IntentPreference.BEAUTY_ID;

                    mBeautyImageView.setBackgroundResource(R.mipmap.beauty_spa_icon_selected);
                } else {
                    mBeautyValue = 0;
                    mBeautyImageSelectState = false;
                    mBeautyImageView.setBackgroundResource(R.mipmap.beauty_spa_icon);
                }
                break;


            case R.id.iv_fun:
                if (mFunImageSelectState == false) {
                    mFunImageSelectState = true;
                    mFunValue = Constants.IntentPreference.FUN_ID;

                    mFunImageView.setBackgroundResource(R.mipmap.fun_icon_selected);

                } else {
                    mFunValue = 0;
                    mFunImageSelectState = false;
                    mFunImageView.setBackgroundResource(R.mipmap.fun_icon);
                }
                break;
            case R.id.iv_health:
                if (mHealthImageSelectState == false) {
                    mHealthValue = Constants.IntentPreference.RetailnServices_ID;
                    mHealthImageSelectState = true;
                    mHealthImageView.setBackgroundResource(R.mipmap.health_icon_selected);
                } else {
                    mHealthValue = 0;
                    mHealthImageSelectState = false;
                    mHealthImageView.setBackgroundResource(R.mipmap.health_icon);
                }


                break;
        }
    }
}
