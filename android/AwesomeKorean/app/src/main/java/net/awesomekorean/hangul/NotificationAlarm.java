package net.awesomekorean.hangul;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

public class NotificationAlarm {

    PendingIntent currentScheduledAlarm = null;


    public void cancelNotification(Context context) {
        Log.d(MainActivity.LOG_PREFIX, "Attempt to cancel the existing notification");
        if(currentScheduledAlarm != null ) {
            Log.d(MainActivity.LOG_PREFIX, "Canceling the notification");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(currentScheduledAlarm);
        }
    }

    public void scheduleNotification(Context context, Notification notification, Integer hour, Integer minute) {

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 7);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        currentScheduledAlarm = PendingIntent.getBroadcast(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // This is for setting alarm to start after some delay
        //long futureInMillis = SystemClock.elapsedRealtime() + delay;
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        // Start an alarm at a specific time of day
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Log.d(MainActivity.LOG_PREFIX, "Scheduled Notification for: " + hour + ":" + minute);
        long everyDay = 24*60*60*1000;
        //long every2min = 2*60*1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), everyDay, currentScheduledAlarm);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), currentScheduledAlarm);

    }

    // notificationId is a unique int for each notification that you must define
    public void showNotification(Notification notification, Context context, Integer notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notification);
    }

    public Notification createNotification(Context context, String notificationTitle, String notificationContent) {

        // Create an intent that will open MainActivity when the notification is clicked
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Create the actual notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        return mBuilder.build();
    }



}
