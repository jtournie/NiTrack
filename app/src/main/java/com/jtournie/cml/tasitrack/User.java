package com.jtournie.cml.tasitrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jtournie on 22/11/14.
 *
 */
public class User {
    private String userName;
    private String fullName;
    private NiTime intakeTimeAM; //0-11
    private NiTime intakeTimePM; //12-23
    private NiTime intakeTimeOnce; //0-23

    public User( Context applicationContext)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        TasitrackPreferences tasitrackPreferences = new TasitrackPreferences( applicationContext);

        intakeTimeAM = new NiTime();
        intakeTimeAM.Hour = tasitrackPreferences.twice_hour_am;
        intakeTimeAM.Minute = tasitrackPreferences.twice_minute_am;

        intakeTimePM = new NiTime();
        intakeTimePM.Hour = tasitrackPreferences.twice_hour_pm;
        intakeTimePM.Minute = tasitrackPreferences.twice_minute_pm;

        intakeTimeOnce = new NiTime();
        intakeTimeOnce.Hour = tasitrackPreferences.onetime_hour;
        intakeTimeOnce.Minute = tasitrackPreferences.onetime_minute;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public NiTime getIntakeTimeAM() {
        return intakeTimeAM;
    }

    public void setIntakeTimeAM(NiTime intakeHour) {
        this.intakeTimeAM = intakeHour;
    }

    public NiTime getIntakeTimePM() {
        return intakeTimePM;
    }

    public void setIntakeTimePM(NiTime intakeTimePM) {
        this.intakeTimePM = intakeTimePM;
    }

    public NiTime getIntakeTimeOnce() {return  intakeTimeOnce;}

    public void setIntakeTimeOnce(NiTime intakeTimeOnce) { this.intakeTimeOnce = intakeTimeOnce; }
}
