package com.biryanify.parichay.biryanify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationService extends FirebaseMessagingService {

    private final String CHANNEL_ID = "firebase_notifications";
    private final int NOTIFICATION_ID = 6435;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("NotificationService", remoteMessage.getData().get("serverTimeStamp"));
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEEE, MMMMM d'th' yyyy, h:mm:ss a");
        ParsePosition pos = new ParsePosition(0);
        Date addOrderDate = originalFormat.parse(remoteMessage.getData().get("serverTimeStamp"), pos);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        String timeDate = timeFormat.format(addOrderDate);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
//        String dateDate = dateFormat.format(addOrderDate);

        String orderDate = remoteMessage.getData().get("orderDate");

        createNotificationChannel();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("date", orderDate);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_food_black_24dp);
        builder.setContentTitle(remoteMessage.getData().get("title"));
        builder.setContentText(remoteMessage.getData().get("body"));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);
        builder.setWhen(addOrderDate.getTime());
        builder.setShowWhen(true);
        builder.setContentIntent(pendingIntent);

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
