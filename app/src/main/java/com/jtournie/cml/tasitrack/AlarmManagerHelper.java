package com.jtournie.cml.tasitrack;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

                //Log.i("CurrentTime", "CurrentTime " + calendar.get(calendar.YEAR) + " " + calendar.get(calendar.MONTH) + " " + calendar.get(calendar.DAY_OF_MONTH) + " " + calendar.get(calendar.HOUR_OF_DAY) + ":" + calendar.get(calendar.MINUTE));
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
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE );
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

                //disables all alarms even the ones that are
                //not active as it does not have any impact
                //and makes things easier for handling the
                //sharedpreferences
                //if (alarm.isEnabled)
                {
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


        //morning and evening intake time
        int iMorningIntakeHour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_am_hour", "7"));
        int iMorningIntakeMinute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_am_minute", "0"));
        int iEveningIntakeHour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_pm_hour", "19"));
        int iEveningIntakeMinute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_pm_minute", "0"));

        //the 4 alarms in the morning
        AlarmModel alarm0am = new AlarmModel();
        AlarmModel alarm1am = new AlarmModel();
        AlarmModel alarm2am = new AlarmModel();
        AlarmModel alarm3am = new AlarmModel();



        alarm0am.timeHour = iMorningIntakeHour;
        alarm0am.timeMinute = iMorningIntakeMinute;
        alarm0am.shiftAlarm( -2, -30);
        alarm0am.isEnabled = sharedPreferences.getBoolean("pref_key_alarm0am_enable", false);
        alarm0am.name = context.getString(R.string.alarm_text_am0);
        alarm0am.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm0am_tone", "N/A"));
        alarm0am.id = 0;

        alarm1am.timeHour = iMorningIntakeHour;
        alarm1am.timeMinute = iMorningIntakeMinute;
        alarm1am.shiftAlarm(-2, 0);
        alarm1am.isEnabled = sharedPreferences.getBoolean("pref_key_alarm1am_enable", false);
        alarm1am.name = context.getString(R.string.alarm_text_am1);
        alarm1am.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm1am_tone", "N/A"));
        alarm1am.id = 1;

        alarm2am.timeHour = iMorningIntakeHour;
        alarm2am.timeMinute = iMorningIntakeMinute;
        alarm2am.isEnabled = sharedPreferences.getBoolean("pref_key_alarm2am_enable", false);
        alarm2am.name = context.getString(R.string.alarm_text_am2);
        alarm2am.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm2am_tone", "N/A"));
        alarm2am.id = 2;

        alarm3am.timeHour = iMorningIntakeHour;
        alarm3am.timeMinute = iMorningIntakeMinute;
        alarm3am.shiftAlarm(1, 0);
        alarm3am.isEnabled = sharedPreferences.getBoolean("pref_key_alarm3am_enable", false);
        alarm3am.name = context.getString(R.string.alarm_text_am3);
        alarm3am.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm3am_tone", "N/A"));
        alarm3am.id = 3;

        //the 4 alarms in the afternoon
        AlarmModel alarm0pm = new AlarmModel();
        AlarmModel alarm1pm = new AlarmModel();
        AlarmModel alarm2pm = new AlarmModel();
        AlarmModel alarm3pm = new AlarmModel();

        alarm0pm.timeHour = iEveningIntakeHour;
        alarm0pm.timeMinute = iEveningIntakeMinute;
        alarm0pm.shiftAlarm( -2, -30);
        alarm0pm.isEnabled = sharedPreferences.getBoolean("pref_key_alarm0pm_enable", false);
        alarm0pm.name = context.getString(R.string.alarm_text_pm0);
        alarm0pm.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm0pm_tone", "N/A"));
        alarm0pm.id = 10;

        alarm1pm.timeHour = iEveningIntakeHour;
        alarm1pm.timeMinute = iEveningIntakeMinute;
        alarm1pm.shiftAlarm( -2, 0);
        alarm1pm.isEnabled = sharedPreferences.getBoolean("pref_key_alarm1pm_enable", false);
        alarm1pm.name = context.getString(R.string.alarm_text_pm1);
        alarm1pm.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm1pm_tone", "N/A"));
        alarm1pm.id = 11;

        alarm2pm.timeHour = iEveningIntakeHour;
        alarm2pm.timeMinute = iEveningIntakeMinute;
        alarm2pm.isEnabled = sharedPreferences.getBoolean("pref_key_alarm2pm_enable", false);
        alarm2pm.name = context.getString(R.string.alarm_text_pm2);
        alarm2pm.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm2pm_tone", "N/A"));
        alarm2pm.id = 12;

        alarm3pm.timeHour = iEveningIntakeHour;
        alarm3pm.timeMinute = iEveningIntakeMinute;
        alarm3pm.shiftAlarm( 1, 0);
        alarm3pm.isEnabled = sharedPreferences.getBoolean("pref_key_alarm3pm_enable", false);
        alarm3pm.name = context.getString(R.string.alarm_text_pm3);
        alarm3pm.alarmTone = Uri.parse(sharedPreferences.getString("pref_key_alarm3pm_tone", "N/A"));
        alarm3pm.id = 13;

        /**
         * Testing
         */
        /*alarm1pm.timeHour = 13;
        alarm1pm.timeMinute = 56;
        alarm2pm.timeHour = 13;
        alarm2pm.timeMinute = 57;
        alarm3pm.timeHour = 13;
        alarm3pm.timeMinute = 58;*/

        listAlarms.add(alarm0am);
        listAlarms.add(alarm1am);
        listAlarms.add(alarm2am);
        listAlarms.add(alarm3am);

        listAlarms.add(alarm0pm);
        listAlarms.add(alarm1pm);
        listAlarms.add(alarm2pm);
        listAlarms.add(alarm3pm);

        return listAlarms;
    }
}
