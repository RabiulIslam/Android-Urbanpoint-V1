package com.urbanpoint.UrbanPoint.adapters.categoryScreensAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.DModelMerchintList;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.common.MerchantDetailActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Ibrar on 1/26/2017.
 */

public class ExpandableMerchintListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    //    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
//    private HashMap<String, List<String>> _listDataChild;
    ArrayList<DModelMerchintList> lstFoodnDrinks = new ArrayList<>();
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private Typeface novaThin, novaRegular;
    private String strPromotionalPermision;
    private String festivalRamdan;
    private String festivalBurger;
    private String festivalBiryani;
    private Utility utilObj;
    private boolean isSubscribed;
    private String mCatID;

    public ExpandableMerchintListAdapter(Context context, ArrayList<DModelMerchintList> _lstFoodnDrinks, String _mCatID) {
        this.mContext = context;
        this.lstFoodnDrinks = _lstFoodnDrinks;
        this.novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        this.novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        this.strPromotionalPermision = AppPreference.getSetting(mContext, "key_Promotion_acessed", "");
        this.isSubscribed = AppPreference.getSettingResturnsBoolean(mContext, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
        Log.d("dfadsfd", "ExpandableMerchintListAdapter: "+isSubscribed);
        this.festivalRamdan = mContext.getResources().getString(R.string.festival_ramadan);
        this.festivalBurger = mContext.getResources().getString(R.string.festival_burger);
        this.festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);
        this.utilObj = new Utility(mContext);
        this.mCatID = _mCatID;

        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.color.transparent)
                .showImageOnFail(android.R.color.transparent)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.lstFoodnDrinks.get(groupPosition).getChild();//npt array so pos not required
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

//        final String childText = (String) getChild(groupPosition, childPosition);
        ViewHolderChild viewHolderChild = null;
        if (convertView == null) {
            viewHolderChild = new ViewHolderChild();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.food_and_drink_expendible_item_offers, null);
            viewHolderChild.imvOffer = (ImageView) convertView.findViewById(R.id.offerImage);
            viewHolderChild.imvLockOffer = (ImageView) convertView.findViewById(R.id.expfoodShowLockOffersIcon);
            viewHolderChild.imvFestival = (ImageView) convertView.findViewById(R.id.festivalImage);
            viewHolderChild.txvName = (TextView) convertView.findViewById(R.id.name);
            viewHolderChild.txvMerchantName = (TextView) convertView.findViewById(R.id.merchant_name);
            viewHolderChild.txvMerchantAddr = (TextView) convertView.findViewById(R.id.distanceAndMerchantAddress);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }
//        if (lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getStatus().equalsIgnoreCase("active")) {

        if (lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getImgUrl() != null) {
            mImageLoader.displayImage(lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getImgUrl(),
                    viewHolderChild.imvOffer, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        }
                    });
        }
        if (isSubscribed) {
            viewHolderChild.imvLockOffer.setVisibility(View.GONE);
        } else {
            viewHolderChild.imvLockOffer.setVisibility(View.VISIBLE);
        }
        if (lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getFestival().length() > 0) {
            viewHolderChild.imvFestival.setVisibility(View.VISIBLE);
            if (lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalRamdan)) {
                viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.ramadan));
            } else if (lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalBiryani)) {
                viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
            } else if (lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getFestival().equalsIgnoreCase(festivalBurger)) {
                viewHolderChild.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));
            } else {
                viewHolderChild.imvFestival.setVisibility(View.GONE);
            }
        } else {
            viewHolderChild.imvFestival.setVisibility(View.GONE);
        }

//        viewHolderChild.txvName.setText(lstFoodnDrinks.get(groupPosition).getChild().getAddress());

//            if (lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getFestival().length()>0){
//                if (lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getFestival().equals("")){
//
//                }
//            }
        viewHolderChild.txvName.setTypeface(novaThin);
        viewHolderChild.txvName.setText(lstFoodnDrinks.get(groupPosition).getChild().get(childPosition).getName());
//             viewHolderChild.imvLockOffer.setVisibility(View.GONE);
//        }
//        viewHolderChild.txvMerchantName.setTypeface(novaThin);
//        viewHolderChild.txvMerchantName.setText(lstFoodnDrinks.get(childPosition).getMerchantName());
//        viewHolderChild.txvMerchantAddr.setTypeface(novaThin);
//        viewHolderChild.txvMerchantAddr.setText(lstFoodnDrinks.get(childPosition).getMerchantAddress());
//        viewHolderChild.txvMerchantName.setText(lstFoodnDrinks.get(groupPosition).getChild().getNumber());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.lstFoodnDrinks.get(groupPosition).getChild().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.lstFoodnDrinks.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.lstFoodnDrinks.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//        String headerTitle = (String) getGroup(groupPosition);
//        DModelMerchintList dModelMerchintList = lstFoodnDrinks.get(groupPosition);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_expandible_merchant_list, null);

            viewHolder.merchant_name = (TextView) convertView
                    .findViewById(R.id.merchant_name);
            viewHolder.merchantAddress = (TextView) convertView
                    .findViewById(R.id.merchantAddress);

            viewHolder.merchantDistance = (TextView) convertView.findViewById(R.id.merchantDistance);
            viewHolder.llMerchantName = (RelativeLayout) convertView.findViewById(R.id.expllMerchntName);
            viewHolder.llIndicatorDown = (LinearLayout) convertView.findViewById(R.id.ll_down);
            viewHolder.llIndicatorUp = (LinearLayout) convertView.findViewById(R.id.ll_up);
            viewHolder.merchantLogoImage = (CircularImageView) convertView.findViewById(R.id.merchantLogoImage);
            viewHolder.imvGFestival = (ImageView) convertView.findViewById(R.id.GroupFestivalImage);
            viewHolder.imvLock = (ImageView) convertView.findViewById(R.id.expfoodShowGroupLockOffersIcon);
            viewHolder.rlMerchintDetail = (RelativeLayout) convertView.findViewById(R.id.rlMerchintDetail);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (isExpanded) {
            viewHolder.llIndicatorDown.setVisibility(View.GONE);
            viewHolder.llIndicatorUp.setVisibility(View.VISIBLE);
        } else {
            viewHolder.llIndicatorDown.setVisibility(View.VISIBLE);
            viewHolder.llIndicatorUp.setVisibility(View.GONE);
        }
//        if (lstFoodnDrinks.get(groupPosition).isActive().equalsIgnoreCase("1")) {

        if (lstFoodnDrinks.get(groupPosition).getMerchantName() != null) {
            viewHolder.merchant_name.setTypeface(novaRegular);
            viewHolder.merchant_name.setText("" + lstFoodnDrinks.get(groupPosition).getMerchantName());
        }
        if (lstFoodnDrinks.get(groupPosition).getMerchantAddress() != null) {
            viewHolder.merchantAddress.setTypeface(novaThin);
            viewHolder.merchantAddress.setText("" + lstFoodnDrinks.get(groupPosition).getMerchantAddress());
        }

        if (!utilObj.checkPermission(mContext) || !utilObj.isLocationEnabled(mContext)) {
            viewHolder.merchantDistance.setText("");

        } else {
            if (lstFoodnDrinks.get(groupPosition).getMerchantDistance() != null) {
                viewHolder.merchantDistance.setTypeface(novaThin);

                String strDistance = lstFoodnDrinks.get(groupPosition).getMerchantDistance();
                String[] parts = strDistance.split(Pattern.quote("."));
                int dist = Integer.parseInt(parts[0]);
                viewHolder.merchantDistance.setText(dist + " km");
            }
        }
        if (isSubscribed) {
            viewHolder.imvLock.setVisibility(View.GONE);
        } else {
            viewHolder.imvLock.setVisibility(View.VISIBLE);
        }
        if (lstFoodnDrinks.get(groupPosition).getFestival().length() > 0) {
            viewHolder.imvGFestival.setVisibility(View.VISIBLE);
            if (lstFoodnDrinks.get(groupPosition).getFestival().equalsIgnoreCase(festivalRamdan)) {
                viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.ramadan));

            } else if (lstFoodnDrinks.get(groupPosition).getFestival().equalsIgnoreCase(festivalBiryani)) {
                viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));

            } else if (lstFoodnDrinks.get(groupPosition).getFestival().equalsIgnoreCase(festivalBurger)) {
                viewHolder.imvGFestival.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));
            } else {
                viewHolder.imvGFestival.setVisibility(View.GONE);
            }
        } else {
            viewHolder.imvGFestival.setVisibility(View.GONE);
        }


        if (lstFoodnDrinks.get(groupPosition).getMerchantsImageUrl() != null) {
            mImageLoader.displayImage(lstFoodnDrinks.get(groupPosition).getMerchantsImageUrl(),
                    viewHolder.merchantLogoImage, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        }
                    });
        }
//        }

        viewHolder.rlMerchintDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                //String productid=AppPreference.getSetting(mContext,Constants.Request.OFFER_ID,"");
                String merchantId = lstFoodnDrinks.get(groupPosition).getId();
                String itemName = lstFoodnDrinks.get(groupPosition).getMerchantName();
                Bundle m_bundle = new Bundle();
                m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
                m_bundle.putString(Constants.Request.MERCHANT_ID, merchantId);
                m_bundle.putString(Constants.Request.NAME, itemName);
                m_bundle.putString(Constants.Request.CATEGORYID, mCatID);
                Intent intent = new Intent(mContext, MerchantDetailActivity.class);
                intent.putExtras(m_bundle);
                mContext.startActivity(intent);
            }
        });
        viewHolder.merchantLogoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                //String productid=AppPreference.getSetting(mContext,Constants.Request.OFFER_ID,"");
                String merchantId = lstFoodnDrinks.get(groupPosition).getId();
                String itemName = lstFoodnDrinks.get(groupPosition).getMerchantName();
                Bundle m_bundle = new Bundle();
                m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
                m_bundle.putString(Constants.Request.MERCHANT_ID, merchantId);
                m_bundle.putString(Constants.Request.NAME, itemName);
                m_bundle.putString(Constants.Request.CATEGORYID, mCatID);
                Intent intent = new Intent(mContext, MerchantDetailActivity.class);
                intent.putExtras(m_bundle);
                mContext.startActivity(intent);
            }
        });
        viewHolder.llMerchantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
                //String productid=AppPreference.getSetting(mContext,Constants.Request.OFFER_ID,"");
                String merchantId = lstFoodnDrinks.get(groupPosition).getId();
                String itemName = lstFoodnDrinks.get(groupPosition).getMerchantName();
                Bundle m_bundle = new Bundle();
                m_bundle.putString(Constants.Request.CUSTOMER_ID, custID);
                m_bundle.putString(Constants.Request.MERCHANT_ID, merchantId);
                m_bundle.putString(Constants.Request.NAME, itemName);
                m_bundle.putString(Constants.Request.CATEGORYID, mCatID);
                Intent intent = new Intent(mContext, MerchantDetailActivity.class);
                intent.putExtras(m_bundle);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        CircularImageView merchantLogoImage;
        ImageView imvGFestival, imvLock;
        TextView merchant_name, merchantAddress, merchantDistance;
        LinearLayout llIndicatorDown, llIndicatorUp;
        RelativeLayout rlMerchintDetail, llMerchantName;
    }

    class ViewHolderChild {
        TextView txvName, txvMerchantName, txvMerchantAddr;
        ImageView imvOffer, imvLockOffer, imvFestival;
    }

    public void updateLockedValues() {
        isSubscribed = AppPreference.getSettingResturnsBoolean(mContext, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
        Log.d("asdadsadqweq", "onResumeaD: ");
    }
}
