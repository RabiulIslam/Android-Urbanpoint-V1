package com.urbanpoint.UrbanPoint.adapters.SearchFilterAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.SearchFilter.FilteredSearchItem;
import com.urbanpoint.UrbanPoint.dataobject.SearchFilter.LookingForFilterListItem;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aparnasalve on 9/3/16.
 */
public class SearchFilterAdapter extends ArrayAdapter<FilteredSearchItem> {

    private Typeface mFont;
    private String mLockOffersStatusFlag;
    private Context mContext;
    private List<FilteredSearchItem> listData;
    //For loading image and chacheing of images.
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private Resources resources;
    private boolean isSubscribed;
    private String festivalRamdan, festivalBurger, festivalBiryani;

    public SearchFilterAdapter(Context ctx, int resource, List<FilteredSearchItem> listData, String lockOffersStatusFlag) {
        super(ctx, resource, listData);
        this.mContext = ctx;
        this.listData = listData;
        isSubscribed = AppPreference.getSettingResturnsBoolean(mContext, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);
        this.festivalRamdan = mContext.getResources().getString(R.string.festival_ramadan);
        this.festivalBurger = mContext.getResources().getString(R.string.festival_burger);
        this.festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);

        //imageLoader configuration
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.color.transparent)
                .showImageOnFail(android.R.color.transparent)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        resources = mContext.getResources();

        this.mLockOffersStatusFlag = lockOffersStatusFlag;

        mFont = MyApplication.getInstance().getFont();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilteredSearchItem filteredSearchItem = listData.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.filter_list_item, null);

            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.name);

            viewHolder.merchant_name = (TextView) convertView
                    .findViewById(R.id.merchant_name);

            viewHolder.discount = (TextView) convertView
                    .findViewById(R.id.discount);

            viewHolder.offerImage = (ImageView) convertView
                    .findViewById(R.id.offerImage);

            viewHolder.foodImage = (ImageView) convertView.findViewById(R.id.imageView_food);
            viewHolder.foodImage.setVisibility(View.GONE);
            viewHolder.beautyImage = (ImageView) convertView.findViewById(R.id.imageView_beauty);
            viewHolder.beautyImage.setVisibility(View.GONE);
            viewHolder.healthImage = (ImageView) convertView.findViewById(R.id.imageView_health);
            viewHolder.healthImage.setVisibility(View.GONE);
            viewHolder.funImage = (ImageView) convertView.findViewById(R.id.imageView_fun);
            viewHolder.funImage.setVisibility(View.GONE);

            viewHolder.lockOffersIcon = (ImageView) convertView.findViewById(R.id.lockOffersIcon);

            viewHolder.distanceAndMerchantAddress = (TextView) convertView
                    .findViewById(R.id.distanceAndMerchantAddress);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (filteredSearchItem.getCategory() != null) {
            if (filteredSearchItem.getCategory().equalsIgnoreCase("Health & Fitness")) {
                viewHolder.healthImage.setVisibility(View.VISIBLE);
                viewHolder.funImage.setVisibility(View.GONE);
                viewHolder.beautyImage.setVisibility(View.GONE);
                viewHolder.foodImage.setVisibility(View.GONE);

            } else if (filteredSearchItem.getCategory().equalsIgnoreCase("Beauty & Spas")) {
                viewHolder.beautyImage.setVisibility(View.VISIBLE);
                viewHolder.funImage.setVisibility(View.GONE);
                viewHolder.healthImage.setVisibility(View.GONE);
                viewHolder.foodImage.setVisibility(View.GONE);
            } else if (filteredSearchItem.getCategory().equalsIgnoreCase("Food & Drink")) {
                viewHolder.beautyImage.setVisibility(View.GONE);
                viewHolder.funImage.setVisibility(View.GONE);
                viewHolder.healthImage.setVisibility(View.GONE);
                viewHolder.foodImage.setVisibility(View.VISIBLE);
            } else if (filteredSearchItem.getCategory().equalsIgnoreCase("Fun & Activities")) {
                viewHolder.beautyImage.setVisibility(View.GONE);
                viewHolder.funImage.setVisibility(View.VISIBLE);
                viewHolder.healthImage.setVisibility(View.GONE);
                viewHolder.foodImage.setVisibility(View.GONE);
            }
        }

        String data = filteredSearchItem.getMerchantaddress();

        if (data != null) {
            viewHolder.distanceAndMerchantAddress.setText("" + data);
            viewHolder.distanceAndMerchantAddress.setVisibility(View.VISIBLE);
        } else {
            viewHolder.distanceAndMerchantAddress.setText("");
        }

        if (filteredSearchItem.getName() != null) {
            viewHolder.name.setText("" + filteredSearchItem.getName());
        } else {
            viewHolder.name.setText("");
        }


        if (filteredSearchItem.getMerchant_name() != null) {
            viewHolder.merchant_name.setText("" + filteredSearchItem.getMerchant_name());
        } else {
            viewHolder.merchant_name.setText("");
        }

        if (filteredSearchItem.getDiscount() != null) {
            String discount = filteredSearchItem.getDiscount();
            boolean contains = discount.contains("%");
            if (contains) {
                viewHolder.discount.setText(resources.getString(R.string.save) + " " + discount);
            } else {
                viewHolder.discount.setText(resources.getString(R.string.save) + " " + discount + "%");
            }
        }


        if (filteredSearchItem.getImage() != null) {
            mImageLoader.displayImage(filteredSearchItem.getImage(), viewHolder.offerImage, options, new SimpleImageLoadingListener() {
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

//        if (Constants.DEFAULT_VALUES.ONE.equalsIgnoreCase(mLockOffersStatusFlag)) {
//            viewHolder.lockOffersIcon.setVisibility(View.VISIBLE);
//        }

        String festivalName = mContext.getResources().getString(R.string.festival_ramadan);
        String festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);


        if (isSubscribed) {
            if (filteredSearchItem.getFestival() != null &&
                    filteredSearchItem.getFestival().length() > 0) {
                viewHolder.lockOffersIcon.setVisibility(View.VISIBLE);
                if (filteredSearchItem.getFestival().equalsIgnoreCase(festivalRamdan)) {
                    viewHolder.lockOffersIcon.setImageResource(R.drawable.ramadan);
                } else if (filteredSearchItem.getFestival().equalsIgnoreCase(festivalBiryani)) {
                    viewHolder.lockOffersIcon.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
                } else if (filteredSearchItem.getFestival().equalsIgnoreCase(festivalBurger)) {
                    viewHolder.lockOffersIcon.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));
                } else {
                    viewHolder.lockOffersIcon.setVisibility(View.GONE);
                }
            } else {
                viewHolder.lockOffersIcon.setVisibility(View.GONE);
            }
        } else {
            viewHolder.lockOffersIcon.setVisibility(View.VISIBLE);
        }


        //-- Set Font
        viewHolder.name.setTypeface(mFont);
        viewHolder.distanceAndMerchantAddress.setTypeface(mFont);
        viewHolder.merchant_name.setTypeface(mFont);
        viewHolder.discount.setTypeface(mFont);


        return convertView;
    }


    class ViewHolder {
        ImageView offerImage, foodImage, healthImage, beautyImage, funImage, lockOffersIcon;
        TextView distanceAndMerchantAddress, merchant_name, name, discount;
    }
}


