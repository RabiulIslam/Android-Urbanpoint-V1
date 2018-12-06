package com.urbanpoint.UrbanPoint.adapters.offerPackagesSubscriptions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.OfferPackagesItemsDetails;
import com.urbanpoint.UrbanPoint.views.customViews.customFontViews.CustomTextView;

import java.util.ArrayList;

/**
 * Created by riteshpandhurkar on 15/3/16.
 */
public class OfferPackagesAdapter extends ArrayAdapter<OfferPackagesItemsDetails> {
    private Context mContext;
    private ArrayList<OfferPackagesItemsDetails> listData;

    private Resources resources;
    private Typeface novaThin, novaRegular;
    private int mSelectedPosition;
    String rowOneDataMonthlySaving = "690";
    String rowTwoDataMonthlySaving = "1380";
    String rowThreeDataMonthlySaving = "2760";

    String rowTwoDataSavePercentage = "40%";
    String rowThreeDataSavePercentage = "60%";

    public OfferPackagesAdapter(Context ctx,int _mSelectedPosition ,int resource, ArrayList<OfferPackagesItemsDetails> listData) {
        super(ctx, resource, listData);
        this.mContext = ctx;
        this.mSelectedPosition = _mSelectedPosition;
        this.listData = listData;
        this.novaThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_thin.ttf");
        this.novaRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/proxima_nova_alt_regular.ttf");
        resources = mContext.getResources();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        OfferPackagesItemsDetails packagesDetails = listData.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
             LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_offer_packages_listview_content, null);
//            Log.d("PAKAGES", "getView: "+listData.size());
            viewHolder.rlMonths = (RelativeLayout) convertView.findViewById(R.id.act_offr_new_pakages_rl_3_month);
            viewHolder.txvHeading = (TextView) convertView.findViewById(R.id.act_offr_new_pakages_txv_heading_1);
            viewHolder.txvSubTitle = (TextView) convertView.findViewById(R.id.act_offr_new_pakages_txv_subtitle_1);
            viewHolder.imvSelector = (ImageView) convertView.findViewById(R.id.act_offr_new_pakages_imv_1);


//            viewHolder.headerName = (CustomTextView) convertView
//                    .findViewById(R.id.headerName);
//
//            viewHolder.pricePerMonth = (CustomTextView) convertView
//                    .findViewById(R.id.pricePerMonth);
//
//            viewHolder.totalPrice = (CustomTextView) convertView
//                    .findViewById(R.id.totalPrice);
//
//            viewHolder.totalDues = (CustomTextView) convertView
//                    .findViewById(R.id.totalDues);
//
//            viewHolder.savePercentage = (CustomTextView) convertView
//                    .findViewById(R.id.savePercentage);
//
//            viewHolder.monthlySavings = (CustomTextView) convertView
//                    .findViewById(R.id.monthlySavings);


            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txvHeading.setText(packagesDetails.getOffer());
        viewHolder.txvSubTitle.setText(packagesDetails.getOffer_SubTitle());
        viewHolder.txvHeading.setTypeface(novaRegular);
        viewHolder.txvSubTitle.setTypeface(novaRegular);

//        Log.d("PAKAGES", "offer name: "+listData.get(mSelectedPosition).getOffer());
//        Log.d("PAKAGES", "position is : "+mSelectedPosition);
//        viewHolder.rlMonths.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
                if (mSelectedPosition>=0 &&
                        position == mSelectedPosition){
                    viewHolder.rlMonths.setBackground(mContext.getResources().getDrawable(R.drawable.purple_background_border));
                    viewHolder.txvHeading.setTextColor(mContext.getResources().getColor(R.color.black));
                    viewHolder.imvSelector.setBackground(mContext.getResources().getDrawable(R.drawable.purple_circle));
                }
                else {
                    viewHolder.rlMonths.setBackground(mContext.getResources().getDrawable(R.drawable.gray_background_border));
                    viewHolder.txvHeading.setTextColor(mContext.getResources().getColor(R.color.gray));
                    viewHolder.imvSelector.setBackground(mContext.getResources().getDrawable(R.drawable.purple_stroke_circle));
                }

//            }
//        });

//        if (packagesDetails.getOffer() != null) {
////            String offer = packagesDetails.getOffer().replace("/", "-");
//            viewHolder.txvHeading.setText(packagesDetails.getOffer());
//        }
//        if (packagesDetails.getQarValue() != null) {
//            int result = 0;
//            int calculatedTotalDues = 0;
//            // In iOS this section is hardcoded, hence done it. [Values are incorrect] -- Start
//            if (position == 0) {
//                result = Integer.parseInt(packagesDetails.getQarValue());
//                calculatedTotalDues = Integer.parseInt(packagesDetails.getQarValue()) * 3;
//                String monthlySavingsData = resources.getString(R.string.offer_packages_monthly_savings).concat(resources.getString(R.string.qar, "" + packagesDetails.getApproximateSavings()));
//                viewHolder.monthlySavings.setText(monthlySavingsData);
//                //viewHolder.savePercentage.setVisibility(View.GONE);
//                String savePercentageData = packagesDetails.getText();
//                if (!(Constants.BLANK.equalsIgnoreCase(savePercentageData)) && savePercentageData != null) {
//                    viewHolder.savePercentage.setText(savePercentageData);
//                    viewHolder.savePercentage.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.savePercentage.setVisibility(View.GONE);
//                }
//
//            } else if (position == 1) {
//                result = Integer.parseInt(packagesDetails.getQarValue());
//                calculatedTotalDues = Integer.parseInt(packagesDetails.getQarValue()) * 6;
//
//                String monthlySavingsData = resources.getString(R.string.offer_packages_monthly_savings).concat(resources.getString(R.string.qar, "" + packagesDetails.getApproximateSavings()));
//                viewHolder.monthlySavings.setText(monthlySavingsData);
//
//                viewHolder.savePercentage.setVisibility(View.VISIBLE);
//
//                // String savePercentageData = resources.getString(R.string.offer_packages_save_one_month, rowTwoDataSavePercentage);
//                String savePercentageData = packagesDetails.getText();
//                if (!(Constants.BLANK.equalsIgnoreCase(savePercentageData)) && savePercentageData != null) {
//                    viewHolder.savePercentage.setText(savePercentageData);
//                    viewHolder.savePercentage.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.savePercentage.setVisibility(View.GONE);
//                }
//
//            } else {
//                result = Integer.parseInt(packagesDetails.getQarValue()) / 12;
//
//                String monthlySavingsData = resources.getString(R.string.offer_packages_monthly_savings).concat(resources.getString(R.string.qar, "" + packagesDetails.getApproximateSavings()));
//                viewHolder.monthlySavings.setText(monthlySavingsData);
//
//                //String savePercentageData = resources.getString(R.string.offer_packages_save_one_month, rowThreeDataSavePercentage);
//                String savePercentageData = packagesDetails.getText();
//                if (!(Constants.BLANK.equalsIgnoreCase(savePercentageData)) && savePercentageData != null) {
//                    viewHolder.savePercentage.setText(savePercentageData);
//                    viewHolder.savePercentage.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.savePercentage.setVisibility(View.GONE);
//                }
//            }
//            // In iOS this section is hardcoded, hence done it. [Values are incorrect] -- End
//
//            String value = resources.getString(R.string.offer_packages_price_per_month).concat(resources.getString(R.string.qar, "" + result));
//            viewHolder.pricePerMonth.setText("" + value);
//
//            String totalPrice = resources.getString(R.string.qar, calculatedTotalDues);
//            viewHolder.totalPrice.setText("" + totalPrice);
//
//            String totalDues = resources.getString(R.string.offer_packages_total_due).concat(" " + resources.getString(R.string.qar, calculatedTotalDues));
//            viewHolder.totalDues.setText("" + totalDues);
//        }

        return convertView;
    }


    class ViewHolder {
        CustomTextView monthlySavings, totalDues, totalPrice, pricePerMonth, headerName, savePercentage;
        RelativeLayout rlMonths;
        ImageView imvSelector;
        TextView txvHeading, txvSubTitle;


    }
}
