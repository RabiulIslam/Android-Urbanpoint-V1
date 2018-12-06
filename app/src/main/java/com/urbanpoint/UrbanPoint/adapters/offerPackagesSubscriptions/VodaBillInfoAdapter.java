package com.urbanpoint.UrbanPoint.adapters.offerPackagesSubscriptions;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.dataobject.offerPackagesSubscriptions.OfferPackagesItemsDetails;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import java.util.ArrayList;

/**
 * Created by riteshpandhurkar on 15/3/16.
 */
public class VodaBillInfoAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Typeface mFont;
    private Context mContext;
    private ArrayList<String> listData;

    public VodaBillInfoAdapter(Context ctx, ArrayList<String> listData) {
        this.mContext = ctx;
        this.listData = listData;
        inflater = LayoutInflater.from(ctx);
        mFont = MyApplication.getInstance().getFont();

    }


    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_voda_subscription_enter_mobile_info_list_item, parent, false);
            mViewHolder = new DataViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (DataViewHolder) convertView.getTag();
        }

        String dataToSet = (String) getItem(position);

        mViewHolder.textData.setText("" + dataToSet);

        //-- Set Font
        mViewHolder.textData.setTypeface(mFont);

        return convertView;
    }

    private class DataViewHolder {
        TextView textData;

        public DataViewHolder(View item) {
            textData = (TextView) item.findViewById(R.id.billInfoText);
        }
    }
}
