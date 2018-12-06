package com.urbanpoint.UrbanPoint.views.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.analytic.AnalyticsTrackers;
import com.urbanpoint.UrbanPoint.dataobject.AppInstance;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.splunk.mint.Mint;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Anurag Sethi
 * The class will start once the application will start and will set the Splunk Key for handling
 * Bugsense issues
 */

public class MyApplication extends Application {

    private static MyApplication applicationContext;
    private DisplayMetrics displaymetrics;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/openSans_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        //  MultiDex.install(this);
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "MH74JX4SNGK89G427BGP");
        applicationContext = this;

        Mint.enableDebug();

        Mint.initAndStartSession(getApplicationContext(), Constants.BugSenseConstants.SPLUNK_API_KEY);

        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        //------------ TO set default language to En..no dependency on device language.
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        //-----------

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        }
//        catch (PackageManager.NameNotFoundException e) {
//
//        }
//        catch (NoSuchAlgorithmException e) {
//
//        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static MyApplication getInstance() {
        return applicationContext;
    }


    //This will print logs only in staging mode in with Log.e
    public void printLogs(String key, String value) {
        Log.e(key, "" + value);
    }

    public Typeface getFont() {
        Typeface typeface = Typeface.createFromAsset(applicationContext.getAssets(), "fonts/openSans_regular.ttf");
        return typeface;
    }

    public String getDensityDpi(Activity activityContext) {
        displaymetrics = new DisplayMetrics();
        activityContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int densityDpi = displaymetrics.densityDpi;
        String status = "";

        switch (densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                // LDPI
                status = "LDPI";
                break;

            case DisplayMetrics.DENSITY_MEDIUM:
                // MDPI
                status = "MDPI";
                break;

            case DisplayMetrics.DENSITY_TV:
            case DisplayMetrics.DENSITY_HIGH:
                // HDPI
                status = "HDPI";
                break;

            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_280:
                // XHDPI
                status = "XHDPI";
                break;

            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_360:
            case DisplayMetrics.DENSITY_400:
            case DisplayMetrics.DENSITY_420:
                // XXHDPI
                status = "XXHDPI";
                break;

            case DisplayMetrics.DENSITY_XXXHIGH:
            case DisplayMetrics.DENSITY_560:
                // XXXHDPI
                status = "XXXHDPI";
                break;
        }

        printLogs("getDensityDpi", "getDensityDpi " + status);
        return status;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppInstance.rModel_merchintList = null;
        AppInstance.offers = null;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }


    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }


    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /* public void trackException(Exception e) {
         if (e != null) {
             Tracker t = getGoogleAnalyticsTracker();

             t.send(new HitBuilders.ExceptionBuilder()
                     .setDescription(
                             new StandardExceptionParser(this, null)
                                     .getDescription(Thread.currentThread().getUncaughtExceptionHandler().toString(), e))
                     .setFatal(false)
                     .build()
             );
         }
     }*/
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action)
                .setLabel(label)
                .build());
    }
}
