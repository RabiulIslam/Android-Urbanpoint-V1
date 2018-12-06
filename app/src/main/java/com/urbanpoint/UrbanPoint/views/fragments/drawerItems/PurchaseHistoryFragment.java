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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.dataobject.PurchaseHistoryDetails;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.drawerItems.PurchaseHistoryManager;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.ResponseCodes;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.commonsware.cwac.merge.MergeAdapter;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.adapters.drawerItemAdapters.PurchaseHistoryDateWiseAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.PurchaseHistory;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseHistoryFragment extends Fragment implements ServiceRedirection {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ImageView mSlideButton;
    private Utility utilObj;
    private ListView mPurchaseHistoryListView;
    private PurchaseHistoryManager mPurchaseHistoryManager;
    private MergeAdapter mMergedAdapter;

    private LayoutInflater mInflater;
    private TextView mNoPurchaseHistoryFound;

    public PurchaseHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_history, null);

        mInflater = inflater;

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        initialize();
        return mRootView;
    }


    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.purchase_history));

        setActionBar(getString(R.string.purchase_history), true);

        utilObj = new Utility(getActivity());
        mPurchaseHistoryManager = new PurchaseHistoryManager(mContext, this);
        bindViews();

        if (NetworkUtils.isConnected(mContext)) {
            utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
            mPurchaseHistoryManager.doGetPurchaseHistory();
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }

    private void bindViews() {
        mNoPurchaseHistoryFound = (TextView) mRootView.findViewById(R.id.noPurchaseHistoryFound);

        mPurchaseHistoryListView = (ListView) mRootView.findViewById(R.id.purchaseHistoryListView);
        mPurchaseHistoryListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
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
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.GET_PURCHASE_HISTORY_TASK_ID) {

            if (AppInstance.purchaseHistory != null) {
                PurchaseHistory purchaseHistory = AppInstance.purchaseHistory;
                if (purchaseHistory.getStatus().equalsIgnoreCase(ResponseCodes.STATUS_ZERO)) {
                    // utilObj.showToast(mContext, purchaseHistory.getMessage(), Toast.LENGTH_LONG);
                    mNoPurchaseHistoryFound.setVisibility(View.VISIBLE);
                    mPurchaseHistoryListView.setVisibility(View.GONE);
                } else {
                    if (purchaseHistory.getPurchaseHistoryDetails().size() == 0) {
                        mNoPurchaseHistoryFound.setVisibility(View.VISIBLE);
                        mPurchaseHistoryListView.setVisibility(View.GONE);
                    } else {
                        mPurchaseHistoryListView.setVisibility(View.VISIBLE);
                        mNoPurchaseHistoryFound.setVisibility(View.GONE);
                        createAdapterViews(purchaseHistory);
                    }
                }
            }
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mActivity, errorMessage, Toast.LENGTH_LONG);
    }

    private void createAdapterViews(PurchaseHistory purchaseHistory) {
        mMergedAdapter = new MergeAdapter();
        HashSet<String> groupWiseDate = purchaseHistory.getGroupWiseDate();
        Resources resources = getResources();
        // create an iterator

        List<String> sorteddate = sortDate(groupWiseDate);


        Iterator iterator = sorteddate.iterator();
        // check values
        while (iterator.hasNext()) {

            View mInflateHeader = mInflater.inflate(R.layout.item_purchase_history_header, null);
            View mInflateFooter = mInflater.inflate(R.layout.item_purchase_history_footer, null);

            TextView mTextViewHeader = (TextView) mInflateHeader.findViewById(R.id.header);
            TextView mTextViewFooter = (TextView) mInflateFooter.findViewById(R.id.totalPoints);

            String data = "" + iterator.next();

            mTextViewHeader.setText(data);
            mMergedAdapter.addView(mInflateHeader, true);

            ArrayList<PurchaseHistoryDetails> purchaseHistoryList = purchaseHistory.getPurchaseHistoryList(data);
            double total = 0.0;
            for (PurchaseHistoryDetails details :
                    purchaseHistoryList) {

                //---------- Now show total of approximate savings instead of  "Purchase_price"
                // total = total + Double.parseDouble(details.getPurchasePrice());
                total = total + Double.parseDouble(details.getApproximateSavings());
            }

            PurchaseHistoryDateWiseAdapter dateWiseAdapter = new PurchaseHistoryDateWiseAdapter(mContext, R.layout.item_purchase_history, purchaseHistoryList);
            mMergedAdapter.addAdapter(dateWiseAdapter);

            BigDecimal bd = new BigDecimal(total);
            BigDecimal res = bd.setScale(0, RoundingMode.DOWN);

            String totalValueToPrint = String.format(resources.getString(R.string.total_saving), "" + res.toPlainString());

            mTextViewFooter.setText(totalValueToPrint);
            mMergedAdapter.addView(mInflateFooter, true);

        }

        mPurchaseHistoryListView.setAdapter(mMergedAdapter);
    }


    private List<String> sortDate(HashSet<String> groupwisedate){

        List<Date> xyz = new ArrayList<Date>();
        List<String> sorteddate = new ArrayList<>();
        try {

            for (String abc1 : groupwisedate) {

                Date date;

                date = new SimpleDateFormat(Constants.DEFAULT_VALUES.DATE_FORMAT, Locale.ENGLISH)
                        .parse(abc1);
                xyz.add(date);

            }

            Collections.sort(xyz, new Comparator<Date>() {

                public int compare(Date arg0, Date arg1) {
                    // return arg0.getDate().compareTo(o2.getDate());
                    return arg0.compareTo(arg1);
                }
            });

            for (Date date1 : xyz) {
                System.out.println("Sorted : " +  new SimpleDateFormat(Constants.DEFAULT_VALUES.DATE_FORMAT, Locale.ENGLISH).format(date1));
                sorteddate.add(new SimpleDateFormat(Constants.DEFAULT_VALUES.DATE_FORMAT, Locale.ENGLISH).format(date1));
            }

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return sorteddate;

    }

}
