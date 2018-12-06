package com.urbanpoint.UrbanPoint.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.interfaces.CallBack;
import com.urbanpoint.UrbanPoint.views.activities.MyApplication;

import sdei.support.lib.communication.HttpUtility;

/**
 * Created by Anurag Sethi on 08-04-2015.
 * The class will handle the asynctask for the webservice call
 */
public class GetWebServiceData extends AsyncTask<String, Void, String> {

    boolean showLoader = false;
    String result = "";
    String url;
    String postData;
    Context context;
    CallBack callbackObj;
    HttpUtility httpUtilityObj;
    int tasksID;

    /**
     * Constructor
     *
     * @param contextObj The Context from where the method is called
     * @param Url        Web Service URL to be called
     * @param listnerObj object of interface CallBack
     * @param jsonString the JSON object of posting data to the web service
     * @param tasksID    the ID to differential multiple webservice calls
     * @return none
     */

    public GetWebServiceData(Context contextObj, String Url, CallBack listnerObj, String jsonString, int tasksID) {
        this.context = contextObj;
        this.postData = jsonString;
        this.url = Url;
        this.callbackObj = listnerObj;
        this.tasksID = tasksID;
        this.httpUtilityObj = new HttpUtility(contextObj);
    }

    /**
     * The method executes before the background processing starts and
     * shows the loader in case its visibility is set as true
     *
     * @return none
     */
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            result = httpUtilityObj.getPostResults(this.url, this.postData, "");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exception from web", e.toString());
            result = "";
        }
        return result;
    }

    /**
     * The method executes after the background processing ends
     *
     * @param result the parsed string output from the provided URL
     * @return none
     */

    @Override
    protected void onPostExecute(String result) {
        MyApplication.getInstance().printLogs("RESPONSE :onPostExecute", "result : " + result);
        MyApplication.getInstance().printLogs("RESPONSE :onPostExecute", "tasksID :" + tasksID);
        String temp = "" + result;
        String responseEmptyErrorMessage = null;

        if ("null".equalsIgnoreCase(temp)) {
            result = null;
            responseEmptyErrorMessage = context.getResources().getString(R.string.response_empty_err_message);
        }
        if (result != null) {
            if (result.trim().length() == 0) {
                result = null;
                responseEmptyErrorMessage = context.getResources().getString(R.string.response_empty_err_message);
            }
        }
        callbackObj.onResult(result, tasksID, responseEmptyErrorMessage);
    }
}
