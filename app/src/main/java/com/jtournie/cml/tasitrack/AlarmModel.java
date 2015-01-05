package com.jtournie.cml.tasitrack;

import android.net.Uri;
import android.text.format.Time;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by jtournie on 29/11/14.
 */
public class AlarmModel {


    public long id = -1;
    public int timeHour;
    public int timeMinute;
    public Uri alarmTone;
    public String name;
    public boolean isEnabled;

    /**
     * From the original hour and minute of the alarm
     * shift the alarm by the amount required
     *
     * To make an alarm earlier than the original time,
     * the hour and/or minute must be negative
     *
     * @param iHour
     * @param iMinute
     */
    public void shiftAlarm( int iHour, int iMinute)
    {
        Calendar shiftedTime = Calendar.getInstance(Locale.getDefault());

        shiftedTime.setTimeZone(TimeZone.getDefault());

        long sTimeZoneDifferenceInMillisecond = TimeZone.getDefault().getOffset(System.currentTimeMillis());

        long originalTimeInMillisecond = (timeHour)*60*60*1000 + timeMinute*60*1000 - sTimeZoneDifferenceInMillisecond;
        shiftedTime.setTimeInMillis( originalTimeInMillisecond + iHour*60*60*1000 + iMinute*60*1000 );

        timeHour = shiftedTime.get(Calendar.HOUR_OF_DAY);
        timeMinute = shiftedTime.get(Calendar.MINUTE);


        return;

    }


}
