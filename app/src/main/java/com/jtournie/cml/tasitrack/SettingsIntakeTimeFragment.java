package com.jtournie.cml.tasitrack;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Created by jtournie on 29/11/14.
 */
public class SettingsIntakeTimeFragment extends PreferenceFragment {
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
       //Log.i(TAG, "Preference killed!!");

       //and reset all the needed alarms
       AlarmManagerHelper.setAlarms(getActivity());

       //send broadcast intent to the widgets so they are updated correctly
       Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
       getActivity().sendBroadcast( intent);

       super.onDestroy();
   }
}
