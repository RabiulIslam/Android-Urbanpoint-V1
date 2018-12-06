package com.urbanpoint.UrbanPoint.adapters.drawerItemAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.async.CommonAsyncTaskResult;
import com.urbanpoint.UrbanPoint.async.CommonAsynctask;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.dataobject.Home.RModel_HomeOffers;
import com.urbanpoint.UrbanPoint.dataobject.drawerItem.MyReviewsListItem;
import com.urbanpoint.UrbanPoint.dataobject.wishList.WishListItem;
import com.urbanpoint.UrbanPoint.interfaces.ICommonCallback;
import com.urbanpoint.UrbanPoint.managers.drawerItems.MyReviewsManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.common.PurchaseSuccessActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aparnasalve on 16/3/16.
 */
public class MyReviewsAdapter extends ArrayAdapter<MyReviewsListItem> {

    private Activity mActivity;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private Resources resources;
    private Context mContext;
    private Utility utilObj;
    String status, message;
    ViewHolder viewHolder = null;
    private ArrayList<MyReviewsListItem> listData = new ArrayList<MyReviewsListItem>();


    public MyReviewsAdapter(Context ctx, int resource, ArrayList<MyReviewsListItem> listData, Activity mActivity) {
        super(ctx, resource, listData);
        this.mContext = ctx;
        this.listData = listData;
        this.mActivity = mActivity;
        utilObj = new Utility(mActivity);
        // mMyReviewManager=new MyReviewsManager(mContext,this);

        //imageLoader configuration
        this.mImageLoader = ImageLoader.getInstance();
        this.mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.no_image_white)
                .showImageForEmptyUri(R.mipmap.no_image_white)
                .showImageOnFail(R.mipmap.no_image_white)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        resources = mContext.getResources();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyReviewsListItem myReviewsListItem = listData.get(position);

        viewHolder = new ViewHolder();
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_reviews_list_item, null);

            viewHolder.itemName = (TextView) convertView
                    .findViewById(R.id.txtV_itemName);

            viewHolder.itemNameMainCourse = (TextView) convertView
                    .findViewById(R.id.txtV_itemName_maiCourse);


            viewHolder.merchantName = (TextView) convertView
                    .findViewById(R.id.txtV_itemMerchantName);

            viewHolder.offerImage = (ImageView) convertView
                    .findViewById(R.id.offerImage);

            viewHolder.dislikeNormalImage = (Button) convertView.findViewById(R.id.imageView_dislike);
            viewHolder.loveItNormalImage = (Button) convertView.findViewById(R.id.imageView_notbad);
            viewHolder.notBadNormalImage = (Button) convertView.findViewById(R.id.imageView_loveit);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.dislikeNormalImage.setTag(viewHolder);
        viewHolder.loveItNormalImage.setTag(viewHolder);
        viewHolder.notBadNormalImage.setTag(viewHolder);


        viewHolder.dislikeNormalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doAddReview(mActivity, resources.getString(R.string.dislike_review), listData.get(position).getId(), Integer.toString(position));

            }
        });

        viewHolder.loveItNormalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddReview(mActivity, resources.getString(R.string.love_it_review), listData.get(position).getId(), Integer.toString(position));

            }
        });


        viewHolder.notBadNormalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddReview(mActivity, resources.getString(R.string.not_bad_review), listData.get(position).getId(), Integer.toString(position));

            }
        });


        if (myReviewsListItem.getImage() != null) {
            mImageLoader.displayImage(myReviewsListItem.getImage(), viewHolder.offerImage, options, new SimpleImageLoadingListener() {
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


        if (!myReviewsListItem.getName().equalsIgnoreCase("null")) {

            String dataToShow = myReviewsListItem.getName();

            if (myReviewsListItem.getMerchantName() != null) {
                // dataToShow = dataToShow + " at " + myReviewsListItem.getMerchantName();
                // dataToShow = myReviewsListItem.getMerchantName()+"\n"+ dataToShow;
                viewHolder.itemName.setText("" + myReviewsListItem.getMerchantName());
                viewHolder.itemNameMainCourse.setText("" + dataToShow);
            }


        }

       /* if (myReviewsListItem.getMerchantName() != null) {

            String dataToShow = myReviewsListItem.getMerchantName();

            viewHolder.merchantName.setText("" + dataToShow);
        }*/


        return convertView;
    }


    class ViewHolder {
        ImageView offerImage;
        Button dislikeNormalImage, dislikeSelectedImage, loveItNormalImage, loveITSelectedImage, notBadNormalImage, notBadSelectedImage;
        TextView itemName, discount, merchantName, itemNameMainCourse;
    }

  /*  public class BestCandidateDisplay extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {

            String response = HttpRequest.post("https://beta135.hamarisuraksha.com/web/WebService/HsJobService.asmx/GetBestCandidates").send("Vendor_IEntity_Code=" + "&IJob_Req_ID=" + strings[0] + "&IJob_Requestor_ID=" + strings[1] + "&Mode=" + "TTL").body();
            return null;
        }
    }*/


    public boolean doAddReview(Activity context, String review, String productId, final String position) {

        final String key = "doAddReview";


        String jsonParam = Constants.BLANK;
        try {
            String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.Request.OFFER_ID, "" + productId);
            jsonObject.put(Constants.Request.MY_REVIEW, "" + review);
            jsonObject.put(Constants.Request.CUSTOMERID, "" + custID);
            jsonParam = jsonObject.toString();

        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        new CommonAsynctask(new ICommonCallback() {
            @Override
            public void onTaskDone(Bundle result, CommonAsyncTaskResult taskResult) {

                if (taskResult == CommonAsyncTaskResult.OK) {

                    JSONObject jsonObj = null;
                    JSONObject reader = null;
                    Bundle m_bundle = new Bundle();
                    try {
                        String jsonString = result.getString(key);
                        jsonObj = new JSONObject(
                                new JSONTokener(result.getString(key)));
                        if (jsonString != null) {
                            reader = new JSONObject(jsonString);
                            status = reader.getString(Constants.Request.STATUS);
                            if (status != null) {
                                if (status.equals(Constants.DEFAULT_VALUES.ONE)) {
                                    listData.remove(Integer.parseInt(position));

                                    utilObj.showCustomiOSMessageBox(mActivity, mContext.getString(R.string.my_review_success_msg), Constants.TWO_SECONDS);

                                    if (listData.size() == 0) {
                                        utilObj.generateEvent(mActivity, Constants.DEFAULT_VALUES.NO_REVIEW_AVAIL, null, null);
                                    }
                                    AppInstance.myReviewsCount --;
                                    AppPreference.setSetting(mActivity, "key_home_offers", "");
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(Constants.DEFAULT_VALUES.MY_REVIEW_COUNT_CHANGED, true);
                                    utilObj.generateEvent(mActivity, Constants.DEFAULT_VALUES.MY_REVIEW_UPDATED, bundle, Constants.DEFAULT_VALUES.MY_REVIEW_COUNT_CHANGED);
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, context, key, jsonParam, true, true, null, false, null).execute(Constants.WebServices.WS_SAVE_REVIEW_LIST);

        return false;
    }


}
