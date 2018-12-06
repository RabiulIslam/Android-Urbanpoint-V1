package com.urbanpoint.UrbanPoint.views.fragments.drawerItems;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.adapters.drawerItemAdapters.MyReviewsAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.drawerItem.MyReviewsList;
import com.urbanpoint.UrbanPoint.dataobject.drawerItem.MyReviewsListItem;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.drawerItems.MyReviewsManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.ResponseCodes;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import java.util.ArrayList;

/**
 * Created by aparnasalve on 16/3/16.
 */
public class MyReviewsFragment extends Fragment implements ServiceRedirection {

    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    public static ListView mMyReviewListView;
    private Utility utilObj;
    private MyReviewsManager mMyReviewManager;
    private TextView mNoMyReviewsFound;
    private ImageView mSlideButton;
    public static MyReviewsAdapter myReviewsAdapter;
    private CheckReviewStatus mCheckReviewStatus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reviews, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        initialize();
        return view;
    }

    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.my_reviews));

        utilObj = new Utility(mActivity);
        setActionBar(getString(R.string.my_reviews), true);
        mMyReviewManager = new MyReviewsManager(mContext, this);

        mCheckReviewStatus = new CheckReviewStatus();

        mActivity.registerReceiver(mCheckReviewStatus, new IntentFilter(
                Constants.DEFAULT_VALUES.NO_REVIEW_AVAIL));

        bindViews();

        doCallGetMyReviewList();
    }

    private void bindViews() {
        mMyReviewListView = (ListView) mRootView.findViewById(R.id.list);
        mNoMyReviewsFound = (TextView) mRootView.findViewById(R.id.noMyReviewsFound);

    }


    public void setActionBar(String title, boolean showNavButton) {
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_purchase_history, null);
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
    public void onResume() {
        super.onResume();

    }

    private void doCallGetMyReviewList() {
        //if (AppInstance.myReviewsList == null) {
        if (NetworkUtils.isConnected(mContext)) {

            utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.loading_reviews), true);
            //assigning the data to the user object
            mMyReviewManager.doGetReviewList();
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
        //}
        /*else {
            ArrayList<MyReviewsListItem> myReviewsListItemArrayList = AppInstance.myReviewsList.getMyReviewsListItemArrayList();
            if (myReviewsListItemArrayList != null) {
                myReviewsAdapter = new MyReviewsAdapter(mContext, R.layout.fragment_listview, AppInstance.myReviewsList.getMyReviewsListItemArrayList(), getActivity());
                mMyReviewListView.setAdapter(myReviewsAdapter);
                myReviewsAdapter.notifyDataSetChanged();
            }
        }*/
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.GET_MY_REVIEW_LIST_TASK_ID) {

            if (AppInstance.myReviewsList != null) {
                MyReviewsList myReviewsList = AppInstance.myReviewsList;
                if (ResponseCodes.STATUS_ZERO.equalsIgnoreCase(myReviewsList.getStatus())) {
                    // utilObj.showToast(mContext, purchaseHistory.getMessage(), Toast.LENGTH_LONG);
                    mNoMyReviewsFound.setVisibility(View.VISIBLE);
                    mMyReviewListView.setVisibility(View.GONE);
                    AppPreference.setSetting(mContext, Constants.Request.MY_REVIEWS_COUNT, "0");

                } else {
                    if (myReviewsList.getMyReviewsListItemArrayList() != null) {
                        if (myReviewsList.getMyReviewsListItemArrayList().size() == 0) {
                            mNoMyReviewsFound.setVisibility(View.VISIBLE);
                            mMyReviewListView.setVisibility(View.GONE);
                            AppPreference.setSetting(mContext, Constants.Request.MY_REVIEWS_COUNT, "0");

                        } else {
                            mMyReviewListView.setVisibility(View.VISIBLE);
                            mNoMyReviewsFound.setVisibility(View.GONE);
                            myReviewsAdapter = new MyReviewsAdapter(mContext, R.layout.fragment_listview, AppInstance.myReviewsList.getMyReviewsListItemArrayList(), getActivity());
                            mMyReviewListView.setAdapter(myReviewsAdapter);
                            myReviewsAdapter.notifyDataSetChanged();
                            AppPreference.setSetting(mContext, Constants.Request.MY_REVIEWS_COUNT, Integer.toString(myReviewsList.getMyReviewsListItemArrayList().size()));

                        }
                    } else {
                        mNoMyReviewsFound.setVisibility(View.VISIBLE);
                        mMyReviewListView.setVisibility(View.GONE);
                    }

                }
            } else {
                utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
            }


        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mActivity, errorMessage, Toast.LENGTH_LONG);
        mNoMyReviewsFound.setVisibility(View.VISIBLE);
        mMyReviewListView.setVisibility(View.GONE);
    }

    private class CheckReviewStatus extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Constants.DEFAULT_VALUES.NO_REVIEW_AVAIL)) {
                mNoMyReviewsFound.setVisibility(View.VISIBLE);
                mMyReviewListView.setVisibility(View.GONE);
            }
        }
    }
}
