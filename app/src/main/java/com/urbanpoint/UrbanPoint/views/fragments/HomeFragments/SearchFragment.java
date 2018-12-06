package com.urbanpoint.UrbanPoint.views.fragments.HomeFragments;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.SearchFilterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity mActivity;
    private FragmentManager frgMngr;
    private Context mContext;
    private View mRootView;
    private Utility utilObj;
    private ImageView mBackButton;
    private Button mSearchButton;
    private EditText mSearchEditText;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
//        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initialize();


        return view;
    }


    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.contact_us));
        utilObj = new Utility(getActivity());
        setActionBar("Search", false);
        frgMngr = getFragmentManager();
        bindViews();
    }

    private void bindViews() {
        mSearchEditText = (EditText) mRootView.findViewById(R.id.searchEditText);
        mSearchButton = (Button) mRootView.findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(this);
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

//        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                frgMngr.popBackStack();
                return false;
            }
        });

        title1.setText(title);
        getActivity().getActionBar().setCustomView(customView);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.searchButton:
                if (mSearchEditText.getText().length() > 0) {
                    searchEnteredText();
                }else {
                    utilObj.showToast(mContext, mContext.getString(R.string.invalid_search_field), Toast.LENGTH_LONG);

                }
                break;
        }
    }

    private void searchEnteredText() {
        //Reset serach result from HOME screen.
        AppInstance.lookingForFilterList = null;

        String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
               /* String page = "1";
                String searchCount="0";*/
        String search_tag = mSearchEditText.getText().toString();

        Bundle m_bundle = new Bundle();
        m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
        m_bundle.putString(Constants.Request.SEARCH_TAG, search_tag);
        Intent intentnew = new Intent(getActivity(), SearchFilterActivity.class);
        intentnew.putExtras(m_bundle);
        startActivity(intentnew);
    }


}
