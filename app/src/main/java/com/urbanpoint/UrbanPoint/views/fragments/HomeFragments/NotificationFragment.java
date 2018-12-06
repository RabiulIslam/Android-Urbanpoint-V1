package com.urbanpoint.UrbanPoint.views.fragments.HomeFragments;


import android.app.ActionBar;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.adapters.homeAdapter.notificationAdapter.CustomNotificationAdapter;
import com.urbanpoint.UrbanPoint.adapters.profileAdapter.SpinnerAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.notification.NotificationList;
import com.urbanpoint.UrbanPoint.managers.Home_WebHit_Get_Notifications;
import com.urbanpoint.UrbanPoint.managers.Home_WebHit_Get_Notifications_Read;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.RoundedImageView;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.SplashScreenActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ImageView mSlideButton;
    private Utility utilObj;
    private RoundedImageView rmvImg;
    private Spinner spnGender;
    private SpinnerAdapter spinnerAdapter;
    private ArrayList<NotificationList> lstNotification;
    private EditText edtName, edtEmail, edtNetwork, edtNationality;
    private ListView lsvNotification;
    private CustomNotificationAdapter customNotificationAdapter;
    private Bundle mBundle;
    private TextView txvHeading, txvNoNotification;
    private Typeface novaThin, novaRegular;
    private ImageView mBackButton;
    private FragmentManager frgMngr;
    private int mSelctedPosition;
    private String date2;
    private boolean isReadNotifctnCounterChanged;
    private int currentPage, pageCount;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initialize();
        int idis = SplashScreenActivity.notificationId;
        if (NetworkUtils.isConnected(mContext)) {
            if (SplashScreenActivity.notificationId > 0) {
//                utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                String strDate;
                date2 = "1 June 2014";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat format2 = new SimpleDateFormat("d MMM yyyy");

                strDate = SplashScreenActivity.notificationDate;
                String[] split = strDate.split(" ");
                try {
                    Date date = format.parse(split[0]);
                    date2 = format2.format(date);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                requestNotificationsRead(SplashScreenActivity.notificationId + "");
            } else {
                utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                requestNotifications(1);
            }
        } else {
            SplashScreenActivity.notificationId = Constants.DEFAULT_VALUES.UNDEFINED_PUSH_NOTIFICATION;
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }

        lsvNotification.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (currentPage < pageCount) {
                    currentPage++;
                    requestNotifications(currentPage);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lsvNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelctedPosition = position;
                Log.d("afwq", "onItemClick: " + position);
                Log.d("afwq", "onItemClick:b " + lstNotification.get(position).isRead());
                Log.d("afwq", "onItemClick:a " + lstNotification.get(position).isRead());
                utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                if (lstNotification.get(position).isRead()) {
                    isReadNotifctnCounterChanged = false;
                } else {
                    isReadNotifctnCounterChanged = true;
                }
                requestNotificationsRead(lstNotification.get(position).getNotificationId());
            }
        });

        return view;
    }


    private void requestNotifications(int _pageCount) {
        Home_WebHit_Get_Notifications home_webHit_get_notifications = new Home_WebHit_Get_Notifications();
        home_webHit_get_notifications.getNotification(mContext, this, _pageCount);
    }

    private void requestNotificationsRead(String _notificationId) {
        Home_WebHit_Get_Notifications_Read home_webHit_get_notifications_read = new Home_WebHit_Get_Notifications_Read();
        home_webHit_get_notifications_read.getNotificationRead(mContext, this, _notificationId);
    }

    private void initialize() {
        novaThin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        novaRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        MyApplication.getInstance().trackScreenView(getString(R.string.my_notification));
        frgMngr = getFragmentManager();
        mSelctedPosition = -1;
        currentPage = 1;
        pageCount = 1;
        isReadNotifctnCounterChanged = false;
        mBundle = new Bundle();
        lstNotification = new ArrayList<NotificationList>();
        utilObj = new Utility(getActivity());
        setActionBar(getResources().getString(R.string.my_notification), false);
        bindViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("sadsdewqe", "onResume: ");
    }

    private void bindViews() {
        txvHeading = (TextView) mRootView.findViewById(R.id.frg_notification_heading);
        txvNoNotification = (TextView) mRootView.findViewById(R.id.frg_notification_txv_no_notification);
        txvHeading.setTypeface(novaThin);
        lsvNotification = (ListView) mRootView.findViewById(R.id.frg_notification_list_view);

    }

    public void setActionBar(String title, boolean showNavButton) {
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

//        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
        Animation animation = AnimationUtils.loadAnimation(mContext,
                R.anim.right_in);

        if (lstNotification != null) {
        } else {
            customView.startAnimation(animation);

        }

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
                frgMngr.popBackStack();
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


        }
    }


    public void showResult(boolean b, String statusText) {
        utilObj.stopiOSLoader();
        if (b) {
            if (Home_WebHit_Get_Notifications.responseModel != null &&
                    Home_WebHit_Get_Notifications.responseModel.getData() != null &&
                    Home_WebHit_Get_Notifications.responseModel.getData().getItems() != null &&
                    Home_WebHit_Get_Notifications.responseModel.getData().getItems().size() > 0) {
                lsvNotification.setVisibility(View.VISIBLE);
                txvNoNotification.setVisibility(View.GONE);
                updateNotificationList();
            } else {
                lsvNotification.setVisibility(View.GONE);
                txvNoNotification.setVisibility(View.VISIBLE);
            }
            if (Home_WebHit_Get_Notifications.responseModel.getData().getPagination() != null) {
                currentPage = Home_WebHit_Get_Notifications.responseModel.getData().getPagination().getCurrentPage();
                pageCount = Home_WebHit_Get_Notifications.responseModel.getData().getPagination().getPageCount();
            }
        } else {
            utilObj.showToast(mContext, statusText, Toast.LENGTH_LONG);

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


    private void updateNotificationList() {

        boolean isRead = false;
        String strDate;
        String date2 = "1 June 2014";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("d MMM yyyy");
        for (int i = 0; i < Home_WebHit_Get_Notifications.responseModel.getData().getItems().size(); i++) {
            if (Home_WebHit_Get_Notifications.responseModel.getData().getItems().get(i).getReaded().equals("1")) {
                isRead = true;
            } else {
                isRead = false;
            }
            strDate = Home_WebHit_Get_Notifications.responseModel.getData().getItems().get(i).getCreatedAt();
            String[] split = strDate.split(" ");
            try {
                Date date = format.parse(split[0]);
                date2 = format2.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            lstNotification.add(new NotificationList(Home_WebHit_Get_Notifications.responseModel.getData().getItems().get(i).getId(),
                    "",
                    Home_WebHit_Get_Notifications.responseModel.getData().getItems().get(i).getMessage(),
                    Home_WebHit_Get_Notifications.responseModel.getData().getItems().get(i).getTitle(),
                    date2,
                    isRead));
            customNotificationAdapter = new CustomNotificationAdapter(mContext, this, lstNotification);
            lsvNotification.setAdapter(customNotificationAdapter);
        }
    }

    public void showReadNotificationResult(boolean b, String statusText) {
        utilObj.stopiOSLoader();
        if (b) {

            if (SplashScreenActivity.notificationId > 0) {
                mBundle.putString(Constants.Request.NOTIFICATION_DATE, date2);
                mBundle.putString(Constants.Request.NOTIFICATION_TEXT_1, SplashScreenActivity.notificationMsg);
                mBundle.putString(Constants.Request.NOTIFICATION_TEXT_2, SplashScreenActivity.notificationtitle);
                mBundle.putString(Constants.Request.NOTIFICATION_TEXT_3, "test2");

            } else {
                lstNotification.set(mSelctedPosition, new NotificationList(lstNotification.get(mSelctedPosition).getNotificationId(),
                        "Opera Cafe",
                        lstNotification.get(mSelctedPosition).getMerchantMessage(),
                        lstNotification.get(mSelctedPosition).getMerchantDetail(),
                        lstNotification.get(mSelctedPosition).getDate(),
                        true));
                customNotificationAdapter.setSelectedPosition(mSelctedPosition);
                customNotificationAdapter.notifyDataSetChanged();

                mBundle.putString(Constants.Request.NOTIFICATION_DATE, lstNotification.get(mSelctedPosition).getDate());
                mBundle.putString(Constants.Request.NOTIFICATION_TEXT_1, lstNotification.get(mSelctedPosition).getMerchantMessage());
                mBundle.putString(Constants.Request.NOTIFICATION_TEXT_2, lstNotification.get(mSelctedPosition).getMerchantDetail());
                mBundle.putString(Constants.Request.NOTIFICATION_TEXT_3, lstNotification.get(mSelctedPosition).getMerchantMessage());
            }
            if (isReadNotifctnCounterChanged) {
                AppInstance.rModel_checkSubscription.setUn_readed_notifications_count(
                        AppInstance.rModel_checkSubscription.getUn_readed_notifications_count() - 1);
                Log.d("asdasdwe", "one less");
            } else {
                Log.d("asdasdwe", "not less");

            }

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment frg = new NotificationDetailFragment();
            frg.setArguments(mBundle);
            ft.replace(R.id.content_frame, frg);
            ft.addToBackStack(null);
            ft.commit();

        } else {
            utilObj.showToast(mContext, statusText, Toast.LENGTH_LONG);

        }
    }
}
