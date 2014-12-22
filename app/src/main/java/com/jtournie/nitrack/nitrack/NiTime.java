package com.jtournie.nitrack.nitrack;


import java.util.Calendar;

/**
 * Created by jtournie on 23/11/14.
 */
public class NiTime {
    public int Hour;
    public int Minute;


    public NiTime getRemainingTime( int hourFromIntake)
    {
        NiTime remainingTime = new NiTime();

        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);
        int currentTimeInMin = currentHour * 60 + currentMinute;
        int intakeTimeInMin = Hour*60+Minute;
        int requestedTimeInMin = intakeTimeInMin + hourFromIntake*60;

        int remainingTimeInMin = 0;
        if ( currentTimeInMin > requestedTimeInMin)
        {
            remainingTimeInMin = requestedTimeInMin - currentTimeInMin + 24*60;
        } else
        {
            remainingTimeInMin = requestedTimeInMin - currentTimeInMin;
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

    /**
     * Compare two NiTime objects
     * @param nt
     * @return 1 if this is earlier, -1 if this is after nt and 0 if equals
     */
    public int isSmaller(NiTime nt)
    {
        if (this.Hour < nt.Hour)
        {
            return 1;
        }

        if (this.Hour > nt.Hour)
        {
            return -1;
        }

        if (this.Minute > nt.Minute)
        {
            return 1;
        }

        if (this.Minute < nt.Minute)
        {
            return -1;
        }

        //otherwise they are equals
        return 0;
    }
}
