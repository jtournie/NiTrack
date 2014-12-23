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

    public User( Context applicationContext)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        intakeTimeAM = new NiTime();
        intakeTimeAM.Hour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_am_hour", "7"));
        intakeTimeAM.Minute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_am_minute", "0"));

        intakeTimePM = new NiTime();
        intakeTimePM.Hour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_pm_hour", "19"));
        intakeTimePM.Minute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_pm_minute", "0"));
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
}
