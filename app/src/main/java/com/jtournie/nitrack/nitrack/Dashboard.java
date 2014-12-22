package com.jtournie.nitrack.nitrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

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

    public void updateContentWithNewCurrentIntakeTime()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //cancel all the alarms - The needed ones will be reset afterwards
        AlarmManagerHelper.cancelAlarms(dashboardActivity);

        //change the intake time to now
        Calendar currentTime = Calendar.getInstance();
        if ( currentTime.get(Calendar.HOUR_OF_DAY) < 12) //change the morning intake time
        {
            editor.putString("pref_key_intake_am_hour", String.valueOf(currentTime.get(Calendar.HOUR_OF_DAY)));
            editor.putString("pref_key_intake_am_minute", String.valueOf(currentTime.get(Calendar.MINUTE)));
            editor.commit();
        } else //change the pm intake time
        {
            editor.putString("pref_key_intake_pm_hour", String.valueOf(currentTime.get(Calendar.HOUR_OF_DAY)));
            editor.putString("pref_key_intake_pm_minute", String.valueOf(currentTime.get(Calendar.MINUTE)));
            editor.commit();
        }

        //and reset all the needed alarms
        AlarmManagerHelper.setAlarms(dashboardActivity);

        //redraw the dashboard
        updateContent();
    }

    public void updateContent()
    {
        //get all the data needed for the user from preferences
        User currentUser = new User( applicationContext);



        //get the complete medicine schedule
        MedicineSchedule medicineSchedule = new MedicineSchedule( currentUser);


        //now rotate the clock background to adapt to the intake time
        ImageView clockBackground = new ImageView(dashboardActivity);
        clockBackground = (ImageView)dashboardActivity.findViewById(R.id.ClockBackground);
        clockBackground.setRotation( ClockBackground.getRotationAngle(applicationContext));

        // now get the text cell references of the table
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

}
