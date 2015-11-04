package com.mixxitevaluatecall.Alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by amin on 29/09/15.
 */
public class DispoAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        if(intent.getBooleanExtra("Dispo",true))
        Toast.makeText(context, "Setting dispo", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Setting indispo", Toast.LENGTH_SHORT).show();
    }
}
