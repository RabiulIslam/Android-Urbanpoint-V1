package com.urbanpoint.UrbanPoint.views.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.urbanpoint.UrbanPoint.interfaces.ServiceRedirection;
import com.urbanpoint.UrbanPoint.managers.HomeManager;
import com.urbanpoint.UrbanPoint.managers.IntroActivityManager;
import com.urbanpoint.UrbanPoint.utils.AppPreference;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.NetworkUtils;
import com.urbanpoint.UrbanPoint.utils.Utility;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Anurag Sethi
 * The activity is used for handling the splash screen operations along with redirection to login screen
 * after the splash screen delay is exhausted
 */
public class SplashScreenActivity extends AppActivity implements ServiceRedirection {

    public static int notificationId;
    public static String notificationMsg;
    public static String notificationtitle;
    public static String notificationDate;
    private Context mContext;
    private Utility utilObj;
    private GoogleCloudMessaging mGoogleCloudMessaging;
    private String mRegID;
    private IntroActivityManager introActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        //initializing the data
        initData();

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String version = pInfo.versionName;
            Log.d("dsfdsafasfd", "showAppUpdateDialogue: " + version);
            AppPreference.setSetting(mContext, "key_app_version", version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String custID = AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, "");
        AppPreference.setSetting(mContext, "key_home_offers", "");
        if (custID.length() > 0) {
            if (NetworkUtils.isConnected(mContext)) {
                introActivityManager.doCheckSubscribe(AppPreference.getSetting(mContext, Constants.Request.CUSTOMER_ID, ""));
                introActivityManager.doFetchHomeOffers2();
            }
        }
        String projectToken = Constants.Request.MIXPANEL_TOKEN; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);


        gcmRegistration();
        //handler to close the splash activity after the set time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String setting = AppPreference.getSetting(getApplicationContext(), Constants.Request.CUSTOMER_ID, "");
                if (setting.equalsIgnoreCase("")) {
                    //Intent to call the Login Activity
                    Intent intentObj = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intentObj);
                } else {
                    Intent intentObj = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentObj);
                }

                //CHanges done by tejaswini bhandarkar to remove com.urbanpoint.UrbanPoint.views.activities.SplashScreenActivity tag name appearing in google analytics acct.
                //  MyApplication.getInstance().getDensityDpi(SplashScreenActivity.this);
                MyApplication.getInstance().trackScreenView(getString(R.string.splash_screen));
                SplashScreenActivity.this.finish();
                SplashScreenActivity.this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        }, Constants.SplashScreen.SPLASH_DELAY_LENGTH);
    }


    @Override
    protected void onResume() {
        super.onResume();
        notificationId = getIntent().getIntExtra("p_notification_id", -1);
        notificationMsg = getIntent().getStringExtra("p_notification_msg");
        notificationDate = getIntent().getStringExtra("p_notification_date");
        notificationtitle = getIntent().getStringExtra("p_notification_title");
        Log.d("sadsadsdasd", "onResume N_ID: " + notificationId);
        Log.d("sadsadsdasd", "onResume N_Msg: " + notificationMsg);
        Log.d("sadsadsdasd", "onResume N_Date: " + notificationDate);
    }


    /**
     * The method is used to initialize the objects
     *
     * @return none
     */
    public void initData() {
        this.mContext = this.getApplicationContext();
        AppInstance.getAppInstance();
        utilObj = new Utility(this);
        introActivityManager = new IntroActivityManager(mContext, this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //-------- PUSH NOTIFICATION CONFIGURATION : START --------------------
    private void gcmRegistration() {
        if (utilObj.checkPlayServices(this)) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(SplashScreenActivity.this);
            mRegID = getRegistrationId(this);
            if (mRegID.isEmpty()) {
                registerInBackground();
            }
        }
    }

    private String getRegistrationId(Context context) {

        String registrationId = AppPreference.getSetting(context, Constants.GCM.RECEIVED_GCM_ID, Constants.BLANK);
        if (registrationId.isEmpty()) {
            Log.i("Registration", "Registration not found.");
            return Constants.BLANK;
        }
        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                String msg = "";
                try {
                    if (mGoogleCloudMessaging == null) {
                        mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(SplashScreenActivity.this);
                    }
                    mRegID = mGoogleCloudMessaging.register(Constants.GCM.GCM_SENDER_ID);
                    Log.e("regId..........", mRegID);
                    msg = "Device registered, registration ID=" + mRegID;
                    Log.d("asdasdsa", "doInBackground: " + mRegID);
                    //				storeRegistrationId(context, regid);
                    AppPreference.setSetting(SplashScreenActivity.this, Constants.GCM.RECEIVED_GCM_ID, mRegID);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }


            protected void onPostExecute(String msg) {
                // mDisplay.append(msg + "\n");
                Log.i("MYGCM", msg);
            }

        }.execute();

        /**
         * Sends the registration ID to your server over HTTP, so it can use
         * GCM/HTTP or CCS to send messages to your app. Not needed for this
         * demo since the device sends upstream messages to a server that echoes
         * back the message using the 'from' address in the message.
         */

    }

    @Override
    public void onSuccessRedirection(int taskID) {

    }

    @Override
    public void onFailureRedirection(String errorMessage) {

    }
    //-------- PUSH NOTIFICATION CONFIGURATION : END --------------------
}
