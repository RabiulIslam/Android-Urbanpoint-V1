package com.urbanpoint.UrbanPoint.adapters.homeAdapter.favoritesAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.main.DModelHomeGrdVw;
import com.urbanpoint.UrbanPoint.utils.Utility;

import java.util.List;

/**
 * Created by Lenovo on 6/20/2017.
 */

public class FavoritesAdapter extends BaseAdapter {
    Context mContext;
    List<DModelHomeGrdVw> lstFavorites;
    LayoutInflater inflater;
    Typeface novaThin, novaRegular;
    private String festivalRamdan;
    private String festivalBurger;
    private String festivalBiryani;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private boolean isSubscribed;
    private Utility utilObj;

    public FavoritesAdapter(Context _mContext, List<DModelHomeGrdVw> _lstFavorites, boolean isSubscribed) {
        this.mContext = _mContext;
        this.lstFavorites = _lstFavorites;
        this.isSubscribed = isSubscribed;
//        this.festivalRamdan = "ramadan";
        this.festivalRamdan = "ramadan";
        this.festivalBurger = mContext.getResources().getString(R.string.festival_burger);
        this.festivalBiryani = mContext.getResources().getString(R.string.festival_biryani);
        inflater = LayoutInflater.from(_mContext);
        novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(_mContext));
        this.utilObj = new Utility(mContext);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.color.transparent)
                .showImageOnFail(android.R.color.transparent)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount() {
        return lstFavorites.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.favorites_offers_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imvOffer = (ImageView) convertView.findViewById(R.id.fav_offer_Image);
            viewHolder.imvLockOffer = (ImageView) convertView.findViewById(R.id.foodShowLockOffersIcon);
            viewHolder.imvFestival = (ImageView) convertView.findViewById(R.id.newOffersFestivalIcon);
            viewHolder.txvName = (TextView) convertView.findViewById(R.id.fav_offr_name);
            viewHolder.txvMerchantName = (TextView) convertView.findViewById(R.id.fav_merchant_name);
            viewHolder.txvMerchantAddress = (TextView) convertView.findViewById(R.id.fav_distance_and_merchant_address);
            viewHolder.txvDistanceUnit =(TextView) convertView.findViewById(R.id.fav_distance_unit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.d("EEE", "getView: " + lstFavorites.size());

        if (lstFavorites.get(position).getStrImgUrl().length() > 0) {
            mImageLoader.displayImage(lstFavorites.get(position).getStrImgUrl(), viewHolder.imvOffer, options, new SimpleImageLoadingListener() {
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
            viewHolder.imvLockOffer.setVisibility(View.GONE);
        } else {
            viewHolder.imvLockOffer.setVisibility(View.VISIBLE);
        }

        if (lstFavorites.get(position).getStrFestival().length() > 0) {
            viewHolder.imvFestival.setVisibility(View.VISIBLE);
            if (lstFavorites.get(position).getStrFestival().equalsIgnoreCase(festivalRamdan)) {
                viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.ramadan));
            } else if (lstFavorites.get(position).getStrFestival().equalsIgnoreCase(festivalBiryani)) {
                viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
            } else if (lstFavorites.get(position).getStrFestival().equalsIgnoreCase(festivalBurger)) {
                viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));
            } else {
                viewHolder.imvFestival.setVisibility(View.GONE);
            }
        } else {
            viewHolder.imvFestival.setVisibility(View.GONE);
        }

        viewHolder.txvName.setText(lstFavorites.get(position).getStrOfferName());
        viewHolder.txvName.setTypeface(novaRegular);
        viewHolder.txvMerchantName.setText(lstFavorites.get(position).getStrMerchantName());
        viewHolder.txvMerchantName.setTypeface(novaRegular);

        if (!utilObj.checkPermission(mContext)) {
            viewHolder.txvMerchantAddress.setText("");
            viewHolder.txvDistanceUnit.setVisibility(View.GONE);
        } else {
            viewHolder.txvMerchantAddress.setText(lstFavorites.get(position).getStrDistance() + "");
        }
        viewHolder.txvMerchantAddress.setTypeface(novaRegular);
        viewHolder.txvDistanceUnit.setTypeface(novaRegular);


        return convertView;
    }

    public static class ViewHolder {
        ImageView imvOffer, imvLockOffer,imvFestival;
        TextView txvName, txvMerchantName, txvMerchantAddress, txvDistanceUnit;
    }
}
