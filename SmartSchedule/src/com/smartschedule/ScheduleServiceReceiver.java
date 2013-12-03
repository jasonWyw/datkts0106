package com.smartschedule;

import java.util.ArrayList;
import java.util.Calendar;

import com.smartschedule.database.SmartSchedulerDatabase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

public class ScheduleServiceReceiver extends WakefulBroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent  alarmIntent;
    private ArrayList<PendingIntent> intentArray;
    private AlarmManager[] alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service =  new Intent(context, SchedulingService.class);

        startWakefulService(context, service);

    }

    public void setAlarm(Context context){
        alarmManager = new AlarmManager[24];
        intentArray =  new ArrayList<PendingIntent>();
        for(int i = 0; i <10; i ++){
            alarmManager[i] = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ScheduleServiceReceiver.class);
            // use Pending Intent for sent signal - send broadcast
            PendingIntent pi =  PendingIntent.getBroadcast(context, i, intent, 0);

//	        alarmManager[i].set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +  5000* i ,pi);

            alarmManager[i].setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +  5000* i, 60000, pi);

            intentArray.add(pi);
//	        // for each 15 min
//	        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//	                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//	                AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
        }
        // chua dung den
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context) {

        alarmManager = new AlarmManager[24];
        for(int i = 0; i <10; i ++){
            alarmManager[i] = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ScheduleServiceReceiver.class);

            // use;l Pending Intent for sent signal - send broadcast
            PendingIntent pi =  PendingIntent.getBroadcast(context, i, intent, 0);
            try{
                alarmManager[i].cancel(pi);
                Log.v("ScheduleServiceReceiver", "cancel alarmManager " + i );
            }
            catch(Exception e){
                Log.e("ScheduleServiceReceiver", "error alarmManager " + i + " "+ e.getMessage());

            }

        }
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void setSchedule(Context context, int id) {
        alarmMgr = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduleServiceReceiver.class);
        intent.putExtra(SmartSchedulerDatabase.COLUMN_EVENT_ID, id);

        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT);

        Calendar mcurrentTime = Calendar.getInstance();
        Calendar mcurrentTime1 = Calendar.getInstance();

//        mcurrentTime1.add(Calendar.HOUR, 1);
        mcurrentTime1.add(Calendar.MINUTE, 1);
        String dat = mcurrentTime.getTime().toString() + " " +
                mcurrentTime1.getTime().toString() + " " +
                (mcurrentTime1.getTimeInMillis() - mcurrentTime.getTimeInMillis());

        Toast.makeText(context, dat, Toast.LENGTH_LONG).show();
        // DateUtils.DAY_IN_MILLIS
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +  5000, 60000*5, pi);
    }

    public void cancelSchedule(Context context, int i) {
        // TODO Auto-generated method stub

    }

}