package com.urbanpoint.UrbanPoint.adapters.drawerItemAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.dataobject.PurchaseHistoryDetails;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.urbanpoint.UrbanPoint.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


public class PurchaseHistoryDateWiseAdapter extends ArrayAdapter<PurchaseHistoryDetails> {

    private Context mContex;
    private ArrayList<PurchaseHistoryDetails> listData = new ArrayList<PurchaseHistoryDetails>();

    //For loading image and chacheing of images.
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;


    public PurchaseHistoryDateWiseAdapter(Context ctx, int resource, ArrayList<PurchaseHistoryDetails> listData) {
        super(ctx, resource, listData);
        this.mContex = ctx;
        this.listData = listData;

        //imageLoader configuration
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(mContex));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.color.transparent)
                .showImageOnFail(android.R.color.transparent)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PurchaseHistoryDetails purchaseHistoryDetails = listData.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContex
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_purchase_history, null);

            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.name);
            viewHolder.merchantAddress = (TextView) convertView
                    .findViewById(R.id.merchantAddress);

            viewHolder.merchantName = (TextView) convertView
                    .findViewById(R.id.merchantName);


            viewHolder.purchasedPrice = (TextView) convertView
                    .findViewById(R.id.purchasedPrice);

            viewHolder.originalPrice = (TextView) convertView
                    .findViewById(R.id.originalPrice);

            viewHolder.purchaseHistoryImageView = (CircularImageView) convertView
                    .findViewById(R.id.purchaseHistoryImageView);

            viewHolder.merchantAddressView = (RelativeLayout) convertView
                    .findViewById(R.id.merchantAddressView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (purchaseHistoryDetails.getName() != null) {
            viewHolder.name.setText("" + purchaseHistoryDetails.getName());
        }
        if (purchaseHistoryDetails.getMerchantName() != null) {
            viewHolder.merchantName.setText("" + purchaseHistoryDetails.getMerchantName());
        }

        if (purchaseHistoryDetails.getPurchasePrice() != null) {
            String purchasePrice = purchaseHistoryDetails.getPurchasePrice();
            if (!purchasePrice.contains("-")) {
                double formattedPurchasePrice = Double.parseDouble(purchasePrice);
                BigDecimal bd = new BigDecimal(formattedPurchasePrice);
                BigDecimal res = bd.setScale(0, RoundingMode.DOWN);
                purchasePrice = res.toPlainString();
            }
            viewHolder.purchasedPrice.setText(mContex.getString(R.string.price_unit) + purchaseHistoryDetails.getApproximateSavings());
        }

        if (purchaseHistoryDetails.getOriginalPrice() != null) {
            String originalPrice = purchaseHistoryDetails.getOriginalPrice();
            if (!originalPrice.contains("-")) {
                double formattedOriginalPrice = Double.parseDouble(originalPrice);
                BigDecimal bd = new BigDecimal(formattedOriginalPrice);
                BigDecimal res = bd.setScale(0, RoundingMode.DOWN);
                originalPrice = res.toPlainString();
            }
           // viewHolder.originalPrice.setPaintFlags(viewHolder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
           // viewHolder.originalPrice.setText(mContex.getString(R.string.price_unit) + originalPrice);
        }

        if (purchaseHistoryDetails.getMerchantAddress() != null) {
            viewHolder.merchantAddress.setText("" + purchaseHistoryDetails.getMerchantAddress());
        } else {
            viewHolder.merchantAddressView.setVisibility(View.INVISIBLE);
        }
        if (purchaseHistoryDetails.getImageUrl() != null) {
            mImageLoader.displayImage(purchaseHistoryDetails.getImageUrl(), viewHolder.purchaseHistoryImageView, options, new SimpleImageLoadingListener() {
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

        return convertView;
    }


    class ViewHolder {
        CircularImageView purchaseHistoryImageView;
        TextView originalPrice, purchasedPrice, name, merchantAddress, merchantName;
        RelativeLayout merchantAddressView;
    }
}
