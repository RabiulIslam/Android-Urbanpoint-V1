package com.urbanpoint.UrbanPoint.views.fragments.HomeFragments;


import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
public class FavoritesFragment extends Fragment implements View.OnClickListener, ServiceRedirection {
    private FragmentActivity mActivity;
    private FragmentManager frgMngr;
    private Context mContext;
    private View mRootView;
    private Utility utilObj;
    private ImageView mBackButton;
    private ListView lsvFavorites;
    private List<DModelHomeGrdVw> lstFavoritesByLocation, lstFavoritesByAlphabetically;
    private FavoritesAdapter favoritesAdapter;
    private RelativeLayout rlAlphabetically, rlLocation;
    private TextView txvLocation, txvAlphabetically;
    private HomeManager homeManager;
    private MerchantManager mMerchantManager;
    private boolean isLocationSort;
    private int mSelectedPosition;
    private boolean isSubscribed;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Typeface novaThin, novaRegular;
    private CustomDialogConfirmationInterfaces contextualDialogConfirmationInterfacesLocation;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, null);

        this.mActivity = getActivity();
        this.mContext = mActivity.getApplicationContext();
        this.mRootView = view;
//        mActivity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initialize();


        if (NetworkUtils.isConnected(mContext)) {
            utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
            homeManager.doFetchFavOffers();
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }

        lsvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("favoritesa", "onItemClick: " + position);
                String custID;
                String productid;
                String itemName;
                custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");

                if (isLocationSort) {
                    productid = lstFavoritesByLocation.get(position).getStrProductId();
                    itemName = lstFavoritesByLocation.get(position).getStrOfferName();
                } else {
                    productid = lstFavoritesByAlphabetically.get(position).getStrProductId();
                    itemName = lstFavoritesByAlphabetically.get(position).getStrOfferName();
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

        lsvFavorites.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final CustomDialogConfirmationInterfaces customDialogConfirmationInterfaces = new CustomDialogConfirmationInterfaces() {
                    @Override
                    public void callConfirmationDialogPositive() {
                        String productid;
                        mSelectedPosition = position;
                        if (isLocationSort) {
                            productid = lstFavoritesByLocation.get(position).getStrProductId();
                        } else {
                            productid = lstFavoritesByAlphabetically.get(position).getStrProductId();
                        }
                        Log.d("asdsa", "callConfirmationDialogPositive: " + position);
                        homeManager.doRemoveFavOffer(productid);
                    }

                    @Override
                    public void callConfirmationDialogNegative() {
                        Log.d("asdsa", "callConfirmationDialogNegative: " + position);
                    }
                };
                utilObj.setIsCustomAlertDialogCancelable(false);
                utilObj.showCustomAlertDialog(mActivity, getContext().getResources().getString(R.string.header_my_faves), getContext().getResources().getString(R.string.remove_faves_confirm_message), getContext().getResources().getString(R.string.yes), getContext().getResources().getString(R.string.no), true, customDialogConfirmationInterfaces);
                return true;
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
        setActionBar("Favorites", false);
        isLocationSort = true;
        isSubscribed = AppPreference.getSettingResturnsBoolean(mActivity, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
        lstFavoritesByLocation = new ArrayList<>();
        lstFavoritesByAlphabetically = new ArrayList<>();
        bindViews();
    }

    private void bindViews() {
        lsvFavorites = (ListView) mRootView.findViewById(R.id.frg_fav_lst_view);
        txvAlphabetically = (TextView) mRootView.findViewById(R.id.frg_fav_txv_alphabetically);
        txvLocation = (TextView) mRootView.findViewById(R.id.frg_fav_txv_location);
        rlAlphabetically = (RelativeLayout) mRootView.findViewById(R.id.frg_fav_rl_alphabetically);
        rlAlphabetically.setOnClickListener(this);
        rlLocation = (RelativeLayout) mRootView.findViewById(R.id.frg_fav_rl_location);
        rlLocation.setOnClickListener(this);
        txvAlphabetically.setTypeface(novaRegular);
        txvLocation.setTypeface(novaRegular);
        favoritesAdapter = new FavoritesAdapter(mContext, lstFavoritesByLocation, isSubscribed);
        lsvFavorites.setAdapter(favoritesAdapter);
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
        Animation animation = AnimationUtils.loadAnimation(getContext(),
                R.anim.right_in);
        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getActionBar().setTitle(title);
        View customView = getActivity().getLayoutInflater().inflate(R.layout.action_bar_offer_main, null);
        TextView title1 = (TextView) customView.findViewById(R.id.textViewTitle);
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
        title1.setTypeface(novaRegular);
        getActivity().getActionBar().setCustomView(customView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_fav_rl_alphabetically:
//                if (lstFavoritesByLocation.size() > 0) {
                updatebtnAlpabetical();

                favoritesAdapter = new FavoritesAdapter(mContext, lstFavoritesByAlphabetically, isSubscribed);
                lsvFavorites.setAdapter(favoritesAdapter);
//                } else {
//                    utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.no_fav_marked), null, null, false, null);
//                }
                break;
            case R.id.frg_fav_rl_location:
//                if (lstFavoritesByAlphabetically.size() > 0) {
                if (!utilObj.isLocationEnabled(mActivity) || !utilObj.checkPermission(getActivity())) {
                    utilObj.showContextualAlertDialog(mActivity, contextualDialogConfirmationInterfacesLocation);
                } else {
                    updateBtnLocation();

                    favoritesAdapter = new FavoritesAdapter(mContext, lstFavoritesByLocation, isSubscribed);
                    lsvFavorites.setAdapter(favoritesAdapter);
                }
//                } else {
//                    utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.no_fav_marked), null, null, false, null);
//                }
                break;
        }
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();
        if (taskID == Constants.TaskID.FETCH_FAV_OFFERS_TASK_ID) {
            if (AppInstance.rModel_favOffers != null) {
                if (AppInstance.rModel_favOffers.getData().size() > 0) {
                    for (int i = 0; i < AppInstance.rModel_favOffers.getData().size(); i++) {
//                        String festival = "";
//                        if (AppInstance.rModel_favOffers.getData().get(i).getFestival() != null &&
//                                AppInstance.rModel_favOffers.getRecentproducts().get(i).getFestival().length() > 0) {
//                            festival = AppInstance.rModel_favOffers.getRecentproducts().get(i).getFestival();
//                        }
//                        if (AppInstance.rModel_favOffers.getData().get(i).getStatus().equalsIgnoreCase("active")) {

                        String strDistance = AppInstance.rModel_favOffers.getData().get(i).getDistance() + "";
                        String[] parts = strDistance.split(Pattern.quote("."));
                        int dist = Integer.parseInt(parts[0]);

                        lstFavoritesByLocation.add(new DModelHomeGrdVw(AppInstance.rModel_favOffers.getData().get(i).getImage(),
                                AppInstance.rModel_favOffers.getData().get(i).getProductname(),
                                AppInstance.rModel_favOffers.getData().get(i).getProductid(),
                                AppInstance.rModel_favOffers.getData().get(i).getMerchantname(),
                                "",
                                dist));
                        lstFavoritesByAlphabetically.add(new DModelHomeGrdVw(AppInstance.rModel_favOffers.getData().get(i).getImage(),
                                AppInstance.rModel_favOffers.getData().get(i).getProductname(),
                                AppInstance.rModel_favOffers.getData().get(i).getProductid(),
                                AppInstance.rModel_favOffers.getData().get(i).getMerchantname(),
                                "",
                                dist));
//                    }

                    }

                    Collections.sort(lstFavoritesByAlphabetically, new Comparator<DModelHomeGrdVw>() {
                        @Override
                        public int compare(DModelHomeGrdVw o1, DModelHomeGrdVw o2) {
                            return o1.getStrMerchantName().compareToIgnoreCase(o2.getStrMerchantName()); // To compare string values
                        }
                    });
                    Collections.sort(lstFavoritesByLocation, new Comparator<DModelHomeGrdVw>() {
                        @Override
                        public int compare(DModelHomeGrdVw o1, DModelHomeGrdVw o2) {
                            return Integer.valueOf(o1.getStrDistance()).compareTo(o2.getStrDistance()); // To compare string values
                        }
                    });


                    AppInstance.mlstNewOffers = new ArrayList<>();
                    AppInstance.mlstNewOffers = lstFavoritesByLocation;

                    if (utilObj.isLocationEnabled(mActivity)) {
                        if (utilObj.checkPermission(getActivity())) {
                            updateBtnLocation();
                            favoritesAdapter = new FavoritesAdapter(mContext, lstFavoritesByLocation, isSubscribed);
                            lsvFavorites.setAdapter(favoritesAdapter);
                        } else {
                            updatebtnAlpabetical();
                            favoritesAdapter = new FavoritesAdapter(mContext, lstFavoritesByAlphabetically, isSubscribed);
                            lsvFavorites.setAdapter(favoritesAdapter);
                        }
                    } else {
                        updatebtnAlpabetical();
                        favoritesAdapter = new FavoritesAdapter(mContext, lstFavoritesByAlphabetically, isSubscribed);
                        lsvFavorites.setAdapter(favoritesAdapter);
                    }
                }
            }
        } else if (taskID == Constants.TaskID.REMOVE_FAV_OFFER_TASK_ID) {
            utilObj.stopiOSLoader();

            if (isLocationSort) {
                lstFavoritesByLocation.remove(mSelectedPosition);
                favoritesAdapter.notifyDataSetChanged();
            } else {
                lstFavoritesByAlphabetically.remove(mSelectedPosition);
                favoritesAdapter.notifyDataSetChanged();
            }
            utilObj.showToast(mContext, AppInstance.removeFromWishlist.getMessage(), Toast.LENGTH_LONG);

        }

    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
//        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
        utilObj.showCustomAlertDialog(mActivity, null, getResources().getString(R.string.no_fav_marked), null, null, false, null);

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

    private void doUpdateLocationPermission() {
        mMerchantManager.doUpdatePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("aserwqer", "onRequestPermissionsResult: " + requestCode);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                //was crashing here
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    if (GPSTracker.gpsTracker != null) {
                        GPSTracker.gpsTracker = null;
                    }
                    AppInstance.rModel_favOffers = null;
                    lstFavoritesByLocation = new ArrayList<>();
                    lstFavoritesByAlphabetically = new ArrayList<>();
                    if (NetworkUtils.isConnected(mContext)) {
                        utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                        doUpdateLocationPermission();
                        homeManager.doFetchFavOffers();
                    } else {
                        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }

                } else {
                    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        Log.d("aserwqer", "onRequestPermissionsResult----: " + requestCode);
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
                AppInstance.rModel_favOffers = null;
                lstFavoritesByLocation = new ArrayList<>();
                lstFavoritesByAlphabetically = new ArrayList<>();
                if (NetworkUtils.isConnected(mContext)) {
                    utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                    doUpdateLocationPermission();
                    homeManager.doFetchFavOffers();
                } else {
                    utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                }
            }
        } else if (Constants.IntentPreference.SOURCE_LOCATION_INTENT_CODE == requestCode) {

            if (utilObj.isLocationEnabled(mActivity)) {
                if (!utilObj.checkPermission(getActivity())) {
                    requestPermission();
                } else {
                    updateBtnLocation();
                    AppInstance.rModel_favOffers = null;
                    lstFavoritesByLocation = new ArrayList<>();
                    lstFavoritesByAlphabetically = new ArrayList<>();
                    if (NetworkUtils.isConnected(mContext)) {
                        utilObj.startiOSLoader(mActivity, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                        doUpdateLocationPermission();
                        homeManager.doFetchFavOffers();
                    } else {
                        utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                    }
                }
            } else {
                Log.d("sadSDSAASD", "onCreateView:5520 ");
            }
        }
    }
}
