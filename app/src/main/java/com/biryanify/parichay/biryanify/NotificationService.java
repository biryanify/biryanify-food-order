package com.biryanify.parichay.biryanify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationService extends FirebaseMessagingService {

    private final String CHANNEL_ID = "firebase_notifications";
    private final int NOTIFICATION_ID = 6435;
    SharedPreferences sharedPreferences;
    public static final String datePref = "datePref";
    public static final String dbDateKey = "dbDateKey";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SimpleDateFormat originalFormat = new SimpleDateFormat("EEEE, MMMMM d'th' yyyy, h:mm:ss a");
        ParsePosition pos = new ParsePosition(0);
        Date addOrderDate = originalFormat.parse(remoteMessage.getData().get("serverTimeStamp"), pos);
//        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
//        String timeDate = timeFormat.format(addOrderDate);

        createNotificationChannel();

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("SENDER_KEY", "Notification Service");

        sharedPreferences = getSharedPreferences(datePref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(dbDateKey, remoteMessage.getData().get("orderDate"));
        editor.apply();

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_food_white_24dp)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setWhen(addOrderDate.getTime())
                .setShowWhen(true)
                .setSound(alarmSound)
                .setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "firebase Notifications";
            String description = "Include all the new order placed";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }


}
