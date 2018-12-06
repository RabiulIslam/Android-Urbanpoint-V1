package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.ForgotPassword;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.appLogin.LoginManager;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.ResponseCodes;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

/**
 * Created by aparnasalve on 21/3/16.
 */
public class ForgotPasswordActivity extends Activity implements ServiceRedirection, View.OnClickListener, CustomDialogConfirmationInterfaces {
    private Context mContext;
    private Utility utilObj;
    private ImageView mBackButton;
    private EditText mUserEmail;
    private Button mForgotPassword;
    private LoginManager loginManagerObj;
    private boolean isMailSent;
    String emailID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        mContext = ForgotPasswordActivity.this;
        initViews();
        MyApplication.getInstance().trackScreenView(getResources().getString(R.string.forgot_pin_page));

    }

    private void initViews() {
        utilObj = new Utility(mContext);
        loginManagerObj = new LoginManager(mContext, this);
        setActionBar(getString(R.string.forgot_password_title), true);
        isMailSent = false;
        bindViews();
    }


    private void bindViews() {

        mUserEmail = (EditText) findViewById(R.id.userEmail);
        mForgotPassword = (Button) findViewById(R.id.forgotPasswordButton);
        mForgotPassword.setOnClickListener(this);
    }


    public void setActionBar(String title, boolean showNavButton) {
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSlideButton.toggle(true);
                finish();
            }
        });

        title1.setText(title);
        getActionBar().setCustomView(customView);
    }


    private boolean validatingRequired() {
        String message = "";
        String email = mUserEmail.getText().toString();

        //validate the content
        if (email.isEmpty()) {
            message = getResources().getString(R.string.EmailErrorMessage);
            utilObj.showCustomAlertDialog(ForgotPasswordActivity.this, null, message, null, null, false, this);
        } else if (!utilObj.checkEmail(email)) {
            message = getResources().getString(R.string.invalid_email);
            utilObj.showCustomAlertDialog(ForgotPasswordActivity.this, null, message, null, null, false, this);
        }

        if (message.equalsIgnoreCase("") || message == null) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.GET_FORGOT_PASSWORD_TASK_ID) {
            if (AppInstance.forgotPassword != null) {
                ForgotPassword forgotPassword = AppInstance.forgotPassword;
                if (ResponseCodes.STATUS_ZERO.equalsIgnoreCase(forgotPassword.getStatus())) {
                    utilObj.showCustomAlertDialog(ForgotPasswordActivity.this, null, getString(R.string.wrong_email_pin), null, null, false, this);
                } else {
                    isMailSent = true;
                    utilObj.showCustomAlertDialog(ForgotPasswordActivity.this, null, forgotPassword.getMessage(), null, null, false, this);
                }

            }
        }

    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showCustomAlertDialog(ForgotPasswordActivity.this, null, errorMessage, null, null, false, this);

    }

    @Override
    public void callConfirmationDialogPositive() {
        if (isMailSent) {
            finish();
        }else {
            mUserEmail.setText("");
        }
    }

    @Override
    public void callConfirmationDialogNegative() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.forgotPasswordButton:
                if (validatingRequired()) {

                    if (NetworkUtils.isConnected(mContext)) {
                        emailID = mUserEmail.getText().toString();

                        MyApplication.getInstance().trackEvent(getResources().getString(R.string.ga_event_category_login_screen_retrieve_pin), getResources().getString(R.string.ga_event_action_login_screen_retrieve_pin), getResources().getString(R.string.ga_event_label_login_screen_retrieve_pin));

                        utilObj.startiOSLoader(mContext, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                        loginManagerObj.doForgotPassword(emailID);

                    } else {
                        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }

                }
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }
}
