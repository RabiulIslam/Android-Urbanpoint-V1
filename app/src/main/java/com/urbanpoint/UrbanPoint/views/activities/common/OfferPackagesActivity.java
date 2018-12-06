package com.urbanpoint.UrbanPoint.views.activities.common;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.merge.MergeAdapter;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.adapters.offerPackagesSubscriptions.OfferPackagesAdapter;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.OfferPackagesItems;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.OfferPackagesItemsDetails;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.categoryScreens.OfferManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.ExpandableHeightListView;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;
import com.urbanpoint.UrbanPoint.views.activities.common.paymentSelectionType.PaymentProcessVodafoneSelectionActivity;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OfferPackagesActivity extends Activity implements ServiceRedirection, View.OnClickListener {

    private Context mContext;
    private Utility utilObj;
    private OfferManager mOfferManager;

    private ImageView mBackButton;

    private MergeAdapter mMergedAdapter;
    private ExpandableHeightListView mOfferPackagesDataListView;
    private LayoutInflater mLayoutInflater;
    private ArrayList<OfferPackagesItemsDetails> mAllPackages;
    private Button mGoToPayment;
    private String mSelectedOfferDollarValue = null;
    private int mOfferPackageID;
    private OfferPackagesItems mOfferPackagesItems;
    private TextView mHeaderOfferPackageMessage;
    private String mSelectedOfferName;
    private OfferPackagesAdapter offerListAdapters;
    private int mSelectedItem;
    private Typeface novaThin, novaRegular;
    private LinearLayout llUpperUi, llBottomUi;
    private Button btnSubscribe;
    private TextView txvFree1stMonth, txvPromotional, txvCreditCard, txvPhoneBill;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_packages);
        mContext = getApplicationContext();
        this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        mLayoutInflater = getLayoutInflater();

        AppInstance.offerPackagesItems = null;
        initViews();
    }

    private void initViews() {
        mOfferManager = new OfferManager(mContext, this);
        utilObj = new Utility(mContext);
        this.novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        this.novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        bindViews();
        mSelectedItem = -1;
        MyApplication.getInstance().trackScreenView(getString(R.string.offer_package_screen));
        doCallOfferPackages();
    }

    private void bindViews() {
        setActionBar(getString(R.string.member_packages), false);

        txvCreditCard = (TextView) findViewById(R.id.offerPackageCreditCardText);
        txvPromotional = (TextView) findViewById(R.id.offerPackagePromotionText);
        txvPhoneBill = (TextView) findViewById(R.id.offerPackagePhoneText);
        txvCreditCard.setTypeface(novaRegular);
        txvPromotional.setTypeface(novaRegular);
        txvPhoneBill.setTypeface(novaRegular);
        txvFree1stMonth = (TextView) findViewById(R.id.freeofferTxv);
        btnSubscribe = (Button) findViewById(R.id.act_offr_new_pakages_btn_subscribe);
        btnSubscribe.setOnClickListener(this);
        llUpperUi = (LinearLayout) findViewById(R.id.mainLayoutUpper);
        llBottomUi = (LinearLayout) findViewById(R.id.mainLayoutBottom);
        mGoToPayment = (Button) findViewById(R.id.goToPayment);
        mGoToPayment.setOnClickListener(this);
        mOfferPackagesDataListView = (ExpandableHeightListView) findViewById(R.id.offerPackagesDataListView);
        mOfferPackagesDataListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        long no = AppPreference.getSettingResturnsLong(mContext, Constants.DEFAULT_VALUES.GAIN_ACCESS_BTN_STATUS, 4);

//        if (no == 3) {
//            Log.d("dfsfdfasf", "bindViews: V ");
//            txvFree1stMonth.setVisibility(View.VISIBLE);
//        } else {
//            Log.d("dfsfdfasf", "bindViews: G ");
//            txvFree1stMonth.setVisibility(View.INVISIBLE);
//        }

//        setHeader();
//        setFooter();

        mOfferPackagesDataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAllPackages != null && mAllPackages.size() > 0) {
                    //--------TODO : HARDCODED ID , AS PER implementation in iOS
                    mOfferPackageID = position + 1;

                    mSelectedItem = position;
                    mOfferPackagesDataListView.setExpanded(true);
                    offerListAdapters = new OfferPackagesAdapter(mContext, mSelectedItem, android.R.layout.simple_list_item_single_choice, mAllPackages);
                    mOfferPackagesDataListView.setAdapter(offerListAdapters);
                    //--------
                    Log.d("PAKAGES", "fragment position: " + mSelectedItem);

                    MyApplication.getInstance().printLogs("position", "" + position);
                    OfferPackagesItemsDetails packagesItemsDetails = mAllPackages.get(position);
                    mSelectedOfferDollarValue = packagesItemsDetails.getDollarValue();

                    mSelectedOfferName = packagesItemsDetails.getOffer().replace("/", "-");
                    MyApplication.getInstance().printLogs("mSelectedOfferDollarValue", mSelectedOfferDollarValue);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //doCallOfferPackages();
    }

    private void doCallOfferPackages() {

        mOfferPackagesItems = AppInstance.offerPackagesItems;

        if (mOfferPackagesItems != null) {

//            if (mOfferPackagesItems != null) {
//                String formattedString = getString(R.string.offer_packages_messages);
//                String replace = formattedString.replace(Constants.DEFAULT_VALUES.REPLACE_STRING_CHARS, mOfferPackagesItems.getLimitedOfferText());
//                MyApplication.getInstance().printLogs("setHeader : PKG ", replace);
//                mHeaderOfferPackageMessage.setText(Html.fromHtml(replace));
//            }

            setAdapterData();

        } else {
            if (NetworkUtils.isConnected(mContext)) {
                utilObj.startiOSLoader(this, R.drawable.image_for_rotation, getString(R.string.please_wait), true);
                mOfferManager.doGetOfferedPackages();
            } else {
                utilObj.showToast(mContext, getString(R.string.no_internet), Toast.LENGTH_LONG);
            }
        }

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
    public void onSuccessRedirection(int taskID) {
        utilObj.stopiOSLoader();

        if (taskID == Constants.TaskID.GET_OFFERED_PACKAGES_LIST_TASK_ID) {

            mOfferPackagesItems = AppInstance.offerPackagesItems;
            if (mOfferPackagesItems != null) {

                if (mOfferPackagesItems != null) {
//                    String formattedString = getString(R.string.offer_packages_messages);
//                    if (!(Constants.BLANK.equalsIgnoreCase(mOfferPackagesItems.getLimitedOfferText()))) {
//                        String replace = formattedString.replace(Constants.DEFAULT_VALUES.REPLACE_STRING_CHARS, mOfferPackagesItems.getLimitedOfferText());
//                        MyApplication.getInstance().printLogs("setHeader : PKG ", replace);
//                        mHeaderOfferPackageMessage.setText(Html.fromHtml(replace));
//                    } else {
//                        String replace = formattedString.replace(Constants.DEFAULT_VALUES.REPLACE_STRING_CHARS, mOfferPackagesItems.getLimitedOfferText());
//                        replace = replace.replace(getString(R.string.limited_time_offer_text), "");
//                        mHeaderOfferPackageMessage.setText(Html.fromHtml(replace));
//                        MyApplication.getInstance().printLogs("setHeader : PKG ", replace);
//                    }
                }

                setAdapterData();

            }
        } else if (taskID == Constants.TaskID.CHECK_PROMO_CODE_IS_VALID) {

        } else {
            utilObj.showToast(this, getResources().getString(R.string.invalid_message), Toast.LENGTH_LONG);
        }

    }

    @Override
    public void onFailureRedirection(String errorMessage) {
        utilObj.stopiOSLoader();
        utilObj.showToast(this, errorMessage, 1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.goToPayment:
                if (mSelectedOfferDollarValue == null) {
                    utilObj.showCustomAlertDialog(this, null, getString(R.string.offer_packages_select_offer_error), null, null, false, null);
                } else {
                    //Intent intentObj = new Intent(OfferPackagesActivity.this, PaymentProcessActivity.class);
                    //Intent intentObj = new Intent(OfferPackagesActivity.this, PaymentProcessSelectionActivity.class);
                    MyApplication.getInstance().trackEvent(getString(R.string.ga_event_category_offer_packages_screen), getString(R.string.ga_event_action_offer_packages_screen) + "/" + mSelectedOfferName, getResources().getString(R.string.ga_event_label_offer_packages_screen));
                    Intent intentObj = new Intent(OfferPackagesActivity.this, PaymentProcessActivity.class);
                    intentObj.putExtra(Constants.Request.AMOUNT, mSelectedOfferDollarValue);
                    intentObj.putExtra(Constants.Request.NAME, mSelectedOfferName);
                    intentObj.putExtra(Constants.Request.OFFER, "" + mOfferPackageID);
                    startActivity(intentObj);
                }
                break;
            case R.id.act_offr_new_pakages_btn_subscribe:
                Intent intentObj = new Intent(OfferPackagesActivity.this, PaymentProcessVodafoneSelectionActivity.class);
                startActivity(intentObj);
                this.finish();
                break;
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    /*private void createAdapterViews(OfferPackagesItems offerPackagesItems) {
        mMergedAdapter = new MergeAdapter();

        //----------------
        View mInflateHeader = mLayoutInflater.inflate(R.layout.item_offers_packages_header_message, null);
        TextView offerPackageMessage = (TextView) mInflateHeader.findViewById(R.id.offerPackageMessage);
        offerPackageMessage.setText(Html.fromHtml(getString(R.string.offer_packages_messages)));
        mMergedAdapter.addView(mInflateHeader, true);
        //----------------

        View mInflateDiscountCode = mLayoutInflater.inflate(R.layout.item_offers_packages_discount_code, null);
        mMergedAdapter.addView(mInflateDiscountCode, true);
        //----------------

        OfferPackagesAdapter offerListAdapters = new OfferPackagesAdapter(mContext, R.layout.fragment_listview, offerPackagesItems.getAllPackages());
        mMergedAdapter.addAdapter(offerListAdapters);

       *//* View mInflateListView = mLayoutInflater.inflate(R.layout.item_offers_packages_listview, null);
        ListView offerListView = (ListView) mInflateListView.findViewById(R.id.list);
        OfferPackagesAdapter offerListAdapters = new OfferPackagesAdapter(mContext, R.layout.fragment_listview, offerPackagesItems.getAllPackages());
        offerListView.setAdapter(offerListAdapters);

        mMergedAdapter.addView(mInflateListView, true);*//*

        //-------------------

        View mInflateFooter = mLayoutInflater.inflate(R.layout.item_offers_packages_footer_message, null);
        TextView offerPackageFooterMessage = (TextView) mInflateFooter.findViewById(R.id.offerPackageFooterMessage);
        offerPackageFooterMessage.setText(Html.fromHtml(getString(R.string.offer_packages_footer_messages)));
        mMergedAdapter.addView(mInflateFooter, true);
        //---------------

        mOfferPackagesDataListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mOfferPackagesDataListView.setAdapter(mMergedAdapter);

    }*/

//    private void setHeader() {
//        //----------------
//        View mInflateHeader = mLayoutInflater.inflate(R.layout.item_offers_packages_header_message, null);
//        mHeaderOfferPackageMessage = (TextView) mInflateHeader.findViewById(R.id.offerPackageMessage);
//        final EditText enteredDiscountCode = (EditText) mInflateHeader.findViewById(R.id.discountCode);
//        Button applyCodeButton = (Button) mInflateHeader.findViewById(R.id.applyCodeButton);
//
//        applyCodeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String code = enteredDiscountCode.getText().toString();
//                if (code.trim().length() == 0) {
//                    utilObj.showCustomAlertDialog(OfferPackagesActivity.this, null, OfferPackagesActivity.this.getString(R.string.offer_packages_discount_code_empty), null, null, false, null);
//                } else {
//
//                }
//            }
//        });
//
//
//        mOfferPackagesDataListView.addHeaderView(mInflateHeader);
//        //----------------
//    }

//    private void setFooter() {
//        View mInflateFooter = mLayoutInflater.inflate(R.layout.item_offers_packages_footer_message, null);
//        TextView offerPackageFooterMessage = (TextView) mInflateFooter.findViewById(R.id.offerPackageFooterMessage);
//        // TextView offerPackageFooterMessage = (TextView) findViewById(R.id.offerPackageFooterMessage);
//        offerPackageFooterMessage.setText(Html.fromHtml(getString(R.string.offer_packages_footer_messages)));
//        mOfferPackagesDataListView.addFooterView(mInflateFooter);
//    }

    private void setAdapterData() {
//        mAllPackages = mOfferPackagesItems.getAllPackages();
        llUpperUi.setVisibility(View.VISIBLE);
        llBottomUi.setVisibility(View.VISIBLE);
        ArrayList<OfferPackagesItemsDetails> allPackages = mOfferPackagesItems.getAllPackages();
        // Added to remove year subscription
//        allPackages.remove(2);

        mAllPackages = new ArrayList<OfferPackagesItemsDetails>();
        mAllPackages.addAll(allPackages);
        Log.d("PAKAGES", "fragment position2: " + mSelectedItem);
        mOfferPackagesDataListView.setExpanded(true);
        offerListAdapters = new OfferPackagesAdapter(mContext, mSelectedItem, android.R.layout.simple_list_item_single_choice, mAllPackages);
        mOfferPackagesDataListView.setAdapter(offerListAdapters);
    }

}
