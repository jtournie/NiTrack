package com.jtournie.cml.tasitrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Created by jtournie on 29/11/14.
 */
public class SettingsNotificationsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private final static String TAG = SettingsNotificationsFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences_intake_time_global from an XML resource

        TasitrackPreferences pref = new TasitrackPreferences(getActivity());

        if (pref.dosage == 1)
        {
            addPreferencesFromResource(R.xml.preferences_notifications_once);
        } else
        {
            addPreferencesFromResource(R.xml.preferences_notifications_twice);
        }


    }

    @Override
    public void onDestroy()
    {
        String TAG = this.getClass().getSimpleName();
        //Log.i(TAG, "Preference killed!!");

        //and reset all the needed alarms
        //AlarmManagerHelper.setAlarms(getActivity());

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key)
    {
        Log.i(TAG, "Preferences changed");

        //cancel all the alarms - The needed ones will be reset once the screen is closed
        AlarmManagerHelper.cancelAlarms(getActivity());

        //and reset all the needed alarms
        AlarmManagerHelper.setAlarms(getActivity());
    }
}
