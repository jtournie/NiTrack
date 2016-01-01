package com.jtournie.cml.tasitrack;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by jtournie on 29/11/14.
 */
public class AlarmActivity extends Activity {
    public final String TAG = this.getClass().getSimpleName();

    private PowerManager.WakeLock mWakeLock;
    private MediaPlayer mPlayer;

    private static final int WAKELOCK_TIMEOUT = 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup layout
        this.setContentView(R.layout.activity_alarm);

        String name = getIntent().getStringExtra(AlarmManagerHelper.NAME);
        int timeHour = getIntent().getIntExtra(AlarmManagerHelper.TIME_HOUR, 0);
        int timeMinute = getIntent().getIntExtra(AlarmManagerHelper.TIME_MINUTE, 0);
        String tone = getIntent().getStringExtra(AlarmManagerHelper.TONE);



        //set the correct text and time
        TextView tvName = (TextView) findViewById(R.id.alarm_screen_name);
        tvName.setText(name);

        TextView tvTime = (TextView) findViewById(R.id.alarm_screen_time);
        tvTime.setText(String.format("%02d : %02d", timeHour, timeMinute));

        //Set the correct icon for the alarm (eating, pill, snack, etc.)
        ImageView alarmIcon = (ImageView) findViewById(R.id.alarm_screen_icon);
        if ( name.equalsIgnoreCase( this.getString(R.string.alarm_text_am0))
                || name.equalsIgnoreCase(this.getString(R.string.alarm_text_pm0))  )
        {
            alarmIcon.setImageResource(R.drawable.snacking_enabled);
        } else if(name.equalsIgnoreCase( this.getString(R.string.alarm_text_am1))
                || name.equalsIgnoreCase(this.getString(R.string.alarm_text_pm1)))
        {
            alarmIcon.setImageResource(R.drawable.noeating_enabled);
        } else if (name.equalsIgnoreCase( this.getString(R.string.alarm_text_am2))
                || name.equalsIgnoreCase(this.getString(R.string.alarm_text_pm2)))
        {
            alarmIcon.setImageResource(R.drawable.pill2);
        } else
        {
            alarmIcon.setImageResource(R.drawable.eating_enabled);
        }


        //if click anywhere on the screen to stop the activity
        LinearLayout layout = (LinearLayout) findViewById(R.id.alarm_screen_main_layout);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (mPlayer != null) { //redundant with the try/catch but make it faster
                        if (mPlayer.isPlaying()) {
                            mPlayer.stop();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        //check if the activity is started correctly
        //as we observed that the tone is played
        //several times after the original ones
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        if (currentHour == timeHour && currentMinute == timeMinute) {
            //Play alarm tone
            mPlayer = new MediaPlayer();
            try {
                if (tone != null && !tone.equals("")) {
                    Uri toneUri = Uri.parse(tone);

                    if (toneUri != null) {
                        mPlayer.setDataSource(this, toneUri);
                        mPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                        mPlayer.setLooping(false);
                        mPlayer.prepare();
                        mPlayer.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();

        // Set the window to keep screen on
        /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);*/


    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
