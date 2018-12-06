package com.urbanpoint.UrbanPoint.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Pravinsingh Waghela on 15/10/15.
 * Checks Network Connectivity and Speed.
 */
public class NetworkUtils {

    /**
     * Get the network info
     * @param context of the class or Activity
     * @return boolean value.
     */
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context of the class or Activity
     * @return boolean value.
     */
    public static boolean isConnected(Context context){
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && (info.isConnected()||info.isConnectedOrConnecting()));
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context of the class or Activity
     * @return boolean value.
     */
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context of the class or Activity
     * @return boolean value.
     */
    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if mobile network is Roaming.
     * @param context of the class or Activity
     * @return boolean value.
     */
    public static boolean isConnectedMobileRoaming(Context context){
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (NetworkUtils.isConnectedMobile(context) && info.isRoaming());
    }

    /**
     * Check if there is fast connectivity
     * @param context of the class or Activity
     * @return
     */
    public static boolean isConnectedFast(Context context){
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() && NetworkUtils.isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * Reference to the below method could be found at,
     * http://developer.android.com/reference/android/telephony/TelephonyManager.html
     * And for Net speed http://3gstore.com/page/78_what_is_evdo_mobile_broadband.html
     * @param type int
     * @param subType int
     * @return boolean value
     */
    public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps also known as 3G.
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps also known as 4G.
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }

}
