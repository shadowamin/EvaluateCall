package com.mixxitevaluatecall.Alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by amin on 29/09/15.
 */
public class AlarmsManager {
    private Context context;

    private static AlarmsManager m_Instance = null;

    public static AlarmsManager getInstance(Context context) {
        if (m_Instance == null) {
            m_Instance = new AlarmsManager(context);
        }
        return m_Instance;
    }
    public AlarmsManager(Context context) {
        this.context = context;

    }
    public void cancelAlarms() {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < 4; i++) {
            Intent intent = new Intent(context, DispoAlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, 0);
            manager.cancel(pendingIntent);
        }


    }


    public void startAlarms(String[] hours) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        for (int i = 0; i < hours.length; i++) {
            Intent intent=new Intent(context, DispoAlarmReceiver.class);
            if(i%2==0)
                intent.putExtra("Dispo",true);
            else
                intent.putExtra("Dispo",false);
               // intent = new Intent(context, IndispoAlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String[] time = hours[i].split(":");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            if(System.currentTimeMillis()>calendar.getTimeInMillis()) {
             long diff= AlarmManager.INTERVAL_DAY-(System.currentTimeMillis()-calendar.getTimeInMillis());
                manager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+diff,
                        AlarmManager.INTERVAL_DAY, pendingIntent);
            }else
                manager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
