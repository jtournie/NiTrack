package com.jtournie.cml.tasitrack;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.jtournie.cml.tasitrack.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidgetClock extends AppWidgetProvider {

    public static String TAG = AppWidgetClock.class.getSimpleName();
    private static int iPreviousAngle = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onReceive (Context context, Intent intent)
    {

        //Log.i(TAG, "On received is called!" + intent.toString());

        if ( intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            //Log.i(TAG, "Building the param to call onUpdate"+intent.toString());
            Bundle extras = intent.getExtras();

            //get the app widget manager instance
            final AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);

            //int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);

            ComponentName thisWidget = new ComponentName(context,AppWidgetClock.class);
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

        }

        super.onReceive(context, intent);

    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_clock);

        //rotate the clock bg as needed
        rotateClockBg( context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Set the correct background image for the clock
     * based on the needed rotation angle. It loads
     * the pre-defined resource, instead of doing a rotation
     * of a reference image as the image changes size depending
     * on the rotation angle.
     *
     * The predefined images are sets for every 10 degrees.
     *
     * @param context
     * @param views
     */
    static void rotateClockBg( Context context, RemoteViews views) {
        int iAngle = (int)ClockBackground.getRotationAngle(context);

        if ( iAngle == iPreviousAngle)
        {
            Log.i(TAG, "Same angle - No need to update widget");
            return;
        }

        iPreviousAngle = iAngle;

        if ( iAngle < 0)
        {
            iAngle = 360 + iAngle;
        }

        int iAngleRounded = iAngle / 10;

        iAngleRounded = iAngleRounded * 10;

        iAngleRounded = iAngleRounded + 1;

        int iDiff = Math.abs(iAngle - iAngleRounded);
        if (   iDiff > 5)
        {
            iAngleRounded = iAngleRounded+10;
        }

        String resource = "clock_colorbg_widget_"+iAngleRounded ;
        int id = context.getResources().getIdentifier(resource, "drawable", context.getPackageName());

        if ( id != 0) {
            views.setImageViewResource(R.id.Widget_ClockBackground, id);
        } else
        {
            Log.e(TAG, "Resource does not exist "+resource);
        }

    }
}


