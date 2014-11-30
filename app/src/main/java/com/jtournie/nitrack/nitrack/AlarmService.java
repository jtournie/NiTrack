package com.jtournie.nitrack.nitrack;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import android.util.Log;

/**
 * Created by jtournie on 29/11/14.
 */
public class AlarmService extends Service {

    public static String TAG = AlarmService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent alarmIntent = new Intent(getBaseContext(), AlarmActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtras(intent);
        getApplication().startActivity(alarmIntent);

        AlarmManagerHelper.setAlarms(this);

        return super.onStartCommand(intent, flags, startId);
    }
}
