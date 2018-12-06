package com.urbanpoint.UrbanPoint.views.fragments.HomeFragments;


import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.BuildConfig;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.adapters.homeAdapter.favoritesAdapter.FavoritesAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.main.DModelHomeGrdVw;
import com.urbanpoint.UrbanPoint.interfaces.CustomDialogConfirmationInterfaces;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.HomeManager;
import com.urbanpoint.UrbanPoint.managers.categoryScreens.MerchantManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.GPSTracker;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.OfferDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOffersFragment extends Fragment implements View.OnClickListener, ServiceRedirection {

    private FragmentActivity mActivity;
    private FragmentManager frgMngr;
    private Context mContext;
    private View mRootView;
    private Utility utilObj;
    private ImageView mBackButton;
    private ListView lsvNewOffers;
    private List<DModelHomeGrdVw> lstNewOffersByLocation, lstNewOffersByAlphabetically, lstFestivalByLocation, lstFestivalByAlphabetically;
    private FavoritesAdapter favoritesAdapter;
    private RelativeLayout rlAlphabetically, rlLocation;
    private TextView txvLocation, txvAlphabetically, txvNotFound;
    private HomeManager homeManager;
    private MerchantManager mMerchantManager;
    private boolean isLocationSort;
    private boolean isSubscribed;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Typeface novaThin, novaRegular;
    private CustomDialogConfirmationInterfaces contextualDialogConfirmationInterfacesLocation;

    public NewOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_offers, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        isSubscribed = AppPreference.getSettingResturnsBoolean(mActivity, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
        initialize();

        lsvNewOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String custID;
                String productid;
                String itemName;
                custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");

                if (isLocationSort) {
                    productid = lstNewOffersByLocation.get(position).getStrProductId();
                    itemName = lstNewOffersByLocation.get(position).getStrOfferName();
                } else {
                    productid = lstNewOffersByAlphabetically.get(position).getStrProductId();
                    itemName = lstNewOffersByAlphabetically.get(position).getStrOfferName();
                }
                Bundle m_bundle = new Bundle();
                m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
                m_bundle.putString(Constants.Request.OFFER_ID, productid);
                m_bundle.putString(Constants.Request.NAME, itemName);

                Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
                intent.putExtras(m_bundle);
                startActivity(intent);
            }
        });

        return view;
    }


    private void initialize() {
        MyApplication.getInstance().trackScreenView(getString(R.string.contact_us));
        utilObj = new Utility(getActivity());
        homeManager = new HomeManager(mContext, this);
        mMerchantManager = new MerchantManager(mContext, this);
        frgMngr = getFragmentManager();
        novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        setActionBar("New Offers", false);
        isLocationSort = true;
        AppInstance.rModel_newOffers = null;
        lstNewOffersByLocation = new ArrayList<>();
        lstNewOffersByAlphabetically = new ArrayList<>();
        lstFestivalByLocation = new ArrayList<>();
        lstFestivalByAlphabetically = new ArrayList<>();
        bindViews();

        if (NetworkUtils.isConnected(mContext)) {
            utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
            homeManager.doFetchNewOffers();
        } else {
            utilObj.showToast(mContext, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }

    private void bindViews() {
        txvNotFound = (TextView) mRootView.findViewById(R.id.frg_new_offer_txv_nt_found);
        lsvNewOffers = (ListView) mRootView.findViewById(R.id.frg_fav_lst_view);
        txvAlphabetically = (TextView) mRootView.findViewById(R.id.frg_fav_txv_alphabetically);
        txvLocation = (TextView) mRootView.findViewById(R.id.frg_fav_txv_location);
        rlAlphabetically = (RelativeLayout) mRootView.findViewById(R.id.frg_fav_rl_alphabetically);
        rlAlphabetically.setOnClickListener(this);
        rlLocation = (RelativeLayout) mRootView.findViewById(R.id.frg_fav_rl_location);
        rlLocation.setOnClickListener(this);
        txvAlphabetically.setTypeface(novaRegular);
        txvLocation.setTypeface(novaRegular);
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
//        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

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
            case R.id.frg_fav_rl_alphabetically:
                updatebtnAlpabetical();

                favoritesAdapter = new FavoritesAdapter(mContext, lstFestivalByAlphabetically, isSubscribed);
                lsvNewOffers.setAdapter(favoritesAdapter);
                break;

            case R.id.frg_fav_rl_location:
                if (!utilObj.isLocationEnabled(mActivity) || !utilObj.checkPermission(getActivity())) {
                    utilObj.showContextualAlertDialog(mActivity, contextualDialogConfirmationInterfacesLocation);
                } else {
                    updateBtnLocation();
                    favoritesAdapter = new FavoritesAdapter(mContext, lstFestivalByLocation, isSubscribed);
                    lsvNewOffers.setAdapter(favoritesAdapter);
                }
                break;
        }
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();
        if (taskID == Constants.TaskID.FETCH_NEW_OFFERS_TASK_ID) {
            if (AppInstance.rModel_newOffers != null) {
                if (AppInstance.rModel_newOffers.getRecentproducts() != null &&
                        AppInstance.rModel_newOffers.getRecentproducts().size() > 0) {
                    for (int i = 0; i < AppInstance.rModel_newOffers.getRecentproducts().size(); i++) {
                        String festival = "";

                        if (AppInstance.rModel_newOffers.getRecentproducts().get(i).getStatus().equalsIgnoreCase("active")) {
                            if (AppInstance.rModel_newOffers.getRecentproducts().get(i).getFestival() != null &&
                                    AppInstance.rModel_newOffers.getRecentproducts().get(i).getFestival().length() > 0) {
                                festival = AppInstance.rModel_newOffers.getRecentproducts().get(i).getFestival();
                            }
                            String strDistance = AppInstance.rModel_newOffers.getRecentproducts().get(i).getDistance();
                            String[] parts = strDistance.split(Pattern.quote("."));
                            int dist = Integer.parseInt(parts[0]);

                            if (festival.length() > 0) {
                                lstFestivalByLocation.add(new DModelHomeGrdVw(AppInstance.rModel_newOffers.getRecentproducts().get(i).getImage(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getName(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getId(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getMerchantname(),
                                        festival,
                                        dist
                                ));
                                lstFestivalByAlphabetically.add(new DModelHomeGrdVw(AppInstance.rModel_newOffers.getRecentproducts().get(i).getImage(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getName(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getId(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getMerchantname(),
                                        festival,
                                        dist
                                ));
                            } else {
                                lstNewOffersByLocation.add(new DModelHomeGrdVw(AppInstance.rModel_newOffers.getRecentproducts().get(i).getImage(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getName(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getId(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getMerchantname(),
                                        festival,
                                        dist
                                ));
                                lstNewOffersByAlphabetically.add(new DModelHomeGrdVw(AppInstance.rModel_newOffers.getRecentproducts().get(i).getImage(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getName(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getId(),
                                        AppInstance.rModel_newOffers.getRecentproducts().get(i).getMerchantname(),
                                        festival,
                                        dist
                                ));
                            }
                        }
                    }
                    Collections.sort(lstFestivalByLocation, new Comparator<DModelHomeGrdVw>() {
                        @Override
                        public int compare(DModelHomeGrdVw o1, DModelHomeGrdVw o2) {
                            return Integer.valueOf(o1.getStrDistance()).compareTo(o2.getStrDistance()); // To compare string values
                        }
                    });

                    Collections.sort(lstFestivalByAlphabetically, new Comparator<DModelHomeGrdVw>() {
                        @Override
                        public int compare(DModelHomeGrdVw o1, DModelHomeGrdVw o2) {
                            return o1.getStrMerchantName().compareToIgnoreCase(o2.getStrMerchantName()); // To compare string values
                        }
                    });
                    Collections.sort(lstNewOffersByLocation, new Comparator<DModelHomeGrdVw>() {
                        @Override
                        public int compare(DModelHomeGrdVw o1, DModelHomeGrdVw o2) {
                            return Integer.valueOf(o1.getStrDistance()).compareTo(o2.getStrDistance()); // To compare string values
                        }
                    });
                    Collections.sort(lstNewOffersByAlphabetically, new Comparator<DModelHomeGrdVw>() {
                        @Override
                        public int compare(DModelHomeGrdVw o1, DModelHomeGrdVw o2) {
                            return o1.getStrMerchantName().compareToIgnoreCase(o2.getStrMerchantName()); // To compare string values
                        }
                    });
                    for (int i = 0; i < lstNewOffersByLocation.size(); i++) {
                        lstFestivalByLocation.add(new DModelHomeGrdVw(
                                lstNewOffersByLocation.get(i).getStrImgUrl(),
                                lstNewOffersByLocation.get(i).getStrOfferName(),
                                lstNewOffersByLocation.get(i).getStrProductId(),
                                lstNewOffersByLocation.get(i).getStrMerchantName(),
                                "",
                                lstNewOffersByLocation.get(i).getStrDistance()
                        ));
                        lstFestivalByAlphabetically.add(new DModelHomeGrdVw(
                                lstNewOffersByAlphabetically.get(i).getStrImgUrl(),
                                lstNewOffersByAlphabetically.get(i).getStrOfferName(),
                                lstNewOffersByAlphabetically.get(i).getStrProductId(),
                                lstNewOffersByAlphabetically.get(i).getStrMerchantName(),
                                "",
                                lstNewOffersByAlphabetically.get(i).getStrDistance()
                        ));
                    }


                    AppInstance.mlstNewOffers = new ArrayList<>();
                    AppInstance.mlstNewOffers = lstNewOffersByLocation;
                    AppPreference.setSetting(mContext, "key_new_offer_badge", "");

                    if (utilObj.isLocationEnabled(mActivity)) {
                        if (utilObj.checkPermission(getActivity())) {
                            updateBtnLocation();
                            favoritesAdapter = new FavoritesAdapter(mContext, lstFestivalByLocation, isSubscribed);
                            lsvNewOffers.setAdapter(favoritesAdapter);
                        } else {
                            updatebtnAlpabetical();
                            favoritesAdapter = new FavoritesAdapter(mContext, lstFestivalByAlphabetically, isSubscribed);
                            lsvNewOffers.setAdapter(favoritesAdapter);
                        }
                    } else {
                        updatebtnAlpabetical();
                        favoritesAdapter = new FavoritesAdapter(mContext, lstFestivalByAlphabetically, isSubscribed);
                        lsvNewOffers.setAdapter(favoritesAdapter);
                    }
                }
            }
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);

    }

    private void updatebtnAlpabetical() {
        rlLocation.setBackground(getResources().getDrawable(R.drawable.gray_background_border));
        txvLocation.setTextColor(Color.BLACK);
        rlAlphabetically.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));
        txvAlphabetically.setTextColor(Color.WHITE);
        isLocationSort = false;
    }

    private void updateBtnLocation() {
        rlAlphabetically.setBackground(getResources().getDrawable(R.drawable.gray_background_border));
        txvAlphabetically.setTextColor(Color.BLACK);
        rlLocation.setBackground(getResources().getDrawable(R.drawable.orange_gadient_rectngle_rounded));
        txvLocation.setTextColor(Color.WHITE);
        isLocationSort = true;

    }

    public void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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
                    AppInstance.rModel_newOffers = null;
                    lstNewOffersByLocation = new ArrayList<>();
                    lstNewOffersByAlphabetically = new ArrayList<>();
                    if (NetworkUtils.isConnected(mContext)) {
                        utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                        doUpdateLocationPermission();
                        homeManager.doFetchNewOffers();
                    } else {
                        utilObj.showToast(mContext, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }
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

    private void doUpdateLocationPermission() {
        mMerchantManager.doUpdatePermissions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.IntentPreference.PACKAGE_LOCATION_INTENT_CODE == requestCode) {
            if (utilObj.checkPermission(getActivity())) {

                updateBtnLocation();
                AppInstance.rModel_newOffers = null;
                lstNewOffersByLocation = new ArrayList<>();
                lstNewOffersByAlphabetically = new ArrayList<>();
                if (NetworkUtils.isConnected(mContext)) {
                    utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                    doUpdateLocationPermission();
                    homeManager.doFetchNewOffers();
                } else {
                    utilObj.showToast(mContext, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_LONG);
                }
            }
        } else if (Constants.IntentPreference.SOURCE_LOCATION_INTENT_CODE == requestCode) {

            if (utilObj.isLocationEnabled(mActivity)) {
                if (!utilObj.checkPermission(getActivity())) {
                    requestPermission();
                } else {
                    updateBtnLocation();
                    AppInstance.rModel_newOffers = null;
                    lstNewOffersByLocation = new ArrayList<>();
                    lstNewOffersByAlphabetically = new ArrayList<>();
                    if (NetworkUtils.isConnected(mContext)) {
                        utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                        doUpdateLocationPermission();
                        homeManager.doFetchNewOffers();
                    } else {
                        utilObj.showToast(mContext, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }
                }
            } else {
            }
        }

    }
}
