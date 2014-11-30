package com.jtournie.nitrack.nitrack;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by jtournie on 29/11/14.
 *
 * AlarmManagerHelper class help to set and cancel alarms
 */
public class AlarmManagerHelper extends BroadcastReceiver {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME_HOUR = "timeHour";
    public static final String TIME_MINUTE = "timeMinute";
    public static final String TONE = "alarmTone";

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarms(context);
    }

    public static void setAlarms(Context context) {
        cancelAlarms(context);

        List<AlarmModel> listAlarms = getAlarms(context);

        for (AlarmModel alarm : listAlarms) {
            if (alarm.isEnabled) {

                PendingIntent pIntent = createPendingIntent(context, alarm);

                Calendar calendar = Calendar.getInstance();

                //Log.i("CurrentTime", "CurrentTime "+calendar.get(calendar.YEAR)+" "+calendar.get(calendar.MONTH)+" "+calendar.get(calendar.DAY_OF_MONTH)+" "+calendar.get(calendar.HOUR_OF_DAY)+":"+calendar.get(calendar.MINUTE));
                calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
                calendar.set(Calendar.MINUTE, alarm.timeMinute);
                calendar.set(Calendar.SECOND, 0);

                //Log.i("CurrentTime", "Adjusted time with hour "+calendar.get(calendar.YEAR)+" "+calendar.get(calendar.MONTH)+" "+calendar.get(calendar.DAY_OF_MONTH)+" "+calendar.get(calendar.HOUR_OF_DAY)+":"+calendar.get(calendar.MINUTE));

                Calendar currentTime = Calendar.getInstance();

                if ( currentTime.after(calendar) == true || currentTime.compareTo(calendar)==0)
                {
                    //set it for tomorrow then
                    long currentTimeInMilli = calendar.getTimeInMillis();
                    calendar.setTimeInMillis( currentTimeInMilli + 24*60*60*1000);
                }

                //Log.i("CurrentTime", "Adjusted time with day "+calendar.get(calendar.YEAR)+" "+calendar.get(calendar.MONTH)+" "+calendar.get(calendar.DAY_OF_MONTH)+" "+calendar.get(calendar.HOUR_OF_DAY)+":"+calendar.get(calendar.MINUTE));
                setAlarm(context, calendar, pIntent);


            }
        }
    }

    @SuppressLint("NewApi")
    private static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }
    }

    public static void cancelAlarms(Context context) {
        List<AlarmModel> listAlarms = getAlarms(context);

        if (listAlarms != null) {
            for (AlarmModel alarm : listAlarms) {
                if (alarm.isEnabled) {
                    PendingIntent pIntent = createPendingIntent(context, alarm);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pIntent);
                }
            }
        }
    }

    private static PendingIntent createPendingIntent(Context context, AlarmModel model) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(ID, model.id);
        intent.putExtra(NAME, model.name);
        intent.putExtra(TIME_HOUR, model.timeHour);
        intent.putExtra(TIME_MINUTE, model.timeMinute);
        intent.putExtra(TONE, model.alarmTone.toString());

        return PendingIntent.getService(context, (int) model.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Temporary function to get the alarm definition
     * before i implement the persistency in sqlite
     * @return
     *  a list of alarm definition
     */
    private static List<AlarmModel> getAlarms(Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


        List<AlarmModel> listAlarms = new ArrayList<AlarmModel>();

        //the 3 alarms in the morning
        AlarmModel alarm1am = new AlarmModel();
        AlarmModel alarm2am = new AlarmModel();
        AlarmModel alarm3am = new AlarmModel();

        alarm1am.timeHour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_hour", "7"))-2;
        alarm1am.timeMinute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_minute", "7"));
        alarm1am.isEnabled = sharedPreferences.getBoolean("pref_key_alarm1_enable", false);
        alarm1am.name = "Stop eating (Morning)";
        alarm1am.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm1_tone", "N/A"));
        alarm1am.id = 1;

        alarm2am.timeHour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_hour", "7"));
        alarm2am.timeMinute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_minute", "7"));
        alarm2am.isEnabled = sharedPreferences.getBoolean("pref_key_alarm2_enable", false);
        alarm2am.name = "Take medicine (Morning)";
        alarm2am.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm2_tone", "N/A"));
        alarm2am.id = 2;

        alarm3am.timeHour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_hour", "7"))+1;
        alarm3am.timeMinute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_minute", "7"));
        alarm3am.isEnabled = sharedPreferences.getBoolean("pref_key_alarm3_enable", false);
        alarm3am.name = "Eating allowed (Morning)";
        alarm3am.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm3_tone", "N/A"));
        alarm3am.id = 3;

        //the 3 alarms in the afternoon
        AlarmModel alarm1pm = new AlarmModel();
        AlarmModel alarm2pm = new AlarmModel();
        AlarmModel alarm3pm = new AlarmModel();

        alarm1pm.timeHour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_hour", "7"))+12-2;
        alarm1pm.timeMinute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_minute", "7"));
        alarm1pm.isEnabled = sharedPreferences.getBoolean("pref_key_alarm1_enable", false);
        alarm1pm.name = "Stop eating (Evening)";
        alarm1pm.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm1_tone", "N/A"));
        alarm1pm.id = 11;

        alarm2pm.timeHour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_hour", "7"))+12;
        alarm2pm.timeMinute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_minute", "7"));
        alarm2pm.isEnabled = sharedPreferences.getBoolean("pref_key_alarm2_enable", false);
        alarm2pm.name = "Take medicine (Evening)";
        alarm2pm.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm2_tone", "N/A"));
        alarm2pm.id = 12;

        alarm3pm.timeHour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_hour", "7"))+12+1;
        alarm3pm.timeMinute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_minute", "7"));
        alarm3pm.isEnabled = sharedPreferences.getBoolean("pref_key_alarm3_enable", false);
        alarm3pm.name = "Eating allowed (Evening)";
        alarm3pm.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm3_tone", "N/A"));
        alarm3pm.id = 13;



        listAlarms.add(alarm1am);
        listAlarms.add(alarm2am);
        listAlarms.add(alarm3am);

        listAlarms.add(alarm1pm);
        listAlarms.add(alarm2pm);
        listAlarms.add(alarm3pm);

        return listAlarms;
    }
}
