package com.pivotalsoft.kammasevasamithi.Services;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pivotalsoft.kammasevasamithi.HomeActivity;
import com.pivotalsoft.kammasevasamithi.R;


/**
 * Created by Gangadhar on 12/2/2017.
 */

public class FireMsgService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Msg", "Message received ["+remoteMessage.getNotification()+"]");

       /* // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 for private mode
        String fullname = sp.getString("fullname", "");*/

        // Create Notification
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_aboutus)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSound(defaultSoundUri)
                .setContentTitle("Kamma Seva Samithi")
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, notificationBuilder.build());
    }
}