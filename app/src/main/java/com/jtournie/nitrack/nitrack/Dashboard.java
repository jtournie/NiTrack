package com.jtournie.nitrack.nitrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jtournie on 22/11/14.
 *
 * Class managing the whole content of the dashboard
 *
 */
public class Dashboard {

    private DashboardActivity dashboardActivity;
    private Context applicationContext;

    public Dashboard( DashboardActivity dashboardActivity, Context applicationContext)
    {
        this.dashboardActivity = dashboardActivity;
        this.applicationContext = applicationContext;
    }

    public void updateContent()
    {
        //get all the data needed
        User currentUser = new User();

        currentUser.setIntakeTime( getIntakeTimeFromSharedPreferences());

        MedicineSchedule medicineSchedule = new MedicineSchedule( currentUser);

        //now get the text cell references of the table
        TextView textRemainingTimeTitle=new TextView(dashboardActivity);
        textRemainingTimeTitle=(TextView)dashboardActivity.findViewById(R.id.TextRemainingTimeTitle);

        TextView textRemainingTimeValue=new TextView(dashboardActivity);
        textRemainingTimeValue=(TextView)dashboardActivity.findViewById(R.id.TextRemainingTimeValue);


        ImageView  iconCurrentState = new ImageView(dashboardActivity);
        iconCurrentState = (ImageView)dashboardActivity.findViewById(R.id.ImageCurrentState);

        //And fill in the cells
        //according to the state we are in, color the cell
        NiTime timeRemainingTime;
        int iCurrentState = medicineSchedule.getState();
        if (iCurrentState == medicineSchedule.STATE_FEASTING_PRE_MEDICINE)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeTakeMedicine();
            textRemainingTimeTitle.setText("Take medicine in");
            textRemainingTimeValue.setText(timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            iconCurrentState.setImageResource(R.drawable.nofoodallowed);
        } else if( iCurrentState == medicineSchedule.STATE_FEASTING_POST_MEDICINE)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeStartEating();
            textRemainingTimeTitle.setText("Can eat in");
            textRemainingTimeValue.setText(timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            iconCurrentState.setImageResource(R.drawable.nofoodallowed);
        } else  if ( iCurrentState == medicineSchedule.STATE_EATING)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeStopEating();
            textRemainingTimeTitle.setText("Stop eating in");
            textRemainingTimeValue.setText(timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            iconCurrentState.setImageResource(R.drawable.foodallowed);
        } else
        {
            textRemainingTimeTitle.setText("Unknown state!");
            textRemainingTimeValue.setText("N/A");
            iconCurrentState.setImageResource(R.drawable.nofoodallowed);
        }


        return;
    }

    private NiTime getIntakeTimeFromSharedPreferences() {
        NiTime niTime = new NiTime();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        niTime.Hour = Integer.parseInt(sharedPreferences.getString("pref_key_intake_hour", "7"));
        niTime.Minute = Integer.parseInt(sharedPreferences.getString("pref_key_intake_minute", "7"));
        return niTime;
    }

}
