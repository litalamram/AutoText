package com.example.lital.autotext.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlarmSetter {

    static String MESSAGE_ID = "id";

    /**
     * Sets an alarm for sending the message
     * @param context app context
     * @param id message's id
     * @param date date and time the message should be sent
     */
    public static void setAlarm(Context context, long id, Calendar date) {

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmMgr == null) {
            return;
        }

        //create a pending intent
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(MESSAGE_ID, id);
        intent.setAction(String.valueOf(id)); //used to distinguish between the intents
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //set the alarm
        long alarmMillis = date.getTimeInMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmMillis, alarmIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, alarmMillis, alarmIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmMillis, alarmIntent);
        }
    }

    /**
     * Cancel an alarm corresponding to the specified message id
     * @param context
     * @param id message's id
     */
    public static void cancelAlarm(Context context, long id) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(String.valueOf(id));
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            return;
        }

        alarmManager.cancel(sender);
    }

}
