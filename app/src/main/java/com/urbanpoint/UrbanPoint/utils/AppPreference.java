package com.urbanpoint.UrbanPoint.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.dataobject.AppInstance;

public class AppPreference {

    // Setting Shared Preference
    public static void setSetting(Context ctx, String key, String value) {
        SharedPreferences settings = ctx.getSharedPreferences(
                ctx.getApplicationInfo().packageName + "_preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        // Commit the edits!
        editor.commit();
    }

    public static void setSetting(Context ctx, String key, long value) {
        SharedPreferences settings = ctx.getSharedPreferences(
                ctx.getApplicationInfo().packageName + "_preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        // Commit the edits!
        editor.commit();
    }
    public static void setSetting(Context ctx, String key, boolean value) {
        SharedPreferences settings = ctx.getSharedPreferences(
                ctx.getApplicationInfo().packageName + "_preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        // Commit the edits!
        editor.commit();
    }

    // Getting Shared Preference
    public static String getSetting(Context ctx, String key, String def) {

        SharedPreferences settings = ctx.getSharedPreferences(
                ctx.getApplicationInfo().packageName + "_preferences", 0);
        Log.d("OldDataIs", "SharefPref is: " +ctx.getApplicationInfo().packageName + "_preferences");
        return settings.getString(key, def);
    }

    public static long getSettingResturnsLong(Context ctx, String key, long def) {

        SharedPreferences settings = ctx.getSharedPreferences(
                ctx.getApplicationInfo().packageName + "_preferences", 0);
        return settings.getLong(key, def);
    }

    public static boolean getSettingResturnsBoolean(Context ctx, String key, boolean def) {

        SharedPreferences settings = ctx.getSharedPreferences(
                ctx.getApplicationInfo().packageName + "_preferences", 0);
        return settings.getBoolean(key, def);
    }

    public static void clearSharedPreference(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(
                ctx.getApplicationInfo().packageName + "_preferences", 0);
        settings.edit().clear().commit();
    }

    public static void toastUtil(Context ctx, String str) {
        Toast toast = Toast.makeText(ctx, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
