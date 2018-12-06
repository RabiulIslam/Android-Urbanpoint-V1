package com.urbanpoint.UrbanPoint.adapters.categoryScreensAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
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
import com.urbanpoint.UrbanPoint.dataobject.CategoryScreens.MerchantItemDetails;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by aparnasalve on 8/3/16.
 */
public class MerchantOfferListAdapter extends ArrayAdapter<MerchantItemDetails> {
    private String mLockOffersStatusFlag;
    private DecimalFormat numberFormat;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private ArrayList<MerchantItemDetails> listData = new ArrayList<MerchantItemDetails>();
    private Resources resources;
    private String strPromotionalPermision;
    private boolean isSubscribed;

    public MerchantOfferListAdapter(Context mContext, int resource, ArrayList<MerchantItemDetails> listData, String lockOffersStatusFlag) {
        super(mContext, resource, listData);
        this.mContext = mContext;
        this.listData = listData;
        strPromotionalPermision = AppPreference.getSetting(mContext, "key_Promotion_acessed", "");
        isSubscribed = AppPreference.getSettingResturnsBoolean(mContext, Constants.DEFAULT_VALUES.IS_USER_SUBSCRIBE, false);

        this.numberFormat = new DecimalFormat("0");

        //imageLoader configuration
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.no_image_white)
                .showImageForEmptyUri(R.mipmap.no_image_white)
                .showImageOnFail(R.mipmap.no_image_white).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        resources = mContext.getResources();
        this.mLockOffersStatusFlag = lockOffersStatusFlag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MerchantItemDetails offersDetails = listData.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.merchant_offer_item, null);

            //-----THIS IS LAYOUT WITH CIRCULAR IMAGE VIEW FOR OFFER MERCHANT --START
            //convertView = inflater.inflate(R.layout.merchant_offer_item_backup_30_may, null);
            //-----THIS IS LAYOUT WITH CIRCULAR IMAGE VIEW FOR OFFER MERCHANT -- END

            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.name);

            viewHolder.merchant_name = (TextView) convertView
                    .findViewById(R.id.merchant_name);

            viewHolder.discount = (TextView) convertView
                    .findViewById(R.id.discount);

            viewHolder.offerImage = (ImageView) convertView
                    .findViewById(R.id.offerImage);


            viewHolder.price = (TextView) convertView.findViewById(R.id.price);

            viewHolder.specialPrice = (TextView) convertView.findViewById(R.id.special_price);

            viewHolder.merchantOffersShowLockOffersIcon = (ImageView) convertView.findViewById(R.id.merchantOffersShowLockOffersIcon);
            viewHolder.merchantOffersFestivalOffersIcon = (ImageView) convertView.findViewById(R.id.merchantOffersFestivalOffersIcon);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (offersDetails.getProductname() != null) {
            viewHolder.name.setText("" + offersDetails.getProductname());
        }
        if (offersDetails.getPrice() != null) {
            String data = offersDetails.getPrice();
            if (data.contains("-")) {
                data = String.format(resources.getString(R.string.qar), data);
            } else {
                double price = Double.parseDouble(offersDetails.getPrice());
                data = String.format(resources.getString(R.string.qar), numberFormat.format(price));
            }
            viewHolder.price.setText("" + data);
            viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.price.setVisibility(View.INVISIBLE);
        }

        if (offersDetails.getSpecialprice() != null) {
            String data = offersDetails.getSpecialprice();
            if (data.contains("-")) {
                data = String.format(resources.getString(R.string.qar), data);
            } else {
                double specialPrice = Double.parseDouble(data);
                data = String.format(resources.getString(R.string.qar), numberFormat.format(specialPrice));
            }
            viewHolder.specialPrice.setText("" + data);
        } else {
            viewHolder.price.setVisibility(View.INVISIBLE);
        }

        if (offersDetails.getDiscount() != null) {
            String discount = offersDetails.getDiscount();
            boolean contains = discount.contains("%");
            if (contains) {
                viewHolder.discount.setText(resources.getString(R.string.save) + " " + discount);
            } else {
                viewHolder.discount.setText(resources.getString(R.string.save) + " " + discount + "%");
            }
        }
        if (offersDetails.getProductimage() != null) {
            mImageLoader.displayImage(offersDetails.getProductimage(), viewHolder.offerImage, options, new SimpleImageLoadingListener() {
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
        } else {
            viewHolder.offerImage.setImageResource(R.mipmap.no_image_white);
        }

        /*
        TODO :// SHOW LOCK ICON IN OFFER LIST IN MERCHANT DETAILS
        String isVodafoneCustomer = AppPreference.getSetting(mContext, Constants.DEFAULT_VALUES.OPERATOR_TYPE, "");

        if (Constants.DEFAULT_VALUES.PURCHASED.equalsIgnoreCase(offersDetails.getStatus())) {
            viewHolder.merchantOffersShowLockOffersIcon.setVisibility(View.GONE);
        } else {
            viewHolder.merchantOffersShowLockOffersIcon.setVisibility(View.VISIBLE);
        }*/

        //---------- commented on 25 NOV 2016------------
        /*String festivalName = mContext.getResources().getString(R.string.festival_ramadan);
        String festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);

        if (Constants.DEFAULT_VALUES.ONE.equalsIgnoreCase(mLockOffersStatusFlag)) {
            viewHolder.merchantOffersShowLockOffersIcon.setVisibility(View.VISIBLE);
            if (festivalName.equalsIgnoreCase(offersDetails.getFestival())) {
                viewHolder.merchantOffersShowLockOffersIcon.setImageResource(R.drawable.ramadan);
            } else if (festivalBiryani.equalsIgnoreCase(offersDetails.getFestival())) {
                viewHolder.merchantOffersShowLockOffersIcon.setImageResource(R.drawable.biryani_icon);
            } else {
                viewHolder.merchantOffersShowLockOffersIcon.setImageResource(R.drawable.lock_offers);
            }
        } else {
            if (festivalName.equalsIgnoreCase(offersDetails.getFestival())) {
                viewHolder.merchantOffersShowLockOffersIcon.setVisibility(View.VISIBLE);
                viewHolder.merchantOffersShowLockOffersIcon.setImageResource(R.drawable.ramadan);
            } else if (festivalBiryani.equalsIgnoreCase(offersDetails.getFestival())) {
                viewHolder.merchantOffersShowLockOffersIcon.setVisibility(View.VISIBLE);
                viewHolder.merchantOffersShowLockOffersIcon.setImageResource(R.drawable.biryani_icon);
            } else {
                viewHolder.merchantOffersShowLockOffersIcon.setVisibility(View.GONE);
                viewHolder.merchantOffersShowLockOffersIcon.setImageResource(R.drawable.lock_offers);
            }
        }*/
        String festivalName = mContext.getResources().getString(R.string.festival_ramadan);
        String festivalBurger = mContext.getResources().getString(R.string.festival_burger);
        String festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);

        if (Constants.DEFAULT_VALUES.ONE.equalsIgnoreCase(mLockOffersStatusFlag)) {
            viewHolder.merchantOffersShowLockOffersIcon.setVisibility(View.VISIBLE);
         } else {
            viewHolder.merchantOffersShowLockOffersIcon.setVisibility(View.GONE);
         }
        if (offersDetails.getFestival() != null &&
                offersDetails.getFestival().length() > 0) {
            viewHolder.merchantOffersFestivalOffersIcon.setVisibility(View.VISIBLE);
            if (festivalName.equalsIgnoreCase(offersDetails.getFestival())) {
                viewHolder.merchantOffersFestivalOffersIcon.setImageResource(R.drawable.ramadan);
                viewHolder.merchantOffersFestivalOffersIcon.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.dp20);
                viewHolder.merchantOffersFestivalOffersIcon.getLayoutParams().width = (int) mContext.getResources().getDimension(R.dimen.dp20);
            } else if (festivalBurger.equalsIgnoreCase(offersDetails.getFestival())) {
                viewHolder.merchantOffersFestivalOffersIcon.setImageResource(R.drawable.burger_icon);
                viewHolder.merchantOffersFestivalOffersIcon.setPadding(0, 0, 5, 5);

                viewHolder.merchantOffersFestivalOffersIcon.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.burger_icon_size);
                viewHolder.merchantOffersFestivalOffersIcon.getLayoutParams().width = (int) mContext.getResources().getDimension(R.dimen.burger_icon_size);

            } else if (festivalBiryani.equalsIgnoreCase(offersDetails.getFestival())) {
                viewHolder.merchantOffersFestivalOffersIcon.setImageResource(R.drawable.biryani_icon);
                viewHolder.merchantOffersFestivalOffersIcon.setPadding(0, 0, 5, 5);

                viewHolder.merchantOffersFestivalOffersIcon.getLayoutParams().height = (int) mContext.getResources().getDimension(R.dimen.burger_icon_size);
                viewHolder.merchantOffersFestivalOffersIcon.getLayoutParams().width = (int) mContext.getResources().getDimension(R.dimen.burger_icon_size);

            } else {
                viewHolder.merchantOffersFestivalOffersIcon.setVisibility(View.GONE);
            }
        } else {
            viewHolder.merchantOffersFestivalOffersIcon.setVisibility(View.GONE);
        }
        //-------------
        return convertView;
    }


    class ViewHolder {
        ImageView offerImage, merchantOffersShowLockOffersIcon, merchantOffersFestivalOffersIcon;
        TextView distanceAndMerchantAddress, merchant_name, name, discount, price, specialPrice;
    }
}


