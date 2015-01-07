package com.jtournie.cml.tasitrack;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Created by jtournie on 29/11/14.
 */
public class SettingsIntakeTimeFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private final static String TAG = SettingsIntakeTimeFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
   public void onDestroy()
   {
       String TAG = this.getClass().getSimpleName();
       //Log.i(TAG, "Preference killed!!");



       //send broadcast intent to the widgets so they are updated correctly
       Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
       getActivity().sendBroadcast( intent);

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
