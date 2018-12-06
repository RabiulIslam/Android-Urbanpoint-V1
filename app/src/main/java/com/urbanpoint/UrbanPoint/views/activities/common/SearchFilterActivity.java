package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.adapters.SearchFilterAdapter.SearchFilterAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.SearchFilter.FilteredSearchItem;
import com.urbanpoint.UrbanPoint.dataobject.SearchFilter.LookingForFilterList;
import com.urbanpoint.UrbanPoint.dataobject.SearchFilter.LookingForFilterListItem;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.appLogin.SearchFilterManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aparnasalve on 9/3/16.
 */
public class SearchFilterActivity extends Activity implements ServiceRedirection, View.OnClickListener {
    private Context mContext;
    private Utility utilObj;
    String page = "1";
    String searchCount = "0";
    String mSearch_tag, mCustomerid;
    private ImageView mBackButton;
    EditText mSearchText;
    ListView mFilterListView;
    SearchFilterManager searchFilterManager;
    TextView mFilterError;
    private Button mSearchbtnImage;
    private ImageView btnClear;
    Button mSearchButton;
    private List<FilteredSearchItem> lstSearchfilter;
    private LinearLayout mMainParentLayout;
    private LinearLayout mSearchBarParentLayout;
    private LinearLayout mBtnClearParentLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looking_for_filter);
        mContext = getApplicationContext();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

        initViews();
        MyApplication.getInstance().trackScreenView(getString(R.string.search_screen));
    }

    private void initViews() {

        utilObj = new Utility(mContext);

        lstSearchfilter = new ArrayList<>();
        searchFilterManager = new SearchFilterManager(mContext, this);
        Bundle m_buBundle = getIntent().getExtras();
        if (m_buBundle != null) {
            mCustomerid = m_buBundle.getString(Constants.Request.CUSTOMER_ID);
            mSearch_tag = m_buBundle.getString(Constants.Request.SEARCH_TAG);
        }
        setActionBar(getString(R.string.search_results), false);
        bindViews();
    }

    private void bindViews() {

        mSearchBarParentLayout = (LinearLayout) findViewById(R.id.searchBarParentLayout);
        mBtnClearParentLayout = (LinearLayout) findViewById(R.id.btnClearParentLayout);

        mMainParentLayout = (LinearLayout) findViewById(R.id.mainParentLayout);
        mMainParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                utilObj.keyboardClose(mContext, v);
                mSearchBarParentLayout.setGravity(Gravity.CENTER);
                mSearchText.setCursorVisible(false);
                if (mSearchText.length() == 0) {
                    mBtnClearParentLayout.setVisibility(View.GONE);
                } else {
                    mBtnClearParentLayout.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        mSearchText = (EditText) findViewById(R.id.searchEditText);
        utilObj.keyboardClose(mContext, mSearchText);

        // for searchEditText
        mSearchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSearchBarParentLayout.setGravity(Gravity.LEFT);
                mSearchText.setCursorVisible(true);
                return false;
            }
        });
        // for main layout of searchEditText
        mSearchBarParentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSearchBarParentLayout.setGravity(Gravity.LEFT);
                mSearchText.setCursorVisible(true);
                mSearchText.requestFocus();
                utilObj.showVirtualKeyboard(mContext);
                return false;
            }
        });

        mSearchButton = (Button) findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(this);

        //  mSearchbtnImage = (Button) findViewById(R.id.searchBtnImage);

        mFilterListView = (ListView) findViewById(R.id.listView_filter);
        mFilterError = (TextView) findViewById(R.id.txtView_filterError);
        btnClear = (ImageView) findViewById(R.id.btn_clear);

        mSearchText.setText(mSearch_tag);

        if (mSearch_tag != null) {
            if (mSearch_tag.length() > 0) {
                mSearchText.setSelection(mSearch_tag.length());
                btnClear.setVisibility(View.VISIBLE);
                mBtnClearParentLayout.setVisibility(View.VISIBLE);
            }
        }

        mSearchText.addTextChangedListener(textWatcher());
        btnClear.setOnClickListener(this);

        // To search text on click of keyboard search icon
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchEnteredText();
                    return true;
                }
                return false;
            }
        });


        mFilterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (AppInstance.lookingForFilterList != null) {
                    LookingForFilterList lookingForFilterList = AppInstance.lookingForFilterList;
                    if (lstSearchfilter != null && lstSearchfilter.size() > 0) {
                        String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                        //String productid=AppPreference.getSetting(mContext,Constants.Request.OFFER_ID,"");
                        String productid = lstSearchfilter.get(position).getId();
                        String itemName = lstSearchfilter.get(position).getName();
                        Bundle m_bundle = new Bundle();
                        m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
                        m_bundle.putString(Constants.Request.OFFER_ID, productid);
                        m_bundle.putString(Constants.Request.NAME, itemName);

//                String lockOffersStatusFlag = lookingForFilterList.getFlag();
//                m_bundle.putString(Constants.OfferDetails.OFFER_LOCK, lockOffersStatusFlag);

//                        if (lstSearchfilter != null) {
//                            String lockOffersStatusFlag = lookingForFilterList.isOfferGetsLocked(mContext); //AppInstance.offers.getLockOffersStatusFlag();
//                            m_bundle.putString(Constants.OfferDetails.OFFER_LOCK, lockOffersStatusFlag);
//                        }

                        Intent intent = new Intent(SearchFilterActivity.this, OfferDetailActivity.class);
                        intent.putExtras(m_bundle);
                        startActivity(intent);
                    }
                }
            }
        });

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
                AppInstance.offerPackagesItems = null;

            }
        });

        title1.setText(title);
        getActionBar().setCustomView(customView);
    }


    @Override
    public void onResume() {
        super.onResume();
        //doFetchSearchData();
    }

    private void doFetchSearchData() {
        if (AppInstance.lookingForFilterList == null) {
            if (NetworkUtils.isConnected(mContext)) {
                utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                searchFilterManager.doGetFilterList(mSearch_tag);
            } else {
                utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear: {
                mBtnClearParentLayout.setVisibility(View.INVISIBLE);
                //mSearchBarParentLayout.setGravity(Gravity.CENTER);
                mSearchText.setText(""); //clear edittext
                break;
            }
            case R.id.searchButton: {
                if (mSearchText.getText().length() > 0) {

                    searchEnteredText();
                } else {
                    utilObj.showToast(mContext, mContext.getString(R.string.invalid_search_field), Toast.LENGTH_LONG);

                }
                break;
            }

        }

    }

    private void searchEnteredText() {
        if (NetworkUtils.isConnected(mContext)) {
            utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
            mSearch_tag = "" + mSearchText.getText().toString();
            MyApplication.getInstance().trackEvent(getString(R.string.ga_search), getString(R.string.ga_keyword) + mSearch_tag, getString(R.string.ga_search));
            searchFilterManager.doGetFilterList(mSearch_tag);
        } else {
            utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        mSearchText.setCursorVisible(false);
        if (taskID == Constants.TaskID.GET_FILTER_LIST_TASK_ID) {

            if (AppInstance.lookingForFilterList != null) {
                if (!Constants.DEFAULT_VALUES.ZERO.equalsIgnoreCase(AppInstance.lookingForFilterList.getStatus())) {
                    lstSearchfilter = new ArrayList<>();
                    mFilterListView.setVisibility(View.VISIBLE);
                    mFilterError.setVisibility(View.GONE);
                    String lockOffersStatusFlag = AppInstance.lookingForFilterList.isOfferGetsLocked(mContext);
                    if (AppInstance.lookingForFilterList != null &&
                            AppInstance.lookingForFilterList.getLookingForFilterListItems().size() > 0) {
                        for (int i = 0; i < AppInstance.lookingForFilterList.getLookingForFilterListItems().size(); i++) {
                            if (AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getStatus().equalsIgnoreCase("active")) {
                                lstSearchfilter.add(new FilteredSearchItem(
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getId(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getSku(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getMerchantaddress(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getProductname(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getCategory(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getPrice(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getSpecialprice(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getDiscount(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getMerchant_id(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getImage(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getMerchantname(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getSecond_name(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getMerchant_name(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getRating(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getProducts_count(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getStatus(),
                                        AppInstance.lookingForFilterList.getLookingForFilterListItems().get(i).getFestival()
                                        ));
                            }
                        }
                    }

//                        SearchFilterAdapter searchFilterAdapter = new SearchFilterAdapter(mContext, R.layout.fragment_listview, AppInstance.lookingForFilterList.getActiveOffers(), lockOffersStatusFlag);
                    SearchFilterAdapter searchFilterAdapter = new SearchFilterAdapter(mContext, R.layout.fragment_listview, lstSearchfilter, lockOffersStatusFlag);
                    mFilterListView.setAdapter(searchFilterAdapter);
                    searchFilterAdapter.notifyDataSetChanged();

                    MyApplication.getInstance().trackEvent(getString(R.string.ga_search), mSearch_tag + ":" + getString(R.string.ga_search_found), getString(R.string.ga_search));

                } else {
                    MyApplication.getInstance().trackEvent(getString(R.string.ga_search), mSearch_tag + ":" + getString(R.string.ga_search_not_found), getString(R.string.ga_search));
                    mFilterError.setVisibility(View.VISIBLE);
                    mFilterListView.setVisibility(View.GONE);
                    //utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
                }
            } else {
                mFilterError.setVisibility(View.VISIBLE);
                mFilterListView.setVisibility(View.GONE);
                utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
            }
        } else {
            mFilterError.setVisibility(View.VISIBLE);
            mFilterListView.setVisibility(View.GONE);
            //utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        mSearchText.setCursorVisible(false);

        utilObj.showToast(mContext, errorMessage, Toast.LENGTH_LONG);
    }


    private TextWatcher textWatcher() {
        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!mSearchText.getText().toString().equals("")) { //if edittext include text
                    btnClear.setVisibility(View.VISIBLE);
                    mBtnClearParentLayout.setVisibility(View.VISIBLE);
                    mSearchText.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                    // mSearchbtnImage.setVisibility(View.GONE);
                } else { //not include text
                    btnClear.setVisibility(View.GONE);
                    mBtnClearParentLayout.setVisibility(View.GONE);
                    mSearchText.setGravity(Gravity.CENTER);
                    /*if (mSearchText.getText().toString().equalsIgnoreCase("")) {
                        mSearchbtnImage.setVisibility(View.VISIBLE);
                    } else {
                        mSearchbtnImage.setVisibility(View.GONE);
                    }*/
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
