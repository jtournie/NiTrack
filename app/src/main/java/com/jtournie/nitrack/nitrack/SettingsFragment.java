package com.jtournie.nitrack.nitrack;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Created by jtournie on 29/11/14.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        //cancel all the alarms - The needed ones will be reset once the screen is closed
        AlarmManagerHelper.cancelAlarms(getActivity());
    }

    @Override
   public void onDestroy()
   {
       String TAG = this.getClass().getSimpleName();
       Log.i(TAG, "Preference killed!!");

       //and reset all the needed alarms
       AlarmManagerHelper.setAlarms(getActivity());

       super.onDestroy();
   }
}
