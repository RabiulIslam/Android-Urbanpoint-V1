package com.urbanpoint.UrbanPoint.adapters.homeAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.main.DModelHomeGrdVw;

import java.util.List;

public class HorizontalGridViewAdapter extends BaseAdapter {
    List<DModelHomeGrdVw> listOffers;
    Context mContext;
    LayoutInflater inflater;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;


    public HorizontalGridViewAdapter(Context _mContext, List<DModelHomeGrdVw> listOffers) {
        this.mContext = _mContext;
        this.listOffers = listOffers;
        inflater = LayoutInflater.from(_mContext);
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(_mContext));
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
        return listOffers.size();
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
        // Log.i(TAG, "getView: " + selectedposition);
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.horizontal_grid_view_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtTimmer = (TextView) convertView.findViewById(R.id.grd_vw_txv_timmer);
            viewHolder.txvOffer = (TextView) convertView.findViewById(R.id.grd_vw_txv_offer);
            viewHolder.imvOffer = (ImageView) convertView.findViewById(R.id.grd_vw_imv_bg);
            viewHolder.imvOverlay = (ImageView) convertView.findViewById(R.id.grd_vw_imv_overlay);
            viewHolder.rlTimmerContnr = (RelativeLayout) convertView.findViewById(R.id.grd_vw_rl_timmer);
            viewHolder.imvFestival = (ImageView) convertView.findViewById(R.id.grd_vw_imv_festival);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (listOffers.get(position).getStrProductId().length() < 0) {
            viewHolder.txtTimmer.setText(listOffers.get(position).getStrProductId());
        } else {
            viewHolder.rlTimmerContnr.setVisibility(View.GONE);
            viewHolder.imvOverlay.setVisibility(View.GONE);
        }

        if (listOffers.get(position).getStrFestival().equalsIgnoreCase(mContext.getResources().getString(R.string.festival_ramadan))) {

            viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.ramadan));

        } else if (listOffers.get(position).getStrFestival().equalsIgnoreCase(mContext.getResources().getString(R.string.festival_burger))) {
            viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.burger_icon));

        } else if (listOffers.get(position).getStrFestival().equalsIgnoreCase(mContext.getResources().getString(R.string.festival_biryani))) {
            viewHolder.imvFestival.setBackground(mContext.getResources().getDrawable(R.drawable.biryani_icon));
        }else {
            viewHolder.imvFestival.setVisibility(View.GONE);
        }

        if (listOffers.get(position).getStrImgUrl().length()>0) {
            mImageLoader.displayImage(listOffers.get(position).getStrImgUrl(), viewHolder.imvOffer, options, new SimpleImageLoadingListener() {
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
        viewHolder.txvOffer.setText(listOffers.get(position).getStrOfferName() + " at " +
        listOffers.get(position).getStrMerchantName());


        return convertView;
    }

    static class ViewHolder {
        TextView txtTimmer, txvOffer;
        ImageView imvOffer, imvOverlay, imvFestival;
        RelativeLayout rlTimmerContnr;
    }

}
