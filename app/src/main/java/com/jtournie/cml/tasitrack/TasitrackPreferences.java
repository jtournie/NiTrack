package com.jtournie.cml.tasitrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jtournie on 22/12/15.
 */
public class TasitrackPreferences
{
    Context context;

    public int dosage;
    public int onetime_hour;
    public int onetime_minute;
    public int twice_hour_am;
    public int twice_minute_am;
    public int twice_hour_pm;
    public int twice_minute_pm;
    public boolean isFirstUse; //detect if the application is open for the first time to show the warning dialog

    public TasitrackPreferences()
    {
        this.dosage = 2;
        this.onetime_hour=21;
        this.onetime_minute=30;
        this.twice_hour_am = 7;
        this.twice_minute_am = 0;
        this.twice_hour_pm = 19;
        this.twice_minute_pm = 0;
    }

    public TasitrackPreferences( Context context)
    {
        this.context = context;
        this.updatePreferences();
    }

    public void updatePreferences()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);

        this.dosage = Integer.parseInt(sharedPreferences.getString("pref_key_dosage", "2"));
        this.onetime_hour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_onetime_hour", "21"));
        this.onetime_minute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_onetime_minute", "30"));
        this.twice_hour_am = Integer.parseInt(sharedPreferences.getString("pref_key_intake_am_hour", "7"));
        this.twice_minute_am = Integer.parseInt(sharedPreferences.getString("pref_key_intake_am_minute", "0"));
        this.twice_hour_pm = Integer.parseInt(sharedPreferences.getString("pref_key_intake_pm_hour", "19"));
        this.twice_minute_pm = Integer.parseInt(sharedPreferences.getString("pref_key_intake_pm_minute", "0"));
        this.isFirstUse = sharedPreferences.getBoolean("pref_key_first_use", true);
    }

    public void setIsFirstUse( boolean isFirstUse)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("pref_key_first_use", isFirstUse);
        editor.commit();
        updatePreferences();
    }
}
