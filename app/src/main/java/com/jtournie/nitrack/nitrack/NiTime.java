package com.jtournie.nitrack.nitrack;


import java.util.Calendar;

/**
 * Created by jtournie on 23/11/14.
 */
public class NiTime {
    public int Hour;
    public int Minute;
    public boolean IsTimeBefore;


    public NiTime getRemainingTime( int hourFromIntake)
    {
        NiTime remainingTime = new NiTime();

        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR);
        int currentMinute = currentTime.get(Calendar.MINUTE);
        int currentTimeInMin = currentHour * 60 + currentMinute;
        int intakeTimeInMin = Hour*60+Minute;
        int requestedTimeInMin = intakeTimeInMin + hourFromIntake*60;

        int remainingTimeInMin = 0;
        if ( currentTimeInMin > requestedTimeInMin)
        {
            remainingTimeInMin = 12*60 - currentTimeInMin + requestedTimeInMin;
            remainingTime.IsTimeBefore = false;
        } else
        {
            remainingTimeInMin = requestedTimeInMin - currentTimeInMin;
            remainingTime.IsTimeBefore = true;
        }


        remainingTime.Hour = remainingTimeInMin/60;
        remainingTime.Minute = remainingTimeInMin%60;


        remainingTime.formatTimeToAbsolute();

        return remainingTime;
    }

    public void formatTimeToAbsolute()
    {
        if ( this.Hour < 0)
        {
            this.Hour = -this.Hour;
        }

        if ( this.Minute < 0)
        {
            this.Minute = -this.Minute;
        }
    }
}
