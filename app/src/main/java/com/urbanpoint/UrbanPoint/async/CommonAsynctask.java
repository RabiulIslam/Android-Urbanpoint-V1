package com.urbanpoint.UrbanPoint.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager.BadTokenException;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.interfaces.ICommonCallback;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;


public class CommonAsynctask extends AsyncTask<String, Void, String> {

    private static final String TAG = CommonAsynctask.class.getSimpleName();
    private String mProgressBarTextColor;
    private String mProgressBarText = "";
    private Utility utilObj;

    private Context mContext = null;
    private ProgressDialog mProgressDialog = null;
    private ICommonCallback mFragmentCallBack = null;
    private String mResponseKeyString = null;
    private String mJsonParamsString = null;
    private boolean mShowProgressBar = true;
    private boolean mIsRequestMethodPost = true;
    private boolean isNetWorkAvailable;
    // private AlertDialogManager alertDialogManager;
    private String mErrorMessage;
    private String mErrorTitle;
    private boolean mIsProgressBarTransparent;

    public CommonAsynctask(ICommonCallback commonCallback, Context context, String responseKey, String jsonParamsString) {
        this.mContext = context;
        this.mFragmentCallBack = commonCallback;
        this.mResponseKeyString = responseKey;
        this.mJsonParamsString = jsonParamsString;
        this.mShowProgressBar = true;
        //this.alertDialogManager = new AlertDialogManager();
        if (this.mIsRequestMethodPost) {
            this.mIsRequestMethodPost = true;
        } else {
            this.mIsRequestMethodPost = false;
        }
    }

    public CommonAsynctask(ICommonCallback commonCallback, Context context, String responseKey, String jsonParamsString, boolean showProgressBar, String progressBarText, boolean isProgressBarTransparent, String progressBarTextColor) {

        this(commonCallback, context, responseKey, jsonParamsString);

        mShowProgressBar = showProgressBar;

        mProgressBarText = progressBarText;

        mIsProgressBarTransparent = isProgressBarTransparent;

        mProgressBarTextColor = progressBarTextColor;

        utilObj = new Utility(context);
    }

    //-- param : Callback,Context,ResponseKey,JsonParamString,isShowProgressBar,isPostRequest
    public CommonAsynctask(ICommonCallback commonCallback, Context context, String responseKey, String jsonParamsString, boolean showProgressBar, boolean isRequestMethodPost, String progressBarText, boolean isProgressBarTransparent, String progressBarTextColor) {

        this(commonCallback, context, responseKey, jsonParamsString, showProgressBar, progressBarText, isProgressBarTransparent, progressBarTextColor);

        mIsRequestMethodPost = isRequestMethodPost;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        try {
            if (mShowProgressBar) {

                if (mProgressBarTextColor != null) {
                    utilObj.setTextColorOfMessage(mProgressBarTextColor);
                }

                if (mProgressBarText == null) {
                    utilObj.startiOSLoader(mContext, R.drawable.image_for_rotation, mContext.getString(R.string.please_wait), true);
                } else if (mIsProgressBarTransparent && mProgressBarText != null) {
                    utilObj.startiOSLoader(mContext, R.drawable.image_for_rotation, mProgressBarText, false);
                } else {
                    utilObj.startiOSLoader(mContext, R.drawable.image_for_rotation, mProgressBarText, true);
                }

            }
        } catch (BadTokenException exception) {
            //  ZipQApplicationContext.getInstance().printLogs(TAG, "Exception caught.");
            exception.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... webServiceURL) {
        String jsonString = null;


        if (NetworkUtils.isConnected(mContext)) {
            isNetWorkAvailable = true;
            ServiceHandler httpServiceHandler = new ServiceHandler();

            if (mIsRequestMethodPost) {

                jsonString = httpServiceHandler.makeServiceCall(webServiceURL[0], ServiceHandler.POST, mJsonParamsString, mContext);
            } else {
                jsonString = httpServiceHandler.makeServiceCall(webServiceURL[0], ServiceHandler.GET, mJsonParamsString, mContext);
            }
            // Making a request to url and getting response
        } else {
            isNetWorkAvailable = false;
        }
        return jsonString;
    }

    @Override
    protected void onPostExecute(String result) {

        CommonAsyncTaskResult resultType = CommonAsyncTaskResult.MISSING_NETWORK;

        try {

            super.onPostExecute(result);


            MyApplication.getInstance().printLogs("onPostExecute ", "Common Async onPostExecute : " + result);

            /*if (mProgressDialog != null && mProgressDialog.isShowing()) {

                mProgressDialog.dismiss();
                mProgressDialog = null;
            }*/
            if (mShowProgressBar) {
                utilObj.stopiOSLoader();
            }

            if (isNetWorkAvailable == false) {
                mErrorMessage = mContext.getResources().getString(R.string.response_empty_err_message);
                mErrorTitle = mContext.getResources().getString(R.string.app_name_with_space);
            } else {
                resultType = CommonAsyncTaskResult.OK;
            }
        } finally {

            Bundle bundle = new Bundle();
            bundle.putString(mResponseKeyString, result);

            // FIXME: Should not be run in the main thread!
            mFragmentCallBack.onTaskDone(bundle, resultType);
        }
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
