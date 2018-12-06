package com.urbanpoint.UrbanPoint.services;

/**
 * Created by Anurag Sethi on 25-06-2015.
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.utils.Constants;
import com.urbanpoint.UrbanPoint.views.activities.SplashScreenActivity;

import java.util.Random;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private int notificationId = Constants.DEFAULT_VALUES.UNDEFINED_PUSH_NOTIFICATION;
    private String message, date, title;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */

    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        message = data.getString("message");
        date = data.getString("date");
        notificationId = Integer.parseInt(data.getString("id"));
        title = data.getString("title");
        SplashScreenActivity.notificationId = notificationId;
        SplashScreenActivity.notificationMsg = message;
        SplashScreenActivity.notificationtitle = title;
        SplashScreenActivity.notificationDate = date;


        Log.d(TAG, "id: " + notificationId);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
//        sendNotification(message,notificationId);
        sendNotification(notificationId, title, message, date);

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
//    private void sendNotification(String message, int _notificationId) {
//        Intent intent = new Intent(this, SplashScreenActivity.class);
////        intent.putExtra("notification_id",1);
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//        //      PendingIntent.FLAG_ONE_SHOT);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        //Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                //.setLargeIcon(largeIcon)
//
//                .setSmallIcon(R.mipmap.ic_launcher)
//                //.set
//                .setContentTitle(getString(R.string.app_name_with_space))
//                .setColor(0XD01A60)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        //notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//        int id = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
//        notificationManager.notify(id, notificationBuilder.build());
//
//    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param _title   FCM message title received.
     * @param _message FCM message received.
     */
    private void sendNotification(int _ntfcType, String _title, String _message, String _date) {
        Random random = new Random();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, SplashScreenActivity.class);
        intent.putExtra("p_notification_id", _ntfcType);
        intent.putExtra("p_notification_date", _date);
        intent.putExtra("p_notification_title", _title);
        intent.putExtra("p_notification_msg", _message);
        PendingIntent notifyPIntent = PendingIntent.getActivity(this, random.nextInt(),
                intent, PendingIntent.FLAG_ONE_SHOT);//


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(_title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(_message))
                .setContentText(_message)
                .setSound(defaultSoundUri)
                .setContentIntent(notifyPIntent)
                .setAutoCancel(true);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default_channel", "Urban Point Alerts", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(_title);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(random.nextInt(),
                notificationBuilder.build());
    }

}
