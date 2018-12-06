package com.urbanpoint.UrbanPoint.views.fragments.main;


import android.Manifest;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urbanpoint.UrbanPoint.adapters.homeAdapter.HorizontalGridViewAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_HomeOffers;
import com.urbanpoint.UrbanPoint.dataobject.main.DModelHomeGrdVw;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.HomeManager;
import com.urbanpoint.UrbanPoint.managers.IntroActivityManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.DashboardActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.SplashScreenActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.views.fragments.HomeFragments.FavoritesFragment;
import com.urbanpoint.UrbanPoint.views.fragments.HomeFragments.NewOffersFragment;
import com.urbanpoint.UrbanPoint.views.fragments.HomeFragments.NotificationFragment;
import com.urbanpoint.UrbanPoint.views.fragments.HomeFragments.VerifyMemberFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, ServiceRedirection {


    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ImageView mSlideButton, imvFestival, mSearchButton;
    private Utility utilObj;
    private Button btnGainAccess;
    private RelativeLayout mSlidingMenuButtonLayout;
    private LinearLayout llIstOffer, llGainAcess;
    private TextView txvOfferDays, txvRedIconBtn, txvHomeScreenMsg, txvMemberMsg, txvBubbleCount,
            txvFood, txvBeauty, txvFun, txvHotel;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private HorizontalScrollView mHorizontalScrollView1, mHorizontalScrollView2;
    private RelativeLayout mSlideRedIconButton;
    private UpdateSubscribeOfferStatus mUpdateSubscribeOfferStatus;
    private Handler retryDoCallCheckSubscribeServiceHandler;
    private Typeface novaThin, novaRegular;
    public boolean isBroadReceiverRegistered;
    private CustomDialogConfirmationInterfaces appUpdateDialogConfirmationInterfacesLocation;


    //Bottom Tab Items
    private final byte NUM_BOTTOM_TABS = 3;

    private final byte BOTTOM_TAB_FAVORITES = 0;
    private final byte BOTTOM_TAB_NOTIFICATIONS = 1;
    private final byte BOTTOM_TAB_NEW_OFFERS = 2;

    LinearLayout[] llBottomTab = new LinearLayout[NUM_BOTTOM_TABS];
    ImageView[] imvBottomTab = new ImageView[NUM_BOTTOM_TABS * 2];
    TextView[] txvBottomTab = new TextView[NUM_BOTTOM_TABS];
    HorizontalGridViewAdapter horizontalGridViewAdapter1, horizontalGridViewAdapter2;
    List<DModelHomeGrdVw> lstGrids1, lstGrids2;
    GridView grdvw1, grdvw2;
    ImageView imvNotification, imvNewOffer;
    RelativeLayout rlGrd1Next, rlGrd2Next, imvSearch, rlNotifctnBubble, rlMembermsg, rlProgress1, rlProgress2;
    int grdvwWidth, grdvwWidth2, grdvwItemWidth, increment1, increment2;
    LinearLayout llFoodnDrinks, llBeautynSpa, llFunActivities, llHotelRooms;
    HomeManager homeManager;
    IntroActivityManager introActivityManager;
    boolean isNetworkConnected = false;
    boolean isAppUpdateDialogueDisplaying;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.home_screen, null);

        //------------------
        OurOnCreate(view);
        String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");

        Log.d("OldDataIs", "onCreate: " + custID);

        return view;

    }

    private void OurOnCreate(View view) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//------------------
        this.mRootView = view;

        initialize();
        setBottomTabs();


//        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);

        grdvw1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInstance.rModel_merchintList = null;
                AppInstance.wishListList = null;
                AppInstance.foodOfferDeatils = null;
                String custID;
                String productid;
                String itemName;
                custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                productid = lstGrids1.get(position).getStrProductId();
                itemName = lstGrids1.get(position).getStrOfferName();
                ((DashboardActivity) getActivity()).navToDetail(custID, productid, itemName);

            }
        });
        grdvw2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInstance.rModel_merchintList = null;
                AppInstance.wishListList = null;
                AppInstance.foodOfferDeatils = null;
                String custID;
                String productid;
                String itemName;
                custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                productid = lstGrids2.get(position).getStrProductId();
                itemName = lstGrids2.get(position).getStrOfferName();
                ((DashboardActivity) getActivity()).navToDetail(custID, productid, itemName);
            }
        });


//        Log.d("Difference", "Prefrnce: " + AppPreference.getSettingResturnsLong(getContext(), "key_Promotional_days", 0));
//        AppPreference.setSetting(getContext(), "key_Promotion_acessed", "availed");
//        String strPermision = AppPreference.getSetting(getContext(), "key_Promotion_acessed", "");
//        Log.d("Difference", "acessed is: " + AppPreference.getSetting(getContext(), "key_Promotion_acessed", ""));
//        long timeP = AppPreference.getSettingResturnsLong(getContext(), "key_Promotional_days", 0);
//        long diff = timeP - System.currentTimeMillis();
//        Log.d("Difference", "Milli: " + timeP);
//        Date date = new Date(timeP);
//        Log.d("Difference", "Date " + date);
//
//        Log.d("DOOOOV", "Age " + AppPreference.getSetting(mContext, Constants.Request.AGE, ""));
//        Log.d("DOOOOV", "DOB " + AppPreference.getSetting(mContext, Constants.Request.DOB, ""));
//
//
//        if (diff > 0 && strPermision.length() == 0) {
//            remDays = getRemaingPromotionalTime(diff);
//            llIstOffer.setVisibility(View.VISIBLE);
//            txvOfferDays.setText(remDays + " Days left");
//        } else {
//            AppPreference.setSetting(getContext(), "key_Promotion_acessed", "Availled");
//        }
        MyApplication.getInstance().trackScreenView(getString(R.string.ga_home_screen));
//        Log.i("myDate", getCustomDateString("2 January 2010"));


//        new TestAsync().execute();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


//    class TestAsync extends AsyncTask<Void, Integer, String>
//    {
//        String TAG = "asdsdsaDEEEE";
//
//        protected void onPreExecute (){
//            super.onPreExecute();
//            Log.d(TAG,"On pre Exceute......");
//        }
//
//        protected String doInBackground(Void...arg0) {
//            Log.d(TAG ,"On doInBackground...");
//
//            for(int i=0; i<10; i++){
//                Integer in = new Integer(i);
//                publishProgress(i);
//            }
//            return "You are at PostExecute";
//        }
//
//        protected void onProgressUpdate(Integer...a){
//            super.onProgressUpdate(a);
//            Log.d(TAG , "You are in progress update ... " + a[0]);
//        }
//
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.d(TAG , "" + result);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    new TestAsync().execute();
//                }
//            },1000);
//        }
//    }

    private void updateGridViewWidth1() {
        ViewGroup.LayoutParams params = grdvw1.getLayoutParams();
        Log.d("Heght", "orignalGridViewWidth1: " + params.width);
        double width = lstGrids1.size() * 160;
        int wi = (int) width;
        params.width = (dpToPx(wi));
        Log.d("Heght", "updateGridViewWidth1: " + params.width);
        grdvwItemWidth = (dpToPx(158));
        grdvwWidth = params.width;
        Log.d("Heght", "updateGridItemWidth1: " + grdvwItemWidth);
        grdvw1.setLayoutParams(params);
        grdvw1.requestLayout();
    }

    private void updateGridViewWidth2() {
        ViewGroup.LayoutParams params = grdvw2.getLayoutParams();
        Log.d("Heght", "orignalGridViewWidth2: " + params.width);
        double width = lstGrids2.size() * 160;
        int wi = (int) width;
        params.width = (dpToPx(wi));
        Log.d("Heght", "updateGridViewWidth1: " + grdvwItemWidth);
        grdvwWidth2 = params.width;
        grdvw2.setLayoutParams(params);
        grdvw2.requestLayout();
    }


    private void setBottomTabs() {
        llBottomTab[BOTTOM_TAB_FAVORITES] = (LinearLayout) mRootView.findViewById(R.id.tab_ll_favorites);
        llBottomTab[BOTTOM_TAB_NOTIFICATIONS] = (LinearLayout) mRootView.findViewById(R.id.tab_ll_notifications);
        llBottomTab[BOTTOM_TAB_NEW_OFFERS] = (LinearLayout) mRootView.findViewById(R.id.tab_ll_offers);

        imvBottomTab[BOTTOM_TAB_FAVORITES] = (ImageView) mRootView.findViewById(R.id.tab_img_favorites);
        imvBottomTab[BOTTOM_TAB_NOTIFICATIONS] = (ImageView) mRootView.findViewById(R.id.tab_img_notifications);
        imvBottomTab[BOTTOM_TAB_NEW_OFFERS] = (ImageView) mRootView.findViewById(R.id.tab_img_offers);

        txvBottomTab[BOTTOM_TAB_FAVORITES] = (TextView) mRootView.findViewById(R.id.tab_txv_favorites);
        txvBottomTab[BOTTOM_TAB_NOTIFICATIONS] = (TextView) mRootView.findViewById(R.id.tab_txv_notifications);
        txvBottomTab[BOTTOM_TAB_NEW_OFFERS] = (TextView) mRootView.findViewById(R.id.tab_txv_offers);

        txvBottomTab[BOTTOM_TAB_FAVORITES].setTypeface(novaRegular);
        txvBottomTab[BOTTOM_TAB_NOTIFICATIONS].setTypeface(novaRegular);
        txvBottomTab[BOTTOM_TAB_NEW_OFFERS].setTypeface(novaRegular);


        llBottomTab[BOTTOM_TAB_FAVORITES].setOnClickListener(this);
        llBottomTab[BOTTOM_TAB_NOTIFICATIONS].setOnClickListener(this);
        llBottomTab[BOTTOM_TAB_NEW_OFFERS].setOnClickListener(this);
    }

    public void setActionBar(final String title, boolean showNavButton) {
        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_home, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mSlideButton = (ImageView) customView.findViewById(R.id.slidingMenuButton);
        mSlidingMenuButtonLayout = (RelativeLayout) customView.findViewById(R.id.slidingMenuButtonLayout);

        mSlidingMenuButtonLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //mSlideButton.toggle(true);
                View view = getActivity().getCurrentFocus();
                utilObj.keyboardClose(mContext, view);
                ((BaseActivity) getActivity()).getSlidingMenu().toggle();

            }
        });


        //------
        mSlideRedIconButton = (RelativeLayout) customView.findViewById(R.id.slidingMenuRedIconButton);
        txvRedIconBtn = (TextView) customView.findViewById(R.id.slidingMenuRedIconButtontext);
        imvSearch = (RelativeLayout) customView.findViewById(R.id.searchButton);
        imvSearch.setOnClickListener(this);
        mSlideRedIconButton.setVisibility(View.GONE);

        //-----


        title1.setText(title);
        if (showNavButton) {
            mSlideButton.setVisibility(View.VISIBLE);
            Log.d("PROFF", "OPEN");

            ((BaseActivity) getActivity()).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            //---------
            // mSlideButton.setVisibility(View.GONE);
            mSlideButton.setVisibility(View.VISIBLE);
            //-------------
            Log.d("PROFF", "CLOSE");

            ((BaseActivity) getActivity()).getSlidingMenu().setTouchModeAbove(
                    SlidingMenu.TOUCHMODE_NONE);
        }
        getActivity().getActionBar().setCustomView(customView);
    }

    private void initialize() {
        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();

        utilObj = new Utility(this.mActivity);
        setActionBar("", false);

        homeManager = new HomeManager(mContext, this);
        isBroadReceiverRegistered = false;
        isAppUpdateDialogueDisplaying = false;
        introActivityManager = new IntroActivityManager(mContext, this);
        this.novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        this.novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");


        //---------
        mUpdateSubscribeOfferStatus = new UpdateSubscribeOfferStatus();
        mActivity.registerReceiver(mUpdateSubscribeOfferStatus, new IntentFilter(
                Constants.DEFAULT_VALUES.UPDATE_USER_SUBSCRIBE_STATUS));

        retryDoCallCheckSubscribeServiceHandler = new Handler();
        increment1 = 0;
        increment2 = 0;
        bindViews();

        btnGainAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendNotification(3, getString(R.string.app_name_with_space), "Wellcome", "2016-07-22 11:02:27");
                if (AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD, "").length() > 0) {
                    ((DashboardActivity) getActivity()).navToSubscribeActivity();
                } else {
                    ((DashboardActivity) getActivity()).navToPaymentProcessVodafoneSelectionActivity();
                }
            }
        });

    }

//    private void sendNotification(int _ntfcType, String _title, String _message, String _date) {
//
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClass(getActivity(), SplashScreenActivity.class);
//        intent.putExtra("p_notification_id", _ntfcType);
//        intent.putExtra("p_notification_date", _date);
//        intent.putExtra("p_notification_msg", _message);
//        PendingIntent notifyPIntent = PendingIntent.getActivity(getActivity(), 0,
//                intent, PendingIntent.FLAG_ONE_SHOT);//
//
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity())
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle(_title)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(_message))
//                .setContentText(_message)
//                .setSound(defaultSoundUri)
//                .setContentIntent(notifyPIntent)
//                .setAutoCancel(true);
//
//
//        NotificationManager notificationManager =
//                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//        Random random = new Random();
//        notificationManager.notify(random.nextInt(),
//                notificationBuilder.build());
//    }


    private void bindViews() {

        txvFood = (TextView) mRootView.findViewById(R.id.frg_home_txv_food_and_drink);
        txvBeauty = (TextView) mRootView.findViewById(R.id.frg_home_txv_beauty_and_spa);
        txvFun = (TextView) mRootView.findViewById(R.id.frg_home_txv_fun);
        txvHotel = (TextView) mRootView.findViewById(R.id.frg_home_txv_hotel);

        txvFood.setTypeface(novaRegular);
        txvBeauty.setTypeface(novaRegular);
        txvFun.setTypeface(novaRegular);
        txvHotel.setTypeface(novaRegular);

        txvBubbleCount = (TextView) mRootView.findViewById(R.id.frg_home_txv_notification_bubble);
        txvHomeScreenMsg = (TextView) mRootView.findViewById(R.id.frg_home_screen_msg);
        txvMemberMsg = (TextView) mRootView.findViewById(R.id.frg_home_txv_member_msg);
        llIstOffer = (LinearLayout) mRootView.findViewById(R.id.frg_home_ll_ist_offer);
        llGainAcess = (LinearLayout) mRootView.findViewById(R.id.mainLayoutGainAcess);

        txvHomeScreenMsg.setTypeface(novaRegular);
        txvMemberMsg.setTypeface(novaRegular);
//        llIstOffer.setOnClickListener(this);
//
        imvFestival =  mRootView.findViewById(R.id.frg_home_imv_festival);
        rlMembermsg =  mRootView.findViewById(R.id.frg_home_rl_member_msg);
        rlProgress1 =  mRootView.findViewById(R.id.frg_home_rl_progrssbar_1);
        rlProgress2 =  mRootView.findViewById(R.id.frg_home_rl_progrssbar_2);
        llFoodnDrinks =  mRootView.findViewById(R.id.frg_home_ll_food_and_drinks);
        llBeautynSpa =  mRootView.findViewById(R.id.frg_home_ll_beauty_and_fitness);
        llFunActivities =  mRootView.findViewById(R.id.frg_home_ll_fun_activities);
        llHotelRooms =  mRootView.findViewById(R.id.frg_home_ll_hotel_rooms);

        rlMembermsg.setOnClickListener(this);
        rlMembermsg.setVisibility(View.GONE);
        llFoodnDrinks.setOnClickListener(this);
        llBeautynSpa.setOnClickListener(this);
        llFunActivities.setOnClickListener(this);
        llHotelRooms.setOnClickListener(this);

        imvNewOffer = (ImageView) mRootView.findViewById(R.id.tab_imv_offers);
        imvNotification = (ImageView) mRootView.findViewById(R.id.tab_imv_round_notification);
        rlNotifctnBubble = (RelativeLayout) mRootView.findViewById(R.id.frg_home_rl_notificatn_bubble);
        mHorizontalScrollView1 = (HorizontalScrollView) mRootView.findViewById(R.id.frg_home_horizontal_scroll_view);
        mHorizontalScrollView2 = (HorizontalScrollView) mRootView.findViewById(R.id.frg_home_horizontal_scroll_view_2);
        rlGrd1Next = (RelativeLayout) mRootView.findViewById(R.id.frg_home_grd_1_next);
        rlGrd1Next.setOnClickListener(this);
        rlGrd2Next = (RelativeLayout) mRootView.findViewById(R.id.frg_home_grd_2_next);
        rlGrd2Next.setOnClickListener(this);
        grdvw1 = (GridView) mRootView.findViewById(R.id.frg_home_lsv_1);
        grdvw2 = (GridView) mRootView.findViewById(R.id.frg_home_lsv_2);
        txvOfferDays = (TextView) mRootView.findViewById(R.id.frg_home_txv_offer_days);
        btnGainAccess = (Button) mRootView.findViewById(R.id.unlockOffers);
        btnGainAccess.setVisibility(View.GONE);

        appUpdateDialogConfirmationInterfacesLocation = new CustomDialogConfirmationInterfaces() {
            @Override
            public void callConfirmationDialogPositive() {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.urbanpoint.UrbanPoint"));
                startActivity(i);
                isAppUpdateDialogueDisplaying = false;
            }

            @Override
            public void callConfirmationDialogNegative() {
                isAppUpdateDialogueDisplaying = false;
            }
        };
    }

    @Override
    public void onClick(View v) {
        AppInstance.offers = null;

        AppInstance.rModel_merchintList = null;
        AppInstance.wishListList = null;
        AppInstance.foodOfferDeatils = null;

        //  mActivity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        Intent intent;
        switch (v.getId()) {
            case R.id.frg_home_ll_food_and_drinks:
//                intent = new Intent(getActivity(), FoodAndDrinkMainActivity.class);
//                startActivity(intent);
                ((DashboardActivity) getActivity()).navToFood();
                break;

            case R.id.frg_home_ll_beauty_and_fitness:
//                intent = new Intent(getActivity(), BeautyAndSpaMainActivity.class);
//                startActivity(intent);
                ((DashboardActivity) getActivity()).navToBeauty();
                break;

            case R.id.frg_home_ll_fun_activities:
//                intent = new Intent(getActivity(), FunActivitiesMainActivity.class);
//                startActivityForResult(intent, 16)
                ((DashboardActivity) getActivity()).navToFun();
                break;

            case R.id.frg_home_ll_hotel_rooms:
//                intent = new Intent(getActivity(), RetailAndServicesMainActivity.class);
//                startActivity(intent);
                ((DashboardActivity) getActivity()).navToRetail();
                break;


            case R.id.searchButton:
//                Intent intentnew = new Intent(getActivity(), SearchFilterActivity.class);
//                startActivity(intentnew);
                ((DashboardActivity) getActivity()).navToSearch();
                Log.d("Heght", "onClickSearch: ");
                break;

            case R.id.frg_home_grd_1_next:
                if (grdvwWidth - (2.5 * grdvwItemWidth) > increment1) {
                    increment1 = increment1 + grdvwItemWidth;
                    mHorizontalScrollView1.smoothScrollTo(increment1, 0);
                } else {
                    increment1 = 0;
                    mHorizontalScrollView1.smoothScrollTo(increment1, 0);
                }
                break;

            case R.id.frg_home_grd_2_next:
                if (grdvwWidth2 - (2.5 * grdvwItemWidth) > increment2) {
                    increment2 = increment2 + grdvwItemWidth;
                    mHorizontalScrollView2.smoothScrollTo(increment2, 0);
                } else {
                    increment2 = 0;
                    mHorizontalScrollView2.smoothScrollTo(increment2, 0);
                }
                break;

            case R.id.frg_home_rl_member_msg:
                navToVerifyMemberFragment();
                break;
            case R.id.tab_ll_notifications:
                navToNotificationFragment();
                break;
            case R.id.tab_ll_favorites:
                navToFavoritesFragment();
                break;
            case R.id.tab_ll_offers:
                navToNewOffersFragment();
                break;
        }
    }

    private void navToVerifyMemberFragment() {
        boolean addToBackStack = true;
        Fragment frg = new VerifyMemberFragment();
        DashboardActivity mainActivity = (DashboardActivity) getActivity();
        mainActivity.switchContent(frg, null, addToBackStack, "homeFragments");
    }

    private void navToNotificationFragment() {
        boolean addToBackStack = true;
        Fragment frg = new NotificationFragment();
        DashboardActivity mainActivity = (DashboardActivity) getActivity();
        mainActivity.switchContent(frg, null, addToBackStack, "homeFragments");
    }

    private void navToFavoritesFragment() {
        boolean addToBackStack = true;
        Fragment frg = new FavoritesFragment();
        DashboardActivity mainActivity = (DashboardActivity) getActivity();
        mainActivity.switchContent(frg, null, addToBackStack, "homeFragments");
    }

    private void navToNewOffersFragment() {
        boolean addToBackStack = true;
        Fragment frg = new NewOffersFragment();
        DashboardActivity mainActivity = (DashboardActivity) getActivity();
        mainActivity.switchContent(frg, null, addToBackStack, "homeFragments");
    }

    private BroadcastReceiver myReceiverHomeOffers = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Intent i = getActivity().getIntent();
//            String strIntent = i.getStringExtra("BRRRR");
            String strIntentHomeOffers = intent.getStringExtra(Constants.DEFAULT_VALUES.BROADCAST_STATUS);
            Log.d("asdasd", "onReceive home offers: " + strIntentHomeOffers);
            //Home offers Intent
            if (strIntentHomeOffers.equals(Constants.DEFAULT_VALUES.ONE)) {
                upDateBothList();
            } else {
                introActivityManager.doFetchHomeOffers2();
            }
            if (AppInstance.rModel_homeOffers != null) {

                if (AppInstance.rModel_homeOffers.getReview() != null &&
                        AppInstance.rModel_homeOffers.getReview().getData() != null) {
                    AppInstance.myReviewsCount = AppInstance.rModel_homeOffers.getReview().getData().size();
                } else {
                    AppInstance.myReviewsCount = 0;
                }
                Log.d("asdasAWASFSDFd", "VISIBLEsIZEBRodcst:" + AppInstance.myReviewsCount);
                // TO show drawer red icon
                if (AppInstance.myReviewsCount > 0 || !AppInstance.isProfileCompleted) {
                    mSlideButton.setVisibility(View.GONE);
                    mSlideRedIconButton.setVisibility(View.VISIBLE);
//                        if (AppInstance.myReviewsCount > 0 && AppInstance.myReviewsCount < 6) {
//                            txvRedIconBtn.setText("" + AppInstance.myReviewsCount);
//                        } else if (AppInstance.myReviewsCount > 5) {
//                            txvRedIconBtn.setText("5+");
//                        }
//                txvRedIconBtn.setText("" + AppInstance.myReviewsCount);
                } else {
                    AppInstance.myReviewsCount = 0;
                    mSlideButton.setVisibility(View.VISIBLE);
                    mSlideRedIconButton.setVisibility(View.GONE);
                }
            }
        }
    };

    private void showAppUpdateDialogue(boolean _isCancellAble) {

        if (!isAppUpdateDialogueDisplaying) {
            isAppUpdateDialogueDisplaying = true;
            utilObj.showAppUpdateAlertDialog(mActivity, _isCancellAble, appUpdateDialogConfirmationInterfacesLocation);
        }
    }

    private BroadcastReceiver myReceiverCheckSubscription = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String strIntentCheckSubscription = intent.getStringExtra(Constants.DEFAULT_VALUES.BROADCAST_SUBSCRIPTION_STATUS);
            Log.d("asdasd1", "onReceive Subscrp: " + strIntentCheckSubscription);
            if (AppInstance.rModel_checkSubscription != null) {
                //Check Subscription Intent

                // setting appUpdateVersion dialogue
                String appVersion = AppPreference.getSetting(mContext, "key_app_version", "");
                boolean foreFullyUpdate = false;
                if (AppInstance.rModel_checkSubscription != null) {
                    if (AppInstance.rModel_checkSubscription.getVersion().getForcefullyUpdated().equalsIgnoreCase("0")) {
                        foreFullyUpdate = true;
                    } else {
                        foreFullyUpdate = false;
                    }
                } else {
                }
                Log.d("dfasdfdsfdsf", "onbroadCast app version is:" + appVersion);

                if (appVersion.length() > 0 && AppInstance.rModel_checkSubscription != null) {
                    if (Float.parseFloat(AppInstance.rModel_checkSubscription.getVersion().getVersion()) > Float.parseFloat(appVersion)) {
                        showAppUpdateDialogue(foreFullyUpdate);
                    } else {
                        Log.d("dfasdfdsfdsf", "onbroadCast: App is upto date");
                    }
                }

                if (strIntentCheckSubscription.equals(Constants.DEFAULT_VALUES.ONE)) {
                    AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 1);
                    AppInstance.getAppInstance().setIsUserSubscribed(true);
                    btnGainAccess.setVisibility(View.GONE);
                    llIstOffer.setVisibility(View.GONE);
                    Log.d("asdasd1", "onReceive: true");
                } else
                    //Handling 1st week Button Visibility
                    if (AppInstance.rModel_checkSubscription.getFirst_weak() != null &&
                            AppInstance.rModel_checkSubscription.getFirst_weak().equals("1")) {
                        Log.d("asdasdqw", "onBroadcast 1st week:");
                        AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 2);
                        AppInstance.getAppInstance().setIsUserSubscribed(true);
                        AppInstance.canUserUnSubscribe = false;
                        txvOfferDays.setText(AppInstance.rModel_checkSubscription.getFirst_week_rem_days() + " Days left");
                        llGainAcess.setVisibility(View.GONE);
                        llIstOffer.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("asdasdqw", "onBroadcast 1st week expired:");
                        AppInstance.getAppInstance().setIsUserSubscribed(false);
                        llGainAcess.setVisibility(View.VISIBLE);
                        llIstOffer.setVisibility(View.GONE);
                        if (AppInstance.rModel_checkSubscription.getFirst_month() != null &&
                                AppInstance.rModel_checkSubscription.getFirst_month().equals("1")) {
                            btnGainAccess.setText(getResources().getString(R.string.offer_get_frst_month));
                            btnGainAccess.setVisibility(View.VISIBLE);
                            AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 3);
                            AppInstance.canUserUnSubscribe = false;
                        } else {
                            btnGainAccess.setText(getResources().getString(R.string.unlock_offers));
                            btnGainAccess.setVisibility(View.VISIBLE);
                            AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);
                            AppInstance.canUserUnSubscribe = false;
                        }
                    }


                //Handling Bubble and Notification Dot
                if (AppInstance.rModel_checkSubscription != null &&
                        AppInstance.rModel_checkSubscription.getUn_readed_badge_count() > 0) {
                    Log.d("asdasdsasd", "OnBroadCast: 0");
                    rlNotifctnBubble.setVisibility(View.VISIBLE);
                    imvNotification.setVisibility(View.GONE);
                    txvBubbleCount.setText(AppInstance.rModel_checkSubscription.getUn_readed_badge_count() + "");
                } else if (AppInstance.rModel_checkSubscription.getUn_readed_notifications_count() > 0) {
                    imvNotification.setVisibility(View.VISIBLE);
                    Log.d("asdasdsasd", "OnBroadCast: >0");
                }


                // Handling A/B testing for subscription screens
                if (AppInstance.rModel_checkSubscription.getSubscription_page() != null &&
                        AppInstance.rModel_checkSubscription.getSubscription_page().
                                equals(Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD)) {
                    AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD,
                            AppInstance.rModel_checkSubscription.getSubscription_page());
                } else {
                    AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD, "");
                }

                //Handling Home Screen Msg

                if (AppInstance.rModel_checkSubscription != null &&
                        AppInstance.rModel_checkSubscription.getHome_screen_message() != null) {
                    txvHomeScreenMsg.setText(AppInstance.rModel_checkSubscription.getHome_screen_message());
                }
                AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, AppInstance.getAppInstance().isUserSubscribed());
            }


        }
    };

    private void upDateBothList() {
        lstGrids1 = new ArrayList<DModelHomeGrdVw>();
        lstGrids2 = new ArrayList<DModelHomeGrdVw>();

        String strJsonResponse = AppPreference.getSetting(mContext, Constants.SharedPreferenceKey.HOME_OFFERS_KEY, "");
        if (strJsonResponse.length() > 0) {
            Gson gson = new GsonBuilder().create();
            RModel_HomeOffers rModel_homeOffers = gson.fromJson(strJsonResponse, RModel_HomeOffers.class);
            AppInstance.rModel_homeOffers = rModel_homeOffers;

            rlProgress1.setVisibility(View.GONE);
            rlProgress2.setVisibility(View.GONE);
            updateGridVwData();
            updateGridViewWidth1();
            updateGridViewWidth2();

            grdvw1.setNumColumns(lstGrids1.size());
            grdvw1.setAdapter(horizontalGridViewAdapter1);
            grdvw2.setNumColumns(lstGrids2.size());
            grdvw2.setAdapter(horizontalGridViewAdapter2);

        }

    }

    private void updateGainAccessbtn() {
        long no = AppPreference.getSettingResturnsLong(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);
        int status_no = (int) no;
        Log.d("asdasdqwd", "VISIBLE:" + status_no);
        switch (status_no) {
            case 1:
                btnGainAccess.setVisibility(View.GONE);
                break;

            case 2:
                llGainAcess.setVisibility(View.GONE);
                llIstOffer.setVisibility(View.VISIBLE);
                AppInstance.canUserUnSubscribe = false;
                break;

            case 3:
                btnGainAccess.setText(mContext.getResources().getString(R.string.offer_get_frst_month));
                btnGainAccess.setVisibility(View.VISIBLE);
                AppInstance.canUserUnSubscribe = false;
                break;

            case 4:
                AppInstance.canUserUnSubscribe = false;
                break;
        }

        if (AppPreference.getSetting(mContext, "key_new_offer_badge", "").length() > 0) {
            imvNewOffer.setVisibility(View.VISIBLE);
        } else {
            imvNewOffer.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(myReceiverHomeOffers, new IntentFilter(Constants.MY_BROADCAST_INTENT_HOME_OFFERS));
        getActivity().registerReceiver(myReceiverCheckSubscription, new IntentFilter(Constants.MY_BROADCAST_INTENT_CHECK_SUBSCRIBE));


        // setting appUpdateVersion dialogue
        String appVersion = AppPreference.getSetting(mContext, "key_app_version", "");
        boolean foreFullyUpdate = false;
        if (AppInstance.rModel_checkSubscription != null) {
            if (AppInstance.rModel_checkSubscription.getVersion().getForcefullyUpdated().equalsIgnoreCase("0")) {
                foreFullyUpdate = true;
            } else {
                foreFullyUpdate = false;
            }
        } else {
        }
        Log.d("dfasdfdsfdsf", "onResume:");
        Log.d("dfasdfdsfdsf", "onbroadCast app version is:" + appVersion);

        if (appVersion.length() > 0 && AppInstance.rModel_checkSubscription != null) {
            if (Float.parseFloat(AppInstance.rModel_checkSubscription.getVersion().getVersion()) > Float.parseFloat(appVersion)) {
                showAppUpdateDialogue(foreFullyUpdate);
            } else {
                Log.d("dfasdfdsfdsf", "onbroadCast: App is upto date");
            }
        }


        updateGainAccessbtn();
        Log.d("dsfasdfjdsalfj", "On Resume");
        if (AppPreference.getSetting(mContext, Constants.SharedPreferenceKey.HOME_OFFERS_KEY, "").length() > 0) {
            Log.d("asdasd", "onBroadcast HomeApi: Sucess");
            upDateBothList();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtils.isConnected(mActivity)) {
                    isNetworkConnected = true;
                    fetchOnResumeData();
                } else {
//                    utilObj.showToast(mActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT);
//                    Toast.makeText(mActivity,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
//         utilObj.showCustomAlertDialog(mActivity,"UP","No Interet","Ok",false,0);
                    if (!AppInstance.isNetworkMsgShowd) {
//                        initialize();
//                        Toast.makeText(mActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        try {
                            utilObj.showCustomAlertDialog(mActivity, null, getString(R.string.no_internet), null, null, false, null);
                            AppInstance.isNetworkMsgShowd = true;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }, Constants.SplashScreen.HOME_DELAY_LENGTH);
    }

    private void fetchOnResumeData() {
        Log.d("asdasd", "onResume:");

        if (AppInstance.isBroadCastSubscriptionCompleted) {
            if (AppPreference.getSetting(mActivity, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "").length() > 0) {
                if (AppInstance.rModel_checkSubscription != null) {

                    // setting appUpdateVersion dialogue
                    String appVersion = AppPreference.getSetting(mContext, "key_app_version", "");
                    boolean foreFullyUpdate = false;
//                    if (AppInstance.rModel_checkSubscription.getVersion()!=null) {
//                        if (AppInstance.rModel_checkSubscription.getVersion().getForcefullyUpdated().equalsIgnoreCase("0")) {
//                            foreFullyUpdate = true;
//                        } else {
//                            foreFullyUpdate = false;
//                        }
//                    } else {
//                    }
//                    Log.d("dfasdfdsfdsf", "onResumeFetchdata:" + appVersion);
//
//                    if (appVersion.length() > 0) {
//                        if (Float.parseFloat(AppInstance.rModel_checkSubscription.getVersion().getVersion()) > Float.parseFloat(appVersion)) {
//                            showAppUpdateDialogue(foreFullyUpdate);
//                        } else {
//                            Log.d("dfasdfdsfdsf", "onResumeFetchdata: App is upto date");
//                        }
//                    }

                    if (AppPreference.getSetting(mActivity, Constants.DEFAULT_VALUES.KEY_USER_SUBSCRIBE_STATUS, "").equalsIgnoreCase("true")) {
                        AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 1);
                        AppInstance.getAppInstance().setIsUserSubscribed(true);
                        btnGainAccess.setVisibility(View.GONE);
                        llIstOffer.setVisibility(View.GONE);
                        Log.d("asdasd1", "onBroadcast: Subs");
                    } else
                        //Handling 1st week Button Visibility
                        if (AppInstance.rModel_checkSubscription.getFirst_weak() != null &&
                                AppInstance.rModel_checkSubscription.getFirst_weak().equals("1")) {
                            Log.d("asdasdqw", "onBroadcast 1st week:");
                            AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 2);
                            AppInstance.getAppInstance().setIsUserSubscribed(true);
                            AppInstance.canUserUnSubscribe = false;
                            txvOfferDays.setText(AppInstance.rModel_checkSubscription.getFirst_week_rem_days() + " Days left");
                            llGainAcess.setVisibility(View.GONE);
                            llIstOffer.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("asdasdqw", "onBroadcast 1st week expired:");
                            AppInstance.getAppInstance().setIsUserSubscribed(false);
                            llGainAcess.setVisibility(View.VISIBLE);
                            llIstOffer.setVisibility(View.GONE);
                            if (AppInstance.rModel_checkSubscription.getFirst_month() != null &&
                                    AppInstance.rModel_checkSubscription.getFirst_month().equals("1")) {
                                btnGainAccess.setText(mContext.getResources().getString(R.string.offer_get_frst_month));
                                btnGainAccess.setVisibility(View.VISIBLE);
                                AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 3);
                                AppInstance.canUserUnSubscribe = false;
                            } else {
                                btnGainAccess.setText(mContext.getResources().getString(R.string.unlock_offers));
                                btnGainAccess.setVisibility(View.VISIBLE);
                                AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);
                                AppInstance.canUserUnSubscribe = false;
                            }
                        }


                    //Handling Bubble and Notification Dot
                    if (AppInstance.rModel_checkSubscription != null &&
                            AppInstance.rModel_checkSubscription.getUn_readed_badge_count() > 0) {
                        Log.d("asdasdsasd", "OnResume: 0");
                        rlNotifctnBubble.setVisibility(View.VISIBLE);
                        imvNotification.setVisibility(View.GONE);
                        txvBubbleCount.setText(AppInstance.rModel_checkSubscription.getUn_readed_badge_count() + "");
                    } else if (AppInstance.rModel_checkSubscription.getUn_readed_notifications_count() > 0) {
                        imvNotification.setVisibility(View.VISIBLE);
                        Log.d("asdasdsasd", "OnResume: >0");
                    }

                    // Handling A/B testing for subscription screens
                    if (AppInstance.rModel_checkSubscription.getSubscription_page() != null &&
                            AppInstance.rModel_checkSubscription.getSubscription_page().
                                    equals(Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD)) {
                        AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD,
                                AppInstance.rModel_checkSubscription.getSubscription_page());
                    } else {
                        AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.SUBSCRIPTION_CREDIT_CARD, "");
                    }
                }
                AppPreference.setSetting(mActivity, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, AppInstance.getAppInstance().isUserSubscribed());

            } else {

                if (NetworkUtils.isConnected(mContext)) {
                    Log.d("asdasd1", "onBroadcast: calls Do check");
                    introActivityManager.doCheckSubscribe(AppPreference.getSetting(mContext,
                            Constants.Request.CUSTOMER_ID, ""));
                } else {
                    utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                }
            }
        }

        if (AppInstance.isBroadCastHomeCompleted) {
            if (AppPreference.getSetting(mContext, Constants.SharedPreferenceKey.HOME_OFFERS_KEY, "").length() > 0) {
                Log.d("asdasd", "onBroadcast HomeApi: Sucess");
//                upDateBothList();
            } else {
                Log.d("asdasd", "onBroadcast HomeApi: Failed");
                introActivityManager.doFetchHomeOffers2();
            }

            if (AppInstance.rModel_homeOffers != null) {

                if (AppInstance.rModel_homeOffers.getReview() != null &&
                        AppInstance.rModel_homeOffers.getReview().getData() != null) {
                    AppInstance.myReviewsCount = AppInstance.rModel_homeOffers.getReview().getData().size();

                }
                Log.d("asdasAWASFSDFd", "VISIBLEsIZE:" + AppInstance.myReviewsCount);

                // TO show drawer red icon
                if (AppInstance.myReviewsCount > 0 || !AppInstance.isProfileCompleted) {
                    mSlideButton.setVisibility(View.GONE);
                    mSlideRedIconButton.setVisibility(View.VISIBLE);
                    if (AppInstance.myReviewsCount > 0 && AppInstance.myReviewsCount < 6) {
                        txvRedIconBtn.setText("" + AppInstance.myReviewsCount);
                    } else if (AppInstance.myReviewsCount > 5) {
                        txvRedIconBtn.setText("5+");
                    }
//                txvRedIconBtn.setText("" + AppInstance.myReviewsCount);
                } else {
                    AppInstance.myReviewsCount = 0;
                    mSlideButton.setVisibility(View.VISIBLE);
                    mSlideRedIconButton.setVisibility(View.GONE);
                }

            }

        } else {
            if (AppInstance.rModel_homeOffers != null) {

                if (AppInstance.rModel_homeOffers.getReview() != null &&
                        AppInstance.rModel_homeOffers.getReview().getData() != null) {
                    AppInstance.myReviewsCount = AppInstance.rModel_homeOffers.getReview().getData().size();

                }
                Log.d("asdasAWASFSDFd", "VISIBLEsIZE:" + AppInstance.myReviewsCount);

                // TO show drawer red icon
                if (AppInstance.myReviewsCount > 0 || !AppInstance.isProfileCompleted) {
                    mSlideButton.setVisibility(View.GONE);
                    mSlideRedIconButton.setVisibility(View.VISIBLE);
                    if (AppInstance.myReviewsCount > 0 && AppInstance.myReviewsCount < 6) {
                        txvRedIconBtn.setText("" + AppInstance.myReviewsCount);
                    } else if (AppInstance.myReviewsCount > 5) {
                        txvRedIconBtn.setText("5+");
                    }
//                txvRedIconBtn.setText("" + AppInstance.myReviewsCount);
                } else {
                    AppInstance.myReviewsCount = 0;
                    mSlideButton.setVisibility(View.VISIBLE);
                    mSlideRedIconButton.setVisibility(View.GONE);
                }

            } else if (isNetworkConnected) {
                if (NetworkUtils.isConnected(mActivity)) {
                    Log.d("dadsf", "fetchOnResumeData: again call ");
                    introActivityManager.doFetchHomeOffers2();
                } else {
                    utilObj.showToast(mContext, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_LONG);
                }
            }
        }
        Log.d("asdasd12", "onResume Comes from: " + AppInstance.isAppLogedIn);
        if (AppInstance.isAppLogedIn) {
            if (NetworkUtils.isConnected(mContext)) {
                Log.d("asdasd12", "onResume Comes from2: " + AppInstance.isAppLogedIn);
                introActivityManager.doCheckSubscribe(AppPreference.getSetting(mContext,
                        Constants.Request.CUSTOMER_ID, ""));
//                introActivityManager.doFetchHomeOffers2();
            } else {
                utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
            }
        }

        AppInstance.isAppLogedIn = true;
        if (AppInstance.rModel_checkSubscription != null &&
                AppInstance.rModel_checkSubscription.getHome_screen_message() != null) {
            txvHomeScreenMsg.setText(AppInstance.rModel_checkSubscription.getHome_screen_message());
        }
        updateGainAccessbtn();

        //For push notification nav to notification detail fragment
        if (SplashScreenActivity.notificationId > 0) {
            navToNotificationFragment();
        }
        if (GPSTracker.gpsTracker != null) {
            Log.d("sadfafdsadaqsG", "onSuccessRedirection:0 " + GPSTracker.gpsTracker.getLatitude());
            GPSTracker.gpsTracker = null;
        }

    }

    public void onSuccessRedirection(int taskID) {

        if (taskID == Constants.TaskID.CHECK_SUBSCRIPTION_TASK_ID) {

//            configureAccessOfferButton();
            utilObj.generateEvent(getActivity(), Constants.DEFAULT_VALUES.MY_REVIEW_UPDATED, null, null);


        } else if (taskID == Constants.TaskID.DO_CHECK_SUBSCRIPTION) {
            Log.d("sadfafdsG", "onSuccessRedirection:0 ");

            if (AppInstance.getAppInstance().isUserSubscribed()) {
//                mUnlockOffersBackgroundLayout.setBackgroundColor(Color.TRANSPARENT);
//            mUnlockOffersBackgroundLayout.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));)
//
//                mUnlockOffers.setVisibility(View.INVISIBLE);
                Log.d("sadfafdsG", "onSuccessRedirection: ");
                btnGainAccess.setVisibility(View.GONE);
            }
        } else if (taskID == Constants.TaskID.FETCH_HOME_OFFERS_TASK_ID) {

            rlProgress1.setVisibility(View.GONE);
            rlProgress2.setVisibility(View.GONE);
            updateGridVwData();
            updateGridViewWidth1();
            updateGridViewWidth2();

            grdvw1.setNumColumns(lstGrids1.size());
            grdvw1.setAdapter(horizontalGridViewAdapter1);
            grdvw2.setNumColumns(lstGrids2.size());
            grdvw2.setAdapter(horizontalGridViewAdapter2);
        }
    }

    public void onFailureRedirection(String errorMessage) {
        AppInstance.isUserSubscribeStatusServiceCalled = false;

        if (!AppInstance.getAppInstance().isUserSubscribed()) {
//                mUnlockOffersBackgroundLayout.setBackgroundColor(Color.TRANSPARENT);
//            mUnlockOffersBackgroundLayout.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));)
//
//                mUnlockOffers.setVisibility(View.INVISIBLE);
            Log.d("sadfafdsG", "onSuccessRedirection: Unsubscribe ");
            btnGainAccess.setVisibility(View.VISIBLE);
        }
    }

    private void updateGridVwData() {
        if (AppInstance.rModel_homeOffers != null) {
            if (AppInstance.rModel_homeOffers.getRecentproducts().size() > 0) {
                for (int i = 0; i < AppInstance.rModel_homeOffers.getRecentproducts().size(); i++) {
//                    if (AppInstance.rModel_homeOffers.getRecentproducts().get(i).getStatus().equalsIgnoreCase("active")) {
                    String festival = "";
//                        if (AppInstance.rModel_homeOffers.getRecentproducts().get(i).getFestival() != null &&
//                                AppInstance.rModel_homeOffers.getRecentproducts().get(i).getFestival().length() > 0) {
//                            festival = AppInstance.rModel_homeOffers.getRecentproducts().get(i).getFestival();
//                            imvFestival.setVisibility(View.VISIBLE);
//                            if (festival.equalsIgnoreCase(mContext.getResources().getString(R.string.festival_ramadan))) {
//                                imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.ramadan));
//
//                            } else if (festival.equalsIgnoreCase(mContext.getResources().getString(R.string.festival_burger))) {
                    imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
//
//                            } else if (festival.equalsIgnoreCase(mContext.getResources().getString(R.string.festival_biryani))) {
//                                imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));
//                            }
//                        }
                    String strDistance = AppInstance.rModel_homeOffers.getRecentproducts().get(i).getDistance();
                    String[] parts = strDistance.split(Pattern.quote("."));
                    int dist = Integer.parseInt(parts[0]);

                    lstGrids1.add(new DModelHomeGrdVw(AppInstance.rModel_homeOffers.getRecentproducts().get(i).getImage(),
                            AppInstance.rModel_homeOffers.getRecentproducts().get(i).getName(),
                            AppInstance.rModel_homeOffers.getRecentproducts().get(i).getId(),
                            AppInstance.rModel_homeOffers.getRecentproducts().get(i).getMerchantname(),
                            "biryani",
                            dist
                    ));
//                    }

                }
                AppInstance.mlstNewOffers = new ArrayList<>();
                AppInstance.mlstNewOffers = lstGrids1;
            }
            for (int i = 0; i < AppInstance.rModel_homeOffers.getMembersfavourite().size(); i++) {
//                if (AppInstance.rModel_homeOffers.getMembersfavourite().get(i).getStatus().equalsIgnoreCase("active")) {
                String festival = "";
                String strDistance = AppInstance.rModel_homeOffers.getMembersfavourite().get(i).getDistance();
                String[] parts = strDistance.split(Pattern.quote("."));
                int dist = Integer.parseInt(parts[0]);
                if (AppInstance.rModel_homeOffers.getMembersfavourite().get(i).getFestival() != null &&
                        AppInstance.rModel_homeOffers.getMembersfavourite().get(i).getFestival().length() > 0) {
                    festival = AppInstance.rModel_homeOffers.getMembersfavourite().get(i).getFestival();
                }
                lstGrids2.add(new DModelHomeGrdVw(AppInstance.rModel_homeOffers.getMembersfavourite().get(i).getImage(),
                        AppInstance.rModel_homeOffers.getMembersfavourite().get(i).getName(),
                        AppInstance.rModel_homeOffers.getMembersfavourite().get(i).getId(),
                        AppInstance.rModel_homeOffers.getMembersfavourite().get(i).getMerchantname(),
                        "",
                        dist
                ));
//                }

            }
            horizontalGridViewAdapter1 = new HorizontalGridViewAdapter(mActivity, lstGrids1);
            horizontalGridViewAdapter2 = new HorizontalGridViewAdapter(mActivity, lstGrids2);

        }
    }


    private class UpdateSubscribeOfferStatus extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            MyApplication.getInstance().printLogs("UpdateSubscribeOfferStatus", "UpdateSubscribeOfferStatus called :" + intent.getAction());
            if (intent.getAction().equalsIgnoreCase(Constants.DEFAULT_VALUES.UPDATE_USER_SUBSCRIBE_STATUS)) {
                // To show voda customer unSubscribe option.
                if (mActivity != null && isAdded()) {
                    if (!AppInstance.isUserSubscribeStatusServiceCalled) {
                        // AppInstance.setIsSubscriptionCheckCalled(false);
                        //doCallCheckSubscribeService();
                        AppInstance.isUserSubscribeStatusServiceCalled = true;
                        //-------------

//                        mUnlockOffersBackgroundLayout.setBackgroundColor(Color.TRANSPARENT);

                        //IF PAYMENT DONE SUCCESSFULLY (vodafoneStatus =1)
//                        mUnlockOffers.setVisibility(View.INVISIBLE);
                        // TO show UNSUBSCRIBE OPTION for VODA customer in menu drawer
                        utilObj.generateEvent(mActivity, Constants.DEFAULT_VALUES.SHOW_VODA_CUSTOMER_UNSUBSCRIBE_OPTION, null, null);
                        //-------------
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(myReceiverHomeOffers);
        getActivity().unregisterReceiver(myReceiverCheckSubscription);
        Log.d("sdfsda", "onPauseF: ");
        if (retryDoCallCheckSubscribeServiceHandler != null) {
            retryDoCallCheckSubscribeServiceHandler.removeCallbacksAndMessages(null);
        }
        MyApplication.getInstance().printLogs("doRetryCheckSubscribeServiceHandler", "onPause called :");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("sdfsda", "onStopF: ");
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

       /* if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(mContext,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}
