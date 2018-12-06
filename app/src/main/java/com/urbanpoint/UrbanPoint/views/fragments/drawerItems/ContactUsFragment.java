package com.urbanpoint.UrbanPoint.views.fragments.drawerItems;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener, CustomDialogConfirmationInterfaces {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ImageView mSlideButton;
    private Utility utilObj;
    private Spinner mContactUsCategorySpinner;
    private Button mContactUsSubmit;
    private EditText mContactUsMessage;
    private LinearLayout mMainParentLayout;



    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initialize();
        return view;
    }


    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.contact_us));

        utilObj = new Utility(getActivity());
        setActionBar(getString(R.string.contact_us), true);

        bindViews();
    }

    private void bindViews() {
        mMainParentLayout = (LinearLayout) mRootView.findViewById(R.id.mainParentLayout);
        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                return false;
            }
        });

        mContactUsCategorySpinner = (Spinner) mRootView.findViewById(R.id.contactUsCategorySpinner);
        mContactUsMessage = (EditText) mRootView.findViewById(R.id.contactUsMessage);
        mContactUsSubmit = (Button) mRootView.findViewById(R.id.contactUsSubmit);
        mContactUsSubmit.setOnClickListener(this);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext,
                R.array.contact_us_reason_array, R.layout.contact_us_custom_spinner);

        adapter.setDropDownViewResource(R.layout.contact_us_custom_spinner);
        mContactUsCategorySpinner.setAdapter(adapter);
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.contactUsSubmit:
                String message = "";

                if (mContactUsCategorySpinner.getSelectedItemPosition() == 0) {
                    message = getResources().getString(R.string.select_reason);
                    //utilObj.showError(this, message, textViewObj, emailObj);
                    utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, this);
                } else if (mContactUsMessage.getText().toString().trim().length() == 0) {
                    message = getResources().getString(R.string.enter_into_comment_box);
                    //utilObj.showError(this, message, textViewObj, emailObj);
                    utilObj.showCustomAlertDialog(mActivity, null, message, null, null, false, this);
                } else {
                    String item = (String) mContactUsCategorySpinner.getSelectedItem();
                    String body = mContactUsMessage.getText().toString();
                   /* Intent mailer = new Intent(Intent.ACTION_SEND);
                    mailer.setType("plain/text");
                    mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.ContactUS.EMAIL});
                    mailer.putExtra(Intent.EXTRA_SUBJECT, item);
                    mailer.putExtra(Intent.EXTRA_TEXT, body);
                   *//* startActivity(Intent.createChooser(mailer, ""));*//*
                    startActivity(mailer);*/

                    // String URI="mailto:?subject=" + item + "&body=" + body;
                    String URI = "mailto:?subject=" + item + "&body=" + body + "&to=" + Constants.ContactUS.EMAIL;

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse(URI);
                    intent.setData(data);
                    startActivity(intent);

                    MyApplication.getInstance().trackEvent(getString(R.string.ga_event_category_contact_us), getString(R.string.ga_event_action_contact_us), getString(R.string.ga_event_label_contact_us));

                }

                break;

        }
    }

    @Override
    public void callConfirmationDialogPositive() {

    }

    @Override
    public void callConfirmationDialogNegative() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mContactUsMessage.setText("");
    }
}
