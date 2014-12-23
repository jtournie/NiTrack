package com.jtournie.cml.tasitrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        AlarmManagerHelper.setAlarms(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        Dashboard dashboard = new Dashboard(this, getApplicationContext());

        dashboard.updateContent();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // Display the fragment as the main content.
            Intent settingsActivityIntent = new Intent(getBaseContext(), SettingsActivity.class);
            settingsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(settingsActivityIntent);
            return true;
        } else if( id == R.id.action_change_intake_time)
        {
            Dashboard dashboard = new Dashboard(this, getApplicationContext());

            dashboard.updateContentWithNewCurrentIntakeTime();

            return true;
        } else if( id == R.id.action_about)
        {
            Intent intent = new Intent(getBaseContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}