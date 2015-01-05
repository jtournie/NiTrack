package com.jtournie.cml.tasitrack;

import android.app.Activity;
import android.os.Bundle;


public class SettingsNotificationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //This is activity is blank solely to be able to display the settings fragment

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsNotificationsFragment())
                .commit();

    }

}
