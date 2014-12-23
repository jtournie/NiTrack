package com.jtournie.cml.tasitrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by jtournie on 22/12/14.
 */
public class ClockBackground {
    /**
     * The goal of this class is simply to calculate
     * the angle needed for the back ground clock
     * to be aligned with the intake time
     * @return
     */
    static public float getRotationAngle(Context applicationContext)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        //reference intake time for the background image
        final float fRefIntakeTimeInMin = 7*60;

        float fIntakeTimeInMin = fRefIntakeTimeInMin;

        Calendar currentTime = Calendar.getInstance();

        if ( currentTime.get(Calendar.AM_PM) == Calendar.AM)
        {
            fIntakeTimeInMin = Float.parseFloat(sharedPreferences.getString("pref_key_intake_am_hour", "7"))*60+
                    Float.parseFloat(sharedPreferences.getString("pref_key_intake_am_minute", "7"));
        } else
        {
            fIntakeTimeInMin = Float.parseFloat(sharedPreferences.getString("pref_key_intake_pm_hour", "7"))*60+
                    Float.parseFloat(sharedPreferences.getString("pref_key_intake_pm_minute", "7"))
                    -12*60;
        }


        final float fDegreePerMinute = 360f/(12*60);

        float fAngle = -(fRefIntakeTimeInMin-fIntakeTimeInMin)*fDegreePerMinute;

        return fAngle ;
    }
}
