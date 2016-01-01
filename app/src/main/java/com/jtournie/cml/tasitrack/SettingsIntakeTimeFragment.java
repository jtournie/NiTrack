package com.jtournie.cml.tasitrack;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jtournie on 29/11/14.
 */
public class SettingsIntakeTimeFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private final static String TAG = SettingsIntakeTimeFragment.class.getName();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //depending on the posology set the correct preferences
        setPreferencesTimeBasedOnDosage();
    }

    @Override
   public void onDestroy()
   {
       String TAG = this.getClass().getSimpleName();
       //Log.i(TAG, "Preference killed!!");



       //send broadcast intent to the widgets so they are updated correctly
       Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
       getActivity().sendBroadcast(intent);

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

        if ( key.compareTo("pref_key_dosage") == 0)
        {
            Toast.makeText(getActivity(), R.string.dosage_reminder, Toast.LENGTH_LONG).show();
        }


        //cancel all the alarms - The needed ones will be reset once the screen is closed
        AlarmManagerHelper.cancelAlarms(getActivity());

        //and reset all the needed alarms
        AlarmManagerHelper.setAlarms(getActivity());

        updatePreferencesTimeBasedOnDosage();

    }

    void updatePreferencesTimeBasedOnDosage()
    {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removeAll();
        setPreferencesTimeBasedOnDosage();
    }

    void setPreferencesTimeBasedOnDosage()
    {
        //Now depending on the dosage (1 or 2) we
        //hide/show the corresponding intake time settings
        TasitrackPreferences pref = new TasitrackPreferences(getActivity());
        Log.i(TAG, "Adjusting the preferences_intake_time_global widgets for dosage " + pref.dosage);

        addPreferencesFromResource(R.xml.preferences_intake_time_global);

        if ( pref.dosage == 1)
        {
            addPreferencesFromResource(R.xml.preferences_intake_time_once);
        } else
        {
            addPreferencesFromResource(R.xml.preferences_intake_time_twice);
        }
    }
}
