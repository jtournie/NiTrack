package com.jtournie.cml.tasitrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
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
        //get all the data needed for the user from preferences_intake_time_global
        User currentUser = new User( applicationContext);


        ImageView  iconStateEating = new ImageView(dashboardActivity);
        iconStateEating = (ImageView)dashboardActivity.findViewById(R.id.IconStateEating);

        ImageView  iconStateSnacking = new ImageView(dashboardActivity);
        iconStateSnacking = (ImageView)dashboardActivity.findViewById(R.id.IconStateSnacking);

        ImageView  iconStateNoEating = new ImageView(dashboardActivity);
        iconStateNoEating = (ImageView)dashboardActivity.findViewById(R.id.IconStateNoEating);


        adjustContentBasedOnDosage( iconStateEating, iconStateSnacking, iconStateNoEating);

        //get the complete medicine schedule
        MedicineSchedule medicineSchedule = new MedicineSchedule( currentUser, applicationContext);


        // now get the text cell references of the table
        TextView textRemainingTimeTitle=new TextView(dashboardActivity);
        textRemainingTimeTitle=(TextView)dashboardActivity.findViewById(R.id.TextRemainingTimeTitle);

        TextView textRemainingTimeValue=new TextView(dashboardActivity);
        textRemainingTimeValue=(TextView)dashboardActivity.findViewById(R.id.TextRemainingTimeValue);


        //And fill in the cells
        //according to the state we are in, color the cell
        NiTime timeRemainingTime;
        int iCurrentState = medicineSchedule.getState();
        if (iCurrentState == medicineSchedule.STATE_FEASTING_PRE_MEDICINE)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeTakeMedicine();
            textRemainingTimeTitle.setText(R.string.dash_take_medicine_in);
            textRemainingTimeValue.setText(" " + timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            iconStateEating.setImageResource(R.drawable.eating_disabled);
            iconStateSnacking.setImageResource(R.drawable.snacking_disabled);
            iconStateNoEating.setImageResource(R.drawable.noeating_enabled);
        } else if( iCurrentState == medicineSchedule.STATE_FEASTING_POST_MEDICINE)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeStartEating();
            textRemainingTimeTitle.setText(R.string.dash_can_eat_in);
            textRemainingTimeValue.setText(" "+timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            iconStateEating.setImageResource(R.drawable.eating_disabled);
            iconStateSnacking.setImageResource(R.drawable.snacking_disabled);
            iconStateNoEating.setImageResource(R.drawable.noeating_enabled);
        } else  if ( iCurrentState == medicineSchedule.STATE_EATING)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeStopEating();
            textRemainingTimeTitle.setText(R.string.dash_stop_eating_in);
            textRemainingTimeValue.setText(" "+timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            iconStateEating.setImageResource(R.drawable.eating_enabled);
            iconStateSnacking.setImageResource(R.drawable.snacking_disabled);
            iconStateNoEating.setImageResource(R.drawable.noeating_disabled);
        } else if ( iCurrentState == medicineSchedule.STATE_SNACK)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeStopEating();
            textRemainingTimeTitle.setText(R.string.dash_stop_eating_in);
            textRemainingTimeValue.setText(" "+timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            iconStateEating.setImageResource(R.drawable.eating_disabled);
            iconStateSnacking.setImageResource(R.drawable.snacking_enabled);
            iconStateNoEating.setImageResource(R.drawable.noeating_disabled);
        } else
        {
            textRemainingTimeTitle.setText("Unknown state!");
            textRemainingTimeValue.setText("N/A");
            iconStateEating.setImageResource(R.drawable.eating_disabled);
            iconStateSnacking.setImageResource(R.drawable.snacking_disabled);
            iconStateNoEating.setImageResource(R.drawable.noeating_disabled);
        }


        return;
    }

    private void adjustContentBasedOnDosage( ImageView iconStateEating, ImageView iconStateSnacking, ImageView iconStateNoEating)
    {
        TasitrackPreferences tasitrackPreferences = new TasitrackPreferences(applicationContext);

        if( tasitrackPreferences.dosage == 1)
        {
            ImageView clockBackground = new ImageView(dashboardActivity);
            clockBackground = (ImageView)dashboardActivity.findViewById(R.id.ClockBackground);
            clockBackground.setImageResource(R.drawable.clock_stopcml);
            iconStateEating.setVisibility(View.INVISIBLE);
            iconStateNoEating.setVisibility(View.INVISIBLE);
            iconStateSnacking.setVisibility(View.INVISIBLE);

        } else
        {
            //now rotate the clock background to adapt to the intake time
            ImageView clockBackground = new ImageView(dashboardActivity);
            clockBackground = (ImageView)dashboardActivity.findViewById(R.id.ClockBackground);
            clockBackground.setImageResource(R.drawable.clock_stopcml);
            clockBackground.setRotation( ClockBackground.getRotationAngle(applicationContext));
            iconStateEating.setVisibility(View.VISIBLE);
            iconStateNoEating.setVisibility(View.VISIBLE);
            iconStateSnacking.setVisibility(View.VISIBLE);
        }
    }

}
