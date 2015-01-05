package com.jtournie.cml.tasitrack;

import android.app.Activity;
import android.os.Bundle;


public class SettingsIntakeTimeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //This is activity is blank solely to be able to display the settings fragment

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsIntakeTimeFragment())
                .commit();

    }

}
