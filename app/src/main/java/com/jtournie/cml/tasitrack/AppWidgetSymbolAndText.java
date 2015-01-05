package com.jtournie.cml.tasitrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class AppWidgetSymbolAndText extends AppWidgetProvider {

    public static String TAG = AppWidgetSymbolAndText.class.getSimpleName();

    public static final String ACTION_WIDGET_CLICKED = "com.jtournie.cml.tasitrack.AppWidgetSymbolAndText.ACTION_WIDGET_CLICKED";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //Log.i(TAG, "On update is called!");


        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, DashboardActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widgetsymbolandtext);
            views.setOnClickPendingIntent(R.id.WidgetImageCurrentState, pendingIntent);
            views.setOnClickPendingIntent(R.id.WidgetTextRemainingTimeTitle, pendingIntent);

            //update the widget
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }



    }

    @Override
    public void onReceive (Context context, Intent intent)
    {

        //Log.i(TAG, "On received is called!"+intent.toString());

        if ( intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Log.i(TAG, "Building the param to call onUpdate"+intent.toString());
            Bundle extras = intent.getExtras();

            //get the app widget manager instance
            final AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);

            //int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);

            ComponentName thisWidget = new ComponentName(context,AppWidgetSymbolAndText.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);



            if ( appWidgetManager == null )
            {
                Log.e(TAG, "dont call update because appWidgetManager null");
                return;
            }

            if ( appWidgetIds == null)
            {
                Log.e(TAG, "dont call update because appWidgetIds null");
                return;
            }

            final int N = appWidgetIds.length;
            for (int i = 0; i < N; i++) {
                updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
            }

        } else if (intent.getAction().equals(ACTION_WIDGET_CLICKED))
        {
            Log.e(TAG, "the dashboard should be started!");
        }

        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(Context context) {
        setUpdateWidgetAlarm(context);
    }

    @Override
    public void onDisabled(Context context) {
        cancelUpdateWidgetAlarm(context);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widgetsymbolandtext);

        //fill in the view
        updateContent( context, views);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }


    public static void updateContent( Context applicationContext, RemoteViews views)
    {
        //get all the data needed for the user from preferences
        User currentUser = new User( applicationContext);

        //get the complete medicine schedule
        MedicineSchedule medicineSchedule = new MedicineSchedule( currentUser);

        //And fill in the cells
        //according to the state we are in, color the cell
        NiTime timeRemainingTime;
        int iCurrentState = medicineSchedule.getState();
        if (iCurrentState == medicineSchedule.STATE_FEASTING_PRE_MEDICINE)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeTakeMedicine();
            views.setTextViewText( R.id.WidgetTextRemainingTimeTitle, applicationContext.getString(R.string.dash_take_medicine_in)+"\n"+timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            views.setImageViewResource( R.id.WidgetImageCurrentState, R.drawable.nofoodallowed);
        } else if( iCurrentState == medicineSchedule.STATE_FEASTING_POST_MEDICINE)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeStartEating();
            views.setTextViewText(R.id.WidgetTextRemainingTimeTitle, applicationContext.getString(R.string.dash_can_eat_in)+"\n"+ timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            views.setImageViewResource(R.id.WidgetImageCurrentState, R.drawable.nofoodallowed);

        } else  if ( iCurrentState == medicineSchedule.STATE_EATING)
        {
            timeRemainingTime = medicineSchedule.getRemainingTimeStopEating();
            views.setTextViewText(R.id.WidgetTextRemainingTimeTitle, applicationContext.getString(R.string.dash_stop_eating_in)+"\n"+ timeRemainingTime.Hour + "h" + timeRemainingTime.Minute);
            views.setImageViewResource( R.id.WidgetImageCurrentState, R.drawable.foodallowed);
        } else
        {
            views.setTextViewText(R.id.WidgetTextRemainingTimeTitle, "Unknown state!");
            views.setImageViewResource( R.id.WidgetImageCurrentState, R.drawable.nofoodallowed);
        }


        return;
    }

    private static void cancelUpdateWidgetAlarm(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 20, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE );
        alarmManager.cancel(pendingIntent);
    }

    private static void setUpdateWidgetAlarm(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 20, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE );
        alarmManager.setRepeating( AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 10000, pendingIntent);
    }
}


