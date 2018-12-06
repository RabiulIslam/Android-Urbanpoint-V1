package com.urbanpoint.UrbanPoint.views.fragments.categoryFragments;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanpoint.UrbanPoint.BuildConfig;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.adapters.categoryScreensAdapters.ExpandableMerchintListAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.DModelMerchintList;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.OffersDetails;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.categoryScreens.MerchantManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.BaseActivity;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.OfferDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by aparnasalve on 7/3/16.
 */


public class FunAndLeisure_MerchantsListTabFragment extends Fragment implements View.OnClickListener, ServiceRedirection {

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private FragmentActivity mActivity;
    private Context mContext;
    private View mRootView;
    private ListView mListView;
    private Utility utilObj;
    private ArrayList<OffersDetails> mSortedOffersList;
    private ExpandableMerchintListAdapter expandableMerchintListAdapter;
    private ArrayList<DModelMerchintList> lstFunByLocation, lstFunByAlphabetically, lstFestivalByLocation, lstFestivalByAlphabetically;
    private ArrayList<DModelMerchintList.Child> lstChild;
    private MerchantManager mMerchantManager;
    private ExpandableListView mExpandibleListView;
    private RelativeLayout rlAlphabetically, rlLocation;
    private TextView txvLocation, txvAlphabetically;
    private boolean isLocationSort;
    private String mCatID;
    private LinearLayout llGenderTabs;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Typeface novaThin, novaRegular;
    private ImageView mBackButton;
    private CustomDialogConfirmationInterfaces contextualDialogConfirmationInterfacesLocation;

//    public static final FunAndLeisure_MerchantsListTabFragment newInstance(String message) {
//        FunAndLeisure_MerchantsListTabFragment f = new FunAndLeisure_MerchantsListTabFragment();
//        Bundle bdl = new Bundle(1);
//        bdl.putString(EXTRA_MESSAGE, message);
//        f.setArguments(bdl);
//        return f;
//    }

    public FunAndLeisure_MerchantsListTabFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // String message = getArguments().getString(EXTRA_MESSAGE);
        View view = inflater.inflate(R.layout.fragment_listview, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        initialize();
        MyApplication.getInstance().trackScreenView(getString(R.string.ga_fun_activity_outlets));

        doCallFetchMerchantList();

        mExpandibleListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("expdble", "GroupPositiion: " + groupPosition);
                Log.d("expdble", "ChildPositiion: " + childPosition);
                String custID;
                String productid;
                String itemName;
                if (isLocationSort) {
                    custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                    productid = lstFestivalByLocation.get(groupPosition).getChild().get(childPosition).getProductId();
                    itemName = lstFestivalByLocation.get(groupPosition).getChild().get(childPosition).getName();
                } else {
                    custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                    productid = lstFestivalByAlphabetically.get(groupPosition).getChild().get(childPosition).getProductId();
                    itemName = lstFestivalByAlphabetically.get(groupPosition).getChild().get(childPosition).getName();
                }
                Bundle m_bundle = new Bundle();
                m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
                m_bundle.putString(Constants.Request.OFFER_ID, productid);
                m_bundle.putString(Constants.Request.NAME, itemName);
                m_bundle.putString(Constants.Request.CATEGORYID, mCatID);

                Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
                intent.putExtras(m_bundle);
                startActivity(intent);
                return false;
            }
        });

        return view;
    }

    private void initialize() {
        utilObj = new Utility(mActivity);
        mMerchantManager = new MerchantManager(mActivity, this);
        mCatID = Constants.IntentPreference.FUN_ID + "";
        lstFunByLocation = new ArrayList<>();
        lstFunByAlphabetically = new ArrayList<>();
        lstFestivalByLocation = new ArrayList<>();
        lstFestivalByAlphabetically = new ArrayList<>();
        novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        isLocationSort = true;
        String header = getString(R.string.fun_activities);
        bindViews();
        setActionBar(header, true);
    }

    private void bindViews() {
        mExpandibleListView = (ExpandableListView) mRootView.findViewById(R.id.lvExp2);
        txvAlphabetically = (TextView) mRootView.findViewById(R.id.frg_outlet_txv_by_alphabetically);
        txvLocation = (TextView) mRootView.findViewById(R.id.frg_outlet_txv_by_location);
        rlAlphabetically = (RelativeLayout) mRootView.findViewById(R.id.frg_outlet_rl_by_alhpabetically);
        rlAlphabetically.setOnClickListener(this);
        rlLocation = (RelativeLayout) mRootView.findViewById(R.id.frg_outlet_rl_by_location);
        rlLocation.setOnClickListener(this);

        txvAlphabetically.setTypeface(novaRegular);
        txvLocation.setTypeface(novaRegular);

        llGenderTabs = (LinearLayout) mRootView.findViewById(R.id.ladiesTabs);
        llGenderTabs.setVisibility(View.GONE);

        this.mListView = (ListView) mRootView.findViewById(R.id.list);

        if (!utilObj.checkPermission(getActivity()) || !(utilObj.isLocationEnabled(mActivity))) {
            updatebtnAlpabetical();
        } else {
            updateBtnLocation();
        }

        contextualDialogConfirmationInterfacesLocation = new CustomDialogConfirmationInterfaces() {
            @Override
            public void callConfirmationDialogPositive() {
                if (NetworkUtils.isConnected(mContext)) {
                    if (utilObj.isLocationEnabled(mActivity)) {
                        requestPermission();
                        Log.d("qwecascasd", "LocationService is on:Y ");
                    } else {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.IntentPreference.SOURCE_LOCATION_INTENT_CODE);
                        Log.d("qwecascasd", "LocationService is off:Y ");
                    }
                } else {
                    utilObj.showCustomAlertDialog(mActivity, null, getString(R.string.no_internet), null, null, false, null);
                }

            }

            @Override
            public void callConfirmationDialogNegative() {
                Log.d("qwecascasd", "onSuccessRedirection:N ");

            }
        };
    }

    public void setActionBar(String title, boolean showNavButton) {
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);

        mBackButton = (ImageView) customView.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSlideButton.toggle(true);
                getActivity().finish();
            }
        });


        title1.setText(title);
        title1.setTypeface(novaRegular);
        ((BaseActivity) getActivity()).getSlidingMenu().setTouchModeAbove(
                SlidingMenu.TOUCHMODE_NONE);
        getActivity().getActionBar().setCustomView(customView);
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.GET_FUN_MERCHANT_LIST_TASK_ID) {
            Log.d("asfdasdf", "onSuccessRedirection: ");

            updateExpendibleListData();


        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        Log.d("asfdasdf", "onSuccessRedirection:F ");
        utilObj.stopiOSLoader();
        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
    }

    private void updateExpendibleListData() {
        if (AppInstance.rModel_merchintList.getData() != null &&
                AppInstance.rModel_merchintList.getData().size() > 0) {
            for (int i = 0; i < AppInstance.rModel_merchintList.getData().size(); i++) {
                lstChild = new ArrayList<>();
                String Gfestival = "";
                String Cfestival = "";
                boolean isChildPresent = true;
//                if (AppInstance.rModel_merchintList.getData().get(i).getProducts() != null) {
//                    for (int j = 0; j < AppInstance.rModel_merchintList.getData().get(i).getProducts().size(); j++) {
//                        if (AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getFestival() != null &&
//                                AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getFestival().length() > 0) {
//                            Gfestival = AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getFestival();
//                            Cfestival = AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getFestival();
//                        } else {
//                            Cfestival = "";
//                        }
//                        isChildPresent = true;
//                        lstChild.add(new DModelMerchintList.Child(
//                                AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getProductid(),
//                                AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getProductimage(),
//                                AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getProductname(),
//                                Cfestival,
//                                AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getStatus(),
//                                AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getGender(),
//                                AppInstance.rModel_merchintList.getData().get(i).getProducts().get(j).getThumbnail()
//                        ));
//                    }
//                }
                if (AppInstance.rModel_merchintList.getData().get(i).getFestival_merchant() != null &&
                        AppInstance.rModel_merchintList.getData().get(i).getFestival_merchant().length() > 0) {
                    Gfestival = AppInstance.rModel_merchintList.getData().get(i).getFestival_merchant();
//                    Cfestival = AppInstance.rModel_merchintList.getData().get(i).getFestival_merchant();
                } else {
//                    Cfestival = "";
                }
                if (Gfestival.length() > 0 && isChildPresent) {
                    lstFestivalByLocation.add(new DModelMerchintList(
                            AppInstance.rModel_merchintList.getData().get(i).getId(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantCount(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantName(),
                            AppInstance.rModel_merchintList.getData().get(i).getAddress(),
                            AppInstance.rModel_merchintList.getData().get(i).getDistance(),
                            AppInstance.rModel_merchintList.getData().get(i).getRating(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantsImage(),
                            AppInstance.rModel_merchintList.getData().get(i).getFirstname(),
                            AppInstance.rModel_merchintList.getData().get(i).getLastname(),
                            AppInstance.rModel_merchintList.getData().get(i).getIsActive(),
                            Gfestival,
                            lstChild));

                    lstFestivalByAlphabetically.add(new DModelMerchintList(
                            AppInstance.rModel_merchintList.getData().get(i).getId(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantCount(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantName(),
                            AppInstance.rModel_merchintList.getData().get(i).getAddress(),
                            AppInstance.rModel_merchintList.getData().get(i).getDistance(),
                            AppInstance.rModel_merchintList.getData().get(i).getRating(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantsImage(),
                            AppInstance.rModel_merchintList.getData().get(i).getFirstname(),
                            AppInstance.rModel_merchintList.getData().get(i).getLastname(),
                            AppInstance.rModel_merchintList.getData().get(i).getIsActive(),
                            Gfestival,
                            lstChild));
                } else if (isChildPresent) {
                    lstFunByLocation.add(new DModelMerchintList(
                            AppInstance.rModel_merchintList.getData().get(i).getId(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantCount(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantName(),
                            AppInstance.rModel_merchintList.getData().get(i).getAddress(),
                            AppInstance.rModel_merchintList.getData().get(i).getDistance(),
                            AppInstance.rModel_merchintList.getData().get(i).getRating(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantsImage(),
                            AppInstance.rModel_merchintList.getData().get(i).getFirstname(),
                            AppInstance.rModel_merchintList.getData().get(i).getLastname(),
                            AppInstance.rModel_merchintList.getData().get(i).getIsActive(),
                            Gfestival,
                            lstChild));

                    lstFunByAlphabetically.add(new DModelMerchintList(
                            AppInstance.rModel_merchintList.getData().get(i).getId(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantCount(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantName(),
                            AppInstance.rModel_merchintList.getData().get(i).getAddress(),
                            AppInstance.rModel_merchintList.getData().get(i).getDistance(),
                            AppInstance.rModel_merchintList.getData().get(i).getRating(),
                            AppInstance.rModel_merchintList.getData().get(i).getMerchantsImage(),
                            AppInstance.rModel_merchintList.getData().get(i).getFirstname(),
                            AppInstance.rModel_merchintList.getData().get(i).getLastname(),
                            AppInstance.rModel_merchintList.getData().get(i).getIsActive(),
                            Gfestival,
                            lstChild));
                }
            }
//                Collections.sort(lstFunByLocation,
//                        (o1, o2) -> o1.getMerchantName().compareTo(o2.getMerchantName()));

            Collections.sort(lstFunByAlphabetically, new Comparator<DModelMerchintList>() {
                @Override
                public int compare(DModelMerchintList o1, DModelMerchintList o2) {
                    return o1.getMerchantName().compareToIgnoreCase(o2.getMerchantName()); // To compare string values
                }
            });

            Collections.sort(lstFestivalByAlphabetically, new Comparator<DModelMerchintList>() {
                @Override
                public int compare(DModelMerchintList o1, DModelMerchintList o2) {
                    return o1.getMerchantName().compareToIgnoreCase(o2.getMerchantName()); // To compare string values
                }
            });

            for (int i = 0; i < lstFunByLocation.size(); i++) {
                lstFestivalByLocation.add(new DModelMerchintList(
                        lstFunByLocation.get(i).getId(),
                        lstFunByLocation.get(i).getMerchantCount(),
                        lstFunByLocation.get(i).getMerchantName(),
                        lstFunByLocation.get(i).getMerchantAddress(),
                        lstFunByLocation.get(i).getMerchantDistance(),
                        lstFunByLocation.get(i).getRating(),
                        lstFunByLocation.get(i).getMerchantsImageUrl(),
                        lstFunByLocation.get(i).getFirstname(),
                        lstFunByLocation.get(i).getLastname(),
                        lstFunByLocation.get(i).getIsActive(),
                        lstFunByLocation.get(i).getFestival(),
                        lstFunByLocation.get(i).getChild()));

                lstFestivalByAlphabetically.add(new DModelMerchintList(
                        lstFunByAlphabetically.get(i).getId(),
                        lstFunByAlphabetically.get(i).getMerchantCount(),
                        lstFunByAlphabetically.get(i).getMerchantName(),
                        lstFunByAlphabetically.get(i).getMerchantAddress(),
                        lstFunByAlphabetically.get(i).getMerchantDistance(),
                        lstFunByAlphabetically.get(i).getRating(),
                        lstFunByAlphabetically.get(i).getMerchantsImageUrl(),
                        lstFunByAlphabetically.get(i).getFirstname(),
                        lstFunByAlphabetically.get(i).getLastname(),
                        lstFunByAlphabetically.get(i).getIsActive(),
                        lstFunByAlphabetically.get(i).getFestival(),
                        lstFunByAlphabetically.get(i).getChild()));
            }

            if (utilObj.isLocationEnabled(mActivity)) {
                if (utilObj.checkPermission(getActivity())) {
                    updateBtnLocation();
                    expandableMerchintListAdapter = new ExpandableMerchintListAdapter(mActivity, lstFestivalByLocation, mCatID);
                    mExpandibleListView.setAdapter(expandableMerchintListAdapter);
                } else {
                    updatebtnAlpabetical();
                    expandableMerchintListAdapter = new ExpandableMerchintListAdapter(mActivity, lstFestivalByAlphabetically, mCatID);
                    mExpandibleListView.setAdapter(expandableMerchintListAdapter);
                }
            } else {
                updatebtnAlpabetical();
                expandableMerchintListAdapter = new ExpandableMerchintListAdapter(mActivity, lstFestivalByAlphabetically, mCatID);
                mExpandibleListView.setAdapter(expandableMerchintListAdapter);
            }
        } else {
            utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.no_offer_availble), null, null, false, null);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //doCallFetchMerchantList();
        if (expandableMerchintListAdapter != null) {
            expandableMerchintListAdapter.notifyDataSetChanged();
        }
    }

    private void doCallFetchMerchantList() {

        if (AppInstance.rModel_merchintList == null) {
            if (NetworkUtils.isConnected(mContext)) {
                utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
//                assigning the data to the user object
                mMerchantManager.doGetFunActivitiesMerchantList();
            } else {
                utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
            }
        }
    }

    private void doUpdateLocationPermission() {
        mMerchantManager.doUpdatePermissions();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_outlet_rl_by_alhpabetically:
//                if (lstFestivalByLocation.size() > 0) {
                updatebtnAlpabetical();

                expandableMerchintListAdapter = new ExpandableMerchintListAdapter(mActivity, lstFestivalByAlphabetically, mCatID);
                mExpandibleListView.setAdapter(expandableMerchintListAdapter);
                expandableMerchintListAdapter.notifyDataSetChanged();
//                } else {
//                    utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.no_offer_availble), null, null, false, null);
//                }
                break;

            case R.id.frg_outlet_rl_by_location:
//                if (lstFestivalByAlphabetically.size() > 0) {
                if (!utilObj.isLocationEnabled(mActivity) || !utilObj.checkPermission(getActivity())) {
                    utilObj.showContextualAlertDialog(mActivity, contextualDialogConfirmationInterfacesLocation);
                } else {
                    updateBtnLocation();
                    expandableMerchintListAdapter = new ExpandableMerchintListAdapter(mActivity, lstFestivalByLocation, mCatID);
                    mExpandibleListView.setAdapter(expandableMerchintListAdapter);
                    expandableMerchintListAdapter.notifyDataSetChanged();
                }
//                } else {
//                    utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.no_offer_availble), null, null, false, null);
//                }
                break;
        }
    }

    private void updatebtnAlpabetical() {
        rlLocation.setBackground(getResources().getDrawable(R.drawable.gray_background_border));
        txvLocation.setTextColor(Color.BLACK);
        rlAlphabetically.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));
        txvAlphabetically.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rlLocation.setElevation(dpToPx(0));
            rlAlphabetically.setElevation(dpToPx(5));
        }
        isLocationSort = false;

    }

    private void updateBtnLocation() {
        rlAlphabetically.setBackground(getResources().getDrawable(R.drawable.gray_background_border));
        txvAlphabetically.setTextColor(Color.BLACK);
        rlLocation.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));
        txvLocation.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rlLocation.setElevation(dpToPx(5));
            rlAlphabetically.setElevation(dpToPx(0));
        }
        isLocationSort = true;

    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

       /* if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(mContext,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //was crashing here
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    if (GPSTracker.gpsTracker != null) {
                        GPSTracker.gpsTracker = null;
                    }
                    AppInstance.rModel_merchintList = null;
                    lstFunByLocation = new ArrayList<>();
                    lstFunByAlphabetically = new ArrayList<>();
                    lstFestivalByLocation = new ArrayList<>();
                    lstFestivalByAlphabetically = new ArrayList<>();
                    doUpdateLocationPermission();
                    doCallFetchMerchantList();
                } else {
                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        if (AppPreference.getSetting(mContext, "key_never_ask_again_location", "").length() > 0) {
                            startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)), Constants.IntentPreference.PACKAGE_LOCATION_INTENT_CODE);
                        } else {
                            AppPreference.setSetting(mContext, "key_never_ask_again_location", "yes");
                        }
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.IntentPreference.PACKAGE_LOCATION_INTENT_CODE == requestCode) {
            if (utilObj.checkPermission(getActivity())) {

                updateBtnLocation();
                AppInstance.rModel_merchintList = null;
                lstFunByLocation = new ArrayList<>();
                lstFunByAlphabetically = new ArrayList<>();
                lstFestivalByLocation = new ArrayList<>();
                lstFestivalByAlphabetically = new ArrayList<>();
                doUpdateLocationPermission();
                doCallFetchMerchantList();

            }
        } else if (Constants.IntentPreference.SOURCE_LOCATION_INTENT_CODE == requestCode) {

            if (utilObj.isLocationEnabled(mActivity)) {
                if (!utilObj.checkPermission(getActivity())) {
                    requestPermission();
                } else {
                    updateBtnLocation();
                    AppInstance.rModel_merchintList = null;
                    lstFunByLocation = new ArrayList<>();
                    lstFunByAlphabetically = new ArrayList<>();
                    lstFestivalByLocation = new ArrayList<>();
                    lstFestivalByAlphabetically = new ArrayList<>();
                    doUpdateLocationPermission();
                    doCallFetchMerchantList();
                }
            } else {
                Log.d("sadSDSAASD", "onCreateView:5520 ");
            }
        }

    }
}

